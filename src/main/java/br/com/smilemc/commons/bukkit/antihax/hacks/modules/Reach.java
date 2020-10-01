package br.com.smilemc.commons.bukkit.antihax.hacks.modules;

import java.util.Calendar;

import br.com.smilemc.commons.bukkit.antihax.Hack;

public class Reach extends Hack {

	public Reach() {
		// TODO Auto-generated constructor stub
		setName("Reach");
		setAutoBanIsEnabled(true);
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MINUTE, 15);
		setBanTime(calendar.getTimeInMillis());
	}
	
}
