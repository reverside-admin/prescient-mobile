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
import android.widget.ListView;
import android.widget.TableRow;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONObject;
import za.co.prescient.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import static za.co.prescient.activity.model.Constant.*;

public class ViewCheckedInGuestList extends Activity {

    private ArrayList<HashMap> list;
    ViewCheckedInGuestListAdapter adapter;
    SessionManager session;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.checked_in_guest_list_view_main);

        //check  for session
        session = new SessionManager(getApplicationContext());
        Log.i("check", "checking the session");
        session.checkSession();

        ListView lview = (ListView) findViewById(R.id.checkedInGuestView);
        populateList();
        adapter = new ViewCheckedInGuestListAdapter(this, list);
        lview.setAdapter(adapter);

    }

    private void populateList() {

        TableRow tbr = (TableRow) findViewById(R.id.tableHeaderRow);
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

        textView = new TextView(this);
        textView.setText("ROOM NUMBER");
        textView.setWidth(100);
        textView.setTypeface(null, Typeface.BOLD);
        textView.setTextColor(Color.WHITE);
        tbr.addView(textView);
        tbr.setBackgroundColor(Color.RED);

        textView = new TextView(this);
        textView.setText("ARRIVAL DATE");
        textView.setWidth(100);
        textView.setTypeface(null, Typeface.BOLD);
        textView.setTextColor(Color.WHITE);
        tbr.addView(textView);
        tbr.setBackgroundColor(Color.RED);


        try {
            Bundle b = getIntent().getExtras();
            String guests = b.getString("checkedInGuests");
            JSONArray jsonArray = new JSONArray(guests);

            // Toast.makeText(getApplicationContext(), "DATA:: " + guests, Toast.LENGTH_SHORT).show();

            list = new ArrayList<HashMap>();
            HashMap temp;


            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject obj = (JSONObject) jsonArray.get(i);
                JSONObject guestProfileObj = obj.getJSONObject("guest");
                JSONObject roomObj = obj.getJSONObject("room");

                String title = guestProfileObj.getString("title");
                String firstName = guestProfileObj.getString("firstName");
                String surName = guestProfileObj.getString("surname");
                String preferredName = guestProfileObj.getString("preferredName");
                String roomNo = roomObj.getString("roomNumber");
                Date arrivalDate = new Date(obj.getLong("arrivalTime"));

                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                String arrDate = sdf.format(arrivalDate);


                temp = new HashMap();
                temp.put(FIRST_COLUMN, title);
                temp.put(SECOND_COLUMN, firstName);
                temp.put(THIRD_COLUMN, surName);
                temp.put(FOURTH_COLUMN, preferredName);
                temp.put(FIFTH_COLUMN, roomNo);
                temp.put(SIXTH_COLUMN, arrDate);

                list.add(temp);
                Log.i("List size : ", "" + list.size());

                System.out.println("Total List : " + list);
                Log.i("ResultData : ", ""+list.get(i));

            }
        } catch (Exception ee) {
            ee.getMessage();
            Log.i("EXP in List", ee.getMessage());
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
