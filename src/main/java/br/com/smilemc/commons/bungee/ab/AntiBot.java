package br.com.smilemc.commons.bungee.ab;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
public class AntiBot {
	
	private boolean inAttackMode = false;
	@Setter
	private boolean pirateAuthentication = true;
	private int maxLoginsInSecond = 3, maxLoginsInSecondInAttackMode = 5;
	@Setter
	private int connectionsInSecond = 0;
	@Setter
	private long lastAttack = 0l;

	public void setInAttackMode(boolean b) {	
		if (b)
			lastAttack = new Date().getTime();
		inAttackMode = b;
	}
	
		
	public boolean connectionIsEnabled() {
				
		if (connectionsInSecond > (inAttackMode ? maxLoginsInSecond : maxLoginsInSecondInAttackMode)) {
			if (!inAttackMode)
				setInAttackMode(true);
			return false;
		} 
		
			
		return true;
	}
	
}
