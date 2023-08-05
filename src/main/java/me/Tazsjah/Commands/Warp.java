package me.Tazsjah.Commands;

import me.Tazsjah.Data.Locations;
import me.Tazsjah.Data.Messages;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Warp implements CommandExecutor {

    Messages msgs;
    Locations locations;

    public Warp(Messages msgs, Locations locations) {
        this.msgs = msgs;
        this.locations = locations;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player player){
            if(args.length != 0) {
                if(player.hasPermission("ffa.warps." + args[0].toLowerCase()) || player.hasPermission("*")){
                    if(locations.checkLocation(args[0].toLowerCase())){
                        player.teleport(locations.getLocation(args[0]));
                        sender.sendMessage(ChatColor.GRAY + "Successfully warped to " + ChatColor.RED + args[0].toLowerCase());
                    }
                } else {
                    sender.sendMessage(ChatColor.RED + "You have no permission for this");
                }
            } else {
                sender.sendMessage(ChatColor.RED + "You must include a warp name to teleport to");
            }
        } else {
            sender.sendMessage(ChatColor.RED + "You must be a player to execute this command");
        }
        return true;
    }
}
