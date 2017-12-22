package com.google.refine.extension.database;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.util.text.TextEncryptor;


public class SimpleTextEncryptor implements TextEncryptor  {
    
    private final StandardPBEStringEncryptor encryptor;

    @Override
    public String decrypt(String encryptedMessage) {
        return this.encryptor.decrypt(encryptedMessage);
    }

    @Override
    public String encrypt(String message) {
        return this.encryptor.encrypt(message);
    }

    public SimpleTextEncryptor(String passwordChars) {
        super();
       
        this.encryptor = new StandardPBEStringEncryptor();
        this.encryptor.setAlgorithm("PBEWithMD5AndDES");
        this.encryptor.setPasswordCharArray(passwordChars.toCharArray());
    }
    
    
    
    public void setPassword(final String password) {
        this.encryptor.setPassword(password);
    }

    
    /**
     * Sets a password, as a char[]
     * 
     * @since 1.8
     * @param password the password to be set.
     */
    public void setPasswordCharArray(final char[] password) {
        this.encryptor.setPasswordCharArray(password);
    }
    
    

}
