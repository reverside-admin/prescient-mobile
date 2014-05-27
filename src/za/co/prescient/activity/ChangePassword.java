package za.co.prescient.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import za.co.prescient.R;

/**
 * Created by Bibhuti on 2014/05/22.
 */
public class ChangePassword extends Activity implements View.OnClickListener {
    SessionManager session;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        session = new SessionManager(getApplicationContext());
        Log.i("check", "checking the session");
        session.checkSession();


        setContentView(R.layout.activity_change_password);
        Log.i("change password activity", "started");

        Button buttonOk = (Button) findViewById(R.id.changepwd);
        buttonOk.setOnClickListener(this);

        Button buttonCancel=(Button)findViewById(R.id.cancel);
        buttonCancel.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                logOut();
            }
        });
    }


    public void onClick(View view) {
        try {
            Context context = getApplicationContext();

            //get username and role coming from previous page
            String userName = this.getIntent().getStringExtra("name");
            String role = this.getIntent().getStringExtra("role");

            String password = ((EditText) findViewById(R.id.pswd))
                    .getText().toString();

            String confirmPassword = ((EditText) findViewById(R.id.confpswd))
                    .getText().toString();


            if (password.equals(confirmPassword)) {
                if (!confirmPassword.equalsIgnoreCase("password")) {

                    //do our job here
                    ServiceInvoker.changePassword(session.getToken(), confirmPassword);

                    //password is changed so new token need tobe set
                    session.setAuthenticationToken(userName, confirmPassword);

                    //DO Navigation Here
                    if (role.equals("ROLE_ADMIN")) {
                        Intent intent = new Intent(context, AdminHomePage.class);
                        intent.putExtra("name", userName);
                        startActivity(intent);
                    } else if (role.equals("ROLE_MANAGER")) {
                        Intent intent = new Intent(context, ManagerHomePage.class);
                        intent.putExtra("name", userName);
                        startActivity(intent);
                    } else if (role.equals("ROLE_STAFF")) {
                        Intent intent = new Intent(context, StaffHomePage.class);
                        intent.putExtra("name", userName);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(context, LoginPage.class);
                        startActivity(intent);
                        Toast.makeText(getApplicationContext(), "User Not Found",
                                Toast.LENGTH_SHORT).show();
                    }
                } else {

                    Toast.makeText(getApplicationContext(), "password can not be password",
                            Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getApplicationContext(), "password mismatched",
                        Toast.LENGTH_SHORT).show();
            }

        } catch (Exception ee) {
            Log.i("Error In Reset Password", ee.getMessage());
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
        // Do Here what ever you want do on back press;
        // if you want to make the back button behave normally call:: super.onBackPressed();
        Log.i("Back", "pressed");

    }


}