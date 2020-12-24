package com.MiracleSheep.DuelPlugin.Save;

import com.MiracleSheep.DuelPlugin.DuelPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;

public class ConfigLoader {


    private final DuelPlugin main;

    public ConfigLoader(DuelPlugin main) {
        this.main = main;

    }


    public Location getPlayerOneSpawn(String Arena) {
        World w = Bukkit.getServer().getWorld("world");
        World d = Bukkit.getServer().getWorld("Arena");

        int x = main.getConfig().getInt("Stadiums." + Arena + ".PlayerOne.X");
        int y = main.getConfig().getInt("Stadiums." + Arena + ".PlayerOne.Y");
        int z = main.getConfig().getInt("Stadiums." + Arena + ".PlayerOne.Z");
        Location playerone = new Location(d, x, y, z);
        return(playerone);
    }

    public Location getPlayerTwoSpawn(String Arena) {
        World w = Bukkit.getServer().getWorld("world");
        World d = Bukkit.getServer().getWorld("Arena");


        int x = main.getConfig().getInt("Stadiums." + Arena + ".PlayerTwo.X");
        int y = main.getConfig().getInt("Stadiums." + Arena + ".PlayerTwo.Y");
        int z = main.getConfig().getInt("Stadiums." + Arena + ".PlayerTwo.Z");

        Location playertwo = new Location(d, x, y, z);
        return(playertwo);
    }

    public Location getSpectatorSpawn(String Arena){
        World w = Bukkit.getServer().getWorld("world");
        World d = Bukkit.getServer().getWorld("Arena");

        int x = main.getConfig().getInt("Stadiums." + Arena + ".Spectator.X");
        int y = main.getConfig().getInt("Stadiums." + Arena + ".Spectator.Y");
        int z = main.getConfig().getInt("Stadiums." + Arena + ".Spectator.Z");
        Location spectator = new Location(d, x, y, z);
        return(spectator);
    }


}
