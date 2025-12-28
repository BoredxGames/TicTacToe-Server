package com.mycompany.tictactoeserver.domain.exception;

public class ExceptionHandlerMiddleware {

    // Singleton
    private static ExceptionHandlerMiddleware instance;

    private ExceptionHandlerMiddleware() {
    }
   
    public static ExceptionHandlerMiddleware getInstance() {
        if (instance == null)
            instance = new ExceptionHandlerMiddleware();
        return instance;
    }
    
    
   
    public void handleException(Exception ex) {
        switch (ex.getMessage()) {

            // case "error-name
            case "database-connection-exception":
                break;
            case "database-dis-connection-exception":
                break;
            case "hashing-exception":
                System.out.println("hashing-exception");
                break;
            case "player-insertion-exception":
                System.out.println("Failed to insert player!");
                break;
            case "player-update-exception":
                System.out.println("Failed to update player!");
                break;
            case "player-deletion-exception":
                System.out.println("Failed to delete player!");
                break;
            case "player-not-found-exception":
                System.out.println("Player not found!");
                break;

        }
    }

}
