package com.test.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.bitcoinj.core.DumpedPrivateKey;
import org.bitcoinj.params.AbstractBitcoinNetParams;
import org.bitcoinj.params.MainNetParams;
import org.bitcoinj.params.TestNet2Params;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 吴晓冬 on 2018/4/12.
 */
@Service
public class BtcService
{
    @Autowired
    private RestTemplate restTemplate;

    @Value("${btc.rpc.url}")
    private String rpcUrl;

    @Value("${btc.rpc.auth}")
    private String rpcAuth;

    @Value("${btc.net}")
    private String net;

    @Value("${btc.feerate}")
    private BigDecimal feerate;

    /**
     * 比特币转账
     */
    public String sendBtc(String fromPrivateKey, String toAddress, BigDecimal value) throws Exception
    {
        AbstractBitcoinNetParams netParams;
        if ("main".equals(net))//正式链
        {
            netParams = MainNetParams.get();
        }
        else//测试链
        {
            netParams = TestNet2Params.get();
        }

        //算出私钥对应的地址
        DumpedPrivateKey dumpedPrivateKey = DumpedPrivateKey.fromBase58(netParams, fromPrivateKey);
        String fromAddress = dumpedPrivateKey.getKey().toAddress(netParams).toString();;

        //调用rpc接口获取地址下的utxo
        List<Map<String, Object>> utxoInfos = (List<Map<String, Object>>) getBlockchainInfo("listunspent", 10, 9999999, new String[]{fromAddress}).get("result");

        //手续费 基础2单位 每增加一条out 增加4单位，每增加一条input增加20单位
        BigDecimal fee = new BigDecimal("2").multiply(feerate);
        //找零
        BigDecimal change = new BigDecimal("0").subtract(value).subtract(fee);

        //计算input
        List<Map<String, Object>> inputs = new ArrayList<>();
        //计算output
        Map<String, BigDecimal> outputs = new HashMap<>();
        outputs.put(toAddress, value);
        fee = fee.add(new BigDecimal("4").multiply(feerate));

        for (Map<String, Object> utxoInfo : utxoInfos)
        {
            Map<String, Object> input = new HashMap<>();
            input.put("txid", utxoInfo.get("txid"));
            input.put("vout", utxoInfo.get("vout"));
            input.put("scriptPubKey", utxoInfo.get("scriptPubKey"));
            inputs.add(input);

            BigDecimal amount =  new BigDecimal(String.valueOf(utxoInfo.get("amount")));
            BigDecimal addFee = new BigDecimal("20").multiply(feerate);

            fee = fee.add(addFee);
            change = change.add(amount).subtract(addFee);

            BigDecimal chargeFee = new BigDecimal("4").multiply(feerate);
            if (change.compareTo(chargeFee) >= 0)
            {
                if (change.compareTo(chargeFee) > 0)
                {
                    fee = fee.add(chargeFee);
                    change = change.subtract(chargeFee);

                    outputs.put(fromAddress, change);
                }
                break;
            }
        }

        if (change.compareTo(new BigDecimal("0")) < 0)
        {
            return "余额不足";
        }

        //生成待签名报文
        String rawtransaction = (String) getBlockchainInfo("createrawtransaction", inputs, outputs).get("result");
        System.out.println("rawtransaction:" + rawtransaction);
        //签名
        String signrawtransaction = (String) ((Map<String, Object>) getBlockchainInfo("signrawtransaction", rawtransaction, inputs, new String[]{fromPrivateKey}).get("result")).get("hex");
        System.out.println("signrawtransaction:" + signrawtransaction);
        //广播
        String txHash = (String) getBlockchainInfo("sendrawtransaction", signrawtransaction).get("result");
        System.out.println("txHash:" + txHash);

        return txHash;
    }

    /**
     * 调用比特币rpc接口
     */
    private Map<String, Object> getBlockchainInfo(String methodName, Object ... params) throws Exception
    {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", rpcAuth);

        Map<String, Object> body = new HashMap<>();
        body.put("method", methodName);

        if (null != params && params.length != 0)
        {
            body.put("params", params);
        }
 
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

        ResponseEntity<Map> blockcountMap = restTemplate.exchange(rpcUrl, HttpMethod.POST, entity, Map.class);

        Map<String, Object> response = blockcountMap.getBody();

        if (null != response.get("error"))
        {
            System.out.println("比特币接口异常");
            System.out.println(new ObjectMapper().writeValueAsString(body));
            System.out.println(new ObjectMapper().writeValueAsString(response));
        }

        return response;
    }
}
