package DB;

import java.util.ArrayList;

import DB.Model.City;
import DB.Model.Route;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class RouteManager {
	private static final String TABLE_ROUTE = "route";
	private static final String ROUTE_ID = "route_id";
	private static final String ROUTE_NAME = "route_name";
	private static final String FROM_CITY_ID = "from_city_id";
	private static final String TO_CITY_ID = "to_city_id";
	
	public static void create(SQLiteDatabase db) {
		String CREATE_PLAYER_TABLE = "CREATE TABLE " + TABLE_ROUTE + "("
                + ROUTE_ID + " INTEGER PRIMARY KEY," + ROUTE_NAME + " TEXT," +
				FROM_CITY_ID + " INTEGER," + TO_CITY_ID + " INTEGER)";
		db.execSQL(CREATE_PLAYER_TABLE);
		createInitial(db, new Route(1, "Beginner Route", 1, 2));
		createInitial(db, new Route(2, "Beginner Route", 2, 1));
	}
	
	private static void createInitial(SQLiteDatabase db, Route route) {
		ContentValues values = new ContentValues();
		values.put(ROUTE_ID, route.routeId);
		values.put(ROUTE_NAME, route.routeName);
		values.put(FROM_CITY_ID, route.from);
		values.put(TO_CITY_ID, route.to);
		db.insert(TABLE_ROUTE, null, values);
	}
	
	public static void drop(SQLiteDatabase db) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_ROUTE);
	}
	
	public static ArrayList<Route> getRoutes(SQLiteDatabase db) {
		ArrayList<Route> list = new ArrayList<Route>();
		String select = "SELECT * FROM " + TABLE_ROUTE;
		Cursor cursor = db.rawQuery(select, null);
		if (cursor.moveToFirst()) {
			do {
				Route route = new Route();
				route.routeId = cursor.getInt(0);
				route.routeName = cursor.getString(1);
				route.from = cursor.getInt(2);
				route.to = cursor.getInt(3);
				list.add(route);
			} while (cursor.moveToNext());
		}
		return list;
	}
	
}
