package io.github.evancolewright;

import com.gmail.andrewandy.UpdateTask;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.WeakHashMap;

public class InvRestore extends JavaPlugin {

    //edited by @andrewandy
    private static InvRestore instance;
    private Map<UUID, Map<World, ItemStack[]>> itemMap = new HashMap<>();
    private Map<UUID, Map<World, ItemStack[]>> armourMap = new HashMap<>();
    private DeathListener listener = null;
    private RestoreCommand command = null;

    // By @andrewandy
    public static JavaPlugin getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        //By @andrewandy
        instance = this;
        new UpdateTask().runTaskTimerAsynchronously(this, 1, 1000);
        //end
        listener = new DeathListener(this);
        command = new RestoreCommand(this);
        getServer().getPluginManager().registerEvents(listener, this);
    }

    @Override
    public void onDisable() {
    }

    //Edited by @andrewandy

    void restoreInventory(Player p) {
        PlayerInventory inventory = p.getInventory();
        inventory.setContents(itemMap.get(p.getUniqueId()).get(p.getWorld()));
        inventory.setArmorContents(armourMap.get(p.getUniqueId()).get(p.getWorld()));
    }

    void storeContents(Player p, ItemStack[] items, ItemStack[] armor) {
        Map<World, ItemStack[]> itemStacks = new WeakHashMap<>();
        itemStacks.put(p.getWorld(), items);
        itemMap.put(p.getUniqueId(), itemStacks);
        itemStacks.clear();
        itemStacks.put(p.getWorld(), armor);
        armourMap.put(p.getUniqueId(), itemStacks);
    }
    //end


}
