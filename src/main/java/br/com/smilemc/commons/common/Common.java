package br.com.smilemc.commons.common;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;

import com.google.gson.Gson;
import com.google.gson.JsonParser;

import br.com.smilemc.commons.common.account.controller.AccountManager;
import br.com.smilemc.commons.common.backend.BackendManager;
import br.com.smilemc.commons.common.backend.redis.Redis;
import br.com.smilemc.commons.common.backend.sql.Sql;
import br.com.smilemc.commons.common.cooldown.controller.CooldownManager;
import br.com.smilemc.commons.common.report.controller.ReportManager;
import br.com.smilemc.commons.common.server.controller.ServerManager;
import br.com.smilemc.commons.common.util.geo.controller.LocationManager;
import br.com.smilemc.commons.common.util.mojang.NameFetcher;
import br.com.smilemc.commons.common.util.mojang.UUIDFetcher;
import lombok.Getter;
import lombok.Setter;

public class Common {

	@Getter
	private static final BackendManager backendManager = new BackendManager();
	@Getter
	private static final Sql defaultSql = new Sql("localhost", "hypemc", "root", "", 3306);
	@Getter
	private static final Redis defaultRedis = new Redis("localhost", 6379);

	@Getter
	private static final JsonParser jsonParser = new JsonParser();
	@Getter
	private static final Gson gson = new Gson();
	@Getter
	private static final AccountManager accountManager = new AccountManager();
	@Getter
	private static final CooldownManager cooldownManager = new CooldownManager();
	@Getter
	private static final ServerManager serverManager = new ServerManager();
	@Getter
	private static final LocationManager locationManager = new LocationManager();
	@Getter
	private static final ReportManager reportManager = new ReportManager();
	@Getter
	private static final UUIDFetcher uuidFetcher = new UUIDFetcher();
	@Getter
	private static final NameFetcher nameFetcher = new NameFetcher();
	@Getter
	@Setter
	private static InstanceType instanceType;

	public static final Pattern NICKNAME_PATTERN = Pattern.compile("[a-zA-Z0-9_]{1,16}");
	public static final String ONLINE_FIELD = "ONLINE-PLAYERS-IN-";
	public static final String GROUP_UPDATE = "GROUP-UPDATE";
	public static final String BAN = "NEW-BAN";
	public static final String NEW_REPORT = "NEW-REPORT";
	public static final String DELETE_REPORT = "DELETE-REPORT";
	public static final String TARGET_SERVER_FIELD = "TARGET-SERVER-FIELD-";
	public static final String SERVERS = "SERVERS";
	public static final String SERVER_UPDATE_STAGE = "UPDATE_STAGE";
	public static final String[] ALLOWED_COUNTRIES = { "BR", "PT" };

	public enum InstanceType {
		BUKKIT, BUNGEE;
	}

	public static boolean isAllowedCountry(String country) {
		for (String countryCode : Common.ALLOWED_COUNTRIES)
			if (countryCode.equals(country))
				return true;
		return false;
	}

	public static boolean validateName(String username) {
		return Common.NICKNAME_PATTERN.matcher(username).matches();
	}

	public static void debug(String msg) {
		System.out.println("[DEBUG] " + msg);

	}

	static Map<String, UUID> crackedUuids = new HashMap<>();

	public static UUID getUuid(String name) {

		if (uuidFetcher.getUUID(name) == null) {

			if (crackedUuids.containsKey(name))
				return crackedUuids.get(name);

			return crackedUuids.put(name, getBackendManager().getSqlManager().getUuidByName(name));
		}

		return uuidFetcher.getUUID(name);
	}

	public static void log(String msg) {
		System.out.println("[LOG] " + msg);
	}

}
