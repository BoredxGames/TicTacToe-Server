package com.mycompany.tictactoeserver.domain.services.communication;

public enum Action {
    LOGIN(10),
    REGISTER(20),
    REQUEST_GAME(30),
    GAME_RESPONSE(40),
    SEND_GAME_UPDATE(50);


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


}
