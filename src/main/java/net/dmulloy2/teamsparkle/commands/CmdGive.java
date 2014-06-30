package net.dmulloy2.teamsparkle.commands;

import net.dmulloy2.teamsparkle.TeamSparkle;
import net.dmulloy2.teamsparkle.types.Permission;
import net.dmulloy2.teamsparkle.types.PlayerData;
import net.dmulloy2.util.Util;

import org.bukkit.Material;
import org.bukkit.OfflinePlayer;

/**
 * @author dmulloy2
 */

public class CmdGive extends TeamSparkleCommand
{
	public CmdGive(TeamSparkle plugin)
	{
		super(plugin);
		this.name = "give";
		this.aliases.add("givetokens");
		this.requiredArgs.add("player");
		this.requiredArgs.add("tokens");
		this.description = "Manually give tokens";
		this.permission = Permission.CMD_GIVE;

		this.mustBePlayer = true;
	}

	@Override
	public void perform()
	{
		OfflinePlayer target = Util.matchOfflinePlayer(args[0]);
		if (target == null)
		{
			err(getMessage("noplayer"));
			return;
		}

		PlayerData data = getPlayerData(target);
		if (data == null)
		{
			err(getMessage("noplayer"));
			return;
		}

		int tokens = argAsInt(1, true);
		if (tokens == -1)
			return;

		data.setTokens(data.getTokens() + tokens);
		sendpMessage(getMessage("give_tokens"), tokens, target.getName());
	}

	@Override
	public Material getHelpMaterial()
	{
		// Don't show in the help menu
		return null;
	}
}