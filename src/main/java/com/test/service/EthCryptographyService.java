package com.test.service;

import org.springframework.stereotype.Service;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Keys;
import org.web3j.crypto.Sign;
import org.web3j.utils.Numeric;

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
    public Map<String, String> initWallet() throws Exception
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
    public String sign(String messageString, String privateKey) throws Exception
    {
        String prefix =  "\u0019Ethereum Signed Message:\n" + messageString.length();
        byte[] message = (prefix + messageString).getBytes();

        //生成证书
        ECKeyPair ecKeyPair = ECKeyPair.create(Numeric.toBigInt(privateKey));

        //生成签名
        Sign.SignatureData signatureData = Sign.signMessage(message, ecKeyPair);

        byte[] signBytes = new byte[65];
        System.arraycopy(signatureData.getR(), 0, signBytes, 0, 32);
        System.arraycopy(signatureData.getS(), 0, signBytes, 32, 32);
        signBytes[64] = signatureData.getV();
        String sign = Numeric.toHexString(signBytes);

        return sign;
    }

    /**
     * 根据原始信息、签名 算出公钥
     */
    public String getKeyFromSign(String messageString, String signString) throws Exception
    {
        String prefix =  "\u0019Ethereum Signed Message:\n" + messageString.length();
        byte[] message = (prefix + messageString).getBytes();

        byte[] signBytes = Numeric.hexStringToByteArray(signString);

        byte[] r = new byte[32];
        System.arraycopy(signBytes, 0, r, 0, 32);
        byte[] s = new byte[32];
        System.arraycopy(signBytes, 32, s, 0, 32);
        byte v = signBytes[64];

        Sign.SignatureData sign = new Sign.SignatureData(v, r, s);

        return Numeric.toHexStringNoPrefix(Sign.signedMessageToKey(message, sign));
    }
}