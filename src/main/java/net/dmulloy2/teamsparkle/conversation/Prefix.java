/**
 * (c) 2014 dmulloy2
 */
package net.dmulloy2.teamsparkle.conversation;

import lombok.AllArgsConstructor;
import net.dmulloy2.teamsparkle.TeamSparkle;

import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.ConversationPrefix;

/**
 * @author dmulloy2
 */

@AllArgsConstructor
public class Prefix implements ConversationPrefix
{
	private final TeamSparkle plugin;

	@Override
	public String getPrefix(ConversationContext context)
	{
		return plugin.getPrefix();
	}
}