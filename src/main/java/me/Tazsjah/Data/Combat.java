package me.Tazsjah.Data;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.UUID;

public class Combat {

    Config config;
    Messages msgs;

    public Combat(Config config, Messages msgs) {
        this.config = config;
        this.msgs = msgs;
    }

//    public String s = "";
//
//    public void createConfig() {
//        s = (String) config.get("f");
//    }

    public HashMap<UUID, Integer> time = new HashMap<>();
    public HashMap<UUID, Boolean> inCombat = new HashMap<>();

    private void resetCombatTimers() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            inCombat.put(player.getUniqueId(), false);
        }
    }

    public void startTimer() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for(Player p : Bukkit.getOnlinePlayers()){
                    if(time.get(p.getUniqueId()) != null) {
                        if(inCombat.replace(p.getUniqueId(), false)) {
                            if (time.get(p.getUniqueId()) > 0) {
                                inCombat.replace(p.getUniqueId(), true);
                                int x = time.get(p.getUniqueId());
                                time.replace(p.getUniqueId(), x - 1);
                            } else {
                                inCombat.replace(p.getUniqueId(), false);
                                time.remove(p.getUniqueId());
                                p.sendMessage(msgs.get("combat-untag"));
                            }
                        } else {
                            inCombat.replace(p.getUniqueId(), false);
                            time.remove(p.getUniqueId());
                        }
                    } else {
                        inCombat.replace(p.getUniqueId(), false);
                        time.remove(p.getUniqueId());
                    }
                }
            }
        }.runTaskTimer(Bukkit.getPluginManager().getPlugin("FFA"), 0, 20);
    }

    public void tag(Player player) {

        if(inCombat.get(player.getUniqueId()) == null) {
            inCombat.put(player.getUniqueId(), true);
            time.put(player.getUniqueId(), (Integer) config.get("combat-time"));
            player.sendMessage(msgs.get("combat-tag"));
            return;
        }

        if(inCombat.get(player.getUniqueId()) == false){
            inCombat.put(player.getUniqueId(), true);
            time.put(player.getUniqueId(), (Integer) config.get("combat-time"));
            player.sendMessage(msgs.get("combat-tag"));
            return;
        }

        time.put(player.getUniqueId(), (Integer) config.get("combat-time"));
        inCombat.put(player.getUniqueId(), true);

    }

    public Boolean inCombat(Player player) {
        if(inCombat.get(player.getUniqueId()) == null) {
            return false;
        }
        if(inCombat.get(player.getUniqueId())) {
            return true;
        }
        return false;
    }

    public void untag(Player player) {
        if(inCombat.get(player.getUniqueId()) == null) {
            inCombat.put(player.getUniqueId(), false);
            time.put(player.getUniqueId(), 0);
            player.sendMessage(msgs.get("combat-untag"));
        }
        if(inCombat.get(player.getUniqueId())){
            inCombat.put(player.getUniqueId(), false);
            time.put(player.getUniqueId(), 0);
            player.sendMessage(msgs.get("combat-untag"));
        }
    }

}
