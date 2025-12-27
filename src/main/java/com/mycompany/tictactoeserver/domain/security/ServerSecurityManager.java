/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tictactoeserver.domain.security;

import com.mycompany.tictactoeserver.domain.exception.HashingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *
 * @author Hazem
 */
public class ServerSecurityManager {
    final static private String algorithm = "SHA-256";

    public ServerSecurityManager() {
    }
    
    public static String hashText(String text) throws HashingException {
        
        try {
            byte[] rowText= text.getBytes();
            
            MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
            
            messageDigest.update(rowText);
            
            byte []encryptedRowText = messageDigest.digest();
            
            String encryptedText = bytesToHex(encryptedRowText);
            
            return encryptedText;
        } catch (NoSuchAlgorithmException ex) {
           throw new  HashingException ();
        }
    }
    
    private static String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            hexString.append(String.format("%x", b));
        }
        return hexString.toString();
    }
   
}
