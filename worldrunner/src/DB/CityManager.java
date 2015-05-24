package DB;

import java.util.ArrayList;
import java.util.List;

import DB.Model.City;
import DB.Model.Equipment;
import DB.Model.Player;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class CityManager {
	private static final String TABLE_CITY = "city";
	private static final String CITY_ID = "city_id";
	private static final String CITY_NAME = "city_name";
	
	public static void create(SQLiteDatabase db) {
		String CREATE_PLAYER_TABLE = "CREATE TABLE " + TABLE_CITY + "("
                + CITY_ID + " INTEGER PRIMARY KEY," + CITY_NAME + " TEXT" + ")";
		db.execSQL(CREATE_PLAYER_TABLE);
		createInitial(db, new City(1, "Beginner Land"));
		createInitial(db, new City(2, "Expert Land"));
	}
	
	private static void createInitial(SQLiteDatabase db, City city) {
		ContentValues values = new ContentValues();
		values.put(CITY_ID, city.cityId);
		values.put(CITY_NAME, city.cityName);
		db.insert(TABLE_CITY, null, values);
	}
	
	public static void drop(SQLiteDatabase db) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_CITY);
	}
	
	public static ArrayList<City> getCity(SQLiteDatabase db) {
		ArrayList<City> list = new ArrayList<City>();
		String select = "SELECT * FROM " + TABLE_CITY;
		Cursor cursor = db.rawQuery(select, null);
		if (cursor.moveToFirst()) {
			do {
				City city = new City(cursor.getInt(0),
						cursor.getString(1));
				list.add(city);
			} while (cursor.moveToNext());
		}
		return list;
	}
	
	public static int updateCity(SQLiteDatabase db, City city) {
		ContentValues values = new ContentValues();
		values.put(CITY_NAME, city.cityName);
		return db.update(TABLE_CITY, values, CITY_ID + " = ?", new String[] { String.valueOf(city.cityId) });
	}
	
	public static void addCity(SQLiteDatabase db, City city) {
		ContentValues values = new ContentValues();
		values.put(CITY_ID, city.cityId);
		values.put(CITY_NAME, city.cityName);
		db.insert(TABLE_CITY, null, values);
	}
	
	public static void deleteCity(SQLiteDatabase db, City city) {
		db.delete(TABLE_CITY, CITY_ID + " = ?", new String[] { String.valueOf(city.cityId) });
	}
}
