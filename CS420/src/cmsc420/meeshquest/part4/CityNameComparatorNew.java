package cmsc420.meeshquest.part4;

import java.util.Comparator;

public class CityNameComparatorNew implements Comparator<City> {

	/* (non-Javadoc)
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	@Override
	public int compare(City city1, City city2) {
		return city2.getName().compareTo(city1.getName());
	}
}