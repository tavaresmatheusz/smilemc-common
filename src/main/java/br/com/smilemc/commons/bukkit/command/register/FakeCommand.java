package br.com.smilemc.commons.bukkit.command.register;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import br.com.smilemc.commons.bukkit.BukkitCommons;
import br.com.smilemc.commons.bukkit.account.BukkitAccount;
import br.com.smilemc.commons.bukkit.api.player.FakePlayerAPI;
import br.com.smilemc.commons.bukkit.api.skin.Skin;
import br.com.smilemc.commons.bukkit.command.BukkitCommandArgs;
import br.com.smilemc.commons.bukkit.events.tag.PlayerChangeTag;
import br.com.smilemc.commons.common.Common;
import br.com.smilemc.commons.common.account.permissions.Group;
import br.com.smilemc.commons.common.account.scoreboard.Tag;
import br.com.smilemc.commons.common.backend.sql.SqlManager.ObjectsTypes;
import br.com.smilemc.commons.common.command.CommandClass;
import br.com.smilemc.commons.common.command.CommandSender;
import br.com.smilemc.commons.common.command.CommandFramework.Command;

public class FakeCommand implements CommandClass {

	private final String[] fakeList = { "RabidMongolian", "RabidMongolian", "Flizerh", "Ghosting", "yGytootic",
			"zLoouerSz_", "zLorddd", "Lordhot", "HoemBack", "zEbolaPvP_", "Louizz12_", "GhostzaOk1k", "plin_G0D_",
			"deNlson" };

	@Command(name = "fake", groupToUse = Group.YOUTUBER, aliases = { "nick" }, runAsync = true)
	public void fake(BukkitCommandArgs cmdArgs) {
		CommandSender sender = cmdArgs.getSender();
		String[] args = cmdArgs.getArgs();

		if (!cmdArgs.isPlayer()) {
			sender.sendMessage("Comando disponível apenas in-game!");
			return;
		}

		if (args.length == 0) {
			sender.sendMessage("§aVocê deve utilizar §e/fake <name>");
			return;
		}

		BukkitAccount bukkitAccount = cmdArgs.getBukkitAccount();
		Player player = cmdArgs.getPlayer();
		if (Common.getCooldownManager().getCooldown("fake:" + player.getUniqueId().toString()) != null) {
			if (!Common.getCooldownManager().getCooldown("fake:" + player.getUniqueId().toString()).expired()) {
				player.sendMessage("§cAguarde §e"
						+ Common.getCooldownManager().getCooldown("fake:" + player.getUniqueId().toString()).expireIn()
						+ " §cpara utilizar o comando novamente!");
				return;
			}
		}

		if (args[0].equalsIgnoreCase("random")) {
			player.chat("/fake " + fakeList[new Random().nextInt(fakeList.length)]);
			return;
		}

		if (args[0].equalsIgnoreCase("list")) {
			if (bukkitAccount.hasGroupPermission(Group.TRIAL)) {
				player.sendMessage("§aLista de jogadores utilizando fake: \n§m--------------------");
				Common.getAccountManager().getAccounts().stream().filter(accs -> ((BukkitAccount) accs).isUsingFake())
						.forEach(accs -> {
							player.sendMessage(accs.getName() + " - §c" + ((BukkitAccount) accs).getFakeName());
						});

			}
			return;
		}

		if (args[0].equalsIgnoreCase("#") || args[0].equalsIgnoreCase("reset")) {

			if (!bukkitAccount.isUsingFake()) {
				sender.sendMessage("§cVocê não está utilizando um fake!");
				return;
			}
			bukkitAccount.setFakeName("");
			bukkitAccount.setUsingFake(false);
			new BukkitRunnable() {

				@Override
				public void run() {
					FakePlayerAPI.changePlayerName(player, bukkitAccount.getName(), true);
					FakePlayerAPI.changePlayerSkin(player, bukkitAccount.getDefaultSkin());
					Bukkit.getPluginManager()
							.callEvent(new PlayerChangeTag(player, Tag.MEMBRO));
				}

			}.runTask(BukkitCommons.getInstance());
			player.sendMessage("§aAgora seu nick voltou ao normal!");
			return;
		}

		String fakeName = args[0];

		if (Common.validateName(fakeName) == false || Common.getUuid(fakeName) != null) {
			sender.sendMessage("§cO nickname inserido é inválido!");
			return;
		}

		bukkitAccount.setFakeName(fakeName);
		bukkitAccount.setUsingFake(true);
		new BukkitRunnable() {

			@Override
			public void run() {
				FakePlayerAPI.changePlayerName(player, fakeName, false);
				FakePlayerAPI.removePlayerSkin(player);
			}

		}.runTask(BukkitCommons.getInstance());
		player.sendMessage(
				"§aAgora seu nickname é §e" + fakeName + "§a! Para voltar ele ao normal utilize §e/fake reset");
		Common.getCooldownManager().newCooldown("fake:" + player.getUniqueId().toString(), 60);
	}

	@Command(name = "skin", runAsync = true)
	public void skin(BukkitCommandArgs cmdArgs) {
		CommandSender sender = cmdArgs.getSender();
		String[] args = cmdArgs.getArgs();
		BukkitAccount bukkitAccount = cmdArgs.getBukkitAccount();
		if (!cmdArgs.isPlayer()) {
			sender.sendMessage("Comando disponível apenas in-game!");
			return;
		}

		Player player = cmdArgs.getPlayer();

		if (args.length == 0) {
			sender.sendMessage("§aVocê deve utilizar §e/skin <name>");
			return;
		}

		String skinName = args[0];
		if (Common.validateName(skinName) == false || Common.getUuidFetcher().getUUID(skinName) == null) {
			sender.sendMessage("§cO nickname inserido é inválido!");
			return;
		}

		Skin skin = BukkitCommons.getInstance().getSkinManager().newSkin(skinName);

		if (!bukkitAccount.isUsingFake())
			try {
				if (Common.getUuid(player.getName()) == null) {
					Common.debug("uuid nulo");
				}
				if (Common.getBackendManager().getSqlManager().getString("skins", Common.getUuid(player.getName()),
						"skinName") == null) {
					Common.debug("get string nulo wtf");

				}
				if (Common.getBackendManager().getSqlManager().getString("skins", Common.getUuid(player.getName()),
						"skinName") != null) {
					Common.getBackendManager().getSqlManager().update(ObjectsTypes.STRING, "skins",
							Common.getUuid(player.getName()), "skinName", skinName);
					Common.getBackendManager().getSqlManager().update(ObjectsTypes.STRING, "skins",
							Common.getUuid(player.getName()), "skinUuid",
							Common.getUuidFetcher().getUUID(skinName).toString());
				} else {

					PreparedStatement statement = Common.getBackendManager().getSqlManager()
							.prepareStatement("INSERT INTO `skins`(`uuid`, `skinUuid`, `skinName`) VALUES (?,?,?)");
					statement.setString(1, Common.getUuidFetcher().getUUID(player.getName()).toString());
					statement.setString(2, Common.getUuidFetcher().getUUID(skinName).toString());
					statement.setString(3, skinName);
					statement.executeUpdate();
					statement.close();
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}

		new BukkitRunnable() {
			@Override
			public void run() {
				FakePlayerAPI.changePlayerSkin(player, skin);
			}
		}.runTask(BukkitCommons.getInstance());
		cmdArgs.getBukkitAccount().setDefaultSkin(skin);
		player.sendMessage("§aVocê §eatualizou §asua skin!");

	}

}
