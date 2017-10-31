package cmsc420.prquadtree;

import cmsc420.meeshquest.part4.City;

public class PRQuadLeaf extends PRQuadNode {
	
	private City newCity;

	public PRQuadLeaf(City newCity) {
		this.newCity = newCity;
		
	}
	
	@Override
	public boolean isLeaf() {
		// TODO Auto-generated method stub
		return true;
	}
	
	

	public City getNewCity() {
		return newCity;
	}

	public void setNewCity(City newCity) {
		this.newCity = newCity;
	}
	
	
	
	
}
