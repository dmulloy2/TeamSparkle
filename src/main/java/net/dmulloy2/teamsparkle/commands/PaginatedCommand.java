package net.dmulloy2.teamsparkle.commands;

import net.dmulloy2.teamsparkle.TeamSparkle;
import net.dmulloy2.teamsparkle.types.PlayerData;

import org.bukkit.Material;
import org.bukkit.OfflinePlayer;

/**
 * @author dmulloy2
 */

public abstract class PaginatedCommand extends net.dmulloy2.commands.PaginatedCommand
{
	protected final TeamSparkle plugin;
	public PaginatedCommand(TeamSparkle plugin)
	{
		super(plugin);
		this.plugin = plugin;
	}

	protected final PlayerData getPlayerData(OfflinePlayer target)
	{
		return plugin.getPlayerDataCache().getData(target);
	}

	protected final String getMessage(String key)
	{
		return plugin.getMessage(key);
	}

	public abstract Material getHelpMaterial();
}