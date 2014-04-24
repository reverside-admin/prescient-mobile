package za.co.prescient.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;
import za.co.prescient.R;

public class StaffHomePage extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_staff_home_page);
		addListenerOnButton();
		String userName = this.getIntent().getStringExtra("name");
		((TextView) findViewById(R.id.userName)).setText(userName);
		Log.i("name : ", userName);
		Toast.makeText(getApplicationContext(), "Welcome " + userName.toUpperCase(), Toast.LENGTH_SHORT).show();
	}



	public void addListenerOnButton() {
		final Context context = this;
		 findViewById(R.id.logout_btn).setOnClickListener(new OnClickListener() {
             @Override
             public void onClick(View view) {
                 Intent intent = new Intent(context, LoginPage.class);
                 startActivity(intent);
             }
		});

//        findViewById(R.id.find_guest).setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(context, AdminFindGuest.class);
//                startActivity(intent);
//            }
//        });
//        findViewById(R.id.display_guest).setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(context, AdminDisplayGuestList.class);
//                startActivity(intent);
//            }
//        });
//        findViewById(R.id.touchpoints).setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(context, AdminDisplayTouchPoints.class);
//                startActivity(intent);
//            }
//        });


	}
}
