/**
 * (c) 2014 dmulloy2
 */
package net.dmulloy2.teamsparkle.gui;

import net.dmulloy2.gui.AbstractGUI;
import net.dmulloy2.teamsparkle.TeamSparkle;
import net.dmulloy2.teamsparkle.types.PlayerData;
import net.dmulloy2.teamsparkle.types.ShopItem;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 * @author dmulloy2
 */

public class ShopGUI extends AbstractGUI
{
	private final TeamSparkle plugin;
	public ShopGUI(TeamSparkle plugin, Player player)
	{
		super(plugin, player);
		this.plugin = plugin;
		this.setup();
	}

	@Override
	public int getSize()
	{
		return 27;
	}

	@Override
	public String getTitle()
	{
		return "TeamSparkle Shop";
	}

	@Override
	public void stock(Inventory inventory)
	{
		for (ItemStack item : plugin.getShopHandler().getShopList(player))
		{
			inventory.addItem(item);
		}
	}

	@Override
	public void onInventoryClick(InventoryClickEvent event)
	{
		Player player = (Player) event.getInventory().getHolder();
		ItemStack item = event.getCurrentItem();
		if (item != null)
		{
			ShopItem sItem = getShopItem(item.getType());
			if (sItem != null)
			{
				perform(player, sItem);
				player.closeInventory();
			}
		}

		event.setCancelled(true);		
	}

	private final ShopItem getShopItem(Material mat)
	{
		return plugin.getShopHandler().getItem(mat);
	}

	private final void perform(Player player, ShopItem item)
	{
		PlayerData data = plugin.getPlayerDataCache().getData(player);

		if (item.getCost() > data.getTokens())
		{
			err(plugin.getMessage("shop_insufficient_funds"));
			return;
		}

		plugin.getShopHandler().processPurchace(player, item);

		sendpMessage(plugin.getMessage("shop_purchace"), 1, item.getTitle(), item.getCost());
	}
}