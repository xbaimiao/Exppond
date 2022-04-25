package com.xbaimiao.minecraft.exppond.core;

import com.google.gson.annotations.SerializedName;
import org.bukkit.entity.Player;

/**
 * @author xbaimiao
 * @date 2022/1/25 10:09 AM
 * @email 3104026189@qq.com
 */
public class Title {

    @SerializedName("main")
    public String main;
    @SerializedName("sub")
    public String sub;

    public void sendTo(Player player) {
        player.sendTitle(main, sub, 20, 30, 20);
    }

}