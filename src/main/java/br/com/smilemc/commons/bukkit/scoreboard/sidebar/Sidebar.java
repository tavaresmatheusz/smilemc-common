package br.com.smilemc.commons.bukkit.scoreboard.sidebar;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import lombok.Getter;

public class Sidebar {
	@Getter
	private Scoreboard scoreboard;
	private Objective objective;
	private LineAdder lineAdder;

	public Sidebar() {
		this.scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
		this.objective = scoreboard.registerNewObjective("score", "dummy");
		this.objective.setDisplaySlot(DisplaySlot.SIDEBAR);
		this.lineAdder = new LineAdder(this.scoreboard, this.objective);
	}

	public void setDisplayName(String name) {
		objective.setDisplayName(name);
	}

	public void setLine(String line, int index) {
		
		Team team = scoreboard.getTeam("line" + index);

		if (team == null) {

			if (line.length() > 16) {
				int dif = line.length() - 16;
				lineAdder.addLine(line.substring(0, dif), "", line.substring(dif), index);
			} else {
				lineAdder.addLine(line, "", "", index);
			}
		} else {

			if (line.length() > 16) {
				int dif = line.length() - 16;

				team.setPrefix(line.substring(0, dif));
				team.setSuffix(line.substring(dif));

			} else {
				team.setPrefix(line);
				team.setSuffix("");
			}
		}
	}

	public void addBlankLine(int index) {
		setLine("§" + new Random().nextInt(9) + "§" + new Random().nextInt(9), index);
	}

}
