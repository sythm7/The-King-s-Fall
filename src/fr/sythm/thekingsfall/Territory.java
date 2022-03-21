package fr.sythm.thekingsfall;

import java.util.Set;

import org.bukkit.Location;

import fr.sythm.utils.Couple;
import fr.sythm.utils.TeamColor;

public class Territory {

	private Couple<Location, Location> areaCoordinates;
	
	private TeamColor teamColor = TeamColor.NONE;
	
	public Territory(Couple<Location, Location> areaCoordinates) {
		this.areaCoordinates = areaCoordinates;
	}
	

	public Couple<Location, Location> getAreaCoordinates() {
		return areaCoordinates;
	}

	public void setAreaCoordinates(Couple<Location, Location> areaCoordinates) {
		this.areaCoordinates = areaCoordinates;
	}

	public TeamColor getTeamColor() {
		return teamColor;
	}
	
	public void setTeamColor(TeamColor teamColor) {
		this.teamColor = teamColor;
	}
	
	public boolean isOverriding(Set<Territory> territoriesList) {
		
		for(Territory territory : territoriesList) {
			
		}
		
		return false;
	}
}
