package com.thetonyk.CommandsHub.Listeners;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockGrowEvent;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.weather.WeatherChangeEvent;

public class EnvironmentListener implements Listener {

	@EventHandler
	public void onWeatherChange(WeatherChangeEvent event) {
		
		event.setCancelled(true);
		
	}
	
	@EventHandler
	public void onRedstoneUpdate(BlockRedstoneEvent event) {
		
		if (!event.getBlock().getWorld().getName().equalsIgnoreCase("world") || event.getBlock().getType() != Material.REDSTONE_LAMP_ON) return;
			
		event.setNewCurrent(event.getOldCurrent());
		
	}
	
	@EventHandler
	public void onExplode(EntityExplodeEvent event) {
		
		if (!event.getEntity().getWorld().getName().equalsIgnoreCase("world") || event.getEntityType() != EntityType.ENDER_CRYSTAL) return;
			
		event.setCancelled(true);
		
	}
	
	@EventHandler
	public void onGrow(BlockGrowEvent event) {
		
		if (!event.getBlock().getWorld().getName().equalsIgnoreCase("world") || !event.getNewState().getType().equals(Material.SUGAR_CANE_BLOCK)) return;
			
		event.setCancelled(true);
		
	}
	
}
