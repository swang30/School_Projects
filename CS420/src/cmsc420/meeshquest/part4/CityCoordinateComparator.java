package cmsc420.meeshquest.part4;

import java.awt.geom.Point2D;
import java.util.Comparator;

public class CityCoordinateComparator implements Comparator<City> {

	/*
	 * Compare y coordinates first, if same, then compare x coordinate.
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	public int compare(City c1, City c2) {
		if(c1.getRemoteY() > c2.getRemoteY()) {
			return 1;
		} 
		else if(c1.getRemoteY() < c2.getRemoteY()) {
			return -1;
		}
		else {
			if(c1.getRemoteX() > c2.getRemoteX()) {
				return 1;
			}
			else if(c1.getRemoteX() < c2.getRemoteX()) {
				return -1;
			}
			else {
				if(c1.getY() > c2.getY()) {
					return 1;
				} 
				else if(c1.getY() < c2.getY()) {
					return -1;
				}
				else {
					if(c1.getX() > c2.getX()) {
						return 1;
					}
					else if(c1.getX() < c2.getX()) {
						return -1;
					}
					else {
						return 0;
					}
				}
			}
		}
	}
}
