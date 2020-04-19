package edu.uml.cs.bmacaig.umlverify.utils;

import org.bukkit.entity.Player;

public class AuthToken {

    private String token;
    private Player player;
    
    public enum TOKEN_STATUS {
        FAILURE, SUCCESS
    }

    public AuthToken(String token, Player player) {
        this.token = token;
        this.player = player;
    }

    public String getToken() {
        return token;
    }
    
    public Player getPlayer() {
        return player;
    }
}