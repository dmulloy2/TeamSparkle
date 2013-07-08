package net.dmulloy2.teamsparkle;

import net.dmulloy2.teamsparkle.util.Util;

/**
 * @author dmulloy2
 */

public class SparkledInstance 
{
	private final String sparkler;
	private final String sparkled;
	private final int pin;
	
	public SparkledInstance(final String sparkler, final String sparkled)
	{
		this.sparkler = sparkler;
		this.sparkled = sparkled;
		this.pin = Util.generatePin();
	}
	
	public final String getSparkler()
	{
		return sparkler;
	}
	
	public final String getSparkled()
	{
		return sparkled;
	}
	
	public final int getPin()
	{
		return pin;
	}
}