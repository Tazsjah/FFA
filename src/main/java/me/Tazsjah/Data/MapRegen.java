package me.Tazsjah.Data;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Integer.parseInt;

public class MapRegen {

    public Config config;
    Messages msgs;
    Locations locs;

    public MapRegen(Config config, Messages msgs, Locations locs) {
        this.config = config;
        this.msgs = msgs;
        this.locs = locs;
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
            player.teleport(locs.getLocation("spawn"));
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
                   warpPlayers();
                   restoreBlocks();
                   mapTime = (int) config.get("regen-map");
               }
           }
       }.runTaskTimer(Bukkit.getPluginManager().getPlugin("FFA"), 0, 20);
    }

    public void updateActionbar() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(msgs.getActionbar(mapTime)));
        }
    }


}
