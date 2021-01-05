package com.MiracleSheep.DuelPlugin.Events;

import com.MiracleSheep.DuelPlugin.DuelPlugin;
import com.MiracleSheep.DuelPlugin.Inventory.DuelSelection;
import com.MiracleSheep.DuelPlugin.Save.ConfigLoader;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
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
                int kitnum = main.getConfig().getConfigurationSection("Kits").getKeys(false).size();

                if (e.getCurrentItem() == null) {
                    return;
                }

                if (slotnum >= 0 && slotnum < kitnum) {
                    main.sendRequest();
                    main.save.setType(e.getSlot());
                    main.getRequester(main.Worldnum).closeInventory();


                } else if (slotnum >= kitnum) {
                    player.sendMessage(ChatColor.DARK_RED + "That is not a valid kit");
                }


            }
        } catch (ClassCastException f) {
            f.getStackTrace();
        }

    }


    @EventHandler
    public void onDisconnect(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        String name = main.getConfig().getString("worldname");
        World w = Bukkit.getServer().getWorld("world");
        if (player.getWorld().getName().startsWith(name)) {
            for (int i = 1 ; i <= main.Worldnum ; i++) {
                player.sendMessage(ChatColor.RED + "Somebody disconnected, you will both be sent home.");
                main.dequipkit(player);
                main.dequipkit(main.getRequested(i));
                main.getRequester(i).setFireTicks(0);
                main.getRequested(i).setFireTicks(0);
                main.getRequested(i).setHealth(20);
                main.getRequester(i).setHealth(20);
                main.getRequester(i).setFoodLevel(20);
                main.getRequested(i).setFoodLevel(20);
                main.getRequested(i).teleport(w.getSpawnLocation());
                main.getRequester(i).teleport(w.getSpawnLocation());
                main.getMultiverseCore().getCore().getMVWorldManager().deleteWorld(main.load.GetCopyWorldName());

            }
        }

    }


    @EventHandler
    public void onDamage(EntityDamageEvent e) {

        if (!(e.getEntity() instanceof Player)) {return;}


            Player player = (Player) e.getEntity();
        String duelworld = String.valueOf(main.getConfig().getString("worldname"));
        World w = Bukkit.getServer().getWorld("world");
        World d = Bukkit.getServer().getWorld(duelworld);
        for (int i = 1; i <= main.Worldnum; i ++) {

            if (main.getRequested(i).getWorld() == main.load.GetCopyWorld(i) && main.getRequester(i).getWorld() == main.load.GetCopyWorld(i) || main.getRequested(i).getWorld() == main.load.GetWorld() && main.getRequester(i).getWorld() == main.load.GetWorld()) {
                if (main.getRequester(i) == player) {
                    if (player.getHealth() - e.getDamage() < 0.1) {
                        e.setCancelled(true);
                        player.sendMessage(ChatColor.RED + "You lost... sending you home...");
                        main.dequipkit(player);
                        main.getRequested(i).sendMessage(ChatColor.GREEN + "You won the duel! Sending you home...");
                        main.dequipkit(main.getRequested(i));
                        main.getRequester(i).setFireTicks(0);
                        main.getRequested(i).setFireTicks(0);
                        main.getRequested(i).setHealth(20);
                        main.getRequester(i).setHealth(20);
                        main.getRequester(i).setFoodLevel(20);
                        main.getRequested(i).setFoodLevel(20);
                        main.getRequested(i).teleport(w.getSpawnLocation());
                        main.getRequester(i).teleport(w.getSpawnLocation());
                        main.getMultiverseCore().getCore().getMVWorldManager().deleteWorld(main.load.GetCopyWorldName());

                    }

                } else if (main.getRequested(i) == player) {
                    if (player.getHealth() - e.getDamage() < 0.1) {
                        e.setCancelled(true);
                        player.sendMessage(ChatColor.RED + "You lost... sending you home...");
                        main.dequipkit(player);
                        main.getRequester(i).sendMessage(ChatColor.GREEN + "You won the duel! Sending you home...");
                        main.dequipkit(main.getRequester(i));
                        main.getRequester(i).setFireTicks(0);
                        main.getRequested(i).setFireTicks(0);
                        main.getRequested(i).setHealth(20);
                        main.getRequester(i).setHealth(20);
                        main.getRequester(i).setFoodLevel(20);
                        main.getRequested(i).setFoodLevel(20);
                        main.getRequested(i).teleport(w.getSpawnLocation());
                        main.getRequester(i).teleport(w.getSpawnLocation());
                        main.getMultiverseCore().getCore().getMVWorldManager().deleteWorld(main.load.GetCopyWorldName());

                    }
                }


            }
        }

    }
}