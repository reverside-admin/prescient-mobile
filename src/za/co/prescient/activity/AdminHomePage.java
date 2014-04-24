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

public class AdminHomePage extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_admin_home_page);
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
            public void onClick(View arg0) {
                Intent intent = new Intent(context, LoginPage.class);
                startActivity(intent);
            }
        });
	}
}
