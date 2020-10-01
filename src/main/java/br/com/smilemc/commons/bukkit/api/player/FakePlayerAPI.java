package br.com.smilemc.commons.bukkit.api.player;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.reflect.FieldAccessException;
import com.comphenix.protocol.utility.MinecraftReflection;
import com.comphenix.protocol.wrappers.EnumWrappers.Difficulty;
import com.comphenix.protocol.wrappers.EnumWrappers.NativeGameMode;
import com.comphenix.protocol.wrappers.EnumWrappers.PlayerInfoAction;

import br.com.smilemc.commons.bukkit.BukkitCommons;
import br.com.smilemc.commons.bukkit.api.skin.Skin;
import br.com.smilemc.commons.common.Common;

import com.comphenix.protocol.wrappers.PlayerInfoData;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.comphenix.protocol.wrappers.WrappedGameProfile;
import com.comphenix.protocol.wrappers.WrappedSignedProperty;


public class FakePlayerAPI {
	
	public static void changePlayerName(Player player, String name) {
		changePlayerName(player, name, true);
	}

	@SuppressWarnings("unchecked")
	public static void changePlayerName(Player player, String name, boolean respawn) {

		if (respawn)
			removeFromTab(player);
		try {
			Object minecraftServer = MinecraftReflection.getMinecraftServerClass().getMethod("getServer").invoke(null);
			Object playerList = minecraftServer.getClass().getMethod("getPlayerList").invoke(minecraftServer);
			Field f = playerList.getClass().getSuperclass().getDeclaredField("playersByName");
			f.setAccessible(true);
			Map<String, Object> playersByName = (Map<String, Object>) f.get(playerList);
			playersByName.remove(player.getName());
			WrappedGameProfile profile = WrappedGameProfile.fromPlayer(player);
			Field field = profile.getHandle().getClass().getDeclaredField("name");
			field.setAccessible(true);
			field.set(profile.getHandle(), name);
			field.setAccessible(false);
			playersByName.put(name, MinecraftReflection.getCraftPlayerClass().getMethod("getHandle").invoke(player));
			f.setAccessible(false);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (respawn)
			respawnPlayer(player);
	}

	public void addToTab(Player player, Collection<? extends Player> players) {
		PacketContainer packet = new PacketContainer(PacketType.Play.Server.PLAYER_INFO);
		packet.getPlayerInfoAction().write(0, PlayerInfoAction.ADD_PLAYER);
		try {
			Object entityPlayer = MinecraftReflection.getCraftPlayerClass().getMethod("getHandle").invoke(player);
			Object getDisplayName = MinecraftReflection.getEntityPlayerClass().getMethod("getPlayerListName")
					.invoke(entityPlayer);
			int ping = (int) MinecraftReflection.getEntityPlayerClass().getField("ping").get(entityPlayer);
			packet.getPlayerInfoDataLists().write(0,
					Arrays.asList(new PlayerInfoData(WrappedGameProfile.fromPlayer(player), ping,
							NativeGameMode.fromBukkit(player.getGameMode()),
							getDisplayName != null ? WrappedChatComponent.fromHandle(getDisplayName) : null)));
		} catch (FieldAccessException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException | NoSuchFieldException e1) {
			e1.printStackTrace();
		}
		
		for (Player online : players) {
			if (!online.canSee(player))
				continue;
			try {
				BukkitCommons.getInstance().getProtocolManager().sendServerPacket(online, packet);
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		}
	}

	public static void removeFromTab(Player player) {
		PacketContainer packet = new PacketContainer(PacketType.Play.Server.PLAYER_INFO);
		packet.getPlayerInfoAction().write(0, PlayerInfoAction.REMOVE_PLAYER);
		try {
			Object entityPlayer = MinecraftReflection.getCraftPlayerClass().getMethod("getHandle").invoke(player);
			Object getDisplayName = MinecraftReflection.getEntityPlayerClass().getMethod("getPlayerListName")
					.invoke(entityPlayer);
			int ping = (int) MinecraftReflection.getEntityPlayerClass().getField("ping").get(entityPlayer);
			packet.getPlayerInfoDataLists().write(0,
					Arrays.asList(new PlayerInfoData(WrappedGameProfile.fromPlayer(player), ping,
							NativeGameMode.fromBukkit(player.getGameMode()),
							getDisplayName != null ? WrappedChatComponent.fromHandle(getDisplayName) : null)));
		} catch (FieldAccessException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException | NoSuchFieldException e1) {
			e1.printStackTrace();
		}
		for (Player online : Bukkit.getOnlinePlayers()) {
			if (!online.canSee(player))
				continue;
			try {
				BukkitCommons.getInstance().getProtocolManager().sendServerPacket(online, packet);
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		}
	}

	public static void respawnPlayer(Player player) {
		respawnSelf(player);
		for (Player online : Bukkit.getOnlinePlayers()) {
			if (online.equals(player) || !online.canSee(player)) {
				continue;
			}
			online.hidePlayer(player);
			online.showPlayer(player);
		}
	}

	@SuppressWarnings("deprecation")
	public static void respawnSelf(Player player) {
		List<PlayerInfoData> data = new ArrayList<>();

		try {
			Object entityPlayer = MinecraftReflection.getCraftPlayerClass().getMethod("getHandle").invoke(player);
			Object getDisplayName = MinecraftReflection.getEntityPlayerClass().getMethod("getPlayerListName")
					.invoke(entityPlayer);
			int ping = (int) MinecraftReflection.getEntityPlayerClass().getField("ping").get(entityPlayer);
			data.add(new PlayerInfoData(WrappedGameProfile.fromPlayer(player), ping,
					NativeGameMode.fromBukkit(player.getGameMode()),
					getDisplayName != null ? WrappedChatComponent.fromHandle(getDisplayName) : null));
		} catch (FieldAccessException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException | NoSuchFieldException e1) {
			e1.printStackTrace();
		}

		PacketContainer addPlayerInfo = new PacketContainer(PacketType.Play.Server.PLAYER_INFO);
		addPlayerInfo.getPlayerInfoAction().write(0, PlayerInfoAction.ADD_PLAYER);
		addPlayerInfo.getPlayerInfoDataLists().write(0, data);

		PacketContainer removePlayerInfo = new PacketContainer(PacketType.Play.Server.PLAYER_INFO);
		removePlayerInfo.getPlayerInfoAction().write(0, PlayerInfoAction.REMOVE_PLAYER);
		removePlayerInfo.getPlayerInfoDataLists().write(0, data);

		PacketContainer respawnPlayer = new PacketContainer(PacketType.Play.Server.RESPAWN);
		respawnPlayer.getIntegers().write(0, player.getWorld().getEnvironment().getId());
		respawnPlayer.getDifficulties().write(0, Difficulty.valueOf(player.getWorld().getDifficulty().name()));
		respawnPlayer.getGameModes().write(0, NativeGameMode.fromBukkit(player.getGameMode()));
		respawnPlayer.getWorldTypeModifier().write(0, player.getWorld().getWorldType());
		boolean isFlying = player.isFlying();
		try {
			BukkitCommons.getInstance().getProtocolManager().sendServerPacket(player, removePlayerInfo);
			BukkitCommons.getInstance().getProtocolManager().sendServerPacket(player, addPlayerInfo);
			BukkitCommons.getInstance().getProtocolManager().sendServerPacket(player, respawnPlayer);
			player.teleport(player.getLocation());
			player.setFlying(isFlying);
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		player.updateInventory();
	}

	public static WrappedSignedProperty changePlayerSkin(Player player, String name, UUID uuid, boolean respawn) {
		WrappedSignedProperty property = null;
		WrappedGameProfile gameProfile = WrappedGameProfile.fromPlayer(player);
		gameProfile.getProperties().clear();
		
		try {
			gameProfile.getProperties().put("textures", property = TextureAPI.textures.get(new WrappedGameProfile(uuid, name)));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if (respawn)
			respawnPlayer(player);
		
		return property;
	}
	
	public static void changePlayerSkin(Player player, WrappedSignedProperty property, boolean respawn) {
		WrappedGameProfile gameProfile = WrappedGameProfile.fromPlayer(player);
		gameProfile.getProperties().clear();
		
		gameProfile.getProperties().put("textures", property);
		
		if (respawn)
			respawnPlayer(player);
	}

	public static void changePlayerSkin(Player player, Skin skin) {
		if (skin == null)
			Common.debug("skin nula");
		if (skin.getName() == null)
			Common.debug("skin name nula");
		if (skin.getUuid() == null)
			Common.debug("skin uuid nula");
		changePlayerSkin(player, skin.getName(), skin.getUuid(), true);
	}
	
	public static void removePlayerSkin(Player player) {
		removePlayerSkin(player, true);
	}

	public static void removePlayerSkin(Player player, boolean respawn) {
		WrappedGameProfile gameProfile = WrappedGameProfile.fromPlayer(player);
		gameProfile.getProperties().clear();
		
		if (respawn) {
			respawnPlayer(player);
		}
	}

	public static boolean validateName(String username) {
		return Common.NICKNAME_PATTERN.matcher(username).matches();
	}
}
