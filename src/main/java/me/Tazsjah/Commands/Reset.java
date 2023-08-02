package me.Tazsjah.Commands;

import me.Tazsjah.Data.Kits;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Reset implements CommandExecutor {

    Kits kits;

    public Reset(Kits kits) {
        this.kits = kits;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player player) {
            if (kits.ownKit(player)) {
                kits.deletePlayerKit(player);
            } else {
                player.sendMessage(ChatColor.RED + "You have no kit set");
            }
        } else {
            sender.sendMessage(ChatColor.RED + "You must be a player to execute this");
        }
        return false;
    }
}
