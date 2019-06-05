package com.gmail.andrewandy.util;

import io.github.evancolewright.InvRestore;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.Random;
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
     * Colourise message by using chatCode "&".
     *
     * @param message the message you want to colourise.
     * @return colourised message
     * @since 1.0
     */

    public static String colourise(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }


    public static void log(Level level, String messages) {
        InvRestore.getInstance().getServer().getLogger().log(Level.INFO, colourise(messages));
    }

    /**
     * Capitalise the first expression of a string
     *
     * @param original message you want to capitalise.
     * @return the capitalised version
     * @since 1.0-SNAPSHOT
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


}
