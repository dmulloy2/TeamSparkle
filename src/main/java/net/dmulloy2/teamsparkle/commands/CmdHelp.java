package net.dmulloy2.teamsparkle.commands;

import java.util.ArrayList;
import java.util.List;

import net.dmulloy2.teamsparkle.TeamSparkle;
import net.dmulloy2.teamsparkle.permissions.Permission;
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
		this.description = "Shows " + plugin.getName() + " help.";
		this.linesPerPage = 6;
		
		this.permission = Permission.CMD_HELP;
	}

	@Override
	public int getListSize() 
	{
		return plugin.getCommandHandler().getRegisteredPrefixedCommands().size();
	}

	@Override
	public String getHeader(int index) 
	{
		return FormatUtil.format(getMessage("help_header"), index, getPageCount());
	}

	@Override
	public List<String> getLines(int startIndex, int endIndex) 
	{
		List<String> lines = new ArrayList<String>();
		for (int i = startIndex; i < endIndex && i < getListSize(); i++) 
		{
			TeamSparkleCommand command = plugin.getCommandHandler().getRegisteredPrefixedCommands().get(i);
			
			if (plugin.getPermissionHandler().hasPermission(sender, command.permission))
				lines.add(command.getUsageTemplate(true));
		}
		return lines;
	}

	
	@Override
	public String getLine(int index) 
	{
		return null;
	}
}