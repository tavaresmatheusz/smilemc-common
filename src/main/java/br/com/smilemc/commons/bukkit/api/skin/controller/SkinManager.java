package br.com.smilemc.commons.bukkit.api.skin.controller;

import java.util.HashMap;
import java.util.Map;

import br.com.smilemc.commons.bukkit.api.skin.Skin;
import br.com.smilemc.commons.common.Common;

public class SkinManager {

	private Map<String, Skin> skinMap;

	public SkinManager() {
		skinMap = new HashMap<>();
	}

	public Skin newSkin(String name) {
		if (skinMap.containsKey(name))
			return skinMap.get(name);
		skinMap.put(name, new Skin(name, Common.getUuidFetcher().getUUID(name)));
		return skinMap.get(name);
	}

	public Skin getSkin(String name) {
		if (!skinMap.containsKey(name))
			return newSkin(name);
		return skinMap.get(name);
	}

}
