package br.com.smilemc.commons.bungee.command.register;

import br.com.smilemc.commons.bungee.BungeeCommons;
import br.com.smilemc.commons.bungee.account.BungeeAccount;
import br.com.smilemc.commons.bungee.command.BungeeCommandArgs;
import br.com.smilemc.commons.common.Common;
import br.com.smilemc.commons.common.account.permissions.Group;
import br.com.smilemc.commons.common.command.CommandClass;
import br.com.smilemc.commons.common.command.CommandSender;
import br.com.smilemc.commons.common.command.CommandFramework.Command;
import br.com.smilemc.commons.common.server.Server;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ClickEvent.Action;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class BungeeCommand implements CommandClass {

	@Command(name = "glist", groupToUse = Group.MODPLUS)
	public void glist(BungeeCommandArgs bungeeCommandArgs) {
		CommandSender sender = bungeeCommandArgs.getSender();
		Server beastServer = null;
		sender.sendMessage("§aLista de servidores:");
		sender.sendMessage("");
		for (Server server : BungeeCommons.getInstance().getServerManager().getServers()) {
			sender.sendMessage("§2§l* §f" + server.getName() + " - " + server.getOnline() + "/" + server.getMaxPlayers());
			if (beastServer == null)
				beastServer = server;
			if (server.getOnline() > beastServer.getOnline())
				beastServer = server;
		}

		sender.sendMessage("");
		sender.sendMessage(
				"§fJogadores online no total: §b" + BungeeCommons.getInstance().getServerManager().getOnlinePlayers());
		sender.sendMessage("§fServidor com mais jogadores: §e" + beastServer.getName() + " - " + beastServer.getOnline()
				+ "/" + beastServer.getMaxPlayers());
	}

	@Command(name = "send", groupToUse = Group.MOD)
	public void send(BungeeCommandArgs bungeeCommandArgs) {
		String[] args = bungeeCommandArgs.getArgs();
		if (bungeeCommandArgs.isPlayer()) {
			CommandSender sender = bungeeCommandArgs.getSender();
			if (args.length < 1) {
				sender.sendMessage("§aVocê deve utilizar §e/send (alvo|current) (servidor)");
				return;
			}

			if (args[0].equalsIgnoreCase("current")) {
				BungeeAccount bungeePlayer = (BungeeAccount) Common.getAccountManager()
						.getAccount(bungeeCommandArgs.getPlayer().getUniqueId());
				if (bungeePlayer.getGroup().ordinal() < Group.MODPLUS.ordinal()) {
					sender.sendMessage("§cVocê não possui permissão para executar este comando!");
					return;
				}
				Server server = BungeeCommons.getInstance().getServerManager().getServer(args[1]);
				if (server == null) {
					sender.sendMessage("§cServidor offline ou inexistente!");
					return;
				}
				long inServer = BungeeCord.getInstance().getPlayers().stream()
						.filter(ps -> ps.getServer() == bungeeCommandArgs.getPlayer().getServer()).count();
				BungeeCord.getInstance().getPlayers().stream()
						.filter(ps -> ps.getServer() == bungeeCommandArgs.getPlayer().getServer()).forEach(players -> {
							players.connect(BungeeCord.getInstance().getServerInfo(server.getName()));
							players.sendMessage(TextComponent.fromLegacyText(
									"§aVocê está sendo §2conectado§a ao servidor §e" + server.getName() + "§f!"));

						});

				sender.sendMessage("§aVocê enviou o total de §2" + inServer + " "
						+ (inServer > 1 ? "jogadores" : "jogador") + " §a ao servidor §2" + server.getName() + "§a!");

				return;
			}

			ProxiedPlayer proxiedTarget = BungeeCord.getInstance().getPlayer(args[0]);

			if (proxiedTarget == null) {
				sender.sendMessage("§cAlvo inexistente ou off-line!");
				return;
			}

			BungeeAccount bungeeTargetPlayer = (BungeeAccount) Common.getAccountManager()
					.getAccount(proxiedTarget.getUniqueId());
			Server server = BungeeCommons.getInstance().getServerManager().getServer(args[1]);

			if (server == null) {
				sender.sendMessage("§cServidor offline ou inexistente!");
				return;
			}

			if (bungeeTargetPlayer.getServer() == server) {
				sender.sendMessage("§cEste jogador já está neste servidor!");
				return;
			}

			proxiedTarget.sendMessage(TextComponent.fromLegacyText("§aVocê está sendo §2conectado§a ao servidor §e" + server.getName() + "§f!"));
			proxiedTarget.connect(BungeeCord.getInstance().getServerInfo(server.getName()));
			sender.sendMessage("§aVocê enviou o jogador §2" + proxiedTarget.getName() + "§a ao servidor §2"
					+ server.getName() + "§a!");
		}
	}

	@Command(name = "find", groupToUse = Group.MOD)
	public void find(BungeeCommandArgs bungeeCommandArgs) {
		CommandSender sender = bungeeCommandArgs.getSender();
		String[] args = bungeeCommandArgs.getArgs();
		if (args.length == 0) {
			sender.sendMessage("§aVocê deve utilizar §e/find (alvo)");
			return;
		}

		ProxiedPlayer proxiedTarget = BungeeCord.getInstance().getPlayer(args[0]);

		if (proxiedTarget == null) {
			sender.sendMessage("§cAlvo inexistente ou off-line!");
			return;
		}

		String server = proxiedTarget.getServer().getInfo().getName();

		sender.sendMessage("§fO jogador §a" + proxiedTarget.getName() + " §ffoi localizado no servidor §2"
				+ server.toUpperCase() + "§a!");

		if (bungeeCommandArgs.isPlayer()) {

			TextComponent textComponent = new TextComponent();
			textComponent.setClickEvent(new ClickEvent(Action.RUN_COMMAND, "/connect " + server));
			textComponent
					.setText("§b§nClique aqui para se conectar ao servidor §2§l§n" + server.toUpperCase() + "§a§n!");

			sender.sendMessage(textComponent);

		}

	}

	@Command(name = "connect", aliases = { "server", "go" })
	public void connect(BungeeCommandArgs bungeeCommandArgs) {
		CommandSender sender = bungeeCommandArgs.getSender();
		String[] args = bungeeCommandArgs.getArgs();
		
		if (!bungeeCommandArgs.isPlayer()) {
			sender.sendMessage("Comando disponível apenas in-game!");
			return;
		}
		
		if (args.length == 0) {
			sender.sendMessage("§eVocê deve utilizar §a/connect (servidor)");
			return;
		}

		Server server = BungeeCommons.getInstance().getServerManager().getServer(args[0]);
		if (server == null) {
			sender.sendMessage("§cServidor offline ou inexistente!");
			return;
		}
		if (server.getName().equalsIgnoreCase("limbo") || server.getName().equalsIgnoreCase("login")) {
			sender.sendMessage("§cServidor inacessível!");
			return;
		}
		
		bungeeCommandArgs.getPlayer().connect(BungeeCord.getInstance().getServerInfo(server.getName()));
		sender.sendMessage("§aVocê está sendo §2conectado§a ao servidor §e" + server.getName() + "§f!");

	}

}