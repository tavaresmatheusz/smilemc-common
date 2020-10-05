package br.com.smilemc.commons.common.account.permissions;

import br.com.smilemc.commons.common.account.permissions.group.AdminPermissions;
import br.com.smilemc.commons.common.account.permissions.group.Permission;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public enum Group {

	MEMBRO("Membro"), VIP("Vip"), PRO("Pro"), BLADE("Blade"), ULTRA("Ultra"), BETA("Beta"), DESIGNER("Designer"),
	YOUTUBER("Youtuber"), YOUTUBERPLUS("Youtuber+"), BUILDER("Builder"), INVESTIDOR("Investidor"), AJUDANTE("Ajudante"),
	TRIAL("Trial"), MOD("Mod"), MODPLUS("Mod+"), ADMIN("Admin", new AdminPermissions()), DIRETOR("Diretor", new AdminPermissions()),
	DONO("Dono", new AdminPermissions());

	String name;
	Permission permission;

	Group(String name) {
		this.name = name;
	}

	Group(String name, Permission permission) {
		this.name = name;
		this.permission = permission;
	}

	public static Group getGroupByName(String name) {

		for (Group v : Group.values())
			if (v.getName().equalsIgnoreCase(name) || v.toString().equalsIgnoreCase(name))
				return v;

		return Group.MEMBRO;
	}

	public static Group getGroupByOrdinal(int ordinal) {

		for (Group v : Group.values())
			if (v.ordinal() == ordinal)
				return v;

		return Group.MEMBRO;
	}

	public static boolean existGroup(String name) {

		for (Group v : Group.values())
			if (v.getName().equalsIgnoreCase(name) || v.toString().equalsIgnoreCase(name))
				return true;

		return false;
	}

}
