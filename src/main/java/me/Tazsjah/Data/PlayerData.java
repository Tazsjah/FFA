package me.Tazsjah.Data;

import me.Tazsjah.Utils.Sidebar;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

import java.io.File;
import java.io.IOException;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.UUID;

public class PlayerData {

    Sidebar bar;
    Config config;
    Messages msgs;

    public PlayerData(Sidebar bar, Config config, Messages msgs) {
        this.bar = bar;
        this.config = config;
        this.msgs = msgs;
    }

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
            if(currentStreak.get(player.getUniqueId()) == 0) {
                currentStreak.put(player.getUniqueId(), 1);
            } else {
                int streak = currentStreak.get(player.getUniqueId());
                int top = playerfile.getInt("top-streak");

                currentStreak.put(player.getUniqueId(), streak + 1);

                announceStreak(player);

                if(streak >= top) {
                    playerfile.set("top-streak", streak + 1);
                }
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
        if(config.getList("streak-ms").contains(getStat(p, "streak"))) {
            for (Object s : msgs.getList("streak-msg")) {
                String l = (String) s;
                Bukkit.broadcastMessage(l.replace("$player", p.getName()).replace("$streak", getStat(p, "streak") + ""));
            }
        }
    }

    public void initializeAll() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            newPlayer(player);
        }
    }

    public void updateScoreboard(Player player) {
        int kills = getStat(player, "kills");
        int deaths = getStat(player, "deaths");
        String kd = kd(player);
        int tops = getStat(player, "topstreak");

        if(currentStreak.get(player.getUniqueId()) == 0) {
            bar.setScore(player, kills, deaths, tops, kd, 0);
            return;
        }
        bar.setScore(player, kills, deaths, tops, kd, currentStreak.get(player.getUniqueId()));
    }

    public String kd(Player player) {

        DecimalFormat df = new DecimalFormat("0.00");
        if(getStat(player,"deaths") == 0) {
            double kdrn = getStat(player, "kills");
            df.setRoundingMode(RoundingMode.DOWN);
            String kdrn2 = df.format(kdrn);
            return kdrn2;
        } else {
            double kdrn = (double) getStat(player, "kills") / getStat(player, "deaths");
            df.setRoundingMode(RoundingMode.DOWN);
            String kdrn2 = df.format(kdrn);
            return kdrn2;
        }
    }

    @EventHandler
    public void kitSet(Player p, String s) {
        File f = new File(Bukkit.getPluginManager().getPlugin("FFA").getDataFolder() + "/Players/", p.getUniqueId() + ".yml");
        FileConfiguration playerfile = YamlConfiguration.loadConfiguration(f);

        playerfile.set("kit", s.toLowerCase());
        p.sendMessage(ChatColor.GRAY + "You have set your main kit to " + s);

        try {
            playerfile.save(f);
        } catch (IOException e) {
            p.sendMessage(ChatColor.RED + "Could not set your kit. Please report this issue to admins");
            throw new RuntimeException(e);
        }

    }

    public String mainKit(Player p) {
        File f = new File(Bukkit.getPluginManager().getPlugin("FFA").getDataFolder() + "/Players/", p.getUniqueId() + ".yml");
        FileConfiguration playerfile = YamlConfiguration.loadConfiguration(f);

        return playerfile.getString("kit");
    }

    public Boolean isKitSet(Player p) {
        File f = new File(Bukkit.getPluginManager().getPlugin("FFA").getDataFolder() + "/Players/", p.getUniqueId() + ".yml");
        FileConfiguration playerfile = YamlConfiguration.loadConfiguration(f);

        if(playerfile.getString("kit") != null) {
            return true;
        }

        return false;
    }

    public void clearKit(Player player) {
        File f = new File(Bukkit.getPluginManager().getPlugin("FFA").getDataFolder() + "/Players/", player.getUniqueId() + ".yml");
        FileConfiguration playerfile = YamlConfiguration.loadConfiguration(f);

        if(playerfile.getString("kit") != null) {
            playerfile.set("kit", null);
            player.sendMessage(ChatColor.GRAY + "You have cleared your main kit");

            try {
                playerfile.save(f);
            } catch (IOException e) {
                player.sendMessage(ChatColor.RED + "Could not clear your kit. Please report this issue to admins");
                throw new RuntimeException(e);
            }
        } else {
            player.sendMessage(ChatColor.RED + "No main kit found");
        }

    }


}
