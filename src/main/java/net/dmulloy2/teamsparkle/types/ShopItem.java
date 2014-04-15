package net.dmulloy2.teamsparkle.types;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author dmulloy2
 */

@Getter
@AllArgsConstructor
public class ShopItem
{
	private final int cost;
	private final String command;
	private final String message;
}