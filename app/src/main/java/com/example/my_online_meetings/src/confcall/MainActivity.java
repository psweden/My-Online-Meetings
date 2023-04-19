package com.example.my_online_meetings.src.confcall;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.os.Bundle;
import com.example.my_online_meetings.src.sqlite.DBAdapter;
import com.example.my_online_meetings.src.sqlite.DBReset;
import com.example.my_online_meetings.src.sqlite.UserData;
import com.example.my_online_meetings.src.sqlite.UserReset;
import com.example.my_online_meetings.src.Calendar.TodayTimeStamp;
import com.example.my_online_meetings.src.Calendar.TomorrowTimeStamp;

/**
 * @ author: Peter Albertsson
 * @ email: peter.albertsson63@gmail.com
 * @ Date: 19 april 2023
 *
 * class-name: MainActivity.java
 * Project:
 * Conference Call, will helps the users to connect to Live-Meeting, without to type dtmf numbers, hands on.
 *
 * Description of the java-class:
 * This application Conference Call will make it easier to make live-meetings call.
 * The user dont need to manual handler to enter pin digits istead can the user
 * only click on the button and do the live meeting calls automatic.
 *
 * The application retrieve new meeting today and tomorrow and import to the application
 * If there is meeting found add new button with the following info
 * - Title of meeting
 * - Type of meeting (Lync Meeting)
 * - Date
 * - Start time
 * - End time
 * - Notification when next meeting will startup
 *
 * Other feature is
 * - Refresh the application >> to get new meetings or delete old meetings
 * - Autocall, only with Handsfree is true
 * - Handsfree, add meetings to the phone contact book
 *
 * Types of meetings:
 * - Passed meetings ==> The time has passed, milliseconds
 * - Empty meetings  ==> Calendar provider doesnt find any meetings
 * - Lync Meetings   ==> Meetings setup by Lync is true meetings
 * - Reuccurent meetings ==> Meetings will came at same time every day
 * - False Meetings ==> There is NO lync/live-meetings the application looks for 'ID:'
 *
 * TODO:
 * - Reuccurent meetings will not be shown in the calendar by the user, who has created the reuccurent meeting.
 * - Reuccurent meetings will be shown for the NOT user who has created the meetings, only when the others users
 *   has accepted the meetings and this users is invited to this type of meetings will be shown the meetings in the calendar.
 *
 */


import com.example.my_online_meetings.R;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.CalendarContract;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

    // Boolean if true >> show 'test-button'
    private boolean testButton = false;

    // Boolean if true >> show dtfm-string on the button
    private boolean dtmfTest = false;

    // To make call and join the meetings
    private String dedicateNumber = "";

    // Set the day on meetings-button 'Today' or 'Tomorrow'
    private String todayTomorrow;

    /* Date and time - attributes for today and tomorrow
     * From java-classes 'TodayTimeStamp and TomorrowTimeStamps'
     * **/
    private String DateToday, DateTomorrow;

    // Init in java-class 'TodayTimeStamp'
    private int cYear, cMonth, cDay;

    // Init in java-class 'TomorrowTimeStamp'
    private int  tYear, tMonth, tDay;

    // Advance settings, user can change, in next version, time-range is >> 00 - 24 hours
    private int sMeetingStartHH = 00, sMeetingStopHH = 24, sMeetingStartMM = 00, sMeetingStopMM = 00;

    // Milliseconds can be used like a statements, to solve out the meetings and set start and end time.
    private long dtStartMilliToday, dtStartMilliTomorrow, milliSecondNow;

    // Buttons today
    private Button b_today1, b_today2, b_today3, b_today4, b_today5, b_today6, b_today7, b_today8, b_today9, b_today10;

    // Button tomorrow
    private Button b_tomorrow1, b_tomorrow2, b_tomorrow3, b_tomorrow4, b_tomorrow5, b_tomorrow6, b_tomorrow7, b_tomorrow8, b_tomorrow9, b_tomorrow10;

    // Default buttons
    private Button b_defaultTodayButton, b_defaultTomorrowButton;

    // Version for the database
    public String getVersion;

    // Prefix to set version number
    private int prefix;

    // count meetings today and tomorrow
    private int countMeetingToday = 0, countMeetingTomorrow = 0, setIDToday = 0, setIDTomorrow = 0, notificationMeetingToday = 0;

    // count if DBAdapter is '0' or over
    private int countDatabaseDBAdapter = 0;

    // handsFree = 1 Mobile = 0
    private String handsFree = "";

    // Sets time right now, is used i the timer
    private String timeNow;

    // App version and Android version
    private String ccVersion = "1.0(1)", androidVersion = "16 - 21";

    public MainActivity() { // Constructor

        // Set the date for Today and init the attributes
        TodayTimeStamp todayTimeStamp = new TodayTimeStamp();
        this.DateToday = todayTimeStamp.getDateToday();
        this.cYear = todayTimeStamp.getCYear();
        this.cMonth = todayTimeStamp.getCMonth();
        this.cDay = todayTimeStamp.getCDay();

        // Set the date for Tomorrow and init the attributes
        TomorrowTimeStamp tomorrowTimeStamp = new TomorrowTimeStamp();
        this.DateTomorrow = tomorrowTimeStamp.getDateTomorrow();
        this.tYear = tomorrowTimeStamp.getTYear();
        this.tMonth = tomorrowTimeStamp.getTMonth();
        this.tDay = tomorrowTimeStamp.getTDay();

    }

    /** INIT the application onCreate() method
     * */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Initite DBReset and DBAdapter
        DBReset dbReset = new DBReset();
        DBAdapter dpa = new DBAdapter();

        // Init and sort databases meetings today and tomorrow
        CompareMillisStartTime cMSTart = new CompareMillisStartTime();

        // Millissecond right now, is for sort out ended meetings
        milliSecondRightNow();

        // Initite database DBReset
        dbReset.init(this);

        /* Set the Database DBReset with following items
         * |Version| Phonenumber | Language | Handsfree | Muted | Autocall |
         *
         * Calling method to set version number of '5' in the database
         * to degrade with '4' to upgrade to '5'
         * **/
        countNextVersion();

        // Get the version of number '5' to upgrade and downgrade with :)
        UserReset resetDBData = DBReset.getUserData(1);
        String dbResetVer = resetDBData.getDBVersion();
        int _verPrefix = Integer.parseInt(dbResetVer);

        if(_verPrefix == 1){
            _verPrefix = 5;
        }

        // Parse int to string and print out to the LOG CAT
        String startText = Integer.toString(_verPrefix);
        Log.d("STARTPREFIX right now is >>> ", startText);

        // Set dedicateNumber phonenumber to Conference center
        this.dedicateNumber = resetDBData.getDBPhonenumber();
        Log.d("[ Dedicated Number to the PBX is >>> ", dedicateNumber + "]");

        /* Check if Handsfree if on '1' or off '0'
         * if '0' = Mobile Mode
         * **/
        this.handsFree = resetDBData.getDBHandsFree();
        Log.d("[ Handsfree on '1' or off '0' >>> ", handsFree + "]");

        // Init the prefix number of '5'
        this.prefix = _verPrefix;
        dpa.DATABASE_VERSION = prefix;

        // Initite database DBAdapter,
        dpa.init(this);

        // Get and show the DB - Version
        dpa.getSQliteDBVersion();

        // Start the Main-Activity
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Show the '<' in the Action-Bar | In main-screen > exit();
        //getActionBar().setDisplayHomeAsUpEnabled(true);

        // Set this statement to dont rotate screen
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // Set the button IDs and Set all button 'GONE'
        setButtonID();

        // Init Today-Calendar and set database DBAdapter
        queryMToday();

        // Init Tomorrow-Calendar and set database DBAdapter
       // queryMTomorrow();

        // Sort Database meetings > insert, update
        int totalM = countMeetingToday + countMeetingTomorrow;
        if(totalM > 0){

            cMSTart.setLongArrayElements(totalM);
        }

        // Initiate buttons for 'Today', with true-tables
        initButtonsToday();
        // Initiate buttons for 'Tomorrow', with true-tables
        initButtonsTomorrow();

        /* Are there any live meetings today or tomorrow
         * if not set alert no meetings.
         * **/
        checkIfThereIsAnyMeetingsDB();

        // Show counted meetings 'Today'
        Log.d(getClass().getName(), "countMeetingToday | counted Meetings Today': >> " + countMeetingToday);
        // Show counted meetings 'Tomorrow'
        Log.d(getClass().getName(), "countMeetingTomorrow | counted Meetings Tomorrow': >> " + countMeetingTomorrow);

        // Set the header textview
        setTextViewHeader();

        // Init PhoneStateListener, to make call and set properties
        PhoneCallListener phoneListener = new PhoneCallListener();
        TelephonyManager telephonyManager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);

        /**
         * Button listener for Button 'Today'
         *
         * */
        b_today1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {

                if(!dedicateNumber.equals("0")){

                    UserData meetingData = DBAdapter.getUserData(1);
                    String dtmf_number = meetingData.getDTMF();
                    String title = meetingData.getTitle();
                    String start = meetingData.getStart();
                    String end = meetingData.getEnd();

                    alertMakeCall(dtmf_number, title, start, end);
                }
                else {

                    alertSetSettings();
                }
            }
        });
        b_today2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {

                if(!dedicateNumber.equals("0")){

                    UserData meetingData = DBAdapter.getUserData(2);
                    String dtmf_number = meetingData.getDTMF();
                    String title = meetingData.getTitle();
                    String start = meetingData.getStart();
                    String end = meetingData.getEnd();

                    alertMakeCall(dtmf_number, title, start, end);
                }
                else {

                    alertSetSettings();
                }
            }
        });
        b_today3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {

                if(!dedicateNumber.equals("0")){

                    UserData meetingData = DBAdapter.getUserData(3);
                    String dtmf_number = meetingData.getDTMF();
                    String title = meetingData.getTitle();
                    String start = meetingData.getStart();
                    String end = meetingData.getEnd();

                    alertMakeCall(dtmf_number, title, start, end);
                }
                else {

                    alertSetSettings();
                }
            }
        });
        b_today4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {

                if(!dedicateNumber.equals("0")){

                    UserData meetingData = DBAdapter.getUserData(4);
                    String dtmf_number = meetingData.getDTMF();
                    String title = meetingData.getTitle();
                    String start = meetingData.getStart();
                    String end = meetingData.getEnd();

                    alertMakeCall(dtmf_number, title, start, end);
                }
                else {

                    alertSetSettings();
                }
            }
        });
        b_today5.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {

                if(!dedicateNumber.equals("0")){


                    UserData meetingData = DBAdapter.getUserData(5);
                    String dtmf_number = meetingData.getDTMF();
                    String title = meetingData.getTitle();
                    String start = meetingData.getStart();
                    String end = meetingData.getEnd();

                    alertMakeCall(dtmf_number, title, start, end);
                }
                else {

                    alertSetSettings();
                }
            }
        });
        b_today6.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {

                if(!dedicateNumber.equals("0")){

                    UserData meetingData = DBAdapter.getUserData(6);
                    String dtmf_number = meetingData.getDTMF();
                    String title = meetingData.getTitle();
                    String start = meetingData.getStart();
                    String end = meetingData.getEnd();

                    alertMakeCall(dtmf_number, title, start, end);

                }
                else {

                    alertSetSettings();
                }
            }
        });
        b_today7.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {

                if(!dedicateNumber.equals("0")){

                    UserData meetingData = DBAdapter.getUserData(7);
                    String dtmf_number = meetingData.getDTMF();
                    String title = meetingData.getTitle();
                    String start = meetingData.getStart();
                    String end = meetingData.getEnd();

                    alertMakeCall(dtmf_number, title, start, end);

                }
                else {

                    alertSetSettings();
                }
            }
        });
        b_today8.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {

                if(!dedicateNumber.equals("0")){

                    UserData meetingData = DBAdapter.getUserData(8);
                    String dtmf_number = meetingData.getDTMF();
                    String title = meetingData.getTitle();
                    String start = meetingData.getStart();
                    String end = meetingData.getEnd();

                    alertMakeCall(dtmf_number, title, start, end);

                }
                else {

                    alertSetSettings();
                }
            }
        });
        b_today9.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {

                if(!dedicateNumber.equals("0")){

                    UserData meetingData = DBAdapter.getUserData(9);
                    String dtmf_number = meetingData.getDTMF();
                    String title = meetingData.getTitle();
                    String start = meetingData.getStart();
                    String end = meetingData.getEnd();

                    alertMakeCall(dtmf_number, title, start, end);

                }
                else {

                    alertSetSettings();
                }
            }
        });
        b_today10.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {

                if(!dedicateNumber.equals("0")){

                    UserData meetingData = DBAdapter.getUserData(10);
                    String dtmf_number = meetingData.getDTMF();
                    String title = meetingData.getTitle();
                    String start = meetingData.getStart();
                    String end = meetingData.getEnd();

                    alertMakeCall(dtmf_number, title, start, end);

                }
                else {

                    alertSetSettings();
                }
            }
        });

        /**
         * Button listener for Button 'Tomorrow'
         *
         * */
        b_tomorrow1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {

                if(!dedicateNumber.equals("0")){

                    int idc = 1 + countMeetingToday;

                    UserData meetingData = DBAdapter.getUserData(idc);
                    String dtmf_number = meetingData.getDTMF();
                    String title = meetingData.getTitle();
                    String start = meetingData.getStart();
                    String end = meetingData.getEnd();

                    alertMakeCall(dtmf_number, title, start, end);

                }
                else {

                    alertSetSettings();
                }
            }
        });

        b_tomorrow2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {

                if(!dedicateNumber.equals("0")){

                    int idc = 2 + countMeetingToday;

                    UserData meetingData = DBAdapter.getUserData(idc);
                    String dtmf_number = meetingData.getDTMF();
                    String title = meetingData.getTitle();
                    String start = meetingData.getStart();
                    String end = meetingData.getEnd();

                    alertMakeCall(dtmf_number, title, start, end);

                }
                else {

                    alertSetSettings();
                }
            }
        });

        b_tomorrow3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {

                if(!dedicateNumber.equals("0")){

                    int idc = 3 + countMeetingToday;

                    UserData meetingData = DBAdapter.getUserData(idc);
                    String dtmf_number = meetingData.getDTMF();
                    String title = meetingData.getTitle();
                    String start = meetingData.getStart();
                    String end = meetingData.getEnd();

                    alertMakeCall(dtmf_number, title, start, end);

                }
                else {

                    alertSetSettings();
                }
            }
        });

        b_tomorrow4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {

                if(!dedicateNumber.equals("0")){

                    int idc = 4 + countMeetingToday;

                    UserData meetingData = DBAdapter.getUserData(idc);
                    String dtmf_number = meetingData.getDTMF();
                    String title = meetingData.getTitle();
                    String start = meetingData.getStart();
                    String end = meetingData.getEnd();

                    alertMakeCall(dtmf_number, title, start, end);

                }
                else {

                    alertSetSettings();
                }
            }
        });

        b_tomorrow5.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {

                if(!dedicateNumber.equals("0")){

                    int idc = 5 + countMeetingToday;

                    UserData meetingData = DBAdapter.getUserData(idc);
                    String dtmf_number = meetingData.getDTMF();
                    String title = meetingData.getTitle();
                    String start = meetingData.getStart();
                    String end = meetingData.getEnd();

                    alertMakeCall(dtmf_number, title, start, end);

                }
                else {

                    alertSetSettings();
                }
            }
        });

        b_tomorrow6.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {

                if(!dedicateNumber.equals("0")){

                    int idc = 6 + countMeetingToday;

                    UserData meetingData = DBAdapter.getUserData(idc);
                    String dtmf_number = meetingData.getDTMF();
                    String title = meetingData.getTitle();
                    String start = meetingData.getStart();
                    String end = meetingData.getEnd();

                    alertMakeCall(dtmf_number, title, start, end);

                }
                else {

                    alertSetSettings();
                }
            }
        });

        b_tomorrow7.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {

                if(!dedicateNumber.equals("0")){

                    int idc = 7 + countMeetingToday;

                    UserData meetingData = DBAdapter.getUserData(idc);
                    String dtmf_number = meetingData.getDTMF();
                    String title = meetingData.getTitle();
                    String start = meetingData.getStart();
                    String end = meetingData.getEnd();

                    alertMakeCall(dtmf_number, title, start, end);

                }
                else {

                    alertSetSettings();
                }
            }
        });

        b_tomorrow8.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {

                if(!dedicateNumber.equals("0")){

                    int idc = 8 + countMeetingToday;

                    UserData meetingData = DBAdapter.getUserData(idc);
                    String dtmf_number = meetingData.getDTMF();
                    String title = meetingData.getTitle();
                    String start = meetingData.getStart();
                    String end = meetingData.getEnd();

                    alertMakeCall(dtmf_number, title, start, end);

                }
                else {

                    alertSetSettings();
                }
            }
        });

        b_tomorrow9.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {

                if(!dedicateNumber.equals("0")){

                    int idc = 9 + countMeetingToday;

                    UserData meetingData = DBAdapter.getUserData(idc);
                    String dtmf_number = meetingData.getDTMF();
                    String title = meetingData.getTitle();
                    String start = meetingData.getStart();
                    String end = meetingData.getEnd();

                    alertMakeCall(dtmf_number, title, start, end);

                }
                else {

                    alertSetSettings();
                }
            }
        });

        b_tomorrow10.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {

                if(!dedicateNumber.equals("0")){

                    int idc = 10 + countMeetingToday;

                    UserData meetingData = DBAdapter.getUserData(idc);
                    String dtmf_number = meetingData.getDTMF();
                    String title = meetingData.getTitle();
                    String start = meetingData.getStart();
                    String end = meetingData.getEnd();

                    alertMakeCall(dtmf_number, title, start, end);

                }
                else {

                    alertSetSettings();
                }
            }
        });
    }

    /** IN USED in the application
     *
     * Get the millisecond right now.
     * To sort out the ended meetings
     * */
    public void milliSecondRightNow(){

        // Millissecond right now
        Calendar.getInstance().getTimeInMillis();
        this.milliSecondNow = System.currentTimeMillis();

        Log.d("[Time format |millisecond| Right Now >>>>> ", milliSecondNow + "]");

        // Format and show the time (HH:MM) right now
        SimpleDateFormat formatTime = new SimpleDateFormat("HH:mm");
        formatTime.setTimeZone(TimeZone.getDefault());
        Log.d("[Time format |HH:MM| Right Now >>>>> ", formatTime.format(milliSecondNow) + "]");

        this.timeNow = formatTime.format(milliSecondNow);

    }

    /** IN USED in the application
     *
     * Starting a Thread will replay every minutes like a timer.
     * */
    public void StartAutoCallThread(){ // | DTEnd | DTStart | AutoCall |

        final ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();
        exec.scheduleAtFixedRate(new Runnable() {

            @Override
            public void run() {

                DTStartAutoCallMeeting();

            }
            // One minutes delay
        }, 0, 1, TimeUnit.MINUTES);
    }

    /** IN USED in the application
     *
     * This method will makes autocalling to the meetings
     * */
    public void DTStartAutoCallMeeting(){

        UserReset getData = DBReset.getUserData(1);
        String autoCall = getData.getDBAutoCall();
        String handsFree = getData.getDBHandsFree();
        String telNum = getData.getDBPhonenumber();

        UserData meetingData = DBAdapter.getUserData(1);
        String dtmf_number = meetingData.getDTMF();
        String dtStart = meetingData.getStart();

        milliSecondRightNow();

        if(dtStart.equals(timeNow) && handsFree.equals("1") && autoCall.equals("1") && !telNum.equals(0)){

            Log.d("[True: DTSTART && TIMENOW, DOING AUTOCALL ! ! ! >>> ", dtStart + " | " + timeNow + " | " + " Handsfree | " + handsFree +  " | Autocall | " + autoCall +  "]");

            doLiveMeetingCall(dtmf_number);
        }

        else {

            // DO NOTHING, only print out
            Log.d("[False: DTSTART && TIMENOW, WAITING FOR AUTOCALL ! ! ! >>> ", dtStart + " | " + timeNow + " | " + " Handsfree | " + handsFree +  " | Autocall | " + autoCall +  "]");
        }
    }

    /** IN USED in the application
     *
     * Will set the notification in the statusbar,
     * if there is any meetings today.
     * Will set text message
     * */
    public void setNotification(){

        // Activate alarmSound if you want sound
        // Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        String meetingsToday = Integer.toString(notificationMeetingToday);

        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, 0);

        int index = 1; //DBAdapter.countDB();
        UserData getData = DBAdapter.getUserData(index);
        String dbStartMeeting = getData.getStart();
        String textM = "Next meeting starts: ";

        // build notification

        if(countMeetingToday == 0){

            meetingsToday = "";
            textM = "";
            dbStartMeeting = "No more live-meetings today!";
        }

        // the addAction re-use the same intent to keep the example short
        Notification notification  = new Notification.Builder(this)
                .setContentTitle("Today " + meetingsToday + " Live-Meetings!")
                .setContentText(textM + dbStartMeeting)
                .setSmallIcon(R.drawable.app_icon)
                //.setOngoing(true) // Forced notification invoked
                //.setSound(alarmSound)
                .setContentIntent(pIntent)
                .setAutoCancel(true)
                .addAction(R.drawable.app_icon, "Call", pIntent)
                .addAction(R.drawable.app_icon, "More", pIntent)
                .addAction(R.drawable.app_icon, "And more", pIntent).build();

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(0, notification);

        // User cant remove the notification
//		notification.flags |= Notification.FLAG_NO_CLEAR;
    }

    /** IN USED in the application
     *
     * Will alert if there is no meetings
     * when the application startup.
     *
     * */
    public void checkIfThereIsAnyMeetingsDB(){

        this.countDatabaseDBAdapter = DBAdapter.countDB();
        if(countDatabaseDBAdapter == 0){

            alertNoAssignedMeetings();
        }

    }

    /** IN USED in the application
     *
     * The method will set the 'Drop-Down' menu.
     * Sets >> itmes on the Action-bar
     *
     * */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Set >> Update icon on actionbar
        MenuInflater inflater1 = getMenuInflater();
        inflater1.inflate(R.menu.main_activity_actions, menu);

        // Set >> your main_menu into the menu
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);

        // Find the menuItem to add your SubMenu
        MenuItem myMenuItem = menu.findItem(R.id.my_menu_item);

        MenuItem item = menu.findItem(R.id.handsFree_modeOnOff);

        UserReset urData = DBReset.getUserData(1);
        String UrHandsFree = urData.getDBHandsFree();

        if(menu != null)
        {
            if(UrHandsFree.equals("1"))
            {
                item.setIcon(R.drawable.ic_action_headset);
                item.setTitle("Handsfree!");
            }
            if(UrHandsFree.equals("0"))
            {
                item.setIcon(R.drawable.ic_action_phone);
                item.setTitle("Mobile mode!");
            }
        }
        getMenuInflater().inflate(R.menu.sub_menu, myMenuItem.getSubMenu());

        return super.onCreateOptionsMenu(menu);
    }

    /** NOT USED in the application
     *
     * But let it stay this is to the system
     * if needed in the future.
     * */
    @Override
    protected void onRestart() {
        // Do someting
        super.onRestart();

    }

    /** IN USED in the application
     *
     * It will done the refresh
     * - Refresh button menu
     * - Handsfree button menu
     * - Mobile button menu
     *
     * */
    private void restartFirstActivity()
    {

        if (Build.VERSION.SDK_INT >= 11) {
            recreate();
        } else {
            Intent intent = getIntent();
            finish();
            startActivity(intent);
        }
    }

    /** IN USED in the application
     *
     * Will set all menu-button events
     * | Refresh | Drop-Down | About | Settings | Mobile | Exit |
     *
     * */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            // Refresh menu
            case R.id.refresh_menu:
                restartFirstActivity();
                Toast.makeText(getBaseContext(), "Refresh Meetings!",
                        Toast.LENGTH_SHORT).show();
                break;
            // Drop-Down menu
            case R.id.my_menu_item:
                break;
            // About menu
            case R.id.submenu_one:
                dialogAlert();
                break;
            // Settings menu
            case R.id.submenu_two:
                Intent i = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(i);
                break;
            // Handsfree menu
            case R.id.submenu_three:

                UserReset getData = DBReset.getUserData(1);
                String handsFree = getData.getDBHandsFree();
                String autoCall = getData.getDBAutoCall();

                if(handsFree.equals("0") && autoCall.equals("1")){
                    // Add contacts
                    setMeetingsToPhoneContactlist();
                    // If Handsfree = 1 true if 0 false
                    DBReset.updateInsertHandsFree(1, "1");
                    // Do autocall
                    StartAutoCallThread();
                }

                if(handsFree.equals("0") && autoCall.equals("0")){
                    // Add contacts
                    setMeetingsToPhoneContactlist();
                    // If Handsfree = 1 true if 0 false
                    DBReset.updateInsertHandsFree(1, "1");
                }

                restartFirstActivity();
                Toast.makeText(getBaseContext(), "Handsfree Activated!",Toast.LENGTH_SHORT).show();

                break;
            // Mobile menu
            case R.id.submenu_four:

                // If Handsfree = 0 false = Mobile
                DBReset.updateInsertHandsFree(1, "0");
                deleteContacts();
                restartFirstActivity();
                Toast.makeText(getBaseContext(), "Mobile Activated!", Toast.LENGTH_SHORT).show();
                break;
            // Alert Exit menu
            case R.id.submenu_five:
                alertExit();
            default:

                return super.onOptionsItemSelected(item);
        }
        return false;
    }

    /** IN USED in the application
     *
     * If backpress Alert Exit yes and no
     *
     * */
    @Override
    public void onBackPressed() {

        alertExit();
    }

    public void alertMakeCall(String dtmf, String title, String start, String end){

        final String callDtmf = dtmf;
        String mTitle = title;
        String mStart = start;
        String mEnd = end;

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do you want to join this meeting?\n" + mTitle + "\nStart: " + mStart + " | End: " + mEnd).setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        doLiveMeetingCall(callDtmf);
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
     * No meetings are assigned!
     * Will ask do you want exit yes or no
     *
     * */
    public void alertNoAssignedMeetings(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("No Live-Meetings assigned. Exit?").setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        MainActivity.this.finish();
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
     * Every keyevents will check
     * if there is no settings saved
     * Alert Settings!
     *
     * */
    public void alertSetSettings(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("You have to set Country in settings!").setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        Intent i = new Intent(MainActivity.this, SettingsActivity.class);
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
     * Alerts exit
     * If backbutton is pressed or Exit in menu
     *
     * */
    public void alertExit(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to exit?").setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        DBReset.updateInsertHandsFree(1, "0");
                        deleteContacts();
                        finishAffinity();
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
     * This method is important and used in the application.
     * - This method take care of exit 'events' of the APP
     * - To downgrade to version '4' and upgrade to version '5'
     * */
    @Override
    protected void onDestroy() {

        DBAdapter dpa = new DBAdapter();
        super.onDestroy();
        /*
         * This 'calls' 'update()' method in DBAdapter
         * to set the version to number of '4' before
         * do exit, so this will degrade to and will be
         * upgrade to version of '5' when the App will start next time.
         * This point will be ==> DB DBAdapter will be and do 'reset' every time it will running the App :)
         * **/
        dpa.updateDBVersion();
    }

    /** IN USED in the application
     *
     * The method will set the logic how the buttons
     * will be styled and showed in relatoins for Today and Tommorow
     *
     * */
    public void initButtonsToday(){

        /**
         * True-tables, for the if-statements.
         * (countMeetingToday) is num of meetings 'Today'
         * (countMeetingTomorrow) is num of meetings 'Tomorrow'
         *
         * Num '0' = none meeting
         * Num '1' = Meeting
         *
         * 		Today			|    Tomorrow		| OK || NOK
         * -------------------------------------------------------
         * 			1			|       1			| 	OK
         * 			1			|       0		    |	OK
         * 			0			|       1		    |	OK
         * 			0			|       0			|	OK
         *
         *
         * */

        if(countMeetingToday >= 1){

            Log.d("(initButtons()) | if-statement 1 |","Set > initSetButtonToday()");
            // Init and set all buttons Today
            initSetButtonToday();
        } else{

            if(this.testButton != false){

                String styledTextButton =

                        "2015-04-05" + " - " + "<font color='#82CAFF'>" + "Today"
                                + "</font>" + "<br />" + "<big> " + "Stockholm"
                                + "</font> </big>" + "<br />"
                                + "<font color='#82CAFF'>" + "Start: " + "</font>"
                                + "10:30" + " | " + "<font color='#82CAFF'>" + " End: "
                                + "</font>" + "11:00";

                setButtonVisible(this.b_defaultTodayButton);
                b_defaultTodayButton.setText(Html.fromHtml(styledTextButton));

            }


            Log.d("(initButtons()) Today | if-statement 0 |","Set > NONE MEETINGS :) ");

        }
    }

    /** IN USED in the application
     *
     * The method will set the logic how the buttons
     * will be styled and showed in relatoins for Today and Tommorow
     *
     * */
    public void initButtonsTomorrow(){

        /**
         * True-tables, for the if-statements.
         * (countMeetingToday) is num of meetings 'Today'
         * (countMeetingTomorrow) is num of meetings 'Tomorrow'
         *
         * Num '0' = none meeting
         * Num '1' = Meeting
         *
         * 		Today			|    Tomorrow		| OK || NOK
         * -------------------------------------------------------
         * 			1			|       1			| 	OK
         * 			1			|       0		    |	OK
         * 			0			|       1		    |	OK
         * 			0			|       0			|	OK
         *
         *
         * */

        if(countMeetingTomorrow >= 1){

            Log.d("(initButtons()) | if-statement 1 |","Set > initSetButtonTomorrow()");

            // Init and set all buttons Tomorrow
            initSetButtonTomorrow();

        } else {

            if(this.testButton != false){

                String styledTextButton =

                        "2015-04-06" + " - " + "<font color='#82CAFF'>" + "Tomorrow"
                                + "</font>" + "<br />" + "<big> " + "Stockholm"
                                + "</font> </big>" + "<br />"
                                + "<font color='#82CAFF'>" + "Start: " + "</font>"
                                + "10:30" + " | " + "<font color='#82CAFF'>" + " End: "
                                + "</font>" + "11:00";

                setButtonVisible(this.b_defaultTomorrowButton);
                b_defaultTomorrowButton.setText(Html.fromHtml(styledTextButton));

            }

            Log.d("(initButtons()) Tomorrow | if-statement 0 |","Set > NONE MEETINGS :) ");

        }
    }

    /** IN USED in the application
     *
     * The method will set the number of '5' to be used with upgrade or downgrade DB-Version
     * Will let it be like this, SUM will be invoked but maybe changed in the future to be used.
     *
     * */
    public void countNextVersion(){

        int index = 1;

        int count = DBReset.countDB();

        String strVersion2 = Integer.toString(count);
        Log.d("Database | how many rows? countDB >>> ", strVersion2);

        // From first time the application startup add version num '5'
        if(count == 0){
            // Sets the database value from start 'first time the user startup the application'
            // |Version '5'| Phonenumber '0' | Language '0. | Handsfree '0' | Muted '0'| Autocall '0'|
            String version = "5";
            String phonenumber = "0";
            String language = "0";
            String handsFree = "0";
            String muted = "0";
            String autocall = "0";

            DBReset.addUserData(new UserReset(version, phonenumber, language, handsFree, muted, autocall));
            UserReset resetDBData = DBReset.getUserData(1);
            // Show version
            String dbVersion = resetDBData.getDBVersion();
            Log.d("[ Get from database 'dbVersion' >>> ", dbVersion + "]");
            // Show phonenumber
            String dbPhonenumber = resetDBData.getDBPhonenumber();
            Log.d("[ Get from database 'dbPhonenumber' >>> ", dbPhonenumber + "]");
            // Show dbLanguage
            String dbLanguage = resetDBData.getDBLanguage();
            Log.d("[ Get from database 'dbLanguage' >>> ", dbLanguage + "]");
            // Show dbHandsFree
            String dbHandsFree= resetDBData.getDBHandsFree();
            Log.d("[ Get from database 'dbHandsFree' >>> ", dbHandsFree + "]");
            // Show dbMuted
            String dbMuted= resetDBData.getDBMuted();
            Log.d("[ Get from database 'dbMuted' >>> ", dbMuted + "]");
            // Show dbAutoCall
            String dbAutoCall= resetDBData.getDBAutoCall();
            Log.d("[ Get from database 'dbAutoCall' >>> ", dbAutoCall + "]");

        }
        if(count >= 1){
            UserReset resetDBD = DBReset.getUserData(1);

            // Get the last 'version' from the database DBReset
            String strVersion = resetDBD.getDBVersion();

            // Show version
            String dbVersion = resetDBD.getDBVersion();
            Log.d("[ Get from database 'dbVersion' >>> ", dbVersion + "]");
            // Show phonenumber
            String dbPhonenumber = resetDBD.getDBPhonenumber();
            Log.d("[ Get from database 'dbPhonenumber' >>> ", dbPhonenumber + "]");
            // Show dbLanguage
            String dbLanguage = resetDBD.getDBLanguage();
            Log.d("[ Get from database 'dbLanguage' >>> ", dbLanguage + "]");
            // Show dbHandsFree
            String dbHandsFree= resetDBD.getDBHandsFree();
            Log.d("[ Get from database 'dbHandsFree' >>> ", dbHandsFree + "]");
            // Show dbMuted
            String dbMuted= resetDBD.getDBMuted();
            Log.d("[ Get from database 'dbMuted' >>> ", dbMuted + "]");
            // Show dbAutoCall
            String dbAutoCall= resetDBD.getDBAutoCall();
            Log.d("[ Get from database 'dbAutoCall' >>> ", dbAutoCall + "]");


            // Parsa 'string' to 'int'
            int _dbVersion = Integer.parseInt(strVersion);

            /* Will be invoked maybe will be used in the future
             *
             * Sum so the version will override and get ++1 new versoin for every time App will startup
             * **/
            //_dbVersion = _dbVersion + init;

            // Int to String and show the result
            String strVer= Integer.toString(_dbVersion);
            Log.d("DBADAPTER SUM  (versionen) who is: ", strVer);

            int summa = _dbVersion;

            // Do (insert | update) of the version to the database DBReset
            String strV = Integer.toString(summa);
            Log.d("(Sum) save to DBReset and is: ", strV); //
            DBReset.updateInsert(index, strV);

            // Check the value after insert is done
            String s = resetDBD.getDBVersion();
            Log.d("DBReset (version | Sum) is after >> (insert): ", s);
        }
    }

    /** IN USED in the application
     *
     * The method will set the attributes into the database DBAdapter
     * Will set the ID + meetings-attribute into the database.
     *
     * */
    public void setDataBaseToday(int id, String location, String title, String date, String start, String end, String dtmf, String millisecond) {

        Log.d(getClass().getName(), "ID value in 'setDataBaseToday': >> " + id);

        // Inserting Lync - Meetings
        Log.d("Insert to database: ", "Inserting ..");

        Log.d("AddUserData: ", "Inserting ..");
        DBAdapter.addUserData(new UserData(location, title, date, start, end, dtmf, millisecond));

        // Reading all contacts and show them in LOGCAT viewer.
        Log.d("Reading: ", "Reading all contacts..");
        List<UserData> data = DBAdapter.getAllUserData();

        for (UserData dt : data) { String log = "Id: "
                + dt.getID() + " ,Location: " + dt.getLocation()
                + " ,Title: " + dt.getTitle() + " ,Date: " + dt.getTodayDate()
                + " ,Start: " + dt.getStart() + " ,End: " + dt.getEnd()
                + " ,DTMF: " + dt.getDTMF();

            // Writing Contacts to log
            Log.d("Name: ", log);

        }
    }

    /** IN USED in the application
     *
     * The method will set the attributes into the database DBAdapter
     * Will set the ID + meetings-attribute into the database.
     *
     * */
    public void setDataBaseTomorrow(int id, String location, String title, String date, String start, String end, String dtmf, String millisecond) {

        Log.d(getClass().getName(), "ID value in 'setDataBaseTomorrow': >> " + id);

        // Inserting Lync - Meetings
        Log.d("Insert to database: ", "Inserting ..");

//					String dTitle = title.substring(0, 19) + "...";
//					String dLocation = location.substring(0, 14);

        Log.d("AddUserData: ", "Inserting ..");
        DBAdapter.addUserData(new UserData(location, title, date, start, end, dtmf, millisecond));

        // Reading all contacts and show them in LOGCAT viewer.
        Log.d("Reading: ", "Reading all contacts..");
        List<UserData> data = DBAdapter.getAllUserData();

        for (UserData dt : data) { String log = "Id: "
                + dt.getID() + " ,Location: " + dt.getLocation()
                + " ,Title: " + dt.getTitle() + " ,Date: " + dt.getTodayDate()
                + " ,Start: " + dt.getStart() + " ,End: " + dt.getEnd()
                + " ,DTMF: " + dt.getDTMF();

            // Writing Contacts to log
            Log.d("Name: ", log);

        }
    }

    /** IN USED in the application
     *
     * The method will set the id to be used in addbutton with html-style
     *
     * */
    public void initSetButtonToday(){


        for (int i = 1; i <= countMeetingToday; i++) {

            int ids = i;

            if(ids == 1){

                Button b = b_today1;
                ids = 1;

                getMeetingsFromDB(ids, b);
            }

            if(ids == 2){

                Button b = b_today2;
                ids = 2;

                getMeetingsFromDB(ids, b);
            }

            if(ids == 3){

                Button b = b_today3;
                ids = 3;

                getMeetingsFromDB(ids, b);
            }

            if(ids == 4){

                Button b = b_today4;
                ids = 4;

                getMeetingsFromDB(ids, b);
            }

            if(ids == 5){

                Button b = b_today5;
                ids = 5;

                getMeetingsFromDB(ids, b);
            }

            if(ids == 6){

                Button b = b_today6;
                ids = 6;

                getMeetingsFromDB(ids, b);
            }

            if(ids == 7){

                Button b = b_today7;
                ids = 7;

                getMeetingsFromDB(ids, b);
            }

            if(ids == 8){

                Button b = b_today8;
                ids = 8;

                getMeetingsFromDB(ids, b);
            }

            if(ids == 9){

                Button b = b_today9;
                ids = 9;

                getMeetingsFromDB(ids, b);
            }

            if(ids == 10){

                Button b = b_today10;
                ids = 10;

                getMeetingsFromDB(ids, b);
            }
        }
    }

    /** IN USED in the application
     *
     * The method will set the id to be used in addbutton with html-style
     *
     * */
    public void initSetButtonTomorrow(){

        // Loop the 'number' of meetings tomorrow.
        for (int i = 1; i <= countMeetingTomorrow; i++) {

            /*
             * Start-position + number '1' (id),
             * so correct start-position retrieve db-post
             * in relation to the database
             * **/
            int ids = i + countMeetingToday;

            if(ids == 1){

                Button b = b_tomorrow1;
                ids = 1;

                getMeetingsFromDB(ids, b);
            }

            if(ids == 2){

                Button b = b_tomorrow2;
                ids = 2;

                getMeetingsFromDB(ids, b);
            }

            if(ids == 3){

                Button b = b_tomorrow3;
                ids = 3;

                getMeetingsFromDB(ids, b);
            }

            if(ids == 4){

                Button b = b_tomorrow4;
                ids = 4;

                getMeetingsFromDB(ids, b);
            }

            if(ids == 5){

                Button b = b_tomorrow5;
                ids = 5;

                getMeetingsFromDB(ids, b);
            }

            if(ids == 6){

                Button b = b_tomorrow6;
                ids = 6;

                getMeetingsFromDB(ids, b);
            }

            if(ids == 7){

                Button b = b_tomorrow7;
                ids = 7;

                getMeetingsFromDB(ids, b);
            }

            if(ids == 8){

                Button b = b_tomorrow8;
                ids = 8;

                getMeetingsFromDB(ids, b);
            }

            if(ids == 9){

                Button b = b_tomorrow9;
                ids = 9;

                getMeetingsFromDB(ids, b);
            }

            if(ids == 10){

                Button b = b_tomorrow10;
                ids = 10;

                getMeetingsFromDB(ids, b);
            }

            /*
             * Level 2 | position '11' to '20'
             * (if 'Today' meetings reach above 10 meetings.
             * **/

            if(ids == 11){

                Button b = b_tomorrow1;
                ids = 11;

                getMeetingsFromDB(ids, b);
            }

            if(ids == 12){

                Button b = b_tomorrow2;
                ids = 12;

                getMeetingsFromDB(ids, b);
            }

            if(ids == 13){

                Button b = b_tomorrow3;
                ids = 13;

                getMeetingsFromDB(ids, b);
            }

            if(ids == 14){

                Button b = b_tomorrow4;
                ids = 14;

                getMeetingsFromDB(ids, b);
            }

            if(ids == 15){

                Button b = b_tomorrow5;
                ids = 15;

                getMeetingsFromDB(ids, b);
            }

            if(ids == 16){

                Button b = b_tomorrow6;
                ids = 16;

                getMeetingsFromDB(ids, b);
            }

            if(ids == 17){

                Button b = b_tomorrow7;
                ids = 17;

                getMeetingsFromDB(ids, b);
            }

            if(ids == 18){

                Button b = b_tomorrow8;
                ids = 18;

                getMeetingsFromDB(ids, b);
            }

            if(ids == 19){

                Button b = b_tomorrow9;
                ids = 19;

                getMeetingsFromDB(ids, b);
            }

            if(ids == 20){

                Button b = b_tomorrow10;
                ids = 20;

                getMeetingsFromDB(ids, b);
            }

        }

    }

    /** IN USED in the application
     *
     * The method will getting the meetings-attribute to set to the buttons.
     * Will get the ID + meetings-attribute then button id to add the html-styling on the buttons
     *
     * */
    public void getMeetingsFromDB(int id, Button button){ // ID and Buttons ID will be passed here

        int ids = id;

        Log.d(getClass().getName(), "ID value in 'getMeetingsFromDB': >> " + ids);
        //countMeetingTotal = 2;

        UserData meetingData = DBAdapter.getUserData(ids);

        String dbLocation = meetingData.getLocation();
        String dbTitle = meetingData.getTitle();
        String dbDate = meetingData.getTodayDate();
        String dbStart = meetingData.getStart();
        String dbEnd = meetingData.getEnd();
        String dbDtmf = meetingData.getDTMF();

        Log.d("Reading data: ", "of a meeting row");
        Log.d("DBLocation :" , dbLocation);
        Log.d("DBTitle :" , dbTitle);
        Log.d("DBDate :" , dbDate);
        Log.d("DBStart :" , dbStart);
        Log.d("DBEnd :" , dbEnd);
        Log.d("DBDTMF :" , dbDtmf);

        setButtonVisible(button);
        addNewMeetingsOnButton(button, dbStart, dbEnd, dbTitle, dbLocation, dbDate, dbDtmf);

    }

    /** IN USED in the application
     *
     * Will set the html-styling on the header textview
     *
     * */
    public void setTextViewHeader() {


        TextView textView_date = (TextView) findViewById(R.id.tv_date);
        String styleTextViewvDate = "Today's date: " + DateToday;
        textView_date.setText(Html.fromHtml(styleTextViewvDate));

    }

    /** IN USED in the application
     *
     * Styling the button-attribute with html-styling
     *
     * */
    public void addNewMeetingsOnButton(Button button, String start, String end, String title, String location, String date, String dtmf) {

        setStringTTO(date);

        if (todayTomorrow == null || title == null
                || date == null || start == null
                || end == null) {

            this.todayTomorrow = "none";
            title = "none";
            date = "none";
            start = "none";
            end = "none";

        } else {


            if(this.dtmfTest != false){

                String styledTextButton =

                        date + " - " + "<font color='#82CAFF'>" + todayTomorrow
                                + "</font>" + "<br />" + "<big> " + title
                                + "</font> </big>" + "<br />"
                                + "<font color='#82CAFF'>" + "Start: " + "</font>"
                                + start + " | " + "<font color='#82CAFF'>" + " End: "
                                + "</font>" + end + "<br />"
                                + "DTMF/Meeting ID: " + dtmf;

                button.setText(Html.fromHtml(styledTextButton));

            }
            if(this.dtmfTest != true){

                String styledTextButton =

                        date + " - " + "<font color='#82CAFF'>" + todayTomorrow
                                + "</font>" + "<br />" + "<big> " + title
                                + "</font> </big>" + "<br />"
                                + "<font color='#82CAFF'>" + "Start: " + "</font>"
                                + start + " | " + "<font color='#82CAFF'>" + " End: "
                                + "</font>" + end;

                button.setText(Html.fromHtml(styledTextButton));

            }

            // old styled button
//			String styledTextButton =
//
//					location + " - " + "<font color='#82CAFF'>" + todayTomorrow
//					+ "</font>" + "<br />" + "<big> " + title
//					+ "</font> </big>" + "<br />" + date + " | "
//					+ "<font color='#82CAFF'>" + "Start: " + "</font>"
//					+ start + "<font color='#82CAFF'>" + " End: "
//					+ "</font>" + end;
//
//			button.setText(Html.fromHtml(styledTextButton));

        }
    }

    /** IN USED in the application
     *
     * Give and set the button ID
     *
     * */
    public void setButtonID() {

        this.b_today1 = (Button) findViewById(R.id.b_today1);
        this.b_today2 = (Button) findViewById(R.id.b_today2);
        this.b_today3 = (Button) findViewById(R.id.b_today3);
        this.b_today4 = (Button) findViewById(R.id.b_today4);
        this.b_today5 = (Button) findViewById(R.id.b_today5);
        this.b_today6 = (Button) findViewById(R.id.b_today6);
        this.b_today7 = (Button) findViewById(R.id.b_today7);
        this.b_today8 = (Button) findViewById(R.id.b_today8);
        this.b_today9 = (Button) findViewById(R.id.b_today9);
        this.b_today10 = (Button) findViewById(R.id.b_today10);

        this.b_tomorrow1 = (Button) findViewById(R.id.b_tomorrow1);
        this.b_tomorrow2 = (Button) findViewById(R.id.b_tomorrow2);
        this.b_tomorrow3 = (Button) findViewById(R.id.b_tomorrow3);
        this.b_tomorrow4 = (Button) findViewById(R.id.b_tomorrow4);
        this.b_tomorrow5 = (Button) findViewById(R.id.b_tomorrow5);
        this.b_tomorrow6 = (Button) findViewById(R.id.b_tomorrow6);
        this.b_tomorrow7 = (Button) findViewById(R.id.b_tomorrow7);
        this.b_tomorrow8 = (Button) findViewById(R.id.b_tomorrow8);
        this.b_tomorrow9 = (Button) findViewById(R.id.b_tomorrow9);
        this.b_tomorrow10 = (Button) findViewById(R.id.b_tomorrow10);



        this.b_defaultTodayButton = (Button) findViewById(R.id.b_defaultTodayButton);
        this.b_defaultTomorrowButton = (Button) findViewById(R.id.b_defaultTomorrowButton);

        /*
         * Makes remove all button until we get one by one in
         * relation to how many meetings Today and Tomorrow
         * **/
        setButtonGone(b_today1);
        setButtonGone(b_today2);
        setButtonGone(b_today3);
        setButtonGone(b_today4);
        setButtonGone(b_today5);
        setButtonGone(b_today6);
        setButtonGone(b_today7);
        setButtonGone(b_today8);
        setButtonGone(b_today9);
        setButtonGone(b_today10);

        setButtonGone(b_tomorrow1);
        setButtonGone(b_tomorrow2);
        setButtonGone(b_tomorrow3);
        setButtonGone(b_tomorrow4);
        setButtonGone(b_tomorrow5);
        setButtonGone(b_tomorrow6);
        setButtonGone(b_tomorrow7);
        setButtonGone(b_tomorrow8);
        setButtonGone(b_tomorrow9);
        setButtonGone(b_tomorrow10);

        setButtonGone(b_defaultTodayButton);
        setButtonGone(b_defaultTomorrowButton);

    }

    /** IN USED in the application
     *
     * The method will set the button to 'gone' removes the buttons
     *
     * */
    public void setButtonGone(Button setGone) {

        setGone.setVisibility(View.GONE);

    }

    /** IN USED in the application
     *
     * The method will set the button to visibly
     *
     * */
    public void setButtonVisible(Button setVisible) {

        setVisible.setVisibility(View.VISIBLE);

    }

    /** IN USED in the application
     *
     * The method set the date to Today or Tommorrow
     *
     * */
    public void setStringTTO(String date) {

        if (DateToday.equals(date)) {

            this.todayTomorrow = "Today";

        } else {
            this.todayTomorrow = "Tomorrow";
        }
    }

    /** IN USED in the application
     *
     * Description:
     * All the 'Tomorrow' calendar events will be improved from the device calendar.
     * If 1 there is an 'live-meetings' catch and add to database.
     * If 0 there is a none live-meetings >> ignore
     *
     * */
    public void queryMToday() {

        // Index all the instance for the calendars functionally
        boolean allDay = false;
        long dtEndMilli = 0;
        int liveMeeting = 0;
        int countMeetings = 0;
        int trueMCountTemp = 0;
        int fasleMCountTemp = 0;
        int position = 0;
        String dtLocation = "";
        String dtTitle = "";
        String dtDate = "";
        String dtStart = "";
        String dtEnd = "";
        String dtDescription = "";
        String dtAllDay = "";
        String dtRDate = "";
        String dtRRule = "";

        /*
         * Get all the instance of the calendar who will be needed to get for the application.
         * Make the point there all instance have the post '0' to '9'
         * **/
        String[] projection = new String[] {
                CalendarContract.Events.CALENDAR_ID, 	// '0'
                CalendarContract.Events.TITLE, 			// '1'
                CalendarContract.Events.DESCRIPTION, 	// '2'
                CalendarContract.Events.DTSTART, 		// '3'
                CalendarContract.Events.DTEND, 			// '4'
                CalendarContract.Events.EVENT_LOCATION, // '5' Live or Lync - meeting
                CalendarContract.Events.ALL_DAY, // '6' Live or Lync - meeting
                CalendarContract.EventsEntity.RDATE, // 7' Live or Lync - meeting
                CalendarContract.EventsEntity.RRULE, // 8' Live or Lync - meeting
        };

        SimpleDateFormat formatTime = new SimpleDateFormat("HH:mm");
        formatTime.setTimeZone(TimeZone.getDefault());

        /*
         * RANGE for the CALENDAR important, 0 = January, 1 = February, ...
         * */
        Calendar startTime = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        startTime.set(cYear, cMonth, cDay, sMeetingStartHH, sMeetingStartMM);

        Calendar endTime = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        endTime.set(cYear, cMonth, cDay, sMeetingStopHH, sMeetingStopMM);

        // Logic settings for this method to work, doesn't really what its for?
        String selection =
                "(( " + CalendarContract.Events.DTSTART + " >= " + startTime.getTimeInMillis() + " ) "
                        + "AND " +
                        "( " + CalendarContract.Events.DTSTART + " <= " + endTime.getTimeInMillis() + " ))";

        // Settings for the cursor, for the calendar package
        Cursor cursor = this.getBaseContext().getContentResolver().query(CalendarContract.Events.CONTENT_URI, projection, selection, null , CalendarContract.Events.DTSTART);

        if (cursor.getCount() > 0) {

            while (cursor.moveToNext()) {

                countMeetings =cursor.getCount(); // How many meetings is there 'today'
                cursor.getString(0); // get the calendar ID not in used but make point most be there for the post '0'
                dtTitle = cursor.getString(1); // get the title (subject) of the meetings
                dtDescription = cursor.getString(2); // get the txt message in the meetings
                dtDate = (new Date(cursor.getLong(3))).toString(); // DTDate in date format 2015-02-25
                dtStart = cursor.getString(3); // DTStart in milliseconds
                dtEnd = cursor.getString(4); // DTEnd in milliseconds
                dtLocation = cursor.getString(5); // get the location where the meetings will be setup in this case >> Lync-meeting
                dtAllDay = cursor.getString(6); // get if AllDay meeting is num '1' true is num '0' false
                dtRDate = cursor.getString(7);
                dtRRule = cursor.getString(8);

                this.dtStartMilliToday = Long.parseLong(dtStart);
                dtStart = formatTime.format(dtStartMilliToday);

                // 'All-Day' meetings, will be auto timespamp from 02 to 02 the day after.
                if(dtAllDay.equals("1")){

                    /*
                     * Should be shown:
                     *
                     * - Tomorrow until 'todays' timestamp 00
                     * - Today from 00 to 00 same day
                     * - Tomorrow from 00 to 02 the day after
                     *
                     * - The point is so the user can enter the meetings in 24 hours :)
                     * **/

                    // Statement will set special features for AllDay Meetings.
                    allDay = true;



                }

                // If the value not is NULL there is a 'true meeting' add to database
                if( dtTitle != null && dtDescription != null && dtDate != null && dtStart != null && dtEnd != null && dtLocation != null){

                    // Search in 'descriptoin' for index 'ID:'
                    String idIndex = "ID:";
                    position = dtDescription.indexOf(idIndex);
                    dtEndMilli = Long.parseLong(dtEnd);
                    dtEnd = formatTime.format(dtEndMilli);
                }

                // If there is any value with NULL throw the 'meetings is false'
                if( dtTitle == null || dtDescription == null || dtDate == null || dtStart == null || dtEnd == null || dtLocation == null){

                    liveMeeting = 0;
                    position = -1;

                }

                // True there is live meetings
                if(position != -1){

                    liveMeeting = 1;

                }

                // False there is NO live meetings
                if(position == -1){

                    liveMeeting = 0;
                }

                if(liveMeeting == 1){

                    // If end-time has 'PASSED' do not set the live-meeting
                    if(milliSecondNow <= dtEndMilli){

                        /*
                         * IMPORTANT: this attribute 'millisecond' is for to
                         * compare and sort the meetings DO NOT CHANGE THIS!!!
                         *
                         * **/
                        String millisecond = Long.toString(dtStartMilliToday);

                        // This ID, will set database id's and count the 'true' meetings
                        int id = 1;

                        // Append true meeting will be added with num '1' every loops
                        trueMCountTemp = trueMCountTemp + id;

                        setMeetingsToday(id, dtLocation, dtTitle, dtDate, dtStart, dtEnd, dtDescription, millisecond);

                        // Count how many live-meetings today
                        this.notificationMeetingToday = notificationMeetingToday + id;
                        /* Set notification 'if there is meeting Today'
                         * And a small icon in the statubar
                         * if not dont show notification
                         * **/
                        setNotification();

                    } else {

                        // DO NOTHING ! ! !
                        Log.d("[[[Time format |HH:MM| has ended 'passed' >>>>> ",formatTime.format(dtEndMilli));
                        Log.d("[[[Time format |HH:MM| Right Now >>>>> ", formatTime.format(milliSecondNow));

                    }

                } else {

                    // DO NOTHING ! ! !

                }
            }
        }

        // Print out and show all the countec meetings true, false and total
        fasleMCountTemp = countMeetings - trueMCountTemp;
        Log.d(getClass().getName(), "[[[ Total meetings in 'Calendar' today: >> " + countMeetings);
        Log.d(getClass().getName(), "[[[ False NONE meetings in 'Calendar' today: >> " + fasleMCountTemp);
        Log.d(getClass().getName(), "[[[ TRUE meetings in 'Calendar' today: >> " + trueMCountTemp);
        this.countMeetingToday = trueMCountTemp;

    }

    /** IN USED in the application
     *
     * Description:
     * All the 'TODAY' calendar events will be sorted indexed and saved in the database.
     *
     * */
    public void setMeetingsToday(int id, String location, String title, String date, String start, String end, String description, String millisecond){
        SortCharToDigits sortChar = new SortCharToDigits();


        this.setIDToday	= setIDToday + id;
        int ids = setIDToday;
        this.countMeetingToday = ids;

        int textID = 0;
        String index = "";
        String setdtmf = "";
        String setLocation = location;
        String setTitle = title;
        String setDate = date;
        String setStart = start;
        String setEnd = end;
        String setDescription = description;
        String setMillisecond = millisecond;

        // ID: index of first position
        index = "ID:";

        // Get index/position of 'ID:' in the message, to retreive the dtmf-numbers
        textID = setDescription.indexOf(index);

        // Get SUB-STRING, position where 'ID:' is started
        setDescription = setDescription.substring(textID);

        // Move 5 step 'forward' to reach the first emtpy ' ' character, find position
        int position = setDescription.indexOf(' ', 5);

        // if 'position != -1' get and SET the SUB-STRING '0 - position'
        if(position != -1){
            setDescription = setDescription.substring(0, position);
        }

        // if 'position == -1' get and SET the SUB-STRING '0'
        if(position == -1){
            setDescription = setDescription.substring(0);
        }

        // Sort the SUB-STRING and copy into the DTMF-STRING
        setdtmf = sortChar.sortCharToDigits(setDescription);

        // Create substring of 'Title to max 20 char and Location to 15 char'
        if(setTitle.length() > 19){

            int indexS;
            setTitle = setTitle.substring(0, 19);
            indexS = setTitle.lastIndexOf(' ');
            setTitle = setTitle.substring(0, indexS)  + "...";
        }

        if(setLocation.length() > 14){

            int indexss;
            setLocation = setLocation.substring(0, 14);
            indexss = setLocation.lastIndexOf(' ');
            setLocation = setLocation.substring(0, indexss);

        }

        Log.d("[ TITLE > ", setTitle + " | LOCATOIN > " + setLocation);

        /*
         * The ID + meeting-strings lenghts till be passed to 'setDataBase',
         * The string will be saved into the database DBAdapter.
         * **/
        Log.d(getClass().getName(), "IDS (QueryMToday()): >> " + ids);
        setDataBaseToday(ids, setLocation, setTitle, setDate, setStart, setEnd, setdtmf, setMillisecond);



    }

    /** IN USED in the application
     *
     * Description:
     * All the 'Tomorrow' calendar events will be improved from the device calendar.
     * If 1 there is an 'live-meetings' catch and add to database.
     * If 0 there is a none live-meetings >> ignore
     *
     * */
    public void queryMTomorrow() {

        // Index all the instance for the calendars functionally
        long dtEndMilli = 0;
        int liveMeeting = 0;
        int countMeetings = 0;
        int trueMCountTemp = 0;
        int fasleMCountTemp = 0;
        int position = 0;
        String dtLocation = "";
        String dtTitle = "";
        String dtDate = "";
        String dtStart = "";
        String dtEnd = "";
        String dtDescription = "";

        /*
         * Get all the instance of the calendar who will be needed to get for the application.
         * Make the point there all instance have the post '0' to '9'
         * **/
        String[] projection = new String[] {
                CalendarContract.Events.CALENDAR_ID, 	// '0'
                CalendarContract.Events.TITLE, 			// '1'
                CalendarContract.Events.DESCRIPTION, 	// '2'
                CalendarContract.Events.DTSTART, 		// '3'
                CalendarContract.Events.DTEND, 			// '4'
                CalendarContract.Events.EVENT_LOCATION, // '5' Live or Lync - meeting

        };

        // Format time for HH:MM
        SimpleDateFormat formatTime = new SimpleDateFormat("HH:mm");
        formatTime.setTimeZone(TimeZone.getDefault());

        /**
         * RANGE for the CALENDAR important, 0 = January, 1 = February, ...
         * */
        Calendar startTime = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        startTime.set(tYear, tMonth, tDay, sMeetingStartHH, sMeetingStartMM);

        Calendar endTime = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        endTime.set(tYear, tMonth, tDay, sMeetingStopHH, sMeetingStopMM);

        // Logic settings for this method to work, doesn't really what its for?
        String selection = "(( " + CalendarContract.Events.DTSTART + " >= " + startTime.getTimeInMillis() + " )"
                + "AND " +
                "( " + CalendarContract.Events.DTSTART + " <= " + endTime.getTimeInMillis() + " ))";


        // Settings for the cursor, for the calendar package
        Cursor cursor = this.getBaseContext().getContentResolver().query(CalendarContract.Events.CONTENT_URI, projection,selection, null, null);

        if (cursor.getCount() > 0) {

            while (cursor.moveToNext()) {

                countMeetings =cursor.getCount(); // How many meetings is there 'today'
                cursor.getString(0); // get the calendar ID not in used but make point most be there for the post '0'
                dtTitle = cursor.getString(1); // get the title (subject) of the meetings
                dtDescription = cursor.getString(2); // get the txt message in the meetings
                dtDate = (new Date(cursor.getLong(3))).toString(); // DTDate in date format 2015-02-25
                dtStart = cursor.getString(3); // DTStart in milliseconds
                dtEnd = cursor.getString(4); // DTEnd in milliseconds
                dtLocation = cursor.getString(5); // get the location where the meetings will be setup in this case >> Lync-meeting

                this.dtStartMilliTomorrow = Long.parseLong(dtStart);
                dtStart = formatTime.format(dtStartMilliTomorrow);

                // If the value not is NULL there is a 'true meeting' add to database
                if( dtTitle != null && dtDescription != null && dtDate != null && dtStart != null && dtEnd != null && dtLocation != null){

                    // Search in 'descriptoin' for index 'ID:'
                    String idIndex = "ID:";
                    position = dtDescription.indexOf(idIndex);
                    dtEndMilli = Long.parseLong(dtEnd);
                    dtEnd = formatTime.format(dtEndMilli);
                }

                // If there is any value with NULL throw the 'meetings is false'
                if( dtTitle == null || dtDescription == null || dtDate == null || dtStart == null || dtEnd == null || dtLocation == null){

                    liveMeeting = 0;
                    position = -1;

                }
                // True there is live meetings
                if(position != -1){

                    liveMeeting = 1;

                }

                // False there is NO live meetings
                if(position == -1){

                    liveMeeting = 0;
                }

                if(liveMeeting == 1){

                    /*
                     * IMPORTANT: this attribute 'millisecond' is for to
                     * compare and sort the meetings DO NOT CHANGE THIS!!!
                     *
                     * **/
                    String millisecond = Long.toString(dtStartMilliTomorrow);

                    // This ID, will set database id's and count the 'true' meetings
                    int id = 1;

                    // Append true meeting will be added with num '1' every loops
                    trueMCountTemp = trueMCountTemp + id;

                    setMeetingsTomorrow(id, dtLocation, dtTitle, dtDate, dtStart, dtEnd, dtDescription, millisecond);

                    /* Set notification 'No more live-meeting Today'
                     * And a small icon in the statubar
                     * **/
                    setNotification();

                } else {

                    // DO NOTHING ! ! !

                }
            }
        }

        // Print out and show all the countec meetings true, false and total
        fasleMCountTemp = countMeetings - trueMCountTemp;
        Log.d(getClass().getName(), "[[[ Total meetings in 'Calendar' tomorrow: >> " + countMeetings);
        Log.d(getClass().getName(), "[[[ False NONE meetings in 'Calendar' tomorrow: >> " + fasleMCountTemp);
        Log.d(getClass().getName(), "[[[ TRUE meetings in 'Calendar' tomorrow: >> " + trueMCountTemp);

    }

    /** IN USED in the application
     *
     * Description:
     * All the 'TODAY' calendar events will be sorted indexed and saved in the database.
     *
     * */
    public void setMeetingsTomorrow(int id, String location, String title, String date, String start, String end, String description, String millisecond){
        SortCharToDigits sortChar = new SortCharToDigits();


        this.setIDTomorrow	= setIDTomorrow + id;
        int ids = setIDTomorrow;
        this.countMeetingTomorrow = ids;

        int textID = 0;
        String index = "";
        String setdtmf = "";
        String setLocation = location;
        String setTitle = title;
        String setDate = date;
        String setStart = start;
        String setEnd = end;
        String setDescription = description;
        String setMillisecond = millisecond;

        // ID: index of first position
        index = "ID:";

        // Get index/position of 'ID:' in the message, to retreive the dtmf-numbers
        textID = setDescription.indexOf(index);

        // Get SUB-STRING, position where 'ID:' is started
        setDescription = setDescription.substring(textID);

        // Move 5 step 'forward' to reach the first emtpy ' ' character, find position
        int position = setDescription.indexOf(' ', 5);

        // if 'position != -1' get and SET the SUB-STRING '0 - position'
        if(position != -1){
            setDescription = setDescription.substring(0, position);
        }

        // if 'position == -1' get and SET the SUB-STRING '0'
        if(position == -1){
            setDescription = setDescription.substring(0);
        }

        // Sort the SUB-STRING and copy into the DTMF-STRING
        setdtmf = sortChar.sortCharToDigits(setDescription);

        // Create substring of 'Title to max 20 char and Location to 15 char'
        if(setTitle.length() > 19){

            int indexS;
            setTitle = setTitle.substring(0, 19);
            indexS = setTitle.lastIndexOf(' ');
            setTitle = setTitle.substring(0, indexS)  + "...";
        }

        if(setLocation.length() > 14){

            int indexss;
            setLocation = setLocation.substring(0, 14);
            indexss = setLocation.lastIndexOf(' ');
            setLocation = setLocation.substring(0, indexss);

        }

        Log.d("[ TITLE > ", setTitle + " | LOCATOIN > " + setLocation);

        /*
         * The ID + meeting-strings lenghts till be passed to 'setDataBase',
         * The string will be saved into the database DBAdapter.
         * **/
        Log.d(getClass().getName(), "IDS (QueryMTomorrow()): >> " + ids);
        setDataBaseTomorrow(ids, setLocation, setTitle, setDate, setStart, setEnd, setdtmf, setMillisecond);

    }

    /** IN USED in the application
     *
     * Description:
     * Will add new meetings-contacts automatic in the phonebook.
     *
     * */
    public void setMeetingsToPhoneContactlist(){

        for (int i = 1; i <= countMeetingToday; i++) {

            int ids = i;

            Log.d(getClass().getName(), "ID value in 'getMeetingsFromDB': >> " + ids);

            UserData meetingData = DBAdapter.getUserData(ids);

            String dbStart = meetingData.getStart();
            String dbDtmf = meetingData.getDTMF();

            Log.d("Reading data: ", "of a meeting row");
            Log.d("DBStart :" , dbStart);
            Log.d("DBDTMF :" , dbDtmf);


            PhoneContactListHandler addContacts = new PhoneContactListHandler();
            addContacts.addAsContactAutomatic(this, ids, dbStart, dbDtmf, "");

        }

    }

    /** IN USED in the application
     *
     * Delete the added meetings to the phones contactlist
     *
     * */
    public void deleteContacts(){

        for (int i = 1; i <= countMeetingToday; i++) {

            int ids = i;

            UserData getAData = DBAdapter.getUserData(ids);
            String dtmf = getAData.getDTMF();

            // deleteContact will be in format +4685454000,12345784#
            String deleteContact = dedicateNumber + "," + dtmf + "#";
            Log.d("[ Delete contacts >> ", deleteContact + " ]");

            PhoneContactListHandler.deleteContact(this, deleteContact);

        }
    }

    /** IN USED in the application
     *
     * About:
     * Will alerts information about the Conference Call
     *
     * */
    public void dialogAlert() {

        String aboutmsg =
                "Conference Call version: " + ccVersion  + "\nAndroid version: " + androidVersion
                        + "\nDeveloped by Peter Albertsson.\n"
                        + "Team Mobile Solution in Sweden.\n\n"
                        + "The Conference Call will make it easier for the user to join the live meetings."
                        + " The user have not to type the digits to join the meeting,"
                        + " this is automatic done by the application."
                        + "Hope you will enjoy this mobile application.\n"
                        + "\n? All rights is reserved by Capgemini Sweden AB | 2015";

        if (!aboutmsg.equals("")) {

            AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();

            alertDialog.setTitle("About");
            alertDialog.setMessage(aboutmsg);

            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Ok",

                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();

        }
    }

    /** IN USED in the application
     *
     * This method has the function to 'make call'
     * */
    public void doLiveMeetingCall(String dtmf_number) {

        String calling = "tel:" + dedicateNumber + "," + dtmf_number + "#";
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse(calling));
        startActivity(callIntent);

    }

    /** IN USED in the application
     *
     * This method will take care about the phone 'events'
     * */
    private class PhoneCallListener extends PhoneStateListener {

        private boolean isPhoneCalling = false;

        String LOG_TAG = "LOGGING 123";

        @Override
        public void onCallStateChanged(int state, String incomingNumber) {

            if (TelephonyManager.CALL_STATE_RINGING == state) {
                // phone ringing
                Log.i(LOG_TAG, "RINGING, number: " + incomingNumber);
            }

            if (TelephonyManager.CALL_STATE_OFFHOOK == state) {
                // active
                Log.i(LOG_TAG, "OFFHOOK");

                isPhoneCalling = true;
            }

            if (TelephonyManager.CALL_STATE_IDLE == state) {
                // run when class initial and phone call ended, need detect flag
                // from CALL_STATE_OFFHOOK
                Log.i(LOG_TAG, "IDLE");

                if (isPhoneCalling) {

                    Log.i(LOG_TAG, "restart app");

                    // restart app
                    Intent i = getBaseContext().getPackageManager()
                            .getLaunchIntentForPackage(
                                    getBaseContext().getPackageName());
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);

                    isPhoneCalling = false;
                }

            }
        }
    }



}

// SAVED ATTRIBUTES FOR THE FUTURE :)

//if(this.addContact != false){
//	// Feature true add Contact-Meetings to the phones
//	setMeetingsToPhoneContactlist();
//	}

//if(carModeOn.equals("1")){
//
//	this.addContact = true;
//}

//Intent i = new Intent(SettingsActivity.this, MainActivity.class);
//startActivity(i);

// Get contrieds codes
//TelephonyManager tm = (TelephonyManager)this.getSystemService(this.TELEPHONY_SERVICE);
//String countryCodeValue = tm.getNetworkCountryIso();
//Log.d("[[[[[[ LAND  >>> ", countryCodeValue);

// Show the '<' in the Action-Bar | Save this to next activity
//getActionBar().setDisplayHomeAsUpEnabled(true);

// Log.d(getClass().getName(), "ID value in 'setDataBaseToday': >> " + id);

// SAVED AND INVOKED METHODS FOR THE FUTURE :)

//for (int i = 1; i <= countMeetingToday; i++) {
//
//	ids = i;
//	Log.d(getClass().getName(), "IDS (Today) - for - loop: >> " + ids);
//
//}

/** Timer to be used in the future
 * */
//public void Timer(){
//
//final ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();
//exec.scheduleAtFixedRate(new Runnable() {
//
//  @Override
//  public void run() {
//
//
//  }
//}, 0, 1, TimeUnit.MINUTES);
//}

/**
 * This method will do split string with ',' will save this method
 * can be used in the future.
 * */
//public void stringTokenSplit(String tokenSplit){
//
//	Log.d("stringTokenSplit >> ", tokenSplit);
//
//	StringTokenizer st = new StringTokenizer(tokenSplit, ",");
//
//	this.location = st.nextToken();
//	this.setSubjectAttribute = st.nextToken();
//	this.DateToday = st.nextToken();
//	this.setStartAttribute = st.nextToken();
//	this.setEndtAttribute = st.nextToken();
//	this.DTMFnumber = st.nextToken();
//
//}

/**
 * This method will write to txt-file, will save this method
 * Can be used in the future.
 * */
//private void writeToFile(String data) {
//    try {
//        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(openFileOutput("config.txt", Context.MODE_PRIVATE));
//        outputStreamWriter.write(data);
//        outputStreamWriter.close();
//    }
//    catch (IOException e) {
//        Log.e("Exception", "File write failed: " + e.toString());
//    }
//}

/**
 * This method will creat the txt-file, will save this method
 * Can be used in the future.
 * */
//private String readFromFile() {
//
//    String ret = "";
//
//    try {
//        InputStream inputStream = openFileInput("config.txt");
//
//        if ( inputStream != null ) {
//            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
//            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
//            String receiveString = "";
//            StringBuilder stringBuilder = new StringBuilder();
//
//            while ( (receiveString = bufferedReader.readLine()) != null ) {
//                stringBuilder.append(receiveString);
//            }
//
//            inputStream.close();
//            ret = stringBuilder.toString();
//        }
//    }
//    catch (FileNotFoundException e) {
//        Log.e("login activity", "File not found: " + e.toString());
//    } catch (IOException e) {
//        Log.e("login activity", "Can not read file: " + e.toString());
//    }
//
//    return ret;
//}

/** not in used, but will save this method
 *
 * The method will do an insert into the database DBAdapter
 *
 * */
//public void updateMeetings(int id){
//
//	Log.d(getClass().getName(), "ID-v?rde kommer in i metoden: >> " + id);
//
//	for (int i = 1; i <= id; i++) {
//
//	Log.d(getClass().getName(), "ID-v?rde Loopar: >> " + i);
//	DBAdapter.updateTest( i, "TEST","TEST", "TEST", "TEST", "TEST", "TEST");
//
//	}
//}
