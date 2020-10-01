package br.com.smilemc.commons.bungee.listener;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import br.com.smilemc.commons.SmileMC;
import br.com.smilemc.commons.bungee.BungeeCommons;
import br.com.smilemc.commons.bungee.account.BungeeAccount;
import br.com.smilemc.commons.bungee.api.server.ServerAPI;
import br.com.smilemc.commons.common.Common;
import br.com.smilemc.commons.common.server.Server;
import br.com.smilemc.commons.common.util.string.StringCenter;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.event.PreLoginEvent;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.event.ServerConnectedEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import redis.clients.jedis.Jedis;

public class ConnectionListener implements Listener {

	@EventHandler
	public void onPreLogin(PreLoginEvent event) {

		if (Common.getUuidFetcher().getUUID(event.getConnection().getName()) == null) {
			event.getConnection().setOnlineMode(false);
		} else {
			event.getConnection().setOnlineMode(true);
			Jedis jedis = Common.getBackendManager().getRedisManager().getJedisPool().getResource();
			jedis.set(Common.TARGET_SERVER_FIELD + event.getConnection().getName(), "lobby");
			jedis.close();
		}

	}

	@EventHandler
	public void onLogin(LoginEvent event) {
		Common.debug(event.getConnection().getAddress().getHostName());
		if (!Common.isAllowedCountry(Common.getLocationManager()
				.getLocation(event.getConnection().getAddress().getHostName()).getCountry_code())) {
			event.setCancelled(true);
			event.setCancelReason(TextComponent.fromLegacyText(SmileMC.PREFIX
					+ "\n§fO Seu país está bloqueado, caso esteja utilizando VPN/PROXY\nconsidere desativar e tentar novamente\n§f\n"
					+ SmileMC.SITE));

			Jedis jedis = Common.getBackendManager().getRedisManager().getJedisPool().getResource();
			jedis.expire(Common.TARGET_SERVER_FIELD + event.getConnection().getName(), 1);
			jedis.close();
			return;
		}

		Common.getAccountManager().registerAccount(event.getConnection().getUniqueId(),
				new BungeeAccount(event.getConnection().getName()));

	}

	@EventHandler
	public void onServerConnect(ServerConnectedEvent event) {
		BungeeAccount bungeeTargetPlayer = (BungeeAccount) Common.getAccountManager()
				.getAccount(event.getPlayer().getUniqueId());
		bungeeTargetPlayer.setServer(
				BungeeCommons.getInstance().getServerManager().getServer(event.getServer().getInfo().getName()));

	}

	@EventHandler
	public void onPostLogin(PostLoginEvent event) {

		ProxiedPlayer player = event.getPlayer();
		Jedis jedis = Common.getBackendManager().getRedisManager().getJedisPool().getResource();

		if (!jedis.exists(Common.TARGET_SERVER_FIELD + player.getName()))
			return;

		Server targetServer = Common.getServerManager()
				.getServer(jedis.get(Common.TARGET_SERVER_FIELD + player.getName()));

		jedis.expire(Common.TARGET_SERVER_FIELD + player.getName(), 1);

		event.getPlayer().connect(ServerAPI.getServerInfo(targetServer.getName()));

		if (targetServer.getName().equalsIgnoreCase("lobby")) {

			BungeeCommons.getInstance().getProxy().getScheduler().runAsync(BungeeCommons.getInstance(), new Runnable() {
				public void run() {
					try {

						if (Common.getBackendManager().getSqlManager().getString("skins",
								Common.getUuidFetcher().getUUID(player.getName()), "skinName") != null)
							return;

						PreparedStatement statement = Common.getBackendManager().getSqlManager()
								.prepareStatement("INSERT INTO `skins`(`uuid`, `skinUuid`, `skinName`) VALUES (?,?,?)");
						statement.setString(1, Common.getUuidFetcher().getUUID(player.getName()).toString());
						statement.setString(2, Common.getUuidFetcher().getUUID(player.getName()).toString());
						statement.setString(3, player.getName());
						statement.executeUpdate();
						statement.close();

					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			});

		}
	}

	@EventHandler
	public void onDisconnect(PlayerDisconnectEvent event) {
		Common.getAccountManager().unregisterAccount(event.getPlayer().getUniqueId());
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onProxyPing(ProxyPingEvent event) {

		ServerPing serverPing = event.getResponse();
		serverPing
				.setDescription("&7&m--&5&m]&6&m--&e&m--&f &6&lHYPE &dServidores de Minecraft &e&m--&6&m--&5&m[&7&m--&f"
						.replace("&", "§") + "\n§f" + StringCenter.centered("§fServidor de §4§lTESTES"));
		serverPing.getPlayers().setMax(2020);

		event.setResponse(serverPing);

	}

}
