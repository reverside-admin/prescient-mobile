package za.co.prescient.activity;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Base64InputStream;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import org.json.JSONObject;
import za.co.prescient.R;
import za.co.prescient.activity.model.TouchPoint;
//import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;



import java.util.ArrayList;
import java.util.List;


public class ListViewActivity extends ListActivity {

    SessionManager session;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //check for session
        session = new SessionManager(getApplicationContext());
        Log.i("check", "checking the session");
        session.checkSession();

        //List Data is passed to the ListViewActivity
        // List<String> touchPoints = this.getIntent().getStringArrayListExtra("touchPoints");
        //Log.i("touchPoints received : ", touchPoints.toString());

        // Setting the Adapter which will handle the list
        setListAdapter(new ListViewAdapter(this, R.layout.activity_listviewactivity, ApplicationData.touchPointList));

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

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {

        TouchPoint touchPoint = (TouchPoint) getListAdapter().getItem(position);

        ApplicationData.selectedTagId=touchPoint.getName();
        //Toast.makeText(this, touchPoint.getName() + "::" + touchPoint.getId(), Toast.LENGTH_SHORT).show();

        Toast.makeText(this, "welcome to "+touchPoint.getName(), Toast.LENGTH_SHORT).show();


        //call webservice to get the the current setup details.
        try {
            String currentSetup = ServiceInvoker.getCurrentTouchPointSetup(session.getToken(), touchPoint.getId());
           // Toast.makeText(this, currentSetup, Toast.LENGTH_SHORT).show();
            Log.i("current setup::", currentSetup);

            JSONObject jsonObject = new JSONObject(currentSetup);
            Long currentSetupId = jsonObject.getLong("id");
            Double xMax=jsonObject.getDouble("lengthX");
            Double yMax=jsonObject.getDouble("lengthY");

            String imageResponse = ServiceInvoker.getCurrentSetupImage(session.getToken(), currentSetupId);
            Log.i("IMAGE::", imageResponse);

            byte[] bytes = Base64.decode(imageResponse,Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

            Intent intent = new Intent(getApplicationContext(), ViewCurrentSetup.class);
            intent.putExtra("image",bytes);
            intent.putExtra("xMax",xMax);
            intent.putExtra("yMax",yMax);

            startActivity(intent);


        } catch (Exception ee) {
            ee.getMessage();
        }
    }

    public void logOut()
    {
        session.logOut();

        Log.i("Manager Login status after logout::", Boolean.toString(session.isLoggedIn()));
        Intent intent = new Intent(getApplicationContext(), LoginPage.class);
        startActivity(intent);
    }


}