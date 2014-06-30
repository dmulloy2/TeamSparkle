/**
 * (c) 2014 dmulloy2
 */
package net.dmulloy2.teamsparkle.gui;

import java.util.ArrayList;
import java.util.List;

import net.dmulloy2.commands.Command;
import net.dmulloy2.gui.AbstractGUI;
import net.dmulloy2.teamsparkle.TeamSparkle;
import net.dmulloy2.teamsparkle.commands.PaginatedCommand;
import net.dmulloy2.teamsparkle.commands.TeamSparkleCommand;
import net.dmulloy2.util.FormatUtil;
import net.minecraft.util.org.apache.commons.lang3.text.WordUtils;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * @author dmulloy2
 */

public class HelpCommandGUI extends AbstractGUI
{
	private final TeamSparkle plugin;
	public HelpCommandGUI(TeamSparkle plugin, Player player)
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
		return "Team Sparkle Help";
	}

	@Override
	public void stock(Inventory inventory)
	{
		for (Command command : plugin.getCommandHandler().getRegisteredPrefixedCommands())
		{
			Material mat = null;
			if (command instanceof TeamSparkleCommand)
				mat = ((TeamSparkleCommand) command).getHelpMaterial();
			else if (command instanceof PaginatedCommand)
				mat = ((PaginatedCommand) command).getHelpMaterial();

			if (mat != null)
			{
				ItemStack stack = new ItemStack(mat);
				if (mat == Material.SKULL_ITEM)
					stack.setDurability((short) 3);

				ItemMeta meta = stack.getItemMeta();

				String name = mat == Material.NETHER_STAR ? "TeamSparkle" : command.getName();
				name = WordUtils.capitalize(name);
				name = FormatUtil.format("&b" + name);
				meta.setDisplayName(name);

				List<String> lore = new ArrayList<String>();
				if (mat == Material.NETHER_STAR)
				{
					lore.addAll(plugin.getExtraHelp());
				}
				else
				{
					for (String line : command.getDescription())
					{
						lore.add(FormatUtil.format("&e" + line));
					}
				}

				meta.setLore(lore);
				stack.setItemMeta(meta);
				inventory.addItem(stack);
			}
		}
	}

	@Override
	public void onInventoryClick(InventoryClickEvent event)
	{
		Player player = (Player) event.getInventory().getHolder();
		ItemStack item = event.getCurrentItem();
		if (item != null)
		{
			Command command = getCommand(item.getType());
			if (command != null)
			{
				player.closeInventory();
				command.execute(player, new String[0]);
			}
		}

		event.setCancelled(true);		
	}

	private final Command getCommand(Material mat)
	{
		for (Command command : plugin.getCommandHandler().getRegisteredPrefixedCommands())
		{
			if (command instanceof TeamSparkleCommand)
				if (mat == ((TeamSparkleCommand) command).getHelpMaterial())
					return command;

			else if (command instanceof PaginatedCommand)
				if (mat == ((PaginatedCommand) command).getHelpMaterial())
					return command;
		}

		return null;
	}
}