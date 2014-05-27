package za.co.prescient.activity;


//import static za.co.prescient.activity.model.Constant.FIFTH_COLUMN;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import org.json.JSONObject;
import za.co.prescient.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import static za.co.prescient.activity.model.Constant.*;

public class ViewGuestListAdapter extends BaseAdapter {
    public ArrayList<HashMap> list;
    Activity activity;
    Context context;
    LayoutInflater inflater1;

    public ViewGuestListAdapter(Activity activity, ArrayList<HashMap> list) {
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
        TextView txtFirst;
        TextView txtSecond;
        TextView txtThird;
        TextView txtFourth;
        // TextView txtFifth;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        final int pos = position;
        // TODO Auto-generated method stub
        ViewHolder holder;
        LayoutInflater inflater = activity.getLayoutInflater();
        inflater1 = inflater;

        context = inflater.getContext();
        final SessionManager session = new SessionManager(context);


        if (convertView == null) {
            convertView = inflater.inflate(R.layout.guest_list_view, null);
            holder = new ViewHolder();
            holder.txtFirst = (TextView) convertView.findViewById(R.id.FirstText);
            holder.txtSecond = (TextView) convertView.findViewById(R.id.SecondText);
            holder.txtThird = (TextView) convertView.findViewById(R.id.ThirdText);
            holder.txtFourth = (TextView) convertView.findViewById(R.id.FourthText);
            // holder.txtFifth = (TextView) convertView.findViewById(R.id.FifthText);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        HashMap map = list.get(position);
        holder.txtFirst.setText((String) map.get(FIRST_COLUMN));
        holder.txtSecond.setText((String) map.get(SECOND_COLUMN));
        holder.txtThird.setText((String) map.get(THIRD_COLUMN));
        holder.txtFourth.setText((String) map.get(FOURTH_COLUMN));
        // holder.txtFifth.setText((String)map.get(FIFTH_COLUMN));


        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Log.i("hello", "ID is::" + list.get(pos).get(FIFTH_COLUMN));
                    Long guestId = (Long) list.get(pos).get(FIFTH_COLUMN);
                    String response = ServiceInvoker.getGuestDetailByGuestId(session.getToken(), guestId);
                    // Toast.makeText(context, "Guest Detail:: " + response, Toast.LENGTH_SHORT).show();
                    //Log.i("guest detail::", response);

                    //parse the response to view guest detail

                    JSONObject jsonGuest = new JSONObject(response);
                    JSONObject jsonGuestDetail = jsonGuest.getJSONObject("guest");

                    String guestTitle = jsonGuestDetail.getString("title");
                    String guestFirstName = jsonGuestDetail.getString("firstName");
                    String guestSurname = jsonGuestDetail.getString("surname");
                    String guestNationality = jsonGuestDetail.getString("nationalityId");
                    String gender = jsonGuestDetail.getString("gender");
                    Long DOBInLong = jsonGuestDetail.getLong("dob");
                    Date dob = new Date(DOBInLong);

                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                    SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                    String dobText = sdf.format(dob);

                    Log.i("DateText", "" + dob);

                    Long arrivalTime = jsonGuest.getLong("arrivalTime");
                    Date arrivalDate = new Date(arrivalTime);
                    String arrivalDateText = sdf1.format(arrivalDate);


                    Long departureTime = jsonGuest.getLong("departureTime");
                    Date departureDate = new Date(departureTime);
                    String departureDateText = sdf1.format(departureDate);

                    JSONObject jsonGuestRoomDetail = jsonGuest.getJSONObject("room");
                    String guestRoomNo = jsonGuestRoomDetail.getString("roomNumber");



                   /* Toast.makeText(context, "Guest Detail:: " + guestTitle +"\n"
                            +guestFirstName+"\n"+guestSurname+"\n"+guestNationality+"\n"+gender+"\n"+dob+"\n"+guestRoomNo+"\n"+
                            arrivalDate+"\n"+departureDate, Toast.LENGTH_SHORT).show();
*/


                    String guestInfo = "\n\n Name: " + guestTitle + " " + guestFirstName + " " + guestSurname + "\n Nationality: " + guestNationality + "\n Gender: " + gender + "\n DOB: " + dobText + "\nRoom Number: " + guestRoomNo + "\nArrival Date: " + arrivalDateText + "\n Departure Date: " + departureDateText;
                    showPopup(view, guestInfo,dobText);
                } catch (Exception e) {
                    e.getMessage();
                }


            }
        });

        return convertView;
    }

    public void showPopup(View view, String guestInfo,String dateText) {
        //call a service to get user detail
        //Toast.makeText(getApplicationContext(),"popup can be shown" , Toast.LENGTH_SHORT).show();
        // LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater1.inflate(R.layout.guest_popup, null);
        final PopupWindow popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);
        Button btnDismiss = (Button) popupView.findViewById(R.id.close);
        TextView detailView = (TextView) popupView.findViewById(R.id.guest_detail_info);



        detailView.setText(guestInfo);
        btnDismiss.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                popupWindow.setFocusable(false);
                popupWindow.dismiss();
            }
        });


        // popupWindow.showAsDropDown(view,0,0);
        popupWindow.setFocusable(true);
        popupWindow.update();

    }


}