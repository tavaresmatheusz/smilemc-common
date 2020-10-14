package br.com.smilemc.commons.bukkit.listener;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.inventory.ItemStack;

import br.com.smilemc.commons.bukkit.account.BukkitAccount;
import br.com.smilemc.commons.bukkit.api.player.AdminMode;
import br.com.smilemc.commons.bukkit.api.player.FakePlayerAPI;
import br.com.smilemc.commons.bukkit.api.player.TagAPI;
import br.com.smilemc.commons.bukkit.events.tag.PlayerChangeTag;
import br.com.smilemc.commons.bukkit.inventory.gui.ReportGUI;
import br.com.smilemc.commons.common.Common;
import br.com.smilemc.commons.common.account.scoreboard.Tag;
import br.com.smilemc.commons.common.account.status.League;

public class PlayerListener implements Listener {

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {

		event.setJoinMessage(null);

		Player player = event.getPlayer();

		Common.getAccountManager().registerAccount(player.getUniqueId(), new BukkitAccount(player.getName()));

		BukkitAccount bukkitAccount = (BukkitAccount) Common.getAccountManager().getAccount(player.getUniqueId());
		bukkitAccount.permissionSetup(player);

		player.teleport(player.getWorld().getSpawnLocation());
		player.chat("/tag " + Tag.getTagByGroup(bukkitAccount.getGroup()));

		Bukkit.getOnlinePlayers().forEach(players -> {
			BukkitAccount bAcc = (BukkitAccount) Common.getAccountManager().getAccount(players.getUniqueId());
			String prefix = bAcc.getTag().getPrefix();
			League league = bAcc.getLeague() == null ? League.UNRANKED : bAcc.getLeague();
			String suffix = (" ") + "§7[" + league.getColor() + league.getSymbol() + "§7]"
					+ (bAcc.getMedal() == null ? "" : " " + bAcc.getMedal().getColor() + bAcc.getMedal().getSymbol());
			TagAPI.setNickName(players, prefix, suffix, bAcc.getTag().getAZ(), player);
		});

		FakePlayerAPI.changePlayerSkin(player, bukkitAccount.getDefaultSkin());

	}

	@EventHandler
	public void onClearWeather(WeatherChangeEvent event) {
		event.getWorld().setWeatherDuration(0);
		event.setCancelled(true);
	}

	@EventHandler
	public void build(BlockBreakEvent event) {
		Player player = event.getPlayer();

		BukkitAccount bukkitAccount = (BukkitAccount) Common.getAccountManager().getAccount(player.getUniqueId());
		if (!bukkitAccount.isBuildingMode())
			event.setCancelled(true);
	}

	@EventHandler
	public void build(BlockPlaceEvent event) {
		Player player = event.getPlayer();

		BukkitAccount bukkitAccount = (BukkitAccount) Common.getAccountManager().getAccount(player.getUniqueId());
		if (!bukkitAccount.isBuildingMode())
			event.setCancelled(true);
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {

		event.setQuitMessage(null);

		Player player = event.getPlayer();

		Common.getAccountManager().unregisterAccount(player.getUniqueId());

	}

	@EventHandler
	public void onClick(PlayerInteractEvent event) {
		Player player = event.getPlayer();

		BukkitAccount bukkitAccount = (BukkitAccount) Common.getAccountManager().getAccount(player.getUniqueId());
		if (bukkitAccount.getAdminMode().isInMode() || player.getItemInHand().getType() == Material.CHEST) 
			player.openInventory(new ReportGUI(1).getInventory());
			
	}

	@EventHandler
	public void onInventoryInteract(InventoryClickEvent event) {
		Player player = (Player) event.getWhoClicked();
		if (event.getInventory().getName().equals("§7Reports")) {
			event.setCancelled(true);
			ItemStack item = event.getCurrentItem();
			ClickType action = event.getClick();

			if (item == null)
				return;
			if (item.getType() == Material.ARROW) {
				player.closeInventory();
				player.openInventory(
						new ReportGUI(Integer.valueOf(item.getItemMeta().getDisplayName().replace("§aPágina ", "")))
								.getInventory());
			}

			if (action == ClickType.RIGHT) {
				// delete
				player.sendMessage("§cVocê deletou o report do jogador §e" + "§c!");
			} else {
				// ir até ele
				player.sendMessage("§areportmanero!");
			}

		}
	}

	@EventHandler
	public void onPlayerChangeTag(PlayerChangeTag event) {
		event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.LEVEL_UP, 2f, 2f);
	}

}
