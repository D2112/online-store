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
    private static Logger log = LoggerFactory.getLogger(PasswordEncryptor.class.getName());

    public static Password encrypt(byte[] passwordBytes) {
        byte[] saltBytes = new byte[16];
        new SecureRandom().nextBytes(saltBytes);
        byte[] hashBytes = encrypt(passwordBytes, saltBytes);

        String hash = new BigInteger(1, hashBytes).toString(16);
        String salt = new BigInteger(1, saltBytes).toString(16);
        return new Password(hash, salt);
    }


    public static boolean comparePassword(byte[] passwordBytes, Password encryptedPassword) {
        //salt string to bytes
        byte[] saltBytes = DatatypeConverter.parseHexBinary(encryptedPassword.getSalt());
        //encrypt the password string with the salt from encrypted password
        byte[] hashBytes = encrypt(passwordBytes, saltBytes);
        String hashOfPasswordToCompare = new BigInteger(1, hashBytes).toString(16);
        return hashOfPasswordToCompare.equals(encryptedPassword.getHash());
    }

    private static byte[] encrypt(byte[] passwordBytes, byte[] saltBytes) {
        byte[] hashBytes = new byte[saltBytes.length + passwordBytes.length];
        System.arraycopy(passwordBytes, 0, hashBytes, 0, passwordBytes.length);
        System.arraycopy(saltBytes, 0, hashBytes, passwordBytes.length, saltBytes.length);

        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(hashBytes);
            hashBytes = md.digest();
        } catch (NoSuchAlgorithmException e) {
            log.error("Error while creating hash", e);
        }
        return hashBytes;
    }
}
