package com.gmail.andrewandy;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import io.github.evancolewright.InvRestore;
import org.bukkit.World;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class DataTask extends BukkitRunnable {

    @Override
    public void run() {



        Map<UUID, Map<World, CustomInventory>> items = RestoreUtil.getInstance().getSavedItems();
        Map<UUID, Map<World, CustomInventory>> armour = RestoreUtil.getInstance().getSavedArmour();
        File folder = InvRestore.getInstance().getDataFolder();
        BiMap<UUID, Map<World, ItemStack[]>> itemsMap = HashBiMap.create(items);

        for (UUID uuid : itemsMap.inverse().values()) {
            BiMap<World, ItemStack[]> inventories = HashBiMap.create(items.get(uuid));
            for (World world : inventories.inverse().values()) {
                HashMap<String, Object> inv = new HashMap<>(CustomInventory.)


            }
        }


    }
}
