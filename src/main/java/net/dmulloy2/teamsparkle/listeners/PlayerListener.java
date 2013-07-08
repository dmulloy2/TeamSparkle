package net.dmulloy2.teamsparkle.listeners;

import net.dmulloy2.teamsparkle.TeamSparkle;
import net.dmulloy2.teamsparkle.data.PlayerData;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 * @author dmulloy2
 */

public class PlayerListener implements Listener
{
	private final TeamSparkle plugin;
	public PlayerListener(final TeamSparkle plugin)
	{
		this.plugin = plugin;
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerJoin(PlayerJoinEvent event)
	{
		Player player = event.getPlayer();
		PlayerData data = plugin.getPlayerDataCache().getData(player.getName());
		if (data == null)
		{
			plugin.debug("Creating new data file for {0}!", player.getName());
			data = plugin.getPlayerDataCache().newData(player.getName());
			data.setTotalSparkles(0);
			data.setTokens(0);
		}
	}
}