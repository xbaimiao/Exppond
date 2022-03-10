package com.xbaimiao.minecraft.exppond;

import com.xbaimiao.minecraft.exppond.command.CommandExecute;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashSet;

public class Main extends JavaPlugin {

    private static Main instance;
    private static BukkitTask runnable;
    private static final HashSet<String> sendTitles = new HashSet<>();
    /**
     * 经验池内是否有玩家
     */
    private static boolean hasPlayer = false;

    public void onEnable() {
        (Main.instance = this).saveDefaultConfig();
        runnable = spawnTask();
        Bukkit.getScheduler().runTaskTimerAsynchronously(this, () -> hasPlayer = check(), 20, 20);
        getCommand("exppond").setExecutor(new CommandExecute());
    }

    public static Main getInstance() {
        return Main.instance;
    }

    private BukkitTask spawnTask() {
        return Bukkit.getScheduler().runTaskTimer(this, () -> {
            if (!hasPlayer) {
                return;
            }
            for (int x = 0; x < Config.amount; ++x) {
                Config.getWorld().spawnEntity(Config.getRandom(), EntityType.THROWN_EXP_BOTTLE);
            }
        }, Config.speed, Config.speed);
    }

    /**
     * @return 经验池内是否有玩家
     */
    private boolean check() {
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            Location location = onlinePlayer.getLocation().clone();
            if (location.getWorld().getName().equals(Config.getWorld().getName())) {
                if (Utils.in(location.getBlockX(), Config.maxX, Config.minX)) {
                    if (Utils.in(location.getBlockY(), Config.maxY, Config.minY)) {
                        if (Utils.in(location.getBlockZ(), Config.maxZ, Config.minZ)) {
                            //在经验池
                            if (!sendTitles.contains(onlinePlayer.getName())) {
                                Config.getTitleJoin().sendTo(onlinePlayer);
                                sendTitles.add(onlinePlayer.getName());
                            }
                            return true;
                        }
                    }
                }
            }
            //不在经验池
            if (sendTitles.contains(onlinePlayer.getName())) {
                Config.getTitleQuit().sendTo(onlinePlayer);
                sendTitles.remove(onlinePlayer.getName());
            }
        }
        return false;
    }

    /**
     * 重载插件
     */
    public void reload() {
        reloadConfig();
        Config.load();
        runnable.cancel();
        runnable = spawnTask();
    }

    public void setLocation(Player player, String key) {
        Location location = player.getLocation().clone();
        getConfig().set("location.world", player.getWorld().getName());
        getConfig().set("location." + key, String.format("%s/%s/%s", location.getBlockX(), location.getBlockY(), location.getBlockZ()));
        saveConfig();
    }

}
