package com.xbaimiao.minecraft.exppond;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

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
            if (location.getWorld().getName().equals(Config.getWorld().getName())){
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

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
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
                    break;
                case "b":
                    setLocation(player, "2");
                    sender.sendMessage("设置b点成功");
                    break;
                case "reload":
                    reload();
                    sender.sendMessage("重载配置文件完成");
                    break;
            }
        }
        return true;
    }

    /**
     * 重载插件
     */
    private void reload() {
        reloadConfig();
        Config.load();
        runnable.cancel();
        runnable = spawnTask();
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
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