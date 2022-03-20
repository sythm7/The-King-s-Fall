package fr.sythm.utils;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.TreeSet;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class HashMapList {
	
	private HashMap<Player, TeamColor> playersMap = new HashMap<>(); // Pour savoir dans quelle team se trouve le joueur (?)
	private EnumMap<TeamColor, TreeSet<Player>> listeJoueursTeam = new EnumMap<>(TeamColor.class);
	private HashMap<Player, Couple<Location, Location>> playersPositionsMap = new HashMap<>();
	
	
	
	public HashMap<Player, TeamColor> getPlayersMap() {
		return playersMap;
	}
	public EnumMap<TeamColor, TreeSet<Player>> getListeJoueursTeam() {
		return listeJoueursTeam;
	}
	public HashMap<Player, Couple<Location, Location>> getPlayersPositionsMap() {
		return playersPositionsMap;
	}

}
