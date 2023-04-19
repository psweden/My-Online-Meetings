

/**
 * @ author: Peter Albertsson
 * @ email: peter.albertsson63@gmail.com
 * @ Date: 19 april 2023
 * 
 * class-name: TempraryDBUserData.java
 * Project:  
 * Some text...
 * 
 * Description of the java-class:
 * This java-class will save all the database post temporary.
 * To be used and be sorted after DTStart in millisecond.
 * And do the update each by each, so all meetings will be shown correct in the users mobile phone
 *
 * TODO: 
 * This java-class is not to do. Is finnished
 * 
 * 
 */
package com.example.my_online_meetings.src.confcall;

import java.util.StringTokenizer;

import android.util.Log;
import com.example.my_online_meetings.src.sqlite.DBAdapter;

/**
 * @author pealbert
 *
 */
public class TemporaryDBUserData {
	
	private String spLocation = "", spTitle = "", spDate = "", spStart = "", spEnd = "", spDtmf = "", spMilliSecond = "";
	
	private String temp1 = "", temp2 = "", temp3 = "", temp4 = "", temp5 = "", temp6 = "", temp7 = "", temp8 = "", temp9 = "", temp10 = "", 
				   temp11 = "", temp12 = "", temp13 = "", temp14 = "", temp15 = "", temp16 = "", temp17 = "", temp18 = "", temp19 = "", temp20 = "";
	
	private String milli1 = "", milli2 = "", milli3 = "", milli4 = "", milli5 = "", milli6 = "", milli7 = "", milli8 = "", milli9 = "", milli10 = "", 
				   milli11 = "", milli12 = "", milli13 = "", milli14 = "", milli15 = "", milli16 = "", milli17 = "", milli18 = "", milli19 = "", milli20 = "";

	/**
	 * 
	 */
	public TemporaryDBUserData() {
		// TODO Auto-generated constructor stub
		
	}
	
	/** IN USED in the application
	 *
	 * Description:
	 * This java-method will split the temporary strings 'temp1'
	 * This strings will contains all database post.
	 * */
	public void stringTokenSplit(String tokenSplit){
		
		Log.d("stringTokenSplit >> ", tokenSplit);
	
		StringTokenizer st = new StringTokenizer(tokenSplit, ",");
		
		this.spLocation = st.nextToken();
		this.spTitle = st.nextToken();
		this.spDate = st.nextToken();
		this.spStart = st.nextToken();
		this.spEnd = st.nextToken();
		this.spDtmf = st.nextToken();
		this.spMilliSecond = st.nextToken();		
		
	}
	
	/** IN USED in the application
	 *
	 * Description:
	 * This java-method will pass all 'sorted' attributes into the database and done 'updates'
	 * */
	public void updateDataBase(int id, String dbLocation, String dbTitle, String dbDate, String dbStart, String dbEnd, String dbDtmf, String dbMillisecond){
		
		DBAdapter.upDateMeetings(id, dbLocation, dbTitle, dbDate, dbStart, dbEnd, dbDtmf, dbMillisecond);		
		
	}
	
	/** IN USED in the application
	 *
	 * Description:
	 * This java-method gets the all 'millisecond' from the Array with strings.
	 * The ID will be added with plus 1 and so on, The passed id will be 
	 * from digits '0' and abouve from the Arrays.
	 * 
	 * Note: This java-method will be done/runned second in this java-class.
	 *
	 * */
	public void matchMilliSecondAndSetIndex(int id, String millis){
		
		int ids = 1 + id;		
		Log.d("[ COMPARE >>> ", "| ID | " + ids + " | MILLIS | " + millis + " | ");
		
		setStringToDB(ids, millis);
		
	}
	
	/** IN USED in the application
	 *
	 * Description:
	 * This java-method 'Compare' the incoming 'millisecond' with 'milli1' and so on.
	 * When found the correct 'match' of millisecond.
	 * - Set the correct temporary string and ID's 
	 * - Do split String and pass ID and strings to the method 'updateDataBase'
	 *
	 * */
	public void setStringToDB(int id, String millis){
		
		if(milli1.equals(millis)){ 
			
			String splitString = this.temp1;
			stringTokenSplit(splitString);
			
			int ids = id;
			String setLocation = this.spLocation;
			String setTitle = this.spTitle;
			String setDate = this.spDate;
			String setStart = this.spStart;
			String setEnd = this.spEnd;
			String setDtmf = this.spDtmf;
			String setMillisecond = this.spMilliSecond;
			
			updateDataBase(ids, setLocation, setTitle, setDate, setStart, setEnd, setDtmf, setMillisecond);
		}
		
		if(milli2.equals(millis)){
			
			String splitString = this.temp2;
			stringTokenSplit(splitString);
			
			int ids = id;
			String setLocation = this.spLocation;
			String setTitle = this.spTitle;
			String setDate = this.spDate;
			String setStart = this.spStart;
			String setEnd = this.spEnd;
			String setDtmf = this.spDtmf;
			String setMillisecond = this.spMilliSecond;
			
			updateDataBase(ids, setLocation, setTitle, setDate, setStart, setEnd, setDtmf, setMillisecond);
		}
		
		if(milli3.equals(millis)){
			
			String splitString = this.temp3;
			stringTokenSplit(splitString);
			
			int ids = id;
			String setLocation = this.spLocation;
			String setTitle = this.spTitle;
			String setDate = this.spDate;
			String setStart = this.spStart;
			String setEnd = this.spEnd;
			String setDtmf = this.spDtmf;
			String setMillisecond = this.spMilliSecond;
			
			updateDataBase(ids, setLocation, setTitle, setDate, setStart, setEnd, setDtmf, setMillisecond);
		}
		
		if(milli4.equals(millis)){ 
			
			String splitString = this.temp4;
			stringTokenSplit(splitString);
			
			int ids = id;
			String setLocation = this.spLocation;
			String setTitle = this.spTitle;
			String setDate = this.spDate;
			String setStart = this.spStart;
			String setEnd = this.spEnd;
			String setDtmf = this.spDtmf;
			String setMillisecond = this.spMilliSecond;
			
			updateDataBase(ids, setLocation, setTitle, setDate, setStart, setEnd, setDtmf, setMillisecond);
		}
		
		if(milli5.equals(millis)){
			
			String splitString = this.temp5;
			stringTokenSplit(splitString);
			
			int ids = id;
			String setLocation = this.spLocation;
			String setTitle = this.spTitle;
			String setDate = this.spDate;
			String setStart = this.spStart;
			String setEnd = this.spEnd;
			String setDtmf = this.spDtmf;
			String setMillisecond = this.spMilliSecond;
			
			updateDataBase(ids, setLocation, setTitle, setDate, setStart, setEnd, setDtmf, setMillisecond);
		}
		
		if(milli6.equals(millis)){
			
			String splitString = this.temp6;
			stringTokenSplit(splitString);
			
			int ids = id;
			String setLocation = this.spLocation;
			String setTitle = this.spTitle;
			String setDate = this.spDate;
			String setStart = this.spStart;
			String setEnd = this.spEnd;
			String setDtmf = this.spDtmf;
			String setMillisecond = this.spMilliSecond;
			
			updateDataBase(ids, setLocation, setTitle, setDate, setStart, setEnd, setDtmf, setMillisecond);
		}
		
		if(milli7.equals(millis)){ 
			
			String splitString = this.temp7;
			stringTokenSplit(splitString);
			
			int ids = id;
			String setLocation = this.spLocation;
			String setTitle = this.spTitle;
			String setDate = this.spDate;
			String setStart = this.spStart;
			String setEnd = this.spEnd;
			String setDtmf = this.spDtmf;
			String setMillisecond = this.spMilliSecond;
			
			updateDataBase(ids, setLocation, setTitle, setDate, setStart, setEnd, setDtmf, setMillisecond);
		}
		
		if(milli8.equals(millis)){ 
			
			String splitString = this.temp8;
			stringTokenSplit(splitString);
			
			int ids = id;
			String setLocation = this.spLocation;
			String setTitle = this.spTitle;
			String setDate = this.spDate;
			String setStart = this.spStart;
			String setEnd = this.spEnd;
			String setDtmf = this.spDtmf;
			String setMillisecond = this.spMilliSecond;
			
			updateDataBase(ids, setLocation, setTitle, setDate, setStart, setEnd, setDtmf, setMillisecond);
		}
		
		if(milli9.equals(millis)){ 
			
			String splitString = this.temp9;
			stringTokenSplit(splitString);
			
			int ids = id;
			String setLocation = this.spLocation;
			String setTitle = this.spTitle;
			String setDate = this.spDate;
			String setStart = this.spStart;
			String setEnd = this.spEnd;
			String setDtmf = this.spDtmf;
			String setMillisecond = this.spMilliSecond;
			
			updateDataBase(ids, setLocation, setTitle, setDate, setStart, setEnd, setDtmf, setMillisecond);
		}
		
		if(milli10.equals(millis)){ 
			
			String splitString = this.temp10;
			stringTokenSplit(splitString);
			
			int ids = id;
			String setLocation = this.spLocation;
			String setTitle = this.spTitle;
			String setDate = this.spDate;
			String setStart = this.spStart;
			String setEnd = this.spEnd;
			String setDtmf = this.spDtmf;
			String setMillisecond = this.spMilliSecond;
			
			updateDataBase(ids, setLocation, setTitle, setDate, setStart, setEnd, setDtmf, setMillisecond);
		}
		
		if(milli11.equals(millis)){ 
			
			String splitString = this.temp11;
			stringTokenSplit(splitString);
			
			int ids = id;
			String setLocation = this.spLocation;
			String setTitle = this.spTitle;
			String setDate = this.spDate;
			String setStart = this.spStart;
			String setEnd = this.spEnd;
			String setDtmf = this.spDtmf;
			String setMillisecond = this.spMilliSecond;
			
			updateDataBase(ids, setLocation, setTitle, setDate, setStart, setEnd, setDtmf, setMillisecond);
		}
		
		if(milli12.equals(millis)){ 
			
			String splitString = this.temp12;
			stringTokenSplit(splitString);
			
			int ids = id;
			String setLocation = this.spLocation;
			String setTitle = this.spTitle;
			String setDate = this.spDate;
			String setStart = this.spStart;
			String setEnd = this.spEnd;
			String setDtmf = this.spDtmf;
			String setMillisecond = this.spMilliSecond;
			
			updateDataBase(ids, setLocation, setTitle, setDate, setStart, setEnd, setDtmf, setMillisecond);
		}
		
		if(milli13.equals(millis)){ 
			
			String splitString = this.temp13;
			stringTokenSplit(splitString);
			
			int ids = id;
			String setLocation = this.spLocation;
			String setTitle = this.spTitle;
			String setDate = this.spDate;
			String setStart = this.spStart;
			String setEnd = this.spEnd;
			String setDtmf = this.spDtmf;
			String setMillisecond = this.spMilliSecond;
			
			updateDataBase(ids, setLocation, setTitle, setDate, setStart, setEnd, setDtmf, setMillisecond);
		}
		
		if(milli14.equals(millis)){ 
			
			String splitString = this.temp14;
			stringTokenSplit(splitString);
			
			int ids = id;
			String setLocation = this.spLocation;
			String setTitle = this.spTitle;
			String setDate = this.spDate;
			String setStart = this.spStart;
			String setEnd = this.spEnd;
			String setDtmf = this.spDtmf;
			String setMillisecond = this.spMilliSecond;
			
			updateDataBase(ids, setLocation, setTitle, setDate, setStart, setEnd, setDtmf, setMillisecond);
		}
		
		if(milli15.equals(millis)){ 
			
			String splitString = this.temp15;
			stringTokenSplit(splitString);
			
			int ids = id;
			String setLocation = this.spLocation;
			String setTitle = this.spTitle;
			String setDate = this.spDate;
			String setStart = this.spStart;
			String setEnd = this.spEnd;
			String setDtmf = this.spDtmf;
			String setMillisecond = this.spMilliSecond;
			
			updateDataBase(ids, setLocation, setTitle, setDate, setStart, setEnd, setDtmf, setMillisecond);
		}
		
		if(milli16.equals(millis)){ 
			
			String splitString = this.temp16;
			stringTokenSplit(splitString);
			
			int ids = id;
			String setLocation = this.spLocation;
			String setTitle = this.spTitle;
			String setDate = this.spDate;
			String setStart = this.spStart;
			String setEnd = this.spEnd;
			String setDtmf = this.spDtmf;
			String setMillisecond = this.spMilliSecond;
			
			updateDataBase(ids, setLocation, setTitle, setDate, setStart, setEnd, setDtmf, setMillisecond);
		}
		
		if(milli17.equals(millis)){ 
			
			String splitString = this.temp17;
			stringTokenSplit(splitString);
			
			int ids = id;
			String setLocation = this.spLocation;
			String setTitle = this.spTitle;
			String setDate = this.spDate;
			String setStart = this.spStart;
			String setEnd = this.spEnd;
			String setDtmf = this.spDtmf;
			String setMillisecond = this.spMilliSecond;
			
			updateDataBase(ids, setLocation, setTitle, setDate, setStart, setEnd, setDtmf, setMillisecond);
		}
		
		if(milli18.equals(millis)){ 
			
			String splitString = this.temp18;
			stringTokenSplit(splitString);
			
			int ids = id;
			String setLocation = this.spLocation;
			String setTitle = this.spTitle;
			String setDate = this.spDate;
			String setStart = this.spStart;
			String setEnd = this.spEnd;
			String setDtmf = this.spDtmf;
			String setMillisecond = this.spMilliSecond;
			
			updateDataBase(ids, setLocation, setTitle, setDate, setStart, setEnd, setDtmf, setMillisecond);
		}
		
		if(milli19.equals(millis)){ 
			
			String splitString = this.temp19;
			stringTokenSplit(splitString);
			
			int ids = id;
			String setLocation = this.spLocation;
			String setTitle = this.spTitle;
			String setDate = this.spDate;
			String setStart = this.spStart;
			String setEnd = this.spEnd;
			String setDtmf = this.spDtmf;
			String setMillisecond = this.spMilliSecond;
			
			updateDataBase(ids, setLocation, setTitle, setDate, setStart, setEnd, setDtmf, setMillisecond);
		}
		
		if(milli20.equals(millis)){ 
			
			String splitString = this.temp20;
			stringTokenSplit(splitString);
			
			int ids = id;
			String setLocation = this.spLocation;
			String setTitle = this.spTitle;
			String setDate = this.spDate;
			String setStart = this.spStart;
			String setEnd = this.spEnd;
			String setDtmf = this.spDtmf;
			String setMillisecond = this.spMilliSecond;
			
			updateDataBase(ids, setLocation, setTitle, setDate, setStart, setEnd, setDtmf, setMillisecond);
		}
		
	}
	
	/** IN USED in the application
	 *
	 * Description:
	 * This java-method will save, temporary copies of all 'unsorted' 'temp1' db-posts (strings) from database.
	 * The attributes 'milli1' will be temporary copied to be compared to set the correct ID to be sorted.
	 * 
	 * Note: This java-method will be done/runned first of all in this java-class.
	 *
	 * */
	public void getTemporaryUserData(int id, String temp_location, String temp_title, String temp_date, String temp_start, String temp_end, String temp_dtmf, String temp_millisecond){
		
		if(id == 1){
			
			String tmLocation = temp_location;
			String tmTitle = temp_title;
			String tmDate = temp_date;
			String tmStart = temp_start;
			String tmEnd = temp_end;
			String tmDtmf = temp_dtmf;
			String tmMilliSecond = temp_millisecond;
			
			this.milli1 = tmMilliSecond;			
			this.temp1 = tmLocation + "," + tmTitle + "," + tmDate + "," + tmStart + "," + tmEnd + "," + tmDtmf + "," + tmMilliSecond;
			
		}
		
		if(id == 2){
			
			String tmLocation = temp_location;
			String tmTitle = temp_title;
			String tmDate = temp_date;
			String tmStart = temp_start;
			String tmEnd = temp_end;
			String tmDtmf = temp_dtmf;
			String tmMilliSecond = temp_millisecond;
			
			this.milli2 = tmMilliSecond;			
			this.temp2 = tmLocation + "," + tmTitle + "," + tmDate + "," + tmStart + "," + tmEnd + "," + tmDtmf + "," + tmMilliSecond;
			
		}
		
		if(id == 3){
			
			String tmLocation = temp_location;
			String tmTitle = temp_title;
			String tmDate = temp_date;
			String tmStart = temp_start;
			String tmEnd = temp_end;
			String tmDtmf = temp_dtmf;
			String tmMilliSecond = temp_millisecond;
			
			this.milli3 = tmMilliSecond;			
			this.temp3 = tmLocation + "," + tmTitle + "," + tmDate + "," + tmStart + "," + tmEnd + "," + tmDtmf + "," + tmMilliSecond;
			
		}
		
		if(id == 4){
			
			String tmLocation = temp_location;
			String tmTitle = temp_title;
			String tmDate = temp_date;
			String tmStart = temp_start;
			String tmEnd = temp_end;
			String tmDtmf = temp_dtmf;
			String tmMilliSecond = temp_millisecond;
			
			this.milli4 = tmMilliSecond;			
			this.temp4 = tmLocation + "," + tmTitle + "," + tmDate + "," + tmStart + "," + tmEnd + "," + tmDtmf + "," + tmMilliSecond;
			
		}
		
		if(id == 5){
			
			String tmLocation = temp_location;
			String tmTitle = temp_title;
			String tmDate = temp_date;
			String tmStart = temp_start;
			String tmEnd = temp_end;
			String tmDtmf = temp_dtmf;
			String tmMilliSecond = temp_millisecond;
			
			this.milli5 = tmMilliSecond;			
			this.temp5 = tmLocation + "," + tmTitle + "," + tmDate + "," + tmStart + "," + tmEnd + "," + tmDtmf + "," + tmMilliSecond;
			
		}
		
		if(id == 6){
			
			String tmLocation = temp_location;
			String tmTitle = temp_title;
			String tmDate = temp_date;
			String tmStart = temp_start;
			String tmEnd = temp_end;
			String tmDtmf = temp_dtmf;
			String tmMilliSecond = temp_millisecond;
			
			this.milli6 = tmMilliSecond;			
			this.temp6 = tmLocation + "," + tmTitle + "," + tmDate + "," + tmStart + "," + tmEnd + "," + tmDtmf + "," + tmMilliSecond;
			
		}
		
		if(id == 7){
			
			String tmLocation = temp_location;
			String tmTitle = temp_title;
			String tmDate = temp_date;
			String tmStart = temp_start;
			String tmEnd = temp_end;
			String tmDtmf = temp_dtmf;
			String tmMilliSecond = temp_millisecond;
			
			this.milli7 = tmMilliSecond;			
			this.temp7 = tmLocation + "," + tmTitle + "," + tmDate + "," + tmStart + "," + tmEnd + "," + tmDtmf + "," + tmMilliSecond;
			
		}
		
		if(id == 8){
			
			String tmLocation = temp_location;
			String tmTitle = temp_title;
			String tmDate = temp_date;
			String tmStart = temp_start;
			String tmEnd = temp_end;
			String tmDtmf = temp_dtmf;
			String tmMilliSecond = temp_millisecond;
			
			this.milli8 = tmMilliSecond;			
			this.temp8 = tmLocation + "," + tmTitle + "," + tmDate + "," + tmStart + "," + tmEnd + "," + tmDtmf + "," + tmMilliSecond;
			
		}
		
		if(id == 9){
			
			String tmLocation = temp_location;
			String tmTitle = temp_title;
			String tmDate = temp_date;
			String tmStart = temp_start;
			String tmEnd = temp_end;
			String tmDtmf = temp_dtmf;
			String tmMilliSecond = temp_millisecond;
			
			this.milli9 = tmMilliSecond;			
			this.temp9 = tmLocation + "," + tmTitle + "," + tmDate + "," + tmStart + "," + tmEnd + "," + tmDtmf + "," + tmMilliSecond;
			
		}
		
		if(id == 10){
			
			String tmLocation = temp_location;
			String tmTitle = temp_title;
			String tmDate = temp_date;
			String tmStart = temp_start;
			String tmEnd = temp_end;
			String tmDtmf = temp_dtmf;
			String tmMilliSecond = temp_millisecond;
			
			this.milli10 = tmMilliSecond;			
			this.temp10 = tmLocation + "," + tmTitle + "," + tmDate + "," + tmStart + "," + tmEnd + "," + tmDtmf + "," + tmMilliSecond;
			
		}
		
		if(id == 11){
			
			String tmLocation = temp_location;
			String tmTitle = temp_title;
			String tmDate = temp_date;
			String tmStart = temp_start;
			String tmEnd = temp_end;
			String tmDtmf = temp_dtmf;
			String tmMilliSecond = temp_millisecond;
			
			this.milli11 = tmMilliSecond;			
			this.temp11 = tmLocation + "," + tmTitle + "," + tmDate + "," + tmStart + "," + tmEnd + "," + tmDtmf + "," + tmMilliSecond;
			
		}
		
		if(id == 12){
			
			String tmLocation = temp_location;
			String tmTitle = temp_title;
			String tmDate = temp_date;
			String tmStart = temp_start;
			String tmEnd = temp_end;
			String tmDtmf = temp_dtmf;
			String tmMilliSecond = temp_millisecond;
			
			this.milli12 = tmMilliSecond;			
			this.temp12 = tmLocation + "," + tmTitle + "," + tmDate + "," + tmStart + "," + tmEnd + "," + tmDtmf + "," + tmMilliSecond;
			
		}
		
		if(id == 13){
			
			String tmLocation = temp_location;
			String tmTitle = temp_title;
			String tmDate = temp_date;
			String tmStart = temp_start;
			String tmEnd = temp_end;
			String tmDtmf = temp_dtmf;
			String tmMilliSecond = temp_millisecond;
			
			this.milli13 = tmMilliSecond;			
			this.temp13 = tmLocation + "," + tmTitle + "," + tmDate + "," + tmStart + "," + tmEnd + "," + tmDtmf + "," + tmMilliSecond;
			
		}
		
		if(id == 14){
			
			String tmLocation = temp_location;
			String tmTitle = temp_title;
			String tmDate = temp_date;
			String tmStart = temp_start;
			String tmEnd = temp_end;
			String tmDtmf = temp_dtmf;
			String tmMilliSecond = temp_millisecond;
			
			this.milli14 = tmMilliSecond;			
			this.temp14 = tmLocation + "," + tmTitle + "," + tmDate + "," + tmStart + "," + tmEnd + "," + tmDtmf + "," + tmMilliSecond;
			
		}
		
		if(id == 15){
			
			String tmLocation = temp_location;
			String tmTitle = temp_title;
			String tmDate = temp_date;
			String tmStart = temp_start;
			String tmEnd = temp_end;
			String tmDtmf = temp_dtmf;
			String tmMilliSecond = temp_millisecond;
			
			this.milli15 = tmMilliSecond;			
			this.temp15 = tmLocation + "," + tmTitle + "," + tmDate + "," + tmStart + "," + tmEnd + "," + tmDtmf + "," + tmMilliSecond;
			
		}
		
		if(id == 16){
			
			String tmLocation = temp_location;
			String tmTitle = temp_title;
			String tmDate = temp_date;
			String tmStart = temp_start;
			String tmEnd = temp_end;
			String tmDtmf = temp_dtmf;
			String tmMilliSecond = temp_millisecond;
			
			this.milli16 = tmMilliSecond;			
			this.temp16 = tmLocation + "," + tmTitle + "," + tmDate + "," + tmStart + "," + tmEnd + "," + tmDtmf + "," + tmMilliSecond;
			
		}
		
		if(id == 17){
			
			String tmLocation = temp_location;
			String tmTitle = temp_title;
			String tmDate = temp_date;
			String tmStart = temp_start;
			String tmEnd = temp_end;
			String tmDtmf = temp_dtmf;
			String tmMilliSecond = temp_millisecond;
			
			this.milli17 = tmMilliSecond;			
			this.temp17 = tmLocation + "," + tmTitle + "," + tmDate + "," + tmStart + "," + tmEnd + "," + tmDtmf + "," + tmMilliSecond;
			
		}
		
		if(id == 18){
			
			String tmLocation = temp_location;
			String tmTitle = temp_title;
			String tmDate = temp_date;
			String tmStart = temp_start;
			String tmEnd = temp_end;
			String tmDtmf = temp_dtmf;
			String tmMilliSecond = temp_millisecond;
			
			this.milli18 = tmMilliSecond;			
			this.temp18 = tmLocation + "," + tmTitle + "," + tmDate + "," + tmStart + "," + tmEnd + "," + tmDtmf + "," + tmMilliSecond;
			
		}
		
		if(id == 19){
			
			String tmLocation = temp_location;
			String tmTitle = temp_title;
			String tmDate = temp_date;
			String tmStart = temp_start;
			String tmEnd = temp_end;
			String tmDtmf = temp_dtmf;
			String tmMilliSecond = temp_millisecond;
			
			this.milli19 = tmMilliSecond;			
			this.temp19 = tmLocation + "," + tmTitle + "," + tmDate + "," + tmStart + "," + tmEnd + "," + tmDtmf + "," + tmMilliSecond;
			
		}
		
		if(id == 20){
			
			String tmLocation = temp_location;
			String tmTitle = temp_title;
			String tmDate = temp_date;
			String tmStart = temp_start;
			String tmEnd = temp_end;
			String tmDtmf = temp_dtmf;
			String tmMilliSecond = temp_millisecond;
			
			this.milli20 = tmMilliSecond;			
			this.temp20 = tmLocation + "," + tmTitle + "," + tmDate + "," + tmStart + "," + tmEnd + "," + tmDtmf + "," + tmMilliSecond;
			
		}
	}	

}
