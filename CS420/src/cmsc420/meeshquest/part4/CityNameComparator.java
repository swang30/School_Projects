package cmsc420.meeshquest.part4;

import java.util.Comparator;

public class CityNameComparator implements Comparator<String> {

	/* (non-Javadoc)
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	@Override
	public int compare(String city1, String city2) {
		return city2.compareTo(city1);
	}
}
