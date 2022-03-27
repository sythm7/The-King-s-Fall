package fr.sythm.thekingsfall;

import java.util.HashSet;

import org.bukkit.entity.Player;

import fr.sythm.utils.TeamColor;

public class Team {

	private TeamColor teamColor = TeamColor.NONE;
	
	private HashSet<Player> playersInTeam = new HashSet<>();
	
	public Team(TeamColor teamColor) {
		this.teamColor = teamColor;
	}
	
	public boolean equals(Object obj) {
		if(! (obj instanceof Team)) 
			return false;
		
		Team team = (Team) obj;
		
		return this.teamColor == team.teamColor;
	}
	
	public TeamColor getTeamColor() {
		return this.teamColor;
	}
	
	public void addPlayer(Player player) {
		this.playersInTeam.add(player);
	}
	
	public void removePlayer(Player player) {
		this.playersInTeam.remove(player);
	}
	
	public HashSet<Player> getPlayersList() {
		return this.playersInTeam;
	}
	
	public int getSize() {
		return this.playersInTeam.size();
	}
	
	@Override
	public String toString() {
		return this.teamColor.toString();
	}
	
}
