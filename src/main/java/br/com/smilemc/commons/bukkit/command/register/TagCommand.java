package br.com.smilemc.commons.bukkit.command.register;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import br.com.smilemc.commons.bukkit.account.BukkitAccount;
import br.com.smilemc.commons.bukkit.command.BukkitCommandArgs;
import br.com.smilemc.commons.bukkit.events.tag.PlayerChangeTag;
import br.com.smilemc.commons.common.Common;
import br.com.smilemc.commons.common.account.scoreboard.Tag;
import br.com.smilemc.commons.common.command.CommandClass;
import br.com.smilemc.commons.common.command.CommandSender;
import br.com.smilemc.commons.common.command.CommandFramework.Command;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ClickEvent.Action;
import net.md_5.bungee.api.chat.TextComponent;

public class TagCommand implements CommandClass {

	@Command(name = "tag", aliases = { "color" })
	public void tag(BukkitCommandArgs bukkitCommandArgs) {

		CommandSender sender = bukkitCommandArgs.getSender();
		String[] args = bukkitCommandArgs.getArgs();

		if (!bukkitCommandArgs.isPlayer()) {
			sender.sendMessage("Comando disponível apenas in-game!");
			return;
		}

		Player player = bukkitCommandArgs.getPlayer();

		BukkitAccount bukkitAccount = (BukkitAccount) Common.getAccountManager().getAccount(player.getUniqueId());

		if (args.length == 0) {

			TextComponent textComponent = new TextComponent();
			TextComponent txt;

			textComponent.setText("§aVocê possui as seguintes tags: ");
			for (Tag tag : Tag.values()) {
				if (bukkitAccount.hasGroupPermission(tag.getGroupToUse())) {
					txt = new TextComponent();
					if (bukkitAccount.getGroup() != tag.getGroupToUse())
						txt.setText(tag.getName() + "§f, ");
					else
						txt.setText(tag.getName());

					txt.setClickEvent(new ClickEvent(Action.RUN_COMMAND,
							"/" + bukkitCommandArgs.getLabel() + " " + tag.toString()));
					textComponent.addExtra(txt);
				} else {
					textComponent.toString().substring(0, 2);
					break;
				}
			}
			player.spigot().sendMessage(textComponent);
			return;
		}

		Tag tag = Tag.getTagByName(args[0].replace("+", "plus").replace("yt", "youtuber"));

		if (tag == null) {
			sender.sendMessage("§cA tag '" + args[0] + "' não existe!");
			return;
		}

		if (!bukkitAccount.hasGroupPermission(tag.getGroupToUse())
				&& !player.hasPermission("tag." + tag.toString().toLowerCase())) {
			sender.sendMessage("§cVocê não possui permissão!");
			return;
		}

		Bukkit.getPluginManager().callEvent(new PlayerChangeTag(player, tag));
		player.sendMessage("§aVocê selecionou a tag " + tag.getName() + "§a!");

	}

}
