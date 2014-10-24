package za.co.prescient.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.*;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.view.*;
import android.widget.*;
import org.json.JSONArray;
import org.json.JSONObject;
import za.co.prescient.R;
import za.co.prescient.activity.model.GuestProfileDetails;
import za.co.prescient.activity.model.ItcsTagRead;

import java.text.SimpleDateFormat;
import java.util.*;


public class ViewCurrentSetup extends Activity {

    SessionManager session;
    RelativeLayout relativeLayout;
    Bitmap bitmap, guestBit;
    Random rnd = new Random();
    ImageView backImage = null;
    boolean isRunning = true;
    static int radius = 10;
    int imgWidth, imgHeight;
    Double backImageXMax;
    Double backImageYmax;
    JSONArray allVIPIds=new JSONArray();

    int counter;

    //volatile Thread backWorker = null;
    //volatile MyThread myThread = null;
    RelativeLayout priLayout = null;
    List<GuestProfileDetails> guestProfileDetailsInContactList = new ArrayList<GuestProfileDetails>();
    List<GuestProfileDetails> allGuestsPreviouslyDetected=new ArrayList<GuestProfileDetails>();



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_current_setup);
        priLayout = (RelativeLayout) findViewById(R.id.priLayout);
        //setContentView(new MyView(this));

        session = new SessionManager(getApplicationContext());
        Log.i("bibhuti", "checking the session");
        session.checkSession();

        //chck the name of the thread Added  today 8:40 8-8-2014
        Thread th=Thread.currentThread();
        Log.i("Current Thread Name::",th.getName());


        try {
            Log.i("current user id::", "" + ApplicationData.CurrentUserId);
            Log.i("selected tag id now",ApplicationData.selectedTagId);
            //call a service to get all guest id present in all touch points.
            String allGuestId=ServiceInvoker.getAllGuestIdsInAllContactLists(session.getToken(), ApplicationData.CurrentUserId);
            Log.i("all VIP id number of vip id before",allVIPIds.length()+"");
            allVIPIds=new JSONArray(allGuestId);

            
            //call a service to get the guest list in all contact list Added today 11:27 7-8-2014
            String allGuestsInContacts = ServiceInvoker.getAllGuestsInAllContactListHavingSpecificTouchPoint(session.getToken(), ApplicationData.CurrentUserId,ApplicationData.selectedTagId);
            JSONArray guestsInAllContact = new JSONArray(allGuestsInContacts);
            Log.i("allGuestsInContacts",""+allGuestsInContacts);
            for (int count = 0; count < guestsInAllContact.length(); count++) {
                JSONObject guest = guestsInAllContact.getJSONObject(count);
                GuestProfileDetails guestProfileDetail = new GuestProfileDetails();
                guestProfileDetail.setFirstName(guest.getString("firstName"));
                guestProfileDetail.setTitle(guest.getString("title"));
                guestProfileDetail.setSurName(guest.getString("surname"));
                guestProfileDetail.setId(guest.getLong("id"));

                guestProfileDetailsInContactList.add(guestProfileDetail);
            }

            Log.i("guest in the contact:", ""+guestProfileDetailsInContactList.size());
        } catch (Exception ee) {
            Log.i("All Guests In Contacts Failed", "");
        }


        byte[] bytes = this.getIntent().getByteArrayExtra("image");

        backImageXMax = this.getIntent().getDoubleExtra("xMax", 0);
        backImageYmax = this.getIntent().getDoubleExtra("yMax", 0);

        imgWidth = priLayout.getWidth();
        imgHeight = priLayout.getHeight();
        bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        // bitmap = Bitmap.createScaledBitmap(bitmap, backImageXMax.intValue(), backImageXMax.intValue(), false);
        // bitmap = Bitmap.createScaledBitmap(bitmap, 900, 600, false);
        backImage = new ImageView(priLayout.getContext());
        priLayout.addView(backImage);
        backImage.setImageBitmap(bitmap);
        backImage.setId(9999);


        new Thread(new Runnable() {
            public void run() {

                while (isRunning) {
                    Log.d("bibhuti", "Running");
                    try {
                        //paint();
                        Log.i("bibhuti", "going to call func");

                        Log.i("$PRESCIENT","run method of the onCreate");
                        //plotGuest(rnd.nextInt(400), rnd.nextInt(500));
                        mHandlerUpdateUi.post(mUpdateUpdateUi);
                        // mHandlerUpdateUitest.post(mUpdateUpdateUitest);
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        isRunning = false;
                        Log.i("bibhuti", "Error has occured");
                        e.printStackTrace();
                    }
                }
            }
        }).start();

        guestBit = BitmapFactory.decodeResource(getResources(),
                R.drawable.guest);
        guestBit = Bitmap.createScaledBitmap(guestBit, 30, 30,
                false);
//        myThread = new MyThread();
//        myThread.start();
//        startGuestPlotWorker();



    }//EndOf method

    final Handler mHandlerUpdateUi = new Handler();

    final Runnable mUpdateUpdateUi = new Runnable() {
        public void run() {
            Log.i("$PRESCIENT Thread","1st run method out side of onCreate start");
            plotGuest();
            Log.i("$PRESCIENT Thread","1st run method out side of onCreate end");


        }
    };


    private void plotGuest() {
        //Log.i("DEMO GUEST detected after ",counter+" seconds"+"\n");

        Log.i("SAMSUNG","start of plot guest............................................");
        try {
            String coordinateValue = ServiceInvoker.getCoordinateValues(session.getToken(), ApplicationData.selectedTagId, backImageXMax.intValue(), backImageYmax.intValue());

            Log.i("selected tag id", ApplicationData.selectedTagId);

            JSONArray jsonArray = new JSONArray(coordinateValue);

            final List<ItcsTagRead> itcsTagReads = new ArrayList<ItcsTagRead>();



            Log.i("@test json array length",jsonArray.length()+"");

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                Integer id = jsonObject.getInt("id");
                String guestCard = jsonObject.getString("guestCard");
                String zoneId = jsonObject.getString("zoneId");
                Double xVal = jsonObject.getDouble("xcoordRead");
                Double yVal = jsonObject.getDouble("ycoordRead");
                itcsTagReads.add(new ItcsTagRead(id, guestCard, zoneId, xVal, yVal));
                Log.i(" SAMSUNG GuestCard detected::", guestCard+"   id::"+id);
            }


            Log.i("@test itcs tag read size",itcsTagReads.size()+"");
            //Added 12:33 PM 7-8-2014
            Log.i("SAMSUNG","Request Send");

            String allGuestsCurrentlyDetectedString=ServiceInvoker.getGuestListInTouchPoint(session.getToken(),ApplicationData.selectedTagId);


            Log.i("SAMSUNG current detected Guest",allGuestsCurrentlyDetectedString);

            JSONArray allGuestsCurrentlyDetectedList=new JSONArray(allGuestsCurrentlyDetectedString);
            List<GuestProfileDetails> allGuestsCurrentlyDetected=new ArrayList<GuestProfileDetails>();
            //List<GuestProfileDetails> allGuestsPreviouslyDetected=new ArrayList<GuestProfileDetails>();

            for(int count=0;count<allGuestsCurrentlyDetectedList.length();count++)
            {
                JSONObject obj=(JSONObject)allGuestsCurrentlyDetectedList.get(count);
                GuestProfileDetails guestProfile=new GuestProfileDetails();
                guestProfile.setId(obj.getLong("id"));
                guestProfile.setFirstName(obj.getString("firstName"));
                allGuestsCurrentlyDetected.add(guestProfile);
                Log.i("DEMO GUEST detected",obj.getString("firstName")+"\n");

            }
            Log.i("DEMO GUEST detected","...................................."+"\n\n");

            Log.i("SAMSUNG","get all guests currently detected");

            Log.i("SAMSUNG","comparing current detected guests with the contact list guests");

            //comparing current detected guests with the contact list guests
            //guestProfileDetailsInContactList vs allGuestsCurrentlyDetected

            Log.i("DEMO all guest Currently detected::",allGuestsCurrentlyDetected.toString());
            Log.i("DEMO all guest Previously detected::",allGuestsPreviouslyDetected.toString());
            Log.i("DEMO all guest Previously detected size::",""+allGuestsPreviouslyDetected.size());


            boolean flag=false;
            int n;
            int j;
            for(n=0;n<allGuestsCurrentlyDetected.size();n++)
            {
                //Log.i("DEMO all guest Previously detected size inside 1st for loop::",""+allGuestsPreviouslyDetected.size());

                flag=false;

                for(j=0;j<allGuestsPreviouslyDetected.size();j++)
                {
                    long gcdid=allGuestsCurrentlyDetected.get(n).getId();
                    long gpdid=allGuestsPreviouslyDetected.get(j).getId();

                    Log.i("DEMO gcdid",gcdid+"");
                    Log.i("DEMO gpdid",gpdid+"");
                    if(gcdid==gpdid)
                    {
                        Log.i("DEMO","gcdid and gpdid matched");
                        flag=true;
                        break;

                    }

                    /*if(allGuestsCurrentlyDetected.get(n).getId()==allGuestsPreviouslyDetected.get(j).getId())
                    {
                        //Log.i("SAMSUNG","VIP Arrival Notification"+allGuestsCurrentlyDetected.get(i).getFirstName());
                        //Toast.makeText(getApplicationContext(), allGuestsCurrentlyDetected.get(i).getFirstName()+" Arrived" , Toast.LENGTH_SHORT).show();
                        //showDialog(guestProfileDetailsInContactList.get(j).getFirstName());
                        Log.i("DEMO","matched");
                        *//*if(j==0)
                        {
                            j++;
                        }*//*
                        flag=true;
                        break;
                    }*/

                }

                if( ((j==0) || (j==allGuestsPreviouslyDetected.size()) ) && flag==false)
                {
                    Log.i("DEMO inside popup if",".");
                    Log.i("DEMO contact list size",guestProfileDetailsInContactList.size()+"");
                    for(int k=0;k<guestProfileDetailsInContactList.size();k++)
                    {
                        Log.i("DEMO id of the guest id",guestProfileDetailsInContactList.get(k).getId()+"");
                        Log.i("DEMO id of the detected guest",allGuestsCurrentlyDetected.get(n).getId()+"");

                        long gid=guestProfileDetailsInContactList.get(k).getId();
                        long cdgid=allGuestsCurrentlyDetected.get(n).getId();

                        if(gid==cdgid)
                        {
                            Log.i("DEMO BOTH ID MATCHED",".");
                            showDialog(guestProfileDetailsInContactList.get(k));
                        }

                       /* if((guestProfileDetailsInContactList.get(k).getId())== (allGuestsCurrentlyDetected.get(n).getId()))
                        {
                            Log.i("DEMO show popup for:::::::",allGuestsCurrentlyDetected.get(n).getId()+"");
                             showDialog(guestProfileDetailsInContactList.get(k));
                        }*/

                    }

                }
                Log.i("DEMO", "end of inner for after popup if");

            }

            allGuestsPreviouslyDetected.clear();
            allGuestsPreviouslyDetected.addAll(allGuestsCurrentlyDetected);


            //


            Log.i("bibhuti", "Called func");
            int childCount = priLayout.getChildCount();
            while (childCount > 1) {
                if (priLayout.getChildAt(priLayout.getChildCount() - 1).getId() != 9999) {
                    Log.i("bibhuti", "going to remove image");
                    priLayout.removeViewAt(priLayout.getChildCount() - 1);
                    Log.i("bibhuti", "removed image");
                }
                childCount--;
            }
        /* add for loop for multiple guests */
            {
                for (int i = 0; i < itcsTagReads.size(); i++) {
                    final int k = i;
                    ImageView guestDot = new ImageView(priLayout.getContext());
                    guestDot.setImageBitmap(guestBit);
                    Log.i("bibhuti", "going to add image");
                    priLayout.addView(guestDot);
                    Log.i("bibhuti", "added image");
                    guestDot.setScaleType(ImageView.ScaleType.FIT_XY);
                    RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) guestDot
                            .getLayoutParams();
                    final Double xLoca = Double.valueOf(itcsTagReads.get(i).getxCoordRead());
                    final Double yLoca = Double.valueOf(itcsTagReads.get(i).getyCoordRead());

                    lp.topMargin = xLoca.intValue();
                    lp.leftMargin = yLoca.intValue();


                    //lp.leftMargin =245;
                    //lp.topMargin =254;


                    guestDot.setLayoutParams(lp);

                    guestDot.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //get card id of the   represent the guest card


                            //on click service will be called
                            try {
                                String card = itcsTagReads.get(k).getGuestCard();

                                Log.i("card detail::", card);

                                String guestDetail = ServiceInvoker.getGuestDetail(session.getToken(), card);
                                Log.i("guest detail", guestDetail);
                                JSONObject jsonGuest = new JSONObject(guestDetail);
                                JSONObject jsonGuestDetail = jsonGuest.getJSONObject("guest");

                                //added 9:21AM 13-8-2014
                                Long guestId=jsonGuestDetail.getLong("id");

                                String guestTitle = jsonGuestDetail.getString("title");
                                String guestFirstName = jsonGuestDetail.getString("firstName");
                                String guestSurname = jsonGuestDetail.getString("surname");
                                String guestPreferredName=jsonGuestDetail.getString("preferredName");
                                String guestNationality = jsonGuestDetail.getString("nationalityId");
                                String guestImageFilePath = jsonGuestDetail.getString("guestImagePath");
                                JSONArray preferences = (JSONArray) jsonGuestDetail.get("guestPreferences");

                                //get other information to show in guest preferenece popup.(guest detail popup call the guest preference popup).
                                //get arrival date
                                Long guestArrivalDateInLong = jsonGuest.getLong("arrivalTime");
                                Date guestArrivalDateInDate = new Date(guestArrivalDateInLong);
                                // SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
                                // String guestArrivalDate = simpleDateFormat.format(guestArrivalDateInDate);

                                //get departure date
                                Long guestDepartureDateInLong = jsonGuest.getLong("departureTime");
                                Date guestDepartureDateInDate = new Date(guestDepartureDateInLong);
                                //simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
                                //String guestDepartureDate = simpleDateFormat.format(guestDepartureDateInDate);

                                //get Dob
                                Long guestDOBInLong = jsonGuestDetail.getLong("dob");
                                Date guestDOBInDate = new Date(guestDOBInLong);
                                //simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
                                //String guestDOB = simpleDateFormat.format(guestDOBInDate);

                                //get No of previous stays of the guest
                                Long previousStays = jsonGuest.getLong("noOfPreviousStays");
                                Log.i("previous stays", previousStays + "");

                                //get passport no
                                String guestPassportNo = jsonGuestDetail.getString("passportNumber");


                                //added now
                                JSONArray jsonGuestRooms = jsonGuest.getJSONArray("rooms");

                                Log.i("This guest residing in::", jsonGuestRooms.toString());
                                String guestRoomNo = "";
                                if (jsonGuestRooms.length() != 0) {
                                    for (int i = 0; i < jsonGuestRooms.length(); i++) {
                                        JSONObject obj = (JSONObject) jsonGuestRooms.get(i);
                                        guestRoomNo = guestRoomNo + " , " + obj.getString("roomNumber");

                                    }
                                    guestRoomNo = guestRoomNo.trim();
                                    guestRoomNo = guestRoomNo.substring(1, guestRoomNo.length());
                                }

                                Log.i("guest image file path before showing popup", guestImageFilePath);

                                showPopup(view, xLoca.intValue(), yLoca.intValue(),guestId, guestTitle, guestFirstName, guestSurname,guestPreferredName, guestNationality, guestRoomNo, guestImageFilePath, guestArrivalDateInDate, guestDepartureDateInDate, guestDOBInDate, guestPassportNo, preferences, previousStays);
                            } catch (Exception ee) {
                                ee.getMessage();
                                Log.i("error in::", "touch event popup");
                            }
                        }
                    });
                }
                //Add clickeventlistner to guestDot

                //increment the count so that we can put timing constraint.
               // counter=counter+4;
            }
        } catch (Exception e) {
            Log.i("error in plotting guests",".");
        }
    }

    //guest detail popup
    public void showPopup(View view, Integer xloc, Integer yloc,Long guestId, String title, String firstName, String surName,String preferredName, String nationality, String roomNo, String guestImageFilePath, Date arrivalDate, Date departureDate, Date DOB, String passportNo, JSONArray preferences, Long previousStays) {
        try {
            LayoutInflater layoutInflater = (LayoutInflater) getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);
            View popupView = layoutInflater.inflate(R.layout.guest_detail_popup, null);
            final PopupWindow popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);


            popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);
            Button btnDismiss = (Button) popupView.findViewById(R.id.dismiss);
            btnDismiss.setOnClickListener(new Button.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    popupWindow.setFocusable(false);
                    popupWindow.dismiss();
                }
            });


            Button btnMore = (Button) popupView.findViewById(R.id.more);


            TextView detailView = (TextView) popupView.findViewById(R.id.guest_detail);
            ImageView guestImageView = (ImageView) popupView.findViewById(R.id.guest_popup_image);


            //added to show image on guest bday and guest departure day on 10-9-2014

            boolean birthDayFlag = checkGuestBirthDay(DOB);
            if (birthDayFlag) {
                ImageView bdayImage = (ImageView) popupView.findViewById(R.id.bdImageView);
                bdayImage.setImageResource(R.drawable.birthday_image);
            }


            //set departure image if he/she is going to leave today
            boolean departureFlag = checkGuestDepartureDay(departureDate);
            if (departureFlag) {
                ImageView departureImage = (ImageView) popupView.findViewById(R.id.dptImageView);
                departureImage.setImageResource(R.drawable.departure_image);
            }

            //

            //get guest image file name and extension
            Bitmap guestImageBitmap = null;
            Log.i("test", "test");
            Log.i("guest image file path::", guestImageFilePath);

           //added 28-8-2014
            if(!guestImageFilePath.equals("null"))
            {
                Log.i("guest image file path inside if::", guestImageFilePath);
                String guestImageFileNameWithoutExtension = guestImageFilePath.substring(0, guestImageFilePath.lastIndexOf("."));
                String guestImageFileExtension = guestImageFilePath.substring(guestImageFilePath.lastIndexOf(".") + 1);
                Log.i("guest image file name::", guestImageFileNameWithoutExtension);
                Log.i("guest image file extension", guestImageFileExtension);

                String guestImage="";
                if(guestImageFileNameWithoutExtension.length()!=0 || guestImageFileExtension.length()!=0)
                {
                    guestImage = ServiceInvoker.getGuestImage(session.getToken(), guestImageFileNameWithoutExtension.trim(), guestImageFileExtension.trim());
                }

                if (guestImage.length() != 0) {
                    byte[] bytes = Base64.decode(guestImage, Base64.DEFAULT);
                    guestImageBitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    guestImageView.setImageBitmap(guestImageBitmap);
                }

            }

            //end of add 28-8-2014


           /* String guestImageFileNameWithoutExtension = guestImageFilePath.substring(0, guestImageFilePath.lastIndexOf("."));
            String guestImageFileExtension = guestImageFilePath.substring(guestImageFilePath.lastIndexOf(".") + 1);
            Log.i("guest image file name::", guestImageFileNameWithoutExtension);
            Log.i("guest image file extension", guestImageFileExtension);

            //call a service to get guest image
            String guestImageResult="";
            if(guestImageFileNameWithoutExtension.length()!=0 || guestImageFileExtension.length()!=0)
            {
                 guestImageResult = ServiceInvoker.getGuestImage(session.getToken(), guestImageFileNameWithoutExtension.trim(), guestImageFileExtension.trim());
            }
            final String guestImage=guestImageResult;
            if (guestImage.length() != 0) {
                byte[] bytes = Base64.decode(guestImage, Base64.DEFAULT);
                guestImageBitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                guestImageView.setImageBitmap(guestImageBitmap);
            }
*/


            //initialized the guestimage to a final temp variable so that ,that can be accessable within other inner class.
            //all variables are declared as final bcoz they need to be passed to the guest preference popup,as the popup is called from a inner class.

            String userInfo = "";
            final Bitmap guestImageForGuestPreferencePopup = guestImageBitmap;
            Log.i("setting user info", "..");
            final String userName = "Name : " + title + " " + preferredName + " " + surName;
            final String nation = "Nationality : " + nationality;
            final String room = "Room No : " + roomNo;
            final Date guestArrivalDate = arrivalDate;
            final Date guestDepartureDate = departureDate;
            final Date guestDOB = DOB;
            final String guestPassportNo = passportNo;
            final JSONArray guestPreferences = preferences;
            final Long guestPreviousStays = previousStays;
            final Long id=guestId;

            userInfo = userName + "\n\n" + nation + "\n\n" + room;
            detailView.setText(userInfo);
            Log.i("user info::", userInfo);

            btnMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    popupWindow.setFocusable(false);
                    popupWindow.dismiss();
                    //guestImageForGuestPreferencePopup is used in this inner class
                    showPreferencePopup(view, guestImageForGuestPreferencePopup,id, userName, nation, room, guestArrivalDate, guestDepartureDate, guestDOB, guestPassportNo, guestPreferences, guestPreviousStays);
                    Log.i("more button is", "clicked");
                }
            });


            popupWindow.setFocusable(true);
            popupWindow.update();
        } catch (Exception e) {
            e.getMessage();
            Log.i("error in fetching guest image", e.getMessage());
        }
    }


    //guest detail poup with guest preference options
    //Todo determine the guest type(vip=true/false from the room type),now it is directly set in the layout.

    public void showPreferencePopup(View view, Bitmap guestBit,Long id, String guestName, String nationality, String roomNo, Date arrivalDate, Date departureDate, Date dob, String passportNo, JSONArray preferences, Long guestPreviousStays) {
        try {

            //call the service here to get the preferences of a particular guest,which has to be shown in this popup.
            //here we need the guest id to call a service,so guest id need to be passed from guest detail popup.


            LayoutInflater layoutInflater = (LayoutInflater) getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);
            View popupView = layoutInflater.inflate(R.layout.guest_detail_with_preference_popup, null);

            final PopupWindow popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);


            //format all the dates coming to this method so that we can show the formatted date in the popup.
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
            String guestArrivalDateFormat = simpleDateFormat.format(arrivalDate);
            String guestDepartureDateFormat = simpleDateFormat.format(departureDate);

            SimpleDateFormat simpleDOBDateFormat = new SimpleDateFormat("dd MMMM");
            String guestDOBFormat = simpleDOBDateFormat.format(dob);


            //get the image view and set the image passed from the caller of this popup.
            if(guestBit!=null)
            {
                ImageView preferencePopupGuestImage = (ImageView) popupView.findViewById(R.id.popup_image);
                preferencePopupGuestImage.setImageBitmap(guestBit);
            }

            //set guest name
            TextView guestFullName = (TextView) popupView.findViewById(R.id.guestName);
            guestFullName.setText(guestName);

            //set nationality
            TextView guestNationality = (TextView) popupView.findViewById(R.id.guestNationality);
            guestNationality.setText(nationality);

            //set room No
            TextView guestRoomNO = (TextView) popupView.findViewById(R.id.guestRoomNo);
            guestRoomNO.setText(roomNo);

            //set DOB
            TextView guestDOB = (TextView) popupView.findViewById(R.id.guestDOB);
            guestDOB.setText("DOB : " + guestDOBFormat);

            //set vip status
            TextView vipStatus=(TextView)popupView.findViewById(R.id.vipStatus);
            int n;
            for(n=0;n<allVIPIds.length();n++)
            {
             long vipId=allVIPIds.getLong(n);
                Log.i("id...vip",vipId+"");
                Log.i("id...",id+"");
                if(id==vipId)
                {
                    vipStatus.setText("VIP: True");
                    break;
                }
            }
            if(n==allVIPIds.length())
            {
                vipStatus.setText("VIP: False");
            }


            //set arrival date
            TextView guestArrivalDate = (TextView) popupView.findViewById(R.id.guestArrivalDate);
            guestArrivalDate.setText("Arrival Date : " + guestArrivalDateFormat);

            //set departure date
            TextView guestDepartureDate = (TextView) popupView.findViewById(R.id.guestDepartureDate);
            guestDepartureDate.setText("Departure Date : " + guestDepartureDateFormat);

            //set passport no
            TextView guestPassportNo = (TextView) popupView.findViewById(R.id.guestPassportNo);
            guestPassportNo.setText("Passport No : " + passportNo);


            //set guests previous stays  guestPreviousStays
            TextView guestStays = (TextView) popupView.findViewById(R.id.guestPreviousStays);
            guestStays.setText("No Of Stays : " + guestPreviousStays);


            //set birthday image for guest if he/she has a  birth day today
            boolean birthDayFlag = checkGuestBirthDay(dob);
            if (birthDayFlag) {
                ImageView bdayImageView = (ImageView) popupView.findViewById(R.id.guestBirthDayImage);
                bdayImageView.setImageResource(R.drawable.birthday_image);
            }


            //set departure image if he/she is going to leave today
            boolean departureFlag = checkGuestDepartureDay(departureDate);
            if (departureFlag) {
                ImageView departureImageView = (ImageView) popupView.findViewById(R.id.departureDateImage);
                departureImageView.setImageResource(R.drawable.departure_image);
            }

            //dynamically extract the guest preferences and dynamically add them to popup.
            //we can't decide the no.of text views in the xml file bcoz no of guest preferences set ,may be vary per guest.

            //title of the guest preference
            TextView guestPreferenceTitle = (TextView) popupView.findViewById(R.id.guest_preference_title);
            guestPreferenceTitle.setText("Guest Preference");


            String preferenceMessage = "";
            for (int i = 0; i < preferences.length(); i++) {

                JSONObject preference = preferences.getJSONObject(i);
                String preferenceDescription = preference.getString("description");
                JSONObject type = preference.getJSONObject("guestPreferenceType");
                String preferenceType = type.getString("name");
                preferenceMessage = preferenceMessage + preferenceType + ":\n  " + preferenceDescription + "\n\n";
            }

            TextView guestPreferenceValue = (TextView) popupView.findViewById(R.id.guest_preference_val);
            guestPreferenceValue.setText(preferenceMessage);


            Log.i("preferences array length", preferences.length() + "");


            popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);
            Button btnClosePreference = (Button) popupView.findViewById(R.id.close_preference);
            btnClosePreference.setOnClickListener(new Button.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    popupWindow.setFocusable(false);
                    popupWindow.dismiss();
                }
            });

            popupWindow.setFocusable(true);
            popupWindow.update();
        } catch (Exception ee) {
            ee.getMessage();
            Log.i("error in", "preference popup");
        }
    }
    //end of guest preference popup


    //Return true if guest has a birth day today
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

    //Return true if the guest's departure date is today
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


    public void logOut() {
        session.logOut();

        Log.i("Manager Login status after logout::", Boolean.toString(session.isLoggedIn()));
        Intent intent = new Intent(getApplicationContext(), LoginPage.class);
        startActivity(intent);
    }


    @Override
    public void onBackPressed() {
        isRunning = false;
        super.onBackPressed();
        Log.i("Back", "pressed");
    }

//Added to show the popup 10:08 8-8-2014

    public void showDialog(GuestProfileDetails guestProfileDetails)
    {
        Log.i("dialog","opened");
        final AlertDialog.Builder dialog = new AlertDialog.Builder(new ContextThemeWrapper(this, android.R.style.Theme_Holo_Light));
        AlertDialog alert = dialog.create();
        String message=guestProfileDetails.getTitle()+" "+guestProfileDetails.getFirstName()+" "+guestProfileDetails.getSurName();

        dialog.setTitle("VIP Alert");
        dialog.setMessage("\n   " + message + "  Just Arrived In " + ApplicationData.selectedTagId + ".\n");
        dialog.setIcon(R.drawable.vip_image);



        dialog.setPositiveButton("ok",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });


        dialog.setNegativeButton("cancel",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        dialog.show();
    }

}//EndOf class
