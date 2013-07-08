package net.dmulloy2.teamsparkle;

/**
 * @author dmulloy2
 */

public class ShopItem
{
	private final int cost;
	private final String command;
	private final String message;
	
	public ShopItem(final int cost, final String command, final String message)
	{
		this.cost = cost;
		this.command = command;
		this.message = message;
	}
	
	public final int getCost()
	{
		return cost;
	}
	
	public final String getCommand()
	{
		return command;
	}
	
	public final String getMessage()
	{
		return message;
	}
}