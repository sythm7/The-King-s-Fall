package fr.sythm.utils;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.TreeSet;
import org.bukkit.entity.Player;

import teamutils.Team;

public class HashMapList {
	
	private HashMap<Player, Team> playersMap = new HashMap<>(); // Pour savoir dans quelle team se trouve le joueur (?)
	private HashMap<Player, Couple<Location2D, Location2D>> playersPositionsMap = new HashMap<>();
	
	
	public HashMap<Player, Team> getPlayersMap() {
		return playersMap;
	}

	public HashMap<Player, Couple<Location2D, Location2D>> getPlayersPositionsMap() {
		return playersPositionsMap;
	}

}
