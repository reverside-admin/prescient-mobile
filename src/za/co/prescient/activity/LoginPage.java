package za.co.prescient.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONObject;
import za.co.prescient.R;
import za.co.prescient.activity.model.Guest;

// TODO : Manipulate response and load corresponding activity according to role
public class LoginPage extends Activity {

    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        ((TextView) findViewById(R.id.guest_recognition_system_tv1)).setTextColor(Color.WHITE);

        TextView tv=(TextView)findViewById(R.id.prescient_name_tv);
        tv.setText(Html.fromHtml(getString(R.string.prescient_name)));

        session=new SessionManager(getApplicationContext());

    }

    public void onLoginClick(View view) {
        try {
            Context context = getApplicationContext();

            String userName = ((EditText) findViewById(R.id.enter_userid))
                    .getText().toString();
            Log.i("UserName : ", userName);

            String password = ((EditText) findViewById(R.id.enter_password))
                    .getText().toString();
            Log.i("Password : ", password);

            if (userName.equals("") || password.equals("")) {
                Toast.makeText(getApplicationContext(),
                        "Please Enter Login Credentials", Toast.LENGTH_SHORT)
                        .show();
            } else {
                String response = ServiceInvoker.getUser(userName, password);
//				Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                Log.i("Response from Servece :", response);

                JSONObject jsonObj = new JSONObject(response);
                JSONObject hotel = (JSONObject) jsonObj.get("hotel");
                Long hotelId = hotel.getLong("id");
                Long userId=jsonObj.getLong("id");

                Guest guest = new Guest(hotelId);

                //create a session and store the user name in the session
                session.createSession(userName,password);

                ApplicationData.guest = guest;
                ApplicationData.currentUserHotelId=hotelId;

                //added 10:38AM 7-8-2014
                ApplicationData.CurrentUserId=userId;



                JSONObject jObj = new JSONObject(response);
                JSONObject ut = (JSONObject) jObj.get("userType");
                JSONObject us = (JSONObject) jObj.get("userStatus");

                String role = ut.getString("value");
                Log.i("Role : ", role);
                String status = us.getString("value");
                Log.i("Status : ", status);

                if(password.equals("password"))
                {
                    Intent intent=new Intent(context,ChangePassword.class);
                    intent.putExtra("name",userName);
                    intent.putExtra("role",role);
                    startActivity(intent);
                }

                else if (role.equals("ROLE_ADMIN")) {
                    Intent intent = new Intent(context, AdminHomePage.class);
                    intent.putExtra("name", jObj.getString("userName"));
//                    intent.putExtra("id", jObj.getString("id"));
                    startActivity(intent);
                } else if (role.equals("ROLE_MANAGER")) {
                    Intent intent = new Intent(context, ManagerHomePage.class);
                    intent.putExtra("name", jObj.getString("userName"));
//                    intent.putExtra("id", jObj.getString("id"));
                    startActivity(intent);
                } else if (role.equals("ROLE_STAFF")) {
                    Intent intent = new Intent(context, StaffHomePage.class);
                    intent.putExtra("name", jObj.getString("userName"));
                    //intent.putExtra("id", jObj.getString("id"));
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(context, LoginPage.class);
                    startActivity(intent);
                    Toast.makeText(getApplicationContext(), "User Not Found",
                            Toast.LENGTH_SHORT).show();
                }
            }

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.getMessage(),
                    Toast.LENGTH_LONG).show();
        }
    }


    @Override
    public void onBackPressed() {
        // Do Here what ever you want do on back press;
        // if you want to make the back button behave normally call:: super.onBackPressed();
        Log.i("Back", "pressed");

    }


}
