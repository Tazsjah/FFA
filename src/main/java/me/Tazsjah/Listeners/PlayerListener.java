package me.Tazsjah.Listeners;

import me.Tazsjah.Data.*;
import me.Tazsjah.Utils.PlayerUtils;
import me.Tazsjah.Utils.Sidebar;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.*;


public class PlayerListener implements Listener {

    Config config;
    Messages msgs;
    PlayerData stats;
    PlayerUtils utils;
    Locations locations;
    Kits kits;
    Sidebar bar;
    Combat combat;

    public PlayerListener(Config config, Messages msgs,
                          PlayerData stats, PlayerUtils utils,
                          Locations locations, Sidebar bar,
                          Kits kits, Combat combat) {
        this.config = config;
        this.msgs = msgs;
        this.stats = stats;
        this.utils = utils;
        this.locations = locations;
        this.bar = bar;
        this.kits = kits;
        this.combat = combat;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onJoin(PlayerJoinEvent event) {
        event.setJoinMessage(msgs.joinMsg(event.getPlayer()));
        stats.newPlayer(event.getPlayer());
        utils.heal(event.getPlayer());
        event.getPlayer().getInventory().clear();
        event.getPlayer().setGameMode(GameMode.valueOf(config.gamemode()));

        int in = config.title("in") * 20;
        int stay = config.title("stay") * 20;
        int out = config.title("out") * 20;

        event.getPlayer().sendTitle(msgs.title("title"), msgs.title("subtitle"), in , stay, out);
        event.getPlayer().teleport(locations.getLocation("spawn"));

        stats.updateScoreboard(event.getPlayer());

        if(kits.ownKit(event.getPlayer())) {
            kits.getPlayerKit(event.getPlayer());
        }

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
    public void onRespawn(PlayerRespawnEvent event) {
        if(kits.ownKit(event.getPlayer())) {
            kits.getPlayerKit(event.getPlayer());
            event.setRespawnLocation(locations.getLocation("spawn"));
        }
    }


}
