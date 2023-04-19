/**
 * 
 */
/**
 * @ author: Peter Albertsson
 * @ email: peter.albertsson63@gmail.com
 * @ Date: 19 april 2023
 * 
 * class-name: TomorrowTimeStamp.java
 * Project:  
 * Conference Call
 * 
 * Description of the java-class:
 * Will set all instance for the Tomorrow date and time
 *
 * TODO: 
 * Nothing to do is ready
 * 
 * 
 */
package com.example.my_online_meetings.src.Calendar;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * @author pealbert
 *
 */
public class TomorrowTimeStamp {
	
	
	private int tYear;
	private int tMonth;
	private int tDay;
	private int tSubMonth;
	private String DateTomorrow;
	
	/** IN USED in the application
	 * 
	 * The method add 1 day forward to check tomorrows meetings
	 * 
	 * */
	public TomorrowTimeStamp(){
		
		// Get the calendar instance formate to yyyy-mm-dd
		Calendar now = Calendar.getInstance();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		// ADD and SET THE DATE ONE DAY FORWARD
		now.add(Calendar.DATE, 1);

		// Format and get the time and date
		String nowDate = formatter.format(now.getTime());
		String[] separateCurrentDate = nowDate.split("-");
		String year = separateCurrentDate[0];
		String month = separateCurrentDate[1];
		String day = separateCurrentDate[2];

		this.tYear = Integer.parseInt(year);
		this.tMonth = Integer.parseInt(month);
		this.tDay = Integer.parseInt(day);

		int sub = 1;
		int subResult = 0;
		subResult = tMonth - sub;
		this.tSubMonth = 0 + subResult;

		this.tMonth = tSubMonth;

		this.DateTomorrow = year + "-" + month + "-" + day;		
	}
	
	public int getTYear(){
		
		return tYear;
	}
	
	public int getTMonth(){

		return tMonth;
	}
	
	public int getTDay(){
	
		return tDay;
	}
	
	public String getDateTomorrow(){
		
		return DateTomorrow;
	}

}
