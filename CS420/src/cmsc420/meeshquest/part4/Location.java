package cmsc420.meeshquest.part4;

import java.awt.geom.Point2D;

/**
 * This class Location has two subclasses: City and Terminal
 * Use of this class in mapRoad().
 * @author swang
 *
 */
public abstract class Location extends Point2D.Float{
	private String name;
	private int remoteX;
	private int remoteY;
	
	protected Location(String name, int remotex, int remotey, int localx, int localy) {
		super.x = localx;
		super.y = localy;
		this.name = name;
		this.remoteX = remotex;
		this.remoteY = remotey;
	}
	
	public abstract Boolean isCity();
	public abstract Boolean isAirport();
	public abstract Boolean isTerminal();

	public String getName() {
		return name;
	}

	public int getRemoteX() {
		return remoteX;
	}

	public int getRemoteY() {
		return remoteY;
	}
	
	public int getLocalX() {
		return (int) super.getX();
	}
	
	public int getLocalY() {
		return (int) super.getY();
	}
	
	public boolean equals(Location l) {
		return this.getName().equals(l.getName());
	}
	
}
