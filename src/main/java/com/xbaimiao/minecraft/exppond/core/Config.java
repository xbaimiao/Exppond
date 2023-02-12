package com.xbaimiao.minecraft.exppond.core;

import com.google.gson.Gson;
import com.xbaimiao.minecraft.exppond.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Random;

public class Config {

    static {
        load();
    }

    private static FileConfiguration configuration;
    private static World world;
    private static Random random;
    private static Title titleJoin;
    private static Title titleQuit;
    public static int minX;
    public static int maxX;
    public static int minY;
    public static int maxY;
    public static int minZ;
    public static int maxZ;
    public static int amount;
    public static int speed;

    /**
     * 获取配置文件世界
     */
    public static World getWorld() {
        if (Config.world == null) {
            final String string = Config.configuration.getString("location.world");
            assert string != null;
            Config.world = Bukkit.getWorld(string);
        }
        return Config.world;
    }

    /**
     * 获取一个随机位置
     */
    public static Location getRandom() {
        final int x = Config.random.nextInt(Config.maxX - Config.minX + 1) + Config.minX;
        final int y = Config.random.nextInt(Config.maxY - Config.minY + 1) + Config.minY;
        final int z = Config.random.nextInt(Config.maxZ - Config.minZ + 1) + Config.minZ;
        return new Location(getWorld(), x, y, z);
    }

    public static Title getTitleJoin() {
        return titleJoin;
    }

    public static Title getTitleQuit() {
        return titleQuit;
    }

    /**
     * 加载配置文件
     */
    public static void load() {
        configuration = Main.getInstance().getConfig();
        Config.random = new Random(System.currentTimeMillis());
        final String string = Config.configuration.getString("location.world");
        assert string != null;
        Config.world = Bukkit.getWorld(string);
        titleJoin = new Gson().fromJson(Config.configuration.getString("title.join"), Title.class);
        titleQuit = new Gson().fromJson(Config.configuration.getString("title.quit"), Title.class);
        Config.amount = Config.configuration.getInt("amount");
        Config.speed = Config.configuration.getInt("speed");
        final String loc1 = Config.configuration.getString("location.1");
        final String loc2 = Config.configuration.getString("location.2");
        if (loc1 != null && loc2 != null) {
            final String[] args1 = loc1.split("/");
            final String[] args2 = loc2.split("/");
            final int x1 = Integer.parseInt(args1[0]);
            final int y1 = Integer.parseInt(args1[1]);
            final int z1 = Integer.parseInt(args1[2]);
            final int x2 = Integer.parseInt(args2[0]);
            final int y2 = Integer.parseInt(args2[1]);
            final int z2 = Integer.parseInt(args2[2]);
            Config.minX = Math.min(x1, x2);
            Config.minY = Math.min(y1, y2);
            Config.minZ = Math.min(z1, z2);
            Config.maxX = Math.max(x1, x2);
            Config.maxY = Math.max(y1, y2);
            Config.maxZ = Math.max(z1, z2);
        }
    }

}
