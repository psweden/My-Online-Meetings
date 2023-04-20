
/**
 * @ author: Peter Albertsson | Team mobile solutions | Capgemini Sverige AB
 * @ email: peter.albertsson@capgemini.com 
 * @ Date: 25 mar 2015
 * 
 * class-name: CompareMillisStartTime.java
 * Project:  
 * Conference Call APP (mobile application).
 * 
 * Description of the java-class:
 * This java-class has one method to pass and save all the database post temporary
 * into the java-class 'TemporaryDBUserData'. * 
 * The method will sort and pass all the milliseconds sorted into the java-class 'TemporaryDBUserData'.
 *
 * TODO: 
 * This java-class is finnished.
 * 
 */

package com.example.my_online_meetings.src.confcall;

import java.util.ArrayList;
import java.util.Arrays;
import com.example.my_online_meetings.src.sqlite.DBAdapter;
import com.example.my_online_meetings.src.sqlite.UserData;
import android.util.Log;

/**
 * @author pealbert
 *
 */
public class CompareMillisStartTime {
		
	/**
	 * 
	 */
	public CompareMillisStartTime() {
		// TODO Auto-generated constructor stub
		
	}
	
	/** IN USED in the application
	 *
	 * Description:
	 * This java-method will from the first, 
	 * pass all the database post to the java-class 'TemporaryDBUserData'
	 * 
	 * The method will even, retrieves all the 'millisecond' make them all to med in format long
	 * Then sort the millisecond, convert them back to format strings and pass them all sorted
	 * back to the java-class 'TemporaryDBUserData' to be sorted and updated in database DBAdapter-class.
	 * 
	 * This strings will contains all database post.
	 * */
	public void setLongArrayElements(int totalMeetings){ 
		TemporaryDBUserData temp = new TemporaryDBUserData();
		
		// Init a new ArrayList, setLongElement, to retrieve all millisecond.
		ArrayList<Long> setLongElement = new ArrayList<Long>();	
		// Get all meetings today and tomorrow
		int countM = totalMeetings;		
		int sizeP = DBAdapter.countDB();
		// Loop with the lenght of all meetings 'countM'
		for (int i = 1; i <= countM; i++) {	
			
			// Retrieve all UserData ID by ID >> 'i'
			UserData ud = DBAdapter.getUserData(i);
			// Get UserData all thats needed to do update to database DBAdapter and do sortings.
			String dbLocation = ud.getLocation();
			String dbTitle = ud.getTitle();
			String dbDate = ud.getTodayDate();
			String dbStart = ud.getStart();
			String dbEnd = ud.getEnd();
			String dbDtmf = ud.getDTMF();
			String dbMilliSecond = ud.getMillisecond();	
			
			String dbTotalString = dbLocation + "," + dbTitle + "," + dbDate + "," + dbStart + "," + dbEnd + "," + dbDtmf + "," + dbMilliSecond;
			Log.d("FROM DATABASE >> ", dbTotalString);
			
			// Save database-data temporary until sort is done
			temp.getTemporaryUserData(i, dbLocation, dbTitle, dbDate, dbStart, dbEnd, dbDtmf, dbMilliSecond);
			
			// Init a new long '_milliSecond' append and add all elements of dbMillisecond
			long _milliSecond = Long.valueOf(dbMilliSecond).longValue();
			// Add all '_milliSecond' into the ArrayList 'setLongElement'
			setLongElement.add(_milliSecond);
			
			// LogCat: Loop and show the ID, DB-Millisecond and the size of the ArrayList 'setLongElement'
			Log.d(getClass().getName(), "[ LOOOP: >> " + "| id: " + i + " | Millisecond: " + dbMilliSecond + " | Size: " + setLongElement.size());
		
		}
		
		// Initiate startMilliSArray is lenght of setLongLement arrays size
		long[] startMilliSArray = new long[setLongElement.size()];
		
		// loop the size of setLongElement
		for (int i = 0; i < setLongElement.size(); i++)
			
			// Copy and put all setLongElement's element into the startMilliSArray.
			startMilliSArray[i] = setLongElement.get(i);
			// LogCat: show how meny meetings there are today and tomorrow	
			Log.d(getClass().getName(), "[ TOTAL M TODAY AND TOMORROW: >> " + totalMeetings);
		
		 // LogCat: Show all the elements available in Arraylist, SetLongElement, UNSORTED!
	    for (long number : startMilliSArray) {
	    	Log.d(getClass().getName(), "[ 'UNSORTED' LONG ARRAY-LIST: >> " + number);	    	
	    }
	    
	    // sorting startMilliSArray
	    Arrays.sort(startMilliSArray);	  

	    // LogCat: Show all the elements available in Arraylist, SetLongElement, SORTED!
	    for (long number : startMilliSArray) {	    	
	    	Log.d(getClass().getName(), "[ 'SORTED' LONG ARRAY-LIST: >> " + number);	    	
	    }
	    
	    // LogCat: Show the size of the startMilliSArray
	    long test = startMilliSArray.length;		
		Log.d(getClass().getName(), "[ SIZE OF LONG ARRAY-LIST: >> " + test);
				
		// Create new arrays of string, append into the
		
		// Initiete a new arrays of strings with the lenght of the Long Array startMilliSArray!
		String[] stringMillis =new  String[startMilliSArray.length];
		
		// Loop the lengt of startMilliSArray
	    for(int i=0;i<startMilliSArray.length;i++)
	    {
	    	// Append all elements and convert Long Array into String Array
	    	stringMillis[i]=String.valueOf(startMilliSArray[i]);
	    	// LogCat: Show the Array of strings
	    	Log.d("[ CONVERT AND APPEND TO STRING ARRAY: >> ", stringMillis[i]);
	    	
	    	// Got all millisecond string into a string.
		    String compareMilliSecond = stringMillis[i];
		    
		    temp.matchMilliSecondAndSetIndex(i, compareMilliSecond);	    	
	    }	    		
	}
}

