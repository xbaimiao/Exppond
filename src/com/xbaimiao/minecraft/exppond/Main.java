package com.xbaimiao.minecraft.exppond;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class Main extends JavaPlugin {

    private static Main instance;
    private static BukkitTask runnable;
    private static final ArrayList<String> sendTitles = new ArrayList<>();

    public void onEnable() {
        (Main.instance = this).saveDefaultConfig();
        task();
    }

    public static Main getInstance() {
        return Main.instance;
    }

    private void task() {
        runnable = new BukkitRunnable() {
            public void run() {
                if (!check()) {
                    return;
                }
                for (int x = 0; x < Config.amount; ++x) {
                    Config.getWorld().spawnEntity(Config.getRandom(), EntityType.THROWN_EXP_BOTTLE);
                }
            }
        }.runTaskTimer(this, Config.speed, Config.speed);
    }

    /**
     * @return 经验池内是否有玩家
     */
    private boolean check() {
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            Location location = onlinePlayer.getLocation();
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
            //不在经验池
            if (sendTitles.contains(onlinePlayer.getName())) {
                Config.getTitleQuit().sendTo(onlinePlayer);
                sendTitles.remove(onlinePlayer.getName());
            }
        }
        return false;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!sender.isOp()) {
            return true;
        }
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length <= 0) {
                sender.sendMessage("§a usage: a | b | reload");
                return true;
            }
            switch (args[0]) {
                case "a":
                    setLocation(player, "1");
                    sender.sendMessage("设置a点成功");
                    reloadConfig();
                    Config.load();
                    runnable.cancel();
                    task();
                    break;
                case "b":
                    setLocation(player, "2");
                    sender.sendMessage("设置b点成功");
                    reloadConfig();
                    Config.load();
                    runnable.cancel();
                    task();
                    break;
                case "reload":
                    reloadConfig();
                    Config.load();
                    runnable.cancel();
                    task();
                    sender.sendMessage("重载配置文件完成");
                    break;
            }
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        return new ArrayList<String>() {{
            add("a");
            add("b");
            add("reload");
        }};
    }

    private void setLocation(Player player, String key) {
        Location location = player.getLocation().clone();
        getConfig().set("location.world", player.getWorld().getName());
        getConfig().set("location." + key, String.format("%s/%s/%s", location.getBlockX(), location.getBlockY(), location.getBlockZ()));
        saveConfig();
    }

}
