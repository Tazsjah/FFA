package me.Tazsjah.Data;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Kits {

    Config config;
    Messages msgs;
    PlayerData data;

    public Kits(Config config, Messages msgs, PlayerData data) {
        this.config = config;
        this.msgs = msgs;
        this.data = data;
    }

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
                if(i != null) {
                    kititems.add(i);
                }
            }
            kit.set("items", kititems);

            try {
                kit.save(kitfile);
                p.sendMessage(ChatColor.GREEN + "Created kit!");
            } catch (IOException e) {
                Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Could not create kit");
                throw new RuntimeException(e);
            }
        }
    }

    public void deleteKit(Player p, String s) {

        File kitfile = new File(f, s.toLowerCase() + ".yml");
        FileConfiguration kit = YamlConfiguration.loadConfiguration(kitfile);

        if(kitfile.exists()) {
            p.sendMessage(ChatColor.GRAY + "The kit has been deleted");
            kitfile.delete();
            return;
        }

        p.sendMessage(ChatColor.RED + "This kit does not exist.");

    }

    public void giveKit(Player player, String s) {

        File kitfile = new File(f, s.toLowerCase() + ".yml");
        FileConfiguration kit = YamlConfiguration.loadConfiguration(kitfile);

        if(kitfile.exists()) {

            if(kit.get("helmet") != null) {
                ItemStack helmet = (ItemStack) kit.get("helmet");
                player.getInventory().setHelmet(helmet);
            }

            if(kit.get("chestplate") != null) {
                ItemStack chestplate = (ItemStack) kit.get("chestplate");
                player.getInventory().setChestplate(chestplate);
            }

            if(kit.get("leggings") != null) {
                ItemStack leggings = (ItemStack) kit.get("leggings");
                player.getInventory().setLeggings(leggings);
            }

            if(kit.get("boots") != null) {
                ItemStack boots = (ItemStack)  kit.get("boots");
                player.getInventory().setBoots(boots);
            }

            if(kit.get("items") != null) {
                List<ItemStack> items = (List<ItemStack>) kit.getList("items");

                for(ItemStack i : items) {
                    player.getInventory().addItem(i);
                }

            }

            player.sendMessage(msgs.get("received-kit").replace("$kit", s));

        } else {
            player.sendMessage(ChatColor.RED + "This kit does not exist.");
        }

    }

    File z = new File(Bukkit.getPluginManager().getPlugin("FFA").getDataFolder() + "/Kits/Players/");
    public void createPlayerKit(Player p, String s) {

        if(kitLimit(p) >= (int) config.get("kit-limit")) { p.sendMessage(ChatColor.RED + "You already have 2 kits set."); return;}

        File kitfile = new File(z + "/" + p.getUniqueId().toString() + "/", s.toLowerCase() + ".yml");
        FileConfiguration kit = YamlConfiguration.loadConfiguration(kitfile);

        if(!kitfile.exists()){
            p.getInventory().getArmorContents();// This sets the armor for the kit

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

            Inventory inv = p.getInventory();
            ArrayList<ItemStack> kititems = new ArrayList<ItemStack>();
            for(ItemStack i : inv.getContents()){
                if(i != null) {
                    kititems.add(i);
                }
            }

            kit.set("items", kititems);
            try {
                kit.save(kitfile);
                p.sendMessage(ChatColor.GREEN + "Created the kit " + s);
            } catch (IOException e) {
                p.sendMessage(ChatColor.RED + "Could not create kit. Please report this issue to admins");
                throw new RuntimeException(e);
            }
        } else {
            p.sendMessage(ChatColor.RED + "This kit exists already");
        }

    }
    public void getPlayerKit(Player player, String s) {
        File kitfile = new File(z + "/" + player.getUniqueId().toString() + "/", s.toLowerCase() + ".yml");
        FileConfiguration kit = YamlConfiguration.loadConfiguration(kitfile);

        if(kitfile.exists()) {

            if(kit.get("helmet") != null) {
                ItemStack helmet = (ItemStack) kit.get("helmet");
                player.getInventory().setHelmet(helmet);
            }

            if(kit.get("chestplate") != null) {
                ItemStack chestplate = (ItemStack) kit.get("chestplate");
                player.getInventory().setChestplate(chestplate);
            }

            if(kit.get("leggings") != null) {
                ItemStack leggings = (ItemStack) kit.get("leggings");
                player.getInventory().setLeggings(leggings);
            }

            if(kit.get("boots") != null) {
                ItemStack boots = (ItemStack)  kit.get("boots");
                player.getInventory().setBoots(boots);
            }

            if(kit.get("items") != null) {
                List<ItemStack> items = (List<ItemStack>) kit.getList("items");
                for(ItemStack i : items) {
                    if(i.getType().toString().endsWith("_HELMET")
                            || i.getType().toString().endsWith("_CHESTPLATE")
                            || i.getType().toString().endsWith("_LEGGINGS")
                            || i.getType().toString().endsWith("_BOOTS")) { return;}
                    player.getInventory().addItem(i);
                }
            }

        } else {
            player.sendMessage(ChatColor.RED + "You do not have a kit set. Use /setkit");
        }
    }
    public void deletePlayerKit(Player player, String s) {
        File kitfile = new File(z + "/" + player.getUniqueId().toString() + "/", s.toLowerCase() + ".yml");
        FileConfiguration kit = YamlConfiguration.loadConfiguration(kitfile);

        if(kitfile.exists()) {
            player.sendMessage(ChatColor.GRAY + "Your kit has been deleted");
            kitfile.delete();
        }
    }
    public void setKit(Player player, String s) {
        data.kitSet(player, s.toLowerCase());
    }
    public List<String> kitList(Player player) {
        File kitfile = new File(z + "/" + player.getUniqueId().toString() + "/");
        List<String> kits = new ArrayList<>();

        if(!kitfile.exists()) { return null; }

        if(kitfile.listFiles().length > 0) {
            for(File s : kitfile.listFiles()) {
                kits.add(ChatColor.RED + s.getName());
            }

            return kits;
        }

        return null;
    }

    public int kitLimit(Player player) {
        File kitfile = new File(z + "/" + player.getUniqueId().toString() + "/");

        if(!kitfile.exists()) { return 0; }

        if(kitfile.listFiles().length > 0) {
            return kitfile.listFiles().length;
        }

        return 0;
    }
}
