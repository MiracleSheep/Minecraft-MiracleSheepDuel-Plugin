package com.MiracleSheep.DuelPlugin;

import com.MiracleSheep.DuelPlugin.DuelPlugin;
import org.bukkit.entity.Player;

public class Acceptclass {

    private final DuelPlugin main;
    String dueltype;

    public Acceptclass(DuelPlugin main) {
        this.main = main;
    }


    public void setType(int i) {
        dueltype = main.getConfig().getStringList("KitNames").get(i);
    }

}