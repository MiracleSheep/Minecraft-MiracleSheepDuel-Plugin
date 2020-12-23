package com.MiracleSheep.DuelPlugin.Events;

import com.MiracleSheep.DuelPlugin.DuelPlugin;
import com.MiracleSheep.DuelPlugin.Inventory.DuelSelection;
import com.MiracleSheep.DuelPlugin.Save.ConfigLoader;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;

import java.net.http.WebSocket;

public class DuelPluginEvents implements Listener {

    private final DuelPlugin main;

    public DuelPluginEvents(DuelPlugin main) {
        this.main = main;
    }

    //making sure listener sees this
    @EventHandler
    public static void onPlayerJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        player.sendMessage(ChatColor.DARK_PURPLE + "[MiracleSheepDuelPlugin] plugin is enabled.");

    }


    @EventHandler
    public void onClick(InventoryClickEvent e) throws ClassCastException {




        if (e.getClickedInventory() == null) {
            return;
        }
        try {
            if (e.getClickedInventory().getHolder() instanceof DuelSelection) {
                e.setCancelled(true);
                Player player = (Player) e.getWhoClicked();
                int slotnum = e.getSlot();

                if (e.getCurrentItem() == null) {
                    return;
                }

                if (slotnum >= 0 && slotnum < main.getConfig().getStringList("KitNames").size()) {
                    main.sendRequest();
                    main.save.setType(e.getSlot());
                    main.getRequester().closeInventory();


                } else if (slotnum >= main.getConfig().getStringList("KitNames").size()) {
                    player.sendMessage(ChatColor.DARK_RED + "That is not a valid kit");
                }


            }
        } catch (ClassCastException f) {
            f.getStackTrace();
        }

    }


    @EventHandler
    public void onDamage(EntityDamageEvent e) {



        if (e.getEntity() instanceof Player) {
            Player player = (Player) e.getEntity();
        World w = Bukkit.getServer().getWorld("world");
        World d = Bukkit.getServer().getWorld("Arena");

        if (main.getRequested().getWorld() == d && main.getRequester().getWorld() == d) {

            if (main.getRequester() == player) {
                if (player.getHealth() - e.getDamage() < 1.5) {
                    e.setCancelled(true);
                    player.sendMessage(ChatColor.RED + "You lost... sending you home...");
                    main.dequipkit(player);
                    main.getRequested().sendMessage(ChatColor.GREEN + "You won the duel! Sending you home...");
                    main.dequipkit(main.getRequested());
                    main.getRequested().setHealth(20);
                    main.getRequester().setHealth(20);
                    main.getRequested().teleport(w.getSpawnLocation());
                    main.getRequester().teleport(w.getSpawnLocation());
                }

            } else if (main.getRequested() == player) {
                if (player.getHealth() - e.getDamage() < 1.5) {
                    e.setCancelled(true);
                    player.sendMessage(ChatColor.RED + "You lost... sending you home...");
                    main.dequipkit(player);
                    main.getRequester().sendMessage(ChatColor.GREEN + "You won the duel! Sending you home...");
                    main.dequipkit(main.getRequester());
                    main.getRequested().setHealth(20);
                    main.getRequester().setHealth(20);
                    main.getRequested().teleport(w.getSpawnLocation());
                    main.getRequester().teleport(w.getSpawnLocation());
                }
            }
        }

        }









        Player p = (Player) e.getEntity();

    }
}