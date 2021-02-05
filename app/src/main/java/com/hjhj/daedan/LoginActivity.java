package com.hjhj.daedan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;

public class LoginActivity extends AppCompatActivity {
    EditText email;
    EditText pw;
    Button btn_login;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.activitylogin_et_email);
        pw = findViewById(R.id.activitylogin_et_pw);
        btn_login = findViewById(R.id.activitylogin_btn_login);






        pw.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                boolean handled = false;
                if(i == EditorInfo.IME_ACTION_DONE){
                    btn_login.callOnClick();
                    handled = true;
                }
                return handled;
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
            }
        });
    }


    public void activitylogin_onclick_signup(View view) {
        startActivity(new Intent(LoginActivity.this, SignupActivity.class));
    }

}