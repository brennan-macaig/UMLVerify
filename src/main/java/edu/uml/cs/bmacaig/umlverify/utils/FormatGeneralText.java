package edu.uml.cs.bmacaig.umlverify.utils;

import org.bukkit.entity.Player;

public class FormatGeneralText {

    /**
     * Formats text not destined for in-game chat, i.e. body of an email.
     */
    public static String format(String text, Player player) {
        String fin = text;
        fin = fin.replaceAll("%user%", player.getName());
        fin = fin.replaceAll("%uuid%", player.getUniqueId().toString());
        return fin;
    }

    public static String format(String text, Player player, String email, String authcode) {
        String fin = text;
        fin = fin.replace("%user%", player.getName());
        fin = fin.replace("%uuid%", player.getUniqueId().toString());
        fin = fin.replace("%email%", email);
        String[] temp = email.split("_");
        fin = fin.replace("%fname%", temp[0]);
        fin = fin.replace("%lname%", temp[1]);
        fin = fin.replace("%authcode%", authcode);
        return fin;
    }
}