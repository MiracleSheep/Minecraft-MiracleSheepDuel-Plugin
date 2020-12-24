package com.MiracleSheep.DuelPlugin.Inventory;

import com.MiracleSheep.DuelPlugin.DuelPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.Console;
import java.io.File;
import java.io.ObjectInputFilter;
import java.util.*;

public class DuelSelection implements InventoryHolder {

    private Inventory inv;
    private final DuelPlugin main;
    int inventorysize = 9;


    public DuelSelection(DuelPlugin main) {


        if (main.getConfig().getConfigurationSection("Kits").getKeys(false).size() > 9 && main.getConfig().getConfigurationSection("Kits").getKeys(false).size() <= 18) {
            inventorysize = 18;
        } else if (main.getConfig().getConfigurationSection("Kits").getKeys(false).size() > 18 && main.getConfig().getConfigurationSection("Kits").getKeys(false).size() <= 27) {
            inventorysize = 27;
        } else if (main.getConfig().getConfigurationSection("Kits").getKeys(false).size() > 27 && main.getConfig().getConfigurationSection("Kits").getKeys(false).size() <= 36) {
            inventorysize = 36;
        } else if (main.getConfig().getConfigurationSection("Kits").getKeys(false).size() > 36 && main.getConfig().getConfigurationSection("Kits").getKeys(false).size() <= 45) {
            inventorysize = 45;
        } else if (main.getConfig().getConfigurationSection("Kits").getKeys(false).size() > 45 && main.getConfig().getConfigurationSection("Kits").getKeys(false).size() <= 54) {
            inventorysize = 54;
        }
        inv = Bukkit.createInventory(this,inventorysize,"DuelSelection");//max size 54
        this.main = main;
        init(this.main);

    }

    private void init(DuelPlugin main) {
        ItemStack item;
        int kitnum = main.getConfig().getConfigurationSection("Kits").getKeys(false).size();
        String[] name = main.getConfig().getConfigurationSection("Kits").getKeys(false).toArray(new String[0]);

        for (int g = 0; g < main.getConfig().getConfigurationSection("Kits").getKeys(false).size(); g++) {

            Material icon = Material.valueOf(String.valueOf(main.getConfig().getString("Kits." + name[g] + ".Icon")));
            item = createItem(name[g] ,icon, Collections.singletonList("Kit #" + (g+1)));
            inv.setItem(g,item);

            }

        for (int i = kitnum; i < inventorysize ; i++) {
            item = createItem("None",Material.WHITE_STAINED_GLASS_PANE, Collections.singletonList("Not a kit"));
            inv.setItem(i,item);
        }


    }



    private ItemStack createItem(String name, Material mat, List<String> lore) {
        ItemStack item = new ItemStack(mat,1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;

    }


    @Override
    public Inventory getInventory() {
        return inv;
    }
}

