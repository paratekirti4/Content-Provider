package com.example.quagnitia.contactdemocontentprovider;

import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;

import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final int REQ_CODE_PER = 1236;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_CONTACTS)) {
//                    new AlertDialog.Builder(this)
//                            .setTitle("Request")
//                            .setMessage("Dont worry we will not steal your data :)")
//                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//
//                                    ActivityCompat.requestPermissions(MainActivity.this,
//                                            new String[]{Manifest.permission.READ_CONTACTS},
//                                            REQ_CODE_PER);
//                                }
//                            }).create().show();

            } else {

                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.READ_CONTACTS},
                        REQ_CODE_PER);
            }
        } else {
            loadContacts();
        }
    }

    private void loadContacts() {

        ContentResolver resolver = getContentResolver();

        //Path storage done for contacts present in phone
        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        //Array created for displaying contact name and number
        String[] projection = {ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER};
        String selection = null;
        String[] selectionArgs = null;
        String sortOrder = null;

        ArrayList<String> dataSet = new ArrayList<>();
        Cursor cursor = resolver.query(uri, projection, selection, selectionArgs, sortOrder);
        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String num = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            dataSet.add(name + "\n" + num);
        }
           /* ListView lvContact;
            lvContact=findViewById(R.id.lvContact);
            ArrayAdapter arrayAdapter=new ArrayAdapter(this,android.R.layout.simple_list_item_1,dataSet);
            lvContact.setAdapter(arrayAdapter);*/

        ((ListView) findViewById(R.id.lvContact)).setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, dataSet));

    }


    /*  private void loadMyData() {
                ContentResolver resolver = getContentResolver();

                Uri uri = Uri.parse("content://com.codekul.provider.AUTH");
                String[] projection = null;
                String selection = null;
                String[] selectionArgs = null;
                String sortOrder = null;

                ArrayList<String> dataSet = new ArrayList<>();

                Cursor cursor = resolver.query(uri, projection, selection, selectionArgs, sortOrder);
                while (cursor.moveToNext()) {
                    String name = cursor.getString(cursor.getColumnIndex("product_name"));
                    String cost = cursor.getString(cursor.getColumnIndex("product_cost"));
                    dataSet.add(name + "\n" + cost);
                }

                ((ListView) findViewById(R.id.lvContact))
                        .setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, dataSet));
            }
    */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQ_CODE_PER) {
            if (grantResults.length > 0) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    loadContacts();
                }
            }
        }
    }

}

