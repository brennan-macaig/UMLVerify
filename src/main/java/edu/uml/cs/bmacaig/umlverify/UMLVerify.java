package edu.uml.cs.bmacaig.umlverify;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import org.bukkit.plugin.java.JavaPlugin;

import edu.uml.cs.bmacaig.umlverify.commands.UnverifyCMD;
import edu.uml.cs.bmacaig.umlverify.commands.VerifyCMD;
import edu.uml.cs.bmacaig.umlverify.eventhandlers.EventListener;
import edu.uml.cs.bmacaig.umlverify.utils.SendEmail;

/**
 * Hello world!
 *
 */
public class UMLVerify extends JavaPlugin {
    
    private SendEmail sendemail;
    public static Hashtable<String,String> issuedTokens;

    @Override
    public void onEnable() {
        final long startTime = System.nanoTime();
        this.sendemail = new SendEmail(this);
        issuedTokens = new Hashtable<String,String>();
        getLogger().info("Loading Config YML file...");
        this.saveDefaultConfig();
        getLogger().info("YML Files loaded. Registering commands...");
        this.getCommand("verify").setExecutor(new VerifyCMD(this, sendemail));
        this.getCommand("unverify").setExecutor(new UnverifyCMD(this));
        getLogger().info("Commands registered. Registering event listeners...");
        getServer().getPluginManager().registerEvents(new EventListener(sendemail, this), this);
        final long endTime = System.nanoTime();
        final long timeElapsed = endTime - startTime;
        final double seconds = (double) timeElapsed / 1000000000;
        getLogger().info("All done! Plugin enabled. Took a total of " + seconds + " seconds.");
    }

    @Override
    public void onDisable() {
    }
}
