package cmsc420.meeshquest.part4;

import java.awt.geom.Line2D;

public class Road extends Line2D.Float {
	public Location start;
	public Location end;
	private double distance;
	private CityNameComparatorNew comp = new CityNameComparatorNew();
	
	public Road(Location start, Location end) {
//		City a = new City("city41", 0, "black", 342, 25);
//		City b = new City("city34", 0, "black", 420, 249);
//		System.out.println(comp.compare(a, b));
		if(start.getName().compareTo(end.getName()) < 0) {
			this.start = start;
			this.end = end;
		} else {
			this.start = end;
			this.end = start;
		}
//		if(start.getName().compareTo(end.getName()) > 0) {
//			this.start = start;
//			this.end = end;
//		} else {
//			this.start = end;
//			this.end = start;
//		}
		this.x1 = this.start.x;
		this.x2 = this.end.x;
		this.y1 = this.start.y;
		this.y2 = this.end.y;
		this.distance = start.distance(end);
	}
	
	public String getRoadName() {
		return "start: " + start.getName() + "--> end: " + end.getName();
	}

	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj != null && (obj.getClass().equals(this.getClass()))) {
			Road r = (Road) obj;
			return (start.equals(r.start) && end.equals(r.end));
		}
		return false;
	}
	
	/**
	 * Returns the hash code value of a road.
	 */
	public int hashCode() {
		int hash = 35;
		hash = 37 * hash + start.hashCode();
		hash = 37 * hash + end.hashCode();
		return hash;
	}
	
	
	
}
