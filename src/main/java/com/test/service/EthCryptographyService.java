package com.test.service;

import org.springframework.stereotype.Service;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Keys;
import org.web3j.crypto.Sign;
import org.web3j.utils.Numeric;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 吴晓冬 on 2018/7/1.
 */
@Service
public class EthCryptographyService
{
    /**
     * 生成私钥、地址对
     */
    public static Map<String, String> initWallet() throws Exception
    {
        ECKeyPair ecKeyPair = Keys.createEcKeyPair();
        String publicKey = Numeric.toHexStringNoPrefix(ecKeyPair.getPublicKey());
        String privateKey = Numeric.toHexStringNoPrefix(ecKeyPair.getPrivateKey());
        String address = "0x" + Keys.getAddress(publicKey);

        if (privateKey.length() != 64)
        {
            return initWallet();
        }

        Map<String, String> wallet = new HashMap<>();
        wallet.put("privateKey", privateKey);
        wallet.put("address", address);

        return wallet;
    }

    /**
     * 用私钥对信息进行签名
     */
    public static String sign(String messageString, String privateKey) throws Exception
    {
        byte[] message = messageString.getBytes();

        //生成证书
        ECKeyPair ecKeyPair = ECKeyPair.create(Numeric.toBigInt(privateKey));
        Credentials credentials = Credentials.create(ecKeyPair);

        //生成签名
        Sign.SignatureData signatureData = Sign.signMessage(message, credentials.getEcKeyPair());

        String sign = Base64.getEncoder().encodeToString(new byte[]{signatureData.getV()}) + Base64.getEncoder().encodeToString(signatureData.getR()) + Base64.getEncoder().encodeToString(signatureData.getS());

        System.out.println(sign);

        return sign;
    }

    /**
     * 根据原始信息、签名 算出公钥
     */
    public static String getKeyFromSign(String messageString, String signString) throws Exception
    {
        byte[] message = messageString.getBytes();

        byte v = Base64.getDecoder().decode(signString.substring(0,4))[0];
        byte[] r = Base64.getDecoder().decode(signString.substring(4,48));
        byte[] s = Base64.getDecoder().decode(signString.substring(48,92));

        Sign.SignatureData sign = new Sign.SignatureData(v, r, s);

        return Numeric.toHexStringNoPrefix(Sign.signedMessageToKey(message, sign));
    }

    public static void main(String[] args) throws Exception
    {
        for (int i = 0; i < 10000; i++)
        {
            Map<String, String> wallet = initWallet();

            String message = "113324123123" + i;

            String sign = sign(message, wallet.get("privateKey"));

            String publicKey = getKeyFromSign(message, sign);

            String address = "0x" + Keys.getAddress(publicKey);

            System.out.println(wallet.get("address").equals(address) + " " + i);
        }

    }
}