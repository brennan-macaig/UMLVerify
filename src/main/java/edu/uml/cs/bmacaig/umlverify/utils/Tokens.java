package edu.uml.cs.bmacaig.umlverify.utils;

import java.io.File;
import java.io.IOException;

import java.io.InputStreamReader;
import java.io.Reader;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class Tokens {

    private final String fileName;
    private final JavaPlugin plugin;

    private File tokensFile;
    private FileConfiguration tokens;

    public Tokens(JavaPlugin plugin, String fileName) {
        if(plugin == null) {
            throw new IllegalArgumentException("plugin cannot be null");
        }
        this.plugin = plugin;
        this.fileName = fileName;
        File dataFolder = plugin.getDataFolder();
        if(dataFolder == null) {
            throw new IllegalStateException();
        }
        this.tokensFile = new File(plugin.getDataFolder(), fileName);
    }

    public void reloadTokens() {
        if (tokensFile == null) {
            tokensFile = new File(plugin.getDataFolder(), "tokens.yml");
        }
        tokens = YamlConfiguration.loadConfiguration(tokensFile);

        Reader defTokensStream = new InputStreamReader(plugin.getResource(fileName));
        if (defTokensStream != null) {
            YamlConfiguration defToken = YamlConfiguration.loadConfiguration(defTokensStream);
            tokens.setDefaults(defToken);
        }
    }

    public FileConfiguration getTokens() {
        if(tokens == null) {
            this.reloadTokens();
        }
        return tokens;
    }

    public void saveTokens() {
        if (tokens == null || tokensFile == null) {
            return;
        }
        try {
            getTokens().save(tokensFile);
        } catch (IOException e) {
            e.printStackTrace();
            plugin.getLogger().severe("Could not save tokens file to " + tokensFile);
        }
    }

    public void saveDefaultTokens() {
        if (!tokensFile.exists()) {
            this.plugin.saveResource(fileName, false);
        }
    }
}