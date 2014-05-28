package za.co.prescient.activity;

import android.os.StrictMode;
import android.util.Base64;
import android.util.Log;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;

public class ServiceInvoker {

    private static String authenticationToken;

    private static final String serviceLocation = "http://192.168.1.102:8080";


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
    public static String getGuestListInTouchPoint(String authenticationToken,String tagId) throws ClientProtocolException, IOException {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);
        HttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(serviceLocation + "/api/touchpoints/" + tagId + "/guestCards");
        httpGet.setHeader("Authorization", "Basic " + authenticationToken);
        ResponseHandler<String> responseHandler = new BasicResponseHandler();
        String response = httpClient.execute(httpGet, responseHandler);
        return response;

    }





    //for guest  layout popup information
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
        HttpGet httpGet = new HttpGet(serviceLocation + "/api/guest/"+guestId);
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
}
