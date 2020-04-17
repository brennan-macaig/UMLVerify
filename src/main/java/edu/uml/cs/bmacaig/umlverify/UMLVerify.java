package edu.uml.cs.bmacaig.umlverify;

import org.bukkit.plugin.java.JavaPlugin;

import edu.uml.cs.bmacaig.umlverify.eventhandlers.EventListener;
import edu.uml.cs.bmacaig.umlverify.utils.Tokens;

/**
 * Hello world!
 *
 */
public class UMLVerify extends JavaPlugin {
    
    private Tokens tokens;


    @Override
    public void onEnable() {
        long startTime = System.nanoTime();
        getLogger().info("Loading Config YML file...");
        this.saveDefaultConfig();
        getLogger().info("Loading Tokens YML file...");
        tokens = new Tokens(this, "tokens.yml");
        tokens.saveDefaultTokens();
        getLogger().info("YML Files loaded. Registering commands...");
        getLogger().info("Commands registered. Registering event listeners...");
        getServer().getPluginManager().registerEvents(new EventListener(), this);
        long endTime = System.nanoTime();
        long timeElapsed = endTime - startTime;
        double seconds = (double) timeElapsed / 1000000000;
        getLogger().info("All done! Plugin enabled. Took a total of " + seconds + " seconds.");
        

    }

    @Override
    public void onDisable() {
        getLogger().info("Saving config");
        this.saveConfig();

    }
}
