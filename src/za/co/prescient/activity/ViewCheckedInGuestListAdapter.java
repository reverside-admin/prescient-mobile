package za.co.prescient.activity;


//import static za.co.prescient.activity.model.Constant.FIFTH_COLUMN;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import org.json.JSONArray;
import org.json.JSONObject;
import za.co.prescient.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import static za.co.prescient.activity.model.Constant.*;


public class ViewCheckedInGuestListAdapter extends BaseAdapter {
    public ArrayList<HashMap> list;
    Activity activity;
    Context context;
    LayoutInflater inflater1;

    public ViewCheckedInGuestListAdapter(Activity activity, ArrayList<HashMap> list) {
        super();
        this.activity = activity;
        this.list = list;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    private class ViewHolder {
        TextView colFirst;
        TextView colSecond;
        TextView colThird;
        TextView colFourth;
        TextView colFifth;
        TextView colSixth;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        System.out.println("call is done");
        final int pos = position;
        ViewHolder holder;
        LayoutInflater inflater = activity.getLayoutInflater();
        inflater1 = inflater;

        context = inflater.getContext();
        final SessionManager session = new SessionManager(context);

        if (convertView == null) {

            convertView = inflater.inflate(R.layout.checked_in_guest_list_view, null);
            holder = new ViewHolder();
            holder.colFirst = (TextView) convertView.findViewById(R.id.FirstCol);
            holder.colSecond = (TextView) convertView.findViewById(R.id.SecondCol);
            holder.colThird = (TextView) convertView.findViewById(R.id.ThirdCol);
            holder.colFourth = (TextView) convertView.findViewById(R.id.FourthCol);
            holder.colFifth = (TextView) convertView.findViewById(R.id.FifthCol);
            holder.colSixth = (TextView) convertView.findViewById(R.id.SixthCol);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        HashMap map = list.get(position);
        holder.colFirst.setText((String) map.get(FIRST_COLUMN));
        holder.colSecond.setText((String) map.get(SECOND_COLUMN));
        holder.colThird.setText((String) map.get(THIRD_COLUMN));
        holder.colFourth.setText((String) map.get(FOURTH_COLUMN));
        holder.colFifth.setText((String) map.get(FIFTH_COLUMN));
        holder.colSixth.setText((String) map.get(SIXTH_COLUMN));


        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Long selectedGuestId = (Long) list.get(pos).get(ID_COLUMN);
                    Log.i("selected guest id",selectedGuestId.toString());
                    //Log.i("selected guest and user auth token",session.getToken());
                    //here a web service is called to get the guest current location
                     String guestCurrentPosition = ServiceInvoker.getCurrentGuestPosition(session.getToken(), selectedGuestId);
                     Log.i("selected guest info",guestCurrentPosition);


                     System.out.println("asdf:::" + guestCurrentPosition.length());
                    if (guestCurrentPosition.length() == 0) {
                        String currentGuestLocationHistory = ServiceInvoker.getCurrentGuestLocationHistory(session.getToken(), selectedGuestId);
                        viewGuestLocationHistory(currentGuestLocationHistory, pos);

                    } else {
                        JSONObject jsonObject = new JSONObject(guestCurrentPosition);
                        String currentZone = jsonObject.getString("zoneId");
                        Toast.makeText(context, list.get(pos).get(FIRST_COLUMN) + " " + list.get(pos).get(SECOND_COLUMN) + " is Currently in " + currentZone, Toast.LENGTH_SHORT).show();
                     }


                } catch (Exception e) {
                    e.getMessage();
                    Log.i("error occurred in the newly added service", e.getMessage());
                }
            }
        });
        return convertView;
    }


    public void viewGuestLocationHistory(String currentGuestLocationHistory, int pos) throws Exception {
        //show popup here

        View popupView = inflater1.inflate(R.layout.guest_history_popup, null);
        final PopupWindow popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, 200);

        popupWindow.showAtLocation(popupView, Gravity.CENTER, 10, 10);
        popupView.setBackgroundColor(Color.BLUE);

        TableRow tableRow = (TableRow) popupView.findViewById(R.id.historyTableRow);
        TableLayout layout = (TableLayout) popupView.findViewById(R.id.testtable);

        //Button is added
        Button dismissButton = (Button) popupView.findViewById(R.id.cross);
        dismissButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                popupWindow.setFocusable(false);
                popupWindow.dismiss();
            }
        });

        //guest history popup table header row
        TextView col1 = new TextView(context);
        TextView col2 = new TextView(context);
        col1.setText("ZONE");
        col2.setText("TIME");
        col1.setPadding(50, 2, 2, 2);
        col2.setPadding(50, 2, 2, 2);
        tableRow = new TableRow(context);
        tableRow.addView(col1);
        tableRow.addView(col2);
        layout.addView(tableRow);
        //End of table header row

        SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        JSONArray jsonArray = new JSONArray(currentGuestLocationHistory);
        String str = "";
        for (int k = 0; k < jsonArray.length(); k++) {
            Log.i("value of k::", k + "");
            JSONObject jsonObject = (JSONObject) jsonArray.get(k);
            String zone = jsonObject.getString("zoneId");

            Date lastDetectedDateAndTime = new Date(jsonObject.getLong("tagReadDatetime"));
            String dateTime = sdf1.format(lastDetectedDateAndTime);
            str = str + zone + "   " + dateTime + "\n";

            tableRow = new TableRow(context);
            TextView zoneName = new TextView(context);
            TextView time = new TextView(context);

            zoneName.setText(zone);
            time.setText(dateTime);

            zoneName.setPadding(20, 5, 5, 5);
            time.setPadding(20, 5, 5, 5);

            tableRow.addView(zoneName);
            tableRow.addView(time);
            layout.addView(tableRow);
            Log.i("str::", str);
        }
        popupWindow.setFocusable(true);
        popupWindow.update();
    }

}