package za.co.prescient.activity;

import android.os.StrictMode;
import android.util.Base64;
import android.util.Log;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;

import java.io.IOException;
import java.util.List;

public class ServiceInvoker {

    private static String authenticationToken;

    private static final String serviceLocation = "http://192.168.1.10:8080";


    public static String getUser(String userName, String password) throws ClientProtocolException, IOException {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);
        authenticationToken = Base64.encodeToString((userName + ":" + password).getBytes(), Base64.NO_WRAP);
        HttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(serviceLocation + "/login/" + userName + "/" + password);
        httpGet.setHeader("Authorization", "Basic " + authenticationToken);
        ResponseHandler<String> responseHandler = new BasicResponseHandler();
        String response = httpClient.execute(httpGet, responseHandler);
        return response;
    }

    public static void changePassword(String token, String confirmPassword) throws ClientProtocolException, IOException {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);
        // authenticationToken = Base64.encodeToString((userName+":"+password).getBytes(), Base64.NO_WRAP);
        HttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(serviceLocation + "/api/login/reset/password/" + confirmPassword);
        httpGet.setHeader("Authorization", "Basic " + token);
        ResponseHandler<String> responseHandler = new BasicResponseHandler();
        httpClient.execute(httpGet, responseHandler);

    }


    public static String getAllAssignedTouchPoint(String authenticationToken) throws ClientProtocolException, IOException {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);
        HttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(serviceLocation + "/api/login/touchpoints");
        Log.i("TOKEN", authenticationToken);
        httpGet.setHeader("Authorization", "Basic " + authenticationToken);
        ResponseHandler<String> responseHandler = new BasicResponseHandler();
        String response = httpClient.execute(httpGet, responseHandler);
        return response;
    }


    public static String getCurrentTouchPointSetup(String authenticationToken, Long touchPointId) throws ClientProtocolException, IOException {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);
        HttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(serviceLocation + "/api/touchpoint/" + touchPointId + "/setups/current");
        httpGet.setHeader("Authorization", "Basic " + authenticationToken);
        ResponseHandler<String> responseHandler = new BasicResponseHandler();
        String response = httpClient.execute(httpGet, responseHandler);
        return response;
    }


    public static String getCurrentSetupImage(String authenticationToken, Long currentSetupId) throws ClientProtocolException, IOException {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);
        HttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(serviceLocation + "/api/touchpoint/setup/" + currentSetupId);
        httpGet.setHeader("Authorization", "Basic " + authenticationToken);
        ResponseHandler<String> responseHandler = new BasicResponseHandler();
        String response = httpClient.execute(httpGet, responseHandler);
        return response;

    }

    //Find all guest in the selected touchpoint
    public static String getGuestListInTouchPoint(String authenticationToken, String tagId) throws ClientProtocolException, IOException {

        Log.i("SAMSUNG","in service invoker");

        Log.i("get guest list in touchpoint service is ", "called");
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);
        HttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(serviceLocation + "/api/touchpoints/" + tagId + "/guestCards");
        httpGet.setHeader("Authorization", "Basic " + authenticationToken);
        ResponseHandler<String> responseHandler = new BasicResponseHandler();
        String response = httpClient.execute(httpGet, responseHandler);
        Log.i("SAMSUNG","after service invoker");

        return response;

    }


    //for guest  layout popup information,give the card  and find the guest detail holding that card.
    public static String getGuestDetail(String authenticationToken, String tagId) throws ClientProtocolException, IOException {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);
        HttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(serviceLocation + "/api/tags/" + tagId + "/guest");
        httpGet.setHeader("Authorization", "Basic " + authenticationToken);
        ResponseHandler<String> responseHandler = new BasicResponseHandler();
        String response = httpClient.execute(httpGet, responseHandler);
        return response;

    }


    //get guest image for layout popup
    public static String getGuestImage(String authenticationToken, String imageName,String imageExtension) throws ClientProtocolException, IOException {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);
        HttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(serviceLocation + "/api/guest/image/" + imageName + "/" + imageExtension);
        httpGet.setHeader("Authorization", "Basic " + authenticationToken);
        ResponseHandler<String> responseHandler = new BasicResponseHandler();
        String response = httpClient.execute(httpGet, responseHandler);
        return response;

    }


    public static String getCoordinateValues(String authenticationToken, String zoneId, Integer xMax, Integer yMax) throws ClientProtocolException, IOException {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);
        HttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(serviceLocation + "/api/touchpoints/" + zoneId + "/guestCards/latest/" + xMax + "/" + yMax);
        httpGet.setHeader("Authorization", "Basic " + authenticationToken);
        ResponseHandler<String> responseHandler = new BasicResponseHandler();
        String response = httpClient.execute(httpGet, responseHandler);
        return response;
    }


    public static String getGuestDetailByGuestId(String authenticationToken, Long guestId) throws ClientProtocolException, IOException {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);
        HttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(serviceLocation + "/api/guest/" + guestId);
        httpGet.setHeader("Authorization", "Basic " + authenticationToken);
        ResponseHandler<String> responseHandler = new BasicResponseHandler();
        String response = httpClient.execute(httpGet, responseHandler);
        return response;
    }

    public static String getCheckedInGuests(String authenticationToken, Long hotelId) throws ClientProtocolException, IOException {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);
        HttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(serviceLocation + "/api/hotels/" + hotelId + "/guests/checkedIn");
        httpGet.setHeader("Authorization", "Basic " + authenticationToken);
        ResponseHandler<String> responseHandler = new BasicResponseHandler();
        String response = httpClient.execute(httpGet, responseHandler);
        return response;

    }


    public static String getCurrentGuestPosition(String authenticationToken, Long guestId) throws ClientProtocolException, IOException {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);
        HttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(serviceLocation + "/api/guests/" +guestId + "/locations");
        httpGet.setHeader("Authorization", "Basic " + authenticationToken);
        ResponseHandler<String> responseHandler = new BasicResponseHandler();
        String response = httpClient.execute(httpGet, responseHandler);
        Log.i("get guest current position@Bibhuti",response);
        return response;
    }

    public static String getCurrentGuestLocationHistory(String authenticationToken, Long guestId) throws ClientProtocolException, IOException {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);
        HttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(serviceLocation + "/api/guests/" + guestId + "/location/history");
        httpGet.setHeader("Authorization", "Basic " + authenticationToken);
        ResponseHandler<String> responseHandler = new BasicResponseHandler();
        String response = httpClient.execute(httpGet, responseHandler);
        Log.i("get guest location history",response);
        return response;
    }

    //get all guest whise bday is today
    public static String getGuestsWhoseBdayIsToday(String authenticationToken, Long hotelId) throws ClientProtocolException, IOException {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);
        HttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(serviceLocation + "/api/hotel/" + hotelId + "/guest/birthday/list");
        httpGet.setHeader("Authorization", "Basic " + authenticationToken);
        ResponseHandler<String> responseHandler = new BasicResponseHandler();
        String response = httpClient.execute(httpGet, responseHandler);
        Log.i("get all guests whose birth day is today",response);
        return response;
    }


    //added on 10:26am 8-7-2014

    //get all guests present in all contact (having special touch point access)lists of the login manager
    public static String getAllGuestsInAllContactListHavingSpecificTouchPoint(String authenticationToken, Long ownerId,String tagId) throws ClientProtocolException, IOException {

        Log.i("get contact list service is invoked now","check");

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);
        HttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(serviceLocation + "/user/"+ownerId+"/contacts/"+tagId+"/guests");
        httpGet.setHeader("Authorization", "Basic " + authenticationToken);
        ResponseHandler<String> responseHandler = new BasicResponseHandler();
        String response = httpClient.execute(httpGet, responseHandler);
        Log.i("get all guests in all contact list of this owner",response);
        return response;
    }

    //get all guest ids present in all contact lists of the login manager
    public static String getAllGuestIdsInAllContactLists(String authenticationToken, Long ownerId) throws ClientProtocolException, IOException {

        Log.i("get contact list service is invoked now","check");

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);
        HttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(serviceLocation + "/api/manager/"+ownerId+"/contacts/guests/ids");
        httpGet.setHeader("Authorization", "Basic " + authenticationToken);
        ResponseHandler<String> responseHandler = new BasicResponseHandler();
        String response = httpClient.execute(httpGet, responseHandler);
        Log.i("get all guests in all contact list of this owner",response);
        return response;
    }





}
