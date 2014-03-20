package za.co.prescient.activity;

import org.json.JSONObject;

import za.co.prescient.R;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

// TODO : Manipulate response and load corresponding activity according to role
public class LoginPage extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login_page);
		((TextView) findViewById(R.id.prescient_name_tv)).setTextColor(Color.BLACK);
		((TextView) findViewById(R.id.guest_recognition_system_tv)).setTextColor(Color.WHITE);
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
				
				JSONObject jObj = new JSONObject(response);
				JSONObject ut = (JSONObject) jObj.get("userType");
				JSONObject us = (JSONObject) jObj.get("userStatus");

				String role = ut.getString("type");
				Log.i("Role : ", role);
				String status = us.getString("status");
				Log.i("Status : ", status);
				
				if (role.equals("admin")) {
					Intent intent = new Intent(context, AdminHomePage.class);
					intent.putExtra("name", jObj.getString("userName"));
					startActivity(intent);
				} else if (role.equals("manager")) {
					Intent intent = new Intent(context, ManagerHomePage.class);
					intent.putExtra("name", jObj.getString("userName"));
					startActivity(intent);
				} else if (role.equals("staff")) {
					Intent intent = new Intent(context, StaffHomePage.class);
					intent.putExtra("name", jObj.getString("userName"));
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
}