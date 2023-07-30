package me.Tazsjah.Commands;

import me.Tazsjah.Data.Locations;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DelWarp implements CommandExecutor {

    Locations locations;

    public DelWarp(Locations locations) {
        this.locations = locations;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player player) {
            if(player.hasPermission("ffa.admin")) {
                if(args.length != 0) {
                    locations.removeLocation(((Player) sender).getPlayer(), args[0]);
                } else {
                    sender.sendMessage(ChatColor.RED + "You must include a warp name");
                }
            } else {
                sender.sendMessage(ChatColor.RED + "You have no permission for this");
            }
        } else {
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "You must be a player to execute this command");
        }
        return true;
    }
}
