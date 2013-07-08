package net.dmulloy2.teamsparkle.handlers;

import java.util.logging.Level;

import net.dmulloy2.teamsparkle.TeamSparkle;
import net.dmulloy2.teamsparkle.util.FormatUtil;

/**
 * @author dmulloy2
 */

public class LogHandler
{
	private TeamSparkle plugin;
	public LogHandler(TeamSparkle plugin) 
	{
		this.plugin = plugin;
	}

	public final void log(Level level, String msg, Object... objects)
	{
		plugin.getServer().getLogger().log(level, FormatUtil.format("[TeamSparkle] " + msg, objects));		
	}

	public final void log(String msg, Object... objects)
	{
		log(Level.INFO, msg, objects);
	}
}