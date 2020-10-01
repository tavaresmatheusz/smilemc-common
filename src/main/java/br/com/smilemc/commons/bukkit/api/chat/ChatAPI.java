package br.com.smilemc.commons.bukkit.api.chat;

import lombok.Getter;
import lombok.Setter;

public class ChatAPI {

	@Getter
	@Setter
	private ChatState chatState = ChatState.ENABLED;
	@Getter
	private static final ChatAPI instance = new ChatAPI();

	public enum ChatState {
		ENABLED, DISABLED;
	}
}
