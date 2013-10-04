package net.dmulloy2.teamsparkle.commands;

import java.util.ArrayList;
import java.util.List;

import net.dmulloy2.teamsparkle.TeamSparkle;
import net.dmulloy2.teamsparkle.util.FormatUtil;

/**
 * @author dmulloy2
 */

public class CmdHelp extends PaginatedCommand
{
	public CmdHelp(TeamSparkle plugin)
	{
		super(plugin);
		this.name = "help";
		this.optionalArgs.add("page");
		this.description = "Shows " + plugin.getName() + " help";
		this.linesPerPage = 6;

		this.usesPrefix = true;
	}

	@Override
	public int getListSize()
	{
		return buildHelpMenu().size();
	}

	@Override
	public String getHeader(int index)
	{
		StringBuilder ret = new StringBuilder();
		ret.append(FormatUtil.format(plugin.getMessage("help_header"), index, getPageCount()));
		ret.append('\n'); // Force a new line
		ret.append(FormatUtil.format(plugin.getMessage("help_promo")));
		ret.append(FormatUtil.format(plugin.getMessage("help_promo_ln2")));

		return ret.toString();
	}

	@Override
	public List<String> getLines(int startIndex, int endIndex)
	{
		List<String> lines = new ArrayList<String>();
		for (int i = startIndex; i < endIndex && i < getListSize(); i++)
		{
			lines.add(buildHelpMenu().get(i));
		}

		return lines;
	}

	@Override
	public String getLine(int index)
	{
		return null;
	}

	private final List<String> buildHelpMenu()
	{
		List<String> ret = new ArrayList<String>();

		for (TeamSparkleCommand cmd : plugin.getCommandHandler().getRegisteredPrefixedCommands())
		{
			if (plugin.getPermissionHandler().hasPermission(sender, cmd.permission))
			{
				ret.add(cmd.getUsageTemplate(true));
			}
		}

		for (TeamSparkleCommand cmd : plugin.getCommandHandler().getRegisteredCommands())
		{
			if (plugin.getPermissionHandler().hasPermission(sender, cmd.permission))
			{
				ret.add(cmd.getUsageTemplate(true));
			}
		}

		return ret;
	}
}