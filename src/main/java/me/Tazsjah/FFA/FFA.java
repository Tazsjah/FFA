package me.Tazsjah.FFA;

import me.Tazsjah.Commands.*;
import me.Tazsjah.Data.*;
import me.Tazsjah.Listeners.GameListener;
import me.Tazsjah.Listeners.PlayerListener;
import me.Tazsjah.Utils.PlayerUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class FFA extends JavaPlugin {
    public void onEnable() {

        // Create default files

        File f = new File(Bukkit.getPluginManager().getPlugin("FFA").getDataFolder(), "config.yml");

        File m = new File(Bukkit.getPluginManager().getPlugin("FFA").getDataFolder(), "messages.yml");


        if(!f.exists()) {this.saveResource("config.yml", false);}
        if(!m.exists()) {this.saveResource("messages.yml", false);}

        // Initialize classes & call instances

        Config config = new Config();

        Locations locations = new Locations();
        Messages messages = new Messages();
        PlayerData stats = new PlayerData();
        PlayerUtils utils = new PlayerUtils();
        Spawn spawn = new Spawn(locations, messages);

        // Register Events

        Bukkit.getPluginManager().registerEvents(new PlayerListener(config, messages, stats, utils, locations), this);
        Bukkit.getPluginManager().registerEvents(new GameListener(stats, utils, locations, messages, config), this);
        Bukkit.getPluginManager().registerEvents(spawn, this);

        // Register Commands

        Bukkit.getPluginCommand("spawn").setExecutor(spawn);
        Bukkit.getPluginCommand("setwarp").setExecutor(new SetWarp(locations));
        Bukkit.getPluginCommand("delwarp").setExecutor(new DelWarp(locations));
        Bukkit.getPluginCommand("heal").setExecutor(new Heal(utils));
        Bukkit.getPluginCommand("warp").setExecutor(new Warp(messages, locations));
        Bukkit.getPluginCommand("top").setExecutor(new Top(utils));
        Bukkit.getPluginCommand("stats").setExecutor(new Stats(messages, stats));

        // Misc

        Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "Enabling the FFA plugin by Tazsjah");
        
    }

    public void onDisable() {
        Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Disabling the FFA plugin by Tazsjah");
    }

}
