/**
 * (c) 2014 dmulloy2
 */
package net.dmulloy2.teamsparkle.commands;

import net.dmulloy2.teamsparkle.TeamSparkle;
import net.dmulloy2.teamsparkle.gui.HelpCommandGUI;

import org.bukkit.Material;
import org.bukkit.entity.Player;

/**
 * @author dmulloy2
 */

public class CmdHelp extends TeamSparkleCommand
{
	public CmdHelp(TeamSparkle plugin)
	{
		super(plugin);
		this.name = "help";
		this.addOptionalArg("page");
		this.description = "Displays TeamSparkle help";
	}

	@Override
	public void perform()
	{
		if (sender instanceof Player)
		{
			HelpCommandGUI hcGUI = new HelpCommandGUI(plugin, player);
			plugin.getGuiHandler().open(player, hcGUI);
			return;
		}

		new net.dmulloy2.commands.CmdHelp(plugin).execute(sender, args);
	}

	@Override
	public Material getHelpMaterial()
	{
		return Material.NETHER_STAR;
	}
}