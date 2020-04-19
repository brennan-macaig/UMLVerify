package edu.uml.cs.bmacaig.umlverify.commands;

import edu.uml.cs.bmacaig.umlverify.utils;
import java.util.regex.Pattern;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import edu.uml.cs.bmacaig.umlverify.UMLVerify;
import edu.uml.cs.bmacaig.umlverify.utils.FormatChat;
import edu.uml.cs.bmacaig.umlverify.utils.Permissions;

public class VerifyCMD implements CommandExecutor {
    @Override
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {

        if (sender instanceof Player)
	    {
            final Player player = (Player) sender;
            final Pattern IGNpat = Pattern.compile("[a-Z0-9_]{3,16}");

            // TODO: command name is not resolveable
            if ((player.hasPermission(Permissions.moderator) || player.hasPermission(Permissions.moderator))) 
            {
                if (args.length == 1)
                {
                   if (IGNpat.matcher(args[0]).matches()) {
                        String cmd = getPlugin().getConfig().getString("verification.promote-command"); // get the string
                        cmd = cmd.replaceAll("%user%", args[0]);
                        getServer().dispatchCommand(getServer().getConsoleSender(), cmd);
                    
                    }
                    else
                    {
                        player.sendMessage(FormatChat.formatChat("&dInvalid username entered!"));
                    }
                }
                else
                {
                    player.sendMessage(FormatChat.formatChat("&dUsage: /verify <username> <email>"));
                }
                else if (args.length == 2)
                {
                     if (IGNpat.matcher(args[0]).matches())
                    {
                        // check for email and start verify process
                    }
                    else
                    {
                        player.sendMessage(FormatChat.formatChat("&dInvalid username entered!"));
                    }
                    
                }
                else
                {
                    player.sendMessage(FormatChat.formatChat("&dUsage: /verify <username> <email>"));
                }
            }
        }
        
        return false;
    }
}