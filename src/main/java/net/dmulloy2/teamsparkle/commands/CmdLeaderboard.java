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
import net.dmulloy2.teamsparkle.permissions.Permission;
import net.dmulloy2.teamsparkle.util.FormatUtil;
import net.dmulloy2.teamsparkle.util.Util;

import org.bukkit.OfflinePlayer;

/**
 * @author dmulloy2
 */

public class CmdLeaderboard extends TeamSparkleCommand
{
	public CmdLeaderboard(TeamSparkle plugin)
	{
		super(plugin);
		this.name = "leaderboard";
		this.aliases.add("lb");
		this.optionalArgs.add("page");
		this.description = "Displays top sparklers";
		
		this.permission = Permission.CMD_LEADERBOARD;
	}

	@Override
	public void perform()
	{
		sendpMessage(plugin.getMessage("leaderboard_wait"));
		Map<String, PlayerData> data = plugin.getPlayerDataCache().getAllPlayerData();
		HashMap<String, Integer> lbmap = new HashMap<String, Integer>();
		for (Entry<String, PlayerData> entrySet : data.entrySet())
		{
			String player = entrySet.getKey();
			PlayerData data1 = entrySet.getValue();
			int sparkles = data1.getTotalSparkles();
			lbmap.put(player, sparkles);
		}
		
		final List<Map.Entry<String, Integer>> sortedEntries = new ArrayList<Map.Entry<String, Integer>>(lbmap.entrySet());
		Collections.sort(
		sortedEntries, new Comparator<Map.Entry<String, Integer>>()
		{
			@Override
			public int compare(final Entry<String, Integer> entry1, final Entry<String, Integer> entry2)
			{
				return -entry1.getValue().compareTo(entry2.getValue());
			}
		});
		
		List<String>lines = new ArrayList<String>();
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
						line.append(FormatUtil.format(
								plugin.getMessage("leaderboard_format"), 
								pos, player.getName(),
								data2.getTotalSparkles()));
						
						lines.add(line.toString());
						pos++;
					}
				}
			}
		}
		
		for (String s : lines)
			sendMessage(s);
		
		sendMessage(plugin.getMessage("leaderboard_check"));
	}
}