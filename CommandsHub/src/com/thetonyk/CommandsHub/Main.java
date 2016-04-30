package com.thetonyk.CommandsHub;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;

import com.thetonyk.CommandsHub.Commands.FlyCommand;
import com.thetonyk.CommandsHub.Commands.GamemodeCommand;
import com.thetonyk.CommandsHub.Commands.LagCommand;
import com.thetonyk.CommandsHub.Commands.RankCommand;
import com.thetonyk.CommandsHub.Commands.WhitelistCommand;
import com.thetonyk.CommandsHub.Listeners.ChatListener;
import com.thetonyk.CommandsHub.Listeners.MessengerListener;
import com.thetonyk.CommandsHub.Listeners.EnvironmentListener;
import com.thetonyk.CommandsHub.Listeners.InventoryListener;
import com.thetonyk.CommandsHub.Listeners.JoinListener;
import com.thetonyk.CommandsHub.Listeners.LeaveListener;
import com.thetonyk.CommandsHub.Listeners.PlayerListener;
import com.thetonyk.CommandsHub.Utils.DisplayUtils;
import com.thetonyk.CommandsHub.Utils.PlayerUtils;

import us.myles.ViaVersion.api.ViaVersion;
import us.myles.ViaVersion.api.boss.BossBar;
import us.myles.ViaVersion.api.boss.BossColor;
import us.myles.ViaVersion.api.boss.BossStyle;

public class Main extends JavaPlugin {

	public static Main hub;
	
	public static BossBar bar;
	public static Boolean mute = false;
	
	public static final String NO_PERMS = "§fUnknown command.";
	public static final String PREFIX = "§a§lHub §8⫸ §7";
	
	@Override
	public void onEnable() {
		
		getLogger().info("Hub Plugin has been enabled.");
		getLogger().info("Plugin by TheTonyk for CommandsPVP");
		
		hub = this;
		
		Bukkit.getMessenger().registerOutgoingPluginChannel(this, "CommandsBungee");
		
		DisplayUtils.redditHearts();
		
		getCommand("fly").setExecutor(new FlyCommand());
		getCommand("gamemode").setExecutor(new GamemodeCommand());
		getCommand("lag").setExecutor(new LagCommand());
		getCommand("rank").setExecutor(new RankCommand());
		getCommand("whitelist").setExecutor(new WhitelistCommand());
		
		PluginManager manager = Bukkit.getPluginManager();
		
		manager.registerEvents(new JoinListener(), this);
		manager.registerEvents(new LeaveListener(), this);
		manager.registerEvents(new PlayerListener(), this);
		manager.registerEvents(new ChatListener(), this);
		manager.registerEvents(new EnvironmentListener(), this);
		manager.registerEvents(new InventoryListener(), this);
		
		Bukkit.getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
		Bukkit.getMessenger().registerIncomingPluginChannel(this, "BungeeCord", new MessengerListener());
		
		bar =  ViaVersion.getInstance().createBossBar("§7Follow us on Twitter: §b§l@CommandsPVP", BossColor.BLUE, BossStyle.SOLID);
		
		for (Player player : Bukkit.getOnlinePlayers()) {
			
			player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
			player.getScoreboard().registerNewObjective("sidebar", "dummy");
			player.getScoreboard().getObjective("sidebar").setDisplaySlot(DisplaySlot.SIDEBAR);
			player.getScoreboard().getObjective("sidebar").setDisplayName(Main.PREFIX + "§oCommandsPVP");
			player.getScoreboard().registerNewObjective("buffer", "dummy");
			player.getScoreboard().getObjective("buffer").setDisplayName(Main.PREFIX + "§oCommandsPVP");
			
			PlayerUtils.updateScoreboard(player);	
			PlayerUtils.updateNametag(player.getName());
			
			bar.addPlayer(player);
			
		}
		
		new BukkitRunnable() {
			
			public void run() {
				
				for (Player player : Bukkit.getOnlinePlayers()) {
					
					if (player.getVehicle() == null || player.getVehicle().getType() != EntityType.ENDER_PEARL) continue;
						
					player.spigot().playEffect(player.getLocation(), Effect.INSTANT_SPELL, 14, 0, 0F, 0F, 0F, 0.15F, 10, 1);;
					
				}
				
			}
			
		}.runTaskTimer(Main.hub, 1, 1);
		
		new BukkitRunnable() {
			
			int buffer = 0;
			
			public void run() {
				
				String type = buffer == 0 ? "sidebar" : "buffer";
				
				for (Player player : Bukkit.getOnlinePlayers()) {
					
					DisplayUtils.sendTab(player);
					player.getScoreboard().getObjective(type).setDisplaySlot(DisplaySlot.SIDEBAR);
					
					if (buffer == 0) PlayerUtils.updateBuffer(player);
					else PlayerUtils.updateScoreboard(player);
					
				}
				
				buffer = buffer == 0 ? 1 : 0;
				
			}
			
		}.runTaskTimer(Main.hub, 1, 20);
		
	}
	
	@Override
	public void onDisable() {
		
		getLogger().info("Hub Plugin has been disabled.");
		
		hub = null;
		
		for (Player player : Bukkit.getOnlinePlayers()) {
		
			player.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
			
		}
		
	}
	
}
