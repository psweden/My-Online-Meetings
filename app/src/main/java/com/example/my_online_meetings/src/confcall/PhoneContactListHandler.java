/**
 * 
 */
/**
 * @ author: Peter Albertsson
 * @ email: peter.albertsson63@gmail.com
 * @ Date: 19 april 2023
 * 
 * class-name: PhoneContactListHandler.java
 * Project:  
 * Conference Call
 * 
 * Description of the java-class:
 * This java-class will add and delete meetings who has been added.
 *
 * TODO: 
 * Nothig to do the class is ready
 * 
 * 
 */
package com.example.my_online_meetings.src.confcall;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.Context;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.net.Uri;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.provider.ContactsContract.PhoneLookup;
import android.util.Log;
import android.widget.Toast;

/**
 * @ author: Peter Albertsson
 * @ email: peter.albertsson63@gmail.com
 * @ Date: 19 april 2023
 *
 */
public class PhoneContactListHandler {

	/**
	 * 
	 */
	public PhoneContactListHandler() {
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * Automatically add a contact into someone's contacts list
	 * 
	 * @param context
	 *            Activity context
	 * @param person
	 *            {@link Person} to add to contacts list
	 */
		
	/** IN USED in the application
	 *
	 * The method will add meetings to the phone contact book
	 * 
	 * */
	public void addAsContactAutomatic(final Context context, int id, String start, String dtmfNum, String emails) {
		
		String addTitle = "Meeting";
		String addStart = start;
		String addSeparator = "-";
		String addContact = "A";
		String addNum = "";
		
	// Usend string for Cars will be >> A1-Meeting-12:45
		
	addNum = Integer.toString(id);	
	if(addNum.equals("1") || addNum.equals("2") || addNum.equals("3") 
	|| addNum.equals("4") || addNum.equals("5") || addNum.equals("6") 
	|| addNum.equals("7") || addNum.equals("8") || addNum.equals("9")){
		
		addNum = "0" + addNum;
	}
	
    addContact = addContact + addNum + addSeparator + addTitle + addSeparator + addStart;	    
    Log.d("Add in 'Contact-List >> ", addContact);
		
    String displayName = addContact;
    String mobileNumber = "4000" + "," + dtmfNum + "#";
    String email = emails;

    ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();

    ops.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
            .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
            .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null).build());

    // Names
    if (displayName != null) {
        ops.add(ContentProviderOperation
                .newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                .withValue(ContactsContract.Data.MIMETYPE,
                        ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME,
                        displayName).build());
    }

    // Mobile Number
    if (mobileNumber != null) {
        ops.add(ContentProviderOperation
                .newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                .withValue(ContactsContract.Data.MIMETYPE,
                        ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, mobileNumber)
                .withValue(ContactsContract.CommonDataKinds.Phone.TYPE,
                        ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE).build());
    }

    // Email
    if (email != null) {
        ops.add(ContentProviderOperation
                .newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                .withValue(ContactsContract.Data.MIMETYPE,
                        ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Email.DATA, email)
                .withValue(ContactsContract.CommonDataKinds.Email.TYPE,
                        ContactsContract.CommonDataKinds.Email.TYPE_WORK).build());
    }

    // Asking the Contact provider to create a new contact
    try {
        context.getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
    } catch (Exception e) {
        e.printStackTrace();
    }

    Toast.makeText(context, "Contact " + displayName + " added.", Toast.LENGTH_SHORT)
	            .show();
	}
		
	/** IN USED in the application
	 *
	 * The method will delete meetings in phone contact book
	 * 
	 * */
	public static boolean deleteContact(Context ctx,String phoneNumber) {
		Uri contactUri = Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI,
		                               Uri.encode(phoneNumber));
           Cursor cur = ctx.getContentResolver().query(contactUri, null, null,
                           null, null);
           try {
               if (cur.moveToFirst()) {
                   do {
                      @SuppressLint("Range") String lookupKey =
                        cur.getString(cur.getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY));
                      Uri uri = Uri.withAppendedPath(
                                     ContactsContract.Contacts.CONTENT_LOOKUP_URI,
                                     lookupKey);
                      ctx.getContentResolver().delete(uri, null, null);
                   } while (cur.moveToNext());
               }
 
           } catch (Exception e) {
                   System.out.println(e.getStackTrace());
           }
           return false;
	}
}
