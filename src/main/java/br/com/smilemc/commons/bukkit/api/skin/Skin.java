package br.com.smilemc.commons.bukkit.api.skin;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

@AllArgsConstructor
@Getter
public class Skin {
	
	@NonNull
	private String name;
	@NonNull
	private UUID uuid;
	
}
