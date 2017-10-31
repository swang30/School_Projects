package cmsc420.meeshquest.part4;

import java.awt.geom.Point2D;
import java.util.Comparator;

public class LocationNameComparator implements Comparator<Location> {

	@Override
	public int compare(Location o1, Location o2) {
		// TODO Auto-generated method stub
		return o1.getName().compareTo(o2.getName());
	}

}
