package br.com.smilemc.commons.common.cooldown;

import java.util.Calendar;
import java.util.Date;

import br.com.smilemc.commons.common.util.string.StringDateUtils;

public class Cooldown {

	private int seconds;
	private Calendar calendar;
	private long time;
	
	public Cooldown(int seconds) {
		// TODO Auto-generated constructor stub

		this.seconds = seconds;

		calendar = Calendar.getInstance();
		calendar.add(Calendar.SECOND, this.seconds);
		time = calendar.getTimeInMillis();
		
	}

	public String expireIn() {
		return StringDateUtils.formatDifference(time).replace("-", "");
	}

	public boolean expired() {
		return new Date().after(new Date(time));
	}

}
