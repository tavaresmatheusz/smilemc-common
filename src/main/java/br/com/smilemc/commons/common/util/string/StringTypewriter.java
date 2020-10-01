package br.com.smilemc.commons.common.util.string;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.ChatColor;

public class StringTypewriter {

	private String displayText;
	private String toText;

	private char[] toTextChar;

	private int toTextLenght;
	private int i = 0;
	private int color = 1;

	private Map<Integer, ChatColor> displayColors;

	private boolean deletingDisplay;
	
	private String effect1, effect2;
	


	

	public StringTypewriter(String effect1, String effect2) {

		this.effect1 = effect1;
		this.effect2 = effect2;
		
		displayText = "";
		toText = effect1;

		toTextChar = toText.toCharArray();
		toTextLenght = toText.length();
		
		displayColors = new HashMap<>();
		
		displayColors.put(1, ChatColor.getByChar('6'));
		displayColors.put(2, ChatColor.getByChar('e'));
		displayColors.put(3, ChatColor.getByChar('f'));
		displayColors.put(4, ChatColor.getByChar('d'));
		displayColors.put(5, ChatColor.getByChar('4'));
		displayColors.put(6, ChatColor.getByChar('c'));
		displayColors.put(7, ChatColor.getByChar('a'));
	}

	public String next() {
		if (deletingDisplay) {

			if (displayText.length() == 0) {
				i = 0;

				if (toText.equals(effect1))
					toText = effect2;
				else
					toText = effect1;

				toTextChar = toText.toCharArray();
				deletingDisplay = false;
				toTextLenght = toText.length();

				if (color + 1 > displayColors.size())
					color = 1;
				else
					color++;

				return displayColors.get(color) + "§l" + displayText;
			}

			displayText = displayText.substring(0, displayText.length() - 1);

		} else {
			if (i + 1 > toTextLenght) {
				deletingDisplay = true;
				return displayColors.get(color) + "§l" + displayText;
			}
			displayText += toTextChar[i];
			i++;
		}

		return displayColors.get(color) + "§l" + displayText;
	}

}
