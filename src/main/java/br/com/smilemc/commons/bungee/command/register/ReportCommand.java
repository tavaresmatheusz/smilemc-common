package br.com.smilemc.commons.bungee.command.register;

import br.com.smilemc.commons.bungee.BungeeCommons;
import br.com.smilemc.commons.bungee.command.BungeeCommandArgs;
import br.com.smilemc.commons.common.Common;
import br.com.smilemc.commons.common.account.permissions.Group;
import br.com.smilemc.commons.common.command.CommandClass;
import br.com.smilemc.commons.common.command.CommandSender;
import br.com.smilemc.commons.common.command.CommandFramework.Command;
import br.com.smilemc.commons.common.report.Report;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class ReportCommand implements CommandClass {

	@Command(name = "report")
	public void report(BungeeCommandArgs bungeeCommandArgs) {
		if (!bungeeCommandArgs.isPlayer())
			return;

		CommandSender sender = bungeeCommandArgs.getSender();
		String[] args = bungeeCommandArgs.getArgs();

		if (args.length < 2) {
			sender.sendMessage("§aVocê deve utilizar §e/report (alvo) (motivo)");
			return;
		}

		ProxiedPlayer proxiedPlayer = BungeeCord.getInstance().getPlayer(args[0]);
		if (proxiedPlayer == null) {
			sender.sendMessage("§cAlvo inexistente ou off-line!");
			return;
		}

		StringBuilder sb = new StringBuilder();
		for (int i = 1; i < args.length; ++i) {
			sb.append(args[i]).append(" ");
		}
		String reason = sb.toString();

		Common.getReportManager()
				.newReport(new Report(proxiedPlayer.getName(), bungeeCommandArgs.getPlayer().getName(), reason, null));
		Common.getAccountManager().getAccounts().stream().filter(accs -> accs.hasGroupPermission(Group.TRIAL))
				.forEach(staffs -> {
					ProxiedPlayer proxiedStaffer = BungeeCommons.getInstance().getProxy().getPlayer(staffs.getName());

					proxiedStaffer.sendMessage(
							TextComponent.fromLegacyText("§aUm novo report foi registrado, §eutilize /reports§a!"));
				});
		sender.sendMessage("§aVocê reportou o jogador §e" + proxiedPlayer.getName() + "§a com sucesso!");
	}

}
