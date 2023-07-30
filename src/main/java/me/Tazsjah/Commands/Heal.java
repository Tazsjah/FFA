package me.Tazsjah.Commands;

import me.Tazsjah.Utils.PlayerUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Heal implements CommandExecutor {

    PlayerUtils utils;

    public Heal(PlayerUtils utils) {
        this.utils = utils;
    }


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender.hasPermission("ffa.heal")) {
            if(args.length != 0) {
                if(Bukkit.getPlayer(args[0]) != null){
                    utils.heal(Bukkit.getPlayer(args[0]));
                    Bukkit.getPlayer(args[0]).sendMessage(ChatColor.GRAY + "You have been healed");
                } else {
                    sender.sendMessage(ChatColor.RED + "This player does not exist");
                }
            } else {
                utils.heal((Player) sender);
                sender.sendMessage(ChatColor.GRAY + "You have been healed");
            }
        } else {
            sender.sendMessage(ChatColor.RED + "You have no permission for this");
        }
        return true;
    }
}
