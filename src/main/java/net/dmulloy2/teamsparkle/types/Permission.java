package net.dmulloy2.teamsparkle.types;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author dmulloy2
 */

@Getter
@AllArgsConstructor
public enum Permission
{
	CMD_BUY("cmd.buy"),
	CMD_CONFIRM("cmd.confirm"),
	CMD_GIVE("cmd.give"),
	CMD_HELP("cmd.help"),
	CMD_INVITE("cmd.invite"),
	CMD_LEADERBOARD("cmd.leaderboard"),
	CMD_RELOAD("cmd.reload"),
	CMD_SHOP("cmd.shop"),
	CMD_STATS("cmd.stats");

	private final String node;
}