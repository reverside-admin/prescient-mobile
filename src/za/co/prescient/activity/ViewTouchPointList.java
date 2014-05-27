package za.co.prescient.activity;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;
import org.json.JSONArray;
import za.co.prescient.R;
import za.co.prescient.activity.model.TouchPoint;

/**
 * Created by Bibhuti on 2014/05/23.
 */
public class ViewTouchPointList extends ListActivity {

    SessionManager session;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //check if session is exist or not
        session = new SessionManager(getApplicationContext());
        Log.i("check", "checking the session");
        session.checkSession();

        setListAdapter(new ListViewAdapter(this, R.layout.activity_listviewactivity, ApplicationData.touchPointList));
    }


    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        TouchPoint touchPoint = (TouchPoint) getListAdapter().getItem(position);
        ApplicationData.selectedTagId = touchPoint.getName();

        Toast.makeText(getApplicationContext(),  ApplicationData.selectedTagId,
                Toast.LENGTH_SHORT).show();

        //make a web service call to get the guest list in this touchpoint
        try {
            String guestList = ServiceInvoker.getGuestListInTouchPoint(session.getToken(), ApplicationData.selectedTagId);
            JSONArray jsonArray = new JSONArray(guestList);
            Log.i("guestList array length::", "" + jsonArray.length());

            Intent intent = new Intent(getApplicationContext(), ViewGuestList.class);
            Bundle b=new Bundle();
            b.putString("guests",jsonArray.toString());
            intent.putExtras(b);
            startActivity(intent);


        } catch (Exception ee) {
            ee.getMessage();
        }
    }
}