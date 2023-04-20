/**
 * 
 */
/**
 * @ author: Peter Albertsson | Team mobile solutions | Capgemini Sverige AB
 * @ email: peter.albertsson@capgemini.com 
 * @ Date: 12 mar 2015
 * 
 * class-name: TodayTime.java
 * Project:  
 * Conference Call
 * 
 * Description of the java-class:
 * Will set all instance for the Today date and time
 *
 * TODO: 
 * Nothing to do is ready
 * 
 * 
 */
package com.example.my_online_meetings.src.Calendar;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.util.Log;

public class TodayTimeStamp {

	private int cYear;
	private int cMonth;
	private int cDay;
	private int cSubMonth;
	private String DateToday;

	/** IN USED in the application
	 * 
	 * The method set the date today
	 * 
	 * */
	public TodayTimeStamp() {
		// TODO Auto-generated constructor stub

		// Get the calendar instance formate to yyyy-mm-dd
		Calendar now = Calendar.getInstance();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

		// Format and get the time and date
		String nowDate = formatter.format(now.getTime());
		String[] separateCurrentDate = nowDate.split("-");
		String year = separateCurrentDate[0];
		String month = separateCurrentDate[1];
		String day = separateCurrentDate[2];

		this.cYear = Integer.parseInt(year);
		this.cMonth = Integer.parseInt(month);
		this.cDay = Integer.parseInt(day);

		int sub = 1;
		int subResult = 0;
		subResult = cMonth - sub;
		this.cSubMonth = 0 + subResult;

		this.cMonth = cSubMonth;

		this.DateToday = year + "-" + month + "-" + day;
	}
		
	public int getCYear(){
		
		return cYear;
	}
	
	public int getCMonth(){

		return cMonth;
	}
	
	public int getCDay(){
	
		return cDay;
	}
	
	public String getDateToday(){
		
		return DateToday;
	}	
	
	/** IN USED in the application
	 * 
	 * The method set the name of this day
	 * 
	 * */
	public String getTodayName(){
		
		Calendar now = Calendar.getInstance();
	    String[] strDays = new String[] { "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday" };
	    // Day_OF_WEEK starts from 1 while array index starts from 0
	    Log.d("Current day is : " + strDays[now.get(Calendar.DAY_OF_WEEK)], "");
	    
	    String tName = strDays[now.get(Calendar.DAY_OF_WEEK)-1];	    
	    Log.d("Today, name of the day is >>> ", tName);
	    
	    return tName;
		
	}
}
