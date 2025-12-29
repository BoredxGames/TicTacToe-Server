package com.mycompany.tictactoeserver.domain.authentication;

import com.mycompany.tictactoeserver.datasource.database.dao.PlayerDAO;
import com.mycompany.tictactoeserver.datasource.model.Player;
import com.mycompany.tictactoeserver.domain.communication.Header;
import com.mycompany.tictactoeserver.domain.communication.Message;
import com.mycompany.tictactoeserver.domain.communication.MessageType;
import com.mycompany.tictactoeserver.domain.entity.AuthenticationDto;
import com.mycompany.tictactoeserver.domain.exception.ExceptionHandlerMiddleware;
import com.mycompany.tictactoeserver.domain.exception.HashingException;
import com.mycompany.tictactoeserver.domain.exception.ServerInterruptException;
import com.mycompany.tictactoeserver.domain.security.ServerSecurityManager;

public class  AuthenticationManager {

    
    private static AuthenticationManager instance ;
    private final PlayerDAO playerDao ;
    
    private AuthenticationManager() {
        this.playerDao = new PlayerDAO();
    }
    
    public static AuthenticationManager getInstance()
    {
        if(instance==null)
            instance= new AuthenticationManager();
        
       return instance;
    }
  
    
    
    
     public Message register(String username , String plainTextPassword){
        
      AuthenticationDto player = new AuthenticationDto(
         playerDao.findByUsername(username)
         );      
        
      if(!player.isPlayerFound()){
      
          try {
              String hashedPassword = ServerSecurityManager.hashText(plainTextPassword);
              playerDao.insert(new Player(username , hashedPassword));
              player.setPassword(hashedPassword);
              player.setUsername(username);
          } catch (HashingException ex) {
                ServerInterruptException customException = new ServerInterruptException(ex.getStackTrace());
                ExceptionHandlerMiddleware.getInstance().handleException(customException);         
          }
      }
     
    
    return new Message( new Header(MessageType.ERROR), player);

    }
     
    public Message login(String username , String plainTextPassword){
        
        try {
            AuthenticationDto player = new AuthenticationDto(
                    playerDao.findByUsername(username)
            );
            
            String hashedPassword = ServerSecurityManager.hashText(plainTextPassword);
            Header messagHeader = new Header();
            
            
            
            if( player.isPlayerFound() && 
                username.equals(player.getUsername()) && 
                hashedPassword.endsWith(player.getPassword())){
                
                messagHeader.setMsgType(MessageType.RESPONSE);
            }
            else{
                messagHeader.setMsgType(MessageType.ERROR);
            }
            
            return new Message( messagHeader, player);
        } catch (HashingException ex) {
            ServerInterruptException customException = new ServerInterruptException(ex.getStackTrace());
                ExceptionHandlerMiddleware.getInstance().handleException(customException);                
        }
        return null ; 
    }
    
    
    
    
   
    
}
