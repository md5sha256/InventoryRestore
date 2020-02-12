package com.gmail.andrewandy;

import io.github.evancolewright.InvRestore;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitTask;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

/**
 * Represents the history of an inventory.
 * Includes a revision history and location of the inventory.
 */
public class InventoryHistory {

    private static File dataFile;
    private static YamlConfiguration config = new YamlConfiguration();
    private final UUID player;
    private int revision = 0;
    private int branchedRevision = -1;
    private Location location;
    private ItemStack[] storageContents;
    private ItemStack[] armourContents;
    private ItemStack[] extraContents;
    private InventoryHistory(PlayerInventory inventory) {
        update(inventory);
        if (inventory.getHolder() != null) {
            this.player = inventory.getHolder().getUniqueId();
        } else {
            this.player = null;
        }
    }

    private InventoryHistory(UUID playerUUID, int targetRevision) {
        this.player = playerUUID;
        setToRevision(targetRevision, false);
    }

    public static void setDataFile(File dataFile) {
        Objects.requireNonNull(dataFile);
        if (dataFile.isFile() || dataFile.canWrite()) {
            throw new IllegalArgumentException("Datafile isn't a file or cannot be written to!");
        }
        String data = config.saveToString();
        try {
            config.load(dataFile);
            config.loadFromString(data);
        } catch (InvalidConfigurationException | IOException ex) {
            throw new IllegalArgumentException(ex);
        }
    }

    public static InventoryHistory of(PlayerInventory inventory) {
        InventoryHistory history = new InventoryHistory(inventory);
        history.resetToLatestRevision(false);
        return history;
    }

    public int getRevision() {
        return revision;
    }

    public int getBranchedRevision() {
        return branchedRevision;
    }

    public UUID getPlayer() {
        return player;
    }

    public void writeTo(PlayerInventory other) {
        Objects.requireNonNull(other);
        other.setStorageContents(storageContents);
        other.setExtraContents(extraContents);
        other.setArmorContents(armourContents);
    }

    public void update(PlayerInventory newState) {
        Objects.requireNonNull(newState);
        this.location = newState.getLocation();
        this.armourContents = newState.getArmorContents();
        this.extraContents = newState.getExtraContents();
        revision++;
    }

    public void setToRevision(int targetRevision, boolean saveCurrentState) {
        if (saveCurrentState) {
            BukkitTask task = save();
            //Wait for save task to finish.
            while (!task.isCancelled()) ;
        }
        if (targetRevision < 1) {
            throw new IllegalArgumentException("Invalid Target Revision!");
        }
        ConfigurationSection section = config.getConfigurationSection(player.toString());
        if (section == null) {
            //Make the section.
            section = config.createSection(player.toString());
            section.createSection("Revisions");
            throw new IllegalArgumentException("Current UUID was not found in database. Please save to db first!");
        }
        ConfigurationSection revisionsSection = section.getConfigurationSection("Revisions");
        assert revisionsSection != null;
        ConfigurationSection revision = revisionsSection.getConfigurationSection("" + targetRevision);
        if (revision == null) {
            throw new IllegalArgumentException("Target revision not found!");
        }
        branchedRevision = section.getInt("Branched");
        Location location = section.getLocation("Location");
        ItemStack[] storageContents = new ItemStack[0], armourContents = new ItemStack[0], extraContents = new ItemStack[0];
        ConfigurationSection items = revision.getConfigurationSection("StorageContents");
        if (items != null) {
            int size = items.getInt("Size");
            storageContents = new ItemStack[size];
            for (int index = 0; index < size; index++) {
                storageContents[index] = items.getItemStack("" + index);
            }
        }
        ConfigurationSection armour = revision.getConfigurationSection("ArmourContents");
        if (armour != null) {
            int size = armour.getInt("Size");
            storageContents = new ItemStack[size];
            for (int index = 0; index < size; index++) {
                storageContents[index] = armour.getItemStack("" + index);
            }
        }
        ConfigurationSection extra = revision.getConfigurationSection("ExtraContents");
        if (extra != null) {
            int size = extra.getInt("Size");
            extraContents = new ItemStack[size];
            for (int index = 0; index < size; index++) {
                extraContents[index] = extra.getItemStack("" + index);
            }
        }
        this.storageContents = storageContents;
        this.armourContents = armourContents;
        this.extraContents = extraContents;
        this.location = location;
        branchedRevision = this.revision;
    }

    public void resetToLatestRevision(boolean saveCurrentState) {
        ConfigurationSection section = config.getConfigurationSection(player.toString());
        if (section == null) {
            throw new IllegalStateException("Please save to database first!");
        }
        ConfigurationSection revisionSection = section.getConfigurationSection("Revisions");
        assert revisionSection != null;
        int latest = revisionSection.getInt("Latest");
        setToRevision(latest, saveCurrentState);
    }

    public BukkitTask save() {
        ConfigurationSection section = config.getConfigurationSection(player.toString());
        section = section == null ? config.createSection(player.toString()) : section;
        ConfigurationSection revisionSection = section.getConfigurationSection("Revisions");
        revisionSection = revisionSection == null ? section.createSection("Revisions") : revisionSection;
        int latestRev = revisionSection.getInt("Latest");
        int targetRev = branchedRevision == -1 ? latestRev + revision - branchedRevision : revision;
        //Update latest revision.
        revisionSection.set("Latest", targetRev);

        ConfigurationSection revision = revisionSection.createSection("" + targetRev);
        //Set the fields
        revision.set("Branched", branchedRevision);
        revision.set("Location", location);
        ConfigurationSection storageSection = revisionSection.createSection("StorageContents");
        for (int index = 0; index < storageContents.length; ) {
            storageSection.set("" + index++, storageContents[index]);
        }
        ConfigurationSection armourSection = revisionSection.createSection("ArmourContents");
        for (int index = 0; index < armourContents.length; ) {
            armourSection.set("" + index++, armourContents[index]);
        }
        ConfigurationSection extraSection = revisionSection.createSection("ExtraContents");
        for (int index = 0; index < armourContents.length; ) {
            extraSection.set("" + index++, extraContents[index]);
        }
        return Bukkit.getScheduler().runTask(InvRestore.getInstance(), () -> {
            try {
                config.save(dataFile);
            } catch (IOException e) {
                throw new IllegalStateException("Caught error when trying to save data to disk!", e);
            }
        });
    }
}
