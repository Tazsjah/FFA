package me.Tazsjah.Listeners;

import me.Tazsjah.Data.MapRegen;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import java.util.ArrayList;
import java.util.List;

public class MapListener implements Listener {


    MapRegen mapregen;

    public MapListener(MapRegen mapregen) {
        this.mapregen = mapregen;
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
                if(block.getType() != Material.AIR) {
                    mapregen.exploded.add(block.getWorld().getName() + "/" + block.getX() + "/" + block.getY() + "/" + block.getZ()  + ":" + block.getType());
                }
            }
        }
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        if(!mapregen.broken.contains(event.getBlock())) {
            mapregen.broken.add(event.getBlock().getWorld().getName() + "/" + event.getBlock().getX() + "/" + event.getBlock().getY() + "/" + event.getBlock().getZ() + ":" + event.getBlock().getType());
        }
    }



}
