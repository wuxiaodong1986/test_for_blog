package com.test.service;

import org.ethereum.crypto.ECIESCoder;
import org.ethereum.crypto.ECKey;
import org.ethereum.util.ByteUtil;
import org.spongycastle.math.ec.ECPoint;
import org.spongycastle.util.Arrays;
import org.spongycastle.util.encoders.Hex;
import org.springframework.util.StringUtils;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Keys;
import org.web3j.utils.Numeric;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

public class EncryptEthj {

    public static void main(String[] args) throws InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchProviderException, UnsupportedEncodingException
    {
//        ECKeyPair ecKeyPair = Keys.createEcKeyPair();
//        String privKeyStr = Numeric.toHexStringNoPrefix(ecKeyPair.getPrivateKey());
        String privKeyStr = "e4340f983e004f916ef9e5c4180d264c4f2f1668c4fb6d3da6488cba4cb7f9c4";
        System.out.println("privateKey: " + privKeyStr);
        BigInteger privKey = Numeric.toBigIntNoPrefix(privKeyStr);
        byte[] payload = Hex.decode(HexStringConverter.getHexStringConverterInstance().stringToHex("{rzccc}"));

        ECKey ecKey =  ECKey.fromPrivate(privKey);
        ECPoint pubKeyPoint = ecKey.getPubKeyPoint();

        byte[] cipher = new byte[0];
        short size = (short) (payload.length + ECIESCoder.getOverhead());
        byte[] prefix = ByteUtil.shortToBytes(size);
        try {
            cipher = ECIESCoder.encrypt(pubKeyPoint, payload, prefix);
        } catch (Throwable e) {e.printStackTrace();}

        System.out.println(Hex.toHexString(cipher));

        byte[] decrypted_payload = new byte[0];
        try {
            decrypted_payload = ECIESCoder.decrypt(privKey, cipher, prefix);
//            decrypted_payload = ECIESCoder.decrypt(privKey, cipher);
        } catch (Throwable e) {
            e.printStackTrace();
        }

        System.out.println(Hex.toHexString(decrypted_payload));
        System.out.println(HexStringConverter.getHexStringConverterInstance().hexToString(Hex.toHexString(decrypted_payload)));

//        decryptService(privKeyStr, Hex.toHexString(cipher));
//        decryptService();
    }

    public static void decryptService()
    {
        String privKeyStr = "e4340f983e004f916ef9e5c4180d264c4f2f1668c4fb6d3da6488cba4cb7f9c4";
        byte[] cipher = Hex.decode("442dcf965745eadbedd537a394e286e3049fae7c793f18d68fed63d6c8e9a2b002374857f45e26984e234f2703237105dec78a579768cd6ff3e9459140525e54273899ae8b75778ce265c4ec5b1769a70174205a4f95fba069dfc514e9204954ebea161fa5496f763a0eb51ffcc61af3f4e8529083c5dad9d015e2a6241544fbb7");
        BigInteger privKey = Numeric.toBigIntNoPrefix(privKeyStr);
        byte[] decrypted_payload = new byte[0];
        try {
            decrypted_payload = ECIESCoder.decrypt(privKey, cipher);
        } catch (Throwable e) {
            e.printStackTrace();
        }

        System.out.println(Hex.toHexString(decrypted_payload));
        System.out.println(HexStringConverter.getHexStringConverterInstance().hexToString(Hex.toHexString(decrypted_payload)));

    }

}