package net.dmulloy2.teamsparkle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.logging.Level;

import org.bukkit.entity.Player;

/**
 * @author dmulloy2
 */

public class ShopManager
{
	private HashMap<Integer, ShopItem> items;
	private final TeamSparkle plugin;
	public ShopManager(final TeamSparkle plugin)
	{
		this.plugin = plugin;
		this.items = new HashMap<Integer, ShopItem>();
		
		updateShopList();
	}
	
	public final List<String> getShopList()
	{
		List<String> lines = new ArrayList<String>();
		
		for (Entry<Integer, ShopItem> item : items.entrySet())
		{
			lines.add(item.getValue().getMessage());
		}
		
		return lines;
	}
	
	public void updateShopList()
	{
		items.clear();
		
		List<String> configItems = plugin.getConfig().getStringList("shopItems");
		if (! configItems.isEmpty())
		{
			for (int i = 0; i < plugin.getConfig().getStringList("shopItems").size(); i++)
			{
				String entry = plugin.getConfig().getStringList("shopItems").get(i);
				int cost = Integer.parseInt(entry.substring(0, entry.indexOf(":")));
				String command = entry.substring(entry.indexOf(":") + 1, entry.indexOf(";"));
				String message = entry.substring(entry.indexOf(";") + 1);
			
				ShopItem item = new ShopItem(cost, command, message);
				items.put(i, item);
			}
		}
		else
		{
			plugin.outConsole(Level.WARNING, "Error building shop list: Shop Item List cannot be empty!");
		}
	}
	
	public ShopItem getItem(int index)
	{
		return items.get(index);
	}
	
	public void sendCommand(Player player, ShopItem item)
	{
		String command = item.getCommand();
		command = command.replaceAll("%p", player.getName());
		plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), command);
	}
}