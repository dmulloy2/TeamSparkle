package net.dmulloy2.teamsparkle.listeners;

import lombok.AllArgsConstructor;
import net.dmulloy2.teamsparkle.TeamSparkle;
import net.dmulloy2.util.TimeUtil;

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