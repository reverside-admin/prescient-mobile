package za.co.prescient.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Base64;
import android.util.Log;

public class SessionManager {

    SharedPreferences pref;
    Editor editor;
    Context context;

    private static final String IS_LOGIN = "IsLoggedIn";
    public static final String KEY_NAME = "name";
    private static final String KEY_PASS = "password";
    private static final String KEY_TOKEN = "authenticationToken";


    public SessionManager(Context context) {
        this.context = context;
        pref = this.context.getSharedPreferences("AndroidHivePref", 0);
        editor = pref.edit();
    }


    public String getName() {
        return pref.getString("name", null);
    }


    public void createSession(String name, String password) {
        String authenticationToken = Base64.encodeToString((name + ":" + password).getBytes(), Base64.NO_WRAP);
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(KEY_NAME, name);
        editor.putString(KEY_TOKEN, authenticationToken);
        editor.commit();
    }

    public void checkSession() {
        if (!this.isLoggedIn()) {
            Intent intent = new Intent(this.context, LoginPage.class);
            this.context.startActivity(intent);
        }
    }

    public String getToken() {
        return pref.getString("authenticationToken", null);
    }

    public void setAuthenticationToken(String name,String password) {
        createSession(name,password);

    }


    public boolean isLoggedIn() {
        return pref.getBoolean(IS_LOGIN, false);

    }

    public void logOut() {
        editor.clear();
        editor.commit();
    }
}
