package io.github.evancolewright;

import com.gmail.andrewandy.Common;
import com.gmail.andrewandy.CustomInventory;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public class InvRestore extends JavaPlugin {

    /*
    Edited, updated and additions made by andrewandy
     */
    private static InvRestore instance;
    private double[] supportedVersions = new double[]{10.0, 14.2};


    public static JavaPlugin getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
        double[] ver = Common.getNumberVersion();
        if (ver[0] < supportedVersions[0] || ver[ver.length - 1] > supportedVersions[1]) {
            Common.log(Level.SEVERE, "&cYou are running an unsupported version of spigot.");
            this.getPluginLoader().disablePlugin(this);
            return;
        }
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
