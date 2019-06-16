package com.gmail.andrewandy;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import io.github.evancolewright.InvRestore;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.UUID;

public class DataTask extends BukkitRunnable {

    @Override
    public void run() {

        Map<UUID, Map<World, CustomInventory>> items = RestoreUtil.getInstance().getSavedItems();
        Map<UUID, Map<World, CustomInventory>> armour = RestoreUtil.getInstance().getSavedArmour();

    }

    /**
     * Save all current player data to the disk.
     *
     * @throws IOException
     */
    void save() throws IOException {

        Map<UUID, Map<World, CustomInventory>> items = RestoreUtil.getInstance().getSavedItems();
        Map<UUID, Map<World, CustomInventory>> armour = RestoreUtil.getInstance().getSavedArmour();

        BiMap<UUID, Map<World, CustomInventory>> biMap = HashBiMap.create(items);

        File file = new File(InvRestore.getInstance().getDataFolder().getAbsolutePath(), "list.yml");
        file.delete();
        file.createNewFile();

        for (UUID uuid : biMap.inverse().values()) {
            Map<World, CustomInventory> invs = items.get(uuid);
            BiMap<World, CustomInventory> biMap1 = HashBiMap.create(invs);
            for (World world : biMap1.inverse().values()) {
                Map<String, Object> serial = invs.get(world).serialize();
                File config = new File(InvRestore.getInstance().getDataFolder(), "config.yml");
                BukkitObjectOutputStream outputStream = new BukkitObjectOutputStream(new FileOutputStream(config));
                outputStream.writeObject(serial);
                outputStream.close();
            }
        }
    }

    /**
     * Load a player's Inventory Data
     *
     * @param player the player in question.
     * @throws IOException
     * @throws ClassNotFoundException
     */

    void load(Player player) throws IOException, ClassNotFoundException {

        if (!HashBiMap.create(RestoreUtil.getInstance().getSavedItems()).inverse().values().contains(player.getUniqueId())) {
            return;
        }
        InputStream config = InvRestore.class.getResourceAsStream("config.yml");
        BukkitObjectInputStream in = new BukkitObjectInputStream(config);
        Map<String, Object> raw = (Map) in.readObject();
        in.close();
        config.close();
        CustomInventory inv = CustomInventory.deserialise(raw);

    }

}
