package com.mycompany.tictactoeserver.domain.entity;/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


/**
 *
 * @author Hazem
 */
public class AuthRequestEntity {
    private String userName ;
    private String password;

    public AuthRequestEntity(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "AuthRequestEntity{" + "userName=" + userName + ", password=" + password + '}';
    }

    public AuthRequestEntity() {
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public static AuthRequestEntity createAuthEntity(String username , String password)
    {
        return new AuthRequestEntity(username , password );
    }



}
