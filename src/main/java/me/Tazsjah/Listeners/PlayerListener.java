package me.Tazsjah.Listeners;

import me.Tazsjah.Data.Config;
import me.Tazsjah.Data.Locations;
import me.Tazsjah.Data.Messages;
import me.Tazsjah.Data.PlayerData;
import me.Tazsjah.Utils.PlayerUtils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;


public class PlayerListener implements Listener {

    Config config;
    Messages msgs;
    PlayerData stats;
    PlayerUtils utils;
    Locations locations;

    public PlayerListener(Config config, Messages msgs, PlayerData stats, PlayerUtils utils, Locations locations) {
        this.config = config;
        this.msgs = msgs;
        this.stats = stats;
        this.utils = utils;
        this.locations = locations;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onJoin(PlayerJoinEvent event) {
        event.setJoinMessage(msgs.joinMsg(event.getPlayer()));
        stats.newPlayer(event.getPlayer());
        utils.heal(event.getPlayer());
        event.getPlayer().setGameMode(GameMode.valueOf(config.gamemode()));

        int in = config.title("in") * 20;
        int stay = config.title("stay") * 20;
        int out = config.title("out") * 20;

        event.getPlayer().sendTitle(msgs.title("title"), msgs.title("subtitle"), in , stay, out);
        event.getPlayer().teleport(locations.getLocation("spawn"));

        if(!event.getPlayer().hasPermission("ffa.admin")){

            event.getPlayer().getInventory().clear();
        }
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        event.setQuitMessage(msgs.leaveMsg(event.getPlayer()));
    }

    @EventHandler
    public void onHunger(FoodLevelChangeEvent event) {
        if(!config.hunger()) {
            event.getEntity().setFoodLevel(20);
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if(event.getEntity() instanceof Player player) {
            if(player.getInventory().isEmpty()) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        if(!event.getPlayer().hasPermission("ffa.drop")) {
            event.setCancelled(true);
        }
    }


}