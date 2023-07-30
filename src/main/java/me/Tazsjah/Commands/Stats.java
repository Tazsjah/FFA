package me.Tazsjah.Commands;

import me.Tazsjah.Data.Messages;
import me.Tazsjah.Data.PlayerData;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Stats implements CommandExecutor {

    Messages msgs;
    PlayerData data;

    public Stats(Messages msgs, PlayerData data) {
        this.msgs = msgs;
        this.data = data;
    }


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(sender instanceof Player) {
            int kills = data.getStat((Player) sender, "kills");
            int deaths = data.getStat((Player) sender, "deaths");
            String kd = data.kd((Player) sender);
            int tops = data.getStat((Player) sender, "topstreak");

            for (String s : msgs.stats((Player) sender, kills, deaths, tops, kd)) {
                sender.sendMessage(s);
            }


        } else {
            sender.sendMessage(ChatColor.RED + "You must be a player to use this command");
        }

        return false;

    }
}
