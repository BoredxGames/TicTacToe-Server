/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tictactoeserver.domain.utils.exception;

import java.security.NoSuchAlgorithmException;

/**
 *
 * @author Hazem
 */
public class HashingException extends NoSuchAlgorithmException {

    public HashingException(StackTraceElement[] stackTrace) {
        super("hashing-exception");
        super.setStackTrace(stackTrace);
    }


}
