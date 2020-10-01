package br.com.smilemc.commons.bukkit.command.register;

import java.util.Calendar;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import br.com.smilemc.commons.bukkit.account.BukkitAccount;
import br.com.smilemc.commons.bukkit.command.BukkitCommandArgs;
import br.com.smilemc.commons.common.Common;
import br.com.smilemc.commons.common.account.Account;
import br.com.smilemc.commons.common.account.permissions.Group;
import br.com.smilemc.commons.common.backend.sql.SqlManager.ObjectsTypes;
import br.com.smilemc.commons.common.command.CommandClass;
import br.com.smilemc.commons.common.command.CommandSender;
import br.com.smilemc.commons.common.command.CommandFramework.Command;
import br.com.smilemc.commons.common.util.string.StringDateUtils;
import redis.clients.jedis.Jedis;

public class AccountCommand implements CommandClass {

	@Command(name = "account", aliases = { "acc" }, groupToUse = Group.MODPLUS, runAsync = true)
	public void account(BukkitCommandArgs bukkitCommandArgs) {

		String[] args = bukkitCommandArgs.getArgs();
		CommandSender sender = bukkitCommandArgs.getSender();
		BukkitAccount bukkitAccount = bukkitCommandArgs.getBukkitAccount();

		if (args.length < 2) {
			sender.sendMessage(
					"§aVocê deve utilizar §e/" + bukkitCommandArgs.getLabel() + " (alvo) (permissions|group|info)");
			return;
		}

		String name = args[0];
		Player targetPlayer = Bukkit.getPlayer(name);
		if (Common.validateName(name) == false && Common.getUuid(name) == null) {
			sender.sendMessage("§cNickname invalido ou inexistente!");
			return;
		}

		Account account = targetPlayer == null ? new BukkitAccount(name)
				: Common.getAccountManager().getAccount(targetPlayer.getUniqueId());

		if (bukkitAccount != null)
			if (!bukkitAccount.hasGroupPermission(account.getGroup())) {
				sender.sendMessage("§cVocê não pode manejar a conta deste jogador!");
				return;
			}

		if (args[1].equalsIgnoreCase("group")) {

			if (args.length < 4) {
				sender.sendMessage("§aVocê deve utilizar §e/" + bukkitCommandArgs.getLabel()
						+ " (alvo) group (set) (group) (tempo)");
				return;

			}

			if (args[2].equalsIgnoreCase("set")) {

				if (bukkitCommandArgs.isPlayer()) {

					Jedis jedis = Common.getBackendManager().getRedisManager().getJedisPool().getResource();

					if (jedis.exists(Common.GROUP_UPDATE)) {
						sender.sendMessage("§cAlgo não ocorreu bem, tente novamente!");
						return;
					}

					if (!Group.existGroup(args[3])) {
						sender.sendMessage("§cO grupo §e'" + args[3] + "'§c não existe!");
						return;
					}
					long time = 0l;
					if (args[4] == null) {
						time = 0l;
					} else {
						Calendar calendar = Calendar.getInstance();
						if (!(args[4].contains("d") || args[4].contains("m") || args[4].contains("y"))) {
							int add;
							try {
								add = Integer.valueOf(args[4].replace("d", "").replace("m", "").replace("y", ""));
							} catch (Exception e) {
								return;
							}
							String Case = args[4].replace(add + "", "");
							switch (Case.toLowerCase()) {
							case "d":
								calendar.add(Calendar.DAY_OF_YEAR, add);
								break;
							case "m":
								calendar.add(Calendar.MONTH, add);
								break;
							case "y":
								calendar.add(Calendar.YEAR, add);
								break;
							default:
								sender.sendMessage("§cVocê deve digitar um formato de data corretamente.");
								return;
							}
							time = calendar.getTimeInMillis();
						}
					}
					Group group = Group.getGroupByName(args[3]);

					account.setGroup(group);
					account.setGroupExpireIn(time);

					Common.getBackendManager().getSqlManager().update(ObjectsTypes.STRING, "account_principal_infos",
							Common.getUuid(name), "group", group.toString());
					Common.getBackendManager().getSqlManager().update(ObjectsTypes.LONG, "account_principal_infos",
							Common.getUuid(name), "groupExpireIn", time);

					jedis.set(Common.GROUP_UPDATE, account.getName());
					jedis.expire(Common.GROUP_UPDATE, 1);

					jedis.close();

					sender.sendMessage(
							"§aVocê alterou o grupo de §e" + account.getName() + " §apara §e" + account.getGroup()
									+ " §aaté §e" + StringDateUtils.formatDifference(time) + " §acom sucesso!");
				} else {

					Jedis jedis = Common.getBackendManager().getRedisManager().getJedisPool().getResource();

					if (jedis.exists(Common.GROUP_UPDATE)) {
						sender.sendMessage("§cAlgo não ocorreu bem, tente novamente!");
					}

					if (!Group.existGroup(args[3])) {
						sender.sendMessage("§cO grupo §e'" + args[3] + "'§c não existe!");
						return;
					}
					long time = 0l;
					if (args[4] == null) {
						time = 0l;
					} else {
						Calendar calendar = Calendar.getInstance();
						if (!(args[4].contains("d") || args[4].contains("m") || args[4].contains("y"))) {
							int add;
							try {
								add = Integer.valueOf(args[4].replace("d", "").replace("m", "").replace("y", ""));
							} catch (Exception e) {
								return;
							}
							String Case = args[4].replace(add + "", "");
							switch (Case.toLowerCase()) {
							case "d":
								calendar.add(Calendar.DAY_OF_YEAR, add);
								break;
							case "m":
								calendar.add(Calendar.MONTH, add);
								break;
							case "y":
								calendar.add(Calendar.YEAR, add);
								break;
							default:
								sender.sendMessage("§cVocê deve digitar um formato de data corretamente.");
								return;
							}
							time = calendar.getTimeInMillis();
						}
					}
					Group group = Group.getGroupByName(args[3]);

					account.setGroup(group);
					account.setGroupExpireIn(time);

					Common.getBackendManager().getSqlManager().update(ObjectsTypes.STRING, "account_principal_infos",
							Common.getUuid(name), "group", group.toString());
					Common.getBackendManager().getSqlManager().update(ObjectsTypes.LONG, "account_principal_infos",
							Common.getUuid(name), "groupExpireIn", time);

					jedis.set(Common.GROUP_UPDATE, account.getName());
					jedis.expire(Common.GROUP_UPDATE, 1);

					jedis.close();

					sender.sendMessage(
							"§aVocê alterou o grupo de §e" + account.getName() + " §apara §e" + account.getGroup()
									+ " §aaté §e" + StringDateUtils.formatDifference(time) + " §acom sucesso!");	

				}

			}

		}

	}

}
