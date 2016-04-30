package com.thetonyk.CommandsHub.Listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import com.thetonyk.CommandsHub.Utils.PermissionsUtils;

public class LeaveListener implements Listener{
	
	@EventHandler
	public void onLeave(PlayerQuitEvent event) {
		
		Player player = event.getPlayer();
		
		player.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
		event.setQuitMessage("");
		
		PermissionsUtils.clearPermissions(player);
		
		if (player.isInsideVehicle()) player.leaveVehicle();

		for (Player players : Bukkit.getOnlinePlayers()) {
			
			if (players.getScoreboard().getTeam(player.getName()) != null) players.getScoreboard().getTeam(player.getName()).unregister();
		
		}
		
	}

}
