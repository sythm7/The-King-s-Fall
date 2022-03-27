package fr.sythm.utils;

import org.bukkit.Location;
import org.bukkit.World;

public class Location2D extends Couple<Integer, Integer> {
	
	private World world;

	public Location2D(World world, Integer x, Integer z) {
		super(x, z);
		this.world = world;
	}
	
	public Location2D(Location location) {
		super(location.getBlockX(), location.getBlockZ());
		this.world = location.getWorld();
	}
	
	public World getWorld() {
		return this.world;
	}
	
	@Override
	public boolean equals(Object obj) {
		
		if(! (obj instanceof Location2D))
			return false;
		
		Location2D location = (Location2D) obj;
		
		return this.firstElement.equals(location.firstElement) && this.secondElement.equals(location.secondElement);
	}
	
	@Override
	public int hashCode() {
		return (this.firstElement.hashCode() + this.secondElement.hashCode() + this.world.hashCode()) * 31;
	}
	
	public String toString() {
		return "(" + this.firstElement + ", " + this.secondElement + ")";
	}
}