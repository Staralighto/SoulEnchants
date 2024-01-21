package starshine.soulenchants;



import org.black_ixx.playerpoints.PlayerPoints;
import org.black_ixx.playerpoints.PlayerPointsAPI;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.boss.BossBar;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import starshine.soulenchants.Enchantments.*;

import java.io.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;


public final class SoulEnchants extends JavaPlugin {
    private static SoulEnchants plugin;
    private static SoulEnchants instance;

    public static Enchantment soulBlade;
    public static Enchantment chiseling;
    public static Enchantment plenty;

    public static Enchantment accurate;
    public static Enchantment vision;
    public static Enchantment diligent;

    private PlayerPointsAPI ppAPI;








    @Override
    public void onEnable() {
        // Plugin startup logic



        plugin=this;
        instance=this;

        loadConfig();
        loadEnchantConfig();
        loadDataConfig();
        saveDefaultConfig();
        saveConfig();





        soulBlade = new SoulBlade("soul_blade");
        chiseling = new Chiseling("chiseling");
        plenty = new Plenty("plenty");
        accurate = new Accurate("accurate");
        vision = new Vision("vision");
        diligent = new Diligent("diligent");


        for(Enchantment enchantment : Method.enchantmentList()){
            registerEnchantment(enchantment);
        }

        Bukkit.getServer().getPluginCommand("soulenchants").setExecutor(new Commands());
        Bukkit.getPluginManager().registerEvents(new EventListener(), this);




        if (Bukkit.getPluginManager().isPluginEnabled("PlayerPoints")) {
            this.ppAPI = PlayerPoints.getInstance().getAPI();
        }

        // When you want to access the API, check if the instance is null
        if (this.ppAPI != null) {
            // Do stuff with the API here
            Bukkit.broadcastMessage("PlayerPoint API connected");
        }






    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        try {
            Field keyField = Enchantment.class.getDeclaredField("byKey");

            keyField.setAccessible(true);
            @SuppressWarnings("unchecked")
            HashMap<NamespacedKey, Enchantment> byKey = (HashMap<NamespacedKey, Enchantment>) keyField.get(null);
            for (Enchantment enchantment : Method.enchantmentList()) {
                if (byKey.containsKey(enchantment.getKey())) {
                    byKey.remove(enchantment.getKey());
                }
            }
            Field nameField = Enchantment.class.getDeclaredField("byName");
            nameField.setAccessible(true);
            @SuppressWarnings("unchecked")
            HashMap<String, Enchantment> byName = (HashMap<String, Enchantment>) nameField.get(null);

            for (Enchantment enchantment : Method.enchantmentList()) {
                if (byName.containsKey(enchantment.getName())) {
                    byName.remove(enchantment.getName());
                }
            }
        } catch (Exception ignored) {

        }


    }

    public static SoulEnchants getInstance() {
        return instance;
    }

    public static SoulEnchants getPlugin() {
        return plugin;
    }

    public static PlayerPointsAPI getPlayerPointsAPI(){
        return SoulEnchants.getPlugin().ppAPI;
    }















    public static void registerEnchantment(Enchantment enchantment) {
        boolean registered = true;
        try {
            Field f = Enchantment.class.getDeclaredField("acceptingNew");
            f.setAccessible(true);
            f.set(null, true);
            try {
                Enchantment.registerEnchantment(enchantment);
            } catch (IllegalArgumentException ignored){

            }
        } catch (Exception e) {
            registered = false;
        }
        if (registered) {
            // 已经注册！
        }
    }

    private void loadConfig() {

        File configFile = new File(getDataFolder(), "config.yml");

        // 检查配置文件是否存在，如果不存在则创建默认的语言配置文件
        if (!configFile.exists()) {
            saveResource("config.yml", false);
        }


        FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);

    }

    private void loadEnchantConfig() {

        File configFile = new File(SoulEnchants.getPlugin().getDataFolder(), "enchantment.yml");

        // 检查配置文件是否存在，如果不存在则创建默认的语言配置文件
        if (!configFile.exists()) {
            saveResource("enchantment.yml", false);
        }

        FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);

    }

    private void loadDataConfig() {

        File configFile = new File(SoulEnchants.getPlugin().getDataFolder(), "itemdata.yml");

        // 检查配置文件是否存在，如果不存在则创建默认的语言配置文件
        if (!configFile.exists()) {
            saveResource("itemdata.yml", false);
        }


        FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);

    }








}
