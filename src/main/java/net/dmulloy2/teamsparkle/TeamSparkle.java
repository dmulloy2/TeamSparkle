/**
 * TeamSparkle - a bukkit plugin
 * Copyright (C) 2013 - 2014 dmulloy2
 *
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */
package net.dmulloy2.teamsparkle;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;
import java.util.logging.Level;

import lombok.Getter;
import net.dmulloy2.SwornPlugin;
import net.dmulloy2.gui.GUIHandler;
import net.dmulloy2.handlers.CommandHandler;
import net.dmulloy2.handlers.LogHandler;
import net.dmulloy2.handlers.PermissionHandler;
import net.dmulloy2.handlers.ResourceHandler;
import net.dmulloy2.teamsparkle.commands.CmdBuy;
import net.dmulloy2.teamsparkle.commands.CmdGive;
import net.dmulloy2.teamsparkle.commands.CmdHelp;
import net.dmulloy2.teamsparkle.commands.CmdInvite;
import net.dmulloy2.teamsparkle.commands.CmdLeaderboard;
import net.dmulloy2.teamsparkle.commands.CmdReload;
import net.dmulloy2.teamsparkle.commands.CmdShop;
import net.dmulloy2.teamsparkle.commands.CmdStats;
import net.dmulloy2.teamsparkle.handlers.ShopHandler;
import net.dmulloy2.teamsparkle.io.PlayerDataCache;
import net.dmulloy2.teamsparkle.listeners.PlayerListener;
import net.dmulloy2.teamsparkle.types.PlayerData;
import net.dmulloy2.util.FormatUtil;
import net.dmulloy2.util.InventoryUtil;
import net.dmulloy2.util.TimeUtil;
import net.dmulloy2.util.Util;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * @author dmulloy2
 */

@Getter
public class TeamSparkle extends SwornPlugin
{
	private ResourceHandler resourceHandler;
	private ShopHandler shopHandler;
	private GUIHandler guiHandler;

	private PlayerDataCache playerDataCache;

	private int autoSaveTask = -1;
	private int rewardTask = -1;

	private String prefix = FormatUtil.format("&3[&eTeamSparkle&3]&e ");

	@Override
	public void onEnable()
	{
		long start = System.currentTimeMillis();

		logHandler = new LogHandler(this);

		// Configuration
		saveDefaultConfig();
		reloadConfig();
		Config.load(this);

		// Messages
		File messages = new File(getDataFolder(), "messages.properties");
		if (messages.exists())
			messages.delete();

		resourceHandler = new ResourceHandler(this, getClassLoader());

		// Register handlers
		permissionHandler = new PermissionHandler(this);
		commandHandler = new CommandHandler(this);
		shopHandler = new ShopHandler(this);
		guiHandler = new GUIHandler(this);

		playerDataCache = new PlayerDataCache(this);

		// Register commands
		commandHandler.setCommandPrefix("ts");
		commandHandler.registerPrefixedCommand(new CmdBuy(this));
		commandHandler.registerPrefixedCommand(new CmdGive(this));
		commandHandler.registerPrefixedCommand(new CmdHelp(this));
		commandHandler.registerPrefixedCommand(new CmdInvite(this));
		commandHandler.registerPrefixedCommand(new CmdLeaderboard(this));
		commandHandler.registerPrefixedCommand(new CmdReload(this));
		commandHandler.registerPrefixedCommand(new CmdShop(this));
		commandHandler.registerPrefixedCommand(new CmdStats(this));

		// Register listener
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(new PlayerListener(this), this);

		// Deploy auto save task
		if (Config.autoSaveEnabled)
		{
			long interval = TimeUtil.toTicks(60 * Config.autoSaveInterval);

			autoSaveTask = new BukkitRunnable()
			{
				@Override
				public void run()
				{
					playerDataCache.save();
				}
			}.runTaskTimerAsynchronously(this, interval, interval).getTaskId();
		}

		// Hourly rewards
		long interval = TimeUtil.toTicks(60 * 60); // 1 hour
		if (Config.hourlyRewardsEnabled && ! Config.hourlyRewards.isEmpty())
			rewardTask = new HourlyRewardTask().runTaskTimerAsynchronously(this, interval, interval).getTaskId();

		logHandler.log(getMessage("log_enabled"), getDescription().getFullName(), System.currentTimeMillis() - start);
	}

	@Override
	public void onDisable()
	{
		long start = System.currentTimeMillis();

		// Save data
		playerDataCache.save();

		// Cancel tasks
		getServer().getScheduler().cancelTask(rewardTask);
		getServer().getScheduler().cancelTask(autoSaveTask);

		logHandler.log(getMessage("log_disabled"), getDescription().getFullName(), System.currentTimeMillis() - start);
	}

	public void outConsole(String string, Object... objects)
	{
		logHandler.log(string, objects);
	}

	public void outConsole(Level level, String string, Object... objects)
	{
		logHandler.log(level, string, objects);
	}

	public void debug(String string, Object... objects)
	{
		logHandler.debug(string, objects);
	}

	@Override
	public void reload()
	{
		reloadConfig();
		Config.load(this);
		shopHandler.reload();
	}

	/**
	 * Gets a message with a given key from the messages.properties
	 *
	 * @param string Message key
	 * @return Associated message, or null if nonexistant
	 */
	public final String getMessage(String string)
	{
		return resourceHandler.getMessage(string);
	}

	/**
	 * Whether or not a given player was sparkled
	 *
	 * @param player Player to check
	 */
	public final boolean isSparkled(Player player)
	{
		return getSparkler(player) != null;
	}

	private final String getSparkler(Player player)
	{
		// This won't be performance intensive, since only players with data get saved
		for (Entry<String, PlayerData> entry : playerDataCache.getAllPlayerData().entrySet())
		{
			PlayerData data = entry.getValue();
			if (data.getInvited().contains(player.getName().toLowerCase()))
				return entry.getKey();
		}

		return null;
	}

	/**
	 * Handles the sparkle of a given player
	 *
	 * @param player Player who was sparkled
	 */
	public final void handleSparkle(Player player)
	{
		String uniqueId = getSparkler(player);
		if (uniqueId == null)
			return;

		OfflinePlayer sparkler = Util.matchOfflinePlayer(uniqueId);
		if (sparkler == null)
			return;

		logHandler.log("Handling {0}''s sparkle of {1}", sparkler.getName(), player.getName());

		// Commands for newly sparkled players
		List<String> commands = Config.sparkledRewards;
		if (! commands.isEmpty())
		{
			for (String command : commands)
			{
				command = replacePlayerVars(command, player);
				if (! getServer().dispatchCommand(getServer().getConsoleSender(), command))
				{
					// Oh no, something went wrong >:(
					logHandler.log(Level.WARNING, "Could not execute command \"{0}\"", command);
				}
				else
				{
					logHandler.debug("Executed command \"{0}\"", command);
				}
			}
		}

		// Welcome them
		player.sendMessage(FormatUtil.format(getMessage("sparkled_welcome"), player.getName()));

		// Reward the sparkler
		PlayerData data = playerDataCache.getData(sparkler);
		if (data == null)
			return;

		data.getInvited().remove(player.getName().toLowerCase());
		data.setTotalSparkles(data.getTotalSparkles() + 1);
		data.setTokens(data.getTokens() + 1);

		// Thank the sparkler
		if (sparkler.isOnline())
		{
			sparkler.getPlayer().sendMessage(FormatUtil.format(getMessage("sparkler_thanks"), player.getName()));
		}
	}

	/**
	 * Gives a player an item, then refreshes their inventory
	 *
	 * @param player {@link Player} to give item to
	 * @param stack {@link ItemStack} to give the player
	 */
	public final void giveItem(Player player, ItemStack stack)
	{
		InventoryUtil.giveItem(player, stack);
		player.updateInventory();
	}

	/**
	 * Replaces variables for player names
	 *
	 * @param string Base string to format
	 * @param player {@link Player} to replace vars for
	 */
	public final String replacePlayerVars(String string, Player player)
	{
		string = string.replaceAll("%p", player.getName());
		string = string.replaceAll("%player", player.getName());
		string = string.replaceAll("@p", player.getName());
		string = string.replaceAll("@player", player.getName());
		string = string.replaceAll("&p", player.getName());
		string = string.replaceAll("&player", player.getName());
		return string;
	}

	private List<String> extraHelp;

	@Override
	public List<String> getExtraHelp()
	{
		if (extraHelp == null)
		{
			extraHelp = Arrays.asList(FormatUtil.format(getMessage("extra_help_1")),
					FormatUtil.format(getMessage("extra_help_2")),
					FormatUtil.format(getMessage("extra_help_3")),
					FormatUtil.format(getMessage("extra_help_4")));
		}

		return extraHelp;
	}

	private CmdHelp helpCommand = new CmdHelp(this);

	/**
	 * Hourly Reward Task
	 */
	public final class HourlyRewardTask extends BukkitRunnable
	{
		@Override
		public void run()
		{
			if (! Config.hourlyRewardsEnabled)
			{
				getServer().getScheduler().cancelTask(rewardTask);
				return;
			}

			String message = getMessage("hourly_reward");
			String serverName = Config.serverName;

			for (Player player : Util.getOnlinePlayers())
			{
				List<String> rewards = Config.hourlyRewards;
				if (! rewards.isEmpty())
				{
					int rand = Util.random(rewards.size());
					String entry = rewards.get(rand);
					if (entry != null)
					{
						String[] split = entry.split(";");
						String command = replacePlayerVars(split[0], player);
						getServer().dispatchCommand(getServer().getConsoleSender(), command);

						player.sendMessage(FormatUtil.format(message, serverName, split[1]));
					}
				}
			}
		}
	}
}