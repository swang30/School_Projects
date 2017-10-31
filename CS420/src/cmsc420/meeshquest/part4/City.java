package cmsc420.meeshquest.part4;

import java.awt.geom.Point2D;

public class City extends Location {
	private int radius;
	private String color;
	
	public City(String name, int radius, String color, int localx, int localy, int remotex, int remotey) {
		super(name, remotex, remotey, localx, localy);
		this.radius = radius;
		this.color = color;
	}

	public String getName() {
		return super.getName();
	}
	
	public int getRemoteX() {
		return super.getRemoteX();
	}


	public int getRemoteY() {
		return super.getRemoteY();
	}
	
	public int getLocalX() {
		return super.getLocalX();
	}
	
	public int getLocalY() {
		return super.getLocalY();
	}

	public int getRadius() {
		return radius;
	}

	public String getColor() {
		return color;
	}

	@Override
	public Boolean isCity() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public Boolean isAirport() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Boolean isTerminal() {
		// TODO Auto-generated method stub
		return false;
	}

	
	
}
