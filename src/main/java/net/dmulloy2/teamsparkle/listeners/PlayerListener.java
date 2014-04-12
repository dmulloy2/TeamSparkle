package net.dmulloy2.teamsparkle.listeners;

import lombok.AllArgsConstructor;
import net.dmulloy2.teamsparkle.TeamSparkle;
import net.dmulloy2.teamsparkle.types.PlayerData;
import net.dmulloy2.teamsparkle.util.TimeUtil;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * @author dmulloy2
 */

@AllArgsConstructor
public class PlayerListener implements Listener
{
	private final TeamSparkle plugin;

	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerJoin(PlayerJoinEvent event)
	{
		final Player player = event.getPlayer();
		PlayerData data = plugin.getPlayerDataCache().getData(player);
		if (data == null)
		{
			plugin.debug("Creating new data file for {0}!", player.getName());
			data = plugin.getPlayerDataCache().newData(player);
			data.setTotalSparkles(0);
			data.setTokens(0);
		}

		// UUID Stuff
		if (! data.getKnownBy().contains(player.getName()))
			data.getKnownBy().add(player.getName());

		data.setLastKnownBy(player.getName());

		if (! player.hasPlayedBefore() && plugin.isSparkled(player))
		{
			// Wait 6 seconds to reward
			new BukkitRunnable()
			{
				@Override
				public void run()
				{
					if (player != null && player.isOnline())
					{
						plugin.handleSparkle(player);
					}
				}
			}.runTaskLater(plugin, TimeUtil.toTicks(6));
		}
	}
}