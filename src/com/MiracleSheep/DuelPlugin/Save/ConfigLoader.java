package com.MiracleSheep.DuelPlugin.Save;

import com.MiracleSheep.DuelPlugin.DuelPlugin;
import com.onarandombox.MultiverseCore.MVWorld;
import com.onarandombox.MultiverseCore.api.MultiverseWorld;
import com.onarandombox.MultiverseCore.event.MVTeleportEvent;
import com.onarandombox.MultiverseCore.utils.WorldManager;
import org.bukkit.*;

import java.io.File;

public class ConfigLoader {


    private final DuelPlugin main;

    public ConfigLoader(DuelPlugin main) {
        this.main = main;

    }


 /*   public MultiverseWorld getMultiverseWorld(){

    String duelworld = String.valueOf(main.getConfig().getString("worldname"));
    MultiverseWorld d = main.passManager().getMVWorld(duelworld);
        return (d);

    }*/

    public World GetWorld() {
        String duelworld = String.valueOf(main.getConfig().getString("worldname"));
        World d = Bukkit.getServer().getWorld(duelworld);
        return (d);
    }

    public World GetCopyWorld() {
        String duelworld = String.valueOf(main.getConfig().getString("worldname"));
        World d = Bukkit.getServer().getWorld(duelworld + "_temp" + main.Worldnum);
        return (d);
    }




    public Location getPlayerOneSpawn(String Arena) {
        int x = main.getConfig().getInt("Stadiums." + Arena + ".PlayerOne.X");
        int y = main.getConfig().getInt("Stadiums." + Arena + ".PlayerOne.Y");
        int z = main.getConfig().getInt("Stadiums." + Arena + ".PlayerOne.Z");

        if (Clone() == true) {
            Location playerone = new Location(GetCopyWorld(), x, y, z);
            return(playerone);
        } else {
            Location playerone = new Location(GetWorld(), x, y, z);
            return(playerone);
        }

    }

    public Location getPlayerTwoSpawn(String Arena) {
        int x = main.getConfig().getInt("Stadiums." + Arena + ".PlayerTwo.X");
        int y = main.getConfig().getInt("Stadiums." + Arena + ".PlayerTwo.Y");
        int z = main.getConfig().getInt("Stadiums." + Arena + ".PlayerTwo.Z");


        if (Clone() == true) {
            Location playertwo = new Location(GetCopyWorld(), x, y, z);
            return(playertwo);
        } else {
            Location playertwo = new Location(GetWorld(), x, y, z);
            return(playertwo);
        }
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

    public boolean Clone() {
        boolean clone = main.getConfig().getBoolean("CloneWorld");
        return(clone);
    }

    public String getArena(String dueltype) {
        String arena = main.getConfig().getString("Kits." + dueltype + ".Arena");

        return(arena);

    }

    public File getDelete() {
        World delete = GetCopyWorld();
        File deleteFolder = delete.getWorldFolder();

        return (deleteFolder);
    }

    public File sourceWorld() {
        World source = GetWorld();
        File sourceFolder = source.getWorldFolder();
        return(sourceFolder);
    }

    public File targetWorld() {

        World target = GetCopyWorld();
        File targetFolder = target.getWorldFolder();
        return (targetFolder);
    }

    public String GetWorldName() {
        String name = main.getConfig().getString("worldname");
        return(name);
    }

    public String GetCopyWorldName() {
        String name = main.getConfig().getString("worldname") + "_temp" + main.Worldnum;
        return(name);

    }







}
