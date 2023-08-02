package me.Tazsjah.Commands;

import me.Tazsjah.Data.Inventory;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetInv implements CommandExecutor {

    Inventory inv;
    public SetInv(Inventory inv) {
        this.inv = inv;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player player) {
            if(!player.hasPermission("ffa.admin")) { sender.sendMessage(ChatColor.RED  + "You have no permission for this");return true;}
            if(args.length == 0) { sender.sendMessage(ChatColor.RED  + "You must include a name for the inventory.");return true; }
            if(player.getTargetBlock(null, 5).getType() == Material.CHEST) {
                if(inv.invExists(args[0])) {
                    Chest chest = (Chest) player.getTargetBlock(null, 5).getState();
                    org.bukkit.inventory.Inventory inventory = chest.getInventory();
                    inv.saveInventory(player, args[0], inventory);
                } else {
                    sender.sendMessage(ChatColor.RED + "That inventory does not exist.");
                }
            } else {
                sender.sendMessage("You must be looking at a chest for this");
            }
        } else {
            sender.sendMessage(ChatColor.RED + "You must be a player to execute this command");
        }
        return false;
    }
}
