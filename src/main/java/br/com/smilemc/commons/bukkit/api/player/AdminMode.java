package br.com.smilemc.commons.bukkit.api.player;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import br.com.smilemc.commons.bukkit.api.vanish.VanishAPI;
import br.com.smilemc.commons.bukkit.inventory.ItensCache;
import br.com.smilemc.commons.common.account.Account;
import br.com.smilemc.commons.common.account.permissions.Group;
import lombok.Getter;

public class AdminMode {

	private Account account;
	@Getter
	private boolean inMode = false;
	private Player player;
	private br.com.smilemc.commons.bukkit.inventory.cache.AdminMode itens = new br.com.smilemc.commons.bukkit.inventory.cache.AdminMode(
			null, null);

	public AdminMode(Account account) {
		this.account = account;
		this.player = Bukkit.getPlayer(this.account.getName());
	}

	public void changeMode() {

		inMode = !inMode;
		if (inMode) {

			player.setGameMode(GameMode.CREATIVE);
			player.getInventory().clear();

			itens.setItensInInventory(player.getInventory().getContents());
			itens.setArmorContents(player.getInventory().getArmorContents());

			for (int i = 0; i < ItensCache.ADMIN_MODE.getItens().length; i++) {
				player.getInventory().setItem(ItensCache.ADMIN_MODE.getSlots()[i], ItensCache.ADMIN_MODE.getItens()[i]);
			}

			VanishAPI.vanishPlayerForGroup(player, Group.getGroupByOrdinal(account.getGroup().ordinal() - 1));

		} else {

			player.setGameMode(GameMode.SURVIVAL);
			player.getInventory().clear();

			if (itens.getItensInInventory() != null)
				player.getInventory().setContents(itens.getItensInInventory());
			if (itens.getArmorContents() != null)
				player.getInventory().setArmorContents(itens.getArmorContents());

			VanishAPI.showPlayerForAll(player);
		}


	}

}
