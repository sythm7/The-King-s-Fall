package fr.tkf.team;

import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;

public class TeamPlayer {

	private Player self = null;
	
	private Scoreboard scoreboard = null;
	
	public TeamPlayer(Player self) {
		this.self = self;
	}
	
	public TeamPlayer(Player self, Scoreboard scoreboard) {
		this.self = self;
		this.scoreboard = scoreboard;
	}
	
	public Player getSelf() {
		return this.self;
	}
	
	public void setSelf(Player player) {
		this.self = player;
		player.setScoreboard(this.scoreboard);
	}
	
	public Scoreboard getScoreboard() {
		return this.scoreboard;
	}
	
	public void setScoreboard(Scoreboard scoreboard) {
		this.scoreboard = scoreboard;
		self.setScoreboard(scoreboard);
	}
}
