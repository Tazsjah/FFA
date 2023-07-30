package me.Tazsjah.Listeners;

import me.Tazsjah.Data.Config;
import me.Tazsjah.Data.Locations;
import me.Tazsjah.Data.Messages;
import me.Tazsjah.Data.PlayerData;
import me.Tazsjah.Utils.PlayerUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.EnderCrystal;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.UUID;

public class GameListener implements Listener {

    PlayerData data;
    PlayerUtils utils;
    Locations locations;
    Messages msgs;

    Config config;

    public GameListener(PlayerData data, PlayerUtils utils, Locations locations, Messages msgs, Config config) {
        this.data = data;
        this.utils = utils;
        this.locations = locations;
        this.msgs = msgs;
        this.config = config;
    }


    HashMap<Entity, UUID> crystalKiller = new HashMap<Entity, UUID>();
    HashMap<Location, UUID> blockOwner = new HashMap<>();
    HashMap<UUID, Entity> playerKiller = new HashMap<UUID, Entity>();
    HashMap<UUID, UUID> regularKiller = new HashMap<>();
    HashMap<UUID, EntityDamageEvent.DamageCause> cause = new HashMap<>();


    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if(event.getPlayer().getTargetBlock(null, 5).getType() == Material.RESPAWN_ANCHOR) {
            if (event.getPlayer().getInventory().getItemInMainHand().getType() == Material.GLOWSTONE) {
                if (blockOwner.get(event.getClickedBlock().getLocation()) == null) {
                    Block block = event.getClickedBlock();
                    blockOwner.put(block.getLocation(), event.getPlayer().getUniqueId());
                }
            }
        }
    }

    @EventHandler
    public void onExplosion(BlockExplodeEvent event) {
        Player player = Bukkit.getPlayer(blockOwner.get(event.getBlock().getLocation()));
        for (Entity e : event.getBlock().getWorld().getNearbyEntities(event.getBlock().getLocation(),5,5,5)) {
            if (e instanceof Player) {
                Player p = (Player) e;
                regularKiller.put(p.getUniqueId(), player.getUniqueId());
            }
        }
        blockOwner.remove(event.getBlock().getLocation());
    }

    @EventHandler
    public void onHit(EntityDamageByEntityEvent event){
        if(event.getEntity() instanceof EnderCrystal){
            if (event.getDamager() instanceof Player) {
                crystalKiller.put(event.getEntity(), event.getDamager().getUniqueId());
            }
            if (event.getDamager() instanceof Arrow) {
                Arrow arrow = (Arrow) event.getDamager();
                Player p = (Player) arrow.getShooter();
                crystalKiller.put(event.getEntity(), p.getUniqueId());
            }
        }
        if(event.getEntity() instanceof Player p) {

            if(event.getDamager() instanceof EnderCrystal){
                playerKiller.put(event.getEntity().getUniqueId(), event.getDamager());
            }

            if(event.getDamager() instanceof Player) {
                regularKiller.put(event.getEntity().getUniqueId(), event.getDamager().getUniqueId());
            }

            if(event.getDamager() instanceof Arrow) {
                Arrow arrow = (Arrow) event.getDamager();
                Player killer = (Player) arrow.getShooter();

                regularKiller.put(event.getEntity().getUniqueId(), killer.getUniqueId());
            }

        }

    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if(event.getEntity() instanceof Player player) {
            if(cause.containsKey(player.getUniqueId())) {
                cause.replace(player.getUniqueId(), event.getCause());
                return;
            }

            cause.put(player.getUniqueId(), event.getCause());
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event){
        event.getEntity().getInventory().clear();
        data.currentStreak.replace(event.getEntity().getUniqueId(), 0);

        if(playerKiller.containsKey(event.getEntity().getUniqueId())){
            if(crystalKiller.get(playerKiller.get(event.getEntity().getUniqueId())) == null) {return;}

            Player killer = Bukkit.getPlayer(crystalKiller.get(playerKiller.get(event.getEntity().getUniqueId())));
            playerKiller.remove(event.getEntity());
            crystalKiller.remove(playerKiller.get(event.getEntity().getUniqueId()));
            if(killer == event.getEntity()) {return;}

            if(killer.getInventory().contains(Material.BOW)){
                ItemStack arrow = new ItemStack(Material.ARROW);
                arrow.setAmount(config.arrow());
                killer.getInventory().addItem(arrow);
            }

            data.addStat(event.getEntity(), "death");
            data.addStat(killer, "kill");
            data.addStat(killer, "streak");
            event.setDeathMessage(msgs.killMsg(event.getEntity(), killer, killer.getHealth()));
            utils.heal(killer);
            cause.remove(event.getEntity().getUniqueId());

            return;
        }

        if(regularKiller.containsKey(event.getEntity().getUniqueId())) {
            Player killer = Bukkit.getPlayer(regularKiller.get(event.getEntity().getUniqueId()));

            regularKiller.remove(event.getEntity().getUniqueId());

            if(regularKiller.get(event.getEntity()) == event.getEntity().getUniqueId()) {return;}

            if(killer.getInventory().contains(Material.BOW)){
                ItemStack arrow = new ItemStack(Material.ARROW);
                arrow.setAmount(config.arrow());
                killer.getInventory().addItem(arrow);
            }

            data.addStat(event.getEntity(), "death");
            data.addStat(killer, "kill");
            data.addStat(killer, "streak");
            event.setDeathMessage(msgs.killMsg(event.getEntity(), killer, killer.getHealth()));
            utils.heal(killer);
            cause.remove(event.getEntity().getUniqueId());

            killer.sendMessage(data.getStat(killer, "streak") + "Streak");

            return;

        }

        if(cause.get(event.getEntity().getUniqueId()) == EntityDamageEvent.DamageCause.VOID) {
            event.setDeathMessage(msgs.voidMsg(event.getEntity()));
            data.addStat(event.getEntity(), "death");
            cause.remove(event.getEntity().getUniqueId());
        }

        if(cause.get(event.getEntity().getUniqueId()) == EntityDamageEvent.DamageCause.FALL) {
            event.setDeathMessage(msgs.fallMsg(event.getEntity()));
            data.addStat(event.getEntity(), "death");
            cause.remove(event.getEntity().getUniqueId());
        }
    }

    @EventHandler
    public void onMobs(EntitySpawnEvent event) {
        if(!(event.getEntity() instanceof Player)) {
            if(!config.mobs()) {
                if(!config.ignoredMobs().contains(event.getEntity().getType().toString())) {
                    event.getEntity().remove();
                    event.setCancelled(true);
                }

            }
        }
    }

    @EventHandler
    public void onWeatherChange(WeatherChangeEvent event) {
        event.getWorld().setClearWeatherDuration(0);
        event.setCancelled(true);
    }



}
