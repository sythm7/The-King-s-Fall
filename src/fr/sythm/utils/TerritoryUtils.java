package fr.sythm.utils;

import java.util.Set;

import org.bukkit.Bukkit;

import fr.sythm.thekingsfall.Territory;

public class TerritoryUtils {

	
	public static Territory getTerritory(Location2D location, Set<Territory> territoriesList) {
		
		for(Territory territory : territoriesList) {
			
			Couple<Location2D, Location2D> area = territory.getAreaCoordinates();
			Location2D first = area.getFirstElement();
			int firstX = first.getFirstElement();
			int firstZ = first.getSecondElement();
			
			Location2D second = area.getSecondElement();
			int secondX = second.getFirstElement();
			int secondZ = second.getSecondElement();

			int locationX = location.getFirstElement();
			int locationZ = location.getSecondElement();
			
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
			
			if(firstX * directionX <= locationX * directionX && locationX * directionX <= secondX * directionX && firstZ * directionZ <= locationZ * directionZ && locationZ * directionZ <= secondZ * directionZ)
				return territory;
			
		}
		
		return null;
	}
}
