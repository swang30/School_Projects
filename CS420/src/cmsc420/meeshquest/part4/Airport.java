package cmsc420.meeshquest.part4;

import java.awt.geom.Point2D;

import java.util.TreeSet;

public class Airport extends Location {
	public TreeSet<Terminal> terminalSet = new TreeSet<Terminal>(new Point2DCoordinateComparator());
	
	public Airport(String name, int localX, int localY, int remoteX, int remoteY) {
		super(name, remoteX, remoteY, localX, localY);
	}


	public String getName() {
		return super.getName();
	}
	
	public int getLocalX() {
		return (int) super.getX();
	}

	public int getLocalY() {
		return (int) super.getY();
	}

	public int getRemoteX() {
		return super.getRemoteX();
	}


	public int getRemoteY() {
		return super.getRemoteY();
	}

	public String toString() {
		return "Airport: " + super.getName();
	}


	@Override
	public Boolean isCity() {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public Boolean isAirport() {
		// TODO Auto-generated method stub
		return true;
	}


	@Override
	public Boolean isTerminal() {
		// TODO Auto-generated method stub
		return false;
	}
	
	
	
	
}
