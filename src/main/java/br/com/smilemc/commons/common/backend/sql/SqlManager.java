package br.com.smilemc.commons.common.backend.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import br.com.smilemc.commons.common.Common;
import lombok.AllArgsConstructor;
import lombok.Getter;

public class SqlManager {

	private Sql sql;

	@Getter
	private Connection connection;

	public SqlManager() {
		// TODO Auto-generated constructor stub
		sql = Common.getDefaultSql();
		connection = sql.connectionSetup();
		setupTables();
	}

	public PreparedStatement prepareStatement(String sql) {
		try {
			return connection.prepareStatement(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@AllArgsConstructor
	public enum ObjectsTypes {
		STRING, INT, LONG, BOOLEAN;
	}

	public void update(ObjectsTypes objectsTypes, String table, UUID uuid, String field, Object object) {
		PreparedStatement preparedStatement = null;

		try {
			preparedStatement = prepareStatement("UPDATE `" + table + "` SET `" + field + "` = ? WHERE `uuid` = ?");

			if (objectsTypes == ObjectsTypes.STRING) {
				preparedStatement.setString(1, (String) object);
			}
			if (objectsTypes == ObjectsTypes.BOOLEAN) {
				preparedStatement.setBoolean(1, (Boolean) object);
			}
			if (objectsTypes == ObjectsTypes.INT) {
				preparedStatement.setInt(1, (int) object);
			}
			if (objectsTypes == ObjectsTypes.LONG) {
				preparedStatement.setLong(1, (long) object);
			}

			preparedStatement.setString(2, uuid.toString());
			preparedStatement.executeUpdate();
			preparedStatement.close();

		} catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	public void setupTables() {
		try {
			PreparedStatement statement = prepareStatement(
					"CREATE TABLE IF NOT EXISTS `account_principal_infos` (`id` INT NOT NULL AUTO_INCREMENT,`uuid` VARCHAR(39) NULL, `group` VARCHAR(999) NULL, `groupExpireIn` LONG NULL, `permissionList` VARCHAR(999) NULL, PRIMARY KEY (`id`));");
			statement.executeUpdate();
			statement = prepareStatement(
					"CREATE TABLE IF NOT EXISTS `account_preferences` (`id` INT NOT NULL AUTO_INCREMENT,`uuid` VARCHAR(39) NULL, `receivingTell` BOOLEAN NULL, `receivingReportMessages` BOOLEAN NULL, `receivingStaffChatMessages` BOOLEAN NULL, `receivingAntiCheatMessages` BOOLEAN NULL, PRIMARY KEY (`id`));");
			statement.executeUpdate();
			statement = prepareStatement(
					"CREATE TABLE IF NOT EXISTS `clans_principal_infos` (`id` INT NOT NULL AUTO_INCREMENT,`clanName` VARCHAR(16) NULL, `clanTag` BOOLEAN NULL,`clanMembers` VARCHAR(999) NULL,`clanAdmins` VARCHAR(999) NULL, PRIMARY KEY (`id`));");
			statement.executeUpdate();
			statement = prepareStatement(
					"CREATE TABLE IF NOT EXISTS `account_status` (`id` INT NOT NULL AUTO_INCREMENT,`uuid` VARCHAR(39) NULL, `league` VARCHAR(999) NULL, `xp` INT NULL, `coins` INT NULL, `totalXp` INT NULL, PRIMARY KEY (`id`));");
			statement.executeUpdate();
			statement = prepareStatement(
					"CREATE TABLE IF NOT EXISTS `account_status_pvp` (`id` INT NOT NULL AUTO_INCREMENT,`uuid` VARCHAR(39) NULL, `kills` INT NULL, `deaths` INT NULL, `ks` INT NULL, `beastKs` INT NULL, PRIMARY KEY (`id`));");
			statement.executeUpdate();

			statement = prepareStatement(
					"CREATE TABLE IF NOT EXISTS `punishments_bans` (`id` INT NOT NULL AUTO_INCREMENT,`uuid` VARCHAR(39) NULL, `authorName` VARCHAR(16) NULL, `banReason` VARCHAR(999) NULL, `banTime` LONG NULL, `active` BOOLEAN NULL, PRIMARY KEY (`id`));");
			statement.executeUpdate();

			statement = prepareStatement(
					"CREATE TABLE IF NOT EXISTS `punishments_mutes` (`id` INT NOT NULL AUTO_INCREMENT,`uuid` VARCHAR(39) NULL, `authorName` VARCHAR(16) NULL, `muteReason` VARCHAR(999) NULL, `muteTime` LONG NULL, `active` BOOLEAN NULL, PRIMARY KEY (`id`));");
			statement.executeUpdate();

			statement = prepareStatement(
					"CREATE TABLE IF NOT EXISTS `reports` (`id` INT NOT NULL AUTO_INCREMENT,`uuid` VARCHAR(39) NULL, `authorName` VARCHAR(16) NULL, `reportReason` VARCHAR(999) NULL, `active` BOOLEAN NULL, PRIMARY KEY (`id`));");
			statement.executeUpdate();

			statement = prepareStatement(
					"CREATE TABLE IF NOT EXISTS `cracked_mc` (`id` INT NOT NULL AUTO_INCREMENT,`name` VARCHAR(16) NULL, `uuid` VARCHAR(39) NULL, `password` VARCHAR(999) NULL, `address` VARCHAR(999) NULL, PRIMARY KEY (`id`));");
			statement.executeUpdate();
			statement = prepareStatement(
					"CREATE TABLE IF NOT EXISTS `proxys` (`id` INT NOT NULL AUTO_INCREMENT,`address` VARCHAR(22) NULL, `enabled` BOOLEAN NULL, PRIMARY KEY (`id`));");
			statement.executeUpdate();
			statement = prepareStatement(
					"CREATE TABLE IF NOT EXISTS `skins` (`id` INT NOT NULL AUTO_INCREMENT, `uuid` VARCHAR(39) NULL, `skinUuid` VARCHAR (39) NULL, `skinName` VARCHAR (16) NULL, PRIMARY KEY (`id`));");
			statement.executeUpdate();

			statement.close();
		} catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	public UUID getUuidByName(String name) {

		String string = null;

		try {

			PreparedStatement statement = prepareStatement("SELECT * FROM `cracked_mc` WHERE `name` = ?");
			statement.setString(1, name);
			ResultSet resultSet = statement.executeQuery();
			if (resultSet.next())
				string = resultSet.getString("uuid");

			resultSet.close();
			statement.close();

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		return string != null ? UUID.fromString(string) : null;
	}

	public String getString(String table, UUID uuid, String field) {

		String string = null;

		try {

			PreparedStatement statement = prepareStatement("SELECT * FROM `" + table + "` WHERE `uuid` = ?");
			statement.setString(1, uuid.toString());
			ResultSet resultSet = statement.executeQuery();
			if (resultSet.next())
				string = resultSet.getString(field);

			resultSet.close();
			statement.close();

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		return string;
	}

	public boolean getBoolean(String table, UUID uuid, String field) {

		boolean booolean = false;

		try {

			PreparedStatement statement = prepareStatement("SELECT * FROM `" + table + "` WHERE `uuid` = ?");
			statement.setString(1, uuid.toString());
			ResultSet resultSet = statement.executeQuery();
			if (resultSet.next())
				booolean = resultSet.getBoolean(field);

			resultSet.close();
			statement.close();

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		return booolean;
	}

	public int getInt(String table, UUID uuid, String field) {

		int integer = 0;

		try {

			PreparedStatement statement = prepareStatement("SELECT * FROM `" + table + "` WHERE `uuid` = ?");
			statement.setString(1, uuid.toString());
			ResultSet resultSet = statement.executeQuery();
			if (resultSet.next())
				integer = resultSet.getInt(field);

			resultSet.close();
			statement.close();

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		return integer;
	}

	public long getLong(String table, UUID uuid, String field) {

		long loong = 0;

		try {

			PreparedStatement statement = prepareStatement("SELECT * FROM `" + table + "` WHERE `uuid` = ?");
			statement.setString(1, uuid.toString());
			ResultSet resultSet = statement.executeQuery();
			if (resultSet.next())
				loong = resultSet.getLong(field);

			resultSet.close();
			statement.close();

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		return loong;
	}

}
