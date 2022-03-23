package fr.sythm.thekingsfall;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.TreeSet;

import org.bukkit.entity.Player;

import fr.sythm.utils.HashMapList;
import fr.sythm.utils.TeamColor;

public class Economy {

	private static final int baseMoneyValue = 10; //Base value of money. Change if needed.
	
	private final int valuePerSecond = 10;
	
	private final int valuePerKill = 100;
	
	private final float valuePerKillModifier = 0.9f;
	
	// Manque paramètres. A foutre après avoir réfléchi à l'économie
	
	private HashMapList hashMapList = new HashMapList();
	
	public Economy() {
		
	}
	
	
	
}
