package DB;

import java.util.ArrayList;
import java.util.List;

import DB.Model.Equipment;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Helper class for doing Equipment DB actions
 *
 */
public class EquipmentManager {
	private static final String TABLE_EQUIPMENT = "Equipment";
	private static final String ETID = "etid";
	private static final String PID = "pid";
	private static final String EID = "eid";
	private static final String PSTID = "pstid";
	private static final String NAME = "name";
	private static final String CATEGORY = "category";
	private static final String EQUIPPED = "equipped";
	private static final String LEADER = "leader";
	private static final String CURRENT_LEVEL = "current_level";
	private static final String CURRENT_EXP = "current_exp";
	private static final String CURRENT_SPEED = "current_speed";
	private static final String CURRENT_REACH = "current_reach";
	private static final String EAID = "eaid";

	/**
	 * Creates the DB
	 * @param db - the SQLiteDatabase to do queries on
	 */
	public static void create(SQLiteDatabase db) {
		String CREATE_EQUIPMENT_TABLE = "CREATE TABLE " + TABLE_EQUIPMENT + "("
                + ETID + " INTEGER PRIMARY KEY," + PID + " INTEGER,"
                + EID + " INTEGER," + PSTID + " INTEGER," + NAME + " TEXT," + CATEGORY + " INTEGER," +
                EQUIPPED +  " INTEGER, " + LEADER + " INTEGER," + CURRENT_LEVEL + " INTEGER," +
                CURRENT_EXP + " INTEGER," + CURRENT_SPEED + " INTEGER," +
                CURRENT_REACH + " INTEGER," + EAID + " INTEGER" + ")";
        db.execSQL(CREATE_EQUIPMENT_TABLE);
        createInitial(db, new Equipment(1, 1, 1, 0, "plain shoes", 1, 0, 0, 1, 0, 100, 100, 1));
        createInitial(db, new Equipment(2, 1, 2, 0, "plain shirt", 2, 0, 0, 1, 0, 100, 100, 1));
        createInitial(db, new Equipment(3, 1, 3, 0, "plain pants", 3, 0, 0, 1, 0, 100, 100, 1));
        createInitial(db, new Equipment(4, 1, 4, 0, "plain hat", 4, 0, 0, 1, 0, 100, 100, 1));
        createInitial(db, new Equipment(5, 1, 5, 0, "plain gloves", 5, 0, 0, 1, 0, 100, 100, 1));
        createInitial(db, new Equipment(6, 1, 1, 0, "plain shoes", 1, 0, 0, 1, 0, 100, 100, 1));
        createInitial(db, new Equipment(7, 1, 1, 0, "plain shoes", 1, 0, 0, 1, 0, 100, 100, 1));
        createInitial(db, new Equipment(8, 1, 1, 0, "plain shoes", 1, 0, 0, 1, 0, 100, 100, 1));
        createInitial(db, new Equipment(9, 1, 1, 0, "plain shoes", 1, 0, 0, 1, 0, 100, 100, 1));
        createInitial(db, new Equipment(10, 1, 1, 0, "plain shoes", 1, 0, 0, 1, 0, 100, 100, 1));
	}   
	
	/**
	 * Helps creates initial dummy data to be used in new DB instance
	 * @param db - connection to db
	 * @param equipment - Equipment model to be inserted into DB
	 */
	private static void createInitial(SQLiteDatabase db, Equipment equipment) {
		ContentValues values = new ContentValues();
		values = addContent(values, equipment);
		db.insert(TABLE_EQUIPMENT, null, values);
	}
	
	/**
	 * Drops this table
	 * @param db - connection to DB
	 */
	public static void drop(SQLiteDatabase db) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_EQUIPMENT);
	}
	
	/**
	 * Gets the list of all Equipment
	 * @param db - connection to DB
	 * @return List of all Equipment
	 */
	public static ArrayList<Equipment> getEquipment(SQLiteDatabase db) {
		ArrayList<Equipment> list = new ArrayList<Equipment>();
		String select = "SELECT * FROM " + TABLE_EQUIPMENT;
		Cursor cursor = db.rawQuery(select, null);
		if (cursor.moveToFirst()) {
			do {
				Equipment equipment = createEquipment(cursor);
				list.add(equipment);
			} while (cursor.moveToNext());
		}
		return list;
	}
	
	/**
	 * Updates the users equipment
	 * @param db - connection to the db
	 * @param equipment - equipment that was changed
	 * @return # of queries that were successfully updated
	 */
	public static int updateEquipment(SQLiteDatabase db, Equipment equipment) {
		ContentValues values = new ContentValues();
		values = addContent(values, equipment);
		return db.update(TABLE_EQUIPMENT, values, ETID + " = ?", new String[] { String.valueOf(equipment.getEtid()) });
	}
	
	/**
	 * Deletes the equipment
	 * @param db - connection to DB
	 * @param equipment - equipment that was deleted
	 */
	public static void deleteEquipment(SQLiteDatabase db, Equipment equipment) {
		db.delete(TABLE_EQUIPMENT, ETID + " = ?", new String[] { String.valueOf(equipment.getEtid()) });
	}
	
	/**
	 * Adds new equipment
	 * @param db - connection ot DB
	 * @param equipment - equipment to be added
	 */
	public static void addEquipment(SQLiteDatabase db, Equipment equipment) {
		ContentValues values = new ContentValues();
		values = addContent(values, equipment);
		db.insert(TABLE_EQUIPMENT, null, values);
	}
	
	/**
	 * Gets the list of equipped equipments
	 * @param db - connection to the DB
	 * @return list of equipment that are equipped
	 */
	public static ArrayList<Equipment> getEquipped(SQLiteDatabase db) {
		ArrayList<Equipment> list = new ArrayList<Equipment>();
		String select = "SELECT * FROM " + TABLE_EQUIPMENT + " WHERE " + EQUIPPED + "=1";
		Cursor cursor = db.rawQuery(select, null);
		if (cursor.moveToFirst()) {
			do {
				Equipment equipment = createEquipment(cursor);
				list.add(equipment);
			} while (cursor.moveToNext());
		}
		return list;
	}
	
	/**
	 * Returns a list of equipments that are in a certain category.
	 * @param db - connection to the DB
	 * @param category - the specific type of equipment
	 * @return list of equipment
	 */
	public static ArrayList<Equipment> getEquipmentCategory(SQLiteDatabase db, int category) {
		ArrayList<Equipment> list = new ArrayList<Equipment>();
		String select = "SELECT * FROM " + TABLE_EQUIPMENT + " WHERE " + EQUIPPED + "=0 AND " + CATEGORY + "=" + category;
		Cursor cursor = db.rawQuery(select, null);
		if (cursor.moveToFirst()) {
			do {
				Equipment equipment = createEquipment(cursor);
				list.add(equipment);
			} while (cursor.moveToNext());
		}
		Log.d("CATEGORY", "" + category);
		return list;
	}
	
	/**
	 * Creates a new Equipment with the data received from SQL query
	 * @param cursor - the container of the rows from SQL Queries
	 * @return a new Equipment
	 */
	private static Equipment createEquipment(Cursor cursor) {
		Equipment equipment = new Equipment();
		equipment.setEtid(cursor.getString(0));
		equipment.setPid(cursor.getString(1));
		equipment.setEid(cursor.getString(2));
		equipment.setPstid(cursor.getString(3));
		equipment.setName(cursor.getString(4));
		equipment.setCategory(cursor.getString(5));
		equipment.setEquipped(cursor.getString(6));
		equipment.setLeader(cursor.getString(7));
		equipment.setCurrent_level(cursor.getString(8));
		equipment.setCurrent_exp(cursor.getString(9));
		equipment.setCurrent_speed(cursor.getString(10));
		equipment.setCurrent_reach(cursor.getString(11));
		equipment.setEaid(cursor.getString(12));
		return equipment;
	}
	
	/**
	 * Add parameters into the ContentValues that are to be used to add data to the DB
	 * @param values - the model that stores the data to be used for query
	 * @param equipment - The equipment that is being stored in the DB
	 * @return Content Value that has the new stored information
	 */
	private static ContentValues addContent(ContentValues values, Equipment equipment) {	
		//values.put(ETID, equipment.getEtid());
		values.put(PID, equipment.getPid());
		values.put(EID, equipment.getEid());
		values.put(PSTID, equipment.getPstid());	
		values.put(NAME, equipment.getName());
		values.put(CATEGORY, equipment.getCategory());
		values.put(EQUIPPED, equipment.getEquipped());
		values.put(LEADER, equipment.getLeader());
		values.put(CURRENT_LEVEL, equipment.getCurrent_level());
		values.put(CURRENT_EXP, equipment.getCurrent_exp());
		values.put(CURRENT_SPEED, equipment.getCurrent_speed());
		values.put(CURRENT_REACH, equipment.getCurrent_reach());
		values.put(EAID, equipment.getEaid());
		return values;
	}
	
	
}
