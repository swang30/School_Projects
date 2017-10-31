package cmsc420.meeshquest.part4;

import java.awt.geom.Point2D;

public class Terminal extends Location {
	private City TerminalCity;
	public Airport TerminalAirport;
	
	public Terminal(String name, int localX, int localY, int remoteX, int remoteY, City TerminalCity, Airport TerminalAirport) {
		super(name, remoteX, remoteY, localX, localY);
		this.TerminalCity = TerminalCity;
		this.TerminalAirport = TerminalAirport;
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

	public City getTerminalCity() {
		return TerminalCity;
	}


	public Airport getTerminalAirport() {
		return TerminalAirport;
	}

	@Override
	public Boolean isCity() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Boolean isAirport() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Boolean isTerminal() {
		// TODO Auto-generated method stub
		return true;
	}

	
	
	
	
}
