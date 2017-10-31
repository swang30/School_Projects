package cmsc420.pmquadtree;

import cmsc420.pmquadtree.PMQuadTree.PMBlack;
import cmsc420.pmquadtree.PMQuadTree.PMWhite;

public class PM3Validator implements Validator {

	@Override
	public boolean valid(PMBlack node) {
		// TODO Auto-generated method stub
		return (node.cityList.size() + node.airportList.size() + node.terminalList.size()) < 2;
	}

	@Override
	public boolean valid(PMWhite node) {
		// TODO Auto-generated method stub
		return true;
	}
	
}
