package br.com.smilemc.commons.bukkit.api.vanish;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import br.com.smilemc.commons.bukkit.account.BukkitAccount;
import br.com.smilemc.commons.common.Common;
import br.com.smilemc.commons.common.account.permissions.Group;

public class VanishAPI {

	public static void vanishPlayerForGroup(Player toHide, Group group) {
		Common.getAccountManager().getAccounts().forEach(acc -> {
			if (!acc.hasGroupPermission(group))
				Bukkit.getPlayer(acc.getName()).hidePlayer(toHide);
		});
	}

	public static void vanishPlayerForAll(Player toHide) {
		Bukkit.getOnlinePlayers().forEach(players -> players.hidePlayer(toHide));
	}

	public static void showPlayerForAll(Player toShow) {

		Bukkit.getOnlinePlayers().forEach(players -> players.showPlayer(toShow));

	}

	public static void hideAllForPlayer(Player toHideAll) {

		Bukkit.getOnlinePlayers().forEach(players -> toHideAll.showPlayer(players));

	}

	public static void showAllForPlayer(Player toShowAll) {
		Common.getAccountManager().getAccounts().forEach(acc -> {

			Player player = Bukkit.getPlayer(acc.getUuid());
			BukkitAccount myAccount = (BukkitAccount) Common.getAccountManager()
					.getAccount(toShowAll.getUniqueId());
			BukkitAccount bukkitAccount = (BukkitAccount) acc;

			if (bukkitAccount.getAdminMode().isInMode()) {
				if (myAccount.hasGroupPermission(bukkitAccount.getGroup()))
					player.showPlayer(toShowAll);
			} else
				player.showPlayer(toShowAll);

		});

	}
}
