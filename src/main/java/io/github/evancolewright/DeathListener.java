package io.github.evancolewright;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class DeathListener implements Listener {

    private InvRestore main = null;

    DeathListener(InvRestore main) {
        this.main = main;
    }

    @EventHandler
    public void onDeath(final PlayerDeathEvent e) {
        final Player p = e.getEntity();
        main.storeContents(p, p.getInventory().getContents(), p.getInventory().getArmorContents());
    }
}
