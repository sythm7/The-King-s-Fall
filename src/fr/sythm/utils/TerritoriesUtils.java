package fr.sythm.utils;

import java.util.Set;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import fr.sythm.thekingsfall.Territory;

public class TerritoriesUtils {

	
	public static Territory getTerritory(Location location, Set<Territory> territoriesList) {
		
		for(Territory territory : territoriesList) {
			
			Couple<Location, Location> area = territory.getAreaCoordinates();
			Location first = area.getFirstElement();
			int firstX = first.getBlockX();
			int firstZ = first.getBlockZ();
			
			Location second = area.getSecondElement();
			int secondX = second.getBlockX();
			int secondZ = second.getBlockZ();

			int locationX = location.getBlockX();
			int locationZ = location.getBlockZ();
			
			int directionX = 0;
			int directionZ = 0;
			
			if(firstX < secondX && firstZ < secondZ) {
				directionX = 1;
				directionZ = 1;
			}
			else if(firstX < secondX && firstZ > secondZ) {
				directionX = 1;
				directionZ = -1;
			}
			else if(firstX > secondX && firstZ < secondZ) {
				directionX = -1;
				directionZ = 1;
			}
			else if(firstX > secondX && firstZ > secondZ) {
				directionX = -1;
				directionZ = -1;
			}
			
			if(firstX * directionX < locationX * directionX && locationX * directionX < secondX * directionX && firstZ * directionZ < locationZ * directionZ && locationZ * directionZ < secondZ * directionZ)
				return territory;
			
		}
		
		return null;
	}
}
