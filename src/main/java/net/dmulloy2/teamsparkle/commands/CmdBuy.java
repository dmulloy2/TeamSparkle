package net.dmulloy2.teamsparkle.commands;

import org.bukkit.Material;

import net.dmulloy2.teamsparkle.TeamSparkle;
import net.dmulloy2.teamsparkle.types.Permission;
import net.dmulloy2.teamsparkle.types.PlayerData;
import net.dmulloy2.teamsparkle.types.ShopItem;

/**
 * @author dmulloy2
 */

public class CmdBuy extends TeamSparkleCommand
{
	public CmdBuy(TeamSparkle plugin)
	{
		super(plugin);
		this.name = "buy";
		this.aliases.add("b");
		this.requiredArgs.add("index");
		this.description = "Purchace an item from the shop";
		this.permission = Permission.CMD_BUY;
		this.mustBePlayer = true;
	}

	@Override
	public void perform()
	{
		PlayerData data = getPlayerData(player);

		int index = argAsInt(0, true);
		if (index == -1)
			return;

		ShopItem item = plugin.getShopHandler().getItem(index);
		if (item == null)
		{
			err(getMessage("item_not_found"));
			return;
		}

		if (item.getCost() > data.getTokens())
		{
			err(getMessage("shop_insufficient_funds"));
			return;
		}

		plugin.getShopHandler().processPurchace(player, item);

		sendpMessage(getMessage("shop_purchace"), 1, item.getTitle(), item.getCost());
	}

	@Override
	public Material getHelpMaterial()
	{
		// Don't display in help menu
		return null;
	}
}