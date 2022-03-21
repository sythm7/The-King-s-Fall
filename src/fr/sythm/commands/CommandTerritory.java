package fr.sythm.commands;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.sythm.thekingsfall.Territory;
import fr.sythm.utils.Couple;
import fr.sythm.utils.TerritoriesUtils;

public class CommandTerritory implements CommandExecutor {
	
	private Map<Player, Couple<Location, Location>> playersPositionsMap = new HashMap<>();
	
	private Set<Territory> territoriesList = new LinkedHashSet<>();

	public CommandTerritory(Map<Player, Couple<Location, Location>> playersPositionsMap, Set<Territory> territoriesList) {
		this.playersPositionsMap = playersPositionsMap;
		this.territoriesList = territoriesList;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String cmdName, String[] args) {
		
		if(! (sender instanceof Player))
			return false;
		
		Player player = (Player) sender;
		
		if(args.length != 1) {
			player.sendMessage(ChatColor.RED + "Invalid number of arguments -> Usage : /territory [create|remove]");
			return false;
		}
		
		if(args[0].equals("create"))
			return this.createTerritory(player);
		else if(args[0].equals("remove"))
			return this.removeTerritory(player);
		else if(args[0].equals("get"))
			return this.displayTerritory(player);
			
		player.sendMessage(ChatColor.RED + "Wrong argument " + args[0] + " -> Usage : /territory [create|remove]");
		
		return false;
	}
	
	public boolean createTerritory(Player player) {
		
		Couple<Location, Location> area = this.playersPositionsMap.get(player);
		
		if(area == null || area.getFirstElement() == null || area.getSecondElement() == null) {
			player.sendMessage(ChatColor.RED + "You need to select an area first !");
			return false;
		}
		
		this.territoriesList.add(new Territory(area));
		
		player.sendMessage(ChatColor.GREEN + "New territory successfully created from " + this.formatAreas(area.getFirstElement(), area.getSecondElement()) + ".");

		return true;
	}
	
	public boolean removeTerritory(Player player) {
		
		Territory territory = TerritoriesUtils.getTerritory(player.getLocation(), this.territoriesList);
		
		if(territory == null) {
			player.sendMessage(ChatColor.RED + "You are not in a territory !");
			return false;
		}
		
		this.territoriesList.remove(territory);
		
		player.sendMessage(ChatColor.GREEN + "Territory from " + this.formatAreas(territory.getAreaCoordinates().getFirstElement(),
				territory.getAreaCoordinates().getSecondElement())
		+ " succesfully removed.");
		
		return true;
	}
	
	public boolean displayTerritory(Player player) {
		
		Territory territory = TerritoriesUtils.getTerritory(player.getLocation(), this.territoriesList);
		if(territory == null) {
			player.sendMessage(ChatColor.RED + "You are not in a territory !");
			return false;
		}
		
		player.sendMessage(ChatColor.GREEN + "You are currently in the territory from " + 
		this.formatAreas(territory.getAreaCoordinates().getFirstElement(), 
				territory.getAreaCoordinates().getSecondElement()) + ".");
		
		return true;
	}
		
	
	public String formatAreas(Location l1, Location l2) {
		return "(" + l1.getBlockX() + ", ~, " + l1.getBlockZ() + ") to "
				+ "(" + l2.getBlockX() + ", ~, " + l2.getBlockZ() + ")";
	}
}
