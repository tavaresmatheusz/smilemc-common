package br.com.smilemc.commons.bungee.redis;

import java.util.UUID;

import br.com.smilemc.commons.bungee.BungeeCommons;
import br.com.smilemc.commons.common.Common;
import br.com.smilemc.commons.common.account.permissions.Group;
import br.com.smilemc.commons.common.server.Server;
import net.md_5.bungee.api.config.ServerInfo;
import redis.clients.jedis.Jedis;

public class RedisMessage {

	Jedis jedis;

	public RedisMessage(Jedis jedis) {

		this.jedis = jedis;
	}

	public void receive() {

		if (jedis.exists(Common.GROUP_UPDATE)) {
			UUID playerUUid = BungeeCommons.getInstance().getProxy().getPlayer(jedis.get(Common.GROUP_UPDATE))
					.getUniqueId();

			Common.getAccountManager().getAccount(playerUUid).setGroup(
					Group.getGroupByName(Common.getBackendManager().getSqlManager().getString("account_principal_infos",
							Common.getUuid(jedis.get(Common.GROUP_UPDATE)), "group")));

			Common.debug("Grupo de " + Common.getUuid(jedis.get(Common.GROUP_UPDATE)) + " atualizado");
		}
	}
	
	public void send() {
		jedis.set(Common.ONLINE_FIELD + "ALL", String.valueOf(BungeeCommons.getInstance().getProxy().getOnlineCount()));
		for (Server server : BungeeCommons.getInstance().getServerManager().getServers()) {
			ServerInfo serverInfo = BungeeCommons.getInstance().getProxy().getServerInfo(server.getName());
			jedis.set(Common.ONLINE_FIELD + server.getName(), String.valueOf(serverInfo.getPlayers().size()));
		}
	}

}
