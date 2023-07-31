package me.Tazsjah.Commands;

import me.Tazsjah.Data.Locations;
import me.Tazsjah.Data.Messages;
import me.Tazsjah.Data.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

public class Spawn implements CommandExecutor, Listener {

    Locations locations;
    Messages msgs;
    PlayerData data;

    public Spawn(Locations locations, Messages msgs) {
        this.locations = locations;
        this.msgs = msgs;
    }

    @Override

    public boolean onCommand(CommandSender sender, Command command, String arg, String[] args) {
        if(sender instanceof Player player) {
                if(locations.checkLocation("spawn")) {
                    ((Player) sender).teleport(locations.getLocation("spawn"));
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
