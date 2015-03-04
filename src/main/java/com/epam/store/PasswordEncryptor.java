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
        byte[] saltBytes = getRandomSalt(16);
        String salt = new BigInteger(1, saltBytes).toString(16);
        //need to generate salt again if previous was not 32 length, rarely it happens
        while (salt.length() != 32) {
            log.debug("Generating salt again");
            saltBytes = getRandomSalt(16);
            salt = new BigInteger(1, saltBytes).toString(16);
        }
        byte[] hashBytes = getHash(passwordBytes, saltBytes);
        String hash = new BigInteger(1, hashBytes).toString(16);
        return new Password(hash, salt);
    }


    public static boolean comparePassword(byte[] passwordBytes, Password encryptedPassword) {
        //salt string to bytes
        byte[] saltBytes = DatatypeConverter.parseHexBinary(encryptedPassword.getSalt());
        //encrypt the password string with the salt from encrypted password
        byte[] hashBytes = getHash(passwordBytes, saltBytes);
        String hashOfPasswordToCompare = new BigInteger(1, hashBytes).toString(16);
        return hashOfPasswordToCompare.equals(encryptedPassword.getHash());
    }

    private static byte[] getHash(byte[] passwordBytes, byte[] saltBytes) {
        byte[] bytesToEncrypt = new byte[saltBytes.length + passwordBytes.length];
        System.arraycopy(passwordBytes, 0, bytesToEncrypt, 0, passwordBytes.length); //copy bytes from password
        System.arraycopy(saltBytes, 0, bytesToEncrypt, passwordBytes.length, saltBytes.length); //copy bytes from salt
        return createHash(bytesToEncrypt);
    }

    private static byte[] createHash(byte[] bytesToEncrypt) {
        byte[] hashBytes = new byte[bytesToEncrypt.length];
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(bytesToEncrypt);
            hashBytes = md.digest();
        } catch (NoSuchAlgorithmException e) {
            log.error("Error while creating hash", e);
        }
        return hashBytes;
    }

    private static byte[] getRandomSalt(int bytesLength) {
        byte[] saltBytes = new byte[bytesLength];
        new SecureRandom().nextBytes(saltBytes);
        return saltBytes;
    }
}
