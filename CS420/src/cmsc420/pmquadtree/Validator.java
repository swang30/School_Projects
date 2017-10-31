package cmsc420.pmquadtree;

import cmsc420.pmquadtree.PMQuadTree.PMBlack;
import cmsc420.pmquadtree.PMQuadTree.PMWhite;

public interface Validator {
	public boolean valid(PMBlack node);
	public boolean valid(PMWhite node);
}
