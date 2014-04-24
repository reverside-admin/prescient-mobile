package za.co.prescient.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;
import za.co.prescient.R;

public class WelcomePage extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_page);
        addListenerOnButton();
        Toast.makeText(getApplicationContext(), R.string.welcome_to_prescient, Toast.LENGTH_SHORT).show();
    }

    public void addListenerOnButton() {
        final Context context = this;
        findViewById(R.id.start_btn).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, LoginPage.class);
                startActivity(intent);
            }
        });
    }
}
