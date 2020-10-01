package br.com.smilemc.commons.common.account.status;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import br.com.smilemc.commons.common.Common;
import lombok.Getter;
import lombok.Setter;

@Getter
public abstract class Status {

	private Common common;
	private UUID uuid;
	protected int kills = 0, deaths = 0, killStreak = 0, beastKs = 0;
	
	@Setter
	private StatusType statusType;

	public enum StatusType {
		PVP, HARDCOREGAMES, GLADIATOR;
	}

	public boolean update(String field, int updateTo) {
		try {
			PreparedStatement preparedStatement = Common.getBackendManager().getSqlManager()
					.prepareStatement("SELECT * FROM `account_status_" + statusType.toString().toLowerCase()
							+ "` WHERE = `uuid` SET = `" + field + "` = ?");
			preparedStatement.setInt(1, updateTo);
			preparedStatement.executeUpdate();
			preparedStatement.close();
			return true;
		} catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return false;
	}

	public Status(UUID uuid, StatusType statusType) {
		this.uuid = uuid;
		this.statusType = statusType;
		loadAll();
	}

	public void loadAll() {
		try {

			PreparedStatement preparedStatement = Common.getBackendManager().getSqlManager().prepareStatement(
					"SELECT * FROM `account_status_" + statusType.toString().toLowerCase() + "` WHERE `uuid` = ?");
			preparedStatement.setString(1, uuid.toString());
			ResultSet resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				kills = resultSet.getInt("kills");
				deaths = resultSet.getInt("deaths");
				killStreak = resultSet.getInt("ks");
				beastKs = resultSet.getInt("beastKs");
			}
			preparedStatement.close();
		} catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	public int getStatisticByNameInSql(String field) {
		int statistic = 0;
		try {
			PreparedStatement preparedStatement = Common.getBackendManager().getSqlManager().prepareStatement(
					"SELECT * FROM `account_status_" + statusType.toString().toLowerCase() + "` WHERE `uuid` = ?");
			preparedStatement.setString(1, uuid.toString());
			ResultSet resultSet = preparedStatement.executeQuery();
			if (resultSet.next())
				statistic = resultSet.getInt(field);
			preparedStatement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return statistic;
	}

}
