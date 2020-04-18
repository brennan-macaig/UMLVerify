package edu.uml.cs.bmacaig.umlverify.commands;

import java.util.regex.Pattern;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import utils.Permissions.moderator

public class VerifyCMD implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (sender instanceof Player)
	    {
            Player player = (Player) sender;
            Pattern IGNpat = Pattern.compile("[a-Z0-9_]{3,16}");

            if ((player.hasPermission(utils.Permissions.moderator) || player.hasPermission(utils.Permissions.moderator)) && commandName.equals("verify"))
            {
                if (args.length == 2)
                {
                    //validate regular verify
                }
                else if (args.length == 1)
                {
                    if (IGNpat.matcher(args[0]).matches())
                    {
                        String cmd = getPlugin().getConfig().getString("verification.promote-command"); // get the string
                        cmd = cmd.replaceAll("%user%", String args[1])
                        getServer().dispatchCommand(getServer().getConsoleSender(), cmd);
                    
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
