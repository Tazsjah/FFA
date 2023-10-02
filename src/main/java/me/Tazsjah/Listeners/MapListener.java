package me.Tazsjah.Listeners;

import me.Tazsjah.Data.Locations;
import me.Tazsjah.Data.MapRegen;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;

public class MapListener implements Listener {

    MapRegen mapregen;
    Locations locations;

    public MapListener(MapRegen mapregen, Locations locations) {
        this.mapregen = mapregen;
        this.locations = locations;
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        if(!mapregen.placed.contains(event.getBlockPlaced())) {
            if(!mapregen.config.getList("ignored-placed").contains(event.getBlock().getType())) {
                mapregen.placed.add(event.getBlockPlaced());
            }
        }
    }

    @EventHandler
    public void onExplode(BlockExplodeEvent event) {
        event.setYield(0);
        for (Block block : event.blockList()) {
            if(!mapregen.exploded.contains(block)) {
                if(block.getType() != Material.AIR && block.getType() != Material.FIRE) {
                    mapregen.exploded.add(block.getWorld().getName() + "/" + block.getX() + "/" + block.getY() + "/" + block.getZ()  + ":" + block.getType());
                }
            }
        }
    }

    @EventHandler
    public void onExplode2(EntityExplodeEvent event) {
        for (Block block : event.blockList()) {
            if(!mapregen.exploded.contains(block)) {
                if(block.getType() != Material.AIR && block.getType() != Material.FIRE) {
                    mapregen.exploded.add(block.getWorld().getName() + "/" + block.getX() + "/" + block.getY() + "/" + block.getZ()  + ":" + block.getType());
                }
            }
        }
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        Location spawn = locations.getLocation("spawn");
        if(!mapregen.broken.contains(event.getBlock())) {
            mapregen.broken.add(event.getBlock().getWorld().getName() + "/" + event.getBlock().getX() + "/" + event.getBlock().getY() + "/" + event.getBlock().getZ() + ":" + event.getBlock().getType());
        }
    }



}
