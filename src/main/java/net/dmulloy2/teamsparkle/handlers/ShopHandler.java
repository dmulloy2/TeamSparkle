package net.dmulloy2.teamsparkle.handlers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;

import net.dmulloy2.teamsparkle.TeamSparkle;
import net.dmulloy2.teamsparkle.types.PlayerData;
import net.dmulloy2.teamsparkle.types.ShopItem;
import net.dmulloy2.types.Reloadable;
import net.dmulloy2.util.ItemUtil;
import net.dmulloy2.util.Util;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.MemorySection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * @author dmulloy2
 */

public class ShopHandler implements Reloadable
{
	private List<ShopItem> items;

	private final TeamSparkle plugin;
	public ShopHandler(TeamSparkle plugin)
	{
		this.plugin = plugin;
		this.items = new ArrayList<>();
		this.reload();
	}

	public final List<ItemStack> getShopList(Player player)
	{
		List<ItemStack> ret = new ArrayList<>();

		PlayerData data = plugin.getPlayerDataCache().getData(player);
		for (ShopItem item : items)
		{
			ItemStack icon = item.getIcon();
			ItemMeta meta = icon.getItemMeta();
			meta.setDisplayName(ChatColor.AQUA + item.getTitle());

			List<String> lore = new ArrayList<>();
			lore.add(ChatColor.GRAY + "Cost: " + item.getCost());
			if (data.canAfford(item))
				lore.add(ChatColor.GREEN + "Can afford!");
			else
				lore.add(ChatColor.RED + "Cannot afford!");
			meta.setLore(lore);
			icon.setItemMeta(meta);
			ret.add(icon);
		}

		return ret;
	}

	public final ShopItem getItem(int index)
	{
		if (index <= items.size())
			return items.get(index);

		return null;
	}

	public final ShopItem getItem(Material type)
	{
		for (ShopItem item : items)
		{
			if (item.getIcon().getType() == type)
				return item;
		}

		return null;
	}

	public List<ShopItem> getItems()
	{
		return items;
	}

	public final void processPurchace(Player player, ShopItem item)
	{
		sendCommand(player, item);

		PlayerData data = plugin.getPlayerDataCache().getData(player);
		data.setTokens(data.getTokens() - item.getCost());
	}

	public final void sendCommand(Player player, ShopItem item)
	{
		String command = item.getCommand();
		command = plugin.replacePlayerVars(command, player);
		plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), command);
	}

	private final ShopItem readItem(String key, MemorySection section)
	{
		int cost = section.getInt("cost");
		ItemStack icon = ItemUtil.readItem(section.getString("icon"));
		String command = section.getString("command");

		return new ShopItem(cost, icon, key, command);
	}

	@Override
	public void reload()
	{
		this.items = new ArrayList<>();

		if (! plugin.getConfig().isSet("shopItems"))
		{
			plugin.getLogHandler().log(Level.WARNING, "Shop list is empty!");
			return;
		}

		Map<String, Object> values = plugin.getConfig().getConfigurationSection("shopItems").getValues(false);
		for (Entry<String, Object> entry : values.entrySet())
		{
			try
			{
				ShopItem item = readItem(entry.getKey(), (MemorySection) entry.getValue());
				if (item != null)
					items.add(item);
			}
			catch (Throwable ex)
			{
				plugin.getLogHandler().log(Level.WARNING, Util.getUsefulStack(ex, "loading shop item " + entry.getKey()));
			}
		}
	}
}