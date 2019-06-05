package io.github.evancolewright;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RestoreCommand implements CommandExecutor {

    private InvRestore main;

    private RestoreCommand() {
    }

    protected RestoreCommand(InvRestore main) {
        this.main = main;
        main.getCommand("invrestore").setExecutor(this);
    }

    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        if (sender instanceof Player) {
            final Player p = (Player) sender;
            if (cmd.getName().equalsIgnoreCase("invrestore")) {
                int length = args.length;
                if (length == 0) {
                    p.sendMessage(ChatColor.RED + "/invrestore <Player>");
                    return false;
                }
                if (length == 1) {
                    final Player target = Bukkit.getPlayer(args[0]);
                    if (target == null) {
                        p.sendMessage(ChatColor.RED + "Player not found");
                        return false;
                    }
                    if (!p.hasPermission("Invrestore.command")) {
                        p.sendMessage(ChatColor.RED + "You do not have permission");
                        return false;
                        //test
                    }
                    if (main.itemsMap.containsKey(target.getName()) && main.armorMap.containsKey(target.getName())) {
                        main.restoreInventory(target);
                        p.sendMessage(ChatColor.GREEN + "Successfully restored " + target.getName() + "'s inventory!");
                    } else {
                        p.sendMessage(ChatColor.RED + "Unable to restore player's inventory!");
                    }
                }
            }
        }

        return false;
    }
}
