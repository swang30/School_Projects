package cmsc420.sortedmap;

import java.util.Comparator;

public class AvlComparator implements Comparator<String>  {
	public int compare(String city1, String city2) {
		if(city1.compareTo(city2) > 0 ) {
			return 1;
		} 
		else if(city1.compareTo(city2) < 0) {
			return -1;
		}
		else {
			return 0;
		}
	}
}
