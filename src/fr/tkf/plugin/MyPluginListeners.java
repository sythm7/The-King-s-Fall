package fr.tkf.plugin;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.TreeSet;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import fr.tkf.team.Team;
import fr.tkf.team.TeamColor;
import fr.tkf.team.TeamEvent;
import fr.tkf.team.TeamEventType;
import fr.tkf.team.TeamPlayer;
import fr.tkf.utils.Couple;
import fr.tkf.utils.Location2D;
import fr.tkf.utils.UtilsAttributes;
import net.md_5.bungee.api.ChatColor;

public class MyPluginListeners implements Listener {
	
	private HashMap<Player, Couple<Location2D, Location2D>> playersPositionsMap;
	private HashMap<Player, Team> playersMap;
	private EnumMap<TeamColor, TreeSet<Player>> listeJoueursTeam;
	private ArrayList<Team> teamsList;

	public MyPluginListeners(UtilsAttributes utilsAttr) {
		this.playersPositionsMap = utilsAttr.getPlayersPositionsMap();
		this.playersMap = utilsAttr.getPlayersMap();
		this.teamsList = utilsAttr.getTeamsList();
	}

	@EventHandler
	public void onPlayerConnect(PlayerJoinEvent event) {
		
		Player player = event.getPlayer();
		
		TeamPlayer foundTeamPlayer = null;
		
		for(Team team : this.teamsList) {
			foundTeamPlayer = team.getPlayer(player);
			if(foundTeamPlayer != null)
				break;
		}
		
		if(foundTeamPlayer != null)
			player.setScoreboard(foundTeamPlayer.getScoreboard());
	}
	
	@EventHandler
	public void onWoodenSwordInteract(PlayerInteractEvent event) {
		
		ItemStack selectedItem = event.getItem();
		
		Player player = event.getPlayer();
		
		Action action = event.getAction();
		
		if(selectedItem == null || ! selectedItem.getType().equals(Material.WOODEN_SWORD))
			return;
		
		if(action.equals(Action.LEFT_CLICK_BLOCK)) {
			Location2D location2D = new Location2D(event.getClickedBlock().getLocation());

			Couple<Location2D, Location2D> locationCouple = this.playersPositionsMap.get(player);
			
			if(locationCouple == null)
				this.playersPositionsMap.put(player, new Couple<>(location2D, null));
			else
				locationCouple.setFirstElement(location2D);
			
			player.sendMessage(ChatColor.GOLD + "First position set to : (" + location2D.getFirstElement() + ", ~," + location2D.getSecondElement() + ").");
			
		}
		else if(action.equals(Action.RIGHT_CLICK_BLOCK)) {
			Location2D location2D = new Location2D(event.getClickedBlock().getLocation());

			Couple<Location2D, Location2D> locationCouple = this.playersPositionsMap.get(player);
			
			if(locationCouple == null)
				this.playersPositionsMap.put(player, new Couple<>(null, location2D));
			else
				locationCouple.setSecondElement(location2D);
			
			player.sendMessage(ChatColor.BLUE + "Second position set to : (" + location2D.getFirstElement() + ", ~," + location2D.getSecondElement() + ").");

		}
	}
	
	@EventHandler
	public void onPlayerDamage(EntityDamageEvent event) {
		
		String deathMessage;

		if (! (event.getEntity() instanceof  Player))
			return;
		
		Player player = (Player) event.getEntity();
	
		if(player.getHealth() - event.getDamage() <= 0) {
			
			if(event.getCause() == DamageCause.ENTITY_ATTACK || event.getCause() == DamageCause.PROJECTILE) {
				return;
			}
			
			deathMessage = player.getName() + " is dead somehow.";
				
			this.performPlayerDeath(event, player, deathMessage);
		}
	}
	
	@EventHandler
	public void onPlayerDamageByEntity(EntityDamageByEntityEvent event) {
		
		String deathMessage;
		
		if (! (event.getEntity() instanceof  Player))
			return;
		
		Player player = (Player) event.getEntity();
		
		if(player.getHealth() - event.getDamage() <= 0) {
			if(event.getDamager() instanceof Player) {
				Player killerPlayer = (Player) event.getDamager();
				deathMessage = player.getName() + " was killed by " + killerPlayer.getName();
			}
			else if(event.getDamager() instanceof Projectile) {
				Projectile projectile = (Projectile) event.getDamager();
				if(projectile.getShooter() instanceof Player) {
					deathMessage = player.getName() + " was killed by " + ((Player)projectile.getShooter()).getName();
				}
				else if(projectile.getShooter() != null)
					deathMessage = player.getName() + " was killed by a " + ((Entity)projectile.getShooter()).getName();
				else
					deathMessage = player.getName() + " is dead somehow by an entity.";
				projectile.remove();
			}
			else
				deathMessage = player.getName() + " is dead somehow by an entity.";
			
			this.performPlayerDeath(event, player, deathMessage);
		}
	}
	
	public void performPlayerDeath(Cancellable event, Player player, String deathMessage) {
		
		player.getInventory().clear();
		player.setHealth(20.0);
		player.setFoodLevel(20);
		
		player.getWorld().strikeLightningEffect(player.getLocation());
		
		event.setCancelled(true);
		
		this.makePlayerSpectate(player, deathMessage);
	}

	@EventHandler
	public void onTeamPlayersChange(TeamEvent event) {
		Player player = event.getPlayer();
		TeamPlayer teamPlayer = event.getTeam().getPlayer(player);
		
		Bukkit.getServer().broadcastMessage(ChatColor.GREEN + "Event catched ! " + ChatColor.DARK_PURPLE + player.getName() + ChatColor.GREEN + " data sent to TeamEvent.");
		
		ScoreboardManager scoreboardManager = Bukkit.getScoreboardManager();
		
		Scoreboard teamScoreboard = scoreboardManager.getNewScoreboard();
		
		if(event.getEventType().equals(TeamEventType.ADD)) {
			
			String teamName;
			if(event.getTeam().toString().equalsIgnoreCase("red"))
				teamName = ChatColor.RED + "Red Team";
			else if(event.getTeam().toString().equalsIgnoreCase("blue"))
				teamName = ChatColor.BLUE + "Blue Team";
			else if(event.getTeam().toString().equalsIgnoreCase("green"))
				teamName = ChatColor.GREEN + "Green Team";
			else
				teamName = ChatColor.YELLOW + "Yellow Team";
			
			Objective objectiveTeamMoney = teamScoreboard.registerNewObjective(event.getTeam().toString(), "dummy", teamName);
			Objective objectiveTeamPlayers = teamScoreboard.registerNewObjective(player.getName(), "dummy", player.getName());
			
			Score money = objectiveTeamMoney.getScore(ChatColor.GOLD + "Monnaie");
			Score playersInTeam = objectiveTeamPlayers.getScore(player.getName());
			
			money.setScore(10);
			//playersInTeam.setScore(1);
			
			objectiveTeamMoney.setDisplaySlot(DisplaySlot.SIDEBAR);
			player.setScoreboard(teamScoreboard);
		

		} else if(event.getEventType().equals(TeamEventType.REMOVE)) {
			player.setScoreboard(scoreboardManager.getNewScoreboard());
			
		} else { // TEAM ELIMINATED
			// Idk for the moment
		}
		
		
	}

	public void makePlayerSpectate(Player player, String deathMessage) {
		
		World world = player.getWorld();
		
		player.setGameMode(GameMode.SPECTATOR);
		Player chosenPlayer = null;
		for(Player worldPlayer : world.getPlayers()) {
			chosenPlayer = worldPlayer;
			if(chosenPlayer.getGameMode().equals(GameMode.SURVIVAL) && ! chosenPlayer.equals(player)) {
				break;
			}
		}
		
		final Player finalPlayer = chosenPlayer;
		
		Bukkit.getServer().broadcastMessage(ChatColor.DARK_PURPLE + deathMessage);
		
		if(chosenPlayer != null) {
			player.teleport(chosenPlayer);
			Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> player.setSpectatorTarget(finalPlayer), 5);
			player.sendTitle(ChatColor.DARK_RED + "You are dead", ChatColor.WHITE + "Spectating " + chosenPlayer.getName(), 20, 60, 20);
		}
			
	}
}
