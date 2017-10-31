package cmsc420.meeshquest.part4;

import java.util.Comparator;

public class RoadNameComparator implements Comparator<Road> {

	@Override
	public int compare(Road r1, Road r2) {
		// TODO Auto-generated method stub
		if(r1.start.getName().compareTo(r2.start.getName()) > 0) {
			return -1;
		}
		else if(r1.start.getName().compareTo(r2.start.getName()) < 0) {
			return 1;
		}
		else {
			if(r1.end.getName().compareTo(r2.end.getName()) > 0) {
				return -1;
			}
			else if(r1.end.getName().compareTo(r2.end.getName()) < 0) {
				return 1;
			}
			else {
				return 0;
			}
		}
	}
	
}
