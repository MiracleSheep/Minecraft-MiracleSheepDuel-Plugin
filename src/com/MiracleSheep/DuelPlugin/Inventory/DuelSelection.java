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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class DuelSelection implements InventoryHolder {

    private Inventory inv;
    private final DuelPlugin main;


    public DuelSelection(DuelPlugin main) {
        inv = Bukkit.createInventory(this,9,"DuelSelection");//max size 54
        this.main = main;
        init(this.main);

    }

    private void init(DuelPlugin main) {
        ItemStack item;



        for (int g = 0; g < main.getConfig().getStringList("KitNames").size(); g++) {
            String name = String.valueOf(main.getConfig().getStringList("KitNames").get(g));
            Material icon = Material.valueOf(String.valueOf(main.getConfig().getString(name)));
            item = createItem(name ,icon, Collections.singletonList("Kit #" + (g+1)));
            inv.setItem(g,item);

            }

        for (int i = main.getConfig().getStringList("KitNames").size(); i < 9 ; i++) {
            item = createItem("None",Material.WHITE_STAINED_GLASS_PANE, Collections.singletonList("Not a kit"));
            inv.setItem(i,item);
        }

/*        List<String> lore = new ArrayList<>();
        lore.add("Please enter the accept");
        lore.add("or deny button");
        item = createItem("Make a selection", Material.BOOK,lore);
        inv.setItem(inv.firstEmpty(),item);*/


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

