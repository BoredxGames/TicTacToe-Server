package com.mycompany.tictactoeserver.domain.entity;

import com.mycompany.tictactoeserver.datasource.model.Player;
import org.json.JSONObject;

public class AuthResponseEntity {
    String id;
    String username;
    int score;

    public AuthResponseEntity() {
    }

    public AuthResponseEntity(Player player) {
        id = player.getId();
        username = player.getUsername();
        score = player.getScore();
    }

    public JSONObject toJson() {
        JSONObject json = new JSONObject();

        json.put("id", id);
        json.put("username", username);
        json.put("score", score);

        return json;

    }

}
