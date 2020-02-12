package io.github.evancolewright;

import com.gmail.andrewandy.InventoryHistory;
import com.gmail.andrewandy.RestoreUtil;
import com.gmail.andrewandy.corelib.util.Common;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;

public class RestoreCommand implements CommandExecutor {
    private static final String NO_CONSOLE = "&cYou must be a player to use this command!";

    /*
    Updated and cleaned up by andrewandy.
     */
    private static final String NO_PLAYER_FOUND = "&cUnknown Player.";
    private static final String INVALID_REVISION = "&eInvalid Revision!";
    @SuppressWarnings("depreceated")
    RestoreCommand() {
        InvRestore.getInstance().getCommand("invrestore").setExecutor(this);
    }

    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        String rawPlayer;
        int revision;
        if (!(sender instanceof Player)) {
            if (args.length < 2) {
                Common.tell(sender, NO_CONSOLE);
                return true;
            }
            int index = 0;
            rawPlayer = args[index].equalsIgnoreCase("revisions") ? args[index++] : args[index];
            Player p = Bukkit.getPlayer(rawPlayer);
            if (p == null) {
                Common.tell(sender, NO_PLAYER_FOUND);
                return true;
            }
            try {
                revision = Integer.parseInt(args[index]);
            } catch (NumberFormatException ex) {
                Common.tell(sender, INVALID_REVISION);
                return true;
            }
            InventoryHistory history = InventoryHistory.of(p.getInventory());
            history.setToRevision(revision, false);
            return true;
        }
        final Player player = (Player) sender;
        if (cmd.getName().equalsIgnoreCase("invrestore")) {
            int length = args.length;
            switch (length) {
                case 0:
                    Common.tell(player, "&cUsage: /invrestore <player> ");
                    return false;
                case 1:
                    final Player target = Bukkit.getPlayer(args[0]);
                    if (args[0].equalsIgnoreCase("help")) {
                        Common.tell(player, "&b <> = Required, &a[] Optional.");
                        Common.tell(player, "&cUsage: /invrestore [player] <revision>");
                        Common.tell(player, "&c       /invrestore <revision>");
                        Common.tell(player, "&a       /invrestore revisions [player]");
                        return true;

                    }
                    if (target == null) {
                        Common.tell(player, NO_PLAYER_FOUND);
                        return false;
                    }
                    if (!player.hasPermission("Invrestore.restore")) {
                        Common.tell(player, "&cInsufficient permission!");
                        return false;
                    }
                    final PlayerInventory inv = target.getInventory();
                    InventoryHistory history = InventoryHistory.of(inv);
                    //TODO Rollback one revision!
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
