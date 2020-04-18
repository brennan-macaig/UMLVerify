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
import org.bukkit.plugin.Plugin;
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
            
            // If they ask for help with the verify command.
            if(msg.equalsIgnoreCase("verify")) {
                p.sendMessage(FormatChat.formatChat("&dTo verify your UML email, please type your school provided email address into chat."));
                p.sendMessage(FormatChat.formatChat("&dThen, follow the instructions that are sent to your school provided email."));
            } else if(msg.startsWith(plugin.getConfig().getString("verification.auth-starts-with"))) {
                // Check if valid auth code
            } else if if(msg.endsWith("uml.edu")) {
                String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." + "[a-zA-Z0-9_+&*-]+)*@" + "(?:[a-zA-Z0-9-]+\\.)+[a-z" + "A-Z]{2,7}$";
                Pattern pat = Pattern.compile(emailRegex);
                if(pat.matcher(msg).matches()) {
                    // Their email is valid. Feed this to the email sender.
                    if(emailer.sendVerification(p.getUniqueId(), p.getName(), msg)) {
                        p.sendMessage(FormatChat.formatChat("&eEmail sent! Check your inbox for instructions"));
                    } else {
                        p.sendMessage(FormatChat.formatChat("&cFailed to send email. Please try again."));
                    }
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