package net.dmulloy2.teamsparkle.commands;

import net.dmulloy2.teamsparkle.TeamSparkle;
import net.dmulloy2.teamsparkle.types.Permission;
import net.dmulloy2.teamsparkle.types.PlayerData;
import net.dmulloy2.util.Util;

import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

/**
 * @author dmulloy2
 */

public class CmdStats extends TeamSparkleCommand
{
	public CmdStats(TeamSparkle plugin)
	{
		super(plugin);
		this.name = "stats";
		this.aliases.add("info");
		this.optionalArgs.add("player");
		this.description = "Check your sparkly stats";
		this.permission = Permission.CMD_STATS;

		this.mustBePlayer = true;
	}

	@Override
	public void perform()
	{
		OfflinePlayer target = null;
		if (args.length == 1)
		{
			target = Util.matchPlayer(args[0]);
			if (target == null)
			{
				target = Util.matchOfflinePlayer(args[0]);
				if (target == null)
				{
					err(getMessage("noplayer"));
					return;
				}
			}
		}
		else
		{
			if (sender instanceof Player)
			{
				target = (Player) sender;
			}
			else
			{
				err(getMessage("console_stats"));
				return;
			}
		}

		PlayerData data = getPlayerData(target);
		if (data == null)
		{
			err(getMessage("noplayer"));
			return;
		}

		sendMessage(getMessage("stats_header"), target.getName());
		sendMessage(getMessage("stats_sparkles"), data.getTotalSparkles());
		sendMessage(getMessage("stats_tokens"), data.getTokens());
	}

	@Override
	public Material getHelpMaterial()
	{
		return Material.BOOK;
	}
}