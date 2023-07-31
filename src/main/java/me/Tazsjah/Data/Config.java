package me.Tazsjah.Data;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Config {

    File f = new File(Bukkit.getPluginManager().getPlugin("FFA").getDataFolder(), "config.yml");
    FileConfiguration config = YamlConfiguration.loadConfiguration(f);


    public Boolean hunger() {
        return config.getBoolean("hunger");
    }

    public int arrow() {
        return config.getInt("arrows");
    }

    public String gamemode(){
        return config.getString("game-mode");
    }

    public int title(String s) {
        if (s == "in") {
            return config.getInt("in");
        }

        if(s == "stay") {
            return config.getInt("stay");
        }

        if(s == "out") {
            return config.getInt("out");
        }

        return 0;
    }

    public Boolean getInv(String s) {
        return config.getBoolean(s);
    }

}
