package br.com.smilemc.commons.bukkit.events.tag;

import org.bukkit.entity.Player;

import br.com.smilemc.commons.bukkit.account.BukkitAccount;
import br.com.smilemc.commons.bukkit.api.player.TagAPI;
import br.com.smilemc.commons.bukkit.events.PlayerCancellableEvent;
import br.com.smilemc.commons.common.Common;
import br.com.smilemc.commons.common.account.scoreboard.Tag;
import br.com.smilemc.commons.common.account.status.League;
import lombok.Getter;

public class PlayerChangeTag extends PlayerCancellableEvent {

	@Getter
	private Tag tag;

	public PlayerChangeTag(Player player, Tag tag) {
		super(player);
		BukkitAccount bukkitAccount = (BukkitAccount) Common.getAccountManager().getAccount(player.getUniqueId());
		League league = bukkitAccount.getLeague() == null ? League.UNRANKED : bukkitAccount.getLeague();
		bukkitAccount.setTag(tag);
		String suffix = (" ") + "§7[" + league.getColor() + league.getSymbol() + "§7]"
				+ (bukkitAccount.getMedal() == null ? ""
						: " " + bukkitAccount.getMedal().getColor() + bukkitAccount.getMedal().getSymbol());

		TagAPI.setNickName(player, tag.getPrefix(), suffix, tag.getAZ());
	}

}
