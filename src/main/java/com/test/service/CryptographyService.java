package com.test.service;

import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPrivateKey;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPublicKey;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.stereotype.Service;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Keys;
import org.web3j.utils.Numeric;
import sun.security.ec.ECPrivateKeyImpl;

import javax.crypto.Cipher;
import java.math.BigInteger;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.SecureRandom;
import java.security.Security;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.security.spec.ECGenParameterSpec;
import java.util.Arrays;
import java.util.Map;

/**
 * 椭圆曲线
 * Created by 吴晓冬 on 2018/5/31.
 */
@Service
public class CryptographyService
{
    public static Map<String, String> initKeypair() throws Exception
    {
        Security.addProvider(new BouncyCastleProvider());

        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("ECDSA", "BC");
        ECGenParameterSpec ecGenParameterSpec = new ECGenParameterSpec("secp256k1");
        keyPairGenerator.initialize(ecGenParameterSpec, new SecureRandom());
        KeyPair keyPair = keyPairGenerator.generateKeyPair();

        BCECPrivateKey privateKey = (BCECPrivateKey) keyPair.getPrivate();
        BCECPublicKey publicKey = (BCECPublicKey) keyPair.getPublic();

        BigInteger privateKeyValue = privateKey.getD();
        byte[] publicKeyBytes = publicKey.getQ().getEncoded(false);
        BigInteger publicKeyValue = new BigInteger(1, Arrays.copyOfRange(publicKeyBytes, 1, publicKeyBytes.length));

        String publicKeyString = Numeric.toHexStringNoPrefix(publicKeyValue);
        String privateKeyString = Numeric.toHexStringNoPrefix(privateKeyValue);
        String address = "0x" + Keys.getAddress(publicKeyValue);

        int PUBLIC_KEY_SIZE = 64;
        int PUBLIC_KEY_LENGTH_IN_HEX = 128;

        System.out.println(PUBLIC_KEY_LENGTH_IN_HEX);

        return null;
    }

    private static String[] getEthWallet() throws Exception
    {
        ECKeyPair ecKeyPair = Keys.createEcKeyPair();
        String publicKey = Numeric.toHexStringNoPrefix(ecKeyPair.getPublicKey());
        String privateKey = Numeric.toHexStringNoPrefix(ecKeyPair.getPrivateKey());
        String address = "0x" + Keys.getAddress(publicKey);

        if (privateKey.length() != 64)
        {
            return getEthWallet();
        }

        String[] wallet = new String[]{address, privateKey};

        return wallet;
    }

    public static void main(String[] args) throws Exception
    {
        initKeypair();
    }

    private String getAddress(String privateKey)
    {
        ECKeyPair ecKeyPair = ECKeyPair.create(Numeric.toBigInt(privateKey));
        String publicKey = Numeric.toHexStringNoPrefix(ecKeyPair.getPublicKey());
        String address = "0x" + Keys.getAddress(publicKey);
        return address;
    }

    public String sign() throws Exception
    {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("EC","BC");

        keyPairGenerator.initialize(256, new SecureRandom());

        KeyPair kp = keyPairGenerator.generateKeyPair();

        ECPublicKey publicKey = (ECPublicKey) kp.getPublic();
        ECPrivateKey privateKey = (ECPrivateKey) kp.getPrivate();

        System.out.println(kp.getPrivate());
        System.out.println(kp.getPublic());

        Cipher encrypter = Cipher.getInstance("ECIES", "BC");
        Cipher decrypter = Cipher.getInstance("ECIES", "BC");
        encrypter.init(Cipher.ENCRYPT_MODE, publicKey);
        decrypter.init(Cipher.DECRYPT_MODE, privateKey);

        return null;
    }
}
