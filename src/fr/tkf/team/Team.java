package fr.tkf.team;

import java.util.HashSet;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Team {

	private TeamColor teamColor = TeamColor.NONE;
	
	private HashSet<TeamPlayer> playersInTeam = new HashSet<>();
	
	public Team(TeamColor teamColor) {
		this.teamColor = teamColor;
	}
	
	public TeamColor getTeamColor() {
		return this.teamColor;
	}
	
	public void addPlayer(Player player) {
		TeamPlayer teamPlayer = new TeamPlayer(player);
		this.playersInTeam.add(teamPlayer);
	}
	
	public void removePlayer(Player player) {
		TeamPlayer teamPlayer = this.getPlayer(player);
		teamPlayer.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
		this.playersInTeam.remove(teamPlayer);
	}
	
	public HashSet<Player> getPlayersList() {
		final HashSet<Player> playersList = new HashSet<>();
		this.playersInTeam.forEach(teamPlayer -> playersList.add(teamPlayer.getSelf()));
		return playersList;
	}
	
	public HashSet<TeamPlayer> getTeamPlayersList() {
		return this.playersInTeam;
	}

	public TeamPlayer getPlayer(Player player) {
		for(TeamPlayer teamPlayer : this.playersInTeam) {
			if(teamPlayer.getSelf().getName().equals(player.getName()))
				return teamPlayer;
		}
		
		return null; // Not in the team
	}
	
	public int getSize() {
		return this.playersInTeam.size();
	}
	
	@Override
	public boolean equals(Object obj) {
		if(! (obj instanceof Team)) 
			return false;
		
		Team team = (Team) obj;
		
		return this.teamColor == team.teamColor;
	}
	
	@Override
	public String toString() {
		return this.teamColor.toString();
	}
}
