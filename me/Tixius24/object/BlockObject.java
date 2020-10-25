package me.Tixius24.object;

import java.io.Serializable;

public class BlockObject implements Serializable {

	private static final long serialVersionUID = 1L;
	private String particle = "";
	private String world = "";
	private double X = 0.0;
	private double Y = 0.0;
	private double Z = 0.0;

	public BlockObject(String particles, String worldName, double x, double y , double z) {
		particle = particles;
		world = worldName;
		X = x;
		Y = y;
		Z = z;
	}

	public String getParticle() {
		return particle;
	}

	public String getWorld() {
		return world;
	}

	public double getX() {
		return X;
	}

	public double getY() {
		return Y;
	}

	public double getZ() {
		return Z;
	}
}
