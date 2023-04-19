/**
 * 
 */
/**
 * @ author: Peter Albertsson
 * @ email: peter.albertsson63@gmail.com
 * @ Date: 19 april 2023
 * 
 * class-name: WorldPhoneNumber.java
 * Project:  
 * Conference call
 * 
 * Description of the java-class:
 * The java-class will return phonenubmers to use when calling the live meetings.
 *
 * TODO: 
 * Nothing to do, the java-class is ready.
 * 
 * 
 */
package com.example.my_online_meetings.src.confcall;
/**
 * @author pealbert
 *
 */
public class WorldPhoneNumber {
	
	private String callNumber = "";

	/** IN USED in the application
	 * 
	 * It will get the right number world wide to join the live-meetings.
	 * 
	 */
	public WorldPhoneNumber() {
		// TODO Auto-generated constructor stub
	}
	
	public String getWorldNumberToAccessLiveMeetings(String callingNum){
	
	if(callingNum.equals("Australia")){
		
		this.callNumber = "+61292535960";
		
		}
	
	if(callingNum.equals("Belgium")){
		
		this.callNumber = "+3227081777";
		
		}
	
	if(callingNum.equals("Brazil")){
		
		this.callNumber = "+551133517067";
		
		}

	if(callingNum.equals("Canada")){
	
	this.callNumber = "+14162164137";
	
	}
	
	if(callingNum.equals("Germany")){
		
		this.callNumber = "+4921156613333";
		
		}
	
	if(callingNum.equals("Denmark")){
		
		this.callNumber = "+4539778800";
		
		}
	
	if(callingNum.equals("Finland")){
		
		this.callNumber = "+358945267900";
		
		}
	
	if(callingNum.equals("France")){
		
		this.callNumber = "+33170480663";
		
		}
	
	if(callingNum.equals("Hong-Kong")){
		
		this.callNumber = "+85221064260";
		
		}
	
	if(callingNum.equals("India")){
		
		this.callNumber = "18602660132";
		
		}
	
	if(callingNum.equals("Italy")){
		
		this.callNumber = "+390238591394";
		
		}
	
	if(callingNum.equals("Netherlands")){
		
		this.callNumber = "+31306899929";
		
		}
	
	if(callingNum.equals("Norway")){
		
		this.callNumber = "+4724127500";
		
		}
	
	if(callingNum.equals("Poland")){
		
		this.callNumber = "+48225844231";
		
		}
	
	if(callingNum.equals("Portugal")){
		
		this.callNumber = "+351213164052";
		
		}
	
	if(callingNum.equals("Sweden")){
		
		this.callNumber = "+46853684000";
		
		}
	
	if(callingNum.equals("Singapore")){
		
		this.callNumber = "+6563498447";
		
		}
	
	if(callingNum.equals("Spain")){
		
		this.callNumber = "+34913754149";
		
		}
	
	if(callingNum.equals("United-Kingdom")){
		
		this.callNumber = "+442076601217";
		
		}
	
	if(callingNum.equals("USA")){
		
		this.callNumber = "+17038652981";
		
		} 	
	
		return callNumber;
	}	
	
}

























