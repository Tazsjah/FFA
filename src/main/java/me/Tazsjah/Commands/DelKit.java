package me.Tazsjah.Commands;

import me.Tazsjah.Data.Kits;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DelKit implements CommandExecutor {

    Kits kits;

    public DelKit(Kits kits) {
        this.kits = kits;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player player) {
            if(player.hasPermission("ffa.admin") || player.hasPermission("*")) {
                if(args.length == 0) {
                    player.sendMessage(ChatColor.RED + "You must include a name for the kit");
                } else {
                    kits.deleteKit(player, args[0]);
                }
            }
        } else {
            sender.sendMessage(ChatColor.RED + "You must be a player to execute this command");
        }

        return false;
    }
}
