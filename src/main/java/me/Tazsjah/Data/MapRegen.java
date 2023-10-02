package me.Tazsjah.Data;

import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import me.Tazsjah.Utils.PlayerUtils;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Integer.parseInt;

public class MapRegen {

    public Config config;
    Messages msgs;
    Locations locs;
    PlayerUtils util;

    Combat combat;

    Kits kits;

    public MapRegen(Config config, Messages msgs, Locations locs, Combat combat, PlayerUtils util, Kits kits) {
        this.config = config;
        this.msgs = msgs;
        this.locs = locs;
        this.combat = combat;
        this.util = util;
        this.kits = kits;
    }

    public List<Block> placed = new ArrayList<>();
    public List<String> exploded = new ArrayList<>();
    public List<String> broken = new ArrayList<>();


    public void restoreBlocks() {
        for(Block block : placed) {
            block.setType(Material.AIR);
        }

        for(String block : broken) {

            String[] blockinfo = block.split(":");
            String[] blockloc = blockinfo[0].split("/");
            Location loc = new Location(Bukkit.getWorld(blockloc[0]), parseInt(blockloc[1]), parseInt(blockloc[2]), parseInt(blockloc[3]));
            Material mat = Material.getMaterial(blockinfo[1]);

            loc.getBlock().setType(mat);

        }

        for(String block : exploded) {

            String[] blockinfo = block.split(":");
            String[] blockloc = blockinfo[0].split("/");
            Location loc = new Location(Bukkit.getWorld(blockloc[0]), parseInt(blockloc[1]), parseInt(blockloc[2]), parseInt(blockloc[3]));
            Material mat = Material.getMaterial(blockinfo[1]);

            loc.getBlock().setType(mat);
        }
    }

    public int mapTime = 0;
    public void startMapTime() {
        mapTime = (int) config.get("regen-map");
        startTime();
    }

    public void warpPlayers() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            LocalPlayer lp = WorldGuardPlugin.inst().wrapPlayer(player);
            RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
            RegionQuery query = container.createQuery();

            Location loc = player.getLocation();
            loc.setY(116);

            if(!query.testState(lp.getLocation(), lp, Flags.INVINCIBILITY)) {
                player.teleport(loc);
                player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 1000000, 0));
                if(kits.data.isKitSet(player)) {
                    player.getPlayer().getInventory().clear();
                    kits.getPlayerKit(player, kits.data.mainKit(player));
                }
            }
        }
    }

    public void startTime() {
        new BukkitRunnable() {
            @Override
            public void run() {
                if(mapTime != 0) {
                    mapTime--;
                    updateActionbar();
                } else {
                    updateActionbar();
                    warpPlayers();
                    restoreBlocks();
                    mapTime = (int) config.get("regen-map");
                }
            }
        }.runTaskTimer(Bukkit.getPluginManager().getPlugin("FFA"), 0, 20);
    }

    public void updateActionbar() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(msgs.getActionbar(mapTime)));
        }
    }


}
