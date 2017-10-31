package cmsc420.prquadtree;

import java.awt.Color;

import cmsc420.drawing.CanvasPlus;
import cmsc420.meeshquest.part4.City;

public class PRQuadtree {
	
	CanvasPlus canvas;
	int spatialHeight;
	int spatialWidth;
	
	public PRQuadtree(CanvasPlus canvas, int height, int width) {
		this.canvas = canvas;
		this.spatialHeight = height;
		this.spatialWidth = width;
	}
	
	protected PRQuadNode root = null;
	
	public City insert(City city) {
		return insert(city, root);
	}
	
	private City insert(City newCity, PRQuadNode rootNode) {
		
		if(rootNode == null) { //first base case: the tree is empty
			canvas.addPoint(newCity.getName(), newCity.getX(), newCity.getY(), Color.BLACK);
			this.root = new PRQuadLeaf(newCity);
		} 
		else if(rootNode.isLeaf()) {  //second base case: only one node in tree
			canvas.addLine(this.spatialWidth/2, 0, this.spatialWidth/2, this.spatialHeight, Color.black);
			canvas.addLine(0, this.spatialHeight/2, this.spatialWidth, this.spatialHeight/2 ,Color.black);
			
			PRQuadInternal grayNode = new PRQuadInternal(this.spatialWidth, this.spatialHeight, this.spatialWidth/2, this.spatialHeight/2);
			this.root = grayNode;
			PRQuadLeaf Node_toInsert = (PRQuadLeaf) rootNode;
			this.insert(Node_toInsert.getNewCity(), this.root);
			return this.insert(newCity, this.root);
		}
		else {
			// recursive step, rootNode is a gray node, find a child to insert recursively
			PRQuadInternal grayNode = (PRQuadInternal) rootNode;		
			
			int x_coor = (int) newCity.getX();
			int y_coor = (int) newCity.getY();
			
			// NW
			if(x_coor < grayNode.x && y_coor >= grayNode.y ) {
				if (grayNode.NW == null) {	

					grayNode.NW = new PRQuadLeaf(newCity);
					canvas.addPoint(newCity.getName(), newCity.getX(), newCity.getY(), Color.BLACK);
				} 
				else {
					if (grayNode.NW.isLeaf()) {	
						PRQuadLeaf city_toInsert = (PRQuadLeaf) grayNode.NW;

						grayNode.NW = new PRQuadInternal(grayNode.getWidth()/2, grayNode.getHeight()/2, grayNode.getX() - grayNode.getWidth()/4, grayNode.getY() + grayNode.getHeight()/4);
						this.insert(city_toInsert.getNewCity(), grayNode.NW);

						return this.insert(newCity, grayNode.NW);
					} 
					else {
						return this.insert(newCity, grayNode.NW);
					}
				}
			}				
			
			// NE
			if(x_coor >= grayNode.x && y_coor >= grayNode.y ) {
				if (grayNode.NE == null) {	
					grayNode.NE = new PRQuadLeaf(newCity);
					canvas.addPoint(newCity.getName(), newCity.getX(), newCity.getY(), Color.BLACK);
				} 
				else {
					if (grayNode.NE.isLeaf()) {				
						PRQuadLeaf city_toInsert = (PRQuadLeaf) grayNode.NE;

						grayNode.NE = new PRQuadInternal(grayNode.getWidth()/2, grayNode.getHeight()/2, grayNode.getX() + grayNode.getWidth()/4, grayNode.getY() + grayNode.getWidth()/4);
						this.insert(city_toInsert.getNewCity(), grayNode.NE);

						return this.insert(newCity, grayNode.NE);
					}
					else {
						return insert(newCity, grayNode.NE);
					}
				}
			}
			
			
			// SW
			if(x_coor < grayNode.x && y_coor < grayNode.y ) {
				if (grayNode.SW == null) {	
					grayNode.SW = new PRQuadLeaf(newCity);
					canvas.addPoint(newCity.getName(), newCity.getX(), newCity.getY(), Color.BLACK);
				} 
				else {
					if (grayNode.SW.isLeaf()) {

						PRQuadLeaf city_toInsert = (PRQuadLeaf) grayNode.SW;

						grayNode.SW = new PRQuadInternal(grayNode.getWidth()/2, grayNode.getHeight()/2, grayNode.getX() - grayNode.getWidth()/4, grayNode.getY() - grayNode.getWidth()/4);
						this.insert(city_toInsert.getNewCity(), grayNode.SW);

						return this.insert(newCity, grayNode.SW);
					} 
					else {
						return insert(newCity, grayNode.SW);
					}
				}
			}
					
			// SE
			if(x_coor >= grayNode.x && y_coor < grayNode.y ) {
				
				if (grayNode.SE == null) {	
					grayNode.SE = new PRQuadLeaf(newCity);
					canvas.addPoint(newCity.getName(), newCity.getX(), newCity.getY(), Color.BLACK);
				} 
				else {
					if (grayNode.SE.isLeaf()) {

						PRQuadLeaf city_toInsert = (PRQuadLeaf) grayNode.SE;

						grayNode.SE = new PRQuadInternal(grayNode.getWidth()/2, grayNode.getHeight()/2, grayNode.getX() + grayNode.getWidth()/4, grayNode.getY() - grayNode.getWidth()/4);
						this.insert(city_toInsert.getNewCity(), grayNode.SE);

						return this.insert(newCity, grayNode.SE);
					}
					else {
						return insert(newCity, grayNode.SE);
					}
				}
			}

		}
		
		return null;
	}
	
	
	public City findCity(City city) {
		return find(city, this.root);
	}
	
	private City find(City city, PRQuadNode currNode) {
		if(currNode == null) {
			return null;
		} 
		else if(currNode.isLeaf()) {
			PRQuadLeaf black = (PRQuadLeaf) currNode;
			if(city.getName() == black.getNewCity().getName()) {
				return black.getNewCity();
			}
		}
		else {
			PRQuadInternal gray = (PRQuadInternal) currNode;
			//NW
			if(city.getX() < gray.getX() && city.getY() >= gray.getY()) {
				return find(city, gray.NW);
			}			
			//NE
			else if(city.getX() >= gray.getX() && city.getY() >= gray.getY()) {
				return find(city, gray.NE);
			}
			//SW
			else if(city.getX() < gray.getX() && city.getY() < gray.getY()) {
				return find(city, gray.SW);
			}
			//SE
			else if(city.getX() > gray.getX() && city.getY() < gray.getY()) {
				return find(city, gray.SE);
			}
		}
		return null;
	}
	
	
	public PRQuadNode delete(City city) {
		return deleteHelper(city, this.root);
	}
	
	private PRQuadNode deleteHelper(City city, PRQuadNode currNode) {
		if(currNode.isLeaf()) {
			canvas.removePoint(city.getName(), city.getX(), city.getY(), Color.BLACK);
			return null;
		} else {
			PRQuadInternal grayNode = (PRQuadInternal) currNode;
			
			//NW
			if(city.getX() < grayNode.getX() && city.getY() >= grayNode.getY()) {
				PRQuadInternal temp = grayNode;
				
				temp.NW = this.deleteHelper(city, grayNode.NW);

				if(temp.NW != null && temp.NE == null && temp.SW == null && temp.SE == null) {
					if(temp.NW.isLeaf()) {
						return temp.NW;
					}
				}
				else if(temp.NW == null && temp.NE != null && temp.SW == null && temp.SE == null) {
					if(temp.NE.isLeaf()) {
						return temp.NE;
					}
				}
				else if(temp.NW == null && temp.NE == null && temp.SW != null && temp.SE == null) {
					if(temp.SW.isLeaf()) {
						return temp.SW;
					}	
				}
				else if(temp.NW == null && temp.NE == null && temp.SW == null && temp.SE != null) {
					if(temp.SE.isLeaf()) {
						return temp.SE;
					}
				}
				
				return temp;
			}
			
			//NE
			if(city.getX() >= grayNode.getX() && city.getY() >= grayNode.getY()) {
				PRQuadInternal temp = grayNode;
				temp.NE = this.deleteHelper(city, grayNode.NE);
				
				if(temp.NW != null && temp.NE == null && temp.SW == null && temp.SE == null) {
					if(temp.NW.isLeaf()) {
						return temp.NW;
					}
				}
				else if(temp.NW == null && temp.NE != null && temp.SW == null && temp.SE == null) {
					if(temp.NE.isLeaf()) {
						return temp.NE;
					}
				}
				else if(temp.NW == null && temp.NE == null && temp.SW != null && temp.SE == null) {
					if(temp.SW.isLeaf()) {
						return temp.SW;
					}	
				}
				else if(temp.NW == null && temp.NE == null && temp.SW == null && temp.SE != null) {
					if(temp.SE.isLeaf()) {
						return temp.SE;
					}
				}
				return temp;
			}
			
			//SW
			if(city.getX() < grayNode.getX() && city.getY() < grayNode.getY()) {
				PRQuadInternal temp = grayNode;
				temp.SW = this.deleteHelper(city, grayNode.SW);
				
				if(temp.NW != null && temp.NE == null && temp.SW == null && temp.SE == null) {
					if(temp.NW.isLeaf()) {
						return temp.NW;
					}
				}
				else if(temp.NW == null && temp.NE != null && temp.SW == null && temp.SE == null) {
					if(temp.NE.isLeaf()) {
						return temp.NE;
					}
				}
				else if(temp.NW == null && temp.NE == null && temp.SW != null && temp.SE == null) {
					if(temp.SW.isLeaf()) {
						return temp.SW;
					}	
				}
				else if(temp.NW == null && temp.NE == null && temp.SW == null && temp.SE != null) {
					if(temp.SE.isLeaf()) {
						return temp.SE;
					}
				}
				return temp;
			}
			
			//SE
			if(city.getX() >= grayNode.getX() && city.getY() < grayNode.getY()) {
				PRQuadInternal temp = grayNode;
				temp.SE = this.deleteHelper(city, grayNode.SE);
				
				if(temp.NW != null && temp.NE == null && temp.SW == null && temp.SE == null) {
					if(temp.NW.isLeaf()) {
						return temp.NW;
					}
				}
				else if(temp.NW == null && temp.NE != null && temp.SW == null && temp.SE == null) {
					if(temp.NE.isLeaf()) {
						return temp.NE;
					}
				}
				else if(temp.NW == null && temp.NE == null && temp.SW != null && temp.SE == null) {
					if(temp.SW.isLeaf()) {
						return temp.SW;
					}	
				}
				else if(temp.NW == null && temp.NE == null && temp.SW == null && temp.SE != null) {
					if(temp.SE.isLeaf()) {
						return temp.SE;
					}
				}
				return temp;
			}
		}
		
		
		return null;
	}
	
	
	public void setRoot(PRQuadNode root) {
		this.root = root;
	}
	
	
	
}
