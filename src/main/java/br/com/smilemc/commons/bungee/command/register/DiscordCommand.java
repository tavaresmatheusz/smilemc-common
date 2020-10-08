package br.com.smilemc.commons.bungee.command.register;

import br.com.smilemc.commons.SmileMC;
import br.com.smilemc.commons.bungee.command.BungeeCommandArgs;
import br.com.smilemc.commons.common.account.permissions.Group;
import br.com.smilemc.commons.common.command.CommandClass;
import br.com.smilemc.commons.common.command.CommandFramework.Command;
import br.com.smilemc.commons.common.command.CommandSender;

public class DiscordCommand implements CommandClass {
	
	@Command(name = "discord", aliases = "dc", groupToUse = Group.TRIAL)
	public void ban(BungeeCommandArgs cmdArgs) {

		CommandSender sender = cmdArgs.getSender();

		sender.sendMessage("§fDiscord do servidor: §ediscord." + SmileMC.SITE);
		
		
	}
	

}
