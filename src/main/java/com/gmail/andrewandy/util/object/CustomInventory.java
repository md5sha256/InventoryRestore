package com.gmail.andrewandy.util.object;

import com.gmail.andrewandy.v1_14_R1.CustomItemStack;
import org.bukkit.Bukkit;
import org.bukkit.World;

import java.io.Serializable;
import java.util.UUID;

public class CustomInventory implements Serializable {

    private CustomItemStack[] contents;
    private CustomItemStack[] armourContents;
    private UUID player;
    private String world;
    private String identifier;

    public CustomInventory(CustomItemStack[] contents, CustomItemStack[] armourContents, UUID player, World world) {
        this.contents = contents;
        this.armourContents = armourContents;
        this.player = player;
        this.world = world.getName();
        identifier = player + "_" + world;
    }

    public CustomItemStack[] getContents() {
        return contents;
    }

    public CustomItemStack[] getArmourContents() {
        return armourContents;
    }

    public UUID getPlayer() {
        return player;
    }

    public World getWorld() {
        return Bukkit.getWorld(world);
    }

    public String getIdentifier() {
        return identifier;
    }
}
