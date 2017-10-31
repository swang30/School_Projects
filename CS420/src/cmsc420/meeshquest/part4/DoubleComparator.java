package cmsc420.meeshquest.part4;

import java.awt.geom.Point2D;
import java.util.Comparator;

public class DoubleComparator implements Comparator<Double> {

	@Override
	public int compare(Double o1, Double o2) {
		// TODO Auto-generated method stub
		if(o1 > o2) {
			return 1;
		} else if(o1 < o2) {
			return -1;
		} else {
			return 0;
		}
	}

}
