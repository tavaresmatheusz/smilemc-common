package br.com.smilemc.commons.common.backend.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Sql {

	private String hostname, database, user, password;
	private int port;

	public Connection connectionSetup() {

		Connection connection = null;

		try {
			connection = DriverManager.getConnection(
					"jdbc:mysql://" + hostname + ":" + port + "/" + database + "?autoReconnect=true", user, password);
		} catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		return connection;

	}

}
