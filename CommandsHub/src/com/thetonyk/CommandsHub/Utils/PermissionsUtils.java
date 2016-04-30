package com.thetonyk.CommandsHub.Utils;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.thetonyk.CommandsHub.Main;
import com.thetonyk.CommandsHub.Utils.PlayerUtils.Rank;

public class PermissionsUtils {
	
	private static Map<String, PermissionAttachment> permissions = new HashMap<String, PermissionAttachment>();
	
	public static void setPermissions(Player player) {
		
		if (!permissions.containsKey(player.getName())) {
			
			permissions.put(player.getName(), player.addAttachment(Main.hub));
			
		}
		
		PermissionAttachment permission = permissions.get(player.getName());
		Rank rank = PlayerUtils.getRank(player.getName());
		
		if (rank == Rank.ADMIN) {
			
			player.setOp(true);
			return;
			
		}
		
		permission.setPermission("hub.lag", true);
		
		if (rank == Rank.PLAYER || rank == Rank.WINNER) {
			return;
		}
		
		permission.setPermission("global.fly", true);
		permission.setPermission("hub.visible", true);
		
		if (rank == Rank.FAMOUS || rank == Rank.FRIEND) {
			return;
		}
		
		permission.setPermission("global.bypasswhitelist", true);
		
		if (rank == Rank.BUILDER) {
			return;
		}
		
		permission.setPermission("global.build", true);
		permission.setPermission("global.gamemode", true);
		permission.setPermission("parkour.jump", true);
		
		if (rank == Rank.ACTIVE_BUILDER) {
			return;
		}
		
		permission.setPermission("global.gamemode", false);
		permission.setPermission("global.build", false);
		permission.setPermission("parkour.jump", false);
		
	}
	
	public static void clearPermissions(Player player) {
		
		if (permissions.containsKey(player.getName())) {
			
			try {
				
				player.removeAttachment(permissions.get(player.getName()));
				
			} catch (Exception e) {
				
				Bukkit.getLogger().severe("§7[PermissionsUtils] §cError to clear permissions of player §6" + player.getName() + "§c.");
				
			}
			
			permissions.remove(player.getName());
			
		}
		
	}
	
	public static void updateBungeePermissions(Player player) {
		
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		
		out.writeUTF("updatePermissions");
		out.writeUTF(player.getUniqueId().toString());
		
		player.sendPluginMessage(Main.hub, "CommandsBungee", out.toByteArray());
		
	}

}
