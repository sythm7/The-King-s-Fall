package fr.sythm.teamutils;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class TeamEvent extends Event implements Cancellable {

	private static final HandlerList HANDLERS = new HandlerList();
	private Player player;
	private Team team;
	private boolean isCancelled;
	private TeamEventType eventType;

	
	public static HandlerList getHandlerList() {
		return HANDLERS;
	}
	
	@Override
	public HandlerList getHandlers() {
		return HANDLERS;
	}
	
	public TeamEvent(Player player, TeamEventType eventType) throws EnumConstantNotPresentException {
		this.player = player;
		this.isCancelled = false;
		if(eventType.equals(TeamEventType.ADD))
			this.eventType = TeamEventType.ADD;
		else if(eventType.toString().equalsIgnoreCase("remove"))
			this.eventType = TeamEventType.REMOVE;
		else if(eventType.toString().equalsIgnoreCase("eliminated"))
			this.eventType = TeamEventType.ELIMINATED;
		else
			throw new EnumConstantNotPresentException(TeamEventType.class, "Cannot initialize a TeamEventType when catching event.");
	}
	
	@Override
	public boolean isCancelled() {
		return this.isCancelled;
	}

	@Override
	public void setCancelled(boolean isCancelled) {
		this.isCancelled = isCancelled;
	}
	
	
	public Player getPlayer() {
		return this.player;
	}
	
	public Team getTeam() {
		return this.team;
	}
	
	public void setTeam(Team team) {
		this.team = team;
	}
	
	public TeamEventType getEventType() {
		return this.eventType;
	}
	
	
}
