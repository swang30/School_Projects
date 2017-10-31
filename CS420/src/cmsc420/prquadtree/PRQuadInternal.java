package cmsc420.prquadtree;

public class PRQuadInternal extends PRQuadNode {
	
	public int width, height;
	public int x, y;
	public PRQuadNode NE, NW, SE, SW;
	
	public PRQuadInternal(int width, int height, int x, int y) {
		this.width = width;
		this.height = height;
		this.x = x;
		this.y = y;
		
		this.NE = this.NW = this.SE = this.SW = null;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getX() {
		return this.x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public PRQuadNode getNE() {
		return NE;
	}

	public void setNE(PRQuadNode nE) {
		NE = nE;
	}

	public PRQuadNode getNW() {
		return NW;
	}

	public void setNW(PRQuadNode nW) {
		NW = nW;
	}

	public PRQuadNode getSE() {
		return SE;
	}

	public void setSE(PRQuadNode sE) {
		SE = sE;
	}

	public PRQuadNode getSW() {
		return SW;
	}

	public void setSW(PRQuadNode sW) {
		SW = sW;
	}

	@Override
	public boolean isLeaf() {
		// TODO Auto-generated method stub
		return false;
	}
	
	
	
}
