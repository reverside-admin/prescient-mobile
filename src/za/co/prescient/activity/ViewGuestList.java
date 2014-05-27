package za.co.prescient.activity;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.*;
import org.json.JSONArray;
import org.json.JSONObject;
import za.co.prescient.R;

import java.util.ArrayList;
import java.util.HashMap;

import static za.co.prescient.activity.model.Constant.*;

/**
 * Created by Bibhuti on 2014/05/25.
 */

public class ViewGuestList extends Activity {
    private ArrayList<HashMap> list;
    ViewGuestListAdapter adapter;
    SessionManager session;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.guest_list_view_main);

        //check  for session
        session = new SessionManager(getApplicationContext());
        Log.i("check", "checking the session");
        session.checkSession();

        ListView lview = (ListView) findViewById(R.id.listview);
        populateList();
        adapter = new ViewGuestListAdapter(this, list);
        lview.setAdapter(adapter);
    }


    private void populateList() {
        TableRow tbr = (TableRow) findViewById(R.id.tableHeader);
        TextView textView = new TextView(this);
        textView.setText("TITLE");
        textView.setWidth(100);
        textView.setTypeface(null, Typeface.BOLD);
        textView.setTextColor(Color.WHITE);
        tbr.addView(textView);

        textView = new TextView(this);
        textView.setText("FIRST NAME");
        textView.setWidth(100);
        textView.setTypeface(null, Typeface.BOLD);
        textView.setTextColor(Color.WHITE);
        tbr.addView(textView);

        textView = new TextView(this);
        textView.setText("SUR NAME");
        textView.setWidth(100);
        textView.setTypeface(null, Typeface.BOLD);
        textView.setTextColor(Color.WHITE);
        tbr.addView(textView);


        textView = new TextView(this);
        textView.setText("PREFERRED NAME");
        textView.setWidth(100);
        textView.setTypeface(null, Typeface.BOLD);
        textView.setTextColor(Color.WHITE);
        tbr.addView(textView);
        tbr.setBackgroundColor(Color.RED);


        try {
            Bundle b = getIntent().getExtras();
            String guests = b.getString("guests");
            JSONArray jsonArray = new JSONArray(guests);

            // Toast.makeText(getApplicationContext(), "DATA:: " + guests, Toast.LENGTH_SHORT).show();

            list = new ArrayList<HashMap>();
            HashMap temp;


            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = (JSONObject) jsonArray.get(i);
                String title = obj.getString("title");
                String firstName = obj.getString("firstName");
                String surName = obj.getString("surname");
                String preferredName = obj.getString("preferredName");
                Long id = obj.getLong("id");
                /*String gender=obj.getString("gender");
                String nationality=obj.getString("nationalityId");
                Long dateTimeMill=obj.getLong("dob");
                Date date=new Date(dateTimeMill);*/


                temp = new HashMap();
                temp.put(FIRST_COLUMN, title);
                temp.put(SECOND_COLUMN, firstName);
                temp.put(THIRD_COLUMN, surName);
                temp.put(FOURTH_COLUMN, preferredName);
                temp.put(FIFTH_COLUMN, id);
                /*temp.put(SIXTH_COLUMN,nationality);
                temp.put(SEVENTH_COLUMN,date.toString());
                temp.put(EIGHTH_COLUMN,nationality);
                temp.put(NINTH_COLUMN,nationality);
                temp.put(TENTH_COLUMN,nationality);*/

                list.add(temp);
            }
        } catch (Exception ee) {
            ee.getMessage();
        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.opt1:
                logOut();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void logOut() {
        session.logOut();

        Log.i("Manager Login status after logout::", Boolean.toString(session.isLoggedIn()));
        Intent intent = new Intent(getApplicationContext(), LoginPage.class);
        startActivity(intent);
    }


}