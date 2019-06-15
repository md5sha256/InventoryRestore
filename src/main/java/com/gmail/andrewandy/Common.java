package com.gmail.andrewandy;

import io.github.evancolewright.InvRestore;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.logging.Level;

public class Common {

    /**
     * Tell player colourised message.
     *
     * @param toWhom   toWhom you want to send the message
     * @param messages the messages you want to send.
     * @since 1.0
     */

    public static void tell(CommandSender toWhom, String... messages) {
        for (final String message : messages)
            toWhom.sendMessage(colourise(message));
    }

    /**
     * Colourise message by using chatCode {@literal &}
     *
     * @param message the message you want to colourise.
     * @return colourised message
     * @since 1.0
     */

    public static String colourise(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    /**
     * Colours a bunch of messages using chatCode {@literal &}
     *
     * @param messages
     * @return a {@link List<String>} version with each element colourised.
     */

    public static List<String> colouriseList(List<String> messages) {
        for (int index = 0; index < messages.size(); index++) {
            String s = messages.get(index);
            messages.add(index, colourise(s));
        }
        return messages;
    }


    public static void log(Level level, String messages) {
        InvRestore.getInstance().getServer().getLogger().log(level, colourise(messages));
    }

    /**
     * Capitalise the first expression of a string
     *
     * @param original message you want to capitalise.
     * @return the capitalised version
     * @since 1.0-
     */

    public static String capitalise(String original) {
        if (original == null || original.length() == 0) {
            return original;
        }
        return original.substring(0, 1).toUpperCase() + original.substring(1);
    }

    /**
     * Get a random number from a set range.
     *
     * @param min minimum number.
     * @param max maximum number
     * @return returns a random number from the given range.
     * @since 1.0
     */

    public static int getRandomNumberInRange(int min, int max) {
        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min");
        }
        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }

    /**
     * Gets the NMS version of the server. (i.e v1_14R1)
     *
     * @return NMS version of a server
     */

    public static String getNMSVersion() {
        String ver = Bukkit.getServer().getClass().getPackage().getName();
        return ver.substring(ver.lastIndexOf('.') + 1);
    }

    /**
     * Get a decimal version of the server version i.e 1.10 = 10.0, 1.10.2 = 10.2 etc.
     *
     * @return a decimal version of the server.
     */

    public static void restoreInventory(UUID uuid, World world) {




    }


    public static double[] getNumberVersion() {

        String nms = getNMSVersion();

        switch (nms) {
            case "v1_8_R1":
                return new double[]{8.0};
            case "v1_8_R2":
                return new double[]{8.3};
            case "v1_8_R3":
                return new double[]{8.4, 8.5, 8.6, 8.7, 8.8};
            case "v1_9_R1":
                return new double[]{9.0, 9.2};
            case "v1_9_R2":
                return new double[]{9.4};
            case "v1_10_R1":
                return new double[]{10.0, 10.1, 10.2};
            case "v1_11_R1":
                return new double[]{11.0, 11.1, 11.2};
            case "v1_12_R1":
                return new double[]{12.0, 12.1, 12.2};
            case "v1_13_R1":
                return new double[]{13.0};
            case "v1_13_R2":
                return new double[]{13.1, 13.2};
            case "v1_14_R2":
                return new double[]{14.0, 14.1, 14.2};
            default:
                return new double[-1];
        }
    }


}
