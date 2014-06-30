package net.dmulloy2.teamsparkle.types;

import lombok.AllArgsConstructor;
import lombok.Getter;

import org.bukkit.inventory.ItemStack;

/**
 * @author dmulloy2
 */

@Getter
@AllArgsConstructor
public class ShopItem
{
	private final int cost;
	private final ItemStack icon;
	private final String title;
	private final String command;

	public final ItemStack getIcon()
	{
		return new ItemStack(icon.getType());
	}
}