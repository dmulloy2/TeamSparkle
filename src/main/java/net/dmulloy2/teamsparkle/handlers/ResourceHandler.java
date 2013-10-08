package net.dmulloy2.teamsparkle.handlers;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.logging.Level;

import net.dmulloy2.teamsparkle.TeamSparkle;
import net.dmulloy2.teamsparkle.io.FileResourceLoader;

/**
 * @author dmulloy2
 */

public class ResourceHandler
{
	private ResourceBundle messages;

	public TeamSparkle plugin;

	public ResourceHandler(TeamSparkle plugin, ClassLoader classLoader)
	{
		try
		{
			messages = ResourceBundle.getBundle("messages", Locale.getDefault(), new FileResourceLoader(classLoader, plugin));
		}
		catch (MissingResourceException ex)
		{
			plugin.getLogger().log(Level.SEVERE, "Could not find resource bundle: messages.properties");
		}
	}

	public ResourceBundle getMessages()
	{
		return messages;
	}
}