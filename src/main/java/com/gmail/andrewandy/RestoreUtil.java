package com.gmail.andrewandy;

import io.github.evancolewright.InvRestore;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.WeakHashMap;

public class RestoreUtil {

    private static RestoreUtil i;
    private static boolean taskIsRegistered = false;
    private Map<UUID, Map<World, CustomInventory>> items = new HashMap<>();
    private Map<UUID, Map<World, CustomInventory>> armour = new HashMap<>();

    private RestoreUtil() {
        registerTask();
    }

    public static RestoreUtil getInstance() {
        if (i == null) {
            return new RestoreUtil();
        }
        return i;
    }

    public static void registerTask() {
        if (!taskIsRegistered) {
            new BukkitRunnable() {
                @Override
                public void run() {

                }
            }.runTaskTimer(InvRestore.getInstance(), 1, 1000);
        }
    }

    //Edited by @andrewandy
    public boolean restoreInventory(Player player) {
        PlayerInventory inventory = player.getInventory();
        World world = player.getWorld();
        if (items.get(player.getUniqueId()) == null || items.get(player.getUniqueId()).get(world) == null || armour.get(player.getUniqueId()) == null || armour.get(player.getUniqueId()).get(world) == null) {
            return false;
        }

        inventory.setContents(items.get(player.getUniqueId()).get(world).getContents());
        inventory.setArmorContents(armour.get(player.getUniqueId()).get(world).getContents());
        return true;
    }

    public boolean restoreInventory(Player player, World world) {
        if (world == null) {
            throw new IllegalArgumentException("Null world!");
        }
        if (items.get(player.getUniqueId()) == null || items.get(player.getUniqueId()).get(world) == null || armour.get(player.getUniqueId()) == null || armour.get(player.getUniqueId()).get(world) == null) {
            return false;
        }
        final PlayerInventory inventory = player.getInventory();
        inventory.setContents(items.get(player.getUniqueId()).get(world).getContents());
        inventory.setArmorContents(armour.get(player.getUniqueId()).get(world).getContents());
        return true;
    }

    public void storeContents(Player player, CustomInventory items, CustomInventory armor) {
        Map<World, CustomInventory> contents = new WeakHashMap<>();
        contents.put(player.getWorld(), items);
        this.items.put(player.getUniqueId(), contents);
        contents.clear();
        contents.put(player.getWorld(), armor);
        armour.put(player.getUniqueId(), contents);
    }

    /**
     * Only needed for the DataTask, should not be used elsewhere.
     *
     * @return a Map of the player's inventroies over worlds
     */
    public Map<UUID, Map<World, CustomInventory>> getSavedArmour() {
        return armour;
    }

    void setSavedArmour(Map<UUID, Map<World, CustomInventory>> armour) {
        this.armour = armour;
    }

    public Map<UUID, Map<World, CustomInventory>> getSavedItems() {
        return items;
    }

    void setSavedItems(Map<UUID, Map<World, CustomInventory>> items) {
        this.items = items;
    }
}
