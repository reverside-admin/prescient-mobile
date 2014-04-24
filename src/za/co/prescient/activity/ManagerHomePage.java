package za.co.prescient.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import za.co.prescient.R;

public class ManagerHomePage extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_home_page);
        addListenerOnButton();
        String userName = this.getIntent().getStringExtra("name");
        ((TextView) findViewById(R.id.userName)).setText(userName);
        Log.i("name : ", userName);
//        String uid = this.getIntent().getStringExtra("id");
//        Log.i("id : ", uid);
        Toast.makeText(getApplicationContext(), "Welcome " + userName.toUpperCase(), Toast.LENGTH_SHORT).show();
    }

    public void addListenerOnButton() {
        final Context context = this;
        /*findViewById(R.id.logout_btn).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, LoginPage.class);
                startActivity(intent);
            }
        });*/

        /*findViewById(R.id.findGuest).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "Work In Progress", Toast.LENGTH_SHORT).show();
            }
        });*/

    }

    public void onFindGuestClick(View view) {
        Context context = getApplicationContext();

        findViewById(R.id.findGuest).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Work In Progress to find a guest", Toast.LENGTH_SHORT).show();
//                String response = ServiceInvoker.getDepartments(password);
            }
        });
    }
}
