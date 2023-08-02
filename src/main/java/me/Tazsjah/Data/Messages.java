package me.Tazsjah.Data;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import java.io.File;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class Messages {

    File f = new File(Bukkit.getPluginManager().getPlugin("FFA").getDataFolder(), "messages.yml");
    FileConfiguration msgs = YamlConfiguration.loadConfiguration(f);


    public List<Integer> streaks() {
        return (List<Integer>) msgs.get("streak-ms");
    }

    public String killMsg(Player v, Player k, Double f) { // Variables are $victim | $left | $killer
        return ChatColor.translateAlternateColorCodes('&',msgs.getString("kill-message").replace("$victim", v.getName()).replace("$killer", k.getName()).replace("$left", f.toString()));
    }

    public String joinMsg(Player player) {
        return ChatColor.translateAlternateColorCodes('&', msgs.getString("join-message").replace("$player", player.getName()));
    }

    public String leaveMsg(Player player) {
        return ChatColor.translateAlternateColorCodes('&', msgs.getString("leave-message").replace("$player", player.getName()));
    }

    public String voidMsg(Player player) {
        return ChatColor.translateAlternateColorCodes('&', msgs.getString("void-message").replace("$victim", player.getName()));
    }

    public String fallMsg(Player player) {
        return ChatColor.translateAlternateColorCodes('&', msgs.getString("fall-message").replace("$victim", player.getName()));
    }

    public String title(String s) {
        if (s == "title"){
            return ChatColor.translateAlternateColorCodes('&', msgs.getString("title"));
        }
        if(s == "subtitle"){
            return ChatColor.translateAlternateColorCodes('&', msgs.getString("subtitle"));
        }

        return null;
    }

    public List<String> stats(Player player, Integer kills, Integer deaths, Integer tops, String kd) {
        List<String> v1 = (List<String>) msgs.getList("stats");
        List<String> v2 = new ArrayList<>();
        for(String s : v1) {
            v2.add(ChatColor.translateAlternateColorCodes('&', s.replace("$player", player.getName())
                    .replace("$kills", kills + "")
                    .replace("$deaths", deaths + "")
                    .replace("$highest-streak", tops + "")
                    .replace("$kd", kd + "")));
        }
        return v2;
    }

    public String get(String s) {
        return ChatColor.translateAlternateColorCodes('&', msgs.getString(s));
    }


    public List<String> scoreboard(Player player, Integer kills, Integer deaths, Integer tops, String kd, Integer streak) {
        if(streak == 0) {
            List<String> scorelist = new ArrayList<>();
            for(String s : msgs.getStringList("scoreboard.without-streak-board")) {
                scorelist.add(ChatColor.translateAlternateColorCodes('&',
                        s.replace("$player", player.getName())
                                .replace("$kills", kills + "")
                                .replace("$deaths", deaths + "")
                                .replace("$kdr", kd)
                                .replace("$highest-streak", tops + "")
                ));
            }

            return scorelist;

        }

        List<String> scorelist = new ArrayList<>();
        for(String s : msgs.getStringList("scoreboard.with-streak-board")) {
            scorelist.add(ChatColor.translateAlternateColorCodes('&',
                    s.replace("$player", player.getName())
                            .replace("$kills", kills + "")
                            .replace("$deaths", deaths + "")
                            .replace("$kdr", kd)
                            .replace("$highest-streak", tops + "")
                            .replace("$current-streak", streak + "")
            ));
        }
        return scorelist;
    }

    public String getActionbar(int i) {
        Duration duration = Duration.ofSeconds(i);
        String d = duration + "";
        return ChatColor.translateAlternateColorCodes
                ('&', msgs.getString("action-bar").replace("$secs", d.replace("PT", "")
                .replace("M", "m ").replace("S", "s ")));
    }


}
