package net.dmulloy2.teamsparkle.commands;

import java.util.ArrayList;
import java.util.List;

import net.dmulloy2.teamsparkle.TeamSparkle;
import net.dmulloy2.teamsparkle.types.Permission;
import net.dmulloy2.teamsparkle.types.ShopItem;
import net.dmulloy2.util.FormatUtil;

/**
 * @author dmulloy2
 */

public class CmdShop extends PaginatedCommand
{
	public CmdShop(TeamSparkle plugin)
	{
		super(plugin);
		this.name = "shop";
		this.optionalArgs.add("page");
		this.description = "Displays TeamSparkle shop";
		this.linesPerPage = 6;

		this.permission = Permission.CMD_SHOP;
	}

	@Override
	public int getListSize()
	{
		return plugin.getShopHandler().getShopList().size();
	}

	@Override
	public String getHeader(int index)
	{
		return FormatUtil.format(getMessage("shop_header"), index, getPageCount());
	}

	@Override
	public List<String> getLines(int startIndex, int endIndex)
	{
		List<String> lines = new ArrayList<String>();
		for (int i = startIndex; i < endIndex && i < getListSize(); i++)
		{
			ShopItem item = plugin.getShopHandler().getItem(i);
			if (item != null)
			{
				String line = FormatUtil.format(plugin.getMessage("shop_entry"), i, item.getMessage(), 1, item.getCost());
				lines.add(line);
			}
		}

		return lines;
	}

	@Override
	public String getLine(int index)
	{
		return null;
	}
}