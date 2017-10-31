package cmsc420.pmquadtree;

import cmsc420.meeshquest.part4.City;
import cmsc420.meeshquest.part4.Road;
import cmsc420.meeshquest.part4.Terminal;
import cmsc420.pmquadtree.PMQuadTree.PMBlack;
import cmsc420.pmquadtree.PMQuadTree.PMWhite;

public class PM1Validator implements Validator{

	@Override
	public boolean valid(PMBlack node) {
		// TODO Auto-generated method stub
//		if((node.cityList.size() + node.airportList.size() + node.terminalList.size()) > 1) {
//			return false;
//		} 
//		else if(node.roadList.size() > 1 && node.cityList.isEmpty()) {
//			return false;
//		}
//		else if((node.roadList.size() + node.airportList.size()) > 1) {
//			return false;
//		}
//		else if( (node.roadList.size() + node.cityList.size()) > 1 ){
//			City c = node.cityList.get(0);
//			for(Road r : node.roadList) {
//				if(!r.start.getName().equals(c.getName()) && !r.end.getName().equals(c.getName())) {
//					return false;
//				}
//			}
//		}
//		return true;
		int count = (node.cityList.size() + node.airportList.size() + node.terminalList.size());
		if (count < 2) {
			if(node.roadList.isEmpty()) {
				return true;
			} else {
				for(Road r : node.roadList) {
					for(City c : node.cityList) {
						if(r.start.getName() != c.getName() && r.end.getName() != c.getName()) {
							count++;
						}
					}
					for(Terminal t : node.terminalList) {
						if(r.start.getName() != t.getName() && r.end.getName() != t.getName()) {
							count++;
						}
					}
				}
				if(count == 0 && node.roadList.size() > 1) {
					return false;
				}
				return (count < 2);
			}
		}
		return false;
	}

	@Override
	public boolean valid(PMWhite node) {
		// TODO Auto-generated method stub
		return true;
	}

}
