package edu.uml.cs.bmacaig.umlverify.utils;

import org.bukkit.entity.Player;

public class FormatGeneralText {

    /**
     * Formats text not destined for in-game chat, i.e. body of an email.
     */
    public static String format(String text, Player player) {
        text.replace("%user%", player.getName());
        text.replace("%uuid%", player.getUniqueId().toString());
        return text;
    }

    public static String format(String text, Player player, String email, String authcode) {
        text.replace("%user%", player.getName());
        text.replace("%uuid%", player.getName());
        text.replace("%email%", email);
        String[] temp = email.split("_");
        text.replace("%fname%", temp[0]);
        text.replace("%lname%", temp[1]);
        text.replace("%authcode%", authcode);
        return text;
    }
}