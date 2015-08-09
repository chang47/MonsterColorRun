package dbReference;

import java.util.ArrayList;
import java.util.List;

import metaModel.City;
import metaModel.Route;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class RouteManager {
	private static final String TABLE_ROUTE = "route";
	private static final String ROUTE_ID = "route_id";
	private static final String ROUTE_NAME = "route_name";
	private static final String DESCRIPTION = "description";
	private static final String MIN = "min";
	private static final String MAX = "max";
	private static final String FROM = "from";
	private static final String TO = "to";
	private static final String CLEAR = "clear";
	private static final String QUANTITY = "quantity";
	private static final String MONSTER_ROUTE_ID = "monster_route_id";
	
	public static void create(SQLiteDatabase db) {
		String CREATE_PLAYER_TABLE = "CREATE TABLE " + TABLE_ROUTE + "("
                + ROUTE_ID + " INTEGER PRIMARY KEY," + ROUTE_NAME + " TEXT," +
				DESCRIPTION + " TEXT," + MIN + " INTEGER," + MAX + " INTEGER," +
                FROM + " INTEGER," + TO + " INTEGER," + CLEAR + " INTEGER," + QUANTITY +
                " INTEGER," + MONSTER_ROUTE_ID + "INTEGER)";
		db.execSQL(CREATE_PLAYER_TABLE);
		createInitial(db, new Route(1, "Sunny Road", "The sun brigthly shines on your first journey", 1, 3, 1, 2, 1, 10, 1));
		createInitial(db, new Route(1, "Sunny Road", "The sun brigthly shines on your first journey", 1, 3, 2, 1, 1, 10, 1));
	}
	
	private static void createInitial(SQLiteDatabase db, Route route) {
		ContentValues values = new ContentValues();
		values.put(ROUTE_ID, route.id);
		values.put(ROUTE_NAME, route.name);
		values.put(DESCRIPTION, route.description);
		values.put(MIN, route.min);
		values.put(MAX, route.max);
		values.put(FROM, route.from);
		values.put(TO, route.to);
		values.put(CLEAR, route.clear);
		values.put(QUANTITY, route.quantity);
		values.put(MONSTER_ROUTE_ID, route.monsterRouteId);
		db.insert(TABLE_ROUTE, null, values);
	}
	
	public static void drop(SQLiteDatabase db) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_ROUTE);
	}
	
	public static List<Route> getRoutes(SQLiteDatabase db) {
		List<Route> list = new ArrayList<Route>();
		String select = "SELECT * FROM " + TABLE_ROUTE;
		Cursor cursor = db.rawQuery(select, null);
		if (cursor.moveToFirst()) {
			do {
				int routeId = cursor.getInt(0);
				String routeName = cursor.getString(1);
				String description = cursor.getString(2);
				int min = cursor.getInt(3);
				int max = cursor.getInt(4);
				int from = cursor.getInt(5);
				int to = cursor.getInt(6);
				int clear = cursor.getInt(7);
				int quantity = cursor.getInt(8);
				int monsterRouteId = cursor.getInt(9);
				Route route = new Route(routeId, routeName, description, min, max, from, to, clear, quantity, monsterRouteId);
				list.add(route);
			} while (cursor.moveToNext());
		}
		return list;
	}
	
}
