package com.xbaimiao.minecraft.exppond.command;

import com.xbaimiao.minecraft.exppond.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xbaimiao
 * @date 2022/1/25 11:26 AM
 * @email 3104026189@qq.com
 */
public class CommandExecute implements TabExecutor {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return new ArrayList<String>() {{
            add("a");
            add("b");
            add("reload");
        }};
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
                    Main.getInstance().setLocation(player, "1");
                    sender.sendMessage("设置a点成功");
                    break;
                case "b":
                    Main.getInstance().setLocation(player, "2");
                    sender.sendMessage("设置b点成功");
                    break;
                case "reload":
                    Main.getInstance().reload();
                    sender.sendMessage("重载配置文件完成");
                    break;
            }
        }
        return true;
    }


}
