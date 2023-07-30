package me.Tazsjah.Data;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;

public class Locations {

    File dir = new File(Bukkit.getPluginManager().getPlugin("FFA").getDataFolder() + "/Warps/");

    public void setLocation(Player player, String string, Location location) {
        File file = new File(dir, string.toLowerCase() + ".yml");
        if(!dir.exists()) {
            dir.mkdir();
        };
        if(!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            FileConfiguration object = YamlConfiguration.loadConfiguration(file);
            object.set("location", location);
            try {
                object.save(file);
                player.sendMessage(ChatColor.GRAY + "Successfully set location for " + ChatColor.RED + string.toLowerCase());
            } catch (IOException e) {
                player.sendMessage(ChatColor.RED + "Could not create location");
                throw new RuntimeException(e);
            }
        } else {
            player.sendMessage(ChatColor.RED + "This location exists already.");
        }
    }

    public Boolean checkLocation(String s) {
        File file = new File(dir, s.toLowerCase() + ".yml");
        FileConfiguration object = YamlConfiguration.loadConfiguration(file);

        if(!file.exists()){
            return false;
        }

        return true;
    }

    public Location getLocation(String string){

        File file = new File(dir, string.toLowerCase() + ".yml");
        FileConfiguration object = YamlConfiguration.loadConfiguration(file);

        if(!file.exists()){
            return null;
        }

        return object.getLocation("location");

    }

    public void removeLocation(Player p, String s){
        File file = new File(dir, s.toLowerCase() + ".yml");
        FileConfiguration object = YamlConfiguration.loadConfiguration(file);

        if(file.exists()){
            p.sendMessage(ChatColor.GRAY + "Location was removed");
            file.delete();
        } else {
            p.sendMessage(ChatColor.GRAY + "No location found with the name " + ChatColor.RED + s.toLowerCase());
        }
    }
}
