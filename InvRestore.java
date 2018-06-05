package me.evanog.invrestore;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Map;
import java.util.HashMap;

public class InvRestore extends JavaPlugin {

    protected Map<String, ItemStack[]> itemsMap = new HashMap<String, ItemStack[]>();
    protected Map<String, ItemStack[]> armorMap = new HashMap<String, ItemStack[]>();
    private DeathListener listener = null;
    private RestoreCommand command = null;

    @Override
    public void onEnable() {
        this.listener = new DeathListener(this);
        this.command = new RestoreCommand(this);
        this.getServer().getPluginManager().registerEvents(listener, this);
    }

    @Override
    public void onDisable() {
    }

    protected void restoreInventory(Player p) {
        PlayerInventory inventory = p.getInventory();
        inventory.setContents(itemsMap.get(p.getName()));
        inventory.setArmorContents(armorMap.get(p.getName()));
    }

    protected void storeContents(Player p, ItemStack[] items, ItemStack[] armor) {
        itemsMap.put(p.getName(), items);
        armorMap.put(p.getName(), armor);
    }


}
