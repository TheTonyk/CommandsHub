package com.thetonyk.CommandsHub.Listeners;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;

import com.thetonyk.CommandsHub.Inventories.SettingsInventory;
import com.thetonyk.CommandsHub.Utils.PlayerUtils;

public class InventoryListener implements Listener {
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {	
		
		if (event.getWhoClicked().getGameMode() != GameMode.ADVENTURE) return;
		
		if (event.getInventory().getTitle().equals("§8⫸ §4Settings")) {
			
			event.setCancelled(true);
			
			if (event.getCurrentItem() != null && event.getCurrentItem().hasItemMeta() && event.getCurrentItem().getItemMeta().hasDisplayName()) {
				
				if (event.getCurrentItem().getItemMeta().getDisplayName().startsWith("§8⫸ §7Player Visibility")) {
					
					PlayerUtils.setPlayersVisibility((Player) event.getWhoClicked());
					
					if (PlayerUtils.getPlayersVisibility((Player) event.getWhoClicked()) == 1) {
						
						for (Player player : Bukkit.getOnlinePlayers()) {
							
							((Player) event.getWhoClicked()).showPlayer(player);
							
						}
						
					} else {
						
						for (Player player : Bukkit.getOnlinePlayers()) {
							
							if (!player.equals((Player) event.getWhoClicked()) && !player.hasPermission("hub.visible")) ((Player) event.getWhoClicked()).hidePlayer(player);
							
						}
						
					}
					
					((Player) event.getWhoClicked()).playSound(event.getWhoClicked().getLocation(), Sound.ORB_PICKUP, 1, 1);
					event.getWhoClicked().openInventory(SettingsInventory.getSettings((Player) event.getWhoClicked()));
					return;
					
				}
				else if (event.getCurrentItem().getItemMeta().getDisplayName().startsWith("§8⫸ §7Chat Visibility")) {
					
					PlayerUtils.setChatVisibility((Player) event.getWhoClicked());
					((Player) event.getWhoClicked()).playSound(event.getWhoClicked().getLocation(), Sound.ORB_PICKUP, 1, 1);
					event.getWhoClicked().openInventory(SettingsInventory.getSettings((Player) event.getWhoClicked()));
					return;
					
				}
				else if (event.getCurrentItem().getItemMeta().getDisplayName().startsWith("§8⫸ §7Mention Alerts")) {
					
					PlayerUtils.setMentionsState((Player) event.getWhoClicked());
					((Player) event.getWhoClicked()).playSound(event.getWhoClicked().getLocation(), Sound.ORB_PICKUP, 1, 1);
					event.getWhoClicked().openInventory(SettingsInventory.getSettings((Player) event.getWhoClicked()));
					return;
					
				}
				else if (event.getCurrentItem().getItemMeta().getDisplayName().startsWith("§8⫸ §7Private Messages")) {
					
					PlayerUtils.setPrivateState((Player) event.getWhoClicked());
					((Player) event.getWhoClicked()).playSound(event.getWhoClicked().getLocation(), Sound.ORB_PICKUP, 1, 1);
					event.getWhoClicked().openInventory(SettingsInventory.getSettings((Player) event.getWhoClicked()));
					return;
					
				}
			
			}
			
		}
		
		if (event.getWhoClicked().getWorld().getName().equals("world") && event.getAction() == InventoryAction.HOTBAR_SWAP) {

			event.setCancelled(true);
			return;
				
		}
				
		if (event.getWhoClicked().getWorld().getName().equals("world")) {
					
			event.setCancelled(true);
			return;
		
		}
			
	}

}
