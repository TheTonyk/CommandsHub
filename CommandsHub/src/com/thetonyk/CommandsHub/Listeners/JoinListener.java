package com.thetonyk.CommandsHub.Listeners;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.util.Vector;

import com.thetonyk.CommandsHub.Main;
import com.thetonyk.CommandsHub.Utils.DisplayUtils;
import com.thetonyk.CommandsHub.Utils.PlayerUtils;

import com.thetonyk.CommandsHub.Utils.PermissionsUtils;

public class JoinListener implements Listener {
	
	@EventHandler
	public void onConnect(PlayerLoginEvent event) {
		
		PermissionsUtils.setPermissions(event.getPlayer());
		
		if (event.getResult() == Result.KICK_WHITELIST) {
			
			if (event.getPlayer().isOp() || event.getPlayer().hasPermission("global.bypasswhitelist")) {
				
				event.allow();
				return;
				
			}
			
			event.setKickMessage("§8⫸ §7You are not whitelisted §8⫷");
			
		}
		
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		
		Player player = event.getPlayer();
		
		DisplayUtils.sendTitle(player, "§aCommandsPVP", "§7Welcome on the §aHub §7⋯ §a" + Bukkit.getOnlinePlayers().size() + " §7players onlines", 0, 40, 10);
		
		event.setJoinMessage("§7[§a+§7] " + PlayerUtils.getRank(player.getName()).getPrefix() + "§7" + player.getName());
		
		if (player.isDead()) player.spigot().respawn();

		if (!Main.bar.getPlayers().contains(player)) Main.bar.addPlayer(player);

		player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
		player.getScoreboard().registerNewObjective("sidebar", "dummy");
		player.getScoreboard().getObjective("sidebar").setDisplaySlot(DisplaySlot.SIDEBAR);
		player.getScoreboard().getObjective("sidebar").setDisplayName(Main.PREFIX + "§oCommandsPVP");
		player.getScoreboard().registerNewObjective("buffer", "dummy");
		player.getScoreboard().getObjective("buffer").setDisplayName(Main.PREFIX + "§oCommandsPVP");
		
		PlayerUtils.updateNametag(player.getName());
		PlayerUtils.updateScoreboard(player);	
		
		Location spawn = Bukkit.getWorld("world").getSpawnLocation().add(0.5, 0, 0.5);
		spawn.setDirection(new Vector(0, 15, -50));
		player.teleport(spawn);
		player.setGameMode(GameMode.ADVENTURE);
		DisplayUtils.hideCoord(event.getPlayer());
		
		PlayerUtils.clearInventory(player);
		PlayerUtils.clearXp(player);
		PlayerUtils.feed(player);
		PlayerUtils.heal(player);
		PlayerUtils.clearEffects(player);
		player.setExp(0);
		player.setTotalExperience(0);
		PlayerUtils.placeItems(player);
		
		if (player.hasPermission("global.fly")) player.setAllowFlight(true);
		
		for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
			
			if (!onlinePlayer.equals(player)) {
			
				if (PlayerUtils.getPlayersVisibility(player) == 0 && !onlinePlayer.hasPermission("hub.visible")) player.hidePlayer(onlinePlayer);
				
				if (PlayerUtils.getPlayersVisibility(onlinePlayer) == 0 && !player.hasPermission("hub.visible")) onlinePlayer.hidePlayer(player);
				
			}
			
		}
		
	}

}
