package com.mycompany.tictactoeserver.domain.services.communication;

public enum Action {
    LOGIN(10),
    REGISTER(20),
    REQUEST_GAME(30),
    GAME_RESPONSE(35),
    GAME_START(40),
    SEND_GAME_UPDATE(50),
    GAME_END(55),
    USERNAME_NOT_FOUND(60),
    REGISTERATION_SUCCESS(70),
    INTERNAL_SERVER_ERROR(80),
    INVALID_CREDENTIAL(90),
    LOGIN_SUCCESS(100),
    USERNAME_ALREADY_EXIST(110),
    PLAYER_BUSY(120),
    PENDING_REQUEST_EXISTS(130),
    TARGET_HAS_PENDING_REQUEST(140),
    ROOM_NOT_FOUND(150),
    INVALID_OPPONENT(160),
    NO_PENDING_REQUEST(170),
    GET_AVAILABLE_PLAYERS(180),
    USER_IS_ONLINE(190);
    
    final int id;

    Action(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static Action valueOf(int id) {
        for (Action action : Action.values()) {
            if (action.getId() == id) {
                return action;
            }
        }

        return null;
    }
 public static Action fromString(String msgType)
    {
        return switch (msgType.toLowerCase()) {
            case "login" -> Action.LOGIN;
           
     
            default -> throw new AssertionError();
        };
     }

}