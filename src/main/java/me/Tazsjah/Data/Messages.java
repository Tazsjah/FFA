package me.Tazsjah.Data;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
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


}
