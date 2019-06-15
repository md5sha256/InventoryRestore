package com.gmail.andrewandy;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.logging.Level;

/**
 * Custom Serializable inventory.
 */
@SerializableAs("InvRestoreCustomInventory")
public class CustomInventory implements ConfigurationSerializable {

    private ItemStack[] contents;
    private UUID player;

    public CustomInventory(UUID player, ItemStack[] contents) {
        this.contents = contents;
        this.player = player;
    }


    /**
     * Serialise this object
     * @return a Map representation of the current state.
     */
    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> result = new HashMap<>();
        Map<UUID, ItemStack[]> inv = new WeakHashMap<>();
        result.put(player.toString(), inv);
        return result;
    }

    public static CustomInventory deserialise(Map<String, Object> raw, UUID target) {
        if (raw.get(target.toString()) == null) {
            return null;
        }
        Map<?, ?> rawInv = (HashMap) raw.get(target.toString());
        BiMap<?, ?> biMap = HashBiMap.create(rawInv);
        for ( Object obj : biMap.inverse().values()) {
            if (!obj.getClass().isAssignableFrom(World.class)) {
                continue;
            }
            UUID worldID = (UUID) obj;
            World world = Bukkit.getWorld(worldID);

            if (world == null) {
                Common.log(Level.SEVERE, "Missing world detected! Skipping file.");
                continue;
            }
            ItemStack[] contents = (ItemStack[]) rawInv.get(worldID);

            return new CustomInventory(target, world, contents);
        }
        return null;

    }

    public ItemStack[] getContents() {
        return contents;
    }
}
