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
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import edu.uml.cs.bmacaig.umlverify.UMLVerify;
import edu.uml.cs.bmacaig.umlverify.utils.FormatChat;
import edu.uml.cs.bmacaig.umlverify.utils.Permissions;
import edu.uml.cs.bmacaig.umlverify.utils.SendEmail;

public class EventListener implements Listener{

    private final SendEmail emailer;
    private final JavaPlugin plugin;

    public EventListener(final SendEmail emailer, final JavaPlugin plugin) {
        this.emailer = emailer;
        this.plugin = plugin;
    }

    // Player chat event
    // Detects if the player should see chat
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerChat(final AsyncPlayerChatEvent event) {

        if (event.getPlayer().hasPermission(Permissions.unverified)) {
            event.setCancelled(true);
            final Player p = event.getPlayer();
            final String msg = event.getMessage();

            final String emailRegex = "^[a-zA-Z]+_[a-zA-Z]+[0-9]*@(student.){0,1}uml.edu$";
            final Pattern pat = Pattern.compile(emailRegex);

            // If they ask for help with the verify command.
            if (msg.equalsIgnoreCase("verify")) {

                p.sendMessage(FormatChat.formatChat(
                        "&eTo verify your UML email, please type your school provided email address into chat."));
                p.sendMessage(FormatChat
                        .formatChat("&eThen, follow the instructions that are sent to your school provided email."));

            } else if(msg.equalsIgnoreCase("restart")) {
                p.sendMessage(FormatChat.formatChat("&eOK. Let's try again!"));
                p.sendMessage(FormatChat.formatChat("&ePlease enter your UML email in chat, then follow the instructions that are emailed to you!"));
                if(UMLVerify.issuedTokens.containsKey(p.getName())) {
                    UMLVerify.issuedTokens.remove(p.getName());
                }
            } else if (UMLVerify.issuedTokens.containsKey(p.getName())) {

                if (UMLVerify.issuedTokens.get(p.getName()).equalsIgnoreCase(msg)) {
                    // User was able to be verified.
                    p.sendMessage(FormatChat.formatChat("&b--> &eThanks for verifying! &b<--"));
                    p.sendMessage(FormatChat.formatChat("&eMake sure you read the server rules before you continue."));
                    p.sendMessage(FormatChat.formatChat(
                            "&ePlease be aware that while logged in you can still be held accountable to the UMass Lowell code of conduct."));
                    p.sendMessage(FormatChat.formatChat("&eType &c/rules &eor &c/conduct &e for more information."));
                    p.sendMessage(FormatChat.formatChat("&b----------><----------"));
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), plugin.getConfig()
                                    .getString("verification.promote-command").replace("%user%", p.getName()));
                        }
                    }.runTask(plugin);
                    Bukkit.getServer().broadcast(FormatChat.formatChat("&e" + p.getName() + " has been verified!"),
                            Permissions.verified);
                } else {
                    // could not verify
                    p.sendMessage(FormatChat.formatChat("&cSorry, but that's not your auth code. Please try again."));
                }

            } else if (pat.matcher(msg).matches()) {

                // They typed an email, assuming needs verification.

                if (emailer.sendVerification(p, msg)) {
                    p.sendMessage(FormatChat.formatChat("&eEmail sent! Check your inbox for instructions"));
                    p.sendMessage(FormatChat.formatChat("&eIf you did not get an email, type &drestart &eto try again."));
                } else {
                    p.sendMessage(FormatChat.formatChat("&cFailed to send email. Please try again."));
                }
            } else {
                p.sendMessage(FormatChat
                        .formatChat("&cYou must verify your UML email to do that! Type &bverify&c for help."));
            }
        }

        final List<Player> playerRecipients = new ArrayList<Player>();
        for (final Player recipient : event.getRecipients()) {
            if (recipient.getName().equals(event.getPlayer().getName()))
                continue;
            if (recipient.hasPermission(Permissions.verified)) {
                playerRecipients.add(recipient);
            }
        }
        playerRecipients.add(event.getPlayer());
        event.getRecipients().clear();
        event.getRecipients().addAll(playerRecipients);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerJoin(final PlayerJoinEvent event) {
        if (event.getPlayer().hasPermission(Permissions.unverified)) {
            event.getPlayer().sendMessage(
                    FormatChat.formatChat("&b--> &eWelcome to Hawkcraft, " + event.getPlayer().getName() + "! &b<--"));
            event.getPlayer().sendMessage(FormatChat
                    .formatChat("&eTo play on this server, you need to verify your UMass Lowell email account."));
            event.getPlayer().sendMessage(FormatChat.formatChat(
                    "&eTo do so, simply type your UMass Lowell email into chat, followed by the enter key"));
            event.getPlayer()
                    .sendMessage(FormatChat.formatChat("&eThen, follow the instructions that are sent to your email!"));
            event.getPlayer().sendMessage(FormatChat.formatChat("&b------------------><-----------------"));
            event.setJoinMessage(null);
        } else {
            event.setJoinMessage(null);
            Bukkit.getServer().broadcast(
                    FormatChat.formatChat("&e" + event.getPlayer().getName() + " has joined the game"),
                    Permissions.verified);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerLeave(final PlayerQuitEvent event) {
        event.setQuitMessage(null);
        if (!event.getPlayer().hasPermission(Permissions.unverified)) {
            Bukkit.getServer().broadcast(FormatChat.formatChat("&e" + event.getPlayer().getName() + " has left the game"), Permissions.verified);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockBreak(final BlockBreakEvent event) {
        final Player p = event.getPlayer();
        if (p.hasPermission(Permissions.unverified)) {
            event.setCancelled(true);
            p.sendMessage(
                    FormatChat.formatChat("&cYou must verify your UML email to do that! Type &bverify&c for help."));
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockPlace(final BlockPlaceEvent event) {
        final Player p = event.getPlayer();
        if(p.hasPermission(Permissions.unverified)) {
            event.setCancelled(true);
            p.sendMessage(FormatChat.formatChat("&cYou must verify your UML email to do that! Type &bverify&c for help."));
        }
    }

}