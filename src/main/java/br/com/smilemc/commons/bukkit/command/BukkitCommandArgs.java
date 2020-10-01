package br.com.smilemc.commons.bukkit.command;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import br.com.smilemc.commons.bukkit.account.BukkitAccount;
import br.com.smilemc.commons.common.Common;
import br.com.smilemc.commons.common.account.Account;
import br.com.smilemc.commons.common.account.permissions.Group;
import br.com.smilemc.commons.common.command.CommandArgs;

public class BukkitCommandArgs extends CommandArgs {

//	CommandSender sender, String label, String[] args, int subCommand
	protected BukkitCommandArgs(CommandSender sender, String label, String[] args, int subCommand) {
		super(new BukkitCommandSender(sender), label, args, subCommand);
	}

	@Override
	public boolean isPlayer() {
		return ((BukkitCommandSender) getSender()).getSender() instanceof Player;
	}

	public Player getPlayer() {
		if (!isPlayer())
			return null;
		return (Player) ((BukkitCommandSender) getSender()).getSender();
	}

	public BukkitAccount getBukkitAccount() {
		if (!isPlayer())
			return null;
		return (BukkitAccount) Common.getAccountManager().getAccount(getPlayer().getUniqueId());
	}

	public int broadcast(String message, Group group) {
		int x = 0;

		for (Account battlePlayer : Common.getAccountManager().getAccounts()) {

			Player player = Bukkit.getPlayer(battlePlayer.getUuid());

			if (player == null)
				continue;
			if (!battlePlayer.hasGroupPermission(group))
				continue;
			
			player.sendMessage(message);
			x++;
		}

		return x;
	}

}
