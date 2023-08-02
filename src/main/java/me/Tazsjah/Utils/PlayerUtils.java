package me.Tazsjah.Utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class PlayerUtils {

    public void heal(Player player) {
        player.getActivePotionEffects().clear();
        player.setHealth(20);
        player.setFoodLevel(20);
        player.setFireTicks(0);
    }

    public void teleportTop(Player player) {
        int x = player.getLocation().getBlockX();
        int z = player.getLocation().getBlockZ();
        Block block = player.getWorld().getHighestBlockAt(x, z);
        Location loc = block.getLocation().add(0, 1, 0);
        player.teleport(loc);
    }

}
