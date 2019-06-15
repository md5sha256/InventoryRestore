package io.github.evancolewright;

import com.gmail.andrewandy.Common;
import com.gmail.andrewandy.CustomInventory;
import com.gmail.andrewandy.RestoreUtil;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;

public class RestoreCommand implements CommandExecutor {
    @SuppressWarnings("depreceated")
    RestoreCommand() {
        InvRestore.getInstance().getCommand("invrestore").setExecutor(this);
    }

    /*
    Updated and cleaned up by andrewandy.
     */

    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        if (!(sender instanceof Player)) {
            if (args[0].equalsIgnoreCase("forceLoad")) {
                if (!sender.hasPermission("Invrestore.forceLoadAll")) {
                    Common.tell(sender, "&cInsufficient permission!");
                }

            }

        }
        final Player player = (Player) sender;
        if (cmd.getName().equalsIgnoreCase("invrestore")) {
            int length = args.length;
            switch (length) {
                case 0:
                    Common.tell(player, "&cUsage: /invresttore <player> [world]");
                    return false;
                case 1:
                    final Player target = Bukkit.getPlayer(args[0]);
                    if (args[0].equalsIgnoreCase("help")) {
                        Common.tell(player, "&b <> = Required, &a[] Optional.");
                        Common.tell(player, "&cUsage: /invrestore <player> [world]");
                        return true;

                    } else if (args[0].equalsIgnoreCase("forceLoad")) {
                        if (!player.hasPermission("Invrestore.forceLoadAll")) {
                            Common.tell(player, "&cInsufficient permission!");
                        }
                    }
                    if (target == null) {
                        Common.tell(player, "&eThat player is either offline or does not exist.");
                        return false;
                    }
                    if (!player.hasPermission("Invrestore.restore")) {
                        Common.tell(player, "&cInsufficient permission!");
                        return false;
                    }
                    final PlayerInventory inv = target.getInventory();
                    RestoreUtil.getInstance().storeContents(target, new CustomInventory(player.getUniqueId(), player.getWorld(),inv.getContents(), "items"), new CustomInventory(player.getUniqueId(), player.getWorld(), inv.getArmorContents(), "armour"));
                    if (!RestoreUtil.getInstance().restoreInventory(target)) {
                        Common.tell(player, "&eUnable to restore " + target.getDisplayName() + " 's &einventory");
                    }
                    Common.tell(player, "&aSucessfully restored " + target.getDisplayName() + " " + "'s &ainventory");
                    return true;
                //For when a world is specified.
                case 2:
                    Player target1 = Bukkit.getPlayer(args[0]);
                    World world = Bukkit.getWorld(args[1]);
                    if (world == null) {
                        Common.tell(player, "&eInvalid World.");
                        return false;
                    }
                    if (target1 == null) {
                        Common.tell(player, "&eThat player is either offline or does not exist.");
                        return false;
                    }
                    if (!player.hasPermission("Invrestore.restore")) {
                        Common.tell(player, "&cInsufficient permission!");
                        return false;
                    }
                    if (!RestoreUtil.getInstance().restoreInventory(target1, world)) {
                        Common.tell(player, "&eUnable to restore " + target1.getDisplayName() + " 's &einventory");
                    }
                    Common.tell(player, "&aSucessfully restored " + target1.getDisplayName() + " " + "'s &ainventory");
                    return true;
                default:
                    Common.tell(player, "&cUsage: /invrestore <player> [world]");
                    return false;
            }
        }
        return false;
    }
}
