package fr.sythm.thekingsfall;

import java.util.Set;

import org.bukkit.Location;
import org.bukkit.World;

import fr.sythm.utils.Couple;
import fr.sythm.utils.TeamColor;
import fr.sythm.utils.TerritoryUtils;

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
	
	public boolean isOverlapping(Set<Territory> territoriesList) {
		
		for(Territory territory : territoriesList) {
			
			for(Location location : territory.getTerritoryCorners()) {
				if(TerritoryUtils.getTerritory(location, territoriesList) != null)
					return true;
			}
		}
		
		return false;
	}
	
	public Location[] getTerritoryCorners() {
		
		Location[] territoryCorners = new Location[4];
		
		World world = this.areaCoordinates.getFirstElement().getWorld();
		
		territoryCorners[0] = this.areaCoordinates.getFirstElement();
		territoryCorners[1] = this.areaCoordinates.getSecondElement();
		territoryCorners[2] = new Location(world, territoryCorners[0].getBlockX(), 0, territoryCorners[1].getBlockZ());
		territoryCorners[3] = new Location(world, territoryCorners[1].getBlockX(), 0, territoryCorners[0].getBlockZ());
		
		return territoryCorners;
	}
	
	@Override
	public int hashCode() {
		return (this.areaCoordinates.hashCode() + this.teamColor.hashCode()) * 31;
	}
	
	@Override
	public boolean equals(Object obj) {
		
		if(! (obj instanceof Territory))
			return false;
		
		Territory territory = (Territory) obj;
		
		return this.areaCoordinates.equals(territory.areaCoordinates) && this.teamColor.equals(territory.teamColor);
	}
}
