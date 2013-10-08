package net.dmulloy2.teamsparkle.handlers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.logging.Level;

import net.dmulloy2.teamsparkle.TeamSparkle;
import net.dmulloy2.teamsparkle.types.PlayerData;
import net.dmulloy2.teamsparkle.types.ShopItem;

import org.bukkit.entity.Player;

/**
 * @author dmulloy2
 */

public class ShopHandler
{
	private HashMap<Integer, ShopItem> items;

	private final TeamSparkle plugin;

	public ShopHandler(TeamSparkle plugin)
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

	public final void onDisable()
	{
		items.clear();
		items = null;
	}

	public final void reload()
	{
		items.clear();
		items = null;

		items = new HashMap<Integer, ShopItem>();

		updateShopList();
	}

	private final void updateShopList()
	{
		List<String> configItems = plugin.getConfig().getStringList("shopItems");
		if (!configItems.isEmpty())
		{
			for (int i = 0; i < plugin.getConfig().getStringList("shopItems").size(); i++)
			{
				items.put(i, readItem(plugin.getConfig().getStringList("shopItems").get(i)));
			}
		}
		else
		{
			plugin.outConsole(Level.WARNING, "Error building shop list: Shop Item List cannot be empty!");
		}
	}

	private final ShopItem readItem(String entry)
	{
		int cost = Integer.parseInt(entry.substring(0, entry.indexOf(":")));
		String command = entry.substring(entry.indexOf(":") + 1, entry.indexOf(";"));
		String message = entry.substring(entry.indexOf(";") + 1);

		return new ShopItem(cost, command, message);
	}

	public final void processPurchace(Player player, ShopItem item)
	{
		sendCommand(player, item);

		PlayerData data = plugin.getPlayerDataCache().getData(player);
		data.setTokens(data.getTokens() - item.getCost());
	}

	public final ShopItem getItem(int index)
	{
		return items.get(index);
	}

	public final void sendCommand(Player player, ShopItem item)
	{
		String command = item.getCommand();
		command = command.replaceAll("%p", player.getName());
		plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), command);
	}
}