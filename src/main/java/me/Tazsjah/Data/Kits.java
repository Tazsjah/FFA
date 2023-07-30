package me.Tazsjah.Data;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Kits {

    File f = new File(Bukkit.getPluginManager().getPlugin("FFA").getDataFolder() + "/Kits/");

    public void createKit(Player p, String s) {
        File kitfile = new File(f, s.toLowerCase() + ".yml");
        FileConfiguration kit = YamlConfiguration.loadConfiguration(kitfile);

        if(!kitfile.exists()){
            if(p.getInventory().getArmorContents() != null) { // This sets the armor for the kit
                if(p.getInventory().getHelmet() != null) {
                    kit.set("helmet", p.getInventory().getHelmet());
                }
                if(p.getInventory().getChestplate() != null){
                    kit.set("chestplate", p.getInventory().getChestplate());
                }
                if(p.getInventory().getLeggings() != null) {
                    kit.set("leggings", p.getInventory().getLeggings());
                }
                if(p.getInventory().getBoots() != null) {
                    kit.set("boots", p.getInventory().getBoots());
                }
            }

            Inventory inv = p.getInventory();
            ArrayList<ItemStack> kititems = new ArrayList<ItemStack>();
            for(ItemStack i : inv.getContents()){
                kititems.add(i);
            }
            kit.set("items", kititems);

            try {
                kit.save(kitfile);
                Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "Created kit!");
            } catch (IOException e) {
                Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Could not create kit");
                throw new RuntimeException(e);
            }
        }
    }

}
