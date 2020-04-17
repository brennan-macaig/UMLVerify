package edu.uml.cs.bmacaig.umlverify.utils;

import java.util.List;
import java.util.UUID;

import org.bukkit.plugin.java.JavaPlugin;

public class SendEmail {

    private final JavaPlugin plugin;
    private final String SMTP_HOST, SMTP_USER, SMTP_PASS, SMTP_FROM, EMAIL_SUBJECT;
    private final List<String> EMAIL_BODY;
    private final int AUTH_LENGTH;

    public SendEmail(JavaPlugin plugin) {
        this.plugin = plugin;
        this.SMTP_HOST = plugin.getConfig().getString("email.smtp-server");
        this.SMTP_USER = plugin.getConfig().getString("email.smtp-user");
        this.SMTP_PASS = plugin.getConfig().getString("email.smtp-pass");
        this.SMTP_FROM = plugin.getConfig().getString("email.from-address");
        this.EMAIL_SUBJECT = plugin.getConfig().getString("email.email-subject");
        this.EMAIL_BODY = plugin.getConfig().getStringList("email.email-body");
        this.AUTH_LENGTH = plugin.getConfig().getInt("verification.auth-length");

    }

    public boolean sendVerification(UUID uuid, String playerName, String emailAddr) {
        return false;
    }

    private String generateAuthToken() {
        // Randomly genereate string given size from config file
        String allowable = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" +
                                "0123456789" +
                                "abcdefghijklmnopqrstuvwxyz";
        StringBuilder sb = new StringBuilder(AUTH_LENGTH);
        for(int i = 0; i < AUTH_LENGTH; i++) {
            int idx = (int) (allowable.length() * Math.random());
            sb.append(allowable.charAt(idx));
        }
        return sb.toString();
    }

    private String parseEmailBody(String body) {
        // Allowable modifiers:
        // %user% - Minecraft user name
        // %email% - Email address used
        // %fname% - Firstname peeled from the email
        // %lname% - Lastname peeled from the email
        // %authcode% - The actual authcode sent to the user
        return "";
    }

}