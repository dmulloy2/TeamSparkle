package net.dmulloy2.teamsparkle.handlers;

import java.util.ArrayList;
import java.util.List;

import net.dmulloy2.teamsparkle.TeamSparkle;
import net.dmulloy2.teamsparkle.commands.CmdHelp;
import net.dmulloy2.teamsparkle.commands.TeamSparkleCommand;
import net.dmulloy2.teamsparkle.util.FormatUtil;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;

/**
 * @author dmulloy2
 */

public class CommandHandler implements CommandExecutor
{
	private final TeamSparkle plugin;
	// Only need the name of command prefix - all other aliases listed in
	// plugin.yml will be usable
	private String commandPrefix;
	private List<TeamSparkleCommand> registeredPrefixedCommands;
	private List<TeamSparkleCommand> registeredCommands;

	public CommandHandler(TeamSparkle plugin)
	{
		this.plugin = plugin;
		registeredCommands = new ArrayList<TeamSparkleCommand>();
	}

	public void registerCommand(TeamSparkleCommand command)
	{
		PluginCommand pluginCommand = plugin.getCommand(command.getName());
		if (pluginCommand != null)
		{
			pluginCommand.setExecutor(command);
			registeredCommands.add(command);
		}
		else
		{
			plugin.outConsole("Entry for command {0} is missing in plugin.yml", command.getName());
		}
	}

	public void registerPrefixedCommand(TeamSparkleCommand command)
	{
		if (commandPrefix != null)
			registeredPrefixedCommands.add(command);
	}

	public List<TeamSparkleCommand> getRegisteredCommands()
	{
		return registeredCommands;
	}

	public List<TeamSparkleCommand> getRegisteredPrefixedCommands()
	{
		return registeredPrefixedCommands;
	}

	public String getCommandPrefix()
	{
		return commandPrefix;
	}

	public void setCommandPrefix(String commandPrefix)
	{
		this.commandPrefix = commandPrefix;
		registeredPrefixedCommands = new ArrayList<TeamSparkleCommand>();
		plugin.getCommand(commandPrefix).setExecutor(this);
	}

	public boolean usesCommandPrefix()
	{
		return commandPrefix != null;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		List<String> argsList = new ArrayList<String>();

		if (args.length > 0)
		{
			String commandName = args[0];
			for (int i = 1; i < args.length; i++)
				argsList.add(args[i]);

			for (TeamSparkleCommand command : registeredPrefixedCommands)
			{
				if (commandName.equalsIgnoreCase(command.getName()) || command.getAliases().contains(commandName.toLowerCase()))
				{
					command.execute(sender, argsList.toArray(new String[0]));
					return true;
				}
			}
			sender.sendMessage(FormatUtil.format(plugin.getMessage("error") + plugin.getMessage("unknown_command"), args[0]));
		}
		else
		{
			new CmdHelp(plugin).execute(sender, args);
		}

		return true;
	}
}