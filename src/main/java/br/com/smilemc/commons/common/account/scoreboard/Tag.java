package br.com.smilemc.commons.common.account.scoreboard;

import br.com.smilemc.commons.common.account.permissions.Group;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Tag {

	MEMBRO("§7§lMEMBRO", "§7", "q", Group.MEMBRO), 
	ELITE("§a§lVIP", "§a§lVIP§3 ", "p", Group.VIP),
	PRO("§6§lPRO", "§6§lPRO§6 ", "o", Group.PRO),
	BLADE("§e§lBLADE", "§e§lBLADE§e ", "n", Group.BLADE),
	ULTRA("§d§lULTRA", "§d§lULTRA§d ", "m", Group.ULTRA),
	BETA("§1§LBETA", "§1§lBETA§1 ", "l", Group.BETA), 
	DESIGNER("§2§lDESIGNER", "§2§lDESIGNER§2 ", "k", Group.DESIGNER), 
	YOUTUBER("§b§lYT", "§b§lYT §b", "j", Group.YOUTUBER),
	YOUTUBERPLUS("§3§lYT+", "§3§lYT+ §3", "i", Group.YOUTUBERPLUS),
	BUILDER("§e§lBUILDER", "§e§lBUILDER§e ", "h", Group.DESIGNER), 
	INVESTIDOR("§a§lINVESTIDOR", "§a§lINVEST §a", "g", Group.INVESTIDOR),
	AJUDANTE("§3§lAJUDANTE", "§3§lAJUDANTE §a", "g", Group.INVESTIDOR),
	TRIAL("§5§lTRIAL", "§5§lTRIAL §5", "f", Group.TRIAL),
	MOD("§5§lMOD", "§5§lMOD §5", "e", Group.MOD),
	MODPLUS("§5§lMOD+", "§5§lMOD+ §5", "d", Group.MODPLUS), 
	ADMIN("§c§lADMIN", "§c§lADMIN §c", "c", Group.ADMIN),
	DIRETOR("§4§lDIRETOR", "§4§lDIRETOR §4", "b", Group.DIRETOR),
	DONO("§4§lDONO", "§4§lDONO §4", "a", Group.DONO);

	private String name, prefix, aZ;
	private Group groupToUse;

	public static Tag getTagByGroup(Group group) {

		for (Tag tag : Tag.values())
			if (tag.getGroupToUse() == group)
				return tag;

		return null;
	}

	public static Tag getTagByOrdinal(int ordinal) {
		for (Tag tag : Tag.values())
			if (tag.ordinal() == ordinal)
				return tag;

		return null;
	}

	public static Tag getTagByName(String string) {
		for (Tag tag : Tag.values())
			if (tag.toString().equalsIgnoreCase(string))
				return tag;

		return null;
	}

}
