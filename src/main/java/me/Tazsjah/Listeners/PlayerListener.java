package me.Tazsjah.Listeners;

import me.Tazsjah.Data.*;
import me.Tazsjah.Utils.PlayerUtils;
import me.Tazsjah.Utils.Sidebar;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityResurrectEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.*;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;


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
        event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 1000000, 0));

        int in = config.title("in") * 20;
        int stay = config.title("stay") * 20;
        int out = config.title("out") * 20;

        event.getPlayer().sendTitle(msgs.title("title"), msgs.title("subtitle"), in , stay, out);
        event.getPlayer().teleport(locations.getLocation("spawn"));

        stats.updateScoreboard(event.getPlayer());

        if(stats.isKitSet(event.getPlayer())) {
            kits.getPlayerKit(event.getPlayer(), stats.mainKit(event.getPlayer()));
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
        if(stats.isKitSet(event.getPlayer())) {
            kits.getPlayerKit(event.getPlayer(), stats.mainKit(event.getPlayer()));
        } else {
            event.getPlayer().sendMessage(ChatColor.GRAY + "You do not have a main kit set. Please use /setkit");
        }

        Bukkit.getScheduler().runTaskLater(Bukkit.getPluginManager().getPlugin("FFA"), new Runnable() {
            @Override
            public void run() {
                event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 1000000, 0));
            }
        }, 2);
    }

    @EventHandler
    public void onResurrect(EntityResurrectEvent event) {
        if(event.getEntity() instanceof Player player) {
            Bukkit.getScheduler().runTaskLater(Bukkit.getPluginManager().getPlugin("FFA"), new Runnable() {
                @Override
                public void run() {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 1000000, 0));
                }
            }, 2);
        }
    }


}
