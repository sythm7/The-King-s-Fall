package fr.tkf.utils;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.TreeSet;
import org.bukkit.entity.Player;

import fr.tkf.team.Team;
import fr.tkf.territory.Territory;

public class UtilsAttributes {
	
	private HashMap<Player, Team> playersMap = new HashMap<>(); // Pour savoir dans quelle team se trouve le joueur (?)
	
	private HashMap<Player, Couple<Location2D, Location2D>> playersPositionsMap = new HashMap<>();
	
	private LinkedHashSet<Territory> territoriesList = new LinkedHashSet<>();
	
	private ArrayList<Team> teamsList = new ArrayList<>();
	
	public HashMap<Player, Team> getPlayersMap() {
		return playersMap;
	}

	public HashMap<Player, Couple<Location2D, Location2D>> getPlayersPositionsMap() {
		return playersPositionsMap;
	}

	public ArrayList<Team> getTeamsList() {
		return teamsList;
	}

	public LinkedHashSet<Territory> getTerritoriesList() {
		return territoriesList;
	}

}