package cmsc420.pmquadtree;


import java.awt.Color;

import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Float;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import cmsc420.drawing.CanvasPlus;
import cmsc420.geom.Inclusive2DIntersectionVerifier;
import cmsc420.meeshquest.part4.Airport;
import cmsc420.meeshquest.part4.City;
import cmsc420.meeshquest.part4.CityNameComparatorNew;
import cmsc420.meeshquest.part4.Location;
import cmsc420.meeshquest.part4.Point2DCoordinateComparator;
import cmsc420.meeshquest.part4.Road;
import cmsc420.meeshquest.part4.RoadNameComparator;
import cmsc420.meeshquest.part4.Terminal;

public class PMQuadTree {


	private int spatialWidth, spatialHeight;
	public PMNode root;
	private Point2D.Float origin;
	private Validator validator;
	private CanvasPlus canvas;
	private Rectangle2D.Float mapSize;
	
	public List<Road> roadListAll;
	public TreeSet<City> cityListAll;
	public TreeSet<Terminal> terminalListAll;
	public TreeSet<Airport> airportListAll;
	
//	public boolean ifAirportSmallestSize = false;
//	public boolean ifTerminalSmallestSize = false;
//	public boolean ifRoadSmallestSize = false;

	public boolean ifSmallestSize = false;

	public abstract class PMNode {
		
		protected static final int WHITE = 0;
		protected static final int BLACK = 1;
		protected static final int GRAY = 2;

		public int type;

		public PMNode(int type) {
			this.type = type;
		}
		
		public PMNode mapRoad(Road road, Point2D.Float origin, int width, int height) {
			throw new UnsupportedOperationException();
		}
		
		public PMNode mapTerminal(Terminal terminal, Point2D.Float origin, int width, int height) {
			throw new UnsupportedOperationException();
		}
		
		public PMNode mapAirport(Airport airport, Point2D.Float origin, int width, int height) {
			throw new UnsupportedOperationException();
		}
		
		public PMNode unmapRoad(Road road, Point2D.Float origin, int width, int height) {
			throw new UnsupportedOperationException();
		}
		
		public PMNode unmapTerminal(Terminal terminal, Point2D.Float origin, int width, int height) {
			throw new UnsupportedOperationException();
		}
		
		public PMNode unmapAirport(Airport airport, Point2D.Float origin, int width, int height) {
			throw new UnsupportedOperationException();
		}
		
	}

	class PMWhite extends PMNode {

		public PMWhite() {
			super(WHITE);
		}


		@Override
		public PMNode mapRoad(Road road, Float origin, int width, int height) {
			// TODO Auto-generated method stub
			PMBlack black = new PMBlack();
			return black.mapRoad(road, origin, width, height);
		}
		
		@Override
		public PMNode mapTerminal(Terminal terminal, Point2D.Float origin, int width, int height) {
			PMBlack black = new PMBlack();
			return black.mapTerminal(terminal, origin, width, height);
		}
		
		@Override
		public PMNode mapAirport(Airport airport, Point2D.Float origin, int width, int height) {
			PMBlack black = new PMBlack();
			return black.mapAirport(airport, origin, width, height);
		}
		
		@Override
		public PMNode unmapRoad(Road road, Point2D.Float origin, int width, int height) {
			return this;
		}
		
		@Override
		public PMNode unmapTerminal(Terminal terminal, Point2D.Float origin, int width, int height) {
			return this;
		}
		
		@Override
		public PMNode unmapAirport(Airport airport, Point2D.Float origin, int width, int height) {
			return this;
		}
	}
	
	public class PMGray extends PMNode {

		public PMNode[] children;
		public Rectangle2D.Float[] regions;
		public Point2D.Float origin;
		public Point2D.Float[] child_origin;
		public int halfW;
		public int halfH;
		
		private List<City> grayCityListAll = new ArrayList<City>();
		private List<Road> grayRoadListAll = new ArrayList<Road>();
		private List<Airport> grayAirportListAll = new ArrayList<Airport>();
		private List<Terminal> grayTerminalListAll = new ArrayList<Terminal>();

		public PMGray(Point2D.Float origin, int width, int height) {
			super(GRAY);
			this.origin = origin;

			this.children = new PMNode[4];
			for(int i = 0; i < 4; i++) {
				this.children[i] = new PMWhite();
			}

			this.halfW = width/2;
			this.halfH = height/2;

			this.child_origin = new Point2D.Float[4];
			this.child_origin[0] = new Point2D.Float(origin.x, origin.y + halfH);
			this.child_origin[1] = new Point2D.Float(origin.x + halfW, origin.y + halfH);
			this.child_origin[2] = new Point2D.Float(origin.x, origin.y);
			this.child_origin[3] = new Point2D.Float(origin.x + halfW, origin.y);

			regions = new Rectangle2D.Float[4];
			for (int i = 0; i < 4; i++) {
				regions[i] = new Rectangle2D.Float(child_origin[i].x, child_origin[i].y,
						halfW, halfH);
			}
		}


		@Override
		public PMNode mapRoad(Road road, Float origin, int width, int height) {
			// TODO Auto-generated method stub
			for(int i = 0; i < 4; i++) {
				if (Inclusive2DIntersectionVerifier.intersects(road, this.regions[i])) {
					this.children[i] = this.children[i].mapRoad(road, this.child_origin[i], halfW, halfH);
				}
			}
			Rectangle2D.Float grayRegion = new Rectangle2D.Float(origin.x, origin.y, width, height);
			
			
			if(!grayRoadListAll.contains(road)) {
				grayRoadListAll.add(road);
			}
			if(Inclusive2DIntersectionVerifier.intersects(road.start, grayRegion)) {
				if(road.start.isCity()) {
					City sCity = (City) road.start;
					if(!grayCityListAll.contains(sCity)) {
						grayCityListAll.add(sCity);
					}
				} else {
					Terminal sTerminal = (Terminal) road.start;
					if(!grayTerminalListAll.contains(sTerminal)) {
						grayTerminalListAll.add(sTerminal);
					}
				}
			}
			
			if(Inclusive2DIntersectionVerifier.intersects(road.end, grayRegion)) {
				if(road.end.isCity()) {
					City eCity = (City) road.end;
					if(!grayCityListAll.contains(eCity)) {
						grayCityListAll.add(eCity);
					}
				} else {
					Terminal eTerminal = (Terminal) road.end;
					if(!grayTerminalListAll.contains(eTerminal)) {
						grayTerminalListAll.add(eTerminal);
					}
				}
			}

			
			return this;
		}
		
		@Override
		public PMNode unmapRoad(Road road, Float origin, int width, int height) {
			boolean ifAllWhite = true;
			for(int i = 0; i < 4; i++) {
				if (Inclusive2DIntersectionVerifier.intersects(road, this.regions[i])) {
					this.children[i] = this.children[i].unmapRoad(road, this.child_origin[i], halfW, halfH);
				}
				if(this.children[i].type != 0) {
					ifAllWhite = false;
				}
			}
			
			Rectangle2D.Float grayRegion = new Rectangle2D.Float(origin.x, origin.y, width, height);
			
			if(grayRoadListAll.contains(road)) {
				grayRoadListAll.remove(road);
			}
			if(Inclusive2DIntersectionVerifier.intersects(road.start, grayRegion)) {
				if(road.start.isCity()) {
					City sCity = (City) road.start;
					if(grayCityListAll.contains(sCity) && numberOfRoadsCityConnects(sCity) == 0) {
						grayCityListAll.remove(sCity);
					}
				} else {
					Terminal sTerminal = (Terminal) road.start;
					if(grayTerminalListAll.contains(sTerminal) && numberOfRoadsCityConnects(sTerminal) == 0) {
						grayTerminalListAll.remove(sTerminal);
					}
				}
			}
			
			if(Inclusive2DIntersectionVerifier.intersects(road.end, grayRegion)) {
				if(road.end.isCity()) {
					City eCity = (City) road.end;
					if(grayCityListAll.contains(eCity) && numberOfRoadsCityConnects(eCity) == 0) {
						grayCityListAll.remove(eCity);
					}
				} else {
					Terminal eTerminal = (Terminal) road.end;
					if(grayTerminalListAll.contains(eTerminal) && numberOfRoadsCityConnects(eTerminal) == 0) {
						grayTerminalListAll.remove(eTerminal);
					}
				}
			}

			PMBlack newNode = new PMBlack();
			newNode.airportList.addAll(grayAirportListAll);
			newNode.cityList.addAll(grayCityListAll);
			newNode.roadList.addAll(grayRoadListAll);
			newNode.terminalList.addAll(grayTerminalListAll);
			
			if(ifAllWhite == true) {
				return new PMWhite();
			} 
			else if(validator.valid(newNode)) {
				return newNode;
			}
			else {
				return this;
			}
		}
				
		@Override
		public PMNode mapTerminal(Terminal terminal, Float origin, int width, int height) {
			// TODO Auto-generated method stub
			for(int i = 0; i < 4; i++) {
				if (Inclusive2DIntersectionVerifier.intersects(terminal, this.regions[i])) {
					this.children[i] = this.children[i].mapTerminal(terminal, this.child_origin[i], halfW, halfH);		
				}
			}
			if(!grayTerminalListAll.contains(terminal)) {
				grayTerminalListAll.add(terminal);
			}
			return this;
		}
		
		@Override
		public PMNode unmapTerminal(Terminal terminal, Point2D.Float origin, int width, int height) {
			boolean ifAllWhite = true;
			for(int i = 0; i < 4; i++) {
				if (Inclusive2DIntersectionVerifier.intersects(terminal, this.regions[i])) {
					this.children[i] = this.children[i].unmapTerminal(terminal, this.child_origin[i], halfW, halfH);		
				}
				if(this.children[i].type != 0) {
					ifAllWhite = false;
				}
			}
			if(grayTerminalListAll.contains(terminal)) {
				grayTerminalListAll.remove(terminal);
			}
			
			PMBlack newNode = new PMBlack();
			newNode.airportList.addAll(grayAirportListAll);
			newNode.cityList.addAll(grayCityListAll);
			newNode.roadList.addAll(grayRoadListAll);
			newNode.terminalList.addAll(grayTerminalListAll);
			
			if(ifAllWhite == true) {
				return new PMWhite();
			} 
			else if(validator.valid(newNode)) {
				return newNode;
			}
			else {
				return this;
			}
		}
		
		@Override
		public PMNode mapAirport(Airport airport, Float origin, int width, int height) {
			// TODO Auto-generated method stub
			for(int i = 0; i < 4; i++) {
				if (Inclusive2DIntersectionVerifier.intersects(airport, this.regions[i])) {
					this.children[i] = this.children[i].mapAirport(airport, this.child_origin[i], halfW, halfH);
				}
			}
			if(!grayAirportListAll.contains(airport)) {
				grayAirportListAll.add(airport);
			}
			return this;
		}
		
		@Override
		public PMNode unmapAirport(Airport airport, Point2D.Float origin, int width, int height) {
			boolean ifAllWhite = true;
			for(int i = 0; i < 4; i++) {
				if (Inclusive2DIntersectionVerifier.intersects(airport, this.regions[i])) {
					this.children[i] = this.children[i].unmapAirport(airport, this.child_origin[i], halfW, halfH);		
				}
				if(this.children[i].type != 0) {
					ifAllWhite = false;
				}
			}
			if(grayAirportListAll.contains(airport)) {
				grayAirportListAll.remove(airport);
			}
			
			PMBlack newNode = new PMBlack();
			newNode.airportList.addAll(grayAirportListAll);
			newNode.cityList.addAll(grayCityListAll);
			newNode.roadList.addAll(grayRoadListAll);
			newNode.terminalList.addAll(grayTerminalListAll);
			
			if(ifAllWhite == true) {
				return new PMWhite();
			} 
			else if(validator.valid(newNode)) {
				return newNode;
			}
			else {
				return this;
			}
		}
		
	}

	public class PMBlack extends PMNode {

		public List<City> cityList;
		public TreeSet<Road> roadList;
		public TreeSet<Terminal> terminalList;
		public TreeSet<Airport> airportList;

		public PMBlack() {
			super(BLACK);
			this.cityList = new ArrayList<City>();
			this.roadList = new TreeSet<Road>(new RoadNameComparator());
			this.terminalList = new TreeSet<Terminal>(new Point2DCoordinateComparator());
			this.airportList = new TreeSet<Airport>(new Point2DCoordinateComparator());
		}

		@Override
		public PMNode mapRoad(Road road, Float origin, int width, int height) {
			this.roadList.add(road);
			//add to cityList
			Rectangle2D.Float region = new Rectangle2D.Float(origin.x, origin.y, width, height);
			if(Inclusive2DIntersectionVerifier.intersects(road.start, region)) {
				if(road.start.isCity()) {
					if(!cityList.contains(road.start)) {
						this.cityList.add( (City) road.start);
						canvas.addPoint(road.start.getName(), road.start.x, road.start.y, Color.black);
					}
				}
				if(!road.start.isCity()) {
					if(!terminalList.contains(road.start)) {
						this.terminalList.add( (Terminal) road.start);
						canvas.addPoint(road.start.getName(), road.start.x, road.start.y, Color.black);
					}
				}
			}
			if(Inclusive2DIntersectionVerifier.intersects(road.end, region)) {
				if(road.end.isCity()) {
					if(!cityList.contains(road.end)) {
						this.cityList.add( (City) road.end);
						canvas.addPoint(road.end.getName(), road.end.x, road.end.y, Color.black);
					}
				}
				if(!road.end.isCity()) {
					if(!terminalList.contains(road.end)) {
						this.terminalList.add( (Terminal) road.end);
						canvas.addPoint(road.end.getName(), road.end.x, road.end.y, Color.black);
					}
				}
			}
			if(validator.valid(this)) {
				return this;
			} else {
				if(width != 1 && height != 1) {
					return partition(origin, width, height);
				} else {
					ifSmallestSize = true;
					return this;
				}	
			}
		}
		
		@Override
		public PMNode unmapRoad(Road road, Float origin, int width, int height) {
			this.roadList.remove(road);
			
			Rectangle2D.Float region = new Rectangle2D.Float(origin.x, origin.y, width, height);
			if(Inclusive2DIntersectionVerifier.intersects(road.start, region)) {
				if(road.start.isCity()) {
					if(numberOfRoadsCityConnects( (City) road.start) == 0) {  // no other road connects this city
						this.cityList.remove( (City) road.start);
						canvas.removePoint(road.start.getName(), road.start.x, road.start.y, Color.black);
					}
				} else {
					this.terminalList.remove( (Terminal) road.start );
					canvas.removePoint(road.start.getName(), road.start.x, road.start.y, Color.black);
				}
			}
			if(Inclusive2DIntersectionVerifier.intersects(road.end, region)) {
				if(road.end.isCity()) {
					if(numberOfRoadsCityConnects( (City) road.end) == 0) {  // no other road connects this city
						this.cityList.remove( (City) road.end);
						canvas.removePoint(road.end.getName(), road.start.x, road.start.y, Color.black);
					}
				} else {
					this.terminalList.remove( (Terminal) road.end);
					canvas.removePoint(road.end.getName(), road.start.x, road.start.y, Color.black);
				}
			}
			if(this.getCardinality() == 0) {
				return new PMWhite();
			} else {
				return this;
			}
		}

		@Override
		public PMNode mapTerminal(Terminal terminal, Float origin, int width, int height) {
			
			this.terminalList.add(terminal);
			
			if(validator.valid(this)) {
				return this;
			} else {
				if(width != 1 && height != 1) {
					return partition(origin, width, height);
				} else {
					ifSmallestSize = true;
					return this;
				}	
			}
		}
		
		@Override
		public PMNode unmapTerminal(Terminal terminal, Float origin, int width, int height) {
			this.terminalList.remove(terminal);
			if(this.getCardinality() == 0) {
				return new PMWhite();
			} else {
				return this;
			}
		}
		
		@Override
		public PMNode mapAirport(Airport airport, Float origin, int width, int height) {
			this.airportList.add(airport);
			
//			System.out.println(origin.toString() + (this.getCardinality() - this.roadList.size()));
			
			if(validator.valid(this)) {
				return this;
			} else {
				if(width != 1 && height != 1) {
					return partition(origin, width, height);
				} else {
					ifSmallestSize = true;
					return this;
				}	
			}
		}
		
		@Override
		public PMNode unmapAirport(Airport airport, Float origin, int width, int height) {
			this.airportList.remove(airport);
			if(this.getCardinality() == 0) {
				return new PMWhite();
			} else {
				return this;
			}
		}
		
		

		private PMNode partition(Point2D.Float origin, int width, int height) {
			PMNode gray = new PMGray(origin, width, height);
			for(Terminal terminal : this.terminalList) {
				gray.mapTerminal(terminal, origin, width, height);
			}
			for(Airport airport : this.airportList) {
				gray.mapAirport(airport, origin, width, height);
			}
			for(Road road : this.roadList) {
				gray.mapRoad(road, origin, width, height);
			}
			
			return gray;
		}
		
		public int getCardinality() {
			return cityList.size() + airportList.size() + terminalList.size() + roadList.size();
		}

	}


	public PMQuadTree(int spatialWidth, int spatialHeight, Validator validator, CanvasPlus canvas) {
		root = new PMWhite();
		this.spatialWidth = spatialWidth;
		this.spatialHeight = spatialHeight;
		this.origin = new Point2D.Float(0.0f, 0.0f);
		this.validator = validator;
		this.roadListAll = new ArrayList<Road>();
		this.cityListAll = new TreeSet<City>(new CityNameComparatorNew());
		this.terminalListAll = new TreeSet<Terminal>(new Point2DCoordinateComparator());
		this.airportListAll = new TreeSet<Airport>(new Point2DCoordinateComparator());
		this.canvas = canvas;
		this.mapSize = new Rectangle2D.Float(this.origin.x, this.origin.y, this.spatialWidth, this.spatialHeight);
	}

	public City findCity(City city) {
		for(Road r : this.roadListAll) {
			if(city.getName().equals(r.start.getName()) || city.getName().equals(r.end.getName())) {
				return city;
			}
		}
		return null;
	}

	public void mapRoad(Road road) {
		this.roadListAll.add(road);
		if(Inclusive2DIntersectionVerifier.intersects(road.start, this.mapSize)) {
			if(road.start.isCity()) {
				this.cityListAll.add( (City) road.start);
			} else {
				this.terminalListAll.add( (Terminal) road.start);
			}
			
		}
		if(Inclusive2DIntersectionVerifier.intersects(road.end, this.mapSize)) {
			if(road.end.isCity()) {
				this.cityListAll.add( (City) road.end);
			} else {
				this.terminalListAll.add( (Terminal) road.end);
			}
		}
		
		root = root.mapRoad(road, origin, spatialWidth, spatialHeight);
		canvas.addLine(road.start.x, road.start.y, road.end.x, road.end.y, Color.black);
	}
	
	public void unmapRoad(Road road) {
		this.roadListAll.remove(road);
		if(road.start.isCity()) {
			if(numberOfRoadsCityConnects( (City) road.start) == 0) {
				this.cityListAll.remove(road.start);
				canvas.removePoint(road.start.getName(), road.start.x, road.start.y, Color.black);
			}
		} else {
			this.terminalListAll.remove(road.start);
			canvas.removePoint(road.start.getName(), road.start.x, road.start.y, Color.black);
		}
		if(road.end.isCity()) {
			if(numberOfRoadsCityConnects( (City) road.end) == 0) {
				this.cityListAll.remove(road.end);
				canvas.removePoint(road.end.getName(), road.end.x, road.end.y, Color.black);
			}
		} else {
			this.terminalListAll.remove(road.end);
			canvas.removePoint(road.end.getName(), road.end.x, road.end.y, Color.black);
		}
		root = root.unmapRoad(road, origin, spatialWidth, spatialHeight);
		canvas.removeLine(road.start.getLocalX(), road.start.getLocalY(), road.end.getLocalX(), road.end.getLocalY(), Color.black);
	}
	
	private int numberOfRoadsCityConnects(Location c) {
		int count = 0;
		for(Road r : this.roadListAll) {
			if(r.start.getName().equals(c.getName()) || r.end.getName().equals(c.getName())) {
				count++;
			}
		}
		return count;
	}
	
	public void mapTerminal(Terminal terminal) {
		this.terminalListAll.add(terminal);
		terminal.getTerminalAirport().terminalSet.add(terminal);
		this.root = root.mapTerminal(terminal, origin, spatialWidth, spatialHeight);
//		Road terminalRoad = new Road(terminal, terminal.getTerminalCity());
//		this.root = root.mapRoad(terminalRoad, origin, spatialWidth, spatialHeight);
		
		canvas.addPoint(terminal.getName(), terminal.getLocalX(), terminal.getLocalY(), Color.blue);
//		canvas.addLine(terminalRoad.start.getLocalX(), terminalRoad.start.getLocalY(), terminalRoad.end.getLocalX(), 
//				terminalRoad.end.getLocalY(), Color.BLACK);
	}
	
	public void unmapTerminal(Terminal terminal) {
		this.terminalListAll.remove(terminal);
		terminal.getTerminalAirport().terminalSet.remove(terminal);
		this.root = this.root.unmapTerminal(terminal, origin, spatialWidth, spatialHeight);
		Road terminalRoad = new Road(terminal, terminal.getTerminalCity());
		this.roadListAll.remove(terminalRoad);
		this.root = root.unmapRoad(terminalRoad, origin, spatialWidth, spatialHeight);
		canvas.removePoint(terminal.getName(), terminal.getLocalX(), terminal.getLocalY(), Color.blue);
		canvas.removeLine(terminalRoad.start.getLocalX(), terminalRoad.start.getLocalY(), terminalRoad.end.getLocalX(), 
				terminalRoad.end.getLocalY(), Color.BLACK);
	}
	
	public void mapAirport(Airport airport, Terminal terminal) {
		this.airportListAll.add(airport);
		airport.terminalSet.add(terminal);
//		this.root = root.mapTerminal(terminal, origin, spatialWidth, spatialHeight);
		this.root = root.mapAirport(airport, origin, spatialWidth, spatialHeight);
		canvas.addPoint(airport.getName(), airport.getX(), airport.getY(), Color.red);
	}
	
	public void unmapAirport(Airport airport) {
		this.airportListAll.remove(airport);
		this.root = this.root.unmapAirport(airport, origin, spatialWidth, spatialHeight);
//		for(Terminal t : airport.terminalSet) {
//			this.root = this.root.unmapTerminal(t, origin, spatialWidth, spatialHeight);
//		}
//		canvas.removePoint(airport.getName(), airport.getX(), airport.getY(), Color.red);
	}
	
	public boolean roadInBound(Road road) {
		if(Inclusive2DIntersectionVerifier.intersects(road, this.mapSize)) {
			return true;
		} 
		return false;
	}

	public boolean containRoad(Road road) {
		for (Road r : this.roadListAll) {
			if(r.start.getName().equals(road.start.getName()) && r.end.getName().equals(road.end.getName())) {
				return true;
			}
		}
		return false;
	}
	
	public boolean containCity(City city) {
		for(City c : this.cityListAll) {
			if(c.getName().equals(city.getName())) {
				return true;
			}
		}
		
		return false;
	}
	
	public int getNumCities() {
		return this.cityListAll.size();
	}
	
	public boolean roadIntersect(Road road) {
		for(Road r : this.roadListAll) {
			if(Inclusive2DIntersectionVerifier.intersects(r, road)) {
				if(Inclusive2DIntersectionVerifier.intersects(r.start, road) && Inclusive2DIntersectionVerifier.intersects(r.end, road)) {
					return true;
				}
				if(Inclusive2DIntersectionVerifier.intersects(road.start, r) && Inclusive2DIntersectionVerifier.intersects(road.end, r)) {
					return true;
				}
				if(r.start.getName() != road.start.getName() && r.start.getName() != road.end.getName()
						&& r.end.getName() != road.start.getName() && r.end.getName() != road.end.getName()) {
					return true;
				}			
			}
		}
		return false;
	}
	
	public String getValidatorName() {
		return this.validator.toString();
	}

}
