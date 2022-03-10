package com.xbaimiao.minecraft.exppond;

import com.google.gson.annotations.SerializedName;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

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

    public void sendTo(@NotNull Player player) {
        player.sendTitle(main, sub, 20, 30, 20);
    }

}