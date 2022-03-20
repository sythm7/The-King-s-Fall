package fr.sythm.thekingsfall;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.TreeSet;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import fr.sythm.utils.Couple;
import fr.sythm.utils.HashMapList;
import fr.sythm.utils.TeamColor;
import net.md_5.bungee.api.ChatColor;

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
	public void onShearsInteract(PlayerInteractEvent event) {
		ItemStack selectedItem = event.getItem();
		
		Player player = event.getPlayer();
		
		Action action = event.getAction();
		
		World world = player.getWorld();
		
		if(selectedItem == null || ! selectedItem.getType().equals(Material.SHEARS))
			return;
		
		if(action.equals(Action.LEFT_CLICK_AIR) || action.equals(Action.LEFT_CLICK_BLOCK)) {
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
		}
	}

//	@EventHandler
//	public void onPlayerDamageByEntity(EntityDamageByEntityEvent event) {
//		
//		String deathMessage = new String();
//
//		if (! (event.getEntity() instanceof  Player))
//			return;
//		
//		Player player = (Player) event.getEntity();
//	
//		if(player.getHealth() - event.getDamage() <= 0) {
//			event.setCancelled(true);
//			if(event.getDamager() instanceof Player) {
//				Player killerPlayer = (Player) event.getDamager();
//				deathMessage = player.getName() + " was killed by " + killerPlayer.getName();
//			}
//			else if(event.getDamager() instanceof Projectile) {
//				Projectile projectile = (Projectile) event.getDamager();
//				if(projectile.getShooter() instanceof Player) {
//					deathMessage = player.getName() + " was killed by " + ((Player)projectile.getShooter()).getName();
//				}
//				else
//					deathMessage = player.getName() + " was killed by a " + ((Entity)projectile.getShooter()).getName();
//			}
//			
//			else {
//				deathMessage = player.getName() + " is dead.";
//			}
//			
//			this.makePlayerSpectate(player, deathMessage);
//		}
//	}
	
	
	@EventHandler
	public void onPlayerDamage(EntityDamageEvent event) {
		
		String deathMessage = new String();

		if (! (event.getEntity() instanceof  Player))
			return;
		
		Player player = (Player) event.getEntity();
	
		if(player.getHealth() - event.getDamage() <= 0) {
			event.setCancelled(true);
			
			if(player.getLastDamageCause() instanceof EntityDamageByEntityEvent) {
				
				EntityDamageByEntityEvent entityDamageEvent = (EntityDamageByEntityEvent) player.getLastDamageCause();
				
				if(entityDamageEvent.getDamager() instanceof Player) {
					Player killerPlayer = (Player) entityDamageEvent.getDamager();
					deathMessage = player.getName() + " was killed by " + killerPlayer.getName();
				}
				else if(entityDamageEvent.getDamager() instanceof Projectile) {
					Projectile projectile = (Projectile) entityDamageEvent.getDamager();
					if(projectile.getShooter() instanceof Player) {
						deathMessage = player.getName() + " was killed by " + ((Player)projectile.getShooter()).getName();
					}
					else
						deathMessage = player.getName() + " was killed by a " + ((Entity)projectile.getShooter()).getName();
				}
				else
					deathMessage = player.getName() + " is dead.";
			}
			else
				deathMessage = player.getName() + " is dead.";

			for(ItemStack itemstack : player.getInventory().getContents()) {
				if(itemstack != null)
					player.getWorld().dropItemNaturally(player.getLocation(), itemstack);
			}
				
			player.getInventory().clear();
			
			player.setHealth(20.0);
			player.setFoodLevel(20);
			
			//player.getWorld().playSound(player.getLocation(), Sound.BLOCK_ANVIL_PLACE, 1, 1);
			//player.getWorld().playSound(player.getLocation(), Sound.BLOCK_ANVIL_PLACE, 1, 1);
			
			//player.getWorld().strikeLightning(player.getLocation());
			
			player.getWorld().strikeLightningEffect(player.getLocation());
			
			this.makePlayerSpectate(player, deathMessage);
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
		
		Bukkit.getServer().broadcastMessage(ChatColor.DARK_PURPLE + deathMessage);
		
		if(chosenPlayer != null) {
			player.teleport(chosenPlayer);
			player.setSpectatorTarget(chosenPlayer);
		}
			
	}
	
	
//	@EventHandler
//	public void onPlayerDeath(PlayerDeathEvent event) {
//		
//		Player player = event.getEntity();
//		Bukkit.getServer().broadcastMessage(player.getName() + " is dead");
//		
//		World world = player.getWorld();
//		
//		RespawnPlayerThread rspPlayerThread = new RespawnPlayerThread(player, world);
//		rspPlayerThread.start();
//		
//	}
//	
//	private class RespawnPlayerThread extends Thread {
//		
//		private Player player;
//		private World world;
//		
//		public RespawnPlayerThread(Player player, World world) {
//			this.player = player;
//			this.world = world;
//		}
//		
//		@Override
//		public void run() {
//			RespawnScheduler rspScheduler = new RespawnScheduler(player);
//			Bukkit.getScheduler().runTask(Main.getInstance(), rspScheduler); 
//			
//			Bukkit.getServer().broadcastMessage("J'attends");
//			synchronized(rspScheduler) {
//				try {
//					rspScheduler.wait();
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
//			}
//			Bukkit.getServer().broadcastMessage("Je me réveille");
//			Bukkit.getScheduler().runTask(Main.getInstance(), () -> {
//				player.setGameMode(GameMode.SPECTATOR);
//				Player chosenPlayer = null;
//				for(Player worldPlayer : world.getPlayers()) {
//					chosenPlayer = worldPlayer;
//					if(chosenPlayer.getGameMode().equals(GameMode.SURVIVAL) || !chosenPlayer.equals(player)) {
//						Bukkit.getServer().broadcastMessage("Found player !");
//						break;
//					}
//				}
//				
//				Bukkit.getServer().broadcastMessage(player.getName() + " is tp to " + chosenPlayer.getName());
//				player.setSpectatorTarget(chosenPlayer);
//			});
//			
//			
//			/*TreeSet<Player> playersList = listeJoueursTeam.get(this.playersMap.get(player));
//			
//			Player chosenPlayer = playersList.first();
//			Iterator<Player> iterator = playersList.iterator();
//			
//			while(iterator.hasNext()) {
//				
//				if(!chosenPlayer.getGameMode().equals(GameMode.SURVIVAL) || chosenPlayer.equals(player)) {
//					chosenPlayer = iterator.next();
//				} else {
//					break;
//				}
//			}*/ //For players in a team
//			
//			
//		}
//	}
//	
//	private class RespawnScheduler implements Runnable{
//
//		private Player player;
//		
//		public RespawnScheduler(Player player) {
//			this.player = player;
//		}
//		@Override
//		public void run() {
//			PacketPlayInClientCommand packet = new PacketPlayInClientCommand(PacketPlayInClientCommand.EnumClientCommand.PERFORM_RESPAWN);
//			EntityPlayer entityPlayer = ((CraftPlayer) player).getHandle();
//			entityPlayer.playerConnection.a(packet);
//			Bukkit.getServer().broadcastMessage("Task done");
//			synchronized(this) {
//				notify();
//			}
//		} // This whole bullshit allow respawning instantly without clicking
//		
//	}
//	
}
