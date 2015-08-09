package metaModel;

import java.util.ArrayList;

import DB.Model.Monster;

// Paths between cities that players can take to 
// fight and grind monsters and reach different locations
public class Route {
	public int id;
	public String name;
	public String description;
	public int min;
	public int max;
	public int from;
	public int to;
	public int clear;
	public int quantity;
	// unique identifier for list of monsters for this route
	public int monsterRouteId;
	
	public Route(int routeId, String routeName, String description, int min, int max, int from, int to,
			int clear, int quantity, int monsterRouteId) {
		this.id = routeId;
		this.name = routeName;
		this.description = description;
		this.min = min;
		this.max = max;
		this.from = from;
		this.to = to;
		this.clear = clear;
		this.quantity = quantity;
		this.monsterRouteId = monsterRouteId;
	}
}
