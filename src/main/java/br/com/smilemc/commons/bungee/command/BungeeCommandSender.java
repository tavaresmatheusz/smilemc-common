package br.com.smilemc.commons.bungee.command;

import java.util.UUID;

import br.com.smilemc.commons.common.command.CommandSender;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

@AllArgsConstructor
@Getter
public class BungeeCommandSender implements CommandSender {

    private net.md_5.bungee.api.CommandSender sender;

    @Override
    public UUID getUniqueId() {
        if (sender instanceof ProxiedPlayer)
            return ((ProxiedPlayer) sender).getUniqueId();
        return UUID.randomUUID();
    }

    @Override
    public void sendMessage(String str) {
        sender.sendMessage(TextComponent.fromLegacyText(str));
    }

    @Override
    public void sendMessage(BaseComponent str) {
        sender.sendMessage(str);
    }

    @Override
    public void sendMessage(BaseComponent[] fromLegacyText) {
        sender.sendMessage(fromLegacyText);
    }

	@Override
	public String getName() {
		return sender.getName();
	}

}
