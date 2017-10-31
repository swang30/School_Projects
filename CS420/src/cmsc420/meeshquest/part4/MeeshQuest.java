package cmsc420.meeshquest.part4;

import java.awt.Color;

import java.awt.Rectangle;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import cmsc420.drawing.CanvasPlus;
import cmsc420.geom.Inclusive2DIntersectionVerifier;
import cmsc420.pmquadtree.PM1Validator;
import cmsc420.pmquadtree.PM3Validator;
import cmsc420.pmquadtree.PMQuadTree;
import cmsc420.pmquadtree.PMQuadTree.PMBlack;
import cmsc420.pmquadtree.PMQuadTree.PMGray;
import cmsc420.pmquadtree.PMQuadTree.PMNode;
import cmsc420.prquadtree.PRQuadtree;
import cmsc420.sortedmap.AvlComparator;
import cmsc420.sortedmap.AvlGTree;
import cmsc420.sortedmap.AvlGTree.AvlNode;
import cmsc420.xml.XmlUtility;

public class MeeshQuest {

	private static TreeMap<String, City> Name_City = new TreeMap<String,City>(new CityNameComparator());
	private static TreeMap<City, Point2D.Float> Coor_City = new TreeMap<City, Point2D.Float>(new CityCoordinateComparator());
	private static TreeSet<Road> RoadSet = new TreeSet<Road>(new RoadNameComparator());
	private static TreeMap<String, Terminal> allTerminalMap = new TreeMap<String, Terminal>();
	private static TreeMap<String, Airport> allAirportMap = new TreeMap<String, Airport>();
	// Remote Coor --> (Terminal Set)  
	private static TreeMap<Point2D.Float, TreeSet<Terminal>> allTerminalCoorMap = new TreeMap<Point2D.Float, TreeSet<Terminal>>(new Point2DCoordinateComparator());
	// Remote Coor --> (Airport Set)  
	private static TreeMap<Point2D.Float, TreeSet<Airport>> allAirportCoorMap = new TreeMap<Point2D.Float, TreeSet<Airport>>(new Point2DCoordinateComparator());
	// Remote Coor --> (City Set)
	private static TreeMap<Point2D.Float, TreeSet<City>> allCityCoorMap = new TreeMap<Point2D.Float, TreeSet<City>>(new Point2DCoordinateComparator());
	
	//Remote Coor --> PMQT
	private static TreeMap<Point2D.Float, PMQuadTree> PMQTMap = new TreeMap<Point2D.Float, PMQuadTree>(new Point2DCoordinateComparator());
	
	private static PRQuadtree prQuadtree;
	private static AvlGTree<String, City> AvlGTree;
	private static int localWidth = 0;
	private static int localHeight = 0;
	private static int remoteWidth = 0;
	private static int remoteHeight = 0;
	private static int pmOrder;
	private static int g = 1;
	private static Document results = null;
	private static Element rootEle = null;
	private static CanvasPlus canvas = null;
	
	private static Rectangle2D.Float remoteMap = null;
	private static Rectangle2D.Float localMap = null;
	
    public static void main(String[] args) {
    	
        try {
//        	Document doc = XmlUtility.validateNoNamespace(System.in);
        	Document doc = XmlUtility.validateNoNamespace(new File("test1.xml"));
        	results = XmlUtility.getDocumentBuilder().newDocument();
        	
        	Element commandNode = doc.getDocumentElement();
        	
        	localWidth = Integer.parseInt(commandNode.getAttribute("localSpatialWidth"));
        	localHeight = Integer.parseInt(commandNode.getAttribute("localSpatialHeight"));
        	remoteWidth = Integer.parseInt(commandNode.getAttribute("remoteSpatialWidth"));
        	remoteHeight = Integer.parseInt(commandNode.getAttribute("remoteSpatialHeight"));
        	
        	canvas = new CanvasPlus("MeeshQuest", localWidth, localHeight);
        	canvas.addRectangle(0, 0, localWidth, localHeight, Color.black, false);
        	
        	remoteMap = new Rectangle2D.Float(0, 0, remoteWidth, remoteHeight);
        	localMap = new Rectangle2D.Float(0, 0, localWidth, localHeight);
        	
        	if(commandNode.getAttribute("pmOrder") != "") {
        		pmOrder = Integer.parseInt(commandNode.getAttribute("pmOrder"));
        	} else {
        		pmOrder = 3;
        	}
        	
        	prQuadtree = new PRQuadtree(canvas, remoteHeight, remoteWidth);
        	
//        	City a = new City("shanghai", 3, "white", 100, 100);
//        	PRQT.insert(a);
//        	City b = new City("Beijing", 3, "white", 50, 200);
//        	PRQT.insert(b);
//        	City c = new City("Guangzhou", 3, "white", 170, 100);
//        	PRQT.insert(c);
//        	City d = new City("Ningbo", 3, "white", 190, 200);
//        	PRQT.insert(d);
        	
        	int g = Integer.parseInt(commandNode.getAttribute("g"));
        	AvlGTree = new AvlGTree<String, City>(new AvlComparator(), g);
        	
        	/*Add root element*/
        	rootEle = results.createElement("results");
        	results.appendChild(rootEle);
        	

        	final NodeList nl = commandNode.getChildNodes();
        	for (int i = 0; i < nl.getLength(); i++) {
        		if (nl.item(i).getNodeType() == Document.ELEMENT_NODE) {
        			commandNode = (Element) nl.item(i);
                
        			/* TODO: Process your commandNode here */
        			if(commandNode.getNodeName().equals("createCity")) {
        				
        				/*create new City Object*/
        				int localx_coor = Integer.parseInt(commandNode.getAttribute("localX"));
        				int localy_coor = Integer.parseInt(commandNode.getAttribute("localY"));
        				int remotex_coor = Integer.parseInt(commandNode.getAttribute("remoteX"));
        				int remotey_coor = Integer.parseInt(commandNode.getAttribute("remoteY"));
        				int radius = Integer.parseInt(commandNode.getAttribute("radius"));
        				Point2D.Float coordinate = new Point2D.Float(localx_coor, localy_coor);
        				City newCity = new City(commandNode.getAttribute("name"), radius, commandNode.getAttribute("color"), 
        										localx_coor, localy_coor, remotex_coor, remotey_coor);
        				
    					Element parameters = results.createElement("parameters");
    					Element name = results.createElement("name");
    					name.setAttribute("value", commandNode.getAttribute("name"));
    					parameters.appendChild(name);
    					Element localX = results.createElement("localX");
    					localX.setAttribute("value", Integer.toString(localx_coor));
    					parameters.appendChild(localX);
    					Element localY = results.createElement("localY");
    					localY.setAttribute("value", Integer.toString(localy_coor));
    					parameters.appendChild(localY);
    					Element remoteX = results.createElement("remoteX");
    					remoteX.setAttribute("value", Integer.toString(remotex_coor));
    					parameters.appendChild(remoteX);
    					Element remoteY = results.createElement("remoteY");
    					remoteY.setAttribute("value", Integer.toString(remotey_coor));
    					parameters.appendChild(remoteY);

    					Point2D.Float remotePoint = new Point2D.Float(remotex_coor, remotey_coor);
    					Point2D.Float localPoint = new Point2D.Float(localx_coor, localy_coor);
    					
        				
        				//System.out.print(Coor_City.keySet());
        				
        				//Check for duplicateCityCoordinates
        				if( (allCityCoorMap.containsKey(remotePoint) && allCityCoorMap.get(remotePoint).contains(localPoint)) || 
        						(allAirportCoorMap.containsKey(remotePoint) && allAirportCoorMap.get(remotePoint).contains(localPoint)) ||
        						(allTerminalCoorMap.containsKey(remotePoint) && allTerminalCoorMap.get(remotePoint).contains(localPoint))) {
        					Element error = results.createElement("error");
        					error.setAttribute("type", "duplicateCityCoordinates");
        					rootEle.appendChild(error);
        					Element command = results.createElement("command");
        					command.setAttribute("name", "createCity");
        					if(commandNode.getAttribute("id") != "") {
        						command.setAttribute("id", commandNode.getAttribute("id"));
        					}
        					error.appendChild(command);

        					error.appendChild(parameters);


        					Element radiusEle = results.createElement("radius");
        					radiusEle.setAttribute("value", Integer.toString(radius));
        					parameters.appendChild(radiusEle);
        					Element color = results.createElement("color");
        					color.setAttribute("value", commandNode.getAttribute("color"));
        					parameters.appendChild(color);
        				}
        				/*Check for duplicate city name*/
        				else if(Name_City.containsKey(commandNode.getAttribute("name"))) {
        					
        					Element error = results.createElement("error");
        					error.setAttribute("type", "duplicateCityName");
        					rootEle.appendChild(error);
        					Element command = results.createElement("command");
        					command.setAttribute("name", "createCity");
        					if(commandNode.getAttribute("id") != "") {
        						command.setAttribute("id", commandNode.getAttribute("id"));
        					}
        					error.appendChild(command);
        					
        					error.appendChild(parameters);
        					
        					Element radiusEle = results.createElement("radius");
        					radiusEle.setAttribute("value", Integer.toString(radius));
        					parameters.appendChild(radiusEle);
        					Element color = results.createElement("color");
        					color.setAttribute("value", commandNode.getAttribute("color"));
        					parameters.appendChild(color);
        				}
        				/*no error, proceed here*/
        				else {
        					//Put city into Name_City treemap
            				Name_City.put(commandNode.getAttribute("name"), newCity);
            				Coor_City.put(newCity,coordinate);
            				if(allCityCoorMap.containsKey(remotePoint)) {
            					allCityCoorMap.get(remotePoint).add(newCity);
            				} else {
            					TreeSet<City> tempSet = new TreeSet<City>(new Point2DCoordinateComparator());
            					tempSet.add(newCity);
            					allCityCoorMap.put(remotePoint, tempSet);
            				}
            				
            				AvlGTree.put(commandNode.getAttribute("name"), newCity);
            				
        					//success
        					Element success = results.createElement("success");
        					rootEle.appendChild(success);
        					//command
        					Element command = results.createElement("command");
            				if(commandNode.getAttribute("id") != "") {
            					command.setAttribute("id", commandNode.getAttribute("id"));
            				}
        					command.setAttribute("name", "createCity");
        					success.appendChild(command);
        					
        					success.appendChild(parameters);
        					//name, x, y, radius, color
        					
        					Element radiusEle = results.createElement("radius");
        					radiusEle.setAttribute("value", Integer.toString(radius));
        					parameters.appendChild(radiusEle);
        					Element color = results.createElement("color");
        					color.setAttribute("value", commandNode.getAttribute("color"));
        					parameters.appendChild(color);
        					//output
        					Element output = results.createElement("output");
        					success.appendChild(output);
        					
        					if(!PMQTMap.containsKey(remotePoint)) {
        						PMQuadTree PMQT = null;
        						if(pmOrder == 1) {
        							PMQT = new PMQuadTree(localWidth, localHeight, new PM1Validator(), canvas);
        						} else {
        							PMQT = new PMQuadTree(localWidth, localHeight, new PM3Validator(), canvas);
        						}
        						PMQTMap.put(remotePoint, PMQT);
        					}
        				}	
        			}
        			
        			else if(commandNode.getNodeName().equals("listCities")) {
        				Element success = results.createElement("success");
        				
        				Element command = results.createElement("command");
        				if(commandNode.getAttribute("id") != "") {
        					command.setAttribute("id", commandNode.getAttribute("id"));
        				}
        				command.setAttribute("name", "listCities");
        				success.appendChild(command);
        				Element parameters = results.createElement("parameters");
        				success.appendChild(parameters);
        				Element sortBy = results.createElement("sortBy");
        				sortBy.setAttribute("value", commandNode.getAttribute("sortBy"));
        				parameters.appendChild(sortBy);
        				Element output = results.createElement("output");
        				success.appendChild(output);
        				Element cityList = results.createElement("cityList");
        				output.appendChild(cityList);
        				Element city = results.createElement("city");
        				
        				if(Name_City.isEmpty() || Coor_City.isEmpty()) {  // noCitiesToList Error
        					Element error = results.createElement("error");
        					error.setAttribute("type", "noCitiesToList");
        					
        					rootEle.appendChild(error);
        					error.appendChild(command);
        					error.appendChild(parameters);
        				}
        				else {
        					rootEle.appendChild(success);
        					//sort by name
            				if(commandNode.getAttribute("sortBy").equals("name")) {
            					
            					Collection<City> nameSet = Name_City.values();
            					Iterator<City> ite = nameSet.iterator();
            					while(ite.hasNext()) {
            						City tempCity = ite.next();
            						city.setAttribute("color", tempCity.getColor());
            						city.setAttribute("name", tempCity.getName());
            						city.setAttribute("radius", Integer.toString(tempCity.getRadius()));
            						city.setAttribute("localX", Integer.toString( (int) tempCity.getX()));
            						city.setAttribute("localY", Integer.toString( (int) tempCity.getY()));
            						city.setAttribute("remoteX", Integer.toString( (int) tempCity.getRemoteX() ));
            						city.setAttribute("remoteY", Integer.toString( (int) tempCity.getRemoteY() ));
            	       				cityList.appendChild(city);
            	       				city = results.createElement("city");
            					}
            				}
            				
            				//sort by coordinate
            				else if(commandNode.getAttribute("sortBy").equals("coordinate")) {
            					Collection<City> nameSet = Coor_City.keySet();
            					Iterator<City> ite = nameSet.iterator();
            					while(ite.hasNext()) {
            						City tempCity = ite.next();
            						city.setAttribute("color", tempCity.getColor());
            						city.setAttribute("name", tempCity.getName());
            						city.setAttribute("radius", Integer.toString(tempCity.getRadius()));
            						city.setAttribute("localX", Integer.toString( (int) tempCity.getX()));
            						city.setAttribute("localY", Integer.toString( (int) tempCity.getY()));
            						city.setAttribute("remoteX", Integer.toString( (int) tempCity.getRemoteX() ));
            						city.setAttribute("remoteY", Integer.toString( (int) tempCity.getRemoteY() ));
            	       				cityList.appendChild(city);
            	       				city = results.createElement("city");
            					}
            				}	
        				}
        				
        			}
        			
        			else if(commandNode.getNodeName().equals("clearAll")) {
        				Name_City = new TreeMap<String,City>(new CityNameComparator());
        		    	Coor_City = new TreeMap<City, Point2D.Float>(new CityCoordinateComparator());
        		    	AvlGTree = new AvlGTree<String, City>(new AvlComparator(), g);
        		    	RoadSet = new TreeSet<Road>(new RoadNameComparator());
        		    	allTerminalMap = new TreeMap<String, Terminal>();
        		    	allAirportMap = new TreeMap<String, Airport>();
        		    	allTerminalCoorMap = new TreeMap<Point2D.Float, TreeSet<Terminal>>(new Point2DCoordinateComparator());
        		    	allAirportCoorMap = new TreeMap<Point2D.Float, TreeSet<Airport>>(new Point2DCoordinateComparator());
        		    	allCityCoorMap = new TreeMap<Point2D.Float, TreeSet<City>>(new Point2DCoordinateComparator());
        		    	PMQTMap = new TreeMap<Point2D.Float, PMQuadTree>(new Point2DCoordinateComparator());
        		    	
        		    	Element success = results.createElement("success");
        		    	rootEle.appendChild(success);
        		    	Element command = results.createElement("command");
        		    	command.setAttribute("name", "clearAll");
        				if(commandNode.getAttribute("id") != "") {
        					command.setAttribute("id", commandNode.getAttribute("id"));
        				}
        		    	success.appendChild(command);
        		    	Element parameters = results.createElement("parameters");
        		    	success.appendChild(parameters);
        		    	Element output = results.createElement("output");
        		    	success.appendChild(output);
        			}
        			
        			
        			else if(commandNode.getNodeName().equals("printAvlTree")) {
        				printAvltree(AvlGTree.root, commandNode);
        			}	
        			
        			else if(commandNode.getNodeName().equals("printPMQuadtree")) {
        				printPMQuadtree(commandNode);
        			}
        			
        			else if(commandNode.getNodeName().equals("mapRoad")) {
        				mapRoad(commandNode);
        			}
        			
        			else if(commandNode.getNodeName().equals("saveMap")) {
        				saveMap(commandNode);
        			}
        		
//        			else if(commandNode.getNodeName().equals("rangeCities")) {
//        				rangeCities(commandNode);
//        			}
        			
        			else if(commandNode.getNodeName().equals("rangeRoads")) {
        				rangeRoads(commandNode);
        			}
        			
        			else if(commandNode.getNodeName().equals("nearestCity")) {
        				nearestCity(commandNode);
        			}
        			
//        			else if(commandNode.getNodeName().equals("nearestIsolatedCity")) {
//        				nearestIsolatedCity(commandNode);
//        			}
        			
//        			else if(commandNode.getNodeName().equals("nearestRoad")) {
//        				nearestRoad(commandNode);
//        			}
//        			
//        			else if(commandNode.getNodeName().equals("nearestCityToRoad")) {
//        				nearestCitytoRoad(commandNode);
//        			}
        			else if(commandNode.getNodeName().equals("mapTerminal")) {
        				mapTerminal(commandNode);
        			}
        			else if(commandNode.getNodeName().equals("mapAirport")) {
        				mapAirport(commandNode);
        			}
        			
        			else if(commandNode.getNodeName().equals("unmapRoad")) {
        				unmapRoad(commandNode);
        			}
        			else if(commandNode.getNodeName().equals("unmapTerminal")) {
        				unmapTerminal(commandNode);
        			}
        			else if(commandNode.getNodeName().equals("unmapAirport")) {
        				unmapAirport(commandNode);
        			}
        			else if(commandNode.getNodeName().equals("deleteCity")) {
        				deleteCity(commandNode);
        			}
        			else if(commandNode.getNodeName().equals("globalRangeCities")) {
        				globalRangeCities(commandNode);
        			}
        			else if(commandNode.getNodeName().equals("mst")) {
        				minimumSTree(commandNode);
        			}
        		}
        	}	
        } catch (SAXException | IOException | ParserConfigurationException e) {
        	/* TODO: Process fatal error here */
        		try {
					results = XmlUtility.getDocumentBuilder().newDocument();
					Element fatal_err = results.createElement("fatalError");
					results.appendChild(fatal_err);
//					System.out.println(e.getMessage());
					
				} catch (ParserConfigurationException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
		} finally {
            try {
				XmlUtility.print(results);
			} catch (TransformerException e) {
				e.printStackTrace();
			}
        }
    }
    
    
    
    private static void minimumSTree(Element commandNode) {
		// TODO Auto-generated method stub
		City startCity = Name_City.get(commandNode.getAttribute("start"));
		TreeSet<Location> list = mstPath(startCity);
		System.out.println(list.toString());
	}

    private static TreeSet<Location> mstPath(Location start) {
    	TreeSet<Location> path = new TreeSet<Location>(new LocationNameComparator());
    	TreeSet<Location> allLocation = new TreeSet<Location>(new LocationNameComparator());
    	TreeSet<Location> allLocationCopy = new TreeSet<Location>(new LocationNameComparator());
    	for(PMQuadTree pmqt : PMQTMap.values()) {
    		allLocation.addAll(pmqt.cityListAll);
    		allLocation.addAll(pmqt.airportListAll);
    		allLocation.addAll(pmqt.terminalListAll);
    	}
    	allLocationCopy.addAll(allLocation);
    	
    	path.add(start);
    	allLocation.remove(start);
    	while(!path.containsAll(allLocationCopy)) {
    		Location next = nearestLocation(allLocation, path);
    		System.out.println(next.getName());
    		path.add(next);
    		allLocation.remove(next);
    	}
    	return path;
    }
    
    
    private static Location nearestLocation(TreeSet<Location> allLocation, TreeSet<Location> path) {
    	TreeMap<Double, Location> returnList = new TreeMap<Double, Location>(new DoubleComparator());
    	ArrayList<Road> allRoads = new ArrayList<Road>();
    	
//    	for(Location l : allLocation) {
//    		System.out.println(l.getName());
//    	}
    	
    	for(PMQuadTree pmqt : PMQTMap.values()) {
    		allRoads.addAll(pmqt.roadListAll);
    	}
    	for(Location currLocation : path) {
    		if(currLocation.isCity() || currLocation.isTerminal()) {
            	for(Road r : allRoads) {
            		if(r.start.getName().equals(currLocation.getName()) && !path.contains(r.end) && allLocation.contains(r.end)) {
            			Double distance = Point2D.distance(r.end.getLocalX(), r.end.getLocalY(), currLocation.getLocalX(), currLocation.getLocalY());
            			returnList.put(distance, r.end);
            		}
            		else if(r.end.getName().equals(currLocation.getName()) && !path.contains(r.start) && allLocation.contains(r.start)) {
            			Double distance = Point2D.distance(r.start.getLocalX(), r.start.getLocalY(), currLocation.getLocalX(), currLocation.getLocalY());
            			returnList.put(distance, r.start);
            		}
            	}
            	if(currLocation.isTerminal()) {
            		Terminal t = (Terminal) currLocation;
            		Airport a = t.getTerminalAirport();
            		if(!path.contains(a) && allLocation.contains(a)) {
            			Double distance = Point2D.distance(t.getLocalX(), t.getLocalY(), a.getLocalX(), a.getLocalY());
                		returnList.put(distance, a);
            		}
            	}
        	}
        	
        	if(currLocation.isAirport()) {
        		for(PMQuadTree pmqt : PMQTMap.values()) {
        			for(Airport a : pmqt.airportListAll) {
//        				if(a.getName().equals("AirportB")) {
//        					for(Location l : path) {
//        						System.out.println(l.getName());
//        					}
//        					System.out.println(path.contains(a) + a.getName());
//        				}
        				if(!path.contains(a) && allLocation.contains(a)) {
        					Double distance = Point2D.distance(currLocation.getRemoteX(), currLocation.getRemoteY(), a.getRemoteX(), a.getRemoteY());
        					returnList.put(distance, a);
        				}
        			}
        		}
        	}
    	}
    	return returnList.get(returnList.firstKey());
    }

	private static void globalRangeCities(Element commandNode) {
		// TODO Auto-generated method stub
    	TreeSet<City> citySet = new TreeSet<City>(new CityNameComparatorNew());
		int remoteX = Integer.parseInt(commandNode.getAttribute("remoteX"));
		int remoteY = Integer.parseInt(commandNode.getAttribute("remoteY"));
		int radius = Integer.parseInt(commandNode.getAttribute("radius"));
		Point2D.Float remotePoint = new Point2D.Float(remoteX, remoteY);
		for (Point2D point : PMQTMap.keySet()) {
			if(point.distance(remotePoint) <= radius) {
				for (City c : PMQTMap.get(point).cityListAll) {
					citySet.add(c);
				}
			}
		}
		
		Element error = results.createElement("error"); 
		Element commandName = results.createElement("command"); 
		Element parameters = results.createElement("parameters"); 
		Element remoteXEle = results.createElement("remoteX");
		Element remoteYEle = results.createElement("remoteY");
		Element radiusEle = results.createElement("radius");
		remoteXEle.setAttribute("value",commandNode.getAttribute("remoteX"));
		remoteYEle.setAttribute("value",commandNode.getAttribute("remoteY"));
		radiusEle.setAttribute("value", commandNode.getAttribute("radius"));
		parameters.appendChild(remoteXEle);
		parameters.appendChild(remoteYEle);
		parameters.appendChild(radiusEle);
		commandName.setAttribute("name", "globalRangeCities"); 
		
        if(commandNode.getAttribute("id") != "") {
			commandName.setAttribute("id", commandNode.getAttribute("id"));
		}
		
		if(citySet.isEmpty()) {   // noCitiesExistInRange
			error.setAttribute("type", "noCitiesExistInRange");
	        error.appendChild(commandName);
	        error.appendChild(parameters);
			rootEle.appendChild(error); 
		}
		else {
			Element successEle = results.createElement("success");
			Element outputEle = results.createElement("output");
			Element cityListEle = results.createElement("cityList");
			
			for(City c : citySet) {
				Element cityEle = results.createElement("city");
				cityEle.setAttribute("name", c.getName());
				cityEle.setAttribute("localX", Integer.toString(c.getLocalX()));
				cityEle.setAttribute("localY", Integer.toString(c.getLocalY()));
				cityEle.setAttribute("remoteX", Integer.toString(c.getRemoteX()));
				cityEle.setAttribute("remoteY", Integer.toString(c.getRemoteY()));
				cityEle.setAttribute("color", c.getColor());
				cityEle.setAttribute("radius", Integer.toString(c.getRadius()));
				cityListEle.appendChild(cityEle);
			}
			
			successEle.appendChild(commandName);
			successEle.appendChild(parameters);
			successEle.appendChild(outputEle);
			outputEle.appendChild(cityListEle);
			rootEle.appendChild(successEle);
		}
	}



	private static void deleteCity(Element commandNode) {
		// TODO Auto-generated method stub
		String cityName = commandNode.getAttribute("name");
		City city = Name_City.get(cityName);
		
		Element error = results.createElement("error"); 
		Element commandName = results.createElement("command"); 
		Element parameters = results.createElement("parameters"); 
		Element nameEle = results.createElement("name");
		nameEle.setAttribute("value", cityName);
		parameters.appendChild(nameEle);
		commandName.setAttribute("name", "deleteCity"); 
		
        if(commandNode.getAttribute("id") != "") {
			commandName.setAttribute("id", commandNode.getAttribute("id"));
		}
		
		if(city == null) {   //  cityDoesNotExist
        	error.setAttribute("type", "cityDoesNotExist");
            error.appendChild(commandName);
            error.appendChild(parameters);
			rootEle.appendChild(error); 
			return;
		}
		else {
			Point2D.Float localPoint = new Point2D.Float(city.getLocalX(), city.getLocalY());
			Point2D.Float remotePoint = new Point2D.Float(city.getRemoteX(), city.getRemoteY());
			Name_City.remove(cityName);
			Coor_City.remove(city);
			allCityCoorMap.get(remotePoint).remove(city);
			PMQuadTree PMQT = PMQTMap.get(remotePoint);
			
			Element successEle = results.createElement("success");
			Element outputEle = results.createElement("output");
			
			if(PMQT.containCity(city)) {
				Element cityUnmappedEle = results.createElement("cityUnmapped");
				cityUnmappedEle.setAttribute("color", city.getColor());
				cityUnmappedEle.setAttribute("name", city.getName());
				cityUnmappedEle.setAttribute("radius", Integer.toString(city.getRadius()));
				cityUnmappedEle.setAttribute("localX", Integer.toString(city.getLocalX()));
				cityUnmappedEle.setAttribute("localY", Integer.toString(city.getLocalY()));
				cityUnmappedEle.setAttribute("remoteX", Integer.toString(city.getRemoteX()));
				cityUnmappedEle.setAttribute("remoteY", Integer.toString(city.getRemoteY()));
				outputEle.appendChild(cityUnmappedEle);
			}
			
			Iterator<Road> iter = RoadSet.iterator();
			while(iter.hasNext()) {
				Road r = iter.next();
				if(r.start.getName().equals(cityName) || r.end.getName().equals(cityName)) {
					PMQT.unmapRoad(r);
					Element roadUnmappedEle = results.createElement("roadUnmapped");
					roadUnmappedEle.setAttribute("end", r.end.getName());
					roadUnmappedEle.setAttribute("start", r.start.getName());
					outputEle.appendChild(roadUnmappedEle);
					iter.remove();
				}
			}
			
			successEle.appendChild(commandName);
			successEle.appendChild(parameters);
			successEle.appendChild(outputEle);
			rootEle.appendChild(successEle);
		}
	}



	private static void unmapAirport(Element commandNode) {
		// TODO Auto-generated method stub
		String airportName = commandNode.getAttribute("name");
		Airport airport = allAirportMap.get(airportName);
		
		Element error = results.createElement("error"); 
		Element commandName = results.createElement("command"); 
		Element parameters = results.createElement("parameters"); 
		Element nameEle = results.createElement("name");
		nameEle.setAttribute("value", airportName);
		parameters.appendChild(nameEle);
		commandName.setAttribute("name", "unmapAirport"); 
		
        if(commandNode.getAttribute("id") != "") {
			commandName.setAttribute("id", commandNode.getAttribute("id"));
		}
        
        error.appendChild(commandName);
        error.appendChild(parameters);
        
        if(airport == null) {   // airportDoesNotExist
        	error.setAttribute("type", "airportDoesNotExist");
			rootEle.appendChild(error); 
			return;
        }
        else {
        	Point2D.Float remotePoint = new Point2D.Float(airport.getRemoteX(), airport.getRemoteY());
			PMQuadTree PMQT =  PMQTMap.get(remotePoint);
			PMQT.unmapAirport(airport);
			
			Element outputEle = results.createElement("output");
			
			allAirportMap.remove(airportName);
			for(Terminal t : airport.terminalSet) {
				PMQT.unmapTerminal(t);
				allTerminalMap.remove(t.getName());
				allTerminalCoorMap.get(remotePoint).remove(t);
				Element terminalUnmappedEle = results.createElement("terminalUnmapped");
				terminalUnmappedEle.setAttribute("airportName", t.getTerminalAirport().getName());
				terminalUnmappedEle.setAttribute("cityName", t.getTerminalCity().getName());
				terminalUnmappedEle.setAttribute("localX", Integer.toString(t.getLocalX()));
				terminalUnmappedEle.setAttribute("localY", Integer.toString(t.getLocalY()));
				terminalUnmappedEle.setAttribute("name", t.getName());
				terminalUnmappedEle.setAttribute("remoteX", Integer.toString(t.getRemoteX()));
				terminalUnmappedEle.setAttribute("remoteY", Integer.toString(t.getRemoteY()));
				outputEle.appendChild(terminalUnmappedEle);
			}
			allAirportCoorMap.get(remotePoint).remove(airport);
			if(allAirportCoorMap.get(remotePoint).isEmpty()) {
				allAirportCoorMap.remove(remotePoint);
			}
			if(allTerminalCoorMap.get(remotePoint).isEmpty()) {
				allTerminalCoorMap.remove(remotePoint);
			}
			
			Element successEle = results.createElement("success");
			successEle.appendChild(commandName);
			successEle.appendChild(parameters);
			successEle.appendChild(outputEle);
			rootEle.appendChild(successEle);
        }
	}



	private static void unmapTerminal(Element commandNode) {
		// TODO Auto-generated method stub
		String terminalName = commandNode.getAttribute("name");
		Terminal terminal = allTerminalMap.get(terminalName);
		
		Element error = results.createElement("error"); 
		Element commandName = results.createElement("command"); 
		Element parameters = results.createElement("parameters"); 
		Element nameEle = results.createElement("name");
		nameEle.setAttribute("value", terminalName);
		parameters.appendChild(nameEle);
		commandName.setAttribute("name", "unmapTerminal"); 
		
        if(commandNode.getAttribute("id") != "") {
			commandName.setAttribute("id", commandNode.getAttribute("id"));
		}
        
        error.appendChild(commandName);
        error.appendChild(parameters);
		
		if(terminal == null) {    //  terminalDoesNotExist
			error.setAttribute("type", "terminalDoesNotExist");
			rootEle.appendChild(error); 
			return;
		} 
		else {
			Point2D.Float remotePoint = new Point2D.Float(terminal.getRemoteX(), terminal.getRemoteY());
			PMQuadTree PMQT =  PMQTMap.get(remotePoint);
			PMQT.unmapTerminal(terminal);
			Airport terminalAirport = terminal.getTerminalAirport();
			
			allTerminalMap.remove(terminalName);
			allTerminalCoorMap.get(remotePoint).remove(terminal);
			if(allTerminalCoorMap.get(remotePoint).isEmpty()) {
				allTerminalCoorMap.remove(remotePoint);
			}
			Element outputEle = results.createElement("output");
			Element airportUnmappedEle = results.createElement("airportUnmapped");
			
			if(terminalAirport.terminalSet.isEmpty()) {
				PMQT.unmapAirport(terminalAirport);
				airportUnmappedEle.setAttribute("name", terminalAirport.getName());
				outputEle.appendChild(airportUnmappedEle);
			}
			
			Element successEle = results.createElement("success");

			rootEle.appendChild(successEle);
			successEle.appendChild(commandName);
			successEle.appendChild(parameters);
			successEle.appendChild(outputEle);

		}
	}



	private static void unmapRoad(Element commandNode) {
		// TODO Auto-generated method stub
		City startCity = Name_City.get(commandNode.getAttribute("start"));
		City endCity = Name_City.get(commandNode.getAttribute("end"));
		
		Element error = results.createElement("error"); 
		Element commandName = results.createElement("command"); 
		Element parameters = results.createElement("parameters"); 
		Element startEle = results.createElement("start");
		Element endEle = results.createElement("end");
		startEle.setAttribute("value", commandNode.getAttribute("start"));
		endEle.setAttribute("value", commandNode.getAttribute("end"));
		parameters.appendChild(startEle);
		parameters.appendChild(endEle);
		commandName.setAttribute("name", "unmapRoad"); 
		
        if(commandNode.getAttribute("id") != "") {
			commandName.setAttribute("id", commandNode.getAttribute("id"));
		}
        
        error.appendChild(commandName);
        error.appendChild(parameters);
		
		if(startCity == null) {   // startPointDoesNotExist
			error.setAttribute("type", "startPointDoesNotExist");
			rootEle.appendChild(error); 
			return;
		}
		else if(endCity == null) {    // endPointDoesNotExist 
			error.setAttribute("type", "endPointDoesNotExist");
			rootEle.appendChild(error); 
			return;
		}
		else if(commandNode.getAttribute("start").equals(commandNode.getAttribute("end"))) {   // startEqualsEnd 
			error.setAttribute("type", "startEqualsEnd");
			rootEle.appendChild(error); 
			return;
		}
		else {
			Road roadUnmap = new Road(startCity, endCity);
			if(!RoadSet.contains(roadUnmap)) {  // roadNotMapped 
				error.setAttribute("type", "roadNotMapped");
				rootEle.appendChild(error); 
				return;
			}
			else {
				Point2D.Float remotePoint = new Point2D.Float(startCity.getRemoteX(), startCity.getRemoteY());
				PMQuadTree PMQT =  PMQTMap.get(remotePoint);
				RoadSet.remove(roadUnmap);
				PMQT.unmapRoad(roadUnmap);
				
				Element outputEle = results.createElement("output");
				Element roadDeletedEle = results.createElement("roadDeleted");
				roadDeletedEle.setAttribute("start", startCity.getName());
				roadDeletedEle.setAttribute("end", endCity.getName());
				outputEle.appendChild(roadDeletedEle);
				Element successEle = results.createElement("success");
				rootEle.appendChild(successEle);
				successEle.appendChild(commandName);
				successEle.appendChild(parameters);
				successEle.appendChild(outputEle);
			}
		}
	}



	private static void mapAirport(Element commandNode) {

		// TODO Auto-generated method stub
    	String name = commandNode.getAttribute("name");
    	int localx = Integer.parseInt(commandNode.getAttribute("localX"));
    	int localy = Integer.parseInt(commandNode.getAttribute("localY"));
    	int remotex = Integer.parseInt(commandNode.getAttribute("remoteX"));
    	int remotey = Integer.parseInt(commandNode.getAttribute("remoteY"));
    	String terminalName = commandNode.getAttribute("terminalName");
    	int terminalX = Integer.parseInt(commandNode.getAttribute("terminalX"));
    	int terminalY = Integer.parseInt(commandNode.getAttribute("terminalY"));
    	City terminalCity = Name_City.get(commandNode.getAttribute("terminalCity"));
    	Airport newAirport = new Airport(name, localx, localy, remotex, remotey);
    	Terminal airportTerminal = new Terminal(terminalName, terminalX, terminalY, remotex, remotey, terminalCity, newAirport);
    	
    	boolean ifAirportIntersect = false;
    	boolean ifTerminalIntersect = false;
    	
    	Point2D.Float remotePoint = new Point2D.Float(remotex, remotey);
        Point2D.Float localPoint = new Point2D.Float(localx, localy);
        Point2D.Float terminalLocalPoint = new Point2D.Float(terminalX, terminalY);
    	PMQuadTree PMQT = PMQTMap.get(remotePoint);
    	
    	Element error = results.createElement("error"); 
		Element commandName = results.createElement("command"); 
		Element parameters = results.createElement("parameters"); 
		Element nameEle = results.createElement("name");
		Element localXEle = results.createElement("localX");
		Element localYEle = results.createElement("localY");
		Element remoteXEle = results.createElement("remoteX");
		Element remoteYEle = results.createElement("remoteY");
		Element terminalNameEle = results.createElement("terminalName");
		Element terminalXEle = results.createElement("terminalX");
		Element terminalYEle = results.createElement("terminalY");
		Element terminalCityEle = results.createElement("terminalCity");
			
		nameEle.setAttribute("value", commandNode.getAttribute("name"));
		localXEle.setAttribute("value", commandNode.getAttribute("localX"));
		localYEle.setAttribute("value", commandNode.getAttribute("localY"));
		remoteXEle.setAttribute("value", commandNode.getAttribute("remoteX"));
		remoteYEle.setAttribute("value", commandNode.getAttribute("remoteY"));
		terminalNameEle.setAttribute("value", commandNode.getAttribute("terminalName"));
		terminalXEle.setAttribute("value", commandNode.getAttribute("terminalX"));
		terminalYEle.setAttribute("value", commandNode.getAttribute("terminalY"));
		terminalCityEle.setAttribute("value", commandNode.getAttribute("terminalCity"));
		
		parameters.appendChild(nameEle);
		parameters.appendChild(localXEle);
		parameters.appendChild(localYEle);
		parameters.appendChild(remoteXEle);
		parameters.appendChild(remoteYEle);
		parameters.appendChild(terminalNameEle);
		parameters.appendChild(terminalXEle);
		parameters.appendChild(terminalYEle);
		parameters.appendChild(terminalCityEle);
		
		commandName.setAttribute("name", "mapAirport"); 
		
        if(commandNode.getAttribute("id") != "") {
			commandName.setAttribute("id", commandNode.getAttribute("id"));
		}
        
       
        if(allTerminalMap.containsKey(name) || Name_City.containsKey(name) || allAirportMap.containsKey(name)) {  // duplicateAirportName
        	error.setAttribute("type", "duplicateAirportName");
			rootEle.appendChild(error);
			error.appendChild(commandName);
			error.appendChild(parameters);
			return;
        }
        else if( (allAirportCoorMap.containsKey(remotePoint) && allAirportCoorMap.get(remotePoint).contains(localPoint)) 
        		|| (allTerminalCoorMap.containsKey(remotePoint) && allTerminalCoorMap.get(remotePoint).contains(localPoint)) 
        		|| (allCityCoorMap.containsKey(remotePoint) && allCityCoorMap.get(remotePoint).contains(localPoint))) {  // duplicateAirportCoordinates
				error.setAttribute("type", "duplicateAirportCoordinates");
				rootEle.appendChild(error);
				error.appendChild(commandName);
				error.appendChild(parameters);
				return;
		}
        else if(!Inclusive2DIntersectionVerifier.intersects(remotePoint, remoteMap)
        		|| !Inclusive2DIntersectionVerifier.intersects(localPoint, localMap)) {   // airportOutOfBounds
			error.setAttribute("type", "airportOutOfBounds");
			rootEle.appendChild(error);
			error.appendChild(commandName);
			error.appendChild(parameters);
			return;
		}
        else if(allTerminalMap.containsKey(terminalName) || Name_City.containsKey(terminalName) || allAirportMap.containsKey(terminalName) ) {   // duplicateTerminalName 
			error.setAttribute("type", "duplicateTerminalName");
			rootEle.appendChild(error);
			error.appendChild(commandName);
			error.appendChild(parameters);
			return;
		}
        else if( (allAirportCoorMap.containsKey(remotePoint) && allAirportCoorMap.get(remotePoint).contains(terminalLocalPoint)) 
        		|| (allTerminalCoorMap.containsKey(remotePoint) && allTerminalCoorMap.get(remotePoint).contains(terminalLocalPoint)) 
        		|| (allCityCoorMap.containsKey(remotePoint) && allCityCoorMap.get(remotePoint).contains(terminalLocalPoint))) {  // duplicateTerminalCoordinates 
				error.setAttribute("type", "duplicateTerminalCoordinates");
				rootEle.appendChild(error);
				error.appendChild(commandName);
				error.appendChild(parameters);
				return;
		}
        else if(!Inclusive2DIntersectionVerifier.intersects(remotePoint, remoteMap)
        		|| !Inclusive2DIntersectionVerifier.intersects(terminalLocalPoint, localMap)) {   // terminalOutOfBounds 
			error.setAttribute("type", "terminalOutOfBounds");
			rootEle.appendChild(error);
			error.appendChild(commandName);
			error.appendChild(parameters);
			return;
		}
        else if(terminalCity == null) {  // connectingCityDoesNotExist
			error.setAttribute("type", "connectingCityDoesNotExist");
			rootEle.appendChild(error);
			error.appendChild(commandName);
			error.appendChild(parameters);
			return;
		}
        else if(terminalCity.getRemoteX() != remotex ||
				terminalCity.getRemoteY() != remotey) {  // connectingCityNotInSameMetropole
			error.setAttribute("type", "connectingCityNotInSameMetropole");
			rootEle.appendChild(error);
			error.appendChild(commandName);
			error.appendChild(parameters);
			return;
		}
        	else {
        		Road newRoad = new Road(airportTerminal, terminalCity);
        		PMQT.mapAirport(newAirport, airportTerminal);
        		for(Road r : PMQT.roadListAll) {
        			if(Inclusive2DIntersectionVerifier.intersects(localPoint, r)) {
        				ifAirportIntersect = true;
        			}
        		}
        		
        		if(PMQT.ifSmallestSize || ifAirportIntersect == true) {   // airportViolatesPMRules
        			error.setAttribute("type", "airportViolatesPMRules");
        			rootEle.appendChild(error);
        			error.appendChild(commandName);
        			error.appendChild(parameters);
        			// UNMAP AIRPORT
        			PMQT.unmapAirport(newAirport);
        			PMQT.ifSmallestSize = false;
        			return;
        		}
        		else {
        			if(!PMQT.containCity(terminalCity)) {  // connectingCityNotMapped
        				error.setAttribute("type", "connectingCityNotMapped");
        				rootEle.appendChild(error);
        				error.appendChild(commandName);
        				error.appendChild(parameters);
        				// UNMAP AIRPORT
        				PMQT.unmapAirport(newAirport);
            			PMQT.ifSmallestSize = false;
        				return;
        			}
        			else {
        				PMQT.mapTerminal(airportTerminal);
        				for(Road r : PMQT.roadListAll) {
        					if(Inclusive2DIntersectionVerifier.intersects(localPoint, r)) {
        						ifTerminalIntersect = true;
        					}
        				}
        				if(PMQT.ifSmallestSize == true || ifTerminalIntersect == true) {   // terminalViolatesPMRules
        					error.setAttribute("type", "terminalViolatesPMRules");
        					rootEle.appendChild(error);
        					error.appendChild(commandName);
        					error.appendChild(parameters);
        					// UNMAP AIRPORT
            				PMQT.unmapAirport(newAirport);
                			PMQT.ifSmallestSize = false;
                			// UNMAP TERMINAL
                			PMQT.unmapTerminal(airportTerminal);
                			PMQT.ifSmallestSize = false;
        					return;
        				}
        				else {
                			if(PMQT.roadIntersect(newRoad)) {   // roadIntersectsAnotherRoad
                				error.setAttribute("type", "roadIntersectsAnotherRoad");
            					rootEle.appendChild(error);
            					error.appendChild(commandName);
            					error.appendChild(parameters);
            					// UNMAP AIRPORT
                				PMQT.unmapAirport(newAirport);
                    			PMQT.ifSmallestSize = false;
                    			// UNMAP TERMINAL
                    			PMQT.unmapTerminal(airportTerminal);
                    			PMQT.ifSmallestSize = false;
            					return;
                			}
                			else {
                				PMQT.mapRoad(newRoad);
                				allAirportMap.put(name, newAirport);
                				
                				allTerminalMap.put(terminalName, airportTerminal);
                				
                				if(allTerminalCoorMap.containsKey(remotePoint)) {
                					allTerminalCoorMap.get(remotePoint).add(airportTerminal);
                				} else {
                					TreeSet<Terminal> tempSet = new TreeSet<Terminal>(new Point2DCoordinateComparator());
                					tempSet.add(airportTerminal);
                					allTerminalCoorMap.put(remotePoint, tempSet);
                				}
                    			
                				if(allAirportCoorMap.containsKey(remotePoint)) {
                					allAirportCoorMap.get(remotePoint).add(newAirport);
                				} else {
                					TreeSet<Airport> tempSet = new TreeSet<Airport>(new Point2DCoordinateComparator());
                					tempSet.add(newAirport);
                					allAirportCoorMap.put(remotePoint, tempSet);
                				}
                				
                    			
                    			Element success = results.createElement("success");
                    			Element output = results.createElement("output");
                    			
                    			rootEle.appendChild(success);
                    			success.appendChild(commandName);
                    			success.appendChild(parameters);

                    			success.appendChild(output);
                			}
        				}
        			}	
        		}	
        	}
	}



	private static void mapTerminal(Element commandNode) {
		// TODO Auto-generated method stub
    	String name = commandNode.getAttribute("name");
    	int localx = Integer.parseInt(commandNode.getAttribute("localX"));
    	int localy = Integer.parseInt(commandNode.getAttribute("localY"));
    	int remotex = Integer.parseInt(commandNode.getAttribute("remoteX"));
    	int remotey = Integer.parseInt(commandNode.getAttribute("remoteY"));
    	String cityName = commandNode.getAttribute("cityName");
    	String airportName = commandNode.getAttribute("airportName");
    	City terminalCity = Name_City.get(cityName);
    	Point2D.Float remotePoint = new Point2D.Float(remotex, remotey);
        Point2D.Float localPoint = new Point2D.Float(localx, localy);
        PMQuadTree PMQT = PMQTMap.get(remotePoint);
    	
    	Airport terminalAirport = allAirportMap.get(airportName);
    	Terminal terminal = new Terminal(name, localx, localy, remotex, remotey, terminalCity, terminalAirport);

    	
    	Element error = results.createElement("error"); 
		Element commandName = results.createElement("command"); 
		Element parameters = results.createElement("parameters"); 
		Element nameEle = results.createElement("name");
		Element localXEle = results.createElement("localX");
		Element localYEle = results.createElement("localY");
		Element remoteXEle = results.createElement("remoteX");
		Element remoteYEle = results.createElement("remoteY");
		Element cityNameEle = results.createElement("cityName");
		Element airportNameEle = results.createElement("airportName");
		
		nameEle.setAttribute("value", commandNode.getAttribute("name"));
		localXEle.setAttribute("value", commandNode.getAttribute("localX"));
		localYEle.setAttribute("value", commandNode.getAttribute("localY"));
		remoteXEle.setAttribute("value", commandNode.getAttribute("remoteX"));
		remoteYEle.setAttribute("value", commandNode.getAttribute("remoteY"));
		cityNameEle.setAttribute("value", commandNode.getAttribute("cityName"));
		airportNameEle.setAttribute("value", commandNode.getAttribute("airportName"));
		
		parameters.appendChild(nameEle);
		parameters.appendChild(localXEle);
		parameters.appendChild(localYEle);
		parameters.appendChild(remoteXEle);
		parameters.appendChild(remoteYEle);
		parameters.appendChild(cityNameEle);
		parameters.appendChild(airportNameEle);
		
		commandName.setAttribute("name", "mapTerminal"); 
		
        if(commandNode.getAttribute("id") != "") {
			commandName.setAttribute("id", commandNode.getAttribute("id"));
		}
    	
       
    	
		if(allTerminalMap.containsKey(name) || Name_City.containsKey(name) || allAirportMap.containsKey(name) ) {   // duplicateTerminalName 
			error.setAttribute("type", "duplicateTerminalName");
			rootEle.appendChild(error);
			error.appendChild(commandName);
			error.appendChild(parameters);
			return;
		}
		else if( (allAirportCoorMap.containsKey(remotePoint) && allAirportCoorMap.get(remotePoint).contains(localPoint)) 
        		|| (allTerminalCoorMap.containsKey(remotePoint) && allTerminalCoorMap.get(remotePoint).contains(localPoint)) 
        		|| (allCityCoorMap.containsKey(remotePoint) && allCityCoorMap.get(remotePoint).contains(localPoint)))  {  // duplicateTerminalCoordinates 
				error.setAttribute("type", "duplicateTerminalCoordinates");
				rootEle.appendChild(error);
				error.appendChild(commandName);
				error.appendChild(parameters);
				return;
		}
		else if(!remoteMap.contains(remotePoint) || !localMap.contains(localPoint)) {   // terminalOutOfBounds 
			error.setAttribute("type", "terminalOutOfBounds");
			rootEle.appendChild(error);
			error.appendChild(commandName);
			error.appendChild(parameters);
			return;
		}
		else if(!allAirportMap.containsKey(airportName)) {  //  airportDoesNotExist
			error.setAttribute("type", "airportDoesNotExist");
			rootEle.appendChild(error);
			error.appendChild(commandName);
			error.appendChild(parameters);
			return;
		}
		else if(allAirportMap.get(airportName).getRemoteX() != remotex ||
				allAirportMap.get(airportName).getRemoteY() != remotey) {  //  airportNotInSameMetropole
//			System.out.println(allAirportMap.get(airportNam
			error.setAttribute("type", "airportNotInSameMetropole");
			rootEle.appendChild(error);
			error.appendChild(commandName);
			error.appendChild(parameters);
			return;
		}
		else if(terminalCity == null) {  // connectingCityDoesNotExist
			error.setAttribute("type", "connectingCityDoesNotExist");
			rootEle.appendChild(error);
			error.appendChild(commandName);
			error.appendChild(parameters);
			return;
		}
		else if(terminalCity.getRemoteX() != remotex ||
				terminalCity.getRemoteY() != remotey) {  // connectingCityNotInSameMetropole
			error.setAttribute("type", "connectingCityNotInSameMetropole");
			rootEle.appendChild(error);
			error.appendChild(commandName);
			error.appendChild(parameters);
			return;
		}
		else if(!PMQT.containCity(terminalCity)) {  // connectingCityNotMapped
			error.setAttribute("type", "connectingCityNotMapped");
			rootEle.appendChild(error);
			error.appendChild(commandName);
			error.appendChild(parameters);
			return;
		}
		else {
	    	Road newRoad = new Road(terminal, terminalCity);
			PMQT.mapTerminal(terminal);
			if(PMQT.ifSmallestSize == true) {   // terminalViolatesPMRules
				error.setAttribute("type", "terminalViolatesPMRules");
				rootEle.appendChild(error);
				error.appendChild(commandName);
				error.appendChild(parameters);
				PMQT.unmapTerminal(terminal);
				PMQT.ifSmallestSize = false;
				return;
			}
			else if(PMQT.roadIntersect(newRoad)) {   // roadIntersectsAnotherRoad
				error.setAttribute("type", "roadIntersectsAnotherRoad");
				rootEle.appendChild(error);
				error.appendChild(commandName);
				error.appendChild(parameters);
				PMQT.unmapTerminal(terminal);
				PMQT.ifSmallestSize = false;
				return;
			}
			else {
				//UNMAP TERMINAL
				PMQT.unmapTerminal(terminal);
				PMQT.ifSmallestSize = false;
				
				PMQT.mapTerminal(terminal);
				PMQT.mapRoad(newRoad);
				allTerminalMap.put(terminal.getName(), terminal);
				
				if(allTerminalCoorMap.containsKey(remotePoint)) {
					allTerminalCoorMap.get(remotePoint).add(terminal);
				} else {
					TreeSet<Terminal> tempSet = new TreeSet<Terminal>(new Point2DCoordinateComparator());
					tempSet.add(terminal);
					allTerminalCoorMap.put(remotePoint, tempSet);
				}
				
				Element success = results.createElement("success");
				Element output = results.createElement("output");
				
				rootEle.appendChild(success);
				success.appendChild(commandName);
				success.appendChild(parameters);

				success.appendChild(output);
			}
		}
	}
    
    

//	private static void nearestCitytoRoad(Element commandNode) {
//		// TODO Auto-generated method stub
//		City startCity = Name_City.get(commandNode.getAttribute("start"));
//		City endCity = Name_City.get(commandNode.getAttribute("end"));
//		if(startCity==null || endCity==null) {
//			return;
//		}
//		Road road = new Road(startCity, endCity);
//		CityNameComparatorNew comp = new CityNameComparatorNew();
//		double minDis = 100000;
//		City nearestCity = null;
//		
//		
////		for(City c : PMQT.isolatedCityAll) {
////			//System.out.println(c.getName() + c.distance((double) x_coor, (double) y_coor));
////			if(nearestCity == null) {
////				nearestCity = c;
////				minDis = road.ptSegDist(c);
////			}
////			else {
////				if(road.ptSegDist(c) < minDis ) {
////					nearestCity = c;
////					minDis = road.ptSegDist(c);
////				}
////				else if(road.ptSegDist(c) == minDis) {
////					if(comp.compare(c, nearestCity) <= 0) {
////						nearestCity = c;
////						minDis = road.ptSegDist(c);
////					}
////				}
////			}
////		}
//		
//		for(City c : PMQT.cityListAll) {
//			//System.out.println(c.getName() + c.distance((double) x_coor, (double) y_coor));
//			if(nearestCity == null) {
//				nearestCity = c;
//				minDis = road.ptSegDist(c);
//			}
//			else {
//				if(road.ptSegDist(c) < minDis ) {
//					if(!c.getName().equals(startCity.getName()) && !c.getName().equals(endCity.getName())) {
//						nearestCity = c;
//						minDis = road.ptSegDist(c);
//					}
//				}
//				else if(road.ptSegDist(c) == minDis) {
//					if(!c.getName().equals(startCity.getName()) && !c.getName().equals(endCity.getName())) {
//						if(comp.compare(c, nearestCity) <= 0) {
//							nearestCity = c;
//							minDis = road.ptSegDist(c);
//						}
//					}
//				}
//			}
//		}
//		
//		
//		Element commandName = results.createElement("command"); 
//		Element parameters = results.createElement("parameters"); 	
//		Element startEle = results.createElement("start");
//		Element endEle = results.createElement("end");
//		startEle.setAttribute("value", commandNode.getAttribute("start"));
//		endEle.setAttribute("value", commandNode.getAttribute("end"));
//		parameters.appendChild(startEle);
//		parameters.appendChild(endEle);
//		
//		if(commandNode.getAttribute("id") != "") {
//			commandName.setAttribute("id", commandNode.getAttribute("id"));
//		}
//		
//		commandName.setAttribute("name", "nearestCityToRoad"); 
//		
//		Road reverseRoad = new Road(endCity, startCity);
//		if(PMQT.containRoad(road) == false && PMQT.containRoad(reverseRoad) == false) { // roadIsNotMapped 
//			Element error = results.createElement("error"); 
//			error.setAttribute("type", "roadIsNotMapped");                                              
//
//			rootEle.appendChild(error); 
//			error.appendChild(commandName); 
//			error.appendChild(parameters); 
//		} else {   
//			if(PMQT.getNumCities() == 2 && PMQT.containCity(startCity) == true && PMQT.containCity(endCity) == true) { // noOtherCitiesMapped
//				Element error = results.createElement("error"); 
//				error.setAttribute("type", "noOtherCitiesMapped");                                              
//
//				rootEle.appendChild(error); 
//				error.appendChild(commandName); 
//				error.appendChild(parameters); 
//			} else {
//				Element success = results.createElement("success"); 
//				Element output = results.createElement("output"); 
//				Element cityEle = results.createElement("city"); 
//				cityEle.setAttribute("name", nearestCity.getName());
//				cityEle.setAttribute("x", Integer.toString((int) nearestCity.x));
//				cityEle.setAttribute("y", Integer.toString((int) nearestCity.y));
//				cityEle.setAttribute("color", nearestCity.getColor());
//				cityEle.setAttribute("radius", Integer.toString((int) nearestCity.getRadius()));
//				
//				rootEle.appendChild(success); 
//				success.appendChild(commandName); 
//				success.appendChild(parameters); 
//				success.appendChild(output);
//				output.appendChild(cityEle);
//			}
//		}
//	}

//	private static void nearestRoad(Element commandNode) {
//		// TODO Auto-generated method stub
//		Road nearestRoad = null;
//		RoadNameComparator comp = new RoadNameComparator();
//		double minDis = 100000;
//		int x_coor = Integer.parseInt(commandNode.getAttribute("x")); 
//		int y_coor = Integer.parseInt(commandNode.getAttribute("y")); 
//		
//		for(Road r : PMQT.roadListAll) {
//			Line2D.Float roadLine = new Line2D.Float(r.start.x, r.start.y, r.end.x, r.end.y);
//			//System.out.println(r.getRoadName() + " " + roadLine.ptSegDist( (double) x_coor, (double) y_coor));
//			
//			if(nearestRoad == null) {
//				nearestRoad = r;
//				minDis = roadLine.ptSegDist( (double) x_coor, (double) y_coor);
//			} else {
//				if(roadLine.ptSegDist( (double) x_coor, (double) y_coor) < minDis) {
//					nearestRoad = r;
//					minDis = roadLine.ptSegDist( (double) x_coor, (double) y_coor);
//				}
//				else if(roadLine.ptSegDist( (double) x_coor, (double) y_coor) == minDis) {
//					if(comp.compare(r, nearestRoad) <= 0) {
//						nearestRoad = r;
//						minDis = roadLine.ptSegDist( (double) x_coor, (double) y_coor);
//					}
//				}
//			}	
//		}
//		
//		Element commandName = results.createElement("command"); 
//		Element parameters = results.createElement("parameters"); 
//		Element x_ele = results.createElement("x"); 
//		Element y_ele = results.createElement("y"); 
//	
//		if(commandNode.getAttribute("id") != "") {
//			commandName.setAttribute("id", commandNode.getAttribute("id"));
//		}
//		
//		commandName.setAttribute("name", "nearestRoad"); 
//		x_ele.setAttribute("value", Integer.toString(x_coor)); 
//		y_ele.setAttribute("value", Integer.toString(y_coor)); 
//		
//		if(PMQT.roadListAll.isEmpty()) {
//			Element error = results.createElement("error"); 
//			error.setAttribute("type", "roadNotFound");                                              
//
//			rootEle.appendChild(error); 
//			error.appendChild(commandName); 
//			error.appendChild(parameters); 
//			parameters.appendChild(x_ele); 
//			parameters.appendChild(y_ele); 
//		}
//		else {
//			Element success = results.createElement("success"); 
//			Element output = results.createElement("output"); 
//			Element roadEle = results.createElement("road"); 
//			roadEle.setAttribute("start", nearestRoad.start.getName());
//			roadEle.setAttribute("end", nearestRoad.end.getName());
//			
//			rootEle.appendChild(success); 
//			success.appendChild(commandName); 
//			success.appendChild(parameters); 
//			success.appendChild(output);
//			output.appendChild(roadEle);
//			parameters.appendChild(x_ele); 
//			parameters.appendChild(y_ele); 
//		}
//		
//	}

//	private static void nearestIsolatedCity(Element commandNode) {
//		// TODO Auto-generated method stub
//    	City nearestCity = null;
//		CityNameComparatorNew comp = new CityNameComparatorNew();
//		double minDis = 100000;
//		int x_coor = Integer.parseInt(commandNode.getAttribute("x")); 
//		int y_coor = Integer.parseInt(commandNode.getAttribute("y")); 
//		
////		for(City c : PMQT.isolatedCityAll) {
////			//System.out.println(c.getName() + c.distance((double) x_coor, (double) y_coor));
////			if(nearestCity == null) {
////				nearestCity = c;
////				minDis = c.distance((double) x_coor, (double) y_coor);
////			}
////			else {
////				if(c.distance((double) x_coor, (double) y_coor) < minDis ) {
////					nearestCity = c;
////					minDis = c.distance((double) x_coor, (double) y_coor);
////				}
////				else if(c.distance((double) x_coor, (double) y_coor) == minDis) {
////					if(comp.compare(c, nearestCity) <= 0) {
////						nearestCity = c;
////						minDis = c.distance((double) x_coor, (double) y_coor);
////					}
////				}
////			}
////		}
//
//		
//		
//		
//		Element commandName = results.createElement("command"); 
//		Element parameters = results.createElement("parameters"); 
//		Element x_ele = results.createElement("x"); 
//		Element y_ele = results.createElement("y"); 
//	
//		
//		if(commandNode.getAttribute("id") != "") {
//			commandName.setAttribute("id", commandNode.getAttribute("id"));
//		}
//		
//		commandName.setAttribute("name", "nearestIsolatedCity"); 
//		x_ele.setAttribute("value", Integer.toString(x_coor)); 
//		y_ele.setAttribute("value", Integer.toString(y_coor)); 
//		
////		if(PMQT.isolatedCityAll.isEmpty()) {
////			Element error = results.createElement("error"); 
////			error.setAttribute("type", "cityNotFound");                                              
////
////			rootEle.appendChild(error); 
////			error.appendChild(commandName); 
////			error.appendChild(parameters); 
////			parameters.appendChild(x_ele); 
////			parameters.appendChild(y_ele); 
////		}
////		else {
////			Element success = results.createElement("success"); 
////			Element output = results.createElement("output"); 
////			Element cityEle = results.createElement("isolatedCity"); 
////			cityEle.setAttribute("name", nearestCity.getName());
////			cityEle.setAttribute("x", Integer.toString((int) nearestCity.x));
////			cityEle.setAttribute("y", Integer.toString((int) nearestCity.y));
////			cityEle.setAttribute("color", nearestCity.getColor());
////			cityEle.setAttribute("radius", Integer.toString(nearestCity.getRadius()));
////
////			rootEle.appendChild(success); 
////			success.appendChild(commandName); 
////			success.appendChild(parameters); 
////			success.appendChild(output);
////			output.appendChild(cityEle);
////			parameters.appendChild(x_ele); 
////			parameters.appendChild(y_ele); 
////		}
//	}


	private static void nearestCity(Element commandNode) {
		// TODO Auto-generated method stub
//		TreeSet<City> nearestCitySet = new TreeSet<City>(new CityNameComparatorNew());
		City nearestCity = null;
		CityNameComparatorNew comp = new CityNameComparatorNew();
		double minDis = 100000;
		int x_coor = Integer.parseInt(commandNode.getAttribute("localX")); 
		int y_coor = Integer.parseInt(commandNode.getAttribute("localY")); 
		int remoteX = Integer.parseInt(commandNode.getAttribute("remoteX")); 
		int remoteY = Integer.parseInt(commandNode.getAttribute("remoteY")); 
		Point2D.Float remotePoint = new Point2D.Float(remoteX, remoteY);
		PMQuadTree PMQT = PMQTMap.get(remotePoint);
		
		Element commandName = results.createElement("command"); 
		Element parameters = results.createElement("parameters"); 
		Element x_ele = results.createElement("localX"); 
		Element y_ele = results.createElement("localY"); 
		Element remoteXEle = results.createElement("remoteX");
		Element remoteYEle = results.createElement("remoteY");
		
		if(commandNode.getAttribute("id") != "") {
			commandName.setAttribute("id", commandNode.getAttribute("id"));
		}
		
		commandName.setAttribute("name", "nearestCity"); 
		x_ele.setAttribute("value", Integer.toString(x_coor)); 
		y_ele.setAttribute("value", Integer.toString(y_coor)); 
		remoteXEle.setAttribute("value", commandNode.getAttribute("remoteX"));
		remoteYEle.setAttribute("value", commandNode.getAttribute("remoteY"));
		
		if(PMQT == null || PMQT.root.type == 0) {  // cityNotFound
			Element error = results.createElement("error"); 
			error.setAttribute("type", "cityNotFound");                                              

			rootEle.appendChild(error); 
			error.appendChild(commandName); 
			error.appendChild(parameters); 
			parameters.appendChild(x_ele); 
			parameters.appendChild(y_ele); 
			parameters.appendChild(remoteXEle);
			parameters.appendChild(remoteYEle);
		}
		else {
			for(City c : PMQT.cityListAll) {
				//System.out.println(c.getName() + c.distance((double) x_coor, (double) y_coor));
				if(nearestCity == null) {
					nearestCity = c;
					minDis = c.distance((double) x_coor, (double) y_coor);
				}
				else {
					if(c.distance((double) x_coor, (double) y_coor) < minDis ) {
						nearestCity = c;
						minDis = c.distance((double) x_coor, (double) y_coor);
					}
					else if(c.distance((double) x_coor, (double) y_coor) == minDis) {
						if(comp.compare(c, nearestCity) <= 0) {
							nearestCity = c;
							minDis = c.distance((double) x_coor, (double) y_coor);
						}
					}
				}
			}
			Element success = results.createElement("success"); 
			Element output = results.createElement("output"); 
			Element cityEle = results.createElement("city"); 
			cityEle.setAttribute("name", nearestCity.getName());
			cityEle.setAttribute("localX", Integer.toString((int) nearestCity.x));
			cityEle.setAttribute("localY", Integer.toString((int) nearestCity.y));
			cityEle.setAttribute("remoteX", Integer.toString((int) nearestCity.getRemoteX()));
			cityEle.setAttribute("remoteY", Integer.toString((int) nearestCity.getRemoteY()));
			cityEle.setAttribute("color", nearestCity.getColor());
			cityEle.setAttribute("radius", Integer.toString(nearestCity.getRadius()));

			rootEle.appendChild(success); 
			success.appendChild(commandName); 
			success.appendChild(parameters); 
			success.appendChild(output);
			output.appendChild(cityEle);
			parameters.appendChild(x_ele); 
			parameters.appendChild(y_ele);
			parameters.appendChild(remoteXEle);
			parameters.appendChild(remoteYEle);
		}
		
	}

	/**
     * return the shortest distance from a road to circle's center.
     * Circle's center (x,y)
     * a = base, b,c = side
     * Heron's formula: Area = sqrt(s(s-a)(s-b)(s-c)), s=(a+b+c)/2
     * Source: https://en.wikipedia.org/wiki/Heron%27s_formula
     */
    private static double roadCircleCenterDistance(double x, double y, Road r) {
    	double x1 = r.start.getX();
    	double y1 = r.start.getY();
    	double x2 = r.end.getX();
    	double y2 = r.end.getY();
    	
    	if(x1==x && x2==x) {
    		return Math.min(Math.abs(y1-y), Math.abs(y2-y));
    	}
    	if(y1==y && y2==y) {
    		return Math.min(Math.abs(x1-x), Math.abs(x2-x));
    	}
    	
    	double a = Math.sqrt( Math.abs(x1-x2)*Math.abs(x1-x2) + Math.abs(y1-y2)*Math.abs(y1-y2) );
    	double b = Math.sqrt( Math.abs(x1-x)*Math.abs(x1-x) + Math.abs(y1-y)*Math.abs(y1-y) );
    	double c = Math.sqrt( Math.abs(x2-x)*Math.abs(x2-x) + Math.abs(y2-y)*Math.abs(y2-y) );
    	double s = (a+b+c)/2;
    	double area = Math.sqrt(s*(s-a)*(s-b)*(s-c));
    	double height = 2*area/a;
    	//System.out.println("start=" + r.start.getName() + " end=" + r.end.getName() + " height=" + height);
    	return height;
    	
    }

	private static void rangeRoads(Element commandNode) throws IOException {
		// TODO Auto-generated method stub
		ArrayList<Road> roadList = new ArrayList<Road>(); 
		int x_coor = Integer.parseInt(commandNode.getAttribute("x")); 
		int y_coor = Integer.parseInt(commandNode.getAttribute("y")); 
		int radius = Integer.parseInt(commandNode.getAttribute("radius")); 
		canvas.addCircle(x_coor, y_coor, radius, Color.blue, false);
		
		
		
		for(Road currRoad : RoadSet) { 
			Line2D.Float roadLine = new Line2D.Float(currRoad.x1, currRoad.y1, currRoad.x2, currRoad.y2);
			if(roadLine.ptSegDist(x_coor, y_coor) <= (double) radius) {
//				System.out.println("Start: " + currRoad.start.getName() + " end: " + currRoad.end.getName() + "  " + roadLine.ptLineDist(x_coor, y_coor));
				roadList.add(currRoad);
			}
		} 

		Element commandName = results.createElement("command"); 
		Element parameters = results.createElement("parameters"); 
		Element x_ele = results.createElement("x"); 
		Element y_ele = results.createElement("y"); 
		Element radius_ele = results.createElement("radius"); 
		
		if(commandNode.getAttribute("id") != "") {
			commandName.setAttribute("id", commandNode.getAttribute("id"));
		}
		
		commandName.setAttribute("name", "rangeRoads"); 
		x_ele.setAttribute("value", Integer.toString(x_coor)); 
		y_ele.setAttribute("value", Integer.toString(y_coor)); 
		radius_ele.setAttribute("value", Integer.toString(radius)); 

		if(roadList.isEmpty()) {   // noRoadsExistInRange Error 
			Element error = results.createElement("error"); 
			error.setAttribute("type", "noRoadsExistInRange");                                              

			rootEle.appendChild(error); 
			error.appendChild(commandName); 
			error.appendChild(parameters); 
			parameters.appendChild(x_ele); 
			parameters.appendChild(y_ele); 
			parameters.appendChild(radius_ele); 

			if(!commandNode.getAttribute("saveMap").isEmpty()) { 
				Element saveMap = results.createElement("saveMap"); 
				saveMap.setAttribute("value", commandNode.getAttribute("saveMap")); 
				parameters.appendChild(saveMap); 

				canvas.save(commandNode.getAttribute("saveMap")); 
				canvas.dispose(); 
			} 
		} 
		else { 
			Element success = results.createElement("success"); 
			Element output = results.createElement("output"); 
			Element cityLists = results.createElement("roadList"); 

			for(Road road : roadList) { 
				Element road_ele = results.createElement("road"); 
				road_ele.setAttribute("end", road.end.getName());
				road_ele.setAttribute("start", road.start.getName());
				cityLists.appendChild(road_ele); 
			} 

			rootEle.appendChild(success); 
			success.appendChild(commandName); 
			success.appendChild(parameters); 
			parameters.appendChild(x_ele); 
			parameters.appendChild(y_ele); 
			parameters.appendChild(radius_ele); 

			if(!commandNode.getAttribute("saveMap").isEmpty()) { 
				Element saveMap = results.createElement("saveMap"); 
				saveMap.setAttribute("value", commandNode.getAttribute("saveMap")); 
				parameters.appendChild(saveMap); 

				canvas.save(commandNode.getAttribute("saveMap")); 
				canvas.dispose(); 
			} 
			output.appendChild(cityLists); 
			success.appendChild(output); 
		} 
	}


//	private static void rangeCities(Element commandNode) throws IOException {
//		// TODO Auto-generated method stub
//		TreeSet<City> cityList = new TreeSet<>(new CityNameComparatorNew());
//		int x_coor = Integer.parseInt(commandNode.getAttribute("x")); 
//		int y_coor = Integer.parseInt(commandNode.getAttribute("y")); 
//		int radius = Integer.parseInt(commandNode.getAttribute("radius")); 
//
//		List<City> all_city = new ArrayList<City>();
//		all_city.addAll(Name_City.values());
//		for(City currCity : all_city) { 
//			double distance = Point2D.distance(x_coor, y_coor, currCity.x, currCity.y);
//			if(distance <= radius) { 
//				if(PMQT.findCity(currCity) != null) { // city is in tree 
//					cityList.add(currCity); 
//				} 
//			} 
//		} 
//
//		Element commandName = results.createElement("command"); 
//		Element parameters = results.createElement("parameters"); 
//		Element x_ele = results.createElement("x"); 
//		Element y_ele = results.createElement("y"); 
//		Element radius_ele = results.createElement("radius"); 
//		
//		if(commandNode.getAttribute("id") != "") {
//			commandName.setAttribute("id", commandNode.getAttribute("id"));
//		}
//		
//		commandName.setAttribute("name", "rangeCities"); 
//		x_ele.setAttribute("value", Integer.toString(x_coor)); 
//		y_ele.setAttribute("value", Integer.toString(y_coor)); 
//		radius_ele.setAttribute("value", Integer.toString(radius)); 
//
//		if(cityList.isEmpty()) {   // noCitiesExistInRange Error 
//			Element error = results.createElement("error"); 
//			error.setAttribute("type", "noCitiesExistInRange");                                              
//
//			rootEle.appendChild(error); 
//			error.appendChild(commandName); 
//			error.appendChild(parameters); 
//			parameters.appendChild(x_ele); 
//			parameters.appendChild(y_ele); 
//			parameters.appendChild(radius_ele); 
//
//			if(!commandNode.getAttribute("saveMap").isEmpty()) { 
//				Element saveMap = results.createElement("saveMap"); 
//				saveMap.setAttribute("value", commandNode.getAttribute("saveMap")); 
//				parameters.appendChild(saveMap); 
//
//				canvas.save(commandNode.getAttribute("saveMap")); 
//				canvas.dispose(); 
//			} 
//		} 
//		else { 
//			Element success = results.createElement("success"); 
//			Element output = results.createElement("output"); 
//			Element cityLists = results.createElement("cityList"); 
//
//			for(City city : cityList) { 
//				Element city_ele = results.createElement("city"); 
//				city_ele.setAttribute("name", city.getName()); 
//				city_ele.setAttribute("x", Integer.toString((int) city.getX())); 
//				city_ele.setAttribute("y", Integer.toString((int) city.getY())); 
//				city_ele.setAttribute("color", city.getColor()); 
//				city_ele.setAttribute("radius", Integer.toString(city.getRadius())); 
//				cityLists.appendChild(city_ele); 
//			} 
//
//			rootEle.appendChild(success); 
//			success.appendChild(commandName); 
//			success.appendChild(parameters); 
//			parameters.appendChild(x_ele); 
//			parameters.appendChild(y_ele); 
//			parameters.appendChild(radius_ele); 
//
//			if(!commandNode.getAttribute("saveMap").isEmpty()) { 
//				Element saveMap = results.createElement("saveMap"); 
//				saveMap.setAttribute("value", commandNode.getAttribute("saveMap")); 
//				parameters.appendChild(saveMap); 
//
//				canvas.save(commandNode.getAttribute("saveMap")); 
//				canvas.dispose(); 
//			} 
//			output.appendChild(cityLists); 
//			success.appendChild(output); 
//		} 
//	}


	private static void saveMap(Element commandNode) throws IOException {
		// TODO Auto-generated method stub
		int remoteX = Integer.parseInt(commandNode.getAttribute("remoteX"));
		int remoteY = Integer.parseInt(commandNode.getAttribute("remoteY"));
		Point2D.Float remotePoint = new Point2D.Float(remoteX, remoteY);
		PMQuadTree PMQT = PMQTMap.get(remotePoint);
		Element success  = results.createElement("success"); 
		Element commandName = results.createElement("command"); 
		Element parameters = results.createElement("parameters"); 
		Element name = results.createElement("name");
		Element remoteXEle = results.createElement("remoteX");
		Element remoteYEle = results.createElement("remoteY");
		Element output = results.createElement("output");
		
		commandName.setAttribute("name", "saveMap"); 
        if(commandNode.getAttribute("id") != "") {
			commandName.setAttribute("id", commandNode.getAttribute("id"));
		}
        name.setAttribute("value", commandNode.getAttribute("name"));
        remoteXEle.setAttribute("value", Integer.toString(remoteX));
        remoteYEle.setAttribute("value", Integer.toString(remoteY));
        
        parameters.appendChild(remoteXEle);
        parameters.appendChild(remoteYEle);
        parameters.appendChild(name);
        
        
        Element errorEle = results.createElement("error");
        errorEle.appendChild(commandName); 
        errorEle.appendChild(parameters); 
        
        if(!Inclusive2DIntersectionVerifier.intersects(remotePoint, remoteMap)) {  // metropoleOutOfBounds
        	errorEle.setAttribute("type", "metropoleOutOfBounds");
        	rootEle.appendChild(errorEle);
        	return;
        }
        else if(PMQT == null || PMQT.root.type == 0) {  // metropoleIsEmpty
	        errorEle.setAttribute("type", "metropoleIsEmpty"); 
	        rootEle.appendChild(errorEle); 
	        return;
		} else {
			rootEle.appendChild(success); 
	        success.appendChild(commandName); 
	        success.appendChild(parameters); 
	        success.appendChild(output);
	        
	        
	        canvas.save(commandNode.getAttribute("name"));
		}
	}





	private static void printPMQuadtree(Element commandNode) {
		int remoteX = Integer.parseInt(commandNode.getAttribute("remoteX"));
		int remoteY = Integer.parseInt(commandNode.getAttribute("remoteY"));
		Point2D.Float remotePoint = new Point2D.Float(remoteX, remoteY);
		PMQuadTree PMQT = PMQTMap.get(remotePoint);
		
		Element error = results.createElement("error"); 
        Element commandName = results.createElement("command"); 
        Element parameters = results.createElement("parameters"); 
        Element remoteXEle = results.createElement("remoteX");
        Element remoteYEle = results.createElement("remoteY");
        remoteXEle.setAttribute("value", Integer.toString(remoteX));
        remoteYEle.setAttribute("value", Integer.toString(remoteY));
        parameters.appendChild(remoteXEle);
        parameters.appendChild(remoteYEle);
        
        if(commandNode.getAttribute("id") != "") {
			commandName.setAttribute("id", commandNode.getAttribute("id"));
		}
        commandName.setAttribute("name", "printPMQuadtree"); 
                        
		error.appendChild(commandName); 
        error.appendChild(parameters); 
		if(!Inclusive2DIntersectionVerifier.intersects(remotePoint, remoteMap)) {  // metropoleOutOfBounds 
			error.setAttribute("type", "metropoleOutOfBounds");
			rootEle.appendChild(error); 
			return;
		}
		else if(PMQT == null || PMQT.root.type == 0) {  // metropoleIsEmpty
	        error.setAttribute("type", "metropoleIsEmpty"); 
	        rootEle.appendChild(error); 
	        return;
		} 
		else {
			PMNode rootNode = PMQT.root;
			Element success  = results.createElement("success"); 
			Element outputs = results.createElement("output"); 
			Element tree = results.createElement("quadtree");  
			tree.setAttribute("order", Integer.toString(pmOrder));
	        rootEle.appendChild(success); 
	        success.appendChild(commandName); 
	        success.appendChild(parameters); 
	        success.appendChild(outputs); 
	        outputs.appendChild(tree);
	        
	        tree.appendChild(printPMQuadtreeHelper(rootNode)); 
	       
		}
	}


	private static Element printPMQuadtreeHelper(PMNode rootNode) {
		// TODO Auto-generated method stub
		if(rootNode.type == 0) {
			Element white = results.createElement("white");
			return white;
		}
		else if(rootNode.type == 1) {
			PMBlack blackNode = (PMBlack) rootNode;
			Element blackEle = results.createElement("black");
			blackEle.setAttribute("cardinality", Integer.toString(blackNode.getCardinality()));
			
//			System.out.println((blackNode.cityList.size() + blackNode.airportList.size() + blackNode.terminalList.size()));
			
			for(Terminal t : blackNode.terminalList) {
				Element terminalEle = results.createElement("terminal");
				terminalEle.setAttribute("airportName", t.getTerminalAirport().getName());
				terminalEle.setAttribute("cityName", t.getTerminalCity().getName());
				terminalEle.setAttribute("localX", Integer.toString(t.getLocalX()));
				terminalEle.setAttribute("localY", Integer.toString(t.getLocalY()));
				terminalEle.setAttribute("remoteX", Integer.toString(t.getRemoteX()));
				terminalEle.setAttribute("remoteY", Integer.toString(t.getRemoteY()));
				terminalEle.setAttribute("name", t.getName());
				blackEle.appendChild(terminalEle);
			}
			for(City c : blackNode.cityList) {
				Element cityEle = results.createElement("city");
				cityEle.setAttribute("color", c.getColor());
				cityEle.setAttribute("name", c.getName());
				cityEle.setAttribute("radius", Integer.toString(c.getRadius()));
				cityEle.setAttribute("localX", Integer.toString((int) c.getX()));
				cityEle.setAttribute("localY", Integer.toString((int) c.getY()));
				cityEle.setAttribute("remoteX", Integer.toString(c.getRemoteX()));
				cityEle.setAttribute("remoteY", Integer.toString(c.getRemoteY()));
				blackEle.appendChild(cityEle);
			}
			for(Airport a : blackNode.airportList) {
				Element airportEle = results.createElement("airport");
				airportEle.setAttribute("localX", Integer.toString(a.getLocalX()));
				airportEle.setAttribute("localY", Integer.toString(a.getLocalY()));
				airportEle.setAttribute("name", a.getName());
				airportEle.setAttribute("remoteX", Integer.toString(a.getRemoteX()));
				airportEle.setAttribute("remoteY", Integer.toString(a.getRemoteY()));
				blackEle.appendChild(airportEle);
			}
			for(Road r : blackNode.roadList) {
				Element roadEle = results.createElement("road");
				roadEle.setAttribute("end", r.end.getName());
				roadEle.setAttribute("start", r.start.getName());
				blackEle.appendChild(roadEle);
			}
			return blackEle;
		}
		else {
			PMGray grayNode = (PMGray) rootNode;
			Element grayEle = results.createElement("gray");
			int x = (int) (grayNode.origin.getX() + grayNode.halfW);
			int y = (int) (grayNode.origin.getY() + grayNode.halfH);
			grayEle.setAttribute("x", Integer.toString(x));
			grayEle.setAttribute("y", Integer.toString(y));
			for(PMNode node : grayNode.children) {
				Element childEle = printPMQuadtreeHelper(node);
				grayEle.appendChild(childEle);
			}
			return grayEle;
		}
	}


//	private static void mapCity(Element commandNode) {
//		// TODO Auto-generated method stub
//		String cityName = commandNode.getAttribute("name");
//		City city = Name_City.get(cityName);
//		
//		if(city != null) {
//			if(PMQT.findCity(city) == null) {
//				if(city.getX() >= 0 && city.getX() <= localWidth && city.getY() >= 0 && city.getY() <= localHeight ) {
//					PMQT.mapIsolatedCity(Name_City.get(commandNode.getAttribute("name")));
//					
//                    Element success  = results.createElement("success"); 
//                    Element commandName = results.createElement("command"); 
//                    Element parameters = results.createElement("parameters"); 
//                    Element outputs = results.createElement("output"); 
//                    Element name = results.createElement("name"); 
//
//                    commandName.setAttribute("name", "mapCity"); 
//                    name.setAttribute("value", city.getName()); 
//                    
//        	        if(commandNode.getAttribute("id") != "") {
//        				commandName.setAttribute("id", commandNode.getAttribute("id"));
//        			}
//
//                    rootEle.appendChild(success); 
//                    success.appendChild(commandName); 
//                    success.appendChild(parameters); 
//                    success.appendChild(outputs); 
//                    parameters.appendChild(name);
//				}
//				else {  // cityOutOfBounds error 
//					Element error = results.createElement("error"); 
//					Element commandName = results.createElement("command"); 
//					Element parameters = results.createElement("parameters"); 
//					Element name = results.createElement("name"); 
//
//					error.setAttribute("type", "cityOutOfBounds"); 
//					commandName.setAttribute("name", "mapCity"); 
//					name.setAttribute("value", city.getName()); 
//					
//        	        if(commandNode.getAttribute("id") != "") {
//        				commandName.setAttribute("id", commandNode.getAttribute("id"));
//        			}
//
//					rootEle.appendChild(error); 
//					error.appendChild(commandName); 
//					error.appendChild(parameters); 
//					parameters.appendChild(name);   
//				}
//			}
//			else { // cityAlreadyMapped Error 
//                Element error = results.createElement("error"); 
//                Element commandName = results.createElement("command"); 
//                Element parameters = results.createElement("parameters"); 
//                Element name = results.createElement("name"); 
//
//                error.setAttribute("type", "cityAlreadyMapped"); 
//                commandName.setAttribute("name", "mapCity"); 
//                name.setAttribute("value", city.getName()); 
//
//    	        if(commandNode.getAttribute("id") != "") {
//    				commandName.setAttribute("id", commandNode.getAttribute("id"));
//    			}
//                
//                rootEle.appendChild(error); 
//                error.appendChild(commandName); 
//                error.appendChild(parameters); 
//                parameters.appendChild(name); 
//			}
//		}
//		else {	// nameNotInDictionary Error 
//			Element error = results.createElement("error"); 
//			Element commandName = results.createElement("command"); 
//			Element parameters = results.createElement("parameters"); 
//			Element name = results.createElement("name"); 
//
//			error.setAttribute("type", "nameNotInDictionary"); 
//			commandName.setAttribute("name", "mapCity"); 
//			name.setAttribute("value", cityName); 
//			
//	        if(commandNode.getAttribute("id") != "") {
//				commandName.setAttribute("id", commandNode.getAttribute("id"));
//			}
//
//			rootEle.appendChild(error); 
//			error.appendChild(commandName); 
//			error.appendChild(parameters); 
//			parameters.appendChild(name);  
//		}
//			
//	}
	
	
	
    private static void mapRoad(Element commandNode) {
		// TODO Auto-generated method stub
//    	
//    	City temp1 = new City("c1", 0, "red", 101, 101, 0, 0);
//    	City temp2 = new City("c2", 0, "red", 101, 120, 0, 0);
//    	City temp3 = new City("c3", 0, "red", 120, 120, 0, 0);
//    	Road r1 = new Road(temp1, temp2);
//    	Road r2 = new Road(temp1, temp3);
//    	System.out.println(Inclusive2DIntersectionVerifier.intersects(r1, r2));
    	
    	
    	City start = Name_City.get(commandNode.getAttribute("start"));
    	City end = Name_City.get(commandNode.getAttribute("end"));
    	Road road = null;
    	
		Element error = results.createElement("error"); 
		Element commandName = results.createElement("command"); 
		Element parameters = results.createElement("parameters"); 
		Element startEle = results.createElement("start");
		Element endEle = results.createElement("end");
		startEle.setAttribute("value", commandNode.getAttribute("start"));
		endEle.setAttribute("value", commandNode.getAttribute("end"));
		parameters.appendChild(startEle);
		parameters.appendChild(endEle);
		
		commandName.setAttribute("name", "mapRoad"); 
		
        if(commandNode.getAttribute("id") != "") {
			commandName.setAttribute("id", commandNode.getAttribute("id"));
		}
        
        Rectangle2D localRegion = new Rectangle(localWidth, localHeight);
        Rectangle2D remoteRegion = new Rectangle(remoteWidth, remoteHeight);
         
        
    	if(start == null) {  // startPointDoesNotExist 
    		error.setAttribute("type", "startPointDoesNotExist"); 
			rootEle.appendChild(error);
			error.appendChild(commandName);
			error.appendChild(parameters);
			return;
    	}
    	else if(end == null) {    // endPointDoesNotExist 
			error.setAttribute("type", "endPointDoesNotExist");
			rootEle.appendChild(error);
			error.appendChild(commandName);
			error.appendChild(parameters);
			return;
    	}
    	else if(start.getName().equals(end.getName())) {   // startEqualsEnd 
			error.setAttribute("type", "startEqualsEnd");
			rootEle.appendChild(error);
			error.appendChild(commandName);
			error.appendChild(parameters);
			return;
    	}
    	else {
    		road = new Road(start, end);
    		if(start.getRemoteX() != end.getRemoteX() || start.getRemoteY() != end.getRemoteY()) {  //roadNotInOneMetropole 
	        	error.setAttribute("type", "roadNotInOneMetropole");
				rootEle.appendChild(error);
				error.appendChild(commandName);
				error.appendChild(parameters);
    			return;
    		}
    		else if(!localRegion.intersectsLine(start.getX(), start.getY(), end.getX(), end.getY()) 
        			|| !remoteRegion.contains(start.getRemoteX(), start.getRemoteY()) 
        			|| !remoteRegion.contains(end.getRemoteX(), end.getRemoteY())) {    // roadOutOfBounds 
        		error.setAttribute("type", "roadOutOfBounds");
				rootEle.appendChild(error);
				error.appendChild(commandName);
				error.appendChild(parameters);
    			return;
    		}
    		else {
    			Point2D.Float remotePoint = new Point2D.Float(start.getRemoteX(), start.getRemoteY());
    			PMQuadTree PMQT = PMQTMap.get(remotePoint);
        		if(PMQT.containRoad(road)) {	  // roadAlreadyMapped 
    				error.setAttribute("type", "roadAlreadyMapped");
    				rootEle.appendChild(error);
    				error.appendChild(commandName);
    				error.appendChild(parameters);
    				return;
        		}
        		else if(PMQT.roadIntersect(road)) {   // roadIntersectsAnotherRoad 
        			error.setAttribute("type", "roadIntersectsAnotherRoad");
    				rootEle.appendChild(error);
    				error.appendChild(commandName);
    				error.appendChild(parameters);
        			return;
        		}
        		else {
        			PMQT.mapRoad(road);
            		if(PMQT.ifSmallestSize) {// roadViolatesPMRules
        				error.setAttribute("type", "roadViolatesPMRules");
        				rootEle.appendChild(error);
        				error.appendChild(commandName);
        				error.appendChild(parameters);
            			//UNMAP ROAD
        				PMQT.ifSmallestSize = false;
        				PMQT.unmapRoad(road);
        				return;
            		}
            		else {

            			RoadSet.add(road);
    					
    					Element success = results.createElement("success");
    					Element output = results.createElement("output");
    					Element roadCreate = results.createElement("roadCreated");
    					roadCreate.setAttribute("start", commandNode.getAttribute("start"));
    					roadCreate.setAttribute("end", commandNode.getAttribute("end"));
    					
    					rootEle.appendChild(success);
    					success.appendChild(commandName);
    					success.appendChild(parameters);

    					success.appendChild(output);
    					output.appendChild(roadCreate);
            		}
        		}
    		}

    	}			         
	}


	public static void printAvltree(AvlNode<String, City> node, Element commandNode) {
		if(AvlGTree.isEmpty()) {
			Element error = results.createElement("error");
			rootEle.appendChild(error);
			error.setAttribute("type", "emptyTree");
			Element command = results.createElement("command");
			command.setAttribute("name", "printAvlTree");
			if(commandNode.getAttribute("id") != "") {
				command.setAttribute("id", commandNode.getAttribute("id"));
			}
			Element parameters = results.createElement("parameters");
			error.appendChild(command);
			error.appendChild(parameters);
		}
		else {
			Element success = results.createElement("success");
			rootEle.appendChild(success);
			Element command = results.createElement("command");
			command.setAttribute("name", "printAvlTree");
			if(commandNode.getAttribute("id") != "") {
				command.setAttribute("id", commandNode.getAttribute("id"));
			}
			success.appendChild(command);
			Element parameters = results.createElement("parameters");
	    	success.appendChild(parameters);
	    	Element output = results.createElement("output");
	    	success.appendChild(output);
	    	Element Avl = results.createElement("AvlGTree");
	    	Avl.setAttribute("cardinality", Integer.toString(AvlGTree.size()));
	    	Avl.setAttribute("height", Integer.toString(AvlGTree.root.getHeight()));
	    	Avl.setAttribute("maxImbalance", Integer.toString(AvlGTree.g));
	    	Avl.appendChild(printAvltreeHelper(node));
	    	output.appendChild(Avl);
		}
    }
    
    private static Element printAvltreeHelper(AvlNode<String, City> node) {
    	if(node == null) {
    		Element emptyChild = results.createElement("emptyChild");
    		return emptyChild;
    	} else {
    		Element rootNodeEle = results.createElement("node");
    		rootNodeEle.setAttribute("key", node.getKey());
    		String valueAtt = "";
    		int x = (int) node.getValue().getX();
    		valueAtt += "(" + Integer.toString(x) + ",";
    		int y = (int) node.getValue().getY();
    		valueAtt += Integer.toString(y) + ")";
    		rootNodeEle.setAttribute("value", valueAtt);
    		
    		if(node.right == null) {
    			Element emptyChild = results.createElement("emptyChild");
    			rootNodeEle.appendChild(emptyChild);
    		} else {
    			Element childNode = printAvltreeHelper(node.right);
    			rootNodeEle.appendChild(childNode);
    		}
    		
    		if(node.left == null) {
    			Element emptyChild = results.createElement("emptyChild");
    			rootNodeEle.appendChild(emptyChild);
    		} else {
    			Element childNode = printAvltreeHelper(node.left);
    			rootNodeEle.appendChild(childNode);
    		}
    		
    		return rootNodeEle;
    	}
    }
    
    
    
}