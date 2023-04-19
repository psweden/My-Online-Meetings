/**
 * 
 */
/**
 * @ author: Peter Albertsson
 * @ email: peter.albertsson63@gmail.com
 * @ Date: 19 april 2023
 * 
 * class-name: DBReset.java
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
/**
 * @author pealbert
 *
 */

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


	public class DBReset {
		
		/**
		 * 
		 */
		public DBReset() {
			// TODO Auto-generated constructor stub
		}

		
		/******************** if debug is set true then it will show all Logcat message ************/
		public static final boolean DEBUG = true;
		
		/******************** Logcat TAG ************/
		public static final String LOG_TAG = "DBReset";
		
		/******************** Table Fields ************/
		public static final String KEY_ID = "_id";	
		
		public static final String KEY_DB_VERSION = "meeting_version";
		
		public static final String KEY_DB_PHONENUMBER = "meeting_phonenumber";
		
		public static final String KEY_DB_LANGUAGE = "meeting_language";
		
		public static final String KEY_DB_HANDSFREE = "meeting_handsfree";
		
		public static final String KEY_DB_MUTED = "meeting_muted";
		
		public static final String KEY_DB_AUTOCALL = "meeting_autocall";
		
		
		/******************** Database Name ************/
		public static final String DATABASE_NAME = "DBResetDatabaseVer54"; // Meeting_Database
		
		/******************** Database Version (Increase one if want to also upgrade your database) ************/
		
		public int DATABASE_VERSION_SET = 1; // started at 1

		/** Table names */
		public static final String USER_RESET_TABLE = "tbl_user_reset";
		
		/******************** Set all table with comma seperated like USER_TABLE,ABC_TABLE ************/
		private static final String[] ALL_TABLES = { USER_RESET_TABLE};
		
		/** Create table syntax */
		private static final String USER_RESET_CREATE = 
				"create table tbl_user_reset(_id integer primary key autoincrement, meeting_version text, meeting_phonenumber text not null, meeting_language text not null, meeting_handsfree text not null, meeting_muted text not null, meeting_autocall text not null);"; //  not null

		private static final Context Context = null;
		
		/******************** Used to open database in syncronized way ************/
		private static DataBaseHelper DBHelper = null;

		
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
				super(context, DATABASE_NAME, null, DATABASE_VERSION_SET);
			}

			@Override
			public void onCreate(SQLiteDatabase db) {
				
				if (DEBUG)
					Log.i(LOG_TAG, "new create");
				try {
					db.execSQL(USER_RESET_CREATE);					

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
		 * Adding to Dabase
		 * |Version '5'| Phonenumber '0' | Language '0. | Car-Mode '0' | Muted '0'| Autocall '0'|
		 * 
		 * */
		public static void addUserData(UserReset uReset) {
	    	final SQLiteDatabase db = open();
	    	
	    	String version = sqlEscapeString(uReset.getDBVersion());
	    	String phonenumber = sqlEscapeString(uReset.getDBPhonenumber());
	    	String language = sqlEscapeString(uReset.getDBLanguage());
	    	String handsfree = sqlEscapeString(uReset.getDBHandsFree());
	    	String muted = sqlEscapeString(uReset.getDBMuted());
	    	String autocall = sqlEscapeString(uReset.getDBAutoCall());
			
			ContentValues cVal = new ContentValues();
			
			cVal.put(KEY_DB_VERSION, version);
			cVal.put(KEY_DB_PHONENUMBER, phonenumber);
			cVal.put(KEY_DB_LANGUAGE, language);
			cVal.put(KEY_DB_HANDSFREE, handsfree);
			cVal.put(KEY_DB_MUTED, muted);
			cVal.put(KEY_DB_AUTOCALL, autocall);
			db.insert(USER_RESET_TABLE, null, cVal);
	        db.close(); 
	    }
		
			
		/** can be USED in the application, so let it be here.
		 * 
		 * The method will getting all rows
		 * 
		 * */
	    public static List<UserReset> getAllUserData() {
	        List<UserReset> contactList = new ArrayList<UserReset>();
	        // Select All Query
	        String selectQuery = "SELECT  * FROM " + USER_RESET_TABLE;
	 
	        final SQLiteDatabase db = open();
	        Cursor cursor = db.rawQuery(selectQuery, null);
	 
	        // looping through all rows and adding to list
	        if (cursor.moveToFirst()) {
	            do {
	            	UserReset data = new UserReset();
	            	
	            	data.setID(Integer.parseInt(cursor.getString(0)));
	            	data.setDBVersion(cursor.getString(1));           	
	            	
	                // Adding contact to list
	                contactList.add(data);
	            } while (cursor.moveToNext());
	        }
	 
	        // return contact list
	        return contactList;
	    }
	    
	 
	    /** IN USED in the application
		 * 
		 * Will get the single attribute numer '5' and ID
		 * 
		 * */
	    public static UserReset getUserData(int id) {
	    	final SQLiteDatabase db = open();
	    	 
	        Cursor cursor = db.query(USER_RESET_TABLE, new String[] { 
	        		KEY_ID, KEY_DB_VERSION, KEY_DB_PHONENUMBER, KEY_DB_LANGUAGE, KEY_DB_HANDSFREE, KEY_DB_MUTED, KEY_DB_AUTOCALL}, 
	        		KEY_ID + "=?", new String[] { String.valueOf(id) }, null, null, null, null);
	        if (cursor != null)
	            cursor.moveToFirst();
	 
	        UserReset data = new UserReset(Integer.parseInt(cursor.getString(0)),cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6));
	        // return contact
	        
	        return data;
	    }
	    	    
	    /** USED in the application, so let it be here.
		 * 
		 * The method will insert new single number '5'
		 * 
		 * */
	    public static void updateInsert(int ids, String version){
	    	
	    	final SQLiteDatabase db = open();
	    	int id = ids;
	    	ContentValues con = new ContentValues();
	    	con.put(KEY_DB_VERSION, version);
	    	db.update(USER_RESET_TABLE, con, KEY_ID + "=" + id,null);
	    	db.close();
	    	
	    }
	    
	    /** USED in the application, so let it be here.
		 * 
		 * The method will insert new single phonenumber
		 * 
		 * */
	    public static void updateInsertPhoneNumber(int ids, String phonenumber){
	    	
	    	final SQLiteDatabase db = open();
	    	int id = ids;
	    	ContentValues con = new ContentValues();
	    	con.put(KEY_DB_PHONENUMBER, phonenumber);
	    	db.update(USER_RESET_TABLE, con, KEY_ID + "=" + id,null);
	    	db.close();
	    	
	    }
	    
	    /** USED in the application, so let it be here.
		 * 
		 * The method will insert new single language
		 * 
		 * */
	    public static void updateInsertLanguage(int ids, String language){
	    	
	    	final SQLiteDatabase db = open();
	    	int id = ids;
	    	ContentValues con = new ContentValues();
	    	con.put(KEY_DB_LANGUAGE, language);
	    	db.update(USER_RESET_TABLE, con, KEY_ID + "=" + id,null);
	    	db.close();
	    	
	    }
	    
	    /** USED in the application, so let it be here.
		 * 
		 * The method will insert new single carmode
		 * 
		 * */
	    public static void updateInsertHandsFree(int ids, String handsfree){
	    	
	    	final SQLiteDatabase db = open();
	    	int id = ids;
	    	ContentValues con = new ContentValues();
	    	con.put(KEY_DB_HANDSFREE, handsfree);
	    	db.update(USER_RESET_TABLE, con, KEY_ID + "=" + id,null);
	    	db.close();
	    	
	    }
	    
	    /** USED in the application, so let it be here.
		 * 
		 * The method will insert new single muted
		 * 
		 * */
	    public static void updateInsertMuted(int ids, String muted){
	    	
	    	final SQLiteDatabase db = open();
	    	int id = ids;
	    	ContentValues con = new ContentValues();
	    	con.put(KEY_DB_MUTED, muted);
	    	db.update(USER_RESET_TABLE, con, KEY_ID + "=" + id,null);
	    	db.close();
	    	
	    }
	    
	    /** USED in the application, so let it be here.
		 * 
		 * The method will insert new single autocall
		 * 
		 * */
	    public static void updateInsertAutoCall(int ids, String autocall){
	    	
	    	final SQLiteDatabase db = open();
	    	int id = ids;
	    	ContentValues con = new ContentValues();
	    	con.put(KEY_DB_AUTOCALL, autocall);
	    	db.update(USER_RESET_TABLE, con, KEY_ID + "=" + id,null);
	    	db.close();
	    	
	    }
	 
	    /** Can be USED in the application
		 * 
		 * Will delete single meetings
		 * 
		 * */
	    public static void deleteUserData(UserReset data) {
	    	final SQLiteDatabase db = open();
	        db.delete(USER_RESET_TABLE, KEY_ID + " = ?",
	                new String[] { String.valueOf(data.getID()) });
	        db.close();
	    }
	 
	        
	    /** USED in the application, so let it be here.
		 * 
		 * The method will count how many row there are.
		 * 
		 * */
	    public static int countDB(){
	    	
	    	final SQLiteDatabase db = open();
	    	
	    	String countQuery = "SELECT  * FROM " + USER_RESET_TABLE;
	        Cursor cursor = db.rawQuery(countQuery, null);
	        int cnt = cursor.getCount();
	        cursor.close();
	        return cnt;    	
	    	
	    }
	    
	    /** Can be USED in the application
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
	    
}
