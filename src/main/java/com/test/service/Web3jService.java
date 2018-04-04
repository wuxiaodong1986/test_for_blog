package com.test.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.*;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.Request;
import org.web3j.protocol.core.Response;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.core.methods.response.EthTransaction;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Convert;
import org.web3j.utils.Numeric;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.ExecutionException;

import static org.web3j.abi.Utils.convert;

/**
 * Created by 吴晓冬 on 2018/4/3.
 */
@Service
public class Web3jService
{
    private String serviceUrl = "https://mainnet.infura.io/your-token";

    private BigInteger gasPriceMax = new BigInteger("100000000000");

    private BigInteger gasPriceMin = new BigInteger("20000000000");

    private static ThreadLocal<Map<String, BigInteger>> nonceHolder = new ThreadLocal<>();

    private static ThreadLocal<BigInteger> priceHolder = new ThreadLocal<>();

    private final static Logger logger = LoggerFactory.getLogger(Web3jService.class);

    /**
     * 创建私钥/地址对
     */
    public Map<String, String> createWallet() throws Exception
    {
        ECKeyPair ecKeyPair = Keys.createEcKeyPair();
        String publicKey = Numeric.toHexStringNoPrefix(ecKeyPair.getPublicKey());
        String privateKey = Numeric.toHexStringNoPrefix(ecKeyPair.getPrivateKey());
        String address = getAddress(publicKey);

        //生成的以太坊私钥如果首字母为0，会丢弃首字母。此时丢弃该记录重新生成
        if (privateKey.length() != 64)
        {
            return createWallet();
        }

        Map<String, String> wallet = new HashMap<>();
        wallet.put("privateKey", privateKey);
        wallet.put("address", address);

        return wallet;
    }

    /**
     * 以太坊转账
     */
    public String sendEth(String fromPrivateKey, String toAddress, BigDecimal value) throws Exception
    {
        Web3j web3j = Web3j.build(new HttpService(serviceUrl));

        String fromAddress = getAddress(fromPrivateKey);

        //发送货币值
        BigInteger sendValue = Convert.toWei(value, Convert.Unit.ETHER).toBigInteger();

        //手续费单价
        BigInteger gasPrice = getGasPrice(web3j);

        BigInteger gasLimit = new BigInteger("21000");

        //获取地址下交易序号
        BigInteger nonce = getNonce(fromAddress, web3j);

        //生成未签名报文
        RawTransaction rawTransaction  = RawTransaction.createEtherTransaction(nonce, gasPrice, gasLimit, toAddress, sendValue);

        //获取证书
        ECKeyPair ecKeyPair = ECKeyPair.create(Numeric.toBigInt(fromPrivateKey));
        Credentials credentials = Credentials.create(ecKeyPair);

        //生成签名后报文
        byte[] hexValue = TransactionEncoder.signMessage(rawTransaction, credentials);
        String signedMessage = Numeric.toHexString(hexValue);

        //签名后报文广播到链上
        EthSendTransaction ethSendTransaction = web3j.ethSendRawTransaction(signedMessage).sendAsync().get();
        String result = ethSendTransaction.getResult();
        Response.Error error = ethSendTransaction.getError();
        if (null != error)
        {
            logger.error("error code:" + error.getCode() + " message:" + error.getMessage() + " data:" + error.getData() + " result:" + result);
        }
        String txHash = ethSendTransaction.getTransactionHash();

        return txHash;
    }

    /**
     * erc20合约币转账
     * @param fromPrivateKey 转账地址的私钥
     * @param toAddress 被转账地址
     * @param value 转账erc20币的数量
     * @param contractAddress erc20币合约地址
     * @param transferName 调用智能合约的方法
     * @param accuracy 货币精度
     * @param gasLimit 最多消耗gas
     * @return
     */
    public String sendErc20(String fromPrivateKey, String toAddress, BigDecimal value, String contractAddress, String transferName, Integer accuracy, BigInteger gasLimit) throws Exception
    {
        Web3j web3j = Web3j.build(new HttpService(serviceUrl));

        String fromAddress = getAddress(fromPrivateKey);

        //发送货币值
        BigInteger sendValue = value.multiply(BigDecimal.TEN.pow(accuracy)).toBigInteger();

        //手续费单价
        BigInteger gasPrice = getGasPrice(web3j);

        //获取地址下交易序号
        BigInteger nonce = getNonce(fromAddress, web3j);

        //生成备注信息（调用合约哪个方法，参数是啥）
        List<Type> inputParameters = new ArrayList<>();
        inputParameters.add(new Address(toAddress));
        inputParameters.add(new Uint256(sendValue));
        List<TypeReference<?>> outputParameters = Arrays.asList(new TypeReference<Bool>(){});
        Function function = new Function(transferName, inputParameters, outputParameters);
        String encodedFunction = FunctionEncoder.encode(function);

        //生成未签名报文
        RawTransaction rawTransaction  = RawTransaction.createTransaction(nonce, gasPrice, gasLimit, contractAddress, encodedFunction);

        //获取证书
        ECKeyPair ecKeyPair = ECKeyPair.create(Numeric.toBigInt(fromPrivateKey));
        Credentials credentials = Credentials.create(ecKeyPair);

        //生成签名后报文
        byte[] hexValue = TransactionEncoder.signMessage(rawTransaction, credentials);
        String signedMessage = Numeric.toHexString(hexValue);

        //签名后报文广播到链上
        EthSendTransaction ethSendTransaction = web3j.ethSendRawTransaction(signedMessage).sendAsync().get();
        String result = ethSendTransaction.getResult();
        Response.Error error = ethSendTransaction.getError();
        if (null != error)
        {
            logger.error("error code:" + error.getCode() + " message:" + error.getMessage() + " data:" + error.getData() + " result:" + result);
        }
        String txHash = ethSendTransaction.getTransactionHash();

        return txHash;
    }

    public Map<String, Object> getErc20Info(String txHash, String transferMethodId, Integer accuracy) throws Exception
    {
        Web3j web3j = Web3j.build(new HttpService(serviceUrl));
        EthTransaction ethTransaction =  web3j.ethGetTransactionByHash(txHash).sendAsync().get();

        String input = ethTransaction.getResult().getInput();
        if (!input.startsWith(transferMethodId))
        {
            return null;
        }
        input = input.substring(transferMethodId.length());

        List<TypeReference<Type>> outputParameters = convert(Arrays.asList(new TypeReference<Address>(){}, new TypeReference<Uint256>(){}));
        List<Type> inputParameters = FunctionReturnDecoder.decode(input, outputParameters);
        Uint256 uint256 = (Uint256) inputParameters.get(1);
        BigDecimal value = new BigDecimal(uint256.getValue().toString()).divide(BigDecimal.TEN.pow(accuracy));

        Address to = (Address) inputParameters.get(0);

        System.out.println(value);
        System.out.println(to);
        return null;
    }

    /**
     * 根据私钥生成地址
     */
    private String getAddress(String privateKey)
    {
        ECKeyPair ecKeyPair = ECKeyPair.create(Numeric.toBigInt(privateKey));
        String publicKey = Numeric.toHexStringNoPrefix(ecKeyPair.getPublicKey());
        String address = "0x" + Keys.getAddress(publicKey);
        return address;
    }

    /**
     * 计算出每gas单价
     */
    private BigInteger getGasPrice(Web3j web3j) throws ExecutionException, InterruptedException
    {
        BigInteger gasPrice = priceHolder.get();

        if (null == gasPrice)
        {
            //获取区块链上当前手续费价格
            gasPrice = web3j.ethGasPrice().sendAsync().get().getGasPrice();

            //如果价格太低，则取我们设置的最小值
            if (gasPrice.compareTo(gasPriceMin) == -1)
            {
                gasPrice = gasPriceMin;
            }
            //如果价格太高，则取我们设置的最大值
            if (gasPrice.compareTo(gasPriceMax) == 1)
            {
                gasPrice = gasPriceMax;
            }
        }

        return gasPrice;
    }

    /**
     * 获取地址下nonce
     * 如果一个进程需要对一个地址进行多次操作，第一次查询链，其他依次累加
     */
    private BigInteger getNonce(String address, Web3j web3j) throws Exception
    {
        Map<String, BigInteger> nonceMap = nonceHolder.get();
        if (null == nonceMap)
        {
            nonceMap = new HashMap<>();
            nonceHolder.set(nonceMap);
        }

        BigInteger nonce = nonceMap.get(address);
        if (null == nonce)
        {
            //获取地址下交易序号
            EthGetTransactionCount ethGetTransactionCount = web3j.ethGetTransactionCount(address, DefaultBlockParameterName.LATEST).send();
            nonce = ethGetTransactionCount.getTransactionCount();

            nonceMap.put(address, nonce);
        }
        else
        {
            nonce = nonce.add(new BigInteger("1"));
            nonceMap.put(address, nonce);
        }

        return nonce;
    }



}
