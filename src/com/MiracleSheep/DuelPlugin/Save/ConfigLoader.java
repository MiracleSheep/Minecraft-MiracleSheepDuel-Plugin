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

    public World GetWorld() {
        String duelworld = String.valueOf(main.getConfig().getString("worldname"));
        World d = Bukkit.getServer().getWorld(duelworld);
        return (d);
    }


    public Location getPlayerOneSpawn(String Arena) {
        int x = main.getConfig().getInt("Stadiums." + Arena + ".PlayerOne.X");
        int y = main.getConfig().getInt("Stadiums." + Arena + ".PlayerOne.Y");
        int z = main.getConfig().getInt("Stadiums." + Arena + ".PlayerOne.Z");
        Location playerone = new Location(GetWorld(), x, y, z);
        return(playerone);
    }

    public Location getPlayerTwoSpawn(String Arena) {
        int x = main.getConfig().getInt("Stadiums." + Arena + ".PlayerTwo.X");
        int y = main.getConfig().getInt("Stadiums." + Arena + ".PlayerTwo.Y");
        int z = main.getConfig().getInt("Stadiums." + Arena + ".PlayerTwo.Z");

        Location playertwo = new Location(GetWorld(), x, y, z);
        return(playertwo);
    }

    public Location getSpectatorSpawn(String Arena){
        int x = main.getConfig().getInt("Stadiums." + Arena + ".Spectator.X");
        int y = main.getConfig().getInt("Stadiums." + Arena + ".Spectator.Y");
        int z = main.getConfig().getInt("Stadiums." + Arena + ".Spectator.Z");
        Location spectator = new Location(GetWorld(), x, y, z);
        return(spectator);
    }

    public boolean IllegalEnchants() {
        boolean Illegalenchants = main.getConfig().getBoolean("IllegalEnchants");
        return(Illegalenchants);
    }


}
