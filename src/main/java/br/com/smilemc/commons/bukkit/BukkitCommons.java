package br.com.smilemc.commons.bukkit;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;

import br.com.smilemc.commons.bukkit.account.BukkitAccount;
import br.com.smilemc.commons.bukkit.api.channels.bungee.BungeeChannelApi;
import br.com.smilemc.commons.bukkit.api.skin.controller.SkinManager;
import br.com.smilemc.commons.bukkit.api.title.TitleAPI;
import br.com.smilemc.commons.bukkit.command.BukkitCommandFramework;
import br.com.smilemc.commons.bukkit.events.tag.PlayerChangeTag;
import br.com.smilemc.commons.common.Common;
import br.com.smilemc.commons.common.Common.InstanceType;
import br.com.smilemc.commons.common.account.permissions.Group;
import br.com.smilemc.commons.common.account.scoreboard.Tag;
import br.com.smilemc.commons.common.backend.redis.RedisManager;
import br.com.smilemc.commons.common.backend.sql.SqlManager;
import br.com.smilemc.commons.common.command.CommandLoader;
import br.com.smilemc.commons.common.report.Report;
import br.com.smilemc.commons.common.report.ReportMessage;
import br.com.smilemc.commons.common.server.Server;
import br.com.smilemc.commons.common.util.ClassGetter;
import lombok.Getter;
import redis.clients.jedis.Jedis;

public class BukkitCommons extends JavaPlugin {

	@Getter
	private static BukkitCommons instance;
	public static BungeeChannelApi bungeeChannelApi;
	@Getter
	private ProtocolManager protocolManager;
	@Getter
	private SkinManager skinManager;

	public void loadServers() {
		Jedis jedis = Common.getBackendManager().getRedisManager().getJedisPool().getResource();
		
		if (!jedis.exists(Common.SERVERS) )
			return;
		
		for (String serverName : jedis.get(Common.SERVERS).split(";"))
			Common.getServerManager().newServer(new Server(serverName, 200));
		jedis.close();
	}

	@Override
	public void onLoad() {

		instance = this;
		bungeeChannelApi = new BungeeChannelApi(this);
		Common.setInstanceType(InstanceType.BUKKIT);
		Common.getBackendManager().setRedisManager(new RedisManager());
		Common.getBackendManager().setSqlManager(new SqlManager());

		super.onLoad();
	}

	@Override
	public void onEnable() {

		skinManager = new SkinManager();
		protocolManager = ProtocolLibrary.getProtocolManager();
		loadServers();

		new CommandLoader(new BukkitCommandFramework(this))
				.loadCommandsFromPackage("br.com.smilemc.commons.bukkit.command.register");

		for (Class<?> listener : ClassGetter.getClassesForPackage(this.getClass(),
				"br.com.smilemc.commons.bukkit.listener")) {
			if (Listener.class.isAssignableFrom(listener)) {
				try {
					Bukkit.getPluginManager().registerEvents((Listener) listener.newInstance(), this);
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}
		}

		new BukkitRunnable() {
			Jedis jedis = Common.getBackendManager().getRedisManager().getJedisPool().getResource();

			@Override
			public void run() {
				Common.getServerManager().allUpdate();
				if (jedis.exists(Common.NEW_REPORT)) {

					ReportMessage reportMessage = Common.getGson().fromJson(jedis.get(Common.NEW_REPORT),
							ReportMessage.class);

					Common.getReportManager()
							.newReport(new Report(reportMessage.getReportedName(), reportMessage.getReportedBy(),
									reportMessage.getReason(), reportMessage.getFormmatedTime()));

				}
				if (jedis.exists(Common.GROUP_UPDATE)) {
					Player player = Bukkit.getPlayer(jedis.get(Common.GROUP_UPDATE));

					if (player == null)
						return;

					BukkitAccount bukkitAccount = (BukkitAccount) Common.getAccountManager()
							.getAccount(player.getUniqueId());
					bukkitAccount.setGroup(Group.getGroupByName(Common.getBackendManager().getSqlManager().getString(
							"account_principal_infos", Common.getUuid(jedis.get(Common.GROUP_UPDATE)), "group")));
					Bukkit.getPluginManager()
							.callEvent(new PlayerChangeTag(player, Tag.getTagByGroup(bukkitAccount.getGroup())));
					for (String perm : bukkitAccount.getPermissionAttachment().getPermissions().keySet()) {
						bukkitAccount.getPermissionAttachment().setPermission(perm, false);
					}
					player.recalculatePermissions();
					
					TitleAPI.setTitle(player, Tag.getTagByGroup(bukkitAccount.getGroup()).getName(),
							"§eSeu grupo foi atualizado para " + Tag.getTagByGroup(bukkitAccount.getGroup()).getName(),
							3, 3, 3);
					player.sendMessage("§aSeu grupo foi atualizado para "
							+ Tag.getTagByGroup(bukkitAccount.getGroup()).getName() + "§a!");
				}
			}
		}.runTaskTimerAsynchronously(this, 0, 20l);

	}

}
