package me.Tazsjah.Commands;

import me.Tazsjah.Data.Kits;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetKit implements CommandExecutor {
    Kits kits;

    public SetKit(Kits kits) {
        this.kits = kits;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(sender instanceof Player player) {
            if(args.length > 0) {
                if(args[0].equalsIgnoreCase("clear")) {
                    kits.clearKit(player);
                } else {
                    if(kits.kitList(player).toString().contains(args[0] + ".yml")) {
                        kits.setKit(player, args[0]);
                    } else {
                        sender.sendMessage(ChatColor.GRAY + "You do not own a kit named " + ChatColor.RED + args[0].toLowerCase());
                    }
                }
            } else {
                player.sendMessage(ChatColor.RED + "You must include what kit to set as your main");
                if(kits.kitList(player) == null) {
                    player.sendMessage(ChatColor.GRAY + "You have no kits available. Use /savekit (name)");
                    return true;
                }

                player.sendMessage(ChatColor.GRAY + "Available Kits: " + kits.kitList(player).toString()
                        .replace(".yml", "")
                        .replace("]", "").replace("[", ""));
            }
        } else {
            sender.sendMessage(ChatColor.RED + "You must be a player to execute this");
        }

        return false;
    }
}
