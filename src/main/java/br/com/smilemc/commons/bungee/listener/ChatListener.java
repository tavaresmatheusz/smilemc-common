package br.com.smilemc.commons.bungee.listener;

import br.com.smilemc.commons.bungee.BungeeCommons;
import br.com.smilemc.commons.bungee.account.BungeeAccount;
import br.com.smilemc.commons.common.Common;
import br.com.smilemc.commons.common.account.permissions.Group;
import br.com.smilemc.commons.common.account.scoreboard.Tag;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class ChatListener implements Listener {

	@EventHandler
	public void onChatEvent(ChatEvent event) {
		ProxiedPlayer proxiedPlayer = (ProxiedPlayer) event.getSender();
		if (event.getMessage().startsWith("/"))
			return;
		BungeeAccount bungeeAccount = (BungeeAccount) Common.getAccountManager()
				.getAccount(proxiedPlayer.getUniqueId());
		if (bungeeAccount.isInStaffChat()) {
			event.setCancelled(true);
			Common.getAccountManager().getAccounts().stream()
					.filter(filtring -> filtring.hasGroupPermission(Group.YOUTUBERPLUS)).forEach(staffs -> {

						if (staffs.getAccountPreferences().isReceivingStaffChatMessages()) {
							ProxiedPlayer proxiedStaffer = BungeeCommons.getInstance().getProxy()
									.getPlayer(staffs.getName());
							proxiedStaffer.sendMessage(TextComponent
									.fromLegacyText("§e§l[SC] §f" + Tag.getTagByGroup(staffs.getGroup()).getPrefix()
											+ staffs.getName() + "§f: " + event.getMessage()));
						}
					});
		}
	}

}
