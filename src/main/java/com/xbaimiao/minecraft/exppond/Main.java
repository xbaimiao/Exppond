package com.xbaimiao.minecraft.exppond;

import com.xbaimiao.minecraft.exppond.command.CommandExecute;
import com.xbaimiao.minecraft.exppond.core.Config;
import com.xbaimiao.minecraft.exppond.core.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashSet;
import java.util.Objects;

public class Main extends JavaPlugin implements Listener {

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
        Bukkit.getPluginManager().registerEvents(this, this);
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

    @EventHandler
    public void drop(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        if (inExpPond(player)) {
            event.setCancelled(true);
        }
    }

    // 玩家是否在经验内
    private boolean inExpPond(Player player) {
        Location location = player.getLocation().clone();
        if (Objects.requireNonNull(location.getWorld()).getName().equals(Config.getWorld().getName())) {
            if (Utils.in(location.getBlockX(), Config.maxX, Config.minX)) {
                if (Utils.in(location.getBlockY(), Config.maxY, Config.minY)) {
                    return Utils.in(location.getBlockZ(), Config.maxZ, Config.minZ);
                }
            }
        }
        return false;
    }


    /**
     * @return 经验池内是否有玩家
     */
    private boolean check() {
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if (inExpPond(onlinePlayer)) {
                //进入经验池
                if (!sendTitles.contains(onlinePlayer.getName())) {
                    Config.getTitleJoin().sendTo(onlinePlayer);
                    sendTitles.add(onlinePlayer.getName());
                }
                return true;
            }
            //离开
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
