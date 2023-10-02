package me.Tazsjah.Listeners;

import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import me.Tazsjah.Data.*;
import me.Tazsjah.Utils.PlayerUtils;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.inventory.ItemStack;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.UUID;

public class GameListener implements Listener {

    PlayerData data;
    PlayerUtils utils;
    Locations locations;
    Messages msgs;
    Config config;
    Combat combat;
    Kits kits;

    public GameListener(PlayerData data, PlayerUtils utils, Locations locations, Messages msgs, Config config, Combat combat, Kits kits) {
        this.data = data;
        this.utils = utils;
        this.locations = locations;
        this.msgs = msgs;
        this.config = config;
        this.combat = combat;
        this.kits = kits;
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
            LocalPlayer lp = WorldGuardPlugin.inst().wrapPlayer(p);
            RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
            RegionQuery query = container.createQuery();

            if(!query.testState(lp.getLocation(), lp, Flags.INVINCIBILITY)) {

                if (event.getDamager() instanceof EnderCrystal) {
                    playerKiller.put(event.getEntity().getUniqueId(), event.getDamager());
                }

                if (event.getDamager() instanceof Player) {
                    regularKiller.put(event.getEntity().getUniqueId(), event.getDamager().getUniqueId());
                    combat.tag((Player) event.getDamager());
                    combat.tag(p);
                }

                if (event.getDamager() instanceof Arrow) {
                    Arrow arrow = (Arrow) event.getDamager();
                    Player killer = (Player) arrow.getShooter();

                    regularKiller.put(event.getEntity().getUniqueId(), killer.getUniqueId());
                    combat.tag(killer);
                    combat.tag(p);
                }
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

    public String health(Player player) {
        double health1 = player.getHealth();
        DecimalFormat df = new DecimalFormat("0.00");
        df.setRoundingMode(RoundingMode.DOWN);
        String health2 = df.format(health1);
        return health2;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDeath(PlayerDeathEvent event){
        if(event.getDeathMessage().toLowerCase().contains("[intentional game design]") || event.getDeathMessage().toLowerCase().contains("fell out of the")){
            event.setDeathMessage(msgs.get("death-message").replace("$victim", event.getEntity().getName()));
        }

        event.getEntity().getInventory().clear();

        data.currentStreak.replace(event.getEntity().getUniqueId(), 0);
        data.addStat(event.getEntity(), "death");
        data.updateScoreboard(event.getEntity());
        combat.untag(event.getEntity());


        if(playerKiller.containsKey(event.getEntity().getUniqueId())){
            if(crystalKiller.get(playerKiller.get(event.getEntity().getUniqueId())) == null) {
                return;
            }

            Player killer = Bukkit.getPlayer(crystalKiller.get(playerKiller.get(event.getEntity().getUniqueId())));

            if(killer == event.getEntity()) {
                event.setDeathMessage(msgs.get("death-message").replace("$victim", event.getEntity().getName()));
                data.updateScoreboard(event.getEntity());
                return;
            }

            playerKiller.remove(event.getEntity());
            crystalKiller.remove(playerKiller.get(event.getEntity().getUniqueId()));
            killer.playSound(killer.getLocation(), Sound.ENTITY_WARDEN_DEATH, 2, 2);

            data.addStat(killer, "kill");
            data.addStat(killer, "streak");
            event.setDeathMessage(msgs.killMsg(event.getEntity(), killer, health(killer)));
            utils.heal(killer);
            cause.remove(event.getEntity().getUniqueId());

            data.updateScoreboard(event.getEntity());
            data.updateScoreboard(killer);

            return;
        }

        if(regularKiller.containsKey(event.getEntity().getUniqueId())) {
            Player killer = Bukkit.getPlayer(regularKiller.get(event.getEntity().getUniqueId()));

            if(killer == event.getEntity()) {
                event.setDeathMessage(msgs.get("death-message").replace("$victim", event.getEntity().getName()));
                data.updateScoreboard(event.getEntity());
                return;
            }

            regularKiller.remove(event.getEntity().getUniqueId());
            killer.playSound(killer.getLocation(), Sound.ENTITY_WARDEN_DEATH, 2, 2);

            data.addStat(killer, "kill");
            data.addStat(killer, "streak");
            event.setDeathMessage(msgs.killMsg(event.getEntity(), killer, health(killer)));
            utils.heal(killer);
            cause.remove(event.getEntity().getUniqueId());

            data.updateScoreboard(event.getEntity());
            data.updateScoreboard(killer);

            return;

        }

        if(cause.get(event.getEntity().getUniqueId()) == EntityDamageEvent.DamageCause.VOID) {
            event.setDeathMessage(msgs.voidMsg(event.getEntity()));
            cause.remove(event.getEntity().getUniqueId());
            data.updateScoreboard(event.getEntity());
            return;
        }

        if(cause.get(event.getEntity().getUniqueId()) == EntityDamageEvent.DamageCause.FALL) {
            event.setDeathMessage(msgs.fallMsg(event.getEntity()));
            cause.remove(event.getEntity().getUniqueId());
            data.updateScoreboard(event.getEntity());
            return;
        }

        event.setDeathMessage(msgs.get("death-message").replace("$victim", event.getEntity().getName()));
        data.updateScoreboard(event.getEntity());

    }

    @EventHandler
    public void onWeatherChange(WeatherChangeEvent event) {
        event.getWorld().setClearWeatherDuration(0);
        event.setCancelled(true);
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        if(regularKiller.get(event.getPlayer().getUniqueId()) != null) {
            Player killer = Bukkit.getPlayer(regularKiller.get(event.getPlayer().getUniqueId()));
        }
        event.setQuitMessage(msgs.leaveMsg(event.getPlayer()));
    }

}
