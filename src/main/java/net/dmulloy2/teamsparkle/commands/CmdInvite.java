package net.dmulloy2.teamsparkle.commands;

import net.dmulloy2.teamsparkle.TeamSparkle;
import net.dmulloy2.teamsparkle.permissions.Permission;
import net.dmulloy2.teamsparkle.util.Util;

/**
 * @author dmulloy2
 */

public class CmdInvite extends TeamSparkleCommand
{
	public CmdInvite(TeamSparkle plugin)
	{
		super(plugin);
		this.name = "invite";
		this.requiredArgs.add("player");
		this.description = "Invite a newly sparkled player";
		this.permission = Permission.CMD_INVITE;
		
		this.mustBePlayer = true;
	}

	@Override
	public void perform() 
	{
		if (Util.hasPlayedBefore(args[0]))
		{
			err(getMessage("has_played_before"));
			return;
		}

		if (plugin.getSparkled().containsValue(args[0]))
		{
			err(getMessage("already_invited"));
			return;
		}
		
		plugin.getSparkled().put(player.getName(), args[0]);

		sendMessage(getMessage("invite_confirmed"), args[0]);
	}
}