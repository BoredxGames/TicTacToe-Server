package com.mycompany.tictactoeserver.domain.entity;

import com.mycompany.tictactoeserver.datasource.model.Player;

public class AuthenticationDto {

    private String username ; 
    private String password ; 

    public AuthenticationDto() {
    }

    public AuthenticationDto(Player player) {
        if(player!=null)
        {
        this.username= player.getUsername();
        this.password= player.getPassword();
        }
        else 
        {
            this.username=null;
            this.password=null;
        }
    }

    public AuthenticationDto(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    
    public boolean isPlayerFound()
    {
        return username!=null;
    }
    
    @Override
    public String toString()
    {
        return "Username: " +username  ;  
    }
}
