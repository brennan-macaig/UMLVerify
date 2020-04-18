package edu.uml.cs.bmacaig.umlverify.utils;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;

import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class SendEmail {

    private final JavaPlugin plugin;
    private final String SMTP_HOST, SMTP_USER, SMTP_PASS, SMTP_FROM, SMTP_PORT, EMAIL_SUBJECT;
    private final List<String> EMAIL_BODY;
    private final int AUTH_LENGTH;

    public SendEmail(JavaPlugin plugin) {
        this.plugin = plugin;
        this.SMTP_HOST = plugin.getConfig().getString("email.smtp-server");
        this.SMTP_USER = plugin.getConfig().getString("email.smtp-user");
        this.SMTP_PASS = plugin.getConfig().getString("email.smtp-pass");
        this.SMTP_FROM = plugin.getConfig().getString("email.from-address");
        this.SMTP_PORT = plugin.getConfig().getString("email.smtp-port");
        this.EMAIL_SUBJECT = plugin.getConfig().getString("email.email-subject");
        this.EMAIL_BODY = plugin.getConfig().getStringList("email.email-body");
        this.AUTH_LENGTH = plugin.getConfig().getInt("verification.auth-length");
    }

    private void writeEmail(Session session, String toEmail, String subject, String body) {
        try {
            MimeMessage msg = new MimeMessage(session);
            msg.addHeader("Content-Type", "text/HTML; charset=UTF-8");
            msg.addHeader("format", "flowed");
            msg.addHeader("Content-Transfer-Encoding", "8bit");

            msg.setFrom(new InternetAddress(SMTP_FROM, "HawkCraft"));
            msg.setReplyTo(InternetAddress.parse(SMTP_FROM, false));
            msg.setSubject(subject, "UTF-8");
            msg.setText(body, "UTF-8");
            msg.setSentDate(new Date());
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail, false));
            Transport.send(msg);
        } catch (Exception e) {
            plugin.getLogger().severe("Was unable to send email. Is SMTP configured correctly?");
            e.printStackTrace();
        }
    }

    public boolean sendVerification(Player player, String emailAddr) {
        String tok = generateAuthToken();
        String body = parseEmailBody(plugin.getConfig().getStringList("email.email-body"), player, tok);
        Properties props = new Properties();
        props.put("mail.smtp.host", SMTP_HOST);
        props.put("mail.smtp.port", SMTP_PORT);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Authenticator auth = new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(SMTP_FROM, SMTP_PASS);
            }
        };

        Session session = Session.getInstance(props, auth);
            
        writeEmail(session, emailAddr,
                            plugin.getConfig().getString("email.email-subject"),
                            body);
        return false;
    }

    private String generateAuthToken() {
        // Randomly genereate string given size from config file
        String allowable = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder sb = new StringBuilder(AUTH_LENGTH);
        for(int i = 0; i < AUTH_LENGTH; i++) {
            int idx = (int) (allowable.length() * Math.random());
            sb.append(allowable.charAt(idx));
        }
        return (plugin.getConfig().getString("verification.auth-starts-with") + sb.toString());
    }

    private String parseEmailBody(List<String> body, Player player, String token) {
        // Allowable modifiers:
        // %user% - Minecraft user name
        // %email% - Email address used
        // %fname% - Firstname peeled from the email
        // %lname% - Lastname peeled from the email
        // %authcode% - The actual authcode sent to the user
        // %uuid% - Minecraft user Unique User Identifier

        
        return "";
    }

}