package metaModel;

public class MetaRoute {
	public int id;
	public int from;
	public int to;
	public int routeId;
	public int clear;
	public int quantity;
	
	public MetaRoute(int id, int from, int to, int routeId, int clear, int quantity) {
		this.id = id;
		this.from = from;
		this.to = to;
		this.routeId = routeId;
		this.clear = clear;
		this.quantity = quantity;
	}
}
