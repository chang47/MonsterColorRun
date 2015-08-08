package dbReference;

import java.util.ArrayList;
import java.util.List;

import metaModel.MetaAbility;
import DB.Model.Sticker;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class AbilitiesManager {
	private static final String ABILITIES_REFERENCE = "abilitiesReference";
	private static final String AID = "aid"; // ability ID 
	private static final String TYPE = "type";
	//private static final String LEVEL = "level";
	private static final String NAME = "name";
	private static final String DESCRIPTION = "description";
	private static final String STEPS = "steps";
	private static final String ATTRIBUTE = "attribute";
	private static final String MODIFIER= "modifier";
	private static final String DURATION = "duration";

	/**
	 * Creates the Table for Stickers
	 * @param db
	 */
	public static void create(SQLiteDatabase db) {
		String CREATE_STICKER_TABLE = "CREATE TABLE " + ABILITIES_REFERENCE + "("
                + AID + " INTEGER PRIMARY KEY," + TYPE + " INTEGER, " 
				+ NAME + " TEXT," + DESCRIPTION + " TEXT," + STEPS + " INTEGER, " + ATTRIBUTE 
				+ " INTEGER," + MODIFIER + " REAL," + DURATION + " INTEGER" + ")";
        db.execSQL(CREATE_STICKER_TABLE);
        
        createInitial(db, new MetaAbility(1, 1, "Fire Storm", "Does 1.5 Times monster's attack to all enemies", 200, 0, 1.5, -1));
        createInitial(db, new MetaAbility(2, 1, "Water Storm", "Does 1.5 Times monster's attack to all enemies", 200, 0, 1.5, -1));
        createInitial(db, new MetaAbility(3, 1, "Fire Storm", "Does 1.5 Times monster's attack to all enemies", 200, 0, 1.5, -1));
        createInitial(db, new MetaAbility(4, 1, "Fire Storm", "Does 1.5 Times monster's attack to all enemies", 200, 0, 1.5, -1));
        createInitial(db, new MetaAbility(5, 1, "Fire Storm", "Does 1.5 Times monster's attack to all enemies", 200, 0, 1.5, -1));
        createInitial(db, new MetaAbility(6, 2, "Fire Storm", "Does 1.5 Times monster's attack to all enemies", 200, 0, 1.5, -1));
        createInitial(db, new MetaAbility(7, 2, "Fire Storm", "Does 1.5 Times monster's attack to all enemies", 200, 0, 1.5, -1));
        createInitial(db, new MetaAbility(8, 2, "Fire Storm", "Does 1.5 Times monster's attack to all enemies", 200, 0, 1.5, -1));
        createInitial(db, new MetaAbility(9, 3, "Fire Storm", "Does 1.5 Times monster's attack to all enemies", 200, 0, 1.5, -1));
        
	}
	
	/**
	 * Helper method to help create initial stickers from DB setup
	 * @param db - connection to the DB
	 * @param sticker - Sticker model to be added to the DB
	 */
	private static void createInitial(SQLiteDatabase db, MetaAbility ability) {
		ContentValues values = new ContentValues();
		values = addContent(values, ability);
		db.insert(ABILITIES_REFERENCE, null, values);
	}
	
	/**
	 * Drops the Sticker table
	 * @param db
	 */
	public static void drop(SQLiteDatabase db) {
		db.execSQL("DROP TABLE IF EXISTS " + ABILITIES_REFERENCE);
	}
	
	/**
	 * Returns an ArrayList of all stickers
	 * @param db
	 * @return list of all stickers
	 */
	public static List<MetaAbility> getAbility(SQLiteDatabase db) {
		List<MetaAbility> list = new ArrayList<MetaAbility>();
		String select = "SELECT * FROM " + ABILITIES_REFERENCE;
		Cursor cursor = db.rawQuery(select, null);
		if (cursor.moveToFirst()) {
			do {
				int id = cursor.getInt(0);
				int type = cursor.getInt(1);
				String name = cursor.getString(2);
				String description = cursor.getString(3);
				int steps = cursor.getInt(4);
				int attribute = cursor.getInt(5);
				double modifier = cursor.getDouble(6);
				int duration = cursor.getInt(7);
						
				
				list.add(new MetaAbility(id, type, name, description, steps, attribute, modifier, duration));
			} while (cursor.moveToNext());
		}
		return list;
	}
	
	/**
	 * add new sticker to the DB
	 * @param db
	 * @param sticker - sticker to be added
	 */
	public static void addAbility(SQLiteDatabase db, MetaAbility ability) {
		ContentValues values = new ContentValues();
		values = addContent(values, ability);
		db.insert(ABILITIES_REFERENCE, null, values);
	}
	
	/**
	 * Add in a list of stickers to the dB
	 * @param db
	 * @param stickers - list of stickers to be added into the DB
	 */
	public static void addStickers(SQLiteDatabase db, List<MetaAbility> abilities) {
		for (MetaAbility ability : abilities) {
			addAbility(db, ability);
		}
	}
	
	/**
	 * Deletes the give sticker in the DB
	 * @param db
	 * @param sticker - sticker to be deleted
	 */
	public static void deleteAbility(SQLiteDatabase db, int uid) {
		db.delete(ABILITIES_REFERENCE, AID + " = ?", new String[] { String.valueOf(uid) });
	}
	
	/**
	 * Helps setup the meta ability to be used in an add query
	 * @param values - the thing to be added to
	 * @param sticker - the sticker that will be affected
	 * @return a ContentValues that have been updated
	 */
	private static ContentValues addContent(ContentValues values, MetaAbility ability) {
		values.put(AID, ability.id);
		values.put(TYPE, ability.type);
		values.put(NAME, ability.name);
		values.put(DESCRIPTION, ability.description);
		values.put(STEPS, ability.steps);
		values.put(ATTRIBUTE, ability.attribute);
		values.put(MODIFIER, ability.modifier);
		values.put(DURATION, ability.duration);
		return values;
	}
}
