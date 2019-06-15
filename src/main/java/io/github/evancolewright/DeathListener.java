package io.github.evancolewright;

import com.gmail.andrewandy.CustomInventory;
import com.gmail.andrewandy.RestoreUtil;
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
        RestoreUtil.getInstance().storeContents(player, new CustomInventory(player.getUniqueId(), inv.getContents(), "items"), new CustomInventory(player.getUniqueId(), inv.getArmorContents(), "armour"));
    }
}
