package me.Tazsjah.Data;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.UUID;

public class PlayerData {

    public HashMap<UUID, Integer> currentStreak = new HashMap<>();

    public void newPlayer(Player player) {

        File f = new File(Bukkit.getPluginManager().getPlugin("FFA").getDataFolder() + "/Players/", player.getUniqueId() + ".yml");
        FileConfiguration playerfile = YamlConfiguration.loadConfiguration(f);

        if(!f.exists()) {
            playerfile.set("kills", 0);
            playerfile.set("deaths", 0);
            playerfile.set("top-streak", 0);
            currentStreak.put(player.getUniqueId(), 0);

            try {
                playerfile.save(f);
            } catch (IOException e) {
                Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Could not create player.yml for " + player.getName() + " [" + player.getUniqueId() + "]");
                throw new RuntimeException(e);
            }
        } else {

            currentStreak.put(player.getUniqueId(), 0);


        }
    } // Initializes players for the first time

    public void addStat(Player player, String s) {
        File f = new File(Bukkit.getPluginManager().getPlugin("FFA").getDataFolder() + "/Players/", player.getUniqueId() + ".yml");
        FileConfiguration playerfile = YamlConfiguration.loadConfiguration(f);
        if(!f.exists()) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Could not update stats for " + player.getName() + " [" + player.getUniqueId() + "]");
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "File does not exist");
        }

        if(s == "kill") {
            int kill = playerfile.getInt("kills");
            playerfile.set("kills", kill + 1);
        }
        if(s == "death") {
            int death = playerfile.getInt("deaths");
            playerfile.set("deaths", death + 1);
        }
        if(s == "streak") {
            if(currentStreak.get(player.getUniqueId()) == null) {
                currentStreak.put(player.getUniqueId(), 1);
            } else {
                int streak = currentStreak.get(player.getUniqueId());
                int top = playerfile.getInt("top-streak");

                if(streak >= top) {
                    playerfile.set("top-streak", streak);
                }

                currentStreak.put(player.getUniqueId(), streak + 1);
            }
        }

        try {
            playerfile.save(f);
        } catch (IOException e) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Could not update stats for " + player.getName() + " [" + player.getUniqueId() + "]");
            throw new RuntimeException(e);
        }

    } // Adds statistics for players

    public int getStat(Player player, String s) {
        File f = new File(Bukkit.getPluginManager().getPlugin("FFA").getDataFolder() + "/Players/", player.getUniqueId() + ".yml");
        FileConfiguration playerfile = YamlConfiguration.loadConfiguration(f);
        if(!f.exists()) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Could not get stats for " + player.getName() + " [" + player.getUniqueId() + "]");
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "File does not exist");
        }

        if (s == "kills") {
            return playerfile.getInt("kills");
        }

        if(s == "deaths") {
            return playerfile.getInt("deaths");
        }

        if(s == "streak") {
            return currentStreak.get(player.getUniqueId());
        }

        if(s == "topstreak") {
            return playerfile.getInt("top-streak");
        }

        return 0;
    } // Get statistics for players

    public void announceStreak(Player p) {

    }

    public String kd(Player player) {
        File f = new File(Bukkit.getPluginManager().getPlugin("FFA").getDataFolder() + "/Players/", player.getUniqueId() + ".yml");
        FileConfiguration playerfile = YamlConfiguration.loadConfiguration(f);
        if(!f.exists()) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Could not get stats for " + player.getName() + " [" + player.getUniqueId() + "]");
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "File does not exist");
        }

        DecimalFormat df = new DecimalFormat("0.00");
        if(playerfile.getInt("deaths") == 0) {
            double kdrn = playerfile.getInt("kills");
            df.setRoundingMode(RoundingMode.DOWN);
            String kdrn2 = df.format(kdrn);
            return kdrn2;
        } else {
            double kdrn = playerfile.getInt("kills") / playerfile.getInt("deaths");
            df.setRoundingMode(RoundingMode.DOWN);
            String kdrn2 = df.format(kdrn);
            return kdrn2;
        }
    }


}