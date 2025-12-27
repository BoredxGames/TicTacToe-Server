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
            byte[] rawText= text.getBytes();
            
            MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
            
            messageDigest.update(rawText);
            
            byte []encryptedrawText = messageDigest.digest();
            
            String encryptedText = bytesToHex(encryptedrawText);
            
            return encryptedText;
        } catch (NoSuchAlgorithmException ex) {
           throw new  HashingException(ex.getStackTrace());
        }
    }
    
   private static String bytesToHex(byte []bytes)
   {
       StringBuilder hexString = new StringBuilder();
       
       for (byte b : bytes){
           hexString.append(String.format("%02X", b));
       }
       return hexString.toString();
   }
   
}

