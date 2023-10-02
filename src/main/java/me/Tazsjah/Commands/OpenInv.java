package me.Tazsjah.Commands;

import me.Tazsjah.Data.Inventory;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class OpenInv implements CommandExecutor {

    Inventory inv;

    public OpenInv(Inventory inv) {
        this.inv = inv;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(args.length == 0) { sender.sendMessage(ChatColor.RED  + "You must include a name for the inventory.");return true; }

        inv.getInventory((Player) sender, args[0].toLowerCase());

        return false;
    }
}
