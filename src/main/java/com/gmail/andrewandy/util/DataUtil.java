package com.gmail.andrewandy.util;

import com.gmail.andrewandy.util.object.CustomInventory;
import io.github.evancolewright.InvRestore;
import org.bukkit.Bukkit;

import java.io.*;
import java.util.*;
import java.util.logging.Level;

public class DataUtil {

    public static void saveItems(Set<CustomInventory> inventories) throws IOException {
        for (CustomInventory inv : inventories) {
            File file = new File(InvRestore.getInstance().getDataFolder(), inv.getIdentifier() + File.separator + ".ser");
            file.createNewFile();
            FileOutputStream fileStream = new FileOutputStream(file);
            ObjectOutputStream out = new ObjectOutputStream(fileStream);
            out.writeObject(inv);
            out.close();
            fileStream.close();
        }
    }


    /**
     * Gets every single inventory that has ever been saved.
     * @param override Override the current cache of inventories
     * @return a map of tbe player and their respective inventories across all worlds.
     */
    public Map<UUID, CustomInventory[]> getAllInventories(boolean override) throws IOException{
        File[] files = InvRestore.getInstance().getDataFolder().listFiles();
        Map<UUID, CustomInventory[]> inventoryMap = new WeakHashMap<>();
        Set<String> filenames = new HashSet<>();
        Set<UUID> uuids = new HashSet<>();
        for (int index = 0; index < files.length; index++) {
         filenames.add(files[index].getName());
        }
        for (String filename: filenames) {
            String[] split = filename.split("_");
            if (split.length != 2) {
                System.out.println(filename);
                throw new IllegalArgumentException("Invalid filename serialised!");
            }
            UUID uuid = UUID.fromString(split[0]);
            if (!uuids.contains(uuid)) {
                uuids.add(uuid);
            }
        }
        for (UUID uuid : uuids) {
            inventoryMap.put(uuid, getInventory(uuid, true));
        }
        return inventoryMap;
    }

    /**
     * Get the inventory of a player from the disk.
     * @param uuid of the player.
     * @param deep whether to check for offline players.
     * @return a {@link CustomInventory} of the player.
     */

    public static CustomInventory[] getInventory(UUID uuid, boolean deep) throws IOException{

        File[] files = InvRestore.getInstance().getDataFolder().listFiles();
        if (files == null) {
            return null;
        }
        int worldCount = 0;
        CustomInventory[] inventories = new CustomInventory[Integer.MAX_VALUE];
        for (int index = 0; index < files.length; index++) {
            if (!files[index].getName().startsWith(uuid.toString())) {
                continue;
            }
            File file = files[index];
            FileInputStream fileStream = new FileInputStream(file);
            ObjectInputStream in = new ObjectInputStream(fileStream);
            Object obj;
            try {
                obj = in.readObject();
            } catch (ClassNotFoundException ex) {
                Common.log(Level.SEVERE, "&cJava version of Plugin version mis-match!");
                return null;
            }
            if (obj == null) {
                return null;
            }
            if (!(obj.getClass().isAssignableFrom(CustomInventory.class))) {
                throw new IllegalStateException("Invalid serial file.");
            }
            CustomInventory inv = (CustomInventory) obj;
            if (Bukkit.getPlayer(inv.getPlayer()) == null && !deep) {
                return null;
            }
            inventories[worldCount] = inv;
            worldCount++;
        }
        CustomInventory[] toReturn = new CustomInventory[worldCount];
        for (int index = 0; index < worldCount; index++) {
            toReturn[index] = inventories[index];
        }
        return toReturn;
    }

}
