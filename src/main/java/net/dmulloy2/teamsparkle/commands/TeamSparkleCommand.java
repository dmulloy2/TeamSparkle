package net.dmulloy2.teamsparkle.commands;

import net.dmulloy2.commands.Command;
import net.dmulloy2.teamsparkle.TeamSparkle;
import net.dmulloy2.teamsparkle.types.PlayerData;

import org.bukkit.Material;
import org.bukkit.OfflinePlayer;

/**
 * @author dmulloy2
 */

public abstract class TeamSparkleCommand extends Command
{
	protected final TeamSparkle plugin;
	public TeamSparkleCommand(TeamSparkle plugin)
	{
		super(plugin);
		this.plugin = plugin;
		this.usesPrefix = true;
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