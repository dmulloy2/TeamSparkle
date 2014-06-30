/**
 * (c) 2014 dmulloy2
 */
package net.dmulloy2.teamsparkle.conversation;

import lombok.AllArgsConstructor;
import net.dmulloy2.teamsparkle.TeamSparkle;
import net.dmulloy2.teamsparkle.types.PlayerData;
import net.dmulloy2.util.FormatUtil;
import net.dmulloy2.util.Util;

import org.bukkit.OfflinePlayer;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.entity.Player;

/**
 * @author dmulloy2
 */

@AllArgsConstructor
public class InvitePrompt extends StringPrompt
{
	private final Player player;
	private final TeamSparkle plugin;

	@Override
	public String getPromptText(ConversationContext context)
	{
		return FormatUtil.format("&eWho would you like to invite?");
	}

	@Override
	public Prompt acceptInput(ConversationContext context, String input)
	{
		PlayerData data = plugin.getPlayerDataCache().getData(player);
		if (data.getInvited().contains(input.toLowerCase()))
		{
			player.sendRawMessage(FormatUtil.format("&cError: &4" + plugin.getMessage("already_invited")));
			return Prompt.END_OF_CONVERSATION;
		}

		OfflinePlayer invite = Util.matchOfflinePlayer(input);
		if (invite == null || ! invite.hasPlayedBefore())
		{
			data.getInvited().add(input.toLowerCase());
			player.sendRawMessage(FormatUtil.format(plugin.getPrefix() + plugin.getMessage("invite_confirmed"), input));
		}
		else
		{
			player.sendRawMessage(FormatUtil.format("&cError: &4" + plugin.getMessage("has_played_before")));
		}

		return Prompt.END_OF_CONVERSATION;
	}
}