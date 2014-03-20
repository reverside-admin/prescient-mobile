package za.co.prescient.activity;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.StrictMode;
import android.util.Base64;

public class ServiceInvoker {
	
	private static String authenticationToken ;
	
	private static final String serviceLocation = "http://192.168.0.116:8080" ;
	
	
	public static String getUser(String userName, String password) throws ClientProtocolException, IOException {
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);
		authenticationToken = Base64.encodeToString((userName+":"+password).getBytes(), Base64.NO_WRAP);
		HttpClient httpClient = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(serviceLocation + "/service/user/"+userName);
		httpGet.setHeader("Authorization", "Basic " + authenticationToken);
	    ResponseHandler<String> responseHandler = new BasicResponseHandler();
		String response = httpClient.execute(httpGet, responseHandler);
		return response;
	}
}
