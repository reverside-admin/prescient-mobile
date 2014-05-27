package za.co.prescient.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;
import za.co.prescient.R;

public class AdminHomePage extends Activity {
    SessionManager session;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_admin_home_page);

        TextView tv=(TextView)findViewById(R.id.prescientName2);
        tv.setText(Html.fromHtml(getString(R.string.prescient_name)));

		String userName = this.getIntent().getStringExtra("name");
		((TextView) findViewById(R.id.userName)).setText(userName);
		Log.i("name : ", userName);
		Toast.makeText(getApplicationContext(), "Welcome " + userName.toUpperCase(), Toast.LENGTH_SHORT).show();

        //check session Here
        session=new SessionManager(getApplicationContext());
        Log.i("check","checking the session");
        session.checkSession();

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
