package cmsc420.meeshquest.part4;

import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Float;
import java.util.Comparator;

public class Point2DCoordinateComparator implements Comparator<Point2D.Float> {

	@Override
	public int compare(Point2D.Float p1, Point2D.Float p2) {
		// TODO Auto-generated method stub
		if(p1.getY() > p2.getY()) {
			return 1;
		}
		else if(p1.getY() < p2.getY()) {
			return -1;
		}
		else {
			if(p1.getX() > p2.getX()) {
				return 1;
			}
			else if(p1.getX() < p2.getX()) {
				return -1;
			}
			else {
				return 0;
			}
		}
	}
	
}
