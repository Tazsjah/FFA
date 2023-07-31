package me.Tazsjah.Utils;

import me.Tazsjah.Data.Messages;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.List;

public class Sidebar {

    Messages msgs;

    public Sidebar(Messages msgs) {
        this.msgs = msgs;
    }

    public void setScore(Player player, Integer kills, Integer deaths, Integer tops, String kd, Integer streak) {
        Scoreboard board = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective obj = board.registerNewObjective("PvP", "dummy",
                ChatColor.translateAlternateColorCodes('&', msgs.get("scoreboard.title")));
        obj.setDisplaySlot(DisplaySlot.SIDEBAR);
        List<String> lines;

        if (streak == 0) {
            lines = (List<String>) msgs.scoreboard(player, kills, deaths, tops, kd, streak);
            int size  = lines.size();
            for (String linestring : lines) {
                size--;
                Team line = board.registerNewTeam("line" + size);
                line.addEntry(ChatColor.translateAlternateColorCodes('&', linestring));
                obj.getScore(ChatColor.translateAlternateColorCodes('&', linestring)).setScore(size);
            }
            player.setScoreboard(board);

        } else {
            lines = (List<String>) msgs.scoreboard(player, kills, deaths, tops, kd, streak);
            int size  = lines.size();
            for (String linestring : lines) {
                size--;
                Team line = board.registerNewTeam("line" + size);
                line.addEntry(ChatColor.translateAlternateColorCodes('&', linestring));
                obj.getScore(ChatColor.translateAlternateColorCodes('&', linestring)).setScore(size);
            }
            player.setScoreboard(board);
        }
    }
}
