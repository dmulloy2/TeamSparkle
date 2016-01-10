/**
 * (c) 2015 dmulloy2
 */
package net.dmulloy2.teamsparkle;

import java.util.List;

import net.dmulloy2.config.ConfigParser;
import net.dmulloy2.config.Key;

/**
 * @author dmulloy2
 */

public class Config
{
	public static void load(TeamSparkle plugin)
	{
		ConfigParser.parse(plugin, Config.class);
	}

	@Key("sparkledRewards")
	public static List<String> sparkledRewards;

	@Key("hourlyRewards")
	public static List<String> hourlyRewards;

	@Key("hourlyRewardsEnabled")
	public static boolean hourlyRewardsEnabled = true;

	@Key("autoSave.enabled")
	public static boolean autoSaveEnabled = true;

	@Key("autoSave.interval")
	public static int autoSaveInterval = 120;

	@Key("serverName")
	public static String serverName = "The Server";

	@Key("debug")
	public static boolean debug = false;
}