package net.dmulloy2.teamsparkle.handlers;

import net.dmulloy2.teamsparkle.TeamSparkle;
import net.dmulloy2.teamsparkle.types.Permission;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author dmulloy2
 */

public class PermissionHandler
{
	private TeamSparkle plugin;

	public PermissionHandler(TeamSparkle plugin)
	{
		this.plugin = plugin;
	}

	public boolean hasPermission(CommandSender sender, Permission permission)
	{
		return (permission == null) ? true : hasPermission(sender, getPermissionString(permission));
	}

	public boolean hasPermission(CommandSender sender, String permission)
	{
		if (sender instanceof Player)
		{
			Player p = (Player) sender;
			return (p.hasPermission(permission) || p.isOp());
		}

		return true;
	}

	private String getPermissionString(Permission permission)
	{
		return plugin.getName().toLowerCase() + "." + permission.node.toLowerCase();
	}

}