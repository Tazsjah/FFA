package me.Tazsjah.Commands;

import me.Tazsjah.Data.Kits;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RemKit implements CommandExecutor {

    Kits kits;

    public RemKit(Kits kits) {
        this.kits = kits;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player player) {
            if(args.length > 0) {
                    kits.deletePlayerKit(player, args[0]);
            } else {
                player.sendMessage(ChatColor.RED + "You must specify which kit to delete");
            }
        } else {
            sender.sendMessage(ChatColor.RED + "You must be a player to execute this");
        }
        return false;
    }
}
