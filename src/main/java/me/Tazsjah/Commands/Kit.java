package me.Tazsjah.Commands;

import me.Tazsjah.Data.Kits;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Kit implements CommandExecutor {

    Kits kits;

    public Kit(Kits kits) {
        this.kits = kits;
    }


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player player) {
            if (player.hasPermission("ffa.admin") || player.hasPermission("*")) {
                if (args.length == 0) {
                    player.sendMessage(ChatColor.RED + "You must include a name for the kit");
                } else {
                    if(args.length > 1) {
                        if (Bukkit.getPlayer(args[1]) == null) {
                            player.sendMessage(ChatColor.RED + "This player does not exist");
                        } else {
                            kits.giveKit(Bukkit.getPlayer(args[1]), args[0]);
                        }
                    } else {
                        kits.giveKit(player, args[0]);
                    }
                }
            } else {
                sender.sendMessage(ChatColor.RED + "You must be a player to execute this command");
            }
        }

        return false;
    }

}
