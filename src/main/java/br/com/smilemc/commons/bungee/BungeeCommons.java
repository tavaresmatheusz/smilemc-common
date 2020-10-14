package br.com.smilemc.commons.bungee;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import br.com.smilemc.commons.bungee.ab.AntiBot;
import br.com.smilemc.commons.bungee.ab.connection.controller.ConnectionManager;
import br.com.smilemc.commons.bungee.command.BungeeCommandFramework;
import br.com.smilemc.commons.bungee.listener.ChatListener;
import br.com.smilemc.commons.bungee.listener.ConnectionListener;
import br.com.smilemc.commons.bungee.redis.RedisMessage;
import br.com.smilemc.commons.common.Common;
import br.com.smilemc.commons.common.Common.InstanceType;
import br.com.smilemc.commons.common.backend.redis.RedisManager;
import br.com.smilemc.commons.common.backend.sql.SqlManager;
import br.com.smilemc.commons.common.command.CommandLoader;
import br.com.smilemc.commons.common.server.Server;
import br.com.smilemc.commons.common.server.controller.ServerManager;
import lombok.Getter;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.plugin.Plugin;
import redis.clients.jedis.Jedis;

public class BungeeCommons extends Plugin {

	@Getter
	private static BungeeCommons instance;

	@Getter
	private ServerManager serverManager;
	@Getter
	private AntiBot antiBot;
	@Getter
	private ConnectionManager connectionManager;

	public int loadServers() {
		List<String> servers = new ArrayList<String>();
		int a = 0;
		for (ServerInfo serverInfo : BungeeCord.getInstance().getServers().values()) {
			a++;
			serverManager.newServer(new Server(serverInfo.getName(), 120));
			servers.add(serverInfo.getName());
		}
		String b = "";
		for (String serverName : servers) {
			if (b.length() > 0)

				b += ";" + serverName;
			else
				b += serverName;
		}

		Jedis jedis = Common.getBackendManager().getRedisManager().getJedisPool().getResource();
		jedis.set(Common.SERVERS, b);
		jedis.close();
		return a;
	}

	@Override
	public void onEnable() {

		instance = this;
		serverManager = Common.getServerManager();
		connectionManager = new ConnectionManager();
		Common.setInstanceType(InstanceType.BUNGEE);
		Common.log("Iniciando conexão com o backend!");

		Common.getBackendManager().setSqlManager(new SqlManager());
		Common.getBackendManager().setRedisManager(new RedisManager());

		Common.log("Conexão realizada!");

		getProxy().getPluginManager().registerListener(this, new ConnectionListener());
		getProxy().getPluginManager().registerListener(this, new ChatListener());

		Common.log(loadServers() + " servidores registrados!");
		antiBot = new AntiBot();

		try {
			Common.log(new CommandLoader(new BungeeCommandFramework(this)).loadCommandsFromPackage(
					"br.com.smilemc.commons.bungee.command.register") + " comandos carregados!");
		} catch (Exception e) {
			e.printStackTrace();
		}

		Jedis jedis = Common.getBackendManager().getRedisManager().getJedisPool().getResource();
		RedisMessage redisMessage = new RedisMessage(jedis);
		BungeeCord.getInstance().getScheduler().schedule(this, new Runnable() {

			@Override
			public void run() {

				redisMessage.send();
				redisMessage.receive();
				serverManager.allUpdate();
			}
		}, 0, 1, TimeUnit.SECONDS);

		super.onEnable();
	}

}
