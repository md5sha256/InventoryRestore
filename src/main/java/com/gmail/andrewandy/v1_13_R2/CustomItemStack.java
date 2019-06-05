package com.gmail.andrewandy.v1_13_R2;

import net.minecraft.server.v1_13_R2.NBTTagCompound;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_13_R2.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class CustomItemStack implements Serializable {
    @SuppressWarnings("Unchecked")

    private HashSet NBT;
    private int stackSize;
    private String displayName;
    private ArrayList lore;
    private HashMap enchantments;
    private Material material;

    public CustomItemStack(ItemStack itemStack) {
        net.minecraft.server.v1_13_R2.ItemStack craftStack = CraftItemStack.asNMSCopy(itemStack);
        NBTTagCompound nbt = craftStack.getTag();
        if (nbt != null) {
            NBT = new HashSet(nbt.getKeys());
        }
        enchantments = new HashMap<>(itemStack.getEnchantments());
        stackSize = craftStack.getCount();
        if (itemStack.hasItemMeta() && itemStack.getItemMeta().hasLore()) {
            lore = new ArrayList(itemStack.getItemMeta().getLore());
        }
        material = itemStack.getType();
    }

    public String getDisplayName() {
        return displayName;
    }

    public ArrayList getLore() {
        return lore;
    }

    public HashMap getEnchantments() {
        return enchantments;
    }

    public HashSet getNBT() {
        return NBT;
    }

    public int getStackSize() {
        return stackSize;
    }

    public Material getMaterial() {
        return material;
    }
}

