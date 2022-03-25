package fr.sythm.commands;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.sythm.thekingsfall.Team;
import fr.sythm.utils.TeamColor;

public class CommandTeam implements CommandExecutor {
	
	private ArrayList<Team> teamsList;

	public CommandTeam(ArrayList<Team> teamsList) {
		this.teamsList = teamsList;
	}


	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String cmdName, String[] args) {
		
		if(! (sender instanceof Player))
			return false;
		
		Player player = (Player) sender;
		
		
		if(args.length < 1) {
			player.sendMessage(ChatColor.RED + "Invalid number of arguments -> Usage : '/team <help | create <color> | remove <color> | <color> addPlayer | <color> removePlayer | <color> list>'");
			return false;
		}	
		
		if(args[0].equals("create")) {
			
			if(args.length == 2)
				return this.createTeam(player, args[1]);
			else {
				player.sendMessage(ChatColor.RED + "Invalid number of arguments -> Usage : '/team <help | create <color> | remove <color> | <color> addPlayer | <color> removePlayer | <color> list>'");
				return false;
			}
			
		} else if (args[0].equals("remove")){
			if(args.length == 2)
				return this.removeTeam(player, args[1]);
			else {
				player.sendMessage(ChatColor.RED + "Invalid number of arguments -> Usage : '/team <help | create <color> | remove <color> | <color> addPlayer | <color> removePlayer | <color> list>'");
				return false;
			}
		} else if (args[0].equalsIgnoreCase(TeamColor.BLUE.toString()) || args[0].equalsIgnoreCase(TeamColor.RED.toString()) || args[0].equalsIgnoreCase(TeamColor.GREEN.toString()) || args[0].equalsIgnoreCase(TeamColor.YELLOW.toString())) {
			if(args.length == 2) {
				if(args[1].equalsIgnoreCase("removePlayer"))
					return this.removePlayer(player, args);
				else if(args[1].equals("list"))
					return this.getPlayersList(player, args[0]);
			} 
			else if(args[1].equalsIgnoreCase("addPlayer"))
				return this.addPlayer(player, args);
			else {
				player.sendMessage(ChatColor.RED + "Invalid number of arguments -> Usage : '/team <help | create <color> | remove <color> | <color> addPlayer | <color> removePlayer | <color> list>'");
				return false;
			}
			
		}
		
		return true;
	}


	private boolean createTeam(Player player, String teamColor) {
		
		teamColor = teamColor.toUpperCase();
		
		if(teamColor.equals(TeamColor.BLUE.toString()) || teamColor.equals(TeamColor.RED.toString()) 
				|| teamColor.equals(TeamColor.GREEN.toString()) || teamColor.equals(TeamColor.YELLOW.toString())) {
			teamsList.add(new Team(Enum.valueOf(TeamColor.class, teamColor)));
			player.sendMessage(ChatColor.GREEN + "Successfully added " + Enum.valueOf(ChatColor.class, teamColor) + teamColor + ChatColor.GREEN + " team.");
		} else {
			player.sendMessage(ChatColor.RED + "Cannot create team. Wrong argument given : " + ChatColor.BLUE + teamColor + ChatColor.RED + ". Usage : '/team create <color>' with <color> = RED or BLUE or GREEN or YELLOW");
			return false;
		}
		return true;
	}
	
	
	private boolean removeTeam(Player player, String teamColor) {
		
		teamColor = teamColor.toUpperCase();
		
		if(teamColor.equals(TeamColor.BLUE.toString()) || teamColor.equals(TeamColor.RED.toString()) 
				|| teamColor.equals(TeamColor.GREEN.toString()) || teamColor.equals(TeamColor.YELLOW.toString())) {
			
			Team teamToRemove = new Team(Enum.valueOf(TeamColor.class, teamColor));
			if(teamsList.contains(teamToRemove)) {
				teamsList.remove(teamToRemove);
				player.sendMessage(ChatColor.GREEN + "Successfully removed " + Enum.valueOf(ChatColor.class, teamColor) + teamColor + ChatColor.GREEN + " team.");
			} else {
				player.sendMessage(ChatColor.RED + "No team named + " + ChatColor.BLUE + teamColor + ChatColor.RED + " were found. Type '/team list' to see a list of all created teams.");
				return false;
			}
		}
		
		return true;
	}

	private boolean addPlayer(Player player, String[] args) {
		
		int count = 0;
		Team team = null;
		
		if( !(args[0].equalsIgnoreCase(TeamColor.BLUE.toString()) || args[0].equalsIgnoreCase(TeamColor.RED.toString()) 
				|| args[0].equalsIgnoreCase(TeamColor.GREEN.toString()) || args[0].equalsIgnoreCase(TeamColor.YELLOW.toString()))) {
			player.sendMessage(ChatColor.RED + "Team name not valid. Wrong argument given : " + ChatColor.BLUE + args[0] + ChatColor.RED + ". Usage : \"/team <color> addPlayer\" with <color> = RED or BLUE or GREEN or YELLOW");
			return false;
		}
		
		args[0] = args[0].toUpperCase();
		
		TeamColor teamColor = Enum.valueOf(TeamColor.class, args[0]);
		for(Team teamsInList : teamsList) {
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
			} else {
				count ++;
				team.addPlayer(searchedPlayer);
			}
		}
		player.sendMessage(ChatColor.GREEN + "Successfully added " + ChatColor.YELLOW + count + ChatColor.GREEN + " players in " + Enum.valueOf(ChatColor.class, args[0]) + args[0] + ChatColor.GREEN + " team.");
		return true;
	}

	
	private boolean removePlayer(Player player, String[] args) {
		
		int count = 0;
		Team team = null;
		
		if( !(args[0].equalsIgnoreCase(TeamColor.BLUE.toString()) || args[0].equalsIgnoreCase(TeamColor.RED.toString()) 
				|| args[0].equalsIgnoreCase(TeamColor.GREEN.toString()) || args[0].equalsIgnoreCase(TeamColor.YELLOW.toString()))) {
			player.sendMessage(ChatColor.RED + "Team name not valid. Wrong argument given : " + ChatColor.BLUE + args[0] + ChatColor.RED + ". Usage : \"/team <color> addPlayer\" with <color> = RED or BLUE or GREEN or YELLOW");
			return false;
		}
		
		args[0] = args[0].toUpperCase();
		
		TeamColor teamColor = Enum.valueOf(TeamColor.class, args[0]);
		for(Team teamsInList : teamsList) {
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
				team.removePlayer(searchedPlayer);
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
		for(Team teamsInList : teamsList) {
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
			player.sendMessage(ChatColor.AQUA + "There is no players to display for team " + Enum.valueOf(ChatColor.class, teamColor) + teamColor + ChatColor.AQUA + ".");
			return false;
		}
		
		sb.append(ChatColor.AQUA + "List of the " + ChatColor.YELLOW + team.getSize() + ChatColor.AQUA + " " + Enum.valueOf(ChatColor.class, teamColor) + teamColor 
				+ ChatColor.AQUA + " team players :\n");
		
		for(Player player1 : team.getPlayersList()) {
			sb.append(Enum.valueOf(ChatColor.class, teamColor) + player1.getName() + ChatColor.AQUA + ", ");
		}
		
		//sb.delete(sb.lastIndexOf(sb.toString()) - 1, sb.lastIndexOf(sb.toString()));
		
		String message = sb.substring(0, sb.length() - 2);
		
		player.sendMessage(message);
		
		return true;
	}

	
}
