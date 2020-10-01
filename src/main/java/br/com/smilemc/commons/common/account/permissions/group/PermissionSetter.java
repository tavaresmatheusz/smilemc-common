package br.com.smilemc.commons.common.account.permissions.group;

import java.util.ArrayList;
import java.util.List;

import br.com.smilemc.commons.common.account.Account;
import br.com.smilemc.commons.common.account.permissions.Group;

public class PermissionSetter {

	private Group group;

	public PermissionSetter(Account account) {
		this.group = account.getGroup();
	}

	public List<String> getPerms() {
		List<String> permsList = new ArrayList<>();

		if (group == Group.DONO || group == Group.ADMIN || group == Group.DIRETOR) {
			permsList.add("bukkit.command.clear");
			permsList.add("bukkit.command.enchant");
			permsList.add("bukkit.command.gamerule");
			permsList.add("bukkit.command.op.take");
			permsList.add("bukkit.command.effect");
			permsList.add("bukkit.command.enchant");
			permsList.add("bukkit.command.kill");
			permsList.add("bukkit.command.time.set");
			permsList.add("bukkit.command.toggledownfall");
			permsList.add("bukkit.command.tps");
			permsList.add("bukkit.command.stop");
			permsList.add("bukkit.command.restart");
			permsList.add("bukkit.command.timings");
			permsList.add("bukkit.command.teleport");
			permsList.add("bukkit.command.whitelist");
			permsList.add("bukkit.command.whitelist.add");
			permsList.add("bukkit.command.whitelist.remove");
			permsList.add("bukkit.command.whitelist.list");
			permsList.add("worldedit.*");
			permsList.add("fawe.*");
		}
		if (group == Group.MODPLUS || group == Group.MOD) {
			permsList.add("bukkit.command.enchant");
			permsList.add("bukkit.command.enchant");
			permsList.add("bukkit.command.clear");
			permsList.add("bukkit.command.enchant");

		}
		return permsList;
	}

}
