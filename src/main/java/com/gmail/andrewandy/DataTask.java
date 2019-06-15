package com.gmail.andrewandy;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import io.github.evancolewright.InvRestore;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;

public class DataTask extends BukkitRunnable {

    @Override
    public void run() {

        Map<UUID, Map<World, CustomInventory>> items = RestoreUtil.getInstance().getSavedItems();
        Map<UUID, Map<World, CustomInventory>> armour = RestoreUtil.getInstance().getSavedArmour();
        File folder = InvRestore.getInstance().getDataFolder();
        BiMap<UUID, Map<World, CustomInventory>> itemsMap = HashBiMap.create(items);


        for (UUID uuid : itemsMap.inverse().values()) {
            BiMap<World, CustomInventory> inventories = HashBiMap.create(items.get(uuid));
            for (World world : inventories.inverse().values()) {
                if (inventories.get(world) == null) {
                    continue;
                }
                try {
                    HashMap<String, Object> inv = new HashMap<>(inventories.get(world).serialize());
                    File file = new File(folder.getAbsolutePath(), "Data" + File.separator + uuid.toString() + "_" + world.getName());
                    file.createNewFile();
                    FileOutputStream fileStream = new FileOutputStream(file);
                    ObjectOutputStream out = new ObjectOutputStream(fileStream);
                    out.writeObject(inv);
                    out.close();
                    fileStream.close();
                } catch (IOException ex) {
                    Common.log(Level.SEVERE, "&cUnable to save data file for" + Bukkit.getPlayer(uuid).getName());
                    ex.printStackTrace();
                }
            }
        }


    }

    private void serialise() {

    }

    private void deserialise() {}
}
