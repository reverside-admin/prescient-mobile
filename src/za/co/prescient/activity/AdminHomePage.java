package za.co.prescient.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONObject;
import za.co.prescient.R;
import za.co.prescient.activity.model.TouchPoint;

import java.util.ArrayList;
import java.util.List;

public class AdminHomePage extends Activity {
    SessionManager session;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_admin_home_page);

        TextView tv=(TextView)findViewById(R.id.prescientName2);
        tv.setText(Html.fromHtml(getString(R.string.prescient_name)));


        ((TextView) findViewById(R.id.application_name2)).setTextColor(Color.WHITE);
        ((TextView) findViewById(R.id.role)).setTextColor(Color.WHITE);


        String userName = this.getIntent().getStringExtra("name");
		//((TextView) findViewById(R.id.userName)).setText(userName);
		Log.i("name : ", userName);
		Toast.makeText(getApplicationContext(), "Welcome " + userName.toUpperCase(), Toast.LENGTH_SHORT).show();

        //check session Here
        session=new SessionManager(getApplicationContext());
        Log.i("check","checking the session");
        session.checkSession();



        Button findGuest = (Button) findViewById(R.id.findGuestInLayout);
        findGuest.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                findGuestOnLayout();
            }
        });

        Button button = (Button) findViewById(R.id.displayAllTouchPoint);
        button.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayTouchPointList();
            }
        });

        //find guest single click
        Button findAGuest = (Button) findViewById(R.id.findAGuestInHotel);
        findAGuest.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                findAGuestInHotel();
            }
        });
	}

    public void findAGuestInHotel()
    {
        System.out.println("Service Invoked");
        System.out.println("session.getGuest() : " + ApplicationData.guest.getHotelId());

        Context context = getApplicationContext();

        Toast.makeText(getApplicationContext(), "HOTEL: " + ApplicationData.guest.getHotelId(), Toast.LENGTH_SHORT).show();
        try {
            String responseData = ServiceInvoker.getCheckedInGuests(session.getToken(), ApplicationData.guest.getHotelId());
            Log.i("responseData : ", responseData);

            JSONArray jsonArray = new JSONArray(responseData);
            Log.i("responseData array length::", "" + jsonArray.length());

            Intent intent = new Intent(getApplicationContext(), ViewCheckedInGuestList.class);
            Bundle b = new Bundle();
            b.putString("checkedInGuests", jsonArray.toString());
            intent.putExtras(b);
            startActivity(intent);
        } catch (Exception e) {
            Log.i("exception in findAGuest", e.getMessage());
        }
    }


    public void displayTouchPointList()
    {
        Log.i("Display guest list::", "clicked");
        try {
            String data = ServiceInvoker.getAllAssignedTouchPoint(session.getToken());
            // Toast.makeText(getApplicationContext(), "DATA:: " + data, Toast.LENGTH_SHORT).show();
            Log.i("JSON DATA::", data);


            List<TouchPoint> touchPointList = new ArrayList<TouchPoint>();
            JSONArray touchPointArray = new JSONArray(data);

            for (int i = 0; i < touchPointArray.length(); i++) {
                JSONObject obj = (JSONObject) touchPointArray.get(i);
                TouchPoint touchPoint = new TouchPoint();
                touchPoint.setId(obj.getLong("id"));
                touchPoint.setName(obj.getString("name"));

                touchPointList.add(touchPoint);
            }
            Log.i("ALL TP OBJECTS::", touchPointList.toString());
            //set the touchpointList in Application data
            ApplicationData.touchPointList = touchPointList;

            Intent intent = new Intent(getApplicationContext(), ViewTouchPointList.class);
            //Intent intent = new Intent(getApplicationContext(), GuestListView.class);
            //Intent intent = new Intent(getApplicationContext(), MultiColumnActivity.class);

            startActivity(intent);

        } catch (Exception e) {
            Log.i("exception", e.getMessage());
        }

    }

    public void findGuestOnLayout()
    {
        Context context = getApplicationContext();

        try {
            String data = ServiceInvoker.getAllAssignedTouchPoint(session.getToken());
            // Toast.makeText(getApplicationContext(), "DATA:: " + data, Toast.LENGTH_SHORT).show();
            Log.i("JSON DATA::", data);


            List<TouchPoint> touchPointList = new ArrayList<TouchPoint>();
            JSONArray touchPointArray = new JSONArray(data);

            for (int i = 0; i < touchPointArray.length(); i++) {
                JSONObject obj = (JSONObject) touchPointArray.get(i);
                TouchPoint touchPoint = new TouchPoint();
                touchPoint.setId(obj.getLong("id"));
                touchPoint.setName(obj.getString("name"));

                touchPointList.add(touchPoint);
            }
            Log.i("ALL TP OBJECTS::", touchPointList.toString());
            //set the touchpointList in Application data
            ApplicationData.touchPointList = touchPointList;

            Intent intent = new Intent(getApplicationContext(), ListViewActivity.class);
            startActivity(intent);

        } catch (Exception e) {
            Log.i("exception", e.getMessage());
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

    public void logOut()
    {
        session.logOut();
        Log.i(" manager Login status after logout::", Boolean.toString(session.isLoggedIn()));

        Intent intent = new Intent(getApplicationContext(), LoginPage.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        // Do Here what ever you want do on back press;
    }


}
