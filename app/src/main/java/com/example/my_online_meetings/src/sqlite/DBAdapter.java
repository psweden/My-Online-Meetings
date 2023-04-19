
/**
 * 
 */
/**
 * @ author: Peter Albertsson
 * @ email: peter.albertsson63@gmail.com
 * @ Date: 19 april 2023
 * 
 * class-name: DBAdapter.java
 * Project:  
 * Conferece call
 * 
 * Description of the java-class:
 * The java-class will communicate with SQLite
 * will done update,insert and so on 
 *
 * TODO: 
 * Nothing to do, the java-class is ready to use.
 * 
 * 
 */
package com.example.my_online_meetings.src.sqlite;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;



public class DBAdapter {	

	
	/******************** if debug is set true then it will show all Logcat message ************/
	public static final boolean DEBUG = true;
	
	/******************** Logcat TAG ************/
	public static final String LOG_TAG = "DBAdapter";
	
	/******************** Table Fields ************/
	public static final String KEY_ID = "_id";	
	
	public static final String KEY_MEETING_LOCATION = "meeting_location";
	
	public static final String KEY_MEETING_TITLE = "meeting_title";
	
	public static final String KEY_MEETING_TODAY_DATE = "meeting_today_date";
	
	public static final String KEY_MEETING_START = "meeting_start";
	
	public static final String KEY_MEETING_END = "meeting_end";
	
	public static final String KEY_MEETING_DTMF = "meeting_dtmf";
	
	public static final String KEY_DB_VERSION = "meeting_version";
	
	public static final String KEY_DB_MILLISECOND = "meeting_millisecond";
	
	
	/******************** Database Name ************/
	public static final String DATABASE_NAME = "CapDBMeetingVer54"; // Meeting_Database
	
	/******************** Database Version (Increase one if want to also upgrade your database) ************/
	
	public int DATABASE_VERSION = 5;// = setVersionStr();// started at 1 | in txt-file from beginning

	/** Table names */
	public static final String USER_TABLE = "tbl_user";
	
	/******************** Set all table with comma seperated like USER_TABLE,ABC_TABLE ************/
	private static final String[] ALL_TABLES = { USER_TABLE};
	
	/** Create table syntax */
	private static final String USER_CREATE = 
			"create table tbl_user(_id integer primary key autoincrement, meeting_location text not null, meeting_title text not null, meeting_today_date text not null, meeting_start text not null, meeting_end text not null, meeting_dtmf text not null, meeting_version, meeting_millisecond text not null);";

	private static final Context Context = null;
	
	/******************** Used to open database in syncronized way ************/
	private static DataBaseHelper DBHelper = null;
	static SQLiteDatabase db;
	
	

	public DBAdapter() { // Constructor
	
		
	}
    /******************* Initialize database *************/
	public void init(Context context) {
		
		if (DBHelper == null) {
			if (DEBUG)
				Log.i("DBAdapter", context.toString());
			DBHelper = new DataBaseHelper(context);
		}		
		
	}
	
  /********************** Main Database creation INNER class ********************/
	private  class DataBaseHelper extends SQLiteOpenHelper {
		public DataBaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			
			if (DEBUG)
				Log.i(LOG_TAG, "new create");
			try {
				db.execSQL(USER_CREATE);
				

			} catch (Exception exception) {
				if (DEBUG)
					Log.i(LOG_TAG, "Exception onCreate() exception");
			}
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			if (DEBUG)
				Log.w(LOG_TAG, "Upgrading database from version" + oldVersion
						+ "to" + newVersion + "...");

			for (String table : ALL_TABLES) {
				Log.d("DB FOR LOOPEN........", "");
				db.execSQL("DROP TABLE IF EXISTS " + table);
			}
			onCreate(db);
		}

	} // Inner class closed
	
	
	/********************** Open database for insert,update,delete in syncronized manner ********************/
	private static synchronized SQLiteDatabase open() throws SQLException {
		return DBHelper.getWritableDatabase();
	}


	/************************ General functions**************************/
	
	
	/*********************** Escape string for single quotes (Insert,Update)************/
	private static String sqlEscapeString(String aString) {
		String aReturn = "";
		
		if (null != aString) {
			//aReturn = aString.replace("'", "''");
			aReturn = DatabaseUtils.sqlEscapeString(aString);
			// Remove the enclosing single quotes ...
			aReturn = aReturn.substring(1, aReturn.length() - 1);
		}
		
		return aReturn;
	}
	/*********************** UnEscape string for single quotes (show data)************/
	private static String sqlUnEscapeString(String aString) {
		
		String aReturn = "";
		
		if (null != aString) {
			aReturn = aString.replace("''", "'");
		}
		
		return aReturn;
	}
	
	
	/**
	 * @return ******************************************************************/
	
	
	/** IN USED in the application
	 * 
	 * Will save all meetings-attribute into the database
	 * 
	 * */
	public static void addUserData(UserData uData) {
    	final SQLiteDatabase db = open();
    	
    	String location = sqlEscapeString(uData.getLocation());
		String title = sqlEscapeString(uData.getTitle());
		String date = sqlEscapeString(uData.getTodayDate());
		String start = sqlEscapeString(uData.getStart());
		String end = sqlEscapeString(uData.getEnd());
		String dtmf = sqlEscapeString(uData.getDTMF());
		String millisecond = sqlEscapeString(uData.getMillisecond());
		
		ContentValues cVal = new ContentValues();
		
		cVal.put(KEY_MEETING_LOCATION, location);
		cVal.put(KEY_MEETING_TITLE, title);
		cVal.put(KEY_MEETING_TODAY_DATE, date);
		cVal.put(KEY_MEETING_START, start);
		cVal.put(KEY_MEETING_END , end);
		cVal.put(KEY_MEETING_DTMF, dtmf);
		cVal.put(KEY_DB_MILLISECOND, millisecond);
		
		db.insert(USER_TABLE, null, cVal);
        db.close(); // Closing database connection
    }
		 
	/** IN USED in the application
	 * 
	 * Will get the meetings-attribute
	 * 
	 * */
	public static UserData getUserData(int id) {
    	final SQLiteDatabase db = open();
    	 
        Cursor cursor = db.query(USER_TABLE, new String[] { 
        		KEY_ID, KEY_MEETING_LOCATION, KEY_MEETING_TITLE, KEY_MEETING_TODAY_DATE, KEY_MEETING_START, KEY_MEETING_END, KEY_MEETING_DTMF, KEY_DB_MILLISECOND}, 
        		KEY_ID + "=?", new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
 
        UserData data = new UserData(Integer.parseInt(cursor.getString(0)),cursor.getString(1),cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7));
        // return contact
        
        return data;
    }    
 
    /** IN USED in the application
	 * 
	 * Will get and show all meetings in the database
	 * 
	 * */
     public static List<UserData> getAllUserData() {
        List<UserData> contactList = new ArrayList<UserData>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + USER_TABLE;
 
        final SQLiteDatabase db = open();
        Cursor cursor = db.rawQuery(selectQuery, null);
 
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
            	UserData data = new UserData();
            	
            	data.setID(Integer.parseInt(cursor.getString(0)));
            	data.setLocation(cursor.getString(1));
            	data.setTitle(cursor.getString(2));            	
            	data.setTodayDate(cursor.getString(3));            	
            	data.setStart(cursor.getString(4));
            	data.setEnd(cursor.getString(5));
            	data.setDTMF(cursor.getString(6));  
            	data.setMillisecond(cursor.getString(7));
            	
                // Adding contact to list
                contactList.add(data);
            } while (cursor.moveToNext());
        }
 
        // return contact list
        return contactList;
    }   
    
    /** can be USED in the application, so let it be here.
	 * 
	 * The method will insert new meetings-strings
	 * 
	 * */
    public static void upDateMeetings(int ids, String location, String title, String date, String start, String end, String dtmf, String millisecond){
    	
    	final SQLiteDatabase db = open();
    	int id = ids;
    	ContentValues con = new ContentValues();
    	con.put(KEY_MEETING_LOCATION, location);
    	con.put(KEY_MEETING_TITLE, title);
    	con.put(KEY_MEETING_TODAY_DATE, date);
    	con.put(KEY_MEETING_START, start);
    	con.put(KEY_MEETING_END, end);
    	con.put(KEY_MEETING_DTMF, dtmf);
    	con.put(KEY_DB_MILLISECOND, millisecond);
    	db.update(USER_TABLE, con, KEY_ID + "=" + id,null);
    	db.close();
    	
    }    
    
    /** can be USED in the application, so let it be here.
	 * 
	 * The method will count how many row there are.
	 * 
	 * */
    public static int countDB(){
    	
    	final SQLiteDatabase db = open();
    	
    	String countQuery = "SELECT  * FROM " + USER_TABLE;
        Cursor cursor = db.rawQuery(countQuery, null);
        int cnt = cursor.getCount();
        cursor.close();
        return cnt;    	
    	
    }
    
    /** can be USED in the application, so let it be here.
	 * 
	 * The method will get the database name.
	 * 
	 * */
    public static String getDBName(){
    	//String dbName = null;
    	String dbName = DATABASE_NAME;   	
    	return dbName;
    }
    
    /** IN USED in the application
	 * 
	 * Will get the last db-version and show it.
	 * 
	 * */
    public int getSQliteDBVersion(){
    	
    	SQLiteDatabase db = open();    	
    	// Get last DB version
    	int DBVersion = db.getVersion();
    	String strOldVersion = Integer.toString(DBVersion);
    	Log.d("DBAdapter | The value is (getVersion): ", strOldVersion);    	    	
    	db.close();    	
    	return DBVersion;
    }
    
    /** can be USED in the application, so let it be here.
	 * 
	 * The method will delete rows in DB
	 * 
	 * */
    public static boolean deleteContact(long rowId) {
    	final SQLiteDatabase db = open();
        return db.delete(USER_TABLE, KEY_ID + "=" + rowId, null) > 0;
      }   
  
    /** IN USED in the application
	 * 
	 * Very important for the application to reset.
	 * The method will set the version to upgrade and downgrade
	 * 
	 * */
    public void updateDBVersion(){
    	
    	SQLiteDatabase db = open();
    	db.setVersion(4);
    	db.close();
    	
    }
}

// SAVED MEHTODS WHO WILL BE USED IN THE FUTURE


	/** NOT IN USED in the application
	 * 
	 * The method will set an instance to retrieve things in the innerclass
	 * 
	 * */
//  public void getAccessToInnerClass(){
//  	    	
//  	SQLiteDatabase db = null;
//  	DBAdapter.DataBaseHelper instance = new DataBaseHelper(Context);
//  	
//  }


//Deleting single contact
//public static void deleteUserData(UserData data) {
//	final SQLiteDatabase db = open();
//    db.delete(USER_TABLE, KEY_ID + " = ?",
//            new String[] { String.valueOf(data.getID()) });
//    db.close();
//}

// Updating single contact
//public static int updateUserData(UserData data) {
//	final SQLiteDatabase db = open();
//
//    ContentValues values = new ContentValues();
//    
//    values.put(KEY_MEETING_LOCATION, data.getLocation());        
//    values.put(KEY_MEETING_TITLE, data.getTitle());        
//    values.put(KEY_MEETING_TODAY_DATE, data.getTodayDate());        
//    values.put(KEY_MEETING_START, data.getStart());        
//    values.put(KEY_MEETING_END, data.getEnd());
//    values.put(KEY_MEETING_DTMF, data.getDTMF());
//
//    // updating row
//    return db.update(USER_TABLE, values, KEY_ID + " = ?",
//            new String[] { String.valueOf(data.getID()) });
//}

//public static void updateRow(long rowId, String location, String title, String date, String start, String end, String dtmf) {
//	final SQLiteDatabase db = open();
//
//ContentValues args = new ContentValues();
//args.put(KEY_MEETING_LOCATION, location);
//args.put(KEY_MEETING_TITLE, title);
//args.put(KEY_MEETING_TODAY_DATE, date);
//args.put(KEY_MEETING_START, start);
//args.put(KEY_MEETING_END , end);
//args.put(KEY_MEETING_DTMF, dtmf);
//
//db.update(USER_TABLE, args, "id=" + rowId, null);
//
//db.close(); // Closing database connection
//}














