package me.Tazsjah.Commands;

import me.Tazsjah.Data.Combat;
import me.Tazsjah.Data.Locations;
import me.Tazsjah.Data.Messages;
import me.Tazsjah.Data.PlayerData;
import me.Tazsjah.Utils.PlayerUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

public class Spawn implements CommandExecutor, Listener {

    Locations locations;
    Messages msgs;
    PlayerData data;
    PlayerUtils utils;
    Combat combat;

    public Spawn(Locations locations, Messages msgs, Combat combat, PlayerUtils utils) {
        this.locations = locations;
        this.msgs = msgs;
        this.combat = combat;
        this.utils = utils;
    }

    @Override

    public boolean onCommand(CommandSender sender, Command command, String arg, String[] args) {
        if(sender instanceof Player player) {
            if(combat.inCombat(player)) {
                if(!player.hasPermission("ffa.bypass") || player.hasPermission("*")) {player.sendMessage(msgs.get("in-combat"));return true;}

                if (locations.checkLocation("spawn")) {
                    ((Player) sender).teleport(locations.getLocation("spawn"));
                    utils.heal(player);
                } else {
                    sender.sendMessage(ChatColor.RED + "Spawn does not exist. Please set it using /setspawn");
                }
                return false;
            }

            if (locations.checkLocation("spawn")) {
                ((Player) sender).teleport(locations.getLocation("spawn"));
                utils.heal(player);
            } else {
                sender.sendMessage(ChatColor.RED + "Spawn does not exist. Please set it using /setspawn");
            }
        } else {
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "You must be a player to execute this");
        }
        return true;

    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onRespawn(PlayerRespawnEvent event) {
        if(locations.checkLocation("spawn")) {
            event.setRespawnLocation(locations.getLocation("spawn"));
        }
    }
}
