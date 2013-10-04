package net.dmulloy2.teamsparkle.handlers;

import java.util.logging.Level;

import net.dmulloy2.teamsparkle.TeamSparkle;
import net.dmulloy2.teamsparkle.util.FormatUtil;

/**
 * @author dmulloy2
 */

public class LogHandler
{
	private final TeamSparkle plugin;
	public LogHandler(TeamSparkle plugin) 
	{
		this.plugin = plugin;
	}

	public final void log(Level level, String msg, Object... objects)
	{
		plugin.getLogger().log(level, FormatUtil.format(msg, objects));
	}

	public final void log(String msg, Object... objects)
	{
		plugin.getLogger().info(FormatUtil.format(msg, objects));
	}
	
	public final void debug(String msg, Object... objects)
	{
		if (plugin.getConfig().getBoolean("debug", false))
		{
			plugin.getLogger().info(FormatUtil.format("[Debug] " + msg, objects));
		}
	}
}