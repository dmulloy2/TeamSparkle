package net.dmulloy2.teamsparkle.permissions;

/**
 * @author dmulloy2
 */

public enum Permission
{
	CMD_BUY("cmd.buy"),
	CMD_CONFIRM("cmd.confirm"),
	CMD_GIVE_TOKENS("cmd.givetokens"),
	CMD_HELP("cmd.help"),
	CMD_INVITE("cmd.invite"),
	CMD_LEADERBOARD("cmd.leaderboard"),
	CMD_RELOAD("cmd.reload"),
	CMD_SHOP("cmd.shop"),
	CMD_STATS("cmd.stats");
	
	public String node;
	Permission(String node) 
	{
		this.node = node;
	}
}