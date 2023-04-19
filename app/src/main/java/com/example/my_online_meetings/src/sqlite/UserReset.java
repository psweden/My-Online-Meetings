/**
 * 
 */
/**
 * @ author: Peter Albertsson
 * @ email: peter.albertsson63@gmail.com
 * @ Date: 19 april 2023
 * 
 * class-name: UserReset.java
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
/**
 * @author pealbert
 *
 */


public  class UserReset {
	

	//private variables	
    int _id;
    String _dbVersion;
    String _dbPhonenumber;
    String _dbLanguage;
    String _dbHandsFree;
    String _dbMuted;
    String _autoCall;
 
    // Empty constructor
    public UserReset(){
 
    }
    // constructor
    public UserReset(int id, String dbVersion, String dbPhonenumber, String dbLanguage, String handsFree, String muted, String autocall){
        this._id = id;
        this._dbVersion = dbVersion;
        this._dbPhonenumber = dbPhonenumber;
        this._dbLanguage = dbLanguage;
        this._dbHandsFree = handsFree;
        this._dbMuted = muted;
        this._autoCall = autocall;
    }
    
    // constructor
    public UserReset(String dbVersion, String dbPhonenumber, String dbLanguage, String handsFree, String muted, String autocall){
    	this._dbVersion = dbVersion;
    	this._dbPhonenumber = dbPhonenumber;
    	this._dbLanguage = dbLanguage;
    	this._dbHandsFree = handsFree;
    	this._dbMuted = muted;
    	this._autoCall = autocall;
    }
 
    
    // ------------ GETTING - METHODS --------------
        
    // getting ID
    public int getID(){
        return this._id;
    }    

    // getting dbVersion
    public String getDBVersion(){
    	return this._dbVersion;
    }
    
    // getting dbPhonenumber
    public String getDBPhonenumber(){
    	return this._dbPhonenumber;
    }
    
    // getting dbLanguage
    public String getDBLanguage(){
    	return this._dbLanguage;
    }
    
    // getting dbCarMode
    public String getDBHandsFree(){
    	return this._dbHandsFree;
    }
    
    // getting dbMuted
    public String getDBMuted(){
    	return this._dbMuted;
    }
    
    // getting dbAutoCall
    public String getDBAutoCall(){
    	return this._autoCall;
    }
    
    // ------------ SETTING - METHODS --------------
       
    
    // setting id
    public void setID(int id){
        this._id = id;
    } 
        
    // setting dbVersion
    public void setDBVersion(String dbVersion){
    	this._dbVersion = dbVersion;
    }
    
    // setting dbPhonenumber
    public void setDBPhonenumber(String dbPhonenumber){
    	this._dbPhonenumber = dbPhonenumber;
    }
    
    // setting dbLanguage
    public void setDBLanguage(String dbLanguage){
    	this._dbLanguage = dbLanguage;
    }
    
    // setting dbCarMode
    public void setDBCarMode(String dbHandsFree){
    	this._dbHandsFree = dbHandsFree;
    }
    
    // setting dbMuted
    public void setDBMuted(String dbMuted){
    	this._dbMuted = dbMuted;
    }
    
    // setting dbAutoCall
    public void setDBAutoCall(String dbAutoCall){
    	this._autoCall = dbAutoCall;
    }

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		
		return "UserInfo [version=" + _dbVersion + ", phonenumber=" + _dbPhonenumber + ", language=" + _dbLanguage  + ", handsFree=" + _dbHandsFree + ", muted=" + _dbMuted + ", autocall=" + _autoCall +"]";

	}
	
}

