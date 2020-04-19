package edu.uml.cs.bmacaig.umlverify.commands;

import java.util.regex.Pattern;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import edu.uml.cs.bmacaig.umlverify.UMLVerify;
import edu.uml.cs.bmacaig.umlverify.utils.FormatChat;
import edu.uml.cs.bmacaig.umlverify.utils.Permissions;
import edu.uml.cs.bmacaig.umlverify.utils.SendEmail;

public class VerifyCMD implements CommandExecutor {
    @Override
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {

        if (sender instanceof Player)
	    {
            final Player player = (Player) sender;
            final Pattern IGNpat = Pattern.compile("[a-Z0-9_]{3,16}");
            final Pattern emailPat = Pattern.compile("^[a-zA-Z]+_[a-zA-Z]+[0-9]*@(student.){0,1}uml.edu$");

            // TODO: command name is not resolveable
            if ((player.hasPermission(Permissions.moderator) || player.hasPermission(Permissions.moderator))) 
            {
                if (args.length == 1)
                {
                    if (IGNpat.matcher(args[0]).matches())
                    {
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
            }
            else if (args.length == 2)
            {
                if (IGNpat.matcher(args[0]).matches())
                {
                    if (emailPat.matcher(args[1]).matches())
                    {
                        if (SendEmail.sendVerification(args[0], args[1]))
                        {
                            player.sendMessage(FormatChat.formatChat("&eEmail sent! Ask player to check their inbox for instructions"));
                        }
                        else
                        {
                            player.sendMessage(FormatChat.formatChat("&cFailed to send email. Please try again."));
                        }
                    }
                    else
                    {
                        player.sendMessage(FormatChat.formatChat("&dInvalid email entered!"));
                    }
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
        
        return false;
    }
}