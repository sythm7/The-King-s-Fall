package fr.sythm.thekingsfall;

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
	
	public boolean equals(Object obj) {
		
		if(! (obj instanceof Territory))
			return false;
		
		Territory territory = (Territory) obj;
		
		Couple<Integer, Integer> thisAreaFirst = new Couple<>(this.areaCoordinates.getFirstElement().getBlockX(), this.areaCoordinates.getFirstElement().getBlockZ());

		Couple<Integer, Integer> thisAreaSecond = new Couple<>(this.areaCoordinates.getSecondElement().getBlockX(), this.areaCoordinates.getSecondElement().getBlockZ());
		
		Couple<Integer, Integer> otherAreaFirst = new Couple<>(this.areaCoordinates.getFirstElement().getBlockX(), this.areaCoordinates.getFirstElement().getBlockZ());

		Couple<Integer, Integer> otherAreaSecond = new Couple<>(this.areaCoordinates.getSecondElement().getBlockX(), this.areaCoordinates.getSecondElement().getBlockZ());

		// TODO Non terminé : à finaliser.
		return false;
		
	}
}
