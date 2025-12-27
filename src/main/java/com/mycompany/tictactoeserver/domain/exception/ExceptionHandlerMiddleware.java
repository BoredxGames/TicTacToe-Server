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
          

        }
    }

}
