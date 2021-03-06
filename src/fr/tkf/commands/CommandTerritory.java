package fr.tkf.commands;

import java.util.Iterator;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import fr.tkf.team.Team;
import fr.tkf.team.TeamColor;
import fr.tkf.territory.Territory;
import fr.tkf.utils.Couple;
import fr.tkf.utils.Utils;
import fr.tkf.utils.Location2D;

public class CommandTerritory implements CommandExecutor {
	
	private String commandUsage = "-> Usage : /territory <help | create | setTeam | remove | get | list>";
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String cmdName, String[] args) {
		
		if(! (sender instanceof Player))
			return false;
		
		Player player = (Player) sender;
		
		if(args.length < 1) {
			player.sendMessage(ChatColor.RED + "Invalid number of arguments " + this.commandUsage);
			return false;
		}
		
		if(args[0].equals("create"))
			return this.createTerritory(player);
		else if(args[0].equals("remove"))
			return this.removeTerritory(player, args);
		else if(args[0].equals("get"))
			return this.displayTerritory(player);
		else if(args[0].equals("list"))
			return this.displayTerritoriesList(player);
		else if(args[0].equals("help"))
			return this.displayHelp(player);
		else if(args.length > 1 && args[0].equals("setTeam"))
			return this.setTeam(player, args);
			
		player.sendMessage(ChatColor.RED + "Wrong argument '" + args[0] + "' " + this.commandUsage);
		
		return false;
	}

	private boolean displayHelp(Player player) {
		
		StringBuilder sb = new StringBuilder();
		
		String newLine = "--------------------------------\n";
		
		sb.append(ChatColor.BLUE + "Help for /territory (aka /ter) :\n");
		sb.append(ChatColor.AQUA + "- /territory create : Creates a territory into the selected area. "
				+ "To select an area you need to pick a wooden_sword, then left click to choose the first position of the area, "
				+ "and right click to choose the second position of the area.\n");
		sb.append(ChatColor.BLUE + newLine + ChatColor.AQUA);
		sb.append(ChatColor.AQUA + "- /territory setTeam <color> [ID] : (see \"/team help\" for <color>, NONE = no color). If [ID] is not specified. Assigns the team represented by <color> to the territory at your location. "
				+ "Else, assigns the team to the territory corresponding to [ID].\n");
		sb.append(ChatColor.BLUE + newLine + ChatColor.AQUA);
		sb.append("- /territory remove [ID] : [ID] is optional. If [ID] is not specified, removes the territory situated at your location. "
				+ "Else, removes the territory determined by [ID]\n");
		sb.append(ChatColor.BLUE + newLine + ChatColor.AQUA);
		sb.append("- /territory get [ID] : [ID] is optional. If [ID] is not specified, displays the territory situated at your location.\n"
				+ "Else, displays the territory determined by [ID]\n");
		sb.append(ChatColor.BLUE + newLine + ChatColor.AQUA);
		sb.append("- /territory list : Displays the list of all created territories. "
				+ "Also useful to see the corresponding IDs for each territory.\n"
				+ ChatColor.BLUE + newLine);
		
		player.sendMessage(sb.toString());
		
		return true;
	}

	private boolean createTerritory(Player player) {
		
		Couple<Location2D, Location2D> area = null;
		
		if(Utils.playersPositionsMap.get(player) != null)
			area = new Couple<>(Utils.playersPositionsMap.get(player).getFirstElement(), Utils.playersPositionsMap.get(player).getSecondElement());
		
		if(area == null || area.getFirstElement() == null || area.getSecondElement() == null) {
			player.sendMessage(ChatColor.RED + "You need to select an area first !");
			return false;
		}
		
		Territory territory = new Territory(area);
				
		if(territory.isOverlapping()) {
			player.sendMessage(ChatColor.RED + "This area is overlapping another territory : territory not created.\nType '/territory list' to show a list of all created territories.");
			return false;
		}
		
		Utils.territoriesList.add(territory);
		
		player.sendMessage(ChatColor.GREEN + "New territory successfully created from " + ChatColor.WHITE + this.formatAreas(area.getFirstElement(), area.getSecondElement()) + ChatColor.GREEN + ".");

		return true;
	}
	
	private boolean setTeam(Player player, String[] args) {
		
		TeamColor color = null;
		
		Territory territory = null;
		
		if(! (args[1].equalsIgnoreCase("red") || args[1].equalsIgnoreCase("blue") || args[1].equalsIgnoreCase("green") || args[1].equalsIgnoreCase("yellow") || args[1].equalsIgnoreCase("none"))) {
			player.sendMessage(ChatColor.RED + "Invalid team " + args[1] + ". Type \"/team list\" to see the list of all created teams.");
			return false;
		}
		
		color = Enum.valueOf(TeamColor.class, args[1].toUpperCase());
		
		if(args.length == 2) {
			territory = Utils.getTerritory(new Location2D(player.getLocation()));
			if(territory == null) {
				player.sendMessage(ChatColor.RED + "You are not in a territory !");
				return false;
			}
		}
		else if(args.length == 3) {
			try {
				int id = Integer.valueOf(args[2]);
				
				territory = this.getTerritoryById(id);
				
				if(territory == null)
					throw new NumberFormatException();
			}
			catch(NumberFormatException nbfException) {
				player.sendMessage(ChatColor.RED + "Wrong argument '"+ args[1] + "' -> Usage : \"/territory setTeam \"" + color + " OR \"/territory setTeam " + color + " <ID>\" "
						+ "with ID the territory's ID among the territories displayed in \"/territory list\".");
				return false;
			}
		}
		else {
			player.sendMessage(ChatColor.RED + "Invalid number of arguments " + commandUsage);
			return false;
		}
		
		if(color != TeamColor.NONE && ! Utils.teamsList.contains(new Team(color))) {
			player.sendMessage(ChatColor.RED + "Team " + color + " does not exist. You can create it with \"/team create " + color + "\".");
			return false;
		}
		
		Team team = null;
		
		if(color.equals(TeamColor.NONE)) {
			if(territory.getTeamColor().equals(TeamColor.NONE)) {
				player.sendMessage(ChatColor.RED + "Can't remove team from territory " + Utils.getTerritoryId(territory) + " (territory has no team).");
				return false;
			}
			
			team = Utils.getTeam(territory.getTeamColor());
			
			team.manuallyRemoveTerritory(territory);
			
			player.sendMessage(ChatColor.GREEN + "Successfully removed team from territory " + Utils.getTerritoryId(territory) + ".");
		}
		else {
			
			String message = "Successfully set ";
			
			TeamColor territoryColor = territory.getTeamColor();
			
			if(! territoryColor.equals(TeamColor.NONE)) {
				team = Utils.getTeam(territoryColor);
				team.manuallyRemoveTerritory(territory);
				message = "Successfully removed " + team + " team and set ";
			}
			
			team = Utils.getTeam(color);
			boolean isAdded = team.manuallyAddTerritory(territory);
			
			if(! isAdded) {
				player.sendMessage(ChatColor.RED + "Team " + team + " already owns a territory. Team not set to this territory.");
				return false;
			}
			
			player.sendMessage(ChatColor.GREEN + message + color + " team to territory " + Utils.getTerritoryId(territory) + ".");
		}
		
		territory.setTeamColor(color);

		return true;
	}
	
	private boolean removeTerritory(Player player, String[] args) {
		
		Territory territory = null;
		
		if(args.length == 1)
			territory = Utils.getTerritory(new Location2D(player.getLocation()));
		else if(args.length == 2) {
			try {
				int id = Integer.valueOf(args[1]);
				
				territory = this.getTerritoryById(id);
				
				if(territory == null)
					throw new NumberFormatException();
			}
			catch(NumberFormatException nbfException) {
				player.sendMessage(ChatColor.RED + "Wrong argument '"+ args[1] + "' -> Usage : '/territory remove' OR '/territory remove <ID>' "
						+ "with ID the territory's ID among the territories displayed in '/territory list'.");
				return false;
			}
		}
		else {
			player.sendMessage(ChatColor.RED + "Wrong arguments -> Usage : '/territory remove' OR '/territory remove <ID>' "
					+ "with ID the territory's ID among the territories displayed in '/territory list'.");
			return false;
		}
		
		if(territory == null) {
			player.sendMessage(ChatColor.RED + "You are not in a territory !\nIf you want to remove a territory from the territories list, "
					+ "type '/territory list' to see the <id> of the territory you want to remove. "
					+ "Then, type '/territory remove <id>'.");
			return false;
		}
		
		if(! territory.getTeamColor().equals(TeamColor.NONE))
			Utils.getTeam(territory.getTeamColor()).manuallyRemoveTerritory(territory);
		
		Utils.territoriesList.remove(territory);
		
		player.sendMessage(ChatColor.GREEN + "Territory with the " + ChatColor.GOLD + "ID (" + Utils.getTerritoryId(territory) + ")" + ChatColor.GREEN + " and with the corners : " + 
		ChatColor.BLUE + this.formatCorners(territory.getTerritoryCorners()) + ChatColor.GREEN + " succesfully removed.");
		
		return true;
	}
	
	private boolean displayTerritoriesList(Player player) {
		
		StringBuilder sb = new StringBuilder();
		
		int id = 0;
		
		for(Territory territory : Utils.territoriesList) {
			
			Location2D[] corners = territory.getTerritoryCorners();
			
			String teamColor = territory.getTeamColor().toString().replaceFirst("NONE", "GRAY");
			
			sb.append(ChatColor.BLUE + "-" + ChatColor.GOLD + " ID : " + id++ + ChatColor.WHITE + "  -  " + ChatColor.DARK_PURPLE + 
					"Team = " + Enum.valueOf(ChatColor.class, teamColor) + teamColor.replaceFirst("GRAY", "NONE") + ChatColor.WHITE +
					"  -  " + ChatColor.AQUA + "Corners coordinates : " + this.formatCorners(corners) + "\n");
		}
		
		if(sb.toString().isEmpty()) {
			player.sendMessage(ChatColor.BLUE + "There are no territories to display.");
			return false;
		}
		
		player.sendMessage(ChatColor.BLUE + "List of all created territories :\n" + sb.toString());
		
		return true;
	}
	
	private boolean displayTerritory(Player player) {
		
		Territory territory = Utils.getTerritory(new Location2D(player.getLocation()));
		if(territory == null) {
			player.sendMessage(ChatColor.RED + "You are not in a territory !");
			return false;
		}
		
		Location2D[] corners = territory.getTerritoryCorners();
		
		String teamColor = territory.getTeamColor().toString().replaceFirst("NONE", "GRAY");
		
		player.sendMessage(ChatColor.GREEN + "You are currently in the territory with the " + ChatColor.GOLD + "ID (" + Utils.getTerritoryId(territory) + ")" + ChatColor.GREEN + ", " + ChatColor.DARK_PURPLE + 
				"Team = " + Enum.valueOf(ChatColor.class, teamColor) + teamColor.replaceFirst("GRAY", "NONE") + ChatColor.GREEN + ", and with corners coordinates : " + ChatColor.AQUA + this.formatCorners(corners));
		
		return true;
	}
	
	private Territory getTerritoryById(int id) {
		
		Iterator<Territory> iterator = Utils.territoriesList.iterator();
		
		Territory territory = null;
		
		int loopId = 0;
		while(iterator.hasNext() && loopId != id + 1) {
			territory = iterator.next();
			loopId++;
		}
		
		if(loopId != id + 1)
			return null;

		return territory;
	}
	
	private String formatAreas(Location2D l1, Location2D l2) {
		return "(" + l1.getFirstElement() + ", ~, " + l1.getSecondElement() + ") to "
				+ "(" + l2.getFirstElement() + ", ~, " + l2.getSecondElement() + ")";
	}
	
	private String formatCorners(Location2D[] corners) {
		
		StringBuilder sb = new StringBuilder();
		
		sb.append("(" + corners[0].getFirstElement() + ", ~, " + corners[0].getSecondElement() + "); " +
				"(" + corners[2].getFirstElement() + ", ~, " + corners[2].getSecondElement() + "); " +
				"(" + corners[1].getFirstElement() + ", ~, " + corners[1].getSecondElement() + "); " +
				"(" + corners[3].getFirstElement() + ", ~, " + corners[3].getSecondElement() + ")");
		
		return sb.toString();
	}
}
