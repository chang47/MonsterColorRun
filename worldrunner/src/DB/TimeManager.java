package DB;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import DB.Model.RunningLog;
import DB.Model.Sticker;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class TimeManager {
	//TODO needs ability level
	private static final String TABLE_TIMER = "Time";
	private static final String ID = "id";
	private static final String STEPS = "steps";
	private static final String DAY = "day";
	private static final String MONTH = "month";
	private static final String YEAR = "year";
	
	/**
	 * Creates the Table for Stickers
	 * @param db
	 */
	public static void create(SQLiteDatabase db) {
		String CREATE_TIMER_TABLE = "CREATE TABLE " + TABLE_TIMER+ "("
                + ID + " INTEGER PRIMARY KEY," + STEPS + " INTEGER,"
                + DAY + " INTEGER," + MONTH + " INTEGER," + YEAR + " INTEGER"
                + ")";
        db.execSQL(CREATE_TIMER_TABLE);
	}
	
	/**
	 * Drops the Time table
	 * @param db
	 */
	public static void drop(SQLiteDatabase db) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_TIMER);
	}
	
	/**
	 * Returns an ArrayList of all time
	 * @param db
	 * @return list of all stickers
	 */
	public static List<RunningLog> getTimes(SQLiteDatabase db) {
		List<RunningLog> list = new ArrayList<RunningLog>();
		String select = "SELECT * FROM " + TABLE_TIMER;
		Cursor cursor = db.rawQuery(select, null);
		if (cursor.moveToFirst()) {
			do {
				int steps = cursor.getInt(0);
				int day = cursor.getInt(1);
				int month = cursor.getInt(2);
				int year = cursor.getInt(3);
				RunningLog run = new RunningLog(steps, day, month, year);
				list.add(run);
			} while (cursor.moveToNext());
		}
		Collections.sort(list);
		return list;
	}
	
	/**
	 * add new running log to the DB
	 * @param db
	 * @param sticker - sticker to be added
	 */
	public static void addTime(SQLiteDatabase db, RunningLog run) {
		ContentValues values = new ContentValues();
		values = addContent(values, run);
		db.insert(TABLE_TIMER, null, values);
	}


	/**
	 * Helps setup the sticker to be used in an add query
	 * @param values - the thing to be added to
	 * @param sticker - the sticker that will be affected
	 * @return a ContentValues that have been updated
	 */
	private static ContentValues addContent(ContentValues values, RunningLog run) {
		//values.put(ID, sticker.pid); // not needed
		values.put(STEPS, run.getSteps());
		values.put(DAY, run.getDay());
		values.put(MONTH, run.getMonth());
		values.put(YEAR, run.getYear());
		return values;
	}
	
}
