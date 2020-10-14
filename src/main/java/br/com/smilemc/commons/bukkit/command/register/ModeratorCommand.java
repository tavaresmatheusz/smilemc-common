package br.com.smilemc.commons.bukkit.command.register;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import br.com.smilemc.commons.bukkit.BukkitCommons;
import br.com.smilemc.commons.bukkit.account.BukkitAccount;
import br.com.smilemc.commons.bukkit.api.actionbar.ActionBarAPI;
import br.com.smilemc.commons.bukkit.api.chat.ChatAPI;
import br.com.smilemc.commons.bukkit.api.chat.ChatAPI.ChatState;
import br.com.smilemc.commons.bukkit.command.BukkitCommandArgs;
import br.com.smilemc.commons.bukkit.inventory.gui.ReportGUI;
import br.com.smilemc.commons.common.account.permissions.Group;
import br.com.smilemc.commons.common.command.CommandClass;
import br.com.smilemc.commons.common.command.CommandFramework.Command;
import br.com.smilemc.commons.common.command.CommandSender;

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
		sender.sendMessage("§aVocê alterou seu modo de jogo para §e" + gameMode.toString() + "§a!");
		bukkitCommandArgs.broadcast(
				"§7§o[" + player.getName() + " alterou seu modo de jogo para " + gameMode.toString() + "]",
				Group.TRIAL);

	}

	@Command(name = "build", aliases = { "b" }, groupToUse = Group.MOD)
	public void build(BukkitCommandArgs cmdArgs) {

		CommandSender sender = cmdArgs.getSender();
		cmdArgs.getBukkitAccount().setBuildingMode(!cmdArgs.getBukkitAccount().isBuildingMode());
		sender.sendMessage("§eVocê " + (!cmdArgs.getBukkitAccount().isBuildingMode() ? "§cdesabilitou" : "§ahabilitou")
				+ " §eo build!");
	}

	@Command(name = "teleport")
	public void teleport(BukkitCommandArgs cmdArgs) {
		if (!cmdArgs.isPlayer()) {
			cmdArgs.getSender().sendMessage("Comando disponível apenas in-game!");
			return;
		}
		CommandSender sender = cmdArgs.getSender();
		String[] args = cmdArgs.getArgs();

		if (args.length == 0) {
			sender.sendMessage(" §e* §fVocê deve usar: §e/tp <alvo>");
			return;
		}

		if (args.length == 1) {
			Player targetPlayer = Bukkit.getPlayer(args[0]);
			if (targetPlayer == null) {
				sender.sendMessage("§cEsse jogador não está on-line!");
				return;
			}

			((Player) cmdArgs.getSender()).teleport(targetPlayer);
			sender.sendMessage("§aVocê foi teleportado até o  jogador §e" + targetPlayer.getName() + "§a!");

			return;
		}

		if (args.length == 2) {
			Player targetPlayer1 = Bukkit.getPlayer(args[0]);
			Player targetPlayer2 = Bukkit.getPlayer(args[1]);
			if (targetPlayer1 == null || targetPlayer2 == null) {
				sender.sendMessage("§cUm jogador especificado não está on-line!");
				return;
			}

			targetPlayer1.teleport(targetPlayer2);
			sender.sendMessage("§aVocê teleportou o jogador §e" + targetPlayer1.getName() + " §aaté o  jogador §e"
					+ targetPlayer2.getName() + "§a!");

			return;
		}

		if (args.length == 3) {

			Location location;

			try {
				location = new Location(((Player) cmdArgs.getSender()).getWorld(), Integer.valueOf(args[0]),
						Integer.valueOf(args[1]), Integer.valueOf(args[2]));
			} catch (Exception e) {
				sender.sendMessage("§cA localização especificada está incorreta!");
				return;
			}

			((Player) cmdArgs.getSender()).teleport(location);
			sender.sendMessage("§aVocê foi teleportado até §eX:" + location.getX() + ", Y:" + location.getY() + ", Z: "
					+ location.getZ());
			return;
		}
	}

	@Command(name = "cc", groupToUse = Group.TRIAL)
	public void ccCommand(BukkitCommandArgs cmdArgs) {
		
		for (int i = 0; i < 100; i++)
			cmdArgs.broadcast("", Group.MEMBRO);


	}

	@Command(name = "chat", groupToUse = Group.TRIAL)
	public void chat(BukkitCommandArgs cmdArgs) {

		CommandSender sender = cmdArgs.getSender();

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
			sender.sendMessage("§dVocê entrou no modo ADMIN");
			sender.sendMessage("§dVocê está invisível para jogadores abaixo do cargo " + Group
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
			sender.sendMessage("§dVocê saiu do modo ADMIN");
			sender.sendMessage("§dVocê está visível para todos jogadores!");
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
