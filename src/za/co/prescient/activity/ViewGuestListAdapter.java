package za.co.prescient.activity;


//import static za.co.prescient.activity.model.Constant.FIFTH_COLUMN;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
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
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import static za.co.prescient.activity.model.Constant.*;

public class ViewGuestListAdapter extends BaseAdapter {
    public ArrayList<HashMap> list;
    Activity activity;
    Context context;
    LayoutInflater inflater1;

    public ViewGuestListAdapter() {
    }

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

    public void addItem(HashMap map) {
        list.add(map);
    }

    private class ViewHolder {
        TextView txtFirst;
        TextView txtSecond;
        TextView txtThird;
        TextView txtFourth;
        //TextView txtFifth;
        //TextView txtSixth;
        //TextView txtSeventh;
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
            //holder.txtFifth = (TextView) convertView.findViewById(R.id.FifthText);
            //holder.txtSixth = (TextView) convertView.findViewById(R.id.SixthText);
            //holder.txtSeventh = (TextView) convertView.findViewById(R.id.SeventhText);


            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        HashMap map = list.get(position);
        holder.txtFirst.setText((String) map.get(FIRST_COLUMN));
        holder.txtSecond.setText((String) map.get(SECOND_COLUMN));
        holder.txtThird.setText((String) map.get(THIRD_COLUMN));
        holder.txtFourth.setText((String) map.get(FOURTH_COLUMN));

        //holder.txtFifth.setText((String) map.get(FIFTH_COLUMN));

        //holder.txtSixth.setText((String) map.get(SIXTH_COLUMN));

        //holder.txtSeventh.setText((String) map.get(SEVENTH_COLUMN));



        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Log.i("hello", "ID is::" + list.get(pos).get(ID_COLUMN));
                    Long guestId = (Long) list.get(pos).get(ID_COLUMN);
                    String response = ServiceInvoker.getGuestDetailByGuestId(session.getToken(), guestId);
                    // Toast.makeText(context, "Guest Detail:: " + response, Toast.LENGTH_SHORT).show();
                    //Log.i("guest detail::", response);

                    //parse the response to view guest detail
                    Log.i("hello responce is",response);

                    JSONObject jsonGuest = new JSONObject(response);
                    JSONObject jsonGuestDetail = jsonGuest.getJSONObject("guest");

                    String guestTitle = jsonGuestDetail.getString("title");
                    String guestFirstName = jsonGuestDetail.getString("firstName");
                    String guestSurname = jsonGuestDetail.getString("surname");
                    String guestPreferredName=jsonGuestDetail.getString("preferredName");
                    String guestNationality = jsonGuestDetail.getString("nationalityId");
                    String gender = jsonGuestDetail.getString("gender");
                    String guestImageFilePath = jsonGuestDetail.getString("guestImagePath");
                    Long DOBInLong = jsonGuestDetail.getLong("dob");
                    Date dob = new Date(DOBInLong);

                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MMMM");
                    SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                    String dobText = sdf.format(dob);

                    Log.i("DateText", "" + dob);

                    Long arrivalTime = jsonGuest.getLong("arrivalTime");
                    Date arrivalDate = new Date(arrivalTime);
                    String arrivalDateText = sdf1.format(arrivalDate);


                    Long departureTime = jsonGuest.getLong("departureTime");
                    Date departureDate = new Date(departureTime);
                    String departureDateText = sdf1.format(departureDate);

                    //JSONObject jsonGuestRoomDetail = jsonGuest.getJSONObject("room");
                    //String guestRoomNo = jsonGuestRoomDetail.getString("roomNumber");

                    String guestRoomNo ="";
                    JSONArray jsonGuestRooms=jsonGuest.getJSONArray("rooms");
                    Log.i("jsonGuestRooms::",jsonGuestRooms.toString());
                    if(jsonGuestRooms.length()!=0)
                    {

                    for (int i=0; i<jsonGuestRooms.length(); i++){
                        JSONObject obj=(JSONObject) jsonGuestRooms.get(i);
                        guestRoomNo=guestRoomNo+" , "+obj.getString("roomNumber");

                    }

                    guestRoomNo=guestRoomNo.trim();
                    guestRoomNo=guestRoomNo.substring(1, guestRoomNo.length());
                    }

                    //added 28-8-2014
                    Bitmap guestImageBitmap = null;
                    if(!guestImageFilePath.equals("null"))
                    {
                        String guestImageFileNameWithoutExtension = guestImageFilePath.substring(0, guestImageFilePath.lastIndexOf("."));
                        String guestImageFileExtension = guestImageFilePath.substring(guestImageFilePath.lastIndexOf(".") + 1);


                        String guestImage="";
                        if(guestImageFileNameWithoutExtension.length()!=0 || guestImageFileExtension.length()!=0)
                        {
                            guestImage = ServiceInvoker.getGuestImage(session.getToken(), guestImageFileNameWithoutExtension.trim(), guestImageFileExtension.trim());
                        }

                        if (guestImage.length() != 0) {
                            byte[] bytes = Base64.decode(guestImage, Base64.DEFAULT);
                            guestImageBitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        }
                    }
                    //end of added 28-8-2014

                    //If guest image is not set then popup will not appear.

                    //get guest image and pass the image to the popup.
                    /*Bitmap guestImageBitmap = null;
                    if(guestImageFilePath!=null)
                    {
                    String guestImageFileNameWithoutExtension = guestImageFilePath.substring(0, guestImageFilePath.lastIndexOf("."));
                    String guestImageFileExtension = guestImageFilePath.substring(guestImageFilePath.lastIndexOf(".") + 1);

                   // Bitmap guestImageBitmap = null;

                    String guestImage = ServiceInvoker.getGuestImage(session.getToken(), guestImageFileNameWithoutExtension.trim(), guestImageFileExtension.trim());
                    if (guestImage.length() != 0) {
                        byte[] bytes = Base64.decode(guestImage, Base64.DEFAULT);
                        guestImageBitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    }
                    }*/



                    String guestInfo = "\n  Name: " + guestTitle + " " + guestPreferredName + " " + guestSurname + "\n  Nationality: " + guestNationality + "\n  Gender: " + gender + "\n  DOB: " + dobText + "\n  Room Number: " + guestRoomNo + "\n  Arrival Date: " + arrivalDateText + "\n  Departure Date: " + departureDateText + "\n\n";
                    showPopup(view, guestInfo, dob, departureDate, guestImageBitmap);
                } catch (Exception e) {
                    Log.i("mistake", e.getMessage());
                    e.getMessage();
                }
            }
        });
        return convertView;
    }


    public void showPopup(View view, String guestInfo, Date dob, Date departureDate, Bitmap guestImage) {

        View popupView = inflater1.inflate(R.layout.guest_popup, null);
        final PopupWindow popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);
        Button btnDismiss = (Button) popupView.findViewById(R.id.close);
        TextView detailView = (TextView) popupView.findViewById(R.id.guest_detail_info);

        //check for birth day
        ImageView bdayImageView = (ImageView) popupView.findViewById(R.id.birthdayImage);
        Boolean birthDayFlag = checkGuestBirthDay(dob);
        Log.i("birth Day", birthDayFlag.toString());

        ImageView departureImageView = (ImageView) popupView.findViewById(R.id.departureImage);
        Boolean departureFlag = checkGuestDepartureDay(departureDate);
        Log.i("departure Day", departureFlag.toString());

        if (birthDayFlag) {
            bdayImageView.setImageResource(R.drawable.birthday_image);
        }
        if (departureFlag) {
            departureImageView.setImageResource(R.drawable.departure_image);
        }

        //set the guest image .
        ImageView guestImageView = (ImageView) popupView.findViewById(R.id.guest_photo);
         if (guestImage != null) {
            guestImageView.setImageBitmap(guestImage);
        }



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

    public boolean checkGuestBirthDay(Date dob) {
        //Guest Birth day
        Calendar cal = Calendar.getInstance();
        cal.setTime(dob);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        Log.i("month::", "" + month);
        Log.i("day::", "" + day);


        //Todays Date
        Date currentDate = new Date();
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(currentDate);
        int thisMonth = cal1.get(Calendar.MONTH);
        int thisDay = cal1.get(Calendar.DAY_OF_MONTH);

        Log.i("This month::", "" + thisMonth + "  " + currentDate.toString());
        Log.i("This day::", "" + thisDay);


        if ((month == thisMonth) && (day == thisDay)) {
            return true;
        } else {
            return false;
        }
    }


    public boolean checkGuestDepartureDay(Date departureDate) {
        //Guest Departure day

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String departureDateString = sdf.format(departureDate);

        String currentDateString = sdf.format(new Date());

        if (departureDateString.equals(currentDateString))
            return true;
        else
            return false;

    }


}