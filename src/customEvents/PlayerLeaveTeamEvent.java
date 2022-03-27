package customEvents;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import fr.sythm.thekingsfall.Team;

public class PlayerLeaveTeamEvent extends Event implements Cancellable {

	private static final HandlerList HANDLERS = new HandlerList();
	private Player player;
	private Team team;
	private boolean isCancelled;
	
	public static HandlerList getHandlerList() {
		return HANDLERS;
	}
	
	@Override
	public HandlerList getHandlers() {
		return HANDLERS;
	}

	public PlayerLeaveTeamEvent(Player player) {
		this.player = player;
		this.isCancelled = false;
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
	
}
