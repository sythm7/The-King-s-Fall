package fr.tkf.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import org.bukkit.entity.Player;
import fr.tkf.team.Team;
import fr.tkf.team.TeamColor;
import fr.tkf.territory.Territory;

public class Utils {
	
	public static HashMap<Player, Team> playersMap = new HashMap<>(); // Pour savoir dans quelle team se trouve le joueur (?)
	
	public static HashMap<Player, Couple<Location2D, Location2D>> playersPositionsMap = new HashMap<>();
	
	public static LinkedHashSet<Territory> territoriesList = new LinkedHashSet<>();
	
	public static ArrayList<Team> teamsList = new ArrayList<>();
	
	public static Territory getTerritory(Location2D location) {
		
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

	public static Team getTeam(TeamColor color) {
		
		for(Team team : teamsList) {
			if(team.getTeamColor().equals(color))
				return team;
		}
		
		return null;
	}
	
			
}