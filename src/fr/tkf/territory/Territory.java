package fr.tkf.territory;

import java.util.Set;
import org.bukkit.World;

import fr.tkf.team.TeamColor;
import fr.tkf.utils.Couple;
import fr.tkf.utils.Location2D;

public class Territory {

	private Couple<Location2D, Location2D> areaCoordinates;
	
	private TeamColor teamColor = TeamColor.NONE;
	
	public Territory(Couple<Location2D, Location2D> areaCoordinates) {
		this.areaCoordinates = areaCoordinates;
		
	}

	public Couple<Location2D, Location2D> getAreaCoordinates() {
		return areaCoordinates;
	}

	public void setAreaCoordinates(Couple<Location2D, Location2D> areaCoordinates) {
		this.areaCoordinates = areaCoordinates;
	}

	public TeamColor getTeamColor() {
		return teamColor;
	}
	
	public void setTeamColor(TeamColor teamColor) {
		this.teamColor = teamColor;
	}
	
	public boolean isOverlapping(Set<Territory> territoriesList) {
		
		for(Location2D location : this.getTerritoryCorners()) {
			if(TerritoryUtils.getTerritory(location, territoriesList) != null)
				return true;
		}
		
		return false;
	}
	
	public Location2D[] getTerritoryCorners() {
		
		Location2D[] territoryCorners = new Location2D[4];
		
		World world = this.areaCoordinates.getFirstElement().getWorld();
		
		territoryCorners[0] = this.areaCoordinates.getFirstElement();
		territoryCorners[1] = this.areaCoordinates.getSecondElement();
		territoryCorners[2] = new Location2D(world, territoryCorners[0].getFirstElement(), territoryCorners[1].getSecondElement());
		territoryCorners[3] = new Location2D(world, territoryCorners[1].getFirstElement(), territoryCorners[0].getSecondElement());
		
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
