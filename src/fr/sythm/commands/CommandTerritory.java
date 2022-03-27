package fr.sythm.commands;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;

import fr.sythm.thekingsfall.Territory;
import fr.sythm.utils.Couple;
import fr.sythm.utils.Location2D;
import fr.sythm.utils.TerritoryUtils;

public class CommandTerritory implements CommandExecutor {
	
	private Map<Player, Couple<Location2D, Location2D>> playersPositionsMap = new HashMap<>();
	
	private Set<Territory> territoriesList = new LinkedHashSet<>();

	public CommandTerritory(Map<Player, Couple<Location2D, Location2D>> playersPositionsMap, Set<Territory> territoriesList) {
		this.playersPositionsMap = playersPositionsMap;
		this.territoriesList = territoriesList;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String cmdName, String[] args) {
		
		if(! (sender instanceof Player))
			return false;
		
		Player player = (Player) sender;
		
		if(args.length < 1) {
			player.sendMessage(ChatColor.RED + "Invalid number of arguments -> Usage : /territory <help | create | remove | get | list>");
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
			
		player.sendMessage(ChatColor.RED + "Wrong argument '" + args[0] + "' -> Usage : /territory <help | create | remove | get | list>");
		
		return false;
	}
	
	public boolean createTerritory(Player player) {
		
		Couple<Location2D, Location2D> area = null;
		
		if(this.playersPositionsMap.get(player) != null)
			area = new Couple<>(this.playersPositionsMap.get(player).getFirstElement(), this.playersPositionsMap.get(player).getSecondElement());
		
		if(area == null || area.getFirstElement() == null || area.getSecondElement() == null) {
			player.sendMessage(ChatColor.RED + "You need to select an area first !");
			return false;
		}
		
		Territory territory = new Territory(area);
		
		StringBuilder coins = new StringBuilder();
		coins.append("Corners du nouveau ter : ");
		
		Arrays.asList(territory.getTerritoryCorners()).forEach(corner -> coins.append(corner.toString() + "; "));
		
		Bukkit.getServer().broadcastMessage(coins.toString());
		
		if(territory.isOverlapping(territoriesList)) {
			player.sendMessage(ChatColor.RED + "This area is overlapping another territory : territory not created.\nType '/territory list' to show a list of all created territories.");
			return false;
		}
		
		this.territoriesList.add(territory);
		
		player.sendMessage(ChatColor.GREEN + "New territory successfully created from " + ChatColor.WHITE + this.formatAreas(area.getFirstElement(), area.getSecondElement()) + ChatColor.GREEN + ".");

		return true;
	}
	
	public boolean removeTerritory(Player player, String[] args) {
		
		Territory territory = null;
		
		if(args.length == 1)
			territory = TerritoryUtils.getTerritory(new Location2D(player.getLocation()), this.territoriesList);
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
		
		this.territoriesList.remove(territory);
		
		player.sendMessage(ChatColor.GREEN + "Territory with the " + ChatColor.GOLD + "ID (" + this.getTerritoryId(territory) + ")" + ChatColor.GREEN + " and with the corners : " + 
		ChatColor.BLUE + this.formatCorners(territory.getTerritoryCorners()) + ChatColor.GREEN + " succesfully removed.");
		
		return true;
	}
	
	public boolean displayTerritoriesList(Player player) {
		
		StringBuilder sb = new StringBuilder();
		
		int id = 0;
		
		for(Territory territory : this.territoriesList) {
			
			Location2D[] corners = territory.getTerritoryCorners();
			
			sb.append(ChatColor.BLUE + "-" + ChatColor.GOLD + " ID : " + id++ + ChatColor.WHITE + "  -  " + ChatColor.AQUA + "Corners coordinates : " + this.formatCorners(corners) + "\n");
		}
		
		if(sb.toString().isEmpty()) {
			player.sendMessage(ChatColor.BLUE + "There is no territory to display.");
			return false;
		}
		
		player.sendMessage(ChatColor.BLUE + "List of all created territories :\n" + sb.toString());
		
		return true;
	}
	
	public boolean displayTerritory(Player player) {
		
		Territory territory = TerritoryUtils.getTerritory(new Location2D(player.getLocation()), this.territoriesList);
		if(territory == null) {
			player.sendMessage(ChatColor.RED + "You are not in a territory !");
			return false;
		}
		
		Location2D[] corners = territory.getTerritoryCorners();
		
		player.sendMessage(ChatColor.GREEN + "You are currently in the territory with the " + ChatColor.GOLD + "ID (" + this.getTerritoryId(territory) +
				")" + ChatColor.GREEN + ", and with corners coordinates : " + ChatColor.AQUA + this.formatCorners(corners));
		
		return true;
	}
	
	public int getTerritoryId(Territory territory) {
		
		int id = 0;
		for(Territory territory1 : this.territoriesList) {
			if(territory1.equals(territory))
				break;
			id++;
		}
		
		return id;
	}
	
	public Territory getTerritoryById(int id) {
		
		Iterator<Territory> iterator = this.territoriesList.iterator();
		
		Territory territory = null;
		
		int loopId = 0;
		while(iterator.hasNext() && loopId != id + 1) {
			territory = iterator.next();
			loopId++;
		}

		return territory;
	}
	
	public String formatAreas(Location2D l1, Location2D l2) {
		return "(" + l1.getFirstElement() + ", ~, " + l1.getSecondElement() + ") to "
				+ "(" + l2.getFirstElement() + ", ~, " + l2.getSecondElement() + ")";
	}
	
	public String formatCorners(Location2D[] corners) {
		
		StringBuilder sb = new StringBuilder();
		
		sb.append("(" + corners[0].getFirstElement() + ", ~, " + corners[0].getSecondElement() + "); " +
				"(" + corners[2].getFirstElement() + ", ~, " + corners[2].getSecondElement() + "); " +
				"(" + corners[1].getFirstElement() + ", ~, " + corners[1].getSecondElement() + "); " +
				"(" + corners[3].getFirstElement() + ", ~, " + corners[3].getSecondElement() + ")");
		
		return sb.toString();
	}
}
