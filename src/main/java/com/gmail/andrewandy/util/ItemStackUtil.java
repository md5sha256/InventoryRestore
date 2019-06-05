package com.gmail.andrewandy.util;

import com.gmail.andrewandy.v1_14_R1.CustomItemStack;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemStackUtil {
    @SuppressWarnings("unchecked")

    static ItemStack getItemStack(Object object) throws IllegalArgumentException {

        if (!(object.getClass().isAssignableFrom(CustomItemStack.class))) {
            throw new IllegalArgumentException("Object is not a type of CustomItemStack");
        }
        CustomItemStack customItemStack = (CustomItemStack) object;

        org.bukkit.inventory.ItemStack item = new ItemStack(customItemStack.getMaterial());
        ItemMeta itemMeta = item.getItemMeta();
        if (!item.hasItemMeta() || itemMeta == null) {
            return null;
        }
        itemMeta.setDisplayName(Common.colourise(customItemStack.getDisplayName()));
        itemMeta.setLore(customItemStack.getLore());
        item.setItemMeta(itemMeta);
        item.addUnsafeEnchantments(customItemStack.getEnchantments());
        return item;
    }

}
