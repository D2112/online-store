package com.epam.store;

import com.epam.store.model.Password;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.DatatypeConverter;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class PasswordEncryptor {
    private static Logger logger = LoggerFactory.getLogger(PasswordEncryptor.class.getName());

    public static Password encrypt(String password) {
        byte[] saltBytes = new byte[16];
        new SecureRandom().nextBytes(saltBytes);

        byte[] hashBytes = getHashBytes(password, saltBytes);

        String hash = new BigInteger(1, hashBytes).toString(16);
        String salt = new BigInteger(1, saltBytes).toString(16);
        return new Password(hash, salt);
    }

    public static boolean comparePassword(String password, Password encryptedPassword) {
        byte[] saltBytes = DatatypeConverter.parseHexBinary(encryptedPassword.getSalt());
        byte[] hashBytes = getHashBytes(password, saltBytes);
        String hash = new BigInteger(1, hashBytes).toString(16);
        return encryptedPassword.getHash().equals(hash);
    }

    public static byte[] getHashBytes(String password, byte[] saltBytes) {
        byte[] passBytes = password.getBytes();
        byte[] hashBytes = new byte[saltBytes.length + passBytes.length];
        System.arraycopy(passBytes, 0, hashBytes, 0, passBytes.length);
        System.arraycopy(saltBytes, 0, hashBytes, passBytes.length, saltBytes.length);

        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(hashBytes);
            hashBytes = md.digest();
        } catch (NoSuchAlgorithmException e) {
            logger.error("Error while creating hash", e);
            e.printStackTrace();
        }
        return hashBytes;
    }
}
