package com.wenhao.itempickuprange.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ModConfig {
    public double minPickupRange = 0.5;
    public double maxPickupRange = 5.0;

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static ModConfig instance;
    private static File configFile;

    public static void load(File configDir) {
        try {
            configFile = new File(configDir, "item_pickup_range.json");
            if (!configFile.exists()) {
                instance = new ModConfig();
                save();
            } else {
                instance = GSON.fromJson(new FileReader(configFile), ModConfig.class);
            }
        } catch (IOException e) {
            e.printStackTrace();
            instance = new ModConfig();
        }
    }

    public static void save() {
        try (FileWriter writer = new FileWriter(configFile)) {
            GSON.toJson(instance, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ModConfig get() {
        return instance;
    }
}
