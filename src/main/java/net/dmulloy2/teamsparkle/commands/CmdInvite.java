package net.dmulloy2.teamsparkle.commands;

import net.dmulloy2.teamsparkle.SparkledInstance;
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
		
		if (plugin.pinMap.containsKey(args[0]))
		{
			err(getMessage("already_invited"));
			return;
		}
		
		SparkledInstance si = new SparkledInstance(player.getName(), args[0]);
		plugin.pinMap.put(args[0], si);
		
		sendMessage(getMessage("invite_confirmed"), args[0], si.getPin());
	}
}