package me.Tazsjah.Data;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.StringUtil;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class Inventory {
    File f = new File(Bukkit.getPluginManager().getPlugin("FFA").getDataFolder() + "/Inventories/");

    Messages msgs;

    public Inventory(Messages msgs) {
        this.msgs = msgs;
    }
    public void saveInventory(Player player, String s, org.bukkit.inventory.Inventory inventory) {

        File invfile = new File(f, s.toLowerCase() + ".yml");
        FileConfiguration inv = YamlConfiguration.loadConfiguration(invfile);

        inv.set("inventory", inventory.getContents());

        try {
            inv.save(invfile);
            player.sendMessage(ChatColor.GRAY + "Inventory has been saved. " + s.toLowerCase());
        } catch (IOException e) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Could not save inventory");
            throw new RuntimeException(e);
        }


    }

    public void getInventory(Player player, String s) {

        org.bukkit.inventory.Inventory inv = Bukkit.createInventory(null, 54, msgs.get("shop-prefix") + s.substring(0, 1).toUpperCase() + s.substring(1));
        addItems(inv, s);
        player.openInventory(inv);

        if(s.contains("gear")) {
            player.playSound(player.getLocation(), Sound.BLOCK_BONE_BLOCK_BREAK, 1, 1.5f);
        }

        if(s.contains("effects")) {
            player.playSound(player.getLocation(), Sound.BLOCK_BREWING_STAND_BREW, 1, 1.5f);
        }

    }

    public void addItems(org.bukkit.inventory.Inventory inventory, String s) {

        File invfile = new File(f, s.toLowerCase() + ".yml");
        FileConfiguration inv = YamlConfiguration.loadConfiguration(invfile);

        List<ItemStack> items = (List<ItemStack>) inv.getList("inventory");

        for(ItemStack item : items) {
            inventory.addItem(item);
        }

    }

    public Boolean invExists(String s) {
        File invfile = new File(f, s.toLowerCase() + ".yml");
        FileConfiguration inv = YamlConfiguration.loadConfiguration(invfile);

        if(invfile.exists()) {
            return true;
        }

        return false;
    }

}
