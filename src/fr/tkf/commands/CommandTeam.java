package fr.tkf.commands;

import java.util.concurrent.atomic.AtomicBoolean;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import fr.tkf.team.Team;
import fr.tkf.team.TeamColor;
import fr.tkf.team.TeamEvent;
import fr.tkf.team.TeamEventType;
import fr.tkf.territory.Territory;
import fr.tkf.utils.Utils;

public class CommandTeam implements CommandExecutor {
	
	private String usageMessage = "Invalid number of arguments -> Usage : /team <help | list | create <color> | remove <color> | <color> addPlayers | <color> removePlayers | <color> list>";

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String cmdName, String[] args) {
		
		if(! (sender instanceof Player))
			return false;
		
		Player player = (Player) sender;
		
		if(args.length < 1) {
			player.sendMessage(ChatColor.RED + this.usageMessage);
			return false;
		}
		
		if(args[0].equals("help"))
			return this.displayHelp(player);
		
		else if(args[0].equals("list"))
			return this.displayTeamList(player);
		
		if(args.length < 2) {
			player.sendMessage(ChatColor.RED + this.usageMessage);
			return false;
		}
		
		if(args[0].equals("create")) {
			
			if(args.length == 2)
				return this.createTeam(player, args[1]);
			else {
				player.sendMessage(ChatColor.RED + this.usageMessage);
				return false;
			}
			
		} else if (args[0].equals("remove")){
			if(args.length == 2)
				return this.removeTeam(player, args[1]);
			else {
				player.sendMessage(ChatColor.RED + this.usageMessage);
				return false;
			}
		} else if (args[0].equalsIgnoreCase(TeamColor.BLUE.toString()) || args[0].equalsIgnoreCase(TeamColor.RED.toString()) || args[0].equalsIgnoreCase(TeamColor.GREEN.toString()) || args[0].equalsIgnoreCase(TeamColor.YELLOW.toString())) {
			if(args.length == 2) {
				if(args[1].equals("list"))
					return this.getPlayersList(player, args[0]);
			} 
			else if(args[1].equalsIgnoreCase("removePlayers"))
					return this.removePlayers(player, args);
			else if(args[1].equalsIgnoreCase("addPlayers"))
				return this.addPlayers(player, args);
			else {
				player.sendMessage(ChatColor.RED + this.usageMessage);
				return false;
			}
		}
		else {
			player.sendMessage(ChatColor.RED + "Invalid argument " + ChatColor.YELLOW + args[1] + ChatColor.RED 
					+ " -> Usage : /team <help | list | create <color> | remove <color> | <color> addPlayers | <color> removePlayers | <color> list>");
			return false;
		}
		
		return true;
	}

	private boolean displayTeamList(Player player) {
		
		final StringBuilder sb = new StringBuilder();

		Utils.teamsList.forEach(team -> {
			final StringBuilder sb1 = new StringBuilder();
			System.out.println("team = " + team);
			sb.append("\n" + Enum.valueOf(ChatColor.class, team.toString()) + team.toString());
			if(! team.getOwnedTerritories().isEmpty()) {
				sb.append(ChatColor.AQUA + "  -  Territories ID : ");
				team.getOwnedTerritories().forEach(territory -> sb1.append(ChatColor.GOLD + "" + Utils.getTerritoryId(territory) + "" + ChatColor.AQUA + ", "));
				sb.append(sb1.substring(0, sb1.length() - 2));
			}
		});
		
		if(sb.toString().isEmpty()) {
			player.sendMessage(ChatColor.BLUE + "There are no teams to display.");
			return false;
		}
		
		player.sendMessage(ChatColor.AQUA + "List of all created teams :" + ChatColor.AQUA + sb);
		
		return true;
	}

	private boolean displayHelp(Player player) {
		
		StringBuilder sb = new StringBuilder();
		
		String newLine = "--------------------------------\n";
		
		sb.append(ChatColor.BLUE + "Help for /team (aka /tm) :\n");
		sb.append(ChatColor.AQUA + "- /team create <color> : Creates a team which is "
				+ "represented by a <color> among 'BLUE', 'RED', 'GREEN' and 'YELLOW'.\n");
		sb.append(ChatColor.BLUE + newLine + ChatColor.AQUA);
		sb.append("- /team list : Display the list of all created teams.\n");
		sb.append(ChatColor.BLUE + newLine + ChatColor.AQUA);
		sb.append("- /team remove <color> : Removes the team represented by <color> if it exists.\n");
		sb.append(ChatColor.BLUE + newLine + ChatColor.AQUA);
		sb.append("- /team <color> addPlayers <player1 player2 ... playerN> : Adds the specified players to the team represented by <color> if possible.\n");
		sb.append(ChatColor.BLUE + newLine + ChatColor.AQUA);
		sb.append("- /team <color> removePlayers <player1 player2 ... playerN> : Removes the specified players from the team represented by <color> if possible.\n");
		sb.append(ChatColor.BLUE + newLine + ChatColor.AQUA);
		sb.append("- /team <color> list : Displays the list of the players in the team represented by <color> if it exists.\n");
		sb.append(ChatColor.BLUE + newLine);
		
		player.sendMessage(sb.toString());
		
		return true;
	}

	private boolean createTeam(Player player, String teamColor) {
		
		teamColor = teamColor.toUpperCase();
		
		if(teamColor.equals(TeamColor.BLUE.toString()) || teamColor.equals(TeamColor.RED.toString()) || teamColor.equals(TeamColor.GREEN.toString()) || teamColor.equals(TeamColor.YELLOW.toString())) {
			Team newTeam = new Team(Enum.valueOf(TeamColor.class, teamColor));
			if(! Utils.teamsList.contains(newTeam)) {
				Utils.teamsList.add(newTeam);
				player.sendMessage(ChatColor.GREEN + "Successfully added " + Enum.valueOf(ChatColor.class, teamColor) + teamColor + ChatColor.GREEN + " team.");
			}
			else {
				player.sendMessage(ChatColor.RED + "Team " + Enum.valueOf(ChatColor.class, teamColor) + teamColor + ChatColor.RED + " already exists !");
				return false;
			}
					
		} else {
			player.sendMessage(ChatColor.RED + "Cannot create team. Wrong argument given : " + ChatColor.BLUE + teamColor + ChatColor.RED + ". Usage : \"/team create <color>\" with <color> = RED or BLUE or GREEN or YELLOW");
			return false;
		}
		return true;
	}
	
	
	private boolean removeTeam(Player player, String teamColor) {
		
		teamColor = teamColor.toUpperCase();
		
		Team teamToRemove = null;
		
		if(teamColor.equals(TeamColor.BLUE.toString()) || teamColor.equals(TeamColor.RED.toString()) 
				|| teamColor.equals(TeamColor.GREEN.toString()) || teamColor.equals(TeamColor.YELLOW.toString())) {
			
			if(Utils.teamsList.contains(new Team(Enum.valueOf(TeamColor.class, teamColor)))) {
				
				teamToRemove = Utils.getTeam(Enum.valueOf(TeamColor.class, teamColor));
				
				Utils.teamsList.remove(teamToRemove);
				player.sendMessage(ChatColor.GREEN + "Successfully removed " + Enum.valueOf(ChatColor.class, teamColor) + teamColor + ChatColor.GREEN + " team.");
			} else {
				player.sendMessage(ChatColor.RED + "No team named + " + ChatColor.BLUE + teamColor + ChatColor.RED + " were found. Type \"/team list\" to see a list of all created teams.");
				return false;
			}
		}
		
		if(teamToRemove == null) {
			player.sendMessage(ChatColor.RED + "This team color does not exist : " + ChatColor.YELLOW + teamColor + ChatColor.RED + ". Type \"/team help\" for further informations.");
			return false;
		}
		
		for(Territory territory : Utils.territoriesList) {
			if(territory.getTeamColor().equals(TeamColor.BLUE))
				territory.setTeamColor(TeamColor.NONE);
		}
		
		for(Player searchedPlayer : teamToRemove.getPlayersList()) {
			TeamEvent teamEvent = new TeamEvent(searchedPlayer, TeamEventType.REMOVE);
			teamEvent.setTeam(teamToRemove);
			Bukkit.getPluginManager().callEvent(teamEvent);
		}
		
		return true;
	}

	private boolean addPlayers(Player player, String[] args) {
		
		int count = 0;
		Team team = null;
		
		if( !(args[0].equalsIgnoreCase(TeamColor.BLUE.toString()) || args[0].equalsIgnoreCase(TeamColor.RED.toString()) 
				|| args[0].equalsIgnoreCase(TeamColor.GREEN.toString()) || args[0].equalsIgnoreCase(TeamColor.YELLOW.toString()))) {
			player.sendMessage(ChatColor.RED + "Team name not valid. Wrong argument given : " + ChatColor.BLUE + args[0] + ChatColor.RED + ". Usage : \"/team <color> addPlayers\" with <color> = RED or BLUE or GREEN or YELLOW");
			return false;
		}
		
		args[0] = args[0].toUpperCase();
		
		TeamColor teamColor = Enum.valueOf(TeamColor.class, args[0]);
		for(Team teamsInList : Utils.teamsList) {
			if(teamsInList.getTeamColor().equals(teamColor)) {
				team = teamsInList;
				break;
			}
		}
		
		if(team == null) {
			player.sendMessage(ChatColor.RED + "No team " + ChatColor.BLUE + args[0] + ChatColor.RED + " were found. Please type \"/team create " + args[0] + "\" to create the team.");
			return false;
		}
		
		for(int i = 2; i < args.length; i++) {
			Player searchedPlayer = player.getServer().getPlayer(args[i]);
			if(searchedPlayer == null) {
				player.sendMessage(ChatColor.RED + "Player " + args[i] + " not found.");
			}
			else if(isPlayerInTeam(searchedPlayer)) { 
				player.sendMessage(ChatColor.RED + "Cannot add player " + ChatColor.YELLOW + searchedPlayer.getName() + ChatColor.RED + " to this team. Reason : Player is already in a team");
			} 
			else {
				count ++;
				team.addPlayer(searchedPlayer);
				TeamEvent teamEvent = new TeamEvent(searchedPlayer, TeamEventType.ADD);
				teamEvent.setTeam(team);
				Bukkit.getPluginManager().callEvent(teamEvent); //Check package customEvents for more infos
			}
		}
		player.sendMessage(ChatColor.GREEN + "Successfully added " + ChatColor.YELLOW + count + ChatColor.GREEN + " players in " + Enum.valueOf(ChatColor.class, args[0]) + args[0] + ChatColor.GREEN + " team.");
		
		return true;
	}

	
	private boolean removePlayers(Player player, String[] args) {
		
		int count = 0;
		Team team = null;
		
		if( !(args[0].equalsIgnoreCase(TeamColor.BLUE.toString()) || args[0].equalsIgnoreCase(TeamColor.RED.toString()) 
				|| args[0].equalsIgnoreCase(TeamColor.GREEN.toString()) || args[0].equalsIgnoreCase(TeamColor.YELLOW.toString()))) {
			player.sendMessage(ChatColor.RED + "Team name not valid. Wrong argument given : " + ChatColor.BLUE + args[0] + ChatColor.RED + ". Usage : \"/team <color> addPlayers\" with <color> = RED or BLUE or GREEN or YELLOW");
			return false;
		}
		
		args[0] = args[0].toUpperCase();
		
		TeamColor teamColor = Enum.valueOf(TeamColor.class, args[0]);
		for(Team teamsInList : Utils.teamsList) {
			if(teamsInList.getTeamColor().equals(teamColor)) {
				team = teamsInList;
				break;
			}
		}
		
		if(team == null) {
			player.sendMessage(ChatColor.RED + "No team named " + ChatColor.BLUE + args[0] + ChatColor.RED + " were found. Please type \"/team remove " + args[0] + "\" to remove the team.");
			return false;
		}
		
		for(int i = 2; i < args.length; i++) {
			Player searchedPlayer = this.getPlayerInTeam(args[i], team);
			if(searchedPlayer == null) {
				player.sendMessage(ChatColor.RED + "Player " + args[i] + " not found.");
			} else {
				count ++;
				TeamEvent teamEvent = new TeamEvent(searchedPlayer, TeamEventType.REMOVE);
				teamEvent.setTeam(team);
				Bukkit.getPluginManager().callEvent(teamEvent); //Check package customEvents for more infos
			}
		}
		player.sendMessage(ChatColor.GREEN + "Successfully removed " + ChatColor.YELLOW + count + ChatColor.GREEN + " players in " 
				+ Enum.valueOf(ChatColor.class, args[0]) + args[0] + ChatColor.GREEN + " team.");
		
		return true;
	}
	
	public Player getPlayerInTeam(String playerName, Team team) {
		
		for(Player player1 : team.getPlayersList()) {
			if(playerName.equals(player1.getName()))
				return player1;
		}
		
		return null;
	}
	
	private boolean getPlayersList(Player player, String teamColor) {
		
		Team team = null;
		
		StringBuilder sb = new StringBuilder();
		
		if( !(teamColor.equalsIgnoreCase(TeamColor.BLUE.toString()) || teamColor.equalsIgnoreCase(TeamColor.RED.toString()) 
				|| teamColor.equalsIgnoreCase(TeamColor.GREEN.toString()) || teamColor.equalsIgnoreCase(TeamColor.YELLOW.toString()))) {
			player.sendMessage(ChatColor.RED + "Team name not valid. Wrong argument given : " + ChatColor.BLUE + teamColor + ChatColor.RED + ". Usage : \"/team <color> list\" with <color> = RED or BLUE or GREEN or YELLOW");
			return false;
		}
		
		teamColor = teamColor.toUpperCase();
		
		TeamColor teamColor1 = Enum.valueOf(TeamColor.class, teamColor);
		for(Team teamsInList : Utils.teamsList) {
			if(teamsInList.getTeamColor().equals(teamColor1)) {
				team = teamsInList;
				break;
			}
		}
		
		if(team == null) {
			player.sendMessage(ChatColor.RED + "No team " + ChatColor.BLUE + teamColor + ChatColor.RED + " were found. Please type \"/team create " + teamColor + "\" to create the team.");
			return false;
		}
		
		if(team.getPlayersList().isEmpty()) {
			player.sendMessage(ChatColor.AQUA + "There are no players to display for team " + Enum.valueOf(ChatColor.class, teamColor) + teamColor + ChatColor.AQUA + ".");
			return false;
		}
		
		sb.append(ChatColor.AQUA + "List of the " + ChatColor.YELLOW + team.getSize() + ChatColor.AQUA + " " + Enum.valueOf(ChatColor.class, teamColor) + teamColor 
				+ ChatColor.AQUA + " team players :\n");
		
		for(Player player1 : team.getPlayersList()) {
			sb.append(Enum.valueOf(ChatColor.class, teamColor) + player1.getName() + ChatColor.AQUA + ", ");
		}
		
		String message = sb.substring(0, sb.length() - 2);
		
		player.sendMessage(message);
		
		return true;
	}
	
	private boolean isPlayerInTeam(Player player) {
		
		AtomicBoolean isInTeam = new AtomicBoolean(false);
		Utils.teamsList.forEach(team -> {
			if(team.getPlayersList().contains(player)) {
				isInTeam.set(true);
				return;
			}
		});
		return isInTeam.get();
	}
	
}
