package fr.tkf.utils;

public class LocationEditor {

	private double xOffSet;
	
	private double yOffSet;
	
	private double zOffSet;
	
	private int xSign;
	
	private int ySign;
	
	private int zSign;
	
	public LocationEditor(double xOffSet, double yOffSet, double zOffSet, int xSign, int ySign, int zSign) {
		this.xOffSet = xOffSet;
		this.yOffSet = yOffSet;
		this.zOffSet = zOffSet;
		this.xSign = xSign;
		this.ySign = ySign;
		this.zSign = zSign;
	}

	public double getXOffSet() {
		return xOffSet;
	}

	public double getYOffSet() {
		return yOffSet;
	}

	public double getZOffSet() {
		return zOffSet;
	}

	public int getXSign() {
		return xSign;
	}

	public int getYSign() {
		return ySign;
	}

	public int getZSign() {
		return zSign;
	}

	public int getXDistance() {
		return (int) xOffSet * xSign;
	}
	
	public int getYDistance() {
		return (int) yOffSet * ySign;
	}
	
	public int getZDistance() {
		return (int) zOffSet * zSign;
	}
	
	public double getXDirection(double x) {
		return xSign * x;
	}
	
	public double getYDirection(double y) {
		return ySign * y;
	}
	
	public double getZDirection(double z) {
		return zSign * z;
	}
}
