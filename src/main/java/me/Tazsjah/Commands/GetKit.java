package me.Tazsjah.Commands;

import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import me.Tazsjah.Data.Kits;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GetKit implements CommandExecutor {

    Kits kits;

    public GetKit(Kits kits) {
        this.kits = kits;
    }


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(sender instanceof Player player) {
            if(args.length > 0) {
                LocalPlayer lp = WorldGuardPlugin.inst().wrapPlayer(player);
                RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
                RegionQuery query = container.createQuery();

                if(query.testState(lp.getLocation(), lp, Flags.INVINCIBILITY)) {
                    if (kits.kitList(player) == null) {
                        player.sendMessage(ChatColor.GRAY + "You have no kits available. Use /savekit (name)");
                        return true;
                    }
                    if (kits.kitList(player).toString().contains(args[0] + ".yml")) {
                        player.getInventory().clear();
                        kits.getPlayerKit(player, args[0]);
                    } else {
                        sender.sendMessage(ChatColor.GRAY + "You do not own a kit named " + ChatColor.RED + args[0].toLowerCase());
                    }
                } else {
                    player.sendMessage(ChatColor.RED + "You cannot perform this command in the arena");
                }
            } else {
                player.sendMessage(ChatColor.RED + "You must specify what kit to get");
                if(kits.kitList(player) == null) {
                    player.sendMessage(ChatColor.GRAY + "You have no kits available. Use /savekit (name)");
                    return true;
                }

                player.sendMessage(ChatColor.GRAY + "Available Kits: " + kits.kitList(player).toString()
                        .replace(".yml", "")
                        .replace("]", "").replace("[", ""));
            }
        } else {
            sender.sendMessage(ChatColor.RED + "You must be a player to execute this");
        }


        return true;
    }
}
