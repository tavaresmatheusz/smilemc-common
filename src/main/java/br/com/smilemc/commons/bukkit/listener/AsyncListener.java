package br.com.smilemc.commons.bukkit.listener;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import br.com.smilemc.commons.bukkit.account.BukkitAccount;
import br.com.smilemc.commons.bukkit.api.chat.ChatAPI;
import br.com.smilemc.commons.bukkit.api.chat.ChatAPI.ChatState;
import br.com.smilemc.commons.common.Common;
import br.com.smilemc.commons.common.account.permissions.Group;
import br.com.smilemc.commons.common.account.status.League;

public class AsyncListener implements Listener {

	@EventHandler
	public void onAsyncPlayerChat(AsyncPlayerChatEvent event) {
		event.setCancelled(true);

		Player player = event.getPlayer();

		BukkitAccount bukkitAccount = (BukkitAccount) Common.getAccountManager().getAccount(player.getUniqueId());

		if (ChatAPI.getInstance().getChatState() == ChatState.ENABLED) {

			League league = bukkitAccount.getLeague() == null ? League.UNRANKED : bukkitAccount.getLeague();
			String suffix = "§7[" + league.getColor() + league.getSymbol() + "§7]"
					+ (bukkitAccount.getMedal() == null ? ""
							: " " + bukkitAccount.getMedal().getColor() + bukkitAccount.getMedal().getSymbol());
			Bukkit.broadcastMessage(suffix + " " + bukkitAccount.getTag().getPrefix() + player.getName() + " §a§l> §f"
					+ (bukkitAccount.hasGroupPermission(Group.PRO) ? event.getMessage().replace("&", "§")
							: event.getMessage()));
		} else {
			player.sendMessage("§cO chat neste momente está desativado!");
		}
	}

}
