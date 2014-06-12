package za.co.prescient.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.*;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.view.*;
import android.widget.*;
import org.json.JSONArray;
import org.json.JSONObject;
import za.co.prescient.R;
import za.co.prescient.activity.model.CoordinateSystem;
import za.co.prescient.activity.model.ItcsTagRead;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


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

    //    volatile Thread backWorker = null;
    //volatile MyThread myThread = null;
    RelativeLayout priLayout = null;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_current_setup);
        priLayout = (RelativeLayout) findViewById(R.id.priLayout);
        //setContentView(new MyView(this));

        session = new SessionManager(getApplicationContext());
        Log.i("bibhuti", "checking the session");
        session.checkSession();
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
                        //plotGuest(rnd.nextInt(400), rnd.nextInt(500));
                        mHandlerUpdateUi.post(mUpdateUpdateUi);
                        Thread.sleep(1500);
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
            plotGuest();
        }
    };


    private void plotGuest() {
        try {
            String coordinateValue = ServiceInvoker.getCoordinateValues(session.getToken(), ApplicationData.selectedTagId, backImageXMax.intValue(), backImageYmax.intValue());

            JSONArray jsonArray = new JSONArray(coordinateValue);

            final List<ItcsTagRead> itcsTagReads = new ArrayList<ItcsTagRead>();

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                Integer id = jsonObject.getInt("id");
                String guestCard = jsonObject.getString("guestCard");
                String zoneId = jsonObject.getString("zoneId");
                Double xVal = jsonObject.getDouble("xcoordRead");
                Double yVal = jsonObject.getDouble("ycoordRead");
                itcsTagReads.add(new ItcsTagRead(id, guestCard, zoneId, xVal, yVal));
            }


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

                                String guestDetail = ServiceInvoker.getGuestDetail(session.getToken(), card);
                                Log.i("guest detail", guestDetail);
                                JSONObject jsonGuest = new JSONObject(guestDetail);
                                JSONObject jsonGuestDetail = jsonGuest.getJSONObject("guest");

                                String guestTitle = jsonGuestDetail.getString("title");
                                String guestFirstName = jsonGuestDetail.getString("firstName");
                                String guestSurname = jsonGuestDetail.getString("surname");
                                String guestNationality = jsonGuestDetail.getString("nationalityId");
                                String guestImageFilePath = jsonGuestDetail.getString("guestImagePath");

                                JSONObject jsonGuestRoomDetail = jsonGuest.getJSONObject("room");
                                String guestRoomNo = jsonGuestRoomDetail.getString("roomNumber");

                                showPopup(view, xLoca.intValue(), yLoca.intValue(), guestTitle, guestFirstName, guestSurname, guestNationality, guestRoomNo, guestImageFilePath);
                            } catch (Exception ee) {
                                ee.getMessage();
                            }
                        }
                    });
                }
                //Add clickeventlistner to guestDot
            }
        } catch (Exception e) {
        }
    }


    public void showPopup(View view, Integer xloc, Integer yloc, String title, String firstName, String surName, String nationality, String roomNo, String guestImageFilePath) {
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
            TextView detailView = (TextView) popupView.findViewById(R.id.guest_detail);
            ImageView guestImageView = (ImageView) popupView.findViewById(R.id.guest_popup_image);

            //get guest image file name and extension
            String guestImageFileNameWithoutExtension = guestImageFilePath.substring(0, guestImageFilePath.lastIndexOf("."));
            String guestImageFileExtension = guestImageFilePath.substring(guestImageFilePath.lastIndexOf(".") + 1);
            Log.i("guest image file name::", guestImageFileNameWithoutExtension);
            Log.i("guest image file extension", guestImageFileExtension);

            //call a service to get guest image
            String guestImage = ServiceInvoker.getGuestImage(session.getToken(), guestImageFileNameWithoutExtension.trim(), guestImageFileExtension.trim());
            if (guestImage.length() != 0) {
                byte[] bytes = Base64.decode(guestImage, Base64.DEFAULT);
                Bitmap guestImageBitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                guestImageView.setImageBitmap(guestImageBitmap);
            }

            String userInfo;
            String userName = "Name : " + title + " " + firstName + " " + surName;
            String nation = "Nationality :" + nationality;
            String room = "Room No :" + roomNo;
            String guestImagePath = "Image Name :" + guestImageFilePath;

            userInfo = userName + "\n\n" + nation + "\n\n" + room + "\n\n" + guestImagePath;
            detailView.setText(userInfo);


            popupWindow.setFocusable(true);
            popupWindow.update();
        } catch (Exception e) {
            e.getMessage();
            Log.i("error in fetching guest image", e.getMessage());
        }
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

}//EndOf class
