package DB.Model;

import java.util.ArrayList;

// Paths between cities that players can take to 
// fight and grind monsters and reach different locations
public class Route {
	public int routeId;
	public String routeName;
	public int from;
	public int to;
	public ArrayList<Monster> monsters;
	
	public Route(int routeId, String routeName, int from, int to) {
		this.routeId = routeId;
		this.routeName = routeName;
		this.from = from;
		this.to = to;
		monsters = new ArrayList<Monster>();
	}
}
