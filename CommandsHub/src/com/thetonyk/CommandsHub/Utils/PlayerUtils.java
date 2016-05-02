package com.thetonyk.CommandsHub.Utils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scoreboard.NameTagVisibility;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.thetonyk.CommandsHub.Main;
import com.thetonyk.CommandsHub.Listeners.MessengerListener;

public class PlayerUtils {
	
	public static void clearInventory(Player player) {
		
        PlayerInventory inventory = player.getInventory();

        inventory.clear();
        inventory.setArmorContents(null);
        player.setItemOnCursor(new ItemStack(Material.AIR));

        InventoryView openedInventory = player.getOpenInventory();
        
        if (openedInventory.getType() == InventoryType.CRAFTING) openedInventory.getTopInventory().clear();
        
    }
	
	public static void clearXp(Player player) {
	
		player.setTotalExperience(0);
        player.setLevel(0);
        player.setExp(0F);
		
	}
	
	public static void feed(Player player) {
		
        player.setSaturation(5.0F);
        player.setExhaustion(0F);
        player.setFoodLevel(20);
        
    }
	
	public static void heal(Player player) {
		
		player.setHealth(player.getMaxHealth());
		
	}
	
	public static void clearEffects(Player player) {
		
		Collection<PotionEffect> activeEffects = player.getActivePotionEffects();

        for (PotionEffect activeEffect : activeEffects) {
        	
            player.removePotionEffect(activeEffect.getType());
            
        }
		
	}
	
	public enum Rank {
		
		PLAYER("", "§7Player"), WINNER("§6Winner §8| ", "§6Winner"), FAMOUS("§bFamous §8| ", "§bFamous"), BUILDER("§2Build §8| ", "§2Builder"),STAFF("§cStaff §8| ", "§cStaff"), MOD("§9Mod §8| ", "§9Moderator"), ADMIN("§4Admin §8| ", "§4Admin"), FRIEND("§3Friend §8| ", "§3Friend"), HOST("§cHost §8| ", "§cHost"), ACTIVE_BUILDER("§2Build §8| ", "§2Builder");
		
		String prefix;
		String name;
		
		private Rank(String prefix, String name) {
			
			this.prefix = prefix;
			this.name = name;
			
		}
		
		public String getPrefix() {
			
			return prefix;
			
		}
		
		public String getName() {
			
			return name;
			
		}
		
	}
	
	public static void setRank(String player, Rank rank) {
		
		try {
			
			Statement sql = DatabaseUtils.getConnection().createStatement();
			sql.executeUpdate("UPDATE users SET rank = '" + rank + "' WHERE name = '" + player + "';");
			sql.close();
			
		} catch (SQLException exception) {
			
			Bukkit.getLogger().severe("[PlayerUtils] Error to update rank of player " + player + ".");
			
		}
		
		if (Bukkit.getPlayer(player) != null && Bukkit.getPlayer(player).isOnline()) {
			
			PermissionsUtils.clearPermissions(Bukkit.getPlayer(player));
			PermissionsUtils.setPermissions(Bukkit.getPlayer(player));
			PermissionsUtils.updateBungeePermissions(Bukkit.getPlayer(player));
		
		}
		
	}
	
	public static Rank getRank(String player) {
		
		Rank rank = Rank.PLAYER;
		
		try {
			
			Statement sql = DatabaseUtils.getConnection().createStatement();
			ResultSet req = sql.executeQuery("SELECT rank FROM users WHERE name = '" + player + "';");
			
			if (req.next()) rank = Rank.valueOf(req.getString("rank"));
			
			sql.close();
			req.close();
			
		} catch (SQLException exception) {
			
			Bukkit.getLogger().severe("[PlayerUtils] Error to get the rank of player " + player + ".");
			
		}
		
		return rank;
		
	}
	
	public static String getRanks() {
		
		String list = "Availables ranks: §aplayer";
		
		for (Rank rank : Rank.values()) {
			
			if (!rank.name().toLowerCase().equalsIgnoreCase("player")) list = list + " §7| §a" + rank.name().toLowerCase();
			
		}
		
		return list + "§7.";
		
	}
	
	public static void updateNametag(String player) {
		
		for (Player players : Bukkit.getOnlinePlayers()) {
			
			if (players.getScoreboard().getTeam(Bukkit.getPlayer(player).getName()) == null) {
				
				players.getScoreboard().registerNewTeam(Bukkit.getPlayer(player).getName());
				players.getScoreboard().getTeam(Bukkit.getPlayer(player).getName()).setAllowFriendlyFire(true);
				players.getScoreboard().getTeam(Bukkit.getPlayer(player).getName()).setCanSeeFriendlyInvisibles(true);
				players.getScoreboard().getTeam(Bukkit.getPlayer(player).getName()).setDisplayName(player + " team");
				players.getScoreboard().getTeam(Bukkit.getPlayer(player).getName()).setNameTagVisibility(NameTagVisibility.ALWAYS);
				
			}
		
			players.getScoreboard().getTeam(Bukkit.getPlayer(player).getName()).setPrefix(getRank(Bukkit.getPlayer(player).getName()).getPrefix() + "§7");
			players.getScoreboard().getTeam(Bukkit.getPlayer(player).getName()).setSuffix("§f");
			players.getScoreboard().getTeam(Bukkit.getPlayer(player).getName()).addEntry(Bukkit.getPlayer(player).getName());
			
			if (Bukkit.getPlayer(player).getScoreboard().getTeam(players.getName()) == null) {
				
				Bukkit.getPlayer(player).getScoreboard().registerNewTeam(players.getName());
				Bukkit.getPlayer(player).getScoreboard().getTeam(players.getName()).setAllowFriendlyFire(true);
				Bukkit.getPlayer(player).getScoreboard().getTeam(players.getName()).setCanSeeFriendlyInvisibles(true);
				Bukkit.getPlayer(player).getScoreboard().getTeam(players.getName()).setDisplayName(players.getName() + " team");
				Bukkit.getPlayer(player).getScoreboard().getTeam(players.getName()).setNameTagVisibility(NameTagVisibility.ALWAYS);
				
			}
		
			Bukkit.getPlayer(player).getScoreboard().getTeam(players.getName()).setPrefix(getRank(players.getName()).getPrefix() + "§7");
			Bukkit.getPlayer(player).getScoreboard().getTeam(players.getName()).setSuffix("§f");
			Bukkit.getPlayer(player).getScoreboard().getTeam(players.getName()).addEntry(players.getName());
		
		}
		
	}
	
	public static Boolean exist(Player player) {
		
		return exist(Bukkit.getOfflinePlayer(player.getUniqueId()));
		
	}
	
	public static Boolean exist(OfflinePlayer player) {
		
		Boolean exist = false;
		
		try {
		
			Statement sql = DatabaseUtils.getConnection().createStatement();
			ResultSet req = sql.executeQuery("SELECT * FROM users WHERE uuid='" + player.getUniqueId().toString() + "';");
			
			if (req.next()) exist = true;
			
			sql.close();
			req.close();
			
		} catch (SQLException exception) {
			
			Bukkit.getLogger().severe("[PlayerUtils] Error to check if player " + player.getName() + " is new.");
			
		}
		
		return exist;
		
	}
	
	public static int getId (String name) {
		
		int id = 0;
		
		try {
			
			Statement sql = DatabaseUtils.getConnection().createStatement();
			ResultSet req = sql.executeQuery("SELECT id FROM users WHERE name ='" + name + "';");
			
			if (req.next()) id = req.getInt("id");
			
			sql.close();
			req.close();
			
		} catch (SQLException exception) {
			
			Bukkit.getLogger().severe("[PlayerUtils] Error to get id of player " + name + ".");
			
		}
		
		return id;
		
	}
	
	public static int getId (UUID uuid) {
		
		int id = 0;
		
		try {
			
			Statement sql = DatabaseUtils.getConnection().createStatement();
			ResultSet req = sql.executeQuery("SELECT id FROM users WHERE uuid ='" + uuid + "';");
			
			if (req.next()) id = req.getInt("id");
			
			sql.close();
			req.close();
			
		} catch (SQLException exception) {
			
			Bukkit.getLogger().severe("[PlayerUtils] Error to get id of player with UUID " + uuid + ".");
			
		}
		
		return id;
		
	}
	
	public static void updateScoreboard (Player player) {
		
		for (String entry : player.getScoreboard().getEntries()) {
			
			if (entry.startsWith("§r  §6")) player.getScoreboard().resetScores(entry);
			
		}
		
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		
		out.writeUTF("PlayerCount");
		out.writeUTF("hub");
		
		player.sendPluginMessage(Main.hub, "BungeeCord", out.toByteArray());
		
		out = ByteStreams.newDataOutput();
		
		out.writeUTF("PlayerCount");
		out.writeUTF("arena1.8");
		
		player.sendPluginMessage(Main.hub, "BungeeCord", out.toByteArray());
		
		out = ByteStreams.newDataOutput();
		
		out.writeUTF("PlayerCount");
		out.writeUTF("arena1.9");
		
		player.sendPluginMessage(Main.hub, "BungeeCord", out.toByteArray());
		
		player.getScoreboard().getObjective("sidebar").getScore(" ").setScore(9);
		player.getScoreboard().getObjective("sidebar").getScore("§r  §6Hub §8⫸ §a" + MessengerListener.playerCount.get("hub") + " §7players").setScore(8);
		player.getScoreboard().getObjective("sidebar").getScore("§r  §6Arena 1.8 §8⫸ §a" + MessengerListener.playerCount.get("arena1.8") + " §7players").setScore(7);
		player.getScoreboard().getObjective("sidebar").getScore("§r  §6Arena 1.9 §8⫸ §a" + /*MessengerListener.playerCount.get("arena1.9") + " §7players"*/ "§7Maintenance").setScore(6);
		player.getScoreboard().getObjective("sidebar").getScore("  ").setScore(5);
		player.getScoreboard().getObjective("sidebar").getScore("§r  §6Rank §8⫸ §a" + PlayerUtils.getRank(player.getName()).getName()).setScore(4);
		player.getScoreboard().getObjective("sidebar").getScore("   ").setScore(3);
		player.getScoreboard().getObjective("sidebar").getScore("§r  §6Next UHC §8⫸ §7Coming soon").setScore(2);
		player.getScoreboard().getObjective("sidebar").getScore("    ").setScore(1);
		player.getScoreboard().getObjective("sidebar").getScore("  §b@CommandsPVP").setScore(0);
		
	}
	
	public static void updateBuffer (Player player) {
		
		for (String entry : player.getScoreboard().getEntries()) {
			
			if (entry.startsWith("  §6")) player.getScoreboard().resetScores(entry);
			
		}
		
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		
		out.writeUTF("PlayerCount");
		out.writeUTF("hub");
		
		player.sendPluginMessage(Main.hub, "BungeeCord", out.toByteArray());
		
		out = ByteStreams.newDataOutput();
		
		out.writeUTF("PlayerCount");
		out.writeUTF("arena1.8");
		
		player.sendPluginMessage(Main.hub, "BungeeCord", out.toByteArray());
		
		out = ByteStreams.newDataOutput();
		
		out.writeUTF("PlayerCount");
		out.writeUTF("arena1.9");
		
		player.sendPluginMessage(Main.hub, "BungeeCord", out.toByteArray());
		
		player.getScoreboard().getObjective("buffer").getScore(" ").setScore(9);
		player.getScoreboard().getObjective("buffer").getScore("  §6Hub §8⫸ §a" + MessengerListener.playerCount.get("hub") + " §7players").setScore(8);
		player.getScoreboard().getObjective("buffer").getScore("  §6Arena 1.8 §8⫸ §a" + MessengerListener.playerCount.get("arena1.8") + " §7players").setScore(7);
		player.getScoreboard().getObjective("buffer").getScore("  §6Arena 1.9 §8⫸ §a" + /*MessengerListener.playerCount.get("arena1.9") + " §7players"*/ "§7Maintenance").setScore(6);
		player.getScoreboard().getObjective("buffer").getScore("  ").setScore(5);
		player.getScoreboard().getObjective("buffer").getScore("  §6Rank §8⫸ §a" + PlayerUtils.getRank(player.getName()).getName()).setScore(4);
		player.getScoreboard().getObjective("buffer").getScore("   ").setScore(3);
		player.getScoreboard().getObjective("buffer").getScore("  §6Next UHC §8⫸ §7Coming soon").setScore(2);
		player.getScoreboard().getObjective("buffer").getScore("    ").setScore(1);
		player.getScoreboard().getObjective("buffer").getScore("  §b@CommandsPVP").setScore(0);
		
	}
	
	public static void placeItems (Player player) {
			
		ItemStack item1 = ItemsUtils.createItem(Material.DIAMOND_SWORD, "§8⫸ §a§lArena 1.8 §8(§7Right-Click to join§8) §8⫷", 1, 0);
		item1 = ItemsUtils.addGlow(item1);
		item1 = ItemsUtils.hideFlags(item1);
		player.getInventory().setItem(0, item1);
		
		ItemStack item2 = ItemsUtils.createItem(Material.DIAMOND_SWORD, "§8⫸ §a§lArena 1.9 §8(§7Right-Click to join§8) §8⫷", 1, 0);
		item2 = ItemsUtils.addGlow(item2);
		item2 = ItemsUtils.hideFlags(item2);
		player.getInventory().setItem(1, item2);
		
		ItemStack item7 = ItemsUtils.createItem(Material.EYE_OF_ENDER, "§8⫸ §d§lFlying Pearl §8(§7Right-Click to launch§8) §8⫷", 1, 0);
		item7 = ItemsUtils.hideFlags(item7);
		player.getInventory().setItem(6, item7);
		
		ItemStack item9 = ItemsUtils.createItem(Material.REDSTONE_COMPARATOR, "§8⫸ §6§lSettings §8(§7Right-Click to open§8) §8⫷", 1, 0);
		item9 = ItemsUtils.hideFlags(item9);
		player.getInventory().setItem(8, item9);
		
	}
	
	public static int getPlayersVisibility (Player player) {
		
		int playerVisibility = 0;
		
		try {
			
			Statement sql = DatabaseUtils.getConnection().createStatement();
			ResultSet req = sql.executeQuery("SELECT players FROM settings WHERE id = " + PlayerUtils.getId(player.getUniqueId()) + ";");
			
			if (req.next()) playerVisibility = req.getInt("players");
			
			sql.close();
			req.close();
			
		} catch (SQLException exception) {
			
			Bukkit.getLogger().severe("[PlayerUtils] Error to get players setting of player " + player.getName() + ".");
			
		}
		
		return playerVisibility;
		
	}
	
	public static int getChatVisibility (Player player) {
		
		int chatVisibility = 0;
		
		try {
			
			Statement sql = DatabaseUtils.getConnection().createStatement();
			ResultSet req = sql.executeQuery("SELECT chat FROM settings WHERE id = " + PlayerUtils.getId(player.getUniqueId()) + ";");
			
			if (req.next()) chatVisibility = req.getInt("chat");
			
			sql.close();
			req.close();
			
		} catch (SQLException exception) {
			
			Bukkit.getLogger().severe("[PlayerUtils] Error to get chat setting of player " + player.getName() + ".");
			
		}
		
		return chatVisibility;
		
	}
	
	public static int getMentionsState (Player player) {
		
		int mentionsState = 0;
		
		try {
			
			Statement sql = DatabaseUtils.getConnection().createStatement();
			ResultSet req = sql.executeQuery("SELECT mentions FROM settings WHERE id = " + PlayerUtils.getId(player.getUniqueId()) + ";");
			
			if (req.next()) mentionsState = req.getInt("mentions");
			
			sql.close();
			req.close();
			
		} catch (SQLException exception) {
			
			Bukkit.getLogger().severe("[PlayerUtils] Error to get mentions setting of player " + player.getName() + ".");
			
		}
		
		return mentionsState;
		
	}
	
	public static int getPrivateState (Player player) {
		
		int privateState = 0;
		
		try {
			
			Statement sql = DatabaseUtils.getConnection().createStatement();
			ResultSet req = sql.executeQuery("SELECT private FROM settings WHERE id = " + PlayerUtils.getId(player.getUniqueId()) + ";");
			
			if (req.next()) privateState = req.getInt("private");
			
			sql.close();
			req.close();
			
		} catch (SQLException exception) {
			
			Bukkit.getLogger().severe("[PlayerUtils] Error to get private setting of player " + player.getName() + ".");
			
		}
		
		return privateState;
		
	}
	
	public static void setPlayersVisibility (Player player) {
		
		int state = PlayerUtils.getPlayersVisibility(player) == 1 ? 0 : 1;
		
		try {
			
			Statement sql = DatabaseUtils.getConnection().createStatement();
			sql.executeUpdate("UPDATE settings SET players = " + state + " WHERE id = " + PlayerUtils.getId(player.getUniqueId()) + ";");	
			sql.close();
			
		} catch (SQLException exception) {
			
			Bukkit.getLogger().severe("[PlayerUtils] Error to toggle players visibility settings of player " + player.getName() + ".");
			
		}
		
	}
	
	public static void setChatVisibility (Player player) {
		
		int state = PlayerUtils.getChatVisibility(player) == 1 ? 0 : 1;
		
		try {
			
			Statement sql = DatabaseUtils.getConnection().createStatement();
			sql.executeUpdate("UPDATE settings SET chat = " + state + " WHERE id = " + PlayerUtils.getId(player.getUniqueId()) + ";");	
			sql.close();
			
		} catch (SQLException exception) {
			
			Bukkit.getLogger().severe("[PlayerUtils] Error to toggle chat visibility settings of player " + player.getName() + ".");
			
		}
		
	}

	public static void setMentionsState (Player player) {
	
		int state = PlayerUtils.getMentionsState(player) == 1 ? 0 : 1;
		
		try {
			
			Statement sql = DatabaseUtils.getConnection().createStatement();
			sql.executeUpdate("UPDATE settings SET mentions = " + state + " WHERE id = " + PlayerUtils.getId(player.getUniqueId()) + ";");
			sql.close();
			
		} catch (SQLException exception) {
			
			Bukkit.getLogger().severe("[PlayerUtils] Error to toggle mentions state of player " + player.getName() + ".");
			
		}
	
	}
	
	public static void setPrivateState (Player player) {
		
		int state = PlayerUtils.getPrivateState(player) == 1 ? 0 : 1;
		
		try {
			
			Statement sql = DatabaseUtils.getConnection().createStatement();
			sql.executeUpdate("UPDATE settings SET private = " + state + " WHERE id = " + PlayerUtils.getId(player.getUniqueId()) + ";");
			sql.close();
			
		} catch (SQLException exception) {
			
			Bukkit.getLogger().severe("[PlayerUtils] Error to toggle private state of player " + player.getName() + ".");
			
		}
	
	}
	
}
