package net.dmulloy2.teamsparkle.commands;

import net.dmulloy2.teamsparkle.TeamSparkle;
import net.dmulloy2.teamsparkle.types.Permission;

/**
 * @author dmulloy2
 */

public class CmdReload extends TeamSparkleCommand
{
	public CmdReload(TeamSparkle plugin)
	{
		super(plugin);
		this.name = "reload";
		this.aliases.add("rl");
		this.description = "Reloads the plugin";
		this.permission = Permission.CMD_RELOAD;
	}

	@Override
	public void perform()
	{
		sendMessage(getMessage("reloading"));

		long start = System.currentTimeMillis();

		plugin.reload();

		long finish = System.currentTimeMillis();

		sendMessage(getMessage("reload_complete"), finish - start);
		plugin.getLogHandler().log("Reloaded TeamSparkle. Took {0} ms!", finish - start);
	}
}