package za.co.prescient.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.*;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONObject;
import za.co.prescient.R;
import za.co.prescient.activity.model.TouchPoint;

import java.util.ArrayList;
import java.util.List;

public class ManagerHomePage extends Activity {
    SessionManager session;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_manager_home_page);

        String userName = this.getIntent().getStringExtra("name");

        TextView tv = (TextView) findViewById(R.id.prescient_name);
        tv.setText(Html.fromHtml(getString(R.string.prescient_name)));

        ((TextView) findViewById(R.id.application_name1)).setTextColor(Color.WHITE);
        ((TextView) findViewById(R.id.user_role)).setTextColor(Color.WHITE);

        //check session Here
        session = new SessionManager(getApplicationContext());
        Log.i("check", "checking the session");
        session.checkSession();

        userName = this.getIntent().getStringExtra("name");
        //((TextView) findViewById(R.id.userName)).setText(userName);
        Log.i("name : ", userName);
//        String uid = this.getIntent().getStringExtra("id");
//        Log.i("id : ", uid);
        Toast.makeText(getApplicationContext(), "Welcome " + userName.toUpperCase(), Toast.LENGTH_SHORT).show();

        //add a alert here for testing purpose

        //call a service to find out list of all guests whose bday is today and iterate the showDialog()
       try{
           Log.i("get all bday guests",".");
          String responseData = ServiceInvoker.getGuestsWhoseBdayIsToday(session.getToken(), ApplicationData.currentUserHotelId);
          JSONArray bdayGuests=new JSONArray(responseData);
           Log.i("bdayguests::",bdayGuests.toString());
           Log.i("before iff",bdayGuests.length()+"");

           String message="";
           if(bdayGuests.length()!=0)
           {
               Log.i("inside iff",bdayGuests.length()+"");
               for(int i=0;i<bdayGuests.length();i++)
               {
                   JSONObject guestStay=bdayGuests.getJSONObject(i);
                   JSONObject guestProfile=guestStay.getJSONObject("guest");

                   String roomNo="";
                   JSONArray rooms=guestStay.getJSONArray("rooms");
                   Log.i("rooms:: for guest no",i+"@@"+rooms);
                   for(int count=0;count<rooms.length();count++)
                   {
                       JSONObject room=(JSONObject)rooms.get(count);
                       String roomNumber=room.getString("roomNumber");
                       roomNo=roomNo+","+roomNumber;
                   }
                   if(roomNo.length()>0)
                   {
                       roomNo=roomNo.substring(1);
                   }


                   Log.i("iteration::",i+"");
                   JSONObject guestObj=(JSONObject)bdayGuests.get(i);
                   String title=(String)guestProfile.get("title");
                   Log.i(" iteration title::",title+"");

                   String preferredName=(String)guestProfile.get("preferredName");
                   Log.i(" iteration preferredName::",preferredName+"");
                   String surname=(String)guestProfile.get("surname");
                   Log.i(" iteration surname::",surname+"");
                   message=title+" "+preferredName+" "+surname;
                   showDialog(message,roomNo);
                   Log.i(" iteration dialog::",message+"");

               }
           }
       }catch(Exception ee)
        {
            Log.i("Exception in ","getting guest whose bday is today");
        }

        //End of alert button

        //Add Listener to Button

        Button findGuest = (Button) findViewById(R.id.findGuest);
        findGuest.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                onFindGuestClick();
            }
        });

        Button button = (Button) findViewById(R.id.guest_list);
        button.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDisplayTouchPointList();
            }
        });

         //find guest single click
        Button findAGuest = (Button) findViewById(R.id.findAGuest);
        findAGuest.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                onFindAGuestClick();
            }
        });





       /* Button alertButton = (Button) findViewById(R.id.alert);
        alertButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAlertClick();
            }
        });*/

        //notification test
       /* Button notify=(Button)findViewById(R.id.notify);
        notify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onNotifyClick();
            }
        });*/

        //end of test

    }



    /*public void onAlertClick()
    {
        Intent intent = new Intent(getApplicationContext(), AlertActivity.class);
        startActivity(intent);
    }*/


   /* public void onNotifyClick()
    {
        Toast.makeText(getApplicationContext(), "Notify!!!!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getApplicationContext(), NotificationCreator.class);
        startActivity(intent);
    }*/

    private void onFindAGuestClick() {
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


    public void onDisplayTouchPointList() {
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


    public void onFindGuestClick() {
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
    // });
    //}

    //logout
    public void logOut() {
        session.logOut();

        Log.i("Manager Login status after logout::", Boolean.toString(session.isLoggedIn()));
        Intent intent = new Intent(getApplicationContext(), LoginPage.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        // Do Here what ever you want do on back press;
        // super.onBackPressed();
        Log.i("Back", "pressed");

    }


    //show dialog here
    public void showDialog(String message,String roomNo)
    {
        Log.i("dialog","opened");
        final AlertDialog.Builder dialog = new AlertDialog.Builder(new ContextThemeWrapper(this, android.R.style.Theme_Holo_Light));
        AlertDialog alert = dialog.create();

        dialog.setTitle("Happy Birth Day");
        dialog.setMessage("\n Wish Happy Birth Day To " + message +"\n in Room No "+roomNo+ "\n");
        dialog.setIcon(R.drawable.bday1);



        dialog.setPositiveButton("ok",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });


        dialog.setNegativeButton("cancel",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
             }
        });
        dialog.show();
    }
}