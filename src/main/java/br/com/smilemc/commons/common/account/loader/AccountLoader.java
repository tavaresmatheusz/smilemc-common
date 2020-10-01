package br.com.smilemc.commons.common.account.loader;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.UUID;

import br.com.smilemc.commons.common.Common;
import br.com.smilemc.commons.common.account.PlayerAccount;
import br.com.smilemc.commons.common.account.config.AccountPreferences;
import br.com.smilemc.commons.common.account.config.AccountPunishments;
import br.com.smilemc.commons.common.account.permissions.Group;
import br.com.smilemc.commons.common.account.status.League;
import br.com.smilemc.commons.common.account.status.Status;
import br.com.smilemc.commons.common.account.status.games.KitPvP;
import br.com.smilemc.commons.common.backend.sql.SqlManager.ObjectsTypes;
import br.com.smilemc.commons.common.punishments.Punish;
import br.com.smilemc.commons.common.punishments.Punish.PunishType;

public class AccountLoader {

	@SuppressWarnings("resource")
	public PlayerAccount getPlayerAccount(UUID uuid) {
		int xp = 0, coins = 0;
		Group group = Group.MEMBRO;
		League league = League.UNRANKED;
		long groupExpireIn = 0;
		String permissionList = "";
		AccountPreferences accountPreferences = new AccountPreferences(uuid);
		Status kitpvp = new KitPvP(uuid);
		AccountPunishments accountPunishments = new AccountPunishments(uuid);
		PreparedStatement statement = null;
		try {

			statement = Common.getBackendManager().getSqlManager()
					.prepareStatement("SELECT * FROM `account_principal_infos` WHERE `uuid` = ?");
			statement.setString(1, uuid.toString());
			ResultSet resultSet = statement.executeQuery();
			if (resultSet.next()) {
				resultSet.close();
				statement.close();

				group = Group.getGroupByName(
						Common.getBackendManager().getSqlManager().getString("account_principal_infos", uuid, "group"));
				groupExpireIn = Common.getBackendManager().getSqlManager().getLong("account_principal_infos", uuid,
						"groupExpireIn");

				if (groupExpireIn > 0) {
					if (new Date().after(new Date(groupExpireIn))) {
						Common.getBackendManager().getSqlManager().update(ObjectsTypes.STRING,
								"account_principal_infos", uuid, "group", Group.MEMBRO.getName());
						Common.getBackendManager().getSqlManager().update(ObjectsTypes.LONG,
								"account_principal_infos", uuid, "groupExpireIn", 0);
						
						group = Group.MEMBRO;
						groupExpireIn = 0;
					}
				}

				xp = Common.getBackendManager().getSqlManager().getInt("account_status", uuid, "xp");
				league = League.getLeagueByName(
						Common.getBackendManager().getSqlManager().getString("account_status", uuid, "league"));
				coins = Common.getBackendManager().getSqlManager().getInt("account_status", uuid, "coins");
				permissionList = Common.getBackendManager().getSqlManager().getString("account_principal_infos", uuid,
						"groupExpireIn");
				accountPreferences.setReceivingAntiCheatMessages(Common.getBackendManager().getSqlManager()
						.getBoolean("account_preferences", uuid, "receivingAntiCheatMessages"));
				accountPreferences.setReceivingReportMessages(Common.getBackendManager().getSqlManager()
						.getBoolean("account_preferences", uuid, "receivingReportMessages"));
				accountPreferences.setReceivingStaffChatMessages(Common.getBackendManager().getSqlManager()
						.getBoolean("account_preferences", uuid, "receivingStaffChatMessages"));
				accountPreferences.setReceivingTell(Common.getBackendManager().getSqlManager()
						.getBoolean("account_preferences", uuid, "receivingTell"));

				statement = Common.getBackendManager().getSqlManager()
						.prepareStatement("SELECT * FROM `punishments_bans` WHERE `uuid` = ?");

				statement.setString(1, uuid.toString());

				resultSet = statement.executeQuery();

				if (resultSet.getFetchSize() > 0)
					for (int i = 0; i < resultSet.getFetchSize(); i++)
						if (resultSet.next())
							accountPunishments.addPunishment(new Punish(uuid, resultSet.getString("authorName"),
									resultSet.getString("banReason"), resultSet.getLong("banTime"), PunishType.BAN,
									resultSet.getBoolean("active")));

				statement = Common.getBackendManager().getSqlManager()
						.prepareStatement("SELECT * FROM `punishments_mutes` WHERE `uuid` = ?");

				statement.setString(1, uuid.toString());

				resultSet = statement.executeQuery();

				if (resultSet.getFetchSize() > 0)
					for (int i = 0; i < resultSet.getFetchSize(); i++)
						if (resultSet.next())
							accountPunishments.addPunishment(new Punish(uuid, resultSet.getString("authorName"),
									resultSet.getString("muteReason"), resultSet.getLong("muteTime"), PunishType.MUTE,
									resultSet.getBoolean("active")));

				resultSet.close();
				statement.close();

			} else {

				statement = Common.getBackendManager().getSqlManager().prepareStatement(
						"INSERT INTO `account_principal_infos`(`uuid`, `group`, `groupExpireIn`, `permissionList`) VALUES (?,?,?,?)");

				statement.setString(1, uuid.toString());
				statement.setString(2, group.getName());
				statement.setLong(3, groupExpireIn);
				statement.setString(4, permissionList);

				statement.executeUpdate();
				// account_status
				statement = Common.getBackendManager().getSqlManager().prepareStatement(
						"INSERT INTO `account_status`(`uuid`, `xp`, `coins`, `totalXp`) VALUES (?,?,?,?)");

				statement.setString(1, uuid.toString());
				statement.setInt(2, xp);
				statement.setInt(3, coins);
				statement.setInt(4, xp);

				statement.executeUpdate();

				statement = Common.getBackendManager().getSqlManager().prepareStatement(
						"INSERT INTO `account_status_pvp`(`uuid`, `kills`, `deaths`, `ks`, `beastKs`) VALUES (?,?,?,?,?)");

				statement.setString(1, uuid.toString());
				statement.setInt(2, kitpvp.getKills());
				statement.setInt(3, kitpvp.getDeaths());
				statement.setInt(4, kitpvp.getKillStreak());
				statement.setInt(5, kitpvp.getBeastKs());

				statement.executeUpdate();

				statement = Common.getBackendManager().getSqlManager().prepareStatement(
						"INSERT INTO `account_preferences`(`uuid`, `receivingTell`, `receivingReportMessages`, `receivingStaffChatMessages`, `receivingAntiCheatMessages`) VALUES (?,?,?,?,?)");

				statement.setString(1, uuid.toString());
				statement.setBoolean(2, true);
				statement.setBoolean(3, true);
				statement.setBoolean(4, true);
				statement.setBoolean(5, true);

				statement.executeUpdate();
				statement.close();

			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return new PlayerAccount(uuid, permissionList, xp, coins, group, league, groupExpireIn, accountPreferences,
				accountPunishments, null, kitpvp, null);
	}

}
