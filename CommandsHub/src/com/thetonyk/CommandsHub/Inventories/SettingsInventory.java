package com.thetonyk.CommandsHub.Inventories;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.thetonyk.CommandsHub.Utils.ItemsUtils;
import com.thetonyk.CommandsHub.Utils.PlayerUtils;

public class SettingsInventory {
	
	public static Inventory getSettings(Player player) {
		
		Inventory inventory = Bukkit.createInventory(null, 9, "§8⫸ §4Settings");
			
		String state1 = PlayerUtils.getPlayersVisibility(player) == 1 ? "§aEnabled" : "§cDisabled";
		ArrayList<String> lore1 = new ArrayList<String>();
		lore1.add("");
		lore1.add("   §7Enable or disable the visibility   ");
		lore1.add("   §7of the others player in the hub.   ");
		lore1.add("");
		lore1.add("   §cOnly work in hub's.");
		lore1.add("");
		ItemStack item1 = ItemsUtils.createItem(Material.EYE_OF_ENDER, "§8⫸ §7Player Visibility§8: " + state1 + " §8⫷", 1, 0, lore1);
		if (PlayerUtils.getPlayersVisibility(player) == 1) item1 = ItemsUtils.addGlow(item1);
		item1 = ItemsUtils.hideFlags(item1);
		inventory.setItem(1, item1);
		
		String state2 = PlayerUtils.getChatVisibility(player) == 1 ? "§aEnabled" : "§cDisabled";
		ArrayList<String> lore2 = new ArrayList<String>();
		lore2.add("");
		lore2.add("   §7Enable or disable the visibility of   ");
		lore2.add("   §7the chat in the hub.   ");
		lore2.add("");
		ItemStack item2 = ItemsUtils.createItem(Material.PAPER, "§8⫸ §7Chat Visibility§8: " + state2 + " §8⫷", 1, 0, lore2);
		if (PlayerUtils.getChatVisibility(player) == 1) item2 = ItemsUtils.addGlow(item2);
		item2 = ItemsUtils.hideFlags(item2);
		inventory.setItem(3, item2);
		
		String state3 = PlayerUtils.getMentionsState(player) == 1 ? "§aEnabled" : "§cDisabled";
		ArrayList<String> lore3 = new ArrayList<String>();
		lore3.add("");
		lore3.add("   §7When this option is enabled, you will   ");
		lore3.add("   §7get a sound notification whenever your   ");
		lore3.add("   §7name is mentioned in the chat.   ");
		lore3.add("");
		ItemStack item3 = ItemsUtils.createItem(Material.MAP, "§8⫸ §7Mention Alerts§8: " + state3 + " §8⫷", 1, 0, lore3);
		if (PlayerUtils.getMentionsState(player) == 1) item3 = ItemsUtils.addGlow(item3);
		item3 = ItemsUtils.hideFlags(item3);
		inventory.setItem(5, item3);
		
		String state4 = PlayerUtils.getPrivateState(player) == 1 ? "§aEnabled" : "§cDisabled";
		ArrayList<String> lore4 = new ArrayList<String>();
		lore4.add("");
		lore4.add("   §7Enable or disable the ability of   ");
		lore4.add("   §7others players being able to   ");
		lore4.add("   §7private message you.   ");
		lore4.add("");
		ItemStack item4 = ItemsUtils.createItem(Material.BOOK_AND_QUILL, "§8⫸ §7Private Messages§8: " + state4 + " §8⫷", 1, 0, lore4);
		if (PlayerUtils.getPrivateState(player) == 1) item4 = ItemsUtils.addGlow(item4);
		item4 = ItemsUtils.hideFlags(item4);
		inventory.setItem(7, item4);
		
		ItemStack separator = ItemsUtils.createItem(Material.STAINED_GLASS_PANE, "§7CommandsPVP", 1, 7);
		
		for (int i = 0; i < inventory.getSize(); i++) {
			
			if (inventory.getItem(i) != null) continue;
				
			inventory.setItem(i, separator);

		}
		
		return inventory;
		
	}

}
