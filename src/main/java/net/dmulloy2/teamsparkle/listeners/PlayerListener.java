package net.dmulloy2.teamsparkle.listeners;

import java.util.Map.Entry;

import net.dmulloy2.teamsparkle.TeamSparkle;
import net.dmulloy2.teamsparkle.data.PlayerData;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * @author dmulloy2
 */

public class PlayerListener implements Listener
{
	private final TeamSparkle plugin;
	public PlayerListener(TeamSparkle plugin)
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
		
		if (! plugin.getSparkled().containsValue(player.getName()))
			return;

		String sparklern = null;
			
		for (Entry<String, String> entry : plugin.getSparkled().entrySet())
		{
			if (entry.getValue().equalsIgnoreCase(player.getName()))
			{
				sparklern = entry.getKey();
			}
		}
		
		if (sparklern == null)
			return;

		new SparkleRewardTask(player, sparklern).runTaskLater(plugin, 120L);
	}
	
	public class SparkleRewardTask extends BukkitRunnable
	{
		private final Player sparkled;
		private final String sparkler;
		public SparkleRewardTask(Player sparkled, String sparkler)
		{
			this.sparkled = sparkled;
			this.sparkler = sparkler;
		}
		
		@Override
		public void run()
		{
			if (sparkled != null && sparkled.isOnline())
			{
				plugin.rewardSparkledPlayer(sparkled.getName(), sparkler);
			}
		}
	}
}