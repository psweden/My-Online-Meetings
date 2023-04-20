
/**
 * 
 */
/**
 * @ author: Peter Albertsson | Team mobile solutions | Capgemini Sverige AB
 * @ email: peter.albertsson@capgemini.com 
 * @ Date: 8 mar 2015
 * 
 * class-name: SettingsActivity.java
 * Project:  
 * Conferece call
 * 
 * Description of the java-class:
 * Will set settings in the application.
 *
 * TODO: 
 * Nothing to do, the java-class is ready to use.
 * 
 * 
 */
package com.example.my_online_meetings.src.confcall;

import com.example.my_online_meetings.src.sqlite.DBReset;
import com.example.my_online_meetings.src.sqlite.UserReset;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.my_online_meetings.R;

public class SettingsActivity extends Activity {
	
	private String setPhonenumber = "", setLanguageCountries = "", setAutoCall = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		
		// Show the '<' in the Action-Bar | In main-screen > exit();
		getActionBar().setDisplayHomeAsUpEnabled(true);		
		
		// Set this statement to dont rotate screen
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		
		// Set text into the Settingsview
		TextView tv = (TextView) findViewById(R.id.tv_handsfreeinfo);	
		tv.setText(Html.fromHtml("<h3>Handsfree</h3> Your meetings will be"
		+ " auto synced and added to your phones contact list. So you easy" 
		+ " can join the meetings from your" + "<b>" + " cars phonebook." + "</b>" +" You will find this new contact on the top in your" 
        + " contactlist." + "<br>" + "Like this format >> " + "<b>" + "A01-Meeting-12:30" + "</b>" 
		+ "<br>" + "<b>" + "Access Handsfree" + "</b>" + " in the drop-down menu"));
		
		// Set text into the settingsview
		TextView tv2 = (TextView) findViewById(R.id.tv_autocallinfo);	
		tv2.setText(Html.fromHtml("<b>" + "Note: " + "</b>" +"Will do autocall when meeting starts"));
		
		// Get database instances
		UserReset urData = DBReset.getUserData(1);
		String autocallData = urData.getDBAutoCall();
		String countryData = urData.getDBLanguage();
		String phonenumberData = urData.getDBPhonenumber();
		
		if(autocallData.equals("0")){
			
			autocallData = "No | Off";
		}
		if(autocallData.equals("1")){
			
			autocallData = "Yes | On";
		}
		
		if(countryData.equals("0")){
			
			countryData = "None saved!";
		}
		if(phonenumberData.equals("0")){
			
			phonenumberData = "None saved!";
		}		
		
		// Set text into settingsview
		TextView tv3 = (TextView) findViewById(R.id.tv_settingsinfo);	
		tv3.setText(Html.fromHtml("<b>" + "Settings, last saved:" + "</b>" + "<br>" 
		+"<b>" + "Autocall: " + "</b>" + "<font color=\"#0000FF\">" + autocallData + "</font>" + "<br>"
		+"<b>" + "Country: " + "</b>" + "<font color=\"#0000FF\">" + countryData + "</font>" + "<br>"
		+"<b>" + "Phonenumber: " + "</b>" + "<font color=\"#0000FF\">" + phonenumberData + "</font>"));	
		
		// Add button Save
		Button bsave = (Button) findViewById(R.id.b_save);	
		
		// Set Radiobutton yes or no after last saved settings
	    UserReset uData = DBReset.getUserData(1);
	    String autoCallTrue = uData.getDBAutoCall();
		RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioGroup1);
		if(!autoCallTrue.equals("0")){		    	
	    	radioGroup.check(R.id.autocall_yes);		    	
	    }
	    if(autoCallTrue.equals("0")){		    	
	    	radioGroup.check(R.id.autocall_no);		    
	    	}

		// Add spinner for choose countries
		Spinner spinner = (Spinner)findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, 
        R.array.countries, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        
        /** IN USED in the application
    	 * 
    	 * Set spinner 'choose countries'
    	 * 
    	 * */
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            
        	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Object item = parent.getItemAtPosition(position);
            	WorldPhoneNumber num = new WorldPhoneNumber();
                String country = (String ) parent.getItemAtPosition(position);
                
                if(!country.equals("Select Countries")){
           
                String number = num.getWorldNumberToAccessLiveMeetings(country);
                
                setPhonenumber = number;
                setLanguageCountries = country;
                
                }else{
                	
                	setPhonenumber = "";
                    setLanguageCountries = "";
                	
                }
                
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        
        /** IN USED in the application
    	 * 
    	 * Button 'Save' alert savesettings
    	 * 
    	 * */
        bsave.setOnClickListener(new View.OnClickListener() {
	         public void onClick(View arg0) {

	        	 // DO SOMETHING
	        	 alertSaveSettings();
	         }
	      });
        
        
	}
	
	/** IN USED in the application
	 * 
	 * Insert and save to Database reset.
	 * 
	 * */
	public void setSavedItem(){		
		
		int index = 1;
		
		String phonenumber = this.setPhonenumber;
		String languageContry = this.setLanguageCountries;

		String autocall = this.setAutoCall;
		
		if(!phonenumber.equals("")){
			
			DBReset.updateInsertPhoneNumber(index, phonenumber);
		}
		
		if(!languageContry.equals("")){
					
					DBReset.updateInsertLanguage(index, languageContry);
				}
		
		if(!autocall.equals("")){
			
			DBReset.updateInsertAutoCall(index, autocall);
		}
		
	}
		
	/** IN USED in the application
	 * 
	 * Alerts, save the settings and get back to MainActivity
	 * 
	 * */
	public void alertSaveSettings(){

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
	    builder.setMessage("Do you want to save this settings?").setCancelable(false)
	    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int id) {
	        	
	        	setSavedItem();
	        	Intent i = new Intent(SettingsActivity.this, MainActivity.class);
	        	startActivity(i);
	         }
	           })
	    .setNegativeButton("No", new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int id) {
	                    dialog.cancel();
	               }
	           });
	    AlertDialog alert = builder.create();
	    alert.show();

	}
	
	/** IN USED in the application
	 * 
	 * Autocall yes or no
	 * 
	 * */
	public void onRadioButtonClicked(View view) {
	    // Is the button now checked?
	    boolean checked = ((RadioButton) view).isChecked();  
	     
	    // Check which radio button was clicked
	    switch(view.getId()) {
	            
	        case R.id.autocall_yes:
	        	
	            if (checked)
	            	this.setAutoCall = "1";	            	
	            	
	            break;
	        case R.id.autocall_no:
	        	
	            if (checked)
	            	this.setAutoCall = "0";
	            break;    
	    }
	}

	/** IN USED in the application
	 * 
	 * If pressed open MainActivity
	 * 
	 * */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

	    switch (item.getItemId()) {

	    case android.R.id.home:
			
	    	Intent i = new Intent(SettingsActivity.this, MainActivity.class);
	         startActivity(i);

	    default:
	        return super.onOptionsItemSelected(item);
	    }
	}
	
	/** IN USED in the application
	 * 
	 * If backpress open MainActivity
	 * 
	 * */
	@Override
	public void onBackPressed() {

		Intent i = new Intent(SettingsActivity.this, MainActivity.class);
        startActivity(i);
	}
	
}
