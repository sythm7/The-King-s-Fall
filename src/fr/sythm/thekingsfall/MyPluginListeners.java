package fr.sythm.thekingsfall;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.TreeSet;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.function.Consumer;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scheduler.BukkitWorker;

import fr.sythm.utils.Couple;
import fr.sythm.utils.HashMapList;
import fr.sythm.utils.TeamColor;
import net.md_5.bungee.api.ChatColor;
import net.minecraft.server.v1_16_R3.EntityPlayer;
import net.minecraft.server.v1_16_R3.PacketPlayInClientCommand;

public class MyPluginListeners implements Listener {
	
	private HashMap<Player, Couple<Location, Location>> playersPositionsMap;
	private HashMap<Player, TeamColor> playersMap;
	private EnumMap<TeamColor, TreeSet<Player>> listeJoueursTeam;

	public MyPluginListeners(HashMapList hashMapList) {
		this.playersPositionsMap = hashMapList.getPlayersPositionsMap();
		this.listeJoueursTeam = hashMapList.getListeJoueursTeam();
		this.playersMap = hashMapList.getPlayersMap();
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

	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event) {
		
		Player player = event.getEntity();
		Bukkit.getServer().broadcastMessage(player.getName() + " is dead");
		
		World world = player.getWorld();
		
		RespawnPlayerThread rspPlayerThread = new RespawnPlayerThread(player, world);
		rspPlayerThread.start();
		
	}
	
	private class RespawnPlayerThread extends Thread{
		
		private Player player;
		private World world;
		
		public RespawnPlayerThread(Player player, World world) {
			this.player = player;
			this.world = world;
		}
		
		@Override
		public void run() {
			RespawnScheduler rspScheduler = new RespawnScheduler(player);
			Bukkit.getScheduler().runTask(Main.getInstance(), rspScheduler); 
			
			Bukkit.getServer().broadcastMessage("J'attends");
			synchronized(rspScheduler) {
				try {
					rspScheduler.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			Bukkit.getServer().broadcastMessage("Je me réveille");
			Bukkit.getScheduler().runTask(Main.getInstance(), () -> {
				player.setGameMode(GameMode.SPECTATOR);
				Player chosenPlayer = null;
				for(Player worldPlayer : world.getPlayers()) {
					chosenPlayer = worldPlayer;
					if(chosenPlayer.getGameMode().equals(GameMode.SURVIVAL) || !chosenPlayer.equals(player)) {
						Bukkit.getServer().broadcastMessage("Found player !");
						break;
					}
				}
				
				Bukkit.getServer().broadcastMessage(player.getName() + " is tp to " + chosenPlayer.getName());
				player.setSpectatorTarget(chosenPlayer);
			});
			
			
			/*TreeSet<Player> playersList = listeJoueursTeam.get(this.playersMap.get(player));
			
			Player chosenPlayer = playersList.first();
			Iterator<Player> iterator = playersList.iterator();
			
			while(iterator.hasNext()) {
				
				if(!chosenPlayer.getGameMode().equals(GameMode.SURVIVAL) || chosenPlayer.equals(player)) {
					chosenPlayer = iterator.next();
				} else {
					break;
				}
			}*/ //For players in a team
			
			
		}
	}
	
	private class RespawnScheduler implements Runnable{

		private Player player;
		
		public RespawnScheduler(Player player) {
			this.player = player;
		}
		@Override
		public void run() {
			PacketPlayInClientCommand packet = new PacketPlayInClientCommand(PacketPlayInClientCommand.EnumClientCommand.PERFORM_RESPAWN);
			EntityPlayer entityPlayer = ((CraftPlayer) player).getHandle();
			entityPlayer.playerConnection.a(packet);
			Bukkit.getServer().broadcastMessage("Task done");
			synchronized(this) {
				notify();
			}
		} // This whole bullshit allow respawning instantly without clicking
		
	}
	
}
