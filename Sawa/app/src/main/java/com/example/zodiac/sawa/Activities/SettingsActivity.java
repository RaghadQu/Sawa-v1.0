package com.example.zodiac.sawa.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.example.zodiac.sawa.Activities.ChangeAccountInfoActivities.ChangeEmailActivity;
import com.example.zodiac.sawa.Activities.ChangeAccountInfoActivities.ChangeMobileActivity;
import com.example.zodiac.sawa.Activities.ChangeAccountInfoActivities.ChangePasswordActivity;
import com.example.zodiac.sawa.R;

/**
 * Created by zodiac on 10/08/2017.
 */

public class SettingsActivity extends Activity {

    TextView toolbarText;
    TextView editProfile , changePassword,  changeEmail , changeMobile;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        toolbarText = (TextView) findViewById(R.id.toolBarText);
        editProfile = (TextView) findViewById(R.id.editProfileLabel);
        changePassword=(TextView) findViewById(R.id.changePasswordLabel);
        changeEmail=(TextView) findViewById(R.id.changeEmailLabel);
        changeMobile=(TextView) findViewById(R.id.changeMobileLabel);


        toolbarText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                if (event.getX() <= (toolbarText.getCompoundDrawables()[DRAWABLE_LEFT].getBounds().width() + 40)) {
                    finish();
                    return true;
                }
                return false;
            }
        });

        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), EditProfileActivity.class);
                startActivity(i);
            }
        });
        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), ChangePasswordActivity.class);
                startActivity(i);
            }
        });
        changeEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), ChangeEmailActivity.class);
                startActivity(i);
            }
        });
        changeMobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), ChangeMobileActivity.class);
                startActivity(i);
            }
        });
    }

}
