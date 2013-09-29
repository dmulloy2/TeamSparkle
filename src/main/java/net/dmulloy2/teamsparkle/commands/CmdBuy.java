package net.dmulloy2.teamsparkle.commands;

import net.dmulloy2.teamsparkle.ShopItem;
import net.dmulloy2.teamsparkle.TeamSparkle;
import net.dmulloy2.teamsparkle.data.PlayerData;
import net.dmulloy2.teamsparkle.permissions.Permission;

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
		
		ShopItem item = plugin.getShopManager().getItem(index);
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
		
		plugin.getShopManager().sendCommand(player, item);
		data.setTokens(data.getTokens() - item.getCost());

		sendMessage(getMessage("shop_purchace"), 1, item.getMessage(), item.getCost());
	}
}