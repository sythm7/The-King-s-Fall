package fr.tkf.team;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import fr.tkf.territory.Territory;

public class Team {

	private TeamColor teamColor = TeamColor.NONE;
	
	private HashSet<TeamPlayer> playersInTeam = new HashSet<>();
	
	private ArrayList<Territory> ownedTerritories = new ArrayList<>();
	
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
	
	public boolean manuallyAddTerritory(Territory territory) {
		if(! this.ownedTerritories.isEmpty()) {
			return false;
		}
		
		this.ownedTerritories.add(territory);
		
		return true;
	}
	
	public void manuallyRemoveTerritory(Territory territory) {
		System.out.print("Liste = ");
		this.ownedTerritories.forEach(ter -> System.out.println(ter.hashCode()));
		System.out.println("Ter = " + territory.hashCode());
		this.ownedTerritories.remove(territory);
		this.ownedTerritories.contains(territory);
	}
	
	public List<Territory> getOwnedTerritories() {
		return this.ownedTerritories;
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
