package net.dmulloy2.teamsparkle.handlers;

import lombok.AllArgsConstructor;
import net.dmulloy2.teamsparkle.TeamSparkle;
import net.dmulloy2.teamsparkle.types.Permission;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author dmulloy2
 */

@AllArgsConstructor
public class PermissionHandler
{
	private final TeamSparkle plugin;

	public final boolean hasPermission(CommandSender sender, Permission permission)
	{
		return permission == null || hasPermission(sender, getPermissionString(permission));
	}

	public final boolean hasPermission(CommandSender sender, String permission)
	{
		if (sender instanceof Player)
		{
			Player player = (Player) sender;
			return player.hasPermission(permission) || player.isOp();
		}

		return true;
	}

	public final String getPermissionString(Permission permission)
	{
		return plugin.getName().toLowerCase() + "." + permission.getNode().toLowerCase();
	}

}