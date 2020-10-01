package br.com.smilemc.commons.common.cooldown.controller;

import java.util.HashMap;
import java.util.Map;

import br.com.smilemc.commons.common.cooldown.Cooldown;

public class CooldownManager {

	private Map<String, Cooldown> cooldownMap;

	public CooldownManager() {
		// TODO Auto-generated constructor stub
		cooldownMap = new HashMap<>();
	}
	
	public void newCooldown(String cooldownIdentifier, int seconds) {
		cooldownMap.put(cooldownIdentifier, new Cooldown(seconds));
	}
	
	public boolean isActive(String cooldownIdentifier) {
		return getCooldown(cooldownIdentifier) == null ? false : getCooldown(cooldownIdentifier).expired();
	}
	
	public Cooldown getCooldown(String cooldownIdentifier) {
		return cooldownMap.containsKey(cooldownIdentifier) ? cooldownMap.get(cooldownIdentifier) : null;
	}

}
