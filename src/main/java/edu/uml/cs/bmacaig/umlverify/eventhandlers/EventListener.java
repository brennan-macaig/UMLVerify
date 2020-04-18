package edu.uml.cs.bmacaig.umlverify.eventhandlers;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import edu.uml.cs.bmacaig.umlverify.utils.FormatChat;
import edu.uml.cs.bmacaig.umlverify.utils.Permissions;
import edu.uml.cs.bmacaig.umlverify.utils.SendEmail;

public class EventListener implements Listener{

    private final SendEmail emailer;
    private final JavaPlugin plugin;

    public EventListener(SendEmail emailer, JavaPlugin plugin) {
        this.emailer = emailer;
        this.plugin = plugin;
    }

    // Player chat event
    // Detects if the player should see chat
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        
        if(event.getPlayer().hasPermission(Permissions.unverified)) {
            event.setCancelled(true);
            Player p = event.getPlayer();
            String msg = event.getMessage();

            String emailRegex = "^[a-zA-Z]+_[a-zA-Z]+[0-9]*@(student.){0,1}uml.edu$";
            Pattern pat = Pattern.compile(emailRegex);

            
            // If they ask for help with the verify command.
            if(msg.equalsIgnoreCase("verify")) {

                // Typed the "verify" command for assistance verifying.

                p.sendMessage(FormatChat.formatChat("&dTo verify your UML email, please type your school provided email address into chat."));
                p.sendMessage(FormatChat.formatChat("&dThen, follow the instructions that are sent to your school provided email."));
            } else if(msg.startsWith(plugin.getConfig().getString("verification.auth-starts-with"))) {
                // Check if valid auth code
                String yaml = "" + p.getUniqueId() + ".code";
                String authcode = ""; // TODO: get authcode from database.
                if (msg.equalsIgnoreCase(authcode)) {
                    // Promote user to the rank
                } else {
                    // Auth code does not match.
                    p.sendMessage(FormatChat.formatChat("&cThe code you provided did not match the code you were emailed. Please try again"));
                }

            } else if(pat.matcher(msg).matches()) {

                // They typed an email, assuming needs verification.

                if(emailer.sendVerification(p, msg)) {
                        p.sendMessage(FormatChat.formatChat("&eEmail sent! Check your inbox for instructions"));
                    } else {
                        p.sendMessage(FormatChat.formatChat("&cFailed to send email. Please try again."));
                    }
            } else {
                p.sendMessage(FormatChat.formatChat("&cYou must verify your UML email to do that! Type &dverify&c for help."));
            }
        }
        
        List<Player> playerRecipients = new ArrayList<Player>();
        for(Player recipient : event.getRecipients()) {
            if(recipient.getName().equals(event.getPlayer().getName())) continue;
            if(recipient.hasPermission(Permissions.verified)) {
                playerRecipients.add(recipient);
            }
        }
        event.getRecipients().clear();
        event.getRecipients().addAll(playerRecipients);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerJoin(PlayerJoinEvent event) {
        event.setJoinMessage(null);
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p.hasPermission(Permissions.verified)) {
                p.sendMessage(FormatChat.formatChat("&e" + event.getPlayer().getName() + " has joined the game"));
            } else {
                continue;
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockBreak(BlockBreakEvent event) {
        Player p = event.getPlayer();
        if(p.hasPermission(Permissions.unverified)) {
            event.setCancelled(true);
            p.sendMessage(FormatChat.formatChat("&cYou must verify your UML email to do that! Type &dverify&c for help."));
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockPlace(BlockPlaceEvent event) {
        Player p = event.getPlayer();
        if(p.hasPermission(Permissions.unverified)) {
            event.setCancelled(true);
            p.sendMessage(FormatChat.formatChat("&cYou must verify your UML email to do that! Type &dverify&c for help."));
        }
    }

}