package com.thetonyk.CommandsHub.Listeners;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EnderCrystal;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Painting;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.spigotmc.event.entity.EntityDismountEvent;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.thetonyk.CommandsHub.Main;
import com.thetonyk.CommandsHub.Inventories.SettingsInventory;
import com.thetonyk.CommandsHub.Utils.DisplayUtils;
import com.thetonyk.CommandsHub.Utils.PlayerUtils;

import us.myles.ViaVersion.api.ViaVersion;

public class PlayerListener implements Listener {
	
	public static List<Location> anvils = new ArrayList<Location>();
	public static List<UUID> cooldown = new ArrayList<UUID>();
	public static List<UUID> serverCooldown = new ArrayList<UUID>();
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		
			if (event.getBlock().getWorld().getName().equals("world") && event.getPlayer().hasPermission("global.build") && event.getPlayer().getGameMode() == GameMode.CREATIVE) return;
			
			event.setCancelled(true);
			
	}
	
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) {
	
		if (event.getBlock().getWorld().getName().equals("world") && event.getPlayer().hasPermission("global.build") && event.getPlayer().getGameMode() == GameMode.CREATIVE) return;
		
		event.setCancelled(true);
		
	}
	
	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
	
		if (event.getPlayer().getWorld().getName().equals("world")) {
			
			if (event.getAction() == Action.PHYSICAL) {
				
				event.setCancelled(true);
				return;
				
			}
			
			if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
				
				if (event.getPlayer().hasPermission("global.build")) return;
				
				switch(event.getClickedBlock().getType()) {
				
					case ANVIL:
					case BEACON:
					case BREWING_STAND:
					case CHEST:
					case WORKBENCH:
					case DISPENSER:
					case DROPPER:
					case ENCHANTMENT_TABLE:
					case ENDER_CHEST:
					case FURNACE:
					case BURNING_FURNACE:
					case HOPPER:
					case ITEM_FRAME:
					case LEVER:
					case BED_BLOCK:
					case TRAPPED_CHEST:
						event.setCancelled(true);
						break;
					default:
						break;
				
				}
					
			}
			
			if (event.getItem() != null && event.getItem().hasItemMeta() && event.getItem().getItemMeta().hasDisplayName()) {
					
				if (event.getItem().getItemMeta().getDisplayName().equalsIgnoreCase("§8⫸ §a§lArena 1.8 §8(§7Right-Click to join§8) §8⫷")) {
					
					event.setCancelled(true);
				
					if (serverCooldown.contains(event.getPlayer().getUniqueId())) return;
					serverCooldown.add(event.getPlayer().getUniqueId());
					
					new BukkitRunnable() {
						
						public void run() {
							
							if (serverCooldown.contains(event.getPlayer().getUniqueId())) serverCooldown.remove(event.getPlayer().getUniqueId());
							
						}
						
					}.runTaskLater(Main.hub, 60);
					
					event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.ORB_PICKUP, 1, 1);
					
					if (ViaVersion.getInstance().isPorted(event.getPlayer().getUniqueId())) {
						
						event.getPlayer().sendMessage("§a§lGlobal §8⫸ §7You can only join this arena in 1.8.");
						return;
						
					}
					
					ByteArrayDataOutput out = ByteStreams.newDataOutput();
					
					out.writeUTF("Connect");
					out.writeUTF("arena1.8");
					
					event.getPlayer().sendPluginMessage(Main.hub, "BungeeCord", out.toByteArray());
					return;
					
				}
				else if (event.getItem().getItemMeta().getDisplayName().equalsIgnoreCase("§8⫸ §a§lArena 1.9 §8(§7Right-Click to join§8) §8⫷")) {
					
					event.setCancelled(true);
				
					if (serverCooldown.contains(event.getPlayer().getUniqueId())) return;
					serverCooldown.add(event.getPlayer().getUniqueId());
					
					new BukkitRunnable() {
						
						public void run() {
							
							if (serverCooldown.contains(event.getPlayer().getUniqueId())) serverCooldown.remove(event.getPlayer().getUniqueId());
							
						}
						
					}.runTaskLater(Main.hub, 60);
					
					event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.ORB_PICKUP, 1, 1);
					
					if (!ViaVersion.getInstance().isPorted(event.getPlayer().getUniqueId())) {
						
						event.getPlayer().sendMessage("§a§lGlobal §8⫸ §7You can only join this arena in 1.9.");
						return;
						
					}
					
					event.getPlayer().sendMessage("§a§lGlobal §8⫸ §7Server maintenance.");
					return;
					
					/*
					 
					SERVEUR EN MAINTENANCE
					 
					ByteArrayDataOutput out = ByteStreams.newDataOutput();
					
					out.writeUTF("Connect");
					out.writeUTF("arena1.9");
					
					player.sendPluginMessage(Main.hub, "BungeeCord", out.toByteArray());
					
					return;
					
					*/
					
				}
				else if (event.getItem().getItemMeta().getDisplayName().equalsIgnoreCase("§8⫸ §6§lSettings §8(§7Right-Click to open§8) §8⫷")) {
					
					event.setCancelled(true);
					event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.ORB_PICKUP, 1, 1);
					event.getPlayer().openInventory(SettingsInventory.getSettings(event.getPlayer()));
					return;
					
				}
				else if (event.getItem().getItemMeta().getDisplayName().equalsIgnoreCase("§8⫸ §d§lFlying Pearl §8(§7Right-Click to launch§8) §8⫷")) {
					
					event.setCancelled(true);
					if (cooldown.contains(event.getPlayer().getUniqueId())) return;
					
					if (event.getPlayer().getVehicle() == null) {
					
						event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.ORB_PICKUP, 1, 1);
						Projectile enderpearl = event.getPlayer().launchProjectile(EnderPearl.class);
						enderpearl.setPassenger(event.getPlayer());
						event.getPlayer().spigot().setCollidesWithEntities(false);
						
					}
					
					cooldown.add(event.getPlayer().getUniqueId());
					
					new BukkitRunnable() {
						
						int left = 10;
						
						public void run() {
							
							if (left < 1) {
								
								DisplayUtils.sendActionBar(event.getPlayer(), "§7You can now use your §d§lFlying pearl");
								
								if (cooldown.contains(event.getPlayer().getUniqueId())) cooldown.remove(event.getPlayer().getUniqueId());
								
								cancel();
								return;
								
							}
							
							DisplayUtils.sendActionBar(event.getPlayer(), "§7You can use your §d§lFlying pearl §7in §a" + left + "s");
							
							left--;
							
						}
						
					}.runTaskTimer(Main.hub, 0, 20);
					
					return;
					
				}
				
				return;
					
			}
			
		}
		
	}
	
	@EventHandler
	public void onEntityInteract(PlayerInteractEntityEvent event) {
		
		if (!event.getPlayer().getWorld().getName().equalsIgnoreCase("world")) return;
			
		if (event.getPlayer().hasPermission("global.build")) return;
				
		switch (event.getRightClicked().getType()) {
		
			case ITEM_FRAME:
				event.setCancelled(true);
				break;
			default:
				break;
		
		}
		
	}
	
	@EventHandler
	public void onHit(PlayerTeleportEvent event) {
		
		if (event.getCause() != PlayerTeleportEvent.TeleportCause.ENDER_PEARL) return;
			
		event.setCancelled(true);
		unglitchPlayer(event.getPlayer());
		
	}
	
	@EventHandler
	public void onDismount(EntityDismountEvent event) {
		
		if (event.getDismounted().getType() != EntityType.ENDER_PEARL) return;
			
		event.getDismounted().remove();
		((Player) event.getEntity()).spigot().setCollidesWithEntities(true);
		
	}
	
	@EventHandler
	public void onHit(ProjectileHitEvent event) {
		
		((Player) event.getEntity().getShooter()).spigot().setCollidesWithEntities(true);
		unglitchPlayer((Player) event.getEntity().getShooter());
		
	}
	
	@EventHandler
	public void onEntityDamage(EntityDamageEvent event) {
		
		if (event.getEntity() instanceof Player || event.getEntity() instanceof Minecart ||event.getEntity() instanceof ArmorStand || event.getEntity() instanceof Painting || event.getEntity() instanceof ItemFrame) {
			
			if (!event.getEntity().getWorld().getName().equals("world")) return;
				
			event.setCancelled(true);
			return;
			
		}

	}
	
	@EventHandler
	public void onArmorStandInteract(PlayerArmorStandManipulateEvent event) {
			
		if (!event.getRightClicked().getWorld().getName().equals("world") || event.getPlayer().hasPermission("global.build") || event.getPlayer().getGameMode() == GameMode.CREATIVE) return;
			
		event.setCancelled(true);
		
	}
	
	@EventHandler
	public void onHungerChange(FoodLevelChangeEvent event) {
			
		event.setCancelled(true);
		event.setFoodLevel(20);
		
	}
	
	@EventHandler
	public void onDrop(PlayerDropItemEvent event) {
		
		event.setCancelled(true);
		
	}
	
	@EventHandler
	public void onPlayerDamage(EntityDamageByEntityEvent event) {
		
		if (event.getEntity() instanceof EnderCrystal && event.getDamager() instanceof Player) {
			
			if (!event.getEntity().getWorld().getName().equalsIgnoreCase("world")) return;
				
			event.setCancelled(true);
				
			if (!event.getDamager().hasPermission("global.build")) return;
				
			event.getEntity().remove();
			
		}
		
	}
	
	@EventHandler(priority = EventPriority.LOW)
	public void onMove(PlayerMoveEvent event) {
		
		if (!event.getTo().getWorld().getName().equalsIgnoreCase("world") || event.getTo().getY() > 0) return;
			
		Location spawn = Bukkit.getWorld("world").getSpawnLocation().add(0.5, 0, 0.5);
		spawn.setDirection(new Vector(0, 15, -50));
		event.getPlayer().teleport(spawn);
		
	}
	
	@EventHandler
	public void onDeath(PlayerDeathEvent event) {
		
		event.setDeathMessage(null);
		
		new BukkitRunnable() {
			
			public void run() {
				
				event.getEntity().spigot().respawn();
				
			}
			
		}.runTaskLater(Main.hub, 1);
	
	}
	
	@EventHandler
	public void onRespaw(PlayerRespawnEvent event) {
		
		new BukkitRunnable() {
			
			public void run() {
				
				event.setRespawnLocation(Bukkit.getWorld("world").getSpawnLocation().add(0.5, 0, 0.5));
				event.getPlayer().setGameMode(GameMode.ADVENTURE);
				
				PlayerUtils.clearInventory(event.getPlayer());
				PlayerUtils.clearXp(event.getPlayer());
				PlayerUtils.feed(event.getPlayer());
				PlayerUtils.heal(event.getPlayer());
				PlayerUtils.clearEffects(event.getPlayer());
				event.getPlayer().setExp(0);
				event.getPlayer().setTotalExperience(0);
				
				if (event.getPlayer().hasPermission("global.fly")) event.getPlayer().setAllowFlight(true);
				
			}
			
		}.runTaskLater(Main.hub, 1);
		
	}
	
	@EventHandler
	public void onChangeWorld(PlayerChangedWorldEvent event) {
		
		if (event.getPlayer().getWorld().getName().equalsIgnoreCase("world") && event.getPlayer().hasPermission("global.fly")) {
			
			event.getPlayer().setAllowFlight(true);
			return;
			
		}
		
		event.getPlayer().setAllowFlight(false);
		event.getPlayer().setFlying(false);
		return;
		
	}
	
	@EventHandler
	public void onChangeGamemode(PlayerGameModeChangeEvent event) {
		
		switch (event.getNewGameMode()) {
		
			case SURVIVAL:
				DisplayUtils.hideCoord(event.getPlayer());
				break;
			case CREATIVE:
				DisplayUtils.showCoord(event.getPlayer());
				break;
			case ADVENTURE:
				DisplayUtils.hideCoord(event.getPlayer());
				break;
			case SPECTATOR:
				DisplayUtils.showCoord(event.getPlayer());
				break;
			default:
				DisplayUtils.hideCoord(event.getPlayer());
				break;
		
		}
		
	}
	
	private void unglitchPlayer (Player player) {
		
		if (player.getLocation().getBlock().getType().equals(Material.AIR)) return;
			
		player.teleport(player.getLocation().add(0, 1, 0));
		unglitchPlayer(player);
		
	}

}
