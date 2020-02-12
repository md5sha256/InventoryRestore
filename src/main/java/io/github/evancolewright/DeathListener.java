package io.github.evancolewright;

import com.gmail.andrewandy.InventoryHistory;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.PlayerInventory;

public class DeathListener implements Listener {

    @EventHandler
    public void onDeath(final PlayerDeathEvent event) {
        final Player player = event.getEntity();
        final PlayerInventory inv = player.getInventory();
        final InventoryHistory history = InventoryHistory.of(inv);
        Bukkit.getScheduler().runTaskLater(InvRestore.getInstance(), () -> history.writeTo(inv), 1);
    }


}
