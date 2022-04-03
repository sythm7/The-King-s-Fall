package fr.tkf.territory;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import fr.tkf.team.TeamColor;

public class TerritoryEvent extends Event {

	private Territory territory;
	
	private TeamColor color;

	public TerritoryEvent(Territory territory, TeamColor color) {
		super();
	}

	@Override
	public HandlerList getHandlers() {
		return null;
	}
	
	public Territory getTerritory() {
		return territory;
	}

	public TeamColor getColor() {
		return color;
	}
}
