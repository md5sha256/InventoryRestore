package com.gmail.andrewandy;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.*;
import java.util.logging.Level;

/**
 * Custom Serializable inventory.
 */
@SerializableAs("InvRestoreCustomInventory")
public class CustomInventory implements ConfigurationSerializable {

    private ItemStack[] contents;
    private UUID player;
    private String type;
    private World world;

    public CustomInventory(UUID player, World world, ItemStack[] contents, String type) {
        this.contents = contents;
        this.player = player;
        this.type = type;
        this.world = world;
    }


    /**
     * Serialise this object
     * @return a Map representation of the current state.
     */
    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> result = new WeakHashMap<>();
        String name = player.toString() + "_" + world.getUID() + "_" + type;
        result.put(name, contents);
        return result;
    }

    public static CustomInventory deserialise(Map<String, Object> raw) {
        BiMap<String, Object> biMap = HashBiMap.create(raw);
        String name = (String) biMap.inverse().values().toArray()[0];
        String[] append = name.split("_");
        UUID player = UUID.fromString(append[0]);
        World world = Bukkit.getWorld(UUID.fromString(append[1]));
        String type = append[2];
        if (world == null) {
            throw new IllegalStateException("Unable to find world");
        }
        ItemStack[] contents = (ItemStack[]) raw.get(name);
        return new CustomInventory(player, world,contents, type);
    }

    public ItemStack[] getContents() {
        return contents;
    }

    public String getType() {
        return type;
    }

}
