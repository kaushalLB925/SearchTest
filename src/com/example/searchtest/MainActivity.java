package com.example.searchtest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import com.Z2I.search.R;

public class MainActivity extends Activity implements OnItemClickListener {

private ArrayList<Map<String, String>> mPeopleList;
private SimpleAdapter mAdapter;
private AutoCompleteTextView mTxtPhoneNo;
private Button call;
String number;

@Override
public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    mPeopleList = new ArrayList<Map<String, String>>();
    PopulatePeopleList();
    mTxtPhoneNo = (AutoCompleteTextView) findViewById(R.id.mmWhoNo);
    call=(Button) findViewById(R.id.button1);
    mAdapter = new SimpleAdapter(this, mPeopleList, R.layout.custcontview,
            new String[] { "Name", "Phone", "Type" }, new int[] {
                    R.id.ccontName, R.id.ccontNo, R.id.ccontType });
    mTxtPhoneNo.setAdapter(mAdapter);
    
    mTxtPhoneNo.setOnItemClickListener(new OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> av, View arg1, int index,
                long arg3) {
            Map<String, String> map = (Map<String, String>) av.getItemAtPosition(index);

            String name  = map.get("Name");
           number = map.get("Phone");
            mTxtPhoneNo.setText(name+" " +number);
        }
    });
    
    call.setOnClickListener(new View.OnClickListener() {
		public void onClick(View v) {
			//Toast.makeText(MainActivity.this, number.toString(), Toast.LENGTH_SHORT).show();
			try {
				if (mTxtPhoneNo != null) {
					startActivity(new Intent(Intent.ACTION_CALL, Uri
							.parse("tel:" +number)));
				}else if(mTxtPhoneNo != null && number.toString().length()==0){
					Toast.makeText(getApplicationContext(), "You missed to type the number!", Toast.LENGTH_SHORT).show();
				}else if(mTxtPhoneNo != null && mTxtPhoneNo.getText().length()<10){
					Toast.makeText(getApplicationContext(), "Check whether you entered correct number!", Toast.LENGTH_SHORT).show();
				}
			} catch (Exception e) {
				Log.e("DialerAppActivity", "error: " + e.getMessage(),
						e);
			}
		}
	});
}

public void PopulatePeopleList() {
    mPeopleList.clear();
    Cursor people = getContentResolver().query(
            ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
    while (people.moveToNext()) {
        String contactName = people.getString(people
                .getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
        String contactId = people.getString(people
                .getColumnIndex(ContactsContract.Contacts._ID));
        String hasPhone = people
                .getString(people
                        .getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));

        if ((Integer.parseInt(hasPhone) > 0)){
            // You know have the number so now query it like this
            Cursor phones = getContentResolver().query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null,
            ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = "+ contactId,
            null, null);
            while (phones.moveToNext()){
                //store numbers and display a dialog letting the user select which.
                String phoneNumber = phones.getString(
                phones.getColumnIndex(
                ContactsContract.CommonDataKinds.Phone.NUMBER));
                String numberType = phones.getString(phones.getColumnIndex(
                ContactsContract.CommonDataKinds.Phone.TYPE));
                Map<String, String> NamePhoneType = new HashMap<String, String>();
                NamePhoneType.put("Name", contactName);
                NamePhoneType.put("Phone", phoneNumber);
                if(numberType.equals("0"))
                    NamePhoneType.put("Type", "Work");
                    else
                    if(numberType.equals("1"))
                    NamePhoneType.put("Type", "Home");
                    else if(numberType.equals("2"))
                    NamePhoneType.put("Type",  "Mobile");
                    else
                    NamePhoneType.put("Type", "Other");
                    //Then add this map to the list.
                    mPeopleList.add(NamePhoneType);
            }
            phones.close();
        }
    }
    people.close();
    startManagingCursor(people);
}


/*public void onItemClick(AdapterView<?> av, View v, int index, long arg){
    Map<String, String> map = (Map<String, String>) av.getItemAtPosition(index);
    Iterator<String> myVeryOwnIterator = map.keySet().iterator();
    while(myVeryOwnIterator.hasNext()) {
        String key=(String)myVeryOwnIterator.next();
        String value=(String)map.get(key);
        mTxtPhoneNo.setText(value);
    }
}*/

@Override
public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.activity_main, menu);
    return true;
}

@Override
public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
	// TODO Auto-generated method stub
	
}

}