package com.example.omurbek.myapplication;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final int RESULT_PICK_CONTACT = 4546;
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;
    private static final String APP_PREFERENCE_KEY = "SAKTAN_TEAM_2";
    SharedPreferences preferences;
    private ListView selectedContactList;
    ListView listView;
    List<RowItem> rowItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        selectedContactList = (ListView) findViewById(R.id.selectedContactList);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Picking contact", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
                startActivityForResult(intent, RESULT_PICK_CONTACT);
                Log.i("mememe", "AFTer contact Picked");
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

//        TODO
//        startService(new Intent(this, SOSService.class));
    }

    private void updateContactList() {
        ArrayList<String> list = new ArrayList<String>();
        rowItems = new ArrayList<RowItem>();
        String contacts = preferences.getString(APP_PREFERENCE_KEY, "");
        if (contacts.length() < 1) return;
        String[] contactList = contacts.split(",");

        Log.i("updateContactList", contacts);

        for (int i = 0; i < contactList.length; i++) {
            String[] contact = contactList[i].split("#");
            Log.i("inarray" + i, contactList[i] + " =>> " + contact[0] + " - " + contact[1] + " -- " + contact[2]);
            String contactName = contact[0];
            String contactNumber = contact[1];
            String photoUri = contact[2];
            list.add(contactName + " ### " + contactNumber + " photo : " + photoUri);
            RowItem item = new RowItem(contact[0], contact[1], photoUri);
            rowItems.add(item);
        }

        CustomListViewAdapter adapter = new CustomListViewAdapter(this,
                R.layout.mylistview, rowItems);
        selectedContactList.setAdapter(adapter);
        selectedContactList.setAdapter(adapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i("onActivityResult", data.getData().toString());
        Log.i("onActivityResult", requestCode + "");
        Log.i("onActivityResult", resultCode + "");
        // check whether the result is ok
        if (resultCode == RESULT_OK) {
            // Check for the request code, we might be usign multiple startActivityForReslut
            switch (requestCode) {
                case RESULT_PICK_CONTACT:
                    contactPicked(data);
                    break;
            }
        } else {
            Log.e("MainActivity", "Failed to pick contact");
        }
    }

    /**
     * Query the Uri and read contact details. Handle the picked contact data.
     *
     * @param data
     */
    private void contactPicked(Intent data) {
        Log.i("contactPicked", data.getDataString());
        Cursor cursor = null;
        try {
            String phoneNo = null;
            String name = null;
            long photoUri;
            Uri uri = data.getData();
            cursor = getContentResolver().query(uri, null, null, null, null);
            cursor.moveToFirst();
            int phoneIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
            int nameIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
            int photo = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI);
            phoneNo = cursor.getString(phoneIndex);
            name = cursor.getString(nameIndex);
            photoUri = cursor.getLong(photo);

            saveContact(phoneNo, name, photoUri);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveContact(String phoneNo, String name, Long photo) {
        Log.i("CONTACT", name + " ## " + phoneNo);
        String phoneNumbers = preferences.getString(APP_PREFERENCE_KEY, "");

        if (phoneNumbers.indexOf(phoneNo) == -1 && phoneNumbers.indexOf(name) == -1) {
            phoneNumbers = phoneNumbers + name + "#" + phoneNo + "#" + photo + ",";
            preferences.edit().putString(APP_PREFERENCE_KEY, phoneNumbers).commit();
            Log.i(APP_PREFERENCE_KEY, phoneNumbers);
            Log.i("updatecantata", phoneNumbers);
            updateContactList();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
