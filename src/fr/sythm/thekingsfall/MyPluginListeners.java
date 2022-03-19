package fr.sythm.thekingsfall;

import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import fr.sythm.utils.Couple;
import net.md_5.bungee.api.ChatColor;

public class MyPluginListeners implements Listener {
	
	private HashMap<Player, Couple<Location, Location>> playersPositionsMap;

	public MyPluginListeners(HashMap<Player, Couple<Location, Location>> playersPositionsMap) {
		this.playersPositionsMap = playersPositionsMap;
	}

	@EventHandler
	public void onWoodenSwordInteract(PlayerInteractEvent event) {
		
		ItemStack selectedItem = event.getItem();
		
		Player player = event.getPlayer();
		
		Action action = event.getAction();
		
		if(selectedItem == null || ! selectedItem.getType().equals(Material.WOODEN_SWORD))
			return;
		
		if(action.equals(Action.LEFT_CLICK_BLOCK)) {
			Location location = event.getClickedBlock().getLocation();

			Couple<Location, Location> locationCouple = this.playersPositionsMap.get(player);
			
			if(locationCouple == null)
				this.playersPositionsMap.put(player, new Couple<>(location, null));
			else
				locationCouple.setFirstElement(location);
			
			player.sendMessage(ChatColor.GOLD + "First position set to : (" + location.getX() + ", " + location.getY() + ", " + location.getZ() + ").");
			
		}
		else if(action.equals(Action.RIGHT_CLICK_BLOCK)) {
			Location location = event.getClickedBlock().getLocation();

			Couple<Location, Location> locationCouple = this.playersPositionsMap.get(player);
			
			if(locationCouple == null)
				this.playersPositionsMap.put(player, new Couple<>(null, location));
			else
				locationCouple.setSecondElement(location);
			
			player.sendMessage(ChatColor.BLUE + "Second position set to : (" + location.getX() + ", " + location.getY() + ", " + location.getZ() + ").");

		}

	}
	
}
