package net.dmulloy2.teamsparkle.commands;

import net.dmulloy2.teamsparkle.TeamSparkle;
import net.dmulloy2.teamsparkle.gui.ShopGUI;
import net.dmulloy2.teamsparkle.types.Permission;

import org.bukkit.Material;

/**
 * @author dmulloy2
 */

public class CmdShop extends TeamSparkleCommand
{
	public CmdShop(TeamSparkle plugin)
	{
		super(plugin);
		this.name = "shop";
		this.description = "Displays TeamSparkle shop";
		this.permission = Permission.CMD_SHOP;
		this.mustBePlayer = true;
	}

	@Override
	public void perform()
	{
		ShopGUI sGUI = new ShopGUI(plugin, player);
		plugin.getGuiHandler().open(player, sGUI);
	}

	@Override
	public Material getHelpMaterial()
	{
		return Material.DIAMOND;
	}
}