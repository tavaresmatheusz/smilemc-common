package br.com.smilemc.commons.bukkit.command.register;

import br.com.smilemc.commons.bukkit.command.BukkitCommandArgs;
import br.com.smilemc.commons.common.account.permissions.Group;
import br.com.smilemc.commons.common.command.CommandClass;
import br.com.smilemc.commons.common.command.CommandSender;
import br.com.smilemc.commons.common.command.CommandFramework.Command;

public class PunishCommand implements CommandClass {
	
	@Command(name = "ban", aliases = "banir", groupToUse = Group.TRIAL)
	public void ban(BukkitCommandArgs cmdArgs) {

		String[] args = cmdArgs.getArgs();
		CommandSender sender = cmdArgs.getSender();
		
		if (args.length == 0) {
			sender.sendMessage("§aVocê deve utilizar §e/ban <name> <reason>");
			return;
		}
		
		
		
	}
	

}
