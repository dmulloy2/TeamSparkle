package net.dmulloy2.teamsparkle.commands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.dmulloy2.teamsparkle.TeamSparkle;
import net.dmulloy2.teamsparkle.data.PlayerData;
import net.dmulloy2.teamsparkle.util.FormatUtil;
import net.dmulloy2.teamsparkle.util.Util;

import org.bukkit.OfflinePlayer;

/**
 * @author dmulloy2
 */

public class CmdLeaderboard extends TeamSparkleCommand
{
	protected boolean updating;
	protected long lastUpdateTime;
	protected List<String> leaderboard;

	public CmdLeaderboard(TeamSparkle plugin)
	{
		super(plugin);
		this.name = "lb";
		this.aliases.add("top");
		this.description = "Display top sparklers";

		this.usesPrefix = true;
	}

	@Override
	public void perform()
	{
		if (leaderboard == null)
		{
			this.leaderboard = new ArrayList<String>();
		}

		if ((System.currentTimeMillis() - lastUpdateTime) > 18000L)
		{
			sendpMessage(getMessage("leaderboard_wait"));

			new BuildLeaderboardThread();
		}

		new DisplayLeaderboardThread();
	}

	public void displayLeaderboard()
	{
		int index = 1;

		if (args.length > 0)
		{
			int indexFromArg = argAsInt(0, false);
			if (indexFromArg > 1)
				index = indexFromArg;
		}

		int pageCount = getPageCount();

		if (index > pageCount)
		{
			err(getMessage("error-no-page-with-index"), args[0]);
			return;
		}

		for (String s : getPage(index))
			sendMessage(s);
	}

	private int linesPerPage = 10;

	public int getPageCount()
	{
		return (getListSize() + linesPerPage - 1) / linesPerPage;
	}

	public int getListSize()
	{
		return leaderboard.size();
	}

	public List<String> getPage(int index)
	{
		List<String> lines = new ArrayList<String>();

		StringBuilder line = new StringBuilder();
		line.append(getHeader(index));
		lines.add(line.toString());

		lines.addAll(getLines((index - 1) * linesPerPage, index * linesPerPage));

		if (index != getPageCount())
		{
			line = new StringBuilder();
			line.append(FormatUtil.format(getMessage("leaderboard_nextpage"), index + 1));
			lines.add(line.toString());
		}

		return lines;
	}

	public String getHeader(int index)
	{
		return FormatUtil.format(getMessage("leaderboard_header"), index, getPageCount());
	}

	public List<String> getLines(int startIndex, int endIndex)
	{
		List<String> lines = new ArrayList<String>();
		for (int i = startIndex; i < endIndex && i < getListSize(); i++)
		{
			lines.add(leaderboard.get(i));
		}

		return lines;
	}

	public class BuildLeaderboardThread extends Thread
	{
		private Thread thread;

		public BuildLeaderboardThread()
		{
			this.thread = new Thread(this, "TeamSparkle-BuildLeaderboard");
			this.thread.start();
		}

		@Override
		public void run()
		{
			updating = true;

			plugin.outConsole("Updating leaderboard...");

			long start = System.currentTimeMillis();

			Map<String, PlayerData> data = plugin.getPlayerDataCache().getAllPlayerData();
			HashMap<String, Integer> sparkleMap = new HashMap<String, Integer>();
			for (Entry<String, PlayerData> entrySet : data.entrySet())
			{
				String player = entrySet.getKey();
				PlayerData data1 = entrySet.getValue();
				int sparkles = data1.getTotalSparkles();
				if (sparkles > 0)
					sparkleMap.put(player, sparkles);
			}

			List<Entry<String, Integer>> sortedEntries = new ArrayList<Entry<String, Integer>>(sparkleMap.entrySet());
			Collections.sort(sortedEntries, new Comparator<Entry<String, Integer>>()
			{
				@Override
				public int compare(final Entry<String, Integer> entry1, final Entry<String, Integer> entry2)
				{
					return -entry1.getValue().compareTo(entry2.getValue());
				}
			});

			List<String> lines = new ArrayList<String>();
			StringBuilder line = new StringBuilder();
			line.append(FormatUtil.format(plugin.getMessage("leaderboard_header")));
			lines.add(line.toString());

			int pos = 1;
			for (Map.Entry<String, Integer> entry : sortedEntries)
			{
				if (pos <= 10)
				{
					String string = entry.getKey();
					OfflinePlayer player = Util.matchOfflinePlayer(string);
					if (player != null)
					{
						PlayerData data2 = getPlayerData(player);
						if (data2 != null)
						{
							line = new StringBuilder();
							line.append(FormatUtil.format(plugin.getMessage("leaderboard_format"), pos, player.getName(),
									data2.getTotalSparkles()));

							lines.add(line.toString());
							pos++;
						}
					}
				}
			}

			leaderboard = lines;

			lastUpdateTime = System.currentTimeMillis();

			updating = false;

			plugin.outConsole("Leaderboard updated! [{0}ms]", System.currentTimeMillis() - start);
		}
	}

	public class DisplayLeaderboardThread extends Thread
	{
		private Thread thread;

		public DisplayLeaderboardThread()
		{
			this.thread = new Thread(this, "TeamSparkle-DisplayLeaderboard");
			this.thread.start();
		}

		@Override
		public void run()
		{
			try
			{
				while (updating)
				{
					sleep(500L);
				}

				displayLeaderboard();
			}
			catch (Exception e)
			{
				err("Could not update leaderboard: {0}", e);
			}
		}
	}
}