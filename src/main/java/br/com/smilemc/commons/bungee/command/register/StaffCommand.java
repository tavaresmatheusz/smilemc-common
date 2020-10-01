package br.com.smilemc.commons.bungee.command.register;

import br.com.smilemc.commons.bungee.account.BungeeAccount;
import br.com.smilemc.commons.bungee.command.BungeeCommandArgs;
import br.com.smilemc.commons.common.Common;
import br.com.smilemc.commons.common.account.permissions.Group;
import br.com.smilemc.commons.common.account.scoreboard.Tag;
import br.com.smilemc.commons.common.command.CommandClass;
import br.com.smilemc.commons.common.command.CommandSender;
import br.com.smilemc.commons.common.command.CommandFramework.Command;

public class StaffCommand implements CommandClass {

	@Command(name = "staffchat", aliases = { "sc" }, groupToUse = Group.YOUTUBERPLUS)
	public void staffchat(BungeeCommandArgs bungeeCommandArgs) {

		CommandSender sender = bungeeCommandArgs.getSender();

		if (!bungeeCommandArgs.isPlayer()) {
			sender.sendMessage("Comando disponível apenas in-game!");
			return;
		}

		BungeeAccount bungeePlayer = (BungeeAccount) Common.getAccountManager()
				.getAccount(bungeeCommandArgs.getPlayer().getUniqueId());

		bungeePlayer.setInStaffChat(!bungeePlayer.isInStaffChat());
		sender.sendMessage("§aAgora você está com o staff-chat "
				+ (bungeePlayer.isInStaffChat() ? "§2ativado" : "§cdesativado") + "§a!");

	}

	@Command(name = "stafflist", groupToUse = Group.TRIAL)
	public void stafflist(BungeeCommandArgs bungeeCommandArgs) {

		CommandSender sender = bungeeCommandArgs.getSender();
		sender.sendMessage("§eLista de staffers §aonlines§e: ");
		Common.getAccountManager().getAccounts().stream()
				.filter(accs -> accs.hasGroupPermission(Group.YOUTUBERPLUS)).forEach(staffs -> {

					sender.sendMessage(
							Tag.getTagByGroup(staffs.getGroup()).getPrefix() + ((BungeeAccount) staffs).getName()
									+ "§f - " + ((BungeeAccount) staffs).getServer().getName().toUpperCase());

				});

	}

}
