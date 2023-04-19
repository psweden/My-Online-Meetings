
/**
 * 
 */
/**
 * @ author: Peter Albertsson
 * @ email: peter.albertsson63@gmail.com
 * @ Date: 19 april 2023
 * 
 * class-name: UserData.java
 * Project:  
 * Conference call
 * 
 * Description of the java-class:
 * The java-class will retrive and gettting, setting the 
 * attributes for SQLite.
 *
 * TODO: 
 * Nothing to do, the java-class is ready
 * 
 * 
 */
package com.example.my_online_meetings.src.sqlite;

public  class UserData {
	

	//private variables	
    int _id;
    String _name;
    String _email;
    String _location;
    String _title;
    String _today_date;
    String _start;
    String _end;
    String _dtmf;
    String _dbVersion;
    String _millisecond;
 
    // Empty constructor
    public UserData(){
 
    }
    // constructor
    public UserData(int id, String location, String title, String today_date, String start, String end, String dtmf, String millisecond){
        this._id = id;
        this._location = location;
        this._title = title;
        this._today_date = today_date;
        this._start = start;
        this._end = end;
        this._dtmf = dtmf;
        this._millisecond = millisecond;
    }
    
    // constructor
    public UserData(String location, String title, String today_date, String start, String end, String dtmf, String millisecond){
        this._location = location;
        this._title = title;
        this._today_date = today_date;
        this._start = start;
        this._end = end;
        this._dtmf = dtmf;
        this._millisecond = millisecond;
    }
    
 // constructor
    public UserData(String dbVersion){
        this._dbVersion = dbVersion;
    }
    
    // ------------ GETTING - METHODS --------------
        
    // getting ID
    public int getID(){
        return this._id;
    }
    
    // getting Location
    public String getLocation(){
        return this._location;
    }
    
    // getting Title
    public String getTitle(){
        return this._title;
    }
    
    // getting TodayDate
    public String getTodayDate(){
        return this._today_date;
    }
    
    // getting Start
    public String getStart(){
        return this._start;
    }
    
    // getting End
    public String getEnd(){
        return this._end;
    }
    
    // getting DTMF
    public String getDTMF(){
        return this._dtmf;
    }    

    // getting dbVersion
    public String getDBVersion(){
    	return this._dbVersion;
    }
    
    // getting millisecond (meeting starts)
    public String getMillisecond(){
    	return this._millisecond;
    }
    
    // ------------ SETTING - METHODS --------------
       
    
    // setting id
    public void setID(int id){
        this._id = id;
    } 
    
    // setting Location
    public void setLocation(String location){
    	this._location = location;
    }
    
    // setting Title
    public void setTitle(String title){
    	this._title = title;
    }
 
    // setting TodayDate
    public void setTodayDate(String todaydate){
    	this._today_date = todaydate;
    }
    
    // setting Start
    public void setStart(String start){
    	this._start = start;
    }
    
    // setting End
    public void setEnd(String end){
    	this._end = end;
    }
    
    // setting DTMF
    public void setDTMF(String dtmf){
    	this._dtmf = dtmf;
    }  
    
    // setting dbVersion
    public void setDBVersion(String dbVersion){
    	this._dbVersion = dbVersion;
    }
    
    // setting Millisecond
    public void setMillisecond(String millisecond){
    	this._millisecond = millisecond;
    }

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		
		return "UserInfo [location=" + _location + ", title=" + _title + ", todaydate=" + _today_date + ", start=" + _start + ", end=" + _end + ", dtmf=" + _dtmf + ", millisecond=" + _millisecond + "]";

	}
	
}
