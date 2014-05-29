package za.co.prescient.activity;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import static za.co.prescient.activity.model.Constant.SECOND_COLUMN;

public class ViewGuestList extends Activity {
    private ArrayList<HashMap> list;
    ViewGuestListAdapter adapter;
    SessionManager session;
    ListView lview;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.guest_list_view_main);

        //check  for session
        session = new SessionManager(getApplicationContext());
        Log.i("check", "checking the session");
        session.checkSession();

        TableRow tableSearchRow=(TableRow)findViewById(R.id.tableSearchRow);
        tableSearchRow.setBackgroundColor(Color.DKGRAY);
        lview = (ListView) findViewById(R.id.listview);
        populateHeader();
        populateList();
        //adapter = new ViewGuestListAdapter(this, list);
        //lview.setAdapter(adapter);


        final EditText editText = (EditText) findViewById(R.id.edSearch);
        editText.setBackgroundColor(Color.WHITE);
        Button searchButton = (Button) findViewById(R.id.btnSearch);


        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                Log.i(" search status::", "Text changed");
                String searchText = editText.getText().toString();
                if (searchText.length() == 0) {
                    populateList();
                } else {
                    search(searchText);
                }

            }
        });
    }


    public void search(String text) {
        ArrayList<HashMap> hashMapsList = new ArrayList<HashMap>();
        for (HashMap map : list) {

            String title=(String)map.get(FIRST_COLUMN);
            String firstName = (String) map.get(SECOND_COLUMN);
            String surName = (String) map.get(THIRD_COLUMN);
            String preferredName = (String) map.get(FOURTH_COLUMN);

            String rowData=title+firstName+surName+preferredName;
            if (rowData.toLowerCase().contains(text.toLowerCase())) {
                hashMapsList.add(map);
                //hashMapsList.add(map);
               // hashMapsList.add(map);
            }
        }
        adapter = new ViewGuestListAdapter(this, hashMapsList);
        lview.setAdapter(adapter);
    }


    private void populateHeader() {
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
    }

    private void populateList() {
        try {
            Bundle b = getIntent().getExtras();
            String guests = b.getString("guests");
            JSONArray jsonArray = new JSONArray(guests);

            list = new ArrayList<HashMap>();
            HashMap temp;


            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = (JSONObject) jsonArray.get(i);
                String title = obj.getString("title");
                String firstName = obj.getString("firstName");
                String surName = obj.getString("surname");
                String preferredName = obj.getString("preferredName");
                Long id = obj.getLong("id");

                temp = new HashMap();
                temp.put(FIRST_COLUMN, title);
                temp.put(SECOND_COLUMN, firstName);
                temp.put(THIRD_COLUMN, surName);
                temp.put(FOURTH_COLUMN, preferredName);
                temp.put(ID_COLUMN, id);

                list.add(temp);
            }
            adapter = new ViewGuestListAdapter(this, list);
            lview.setAdapter(adapter);
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