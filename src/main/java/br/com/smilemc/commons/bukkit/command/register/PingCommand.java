package br.com.smilemc.commons.bukkit.command.register;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import br.com.smilemc.commons.bukkit.api.player.PingAPI;
import br.com.smilemc.commons.bukkit.command.BukkitCommandArgs;
import br.com.smilemc.commons.common.command.CommandClass;
import br.com.smilemc.commons.common.command.CommandFramework.Command;

public class PingCommand implements CommandClass {

	@Command(name = "ping", aliases = { "ms", "latency" })
	public void ping(BukkitCommandArgs bukkitCommandArgs) {
		if (!bukkitCommandArgs.isPlayer()) {
			bukkitCommandArgs.getSender().sendMessage("Comando disponível apenas in-game!");
			return;
		}
		if (bukkitCommandArgs.getArgs().length == 0) {
			Player player = bukkitCommandArgs.getPlayer();
			player.sendMessage("§eSeu ping atual é de §a" + PingAPI.getPing(player) + "ms§e!");
		} else {
			Player target = Bukkit.getPlayer(bukkitCommandArgs.getArgs()[0]);
			if (target == null) {
				bukkitCommandArgs.getSender().sendMessage("§cEste jogador está offline!");
				return;
			}
			Player player = bukkitCommandArgs.getPlayer();
			player.sendMessage(
					"§eO ping atual de §a" + target.getName() + " §eé de §a" + PingAPI.getPing(target) + "ms§e!");
		}

	}
}
