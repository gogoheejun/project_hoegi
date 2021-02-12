package com.hjhj.daedan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    EditText et_email;
    EditText et_pw;
    Button btn_login;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        et_email = findViewById(R.id.activitylogin_et_email);
        et_pw = findViewById(R.id.activitylogin_et_pw);
        btn_login = findViewById(R.id.activitylogin_btn_login);
    
        mAuth = FirebaseAuth.getInstance();
        
        
        et_pw.setOnEditorActionListener(new TextView.OnEditorActionListener() {
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

        //로그인버튼
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                String  email = et_email.getText().toString();
                String password = et_pw.getText().toString();
                
                if(email.length()>0 && password.length()>0){
                    mAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        Toast.makeText(LoginActivity.this, "로그인 성공", Toast.LENGTH_SHORT).show();
                                        
                                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(intent);
                                    }else{
                                        if(task.getException() != null){
                                            Toast.makeText(LoginActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                            });
                }else{
                    Toast.makeText(LoginActivity.this, "이메일 또는 비밀번호를 입력해주세요", Toast.LENGTH_SHORT).show();
                }
                
            }
        });
    }


    //회원가입버튼
    public void activitylogin_onclick_signup(View view) {
        startActivity(new Intent(LoginActivity.this, SignupActivity.class));
    }

}