package br.com.smilemc.commons.common.util.geo.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

import br.com.smilemc.commons.common.Common;
import br.com.smilemc.commons.common.util.geo.Location;

public class LocationManager {

	private Map<String, Location> locationMap;

	public LocationManager() {
		// TODO Auto-generated constructor stub
		locationMap = new HashMap<>();
	}

	public void newLocation(String ipv4) {

		try {

			URLConnection connection = new URL("https://geoip-db.com/json/" + ipv4).openConnection();

			Location location = Common.getGson().fromJson(
					new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8")), Location.class);

			locationMap.put(ipv4, location);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public Location getLocation(String ipv4) {

		if (!locationMap.containsKey(ipv4))
			newLocation(ipv4);
		
		return locationMap.get(ipv4);
	}
}
