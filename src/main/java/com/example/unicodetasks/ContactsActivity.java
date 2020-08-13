package com.example.unicodetasks;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.content.Intent;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class ContactsActivity extends AppCompatActivity {
    //ListView listView;
    //String[] from;
    //int[] to;

    private static final int REQUEST_CODE_PICK_CONTACTS = 1;
    private Uri uriContact;
    private String contactID;
    Button loadButton;// contacts unique ID
    TextView nameTextView;
    TextView numberTextView;
    TextView emailTextView;

    public void thirdActivity(View view){
       Intent intent = new Intent(this, WeatherActivity.class);
       startActivity(intent);

    }


    public void showContacts(View view){

        startActivityForResult(new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI), REQUEST_CODE_PICK_CONTACTS);
        nameTextView.setVisibility(View.VISIBLE);
        numberTextView.setVisibility(View.VISIBLE);
        emailTextView.setVisibility(View.VISIBLE);


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        loadButton = findViewById(R.id.loadButton);
        nameTextView = findViewById(R.id.nameTextView);
        numberTextView = findViewById(R.id.numberTextView);
        emailTextView = findViewById(R.id.emailTextView);

        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_CONTACTS}, PackageManager.PERMISSION_GRANTED);

        //listView= findViewById(R.id.listView);

       /* try{
            Cursor cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,null,null,null);
            startManagingCursor(cursor);

             from = new String[]{ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER
                     , ContactsContract.CommonDataKinds.Phone._ID};

             to= new int[]{android.R.id.text1, android.R.id.text2};

            SimpleCursorAdapter simpleCursorAdapter = new SimpleCursorAdapter(this , android.R.layout.simple_list_item_1, cursor , from , to);

            listView.setAdapter(simpleCursorAdapter);
            listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

    }catch(Exception e){
            e.printStackTrace();
        }*/

      /* listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           @Override
           public void onItemClick(AdapterView<?> parent, View view, int position, long id){
               try{

                   retrieveContactName();
                   retrieveContactNumber();
               }catch(Exception e){
                 e.printStackTrace();
               }
           }
       });
*/

    }


        @Override
        protected void onActivityResult ( int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_PICK_CONTACTS && resultCode == RESULT_OK) {
            uriContact = data.getData();

            retrieveContactName();
            retrieveContactNumber();
            retrieveContactEmail();


        }
    }

    private void retrieveContactNumber() {

        String contactNumber = null;

        // getting contacts ID
        Cursor cursorID = getContentResolver().query(uriContact,
                new String[]{ContactsContract.Contacts._ID},
                null, null, null);

        if (cursorID.moveToFirst()) {

            contactID = cursorID.getString(cursorID.getColumnIndex(ContactsContract.Contacts._ID));
        }

        cursorID.close();



        // Using the contact ID now we will get contact phone number
        Cursor cursorPhone = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER},

                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ? AND " +
                        ContactsContract.CommonDataKinds.Phone.TYPE + " = " +
                        ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE,

                new String[]{contactID},
                null);

        if (cursorPhone.moveToFirst()) {
            contactNumber = cursorPhone.getString(cursorPhone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
        }

        cursorPhone.close();

        numberTextView.setText(contactNumber);


    }

    private void retrieveContactName() {

        String contactName = null;

        // querying contact data store
        Cursor cursor = getContentResolver().query(uriContact, null, null, null, null);

        if (cursor.moveToFirst()) {

            // DISPLAY_NAME = The display name for the contact.
            // HAS_PHONE_NUMBER =   An indicator of whether this contact has at least one phone number.

            contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
        }

        cursor.close();
        nameTextView.setText(contactName);
    }

    private void retrieveContactEmail(){

        String contactEmail = null;

        Cursor cursorPhone = getContentResolver().query(ContactsContract.CommonDataKinds.Email.CONTENT_URI,null,


                ContactsContract.CommonDataKinds.Email.CONTACT_ID + "= ?",
                new String[]{contactID},
                null);



        if(cursorPhone.moveToFirst()){

            contactEmail = cursorPhone.getString(cursorPhone.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
        }

        cursorPhone.close();
        emailTextView.setText(contactEmail);
    }
}



