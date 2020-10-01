package br.com.smilemc.commons.common.punishments.types;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;

import br.com.smilemc.commons.common.Common;
import br.com.smilemc.commons.common.punishments.Punishment;

public class Ban extends Punishment {

	private UUID uuid;

	public Ban(UUID uuid) {
		this.uuid = uuid;
	}

	@Override
	public void punish(String author, String reason, long time) {
		// TODO Auto-generated method stub
		try {

			PreparedStatement preparedStatement = Common.getBackendManager().getSqlManager()
					.prepareStatement(
							"INSERT INTO `punishments_bans`(`uuid`, `authorName`, `banReason`, `banTime`, `active`) VALUES (?,?,?,?,?)");
			preparedStatement.setString(1, uuid.toString());
			preparedStatement.setString(2, author);
			preparedStatement.setString(3, reason);
			preparedStatement.setLong(4, time);
			preparedStatement.setBoolean(5, true);
			preparedStatement.executeUpdate();
			preparedStatement.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void unpunish() {
		
	}

}
