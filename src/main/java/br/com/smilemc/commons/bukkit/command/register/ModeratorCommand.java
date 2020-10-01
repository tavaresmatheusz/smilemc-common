package br.com.smilemc.commons.bukkit.command.register;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import br.com.smilemc.commons.bukkit.BukkitCommons;
import br.com.smilemc.commons.bukkit.api.actionbar.ActionBarAPI;
import br.com.smilemc.commons.bukkit.api.chat.ChatAPI;
import br.com.smilemc.commons.bukkit.api.chat.ChatAPI.ChatState;
import br.com.smilemc.commons.bukkit.command.BukkitCommandArgs;
import br.com.smilemc.commons.bukkit.inventory.gui.ReportGUI;
import br.com.smilemc.commons.common.account.permissions.Group;
import br.com.smilemc.commons.common.command.CommandClass;
import br.com.smilemc.commons.common.command.CommandSender;
import br.com.smilemc.commons.common.command.CommandFramework.Command;

public class ModeratorCommand implements CommandClass {

	@Command(name = "gamemode", aliases = { "gm" }, groupToUse = Group.TRIAL)
	public void gamemode(BukkitCommandArgs bukkitCommandArgs) {
		CommandSender sender = bukkitCommandArgs.getSender();
		String[] args = bukkitCommandArgs.getArgs();
		if (!bukkitCommandArgs.isPlayer()) {
			sender.sendMessage("Comando disponível apenas in-game!");
			return;
		}

		if (args.length == 0) {
			sender.sendMessage("§aVocê deve utilizar §e/gamemode (0|1)");
			return;
		}

		GameMode gameMode = null;
		switch (args[0]) {
		case "1":
			gameMode = GameMode.CREATIVE;
			break;
		case "creative":
			gameMode = GameMode.CREATIVE;
			break;
		case "survival":
			gameMode = GameMode.SURVIVAL;
			break;
		case "0":
			gameMode = GameMode.SURVIVAL;
			break;
		case "spec":
			gameMode = GameMode.SPECTATOR;
			break;
		case "spectator":
			gameMode = GameMode.SPECTATOR;
			break;
		case "3":
			gameMode = GameMode.SPECTATOR;
			break;
		case "2":
			gameMode = GameMode.ADVENTURE;
			break;
		case "adventure":
			gameMode = GameMode.ADVENTURE;
			break;
		}
		if (gameMode == null) {
			sender.sendMessage("§cO modo de jogo §e'" + args[0] + "' §cnão existe!");
			return;
		}
		Player player;
		if (args.length == 1)
			player = bukkitCommandArgs.getPlayer();
		else
			player = Bukkit.getPlayer(args[1]);

		if (player.getGameMode() == gameMode) {
			sender.sendMessage("§cVocê já está nesse gamemode!");
			return;
		}

		player.setGameMode(gameMode);
		player.sendMessage("§aVocê alterou seu modo de jogo para §e" + gameMode.toString() + "§a!");
		bukkitCommandArgs.broadcast(
				"§7§o[" + player.getName() + " alterou seu modo de jogo para " + gameMode.toString() + "]",
				Group.TRIAL);

	}

	@Command(name = "chat", aliases = { "cc" }, groupToUse = Group.TRIAL)
	public void chat(BukkitCommandArgs cmdArgs) {

		String[] args = cmdArgs.getArgs();
		CommandSender sender = cmdArgs.getSender();
		String label = cmdArgs.getLabel();
		if (label.equalsIgnoreCase("cc") || (args[0] != null && args[0].equalsIgnoreCase("clear"))) {
			for (int i = 0; i < 100; i++)
				cmdArgs.broadcast("", Group.MEMBRO);

			cmdArgs.broadcast("§7§o[" + sender.getName() + " limpou o chat]", Group.TRIAL);
			return;
		}
		sender.sendMessage("§eVocê "
				+ (ChatAPI.getInstance().getChatState() == ChatState.ENABLED ? "§cdesabilitou" : "§ahabilitou")
				+ " §eo chat!");
		ChatAPI.getInstance().setChatState(
				ChatAPI.getInstance().getChatState() == ChatState.ENABLED ? ChatState.DISABLED : ChatState.ENABLED);

		cmdArgs.broadcast("§7§o[" + sender.getName() + " "
				+ (ChatAPI.getInstance().getChatState() == ChatState.ENABLED ? "desabilitou" : "habilitou")
				+ " o chat]", Group.TRIAL);

	}

	@Command(name = "admin", aliases = { "adm", "v", "vanish" }, groupToUse = Group.TRIAL)
	public void admin(BukkitCommandArgs cmdArgs) {
		CommandSender sender = cmdArgs.getSender();
		Player player = cmdArgs.getPlayer();
		if (!cmdArgs.isPlayer()) {
			sender.sendMessage("Comando disponível apenas in-game!");
			return;
		}
		cmdArgs.getBukkitAccount().getAdminMode().changeMode();

		if (cmdArgs.getBukkitAccount().getAdminMode().isInMode()) {
			player.sendMessage("§dVocê entrou no modo ADMIN");
			player.sendMessage("§dVocê está invisível para jogadores abaixo do cargo " + Group
					.getGroupByOrdinal(cmdArgs.getBukkitAccount().getGroup().ordinal() - 1).getName().toUpperCase()
					+ "!");

			new BukkitRunnable() {

				@Override
				public void run() {
					if (cmdArgs.getBukkitAccount() == null || cmdArgs.getPlayer() == null
							|| cmdArgs.getBukkitAccount().getAdminMode().isInMode() == false) {
						cancel();
						return;
					}
					ActionBarAPI.send(player, "§cVocê está no modo ADMIN");
				}
			}.runTaskTimer(BukkitCommons.getInstance(), 0, 20l);

		} else {
			player.sendMessage("§dVocê saiu do modo ADMIN");
			player.sendMessage("§dVocê está visível para todos jogadores!");
		}
		cmdArgs.broadcast("§7§o[" + player.getName() + " "
				+ (cmdArgs.getBukkitAccount().getAdminMode().isInMode() ? "entrou no" : "saiu do") + " modo admin]",
				Group.TRIAL);
	}

	@Command(name = "reports", groupToUse = Group.TRIAL)
	public void reports(BukkitCommandArgs bukkitCommandArgs) {
		CommandSender sender = bukkitCommandArgs.getSender();
		Player player = bukkitCommandArgs.getPlayer();
		if (!bukkitCommandArgs.isPlayer()) {
			sender.sendMessage("Comando disponível apenas in-game!");
			return;
		}

		player.openInventory(new ReportGUI(1).getInventory());
	}

}
