package net.dmulloy2.teamsparkle.commands;

import org.bukkit.Material;

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
		long start = System.currentTimeMillis();
		sendpMessage(getMessage("reloading"));

		plugin.reload();

		sendpMessage(getMessage("reload_complete"), System.currentTimeMillis() - start);
		plugin.getLogHandler().log("Reloaded TeamSparkle. Took {0} ms!", System.currentTimeMillis() - start);
	}

	@Override
	public Material getHelpMaterial()
	{
		// Don't show in the help menu
		return null;
	}
}