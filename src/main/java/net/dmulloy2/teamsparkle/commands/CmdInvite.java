package net.dmulloy2.teamsparkle.commands;

import net.dmulloy2.teamsparkle.TeamSparkle;
import net.dmulloy2.teamsparkle.conversation.InvitePrompt;
import net.dmulloy2.teamsparkle.conversation.Prefix;
import net.dmulloy2.teamsparkle.types.Permission;

import org.bukkit.Material;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationFactory;

/**
 * @author dmulloy2
 */

public class CmdInvite extends TeamSparkleCommand
{
	public CmdInvite(TeamSparkle plugin)
	{
		super(plugin);
		this.name = "invite";
		this.description = "Invite a newly sparkled player";
		this.permission = Permission.CMD_INVITE;
		this.mustBePlayer = true;
	}

	@Override
	public void perform()
	{
		InvitePrompt prompt = new InvitePrompt(player, plugin);
		Conversation convo = new ConversationFactory(plugin)
			.withFirstPrompt(prompt)
			.withPrefix(new Prefix(plugin))
			.buildConversation(player);
		convo.begin();
	}

	@Override
	public Material getHelpMaterial()
	{
		return Material.SKULL_ITEM;
	}
}