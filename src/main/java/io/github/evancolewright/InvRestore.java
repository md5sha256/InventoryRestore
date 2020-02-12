package io.github.evancolewright;

import com.gmail.andrewandy.CustomInventory;
import com.gmail.andrewandy.corelib.util.Common;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public class InvRestore extends JavaPlugin {

    /*
    Edited, updated and additions made by andrewandy
     */
    private static InvRestore instance;


    public static JavaPlugin getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
        new DeathListener();
        new RestoreCommand();
        ConfigurationSerialization.registerClass(CustomInventory.class);
        Common.log(Level.INFO, "&a[Inventory&eRestore] Plugin is now enabled.");
    }

    @Override
    public void onDisable() {
        Common.log(Level.INFO, "&a[Inventory&eRestore] Plugin is now disabled!");
        instance = null;
    }
}
