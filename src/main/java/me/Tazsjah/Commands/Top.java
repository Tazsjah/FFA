package me.Tazsjah.Commands;

import me.Tazsjah.Utils.PlayerUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Top implements CommandExecutor {

    PlayerUtils utils;

    public Top(PlayerUtils utils) {
        this.utils = utils;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player player) {
            if(player.hasPermission("ffa.admin") || player.hasPermission("*")) {
                utils.teleportTop(((Player) sender).getPlayer());
            } else {
                sender.sendMessage(ChatColor.RED + "You have no permission for this");
            }
        } else {
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "You must be a player to execute this command");
        }
        return true;
    }
}
