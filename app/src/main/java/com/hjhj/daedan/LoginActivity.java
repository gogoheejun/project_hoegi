package com.hjhj.daedan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

import static androidx.core.content.ContextCompat.checkSelfPermission;

public class LoginActivity extends AppCompatActivity {
    EditText et_email;
    EditText et_pw;
    Button btn_login;
    private FirebaseAuth mAuth;

    String token;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Log.e("로그인디버깅","LoginActivity onCreate");

        et_email = findViewById(R.id.activitylogin_et_email);
        et_pw = findViewById(R.id.activitylogin_et_pw);
        btn_login = findViewById(R.id.activitylogin_btn_login);
    
        mAuth = FirebaseAuth.getInstance();


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED) {
                String[] permissions = new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE};
                requestPermissions(permissions, 0);
            }
        }



//토큰값 가져오기 (기기마다 다름. 그니까 한 기기에서 한 아이디로만 접속 추천. id/token 연결해서 데이터에 저장할거니까)
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if(!task.isSuccessful()){
                    Toast.makeText(LoginActivity.this, "토큰등록 실패", Toast.LENGTH_SHORT).show();
                    return;
                }

                token = task.getResult();

                //추후에 이 토큰값을 dothome서버에서 사용하고자 하기에 Log로 출력해보기
                //그리고 화면에 보기 위해 Toast도 출력
//                Toast.makeText(LoginActivity.this, ""+token, Toast.LENGTH_SHORT).show();
                Log.i("TOKEN", token);
                //원래는 이 token값을 웹서버(dothome같은) 에 전송하여 회원정보를 db에 저장하듯이 token값도 DB에 저장해놓아야 함

            }
        });

//--------------------------------
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
                String  email = et_email.getText().toString().trim();
                String password = et_pw.getText().toString().trim();
                
                if(email.length()>0 && password.length()>0){
                    mAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){
                                        FirebaseUser user = mAuth.getCurrentUser();

                                        //user정보를 GUser에 담기
                                        intoGUser(user.getUid());
                                        Toast.makeText(LoginActivity.this, "로그인 성공", Toast.LENGTH_SHORT).show();

                                        //sharedpreference에 넣기
                                        SharedPreferences pref = getSharedPreferences("account", MODE_PRIVATE);
                                        SharedPreferences.Editor editor = pref.edit();
                                        editor.putString("email", email);
                                        editor.putString("password",password);
                                        editor.commit();

//
//                                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                                        startActivity(intent);
//                                        finish();
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

        //sharedPreference에서 로그인아이디 가져오기
        //todo 지금 얘가 문제야. 얘 삭제하면 잘돼. 그러나 자동로그인이 안돼..
        onLoginAgain();
    }

    private void onLoginAgain() {
        Log.e("로그인디버깅","LoginActivity onLoginAgain()");

        SharedPreferences pref = getSharedPreferences("account",MODE_PRIVATE);
        String email = pref.getString("email",null);
        String password = pref.getString("password",null);
        Log.e("로그인디버깅", "LoginActibity onLoginAgain() "+email+password);
        if(email!= null && password != null){
            //if정보갖고온게 있다면,
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){

                                FirebaseUser user = mAuth.getCurrentUser();

                                //user정보를 GUser에 담기
                                intoGUser(user.getUid());

                                Toast.makeText(LoginActivity.this, "로그인 성공", Toast.LENGTH_SHORT).show();

                            }else{
                                if(task.getException() != null){
                                    Toast.makeText(LoginActivity.this, "로그인 정보가 없어요", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            }
                        }
                    });
        }else{
            Toast.makeText(this, "hi~", Toast.LENGTH_SHORT).show();
            return;
        }

    }

    public void intoGUser(String userId){
        Log.e("로그인디버깅","LoginActivity intoGUser()");

        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.collection("users").document(userId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot documentSnapshot = task.getResult();
                   GUser.nickname = documentSnapshot.getString("nickname");
                   GUser.userId = documentSnapshot.getString("userid");
                    Log.e("로그인디버깅","LoginActivity intoGUser()1"+GUser.nickname);
                    String email = documentSnapshot.getString("email");
                    Log.e("로그인디버깅","LoginActivity intoGUser()2"+email);
                    String domain = email.substring(email.indexOf("@")+1);
                    Log.e("로그인디버깅","LoginActivity intoGUser()3"+domain);

                    switch (domain){
                       case "hufs.ac.kr":
                           GUser.school = "한국외국어대학교";
                           break;
                       case "uos.ac.kr":
                           GUser.school = "서울시립대학교";
                           break;
                       case "khu.ac.kr":
                           GUser.school = "경희대학교";
                           break;
                   }
                   GUser.profileUrl = documentSnapshot.getString("profile");

                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    Log.e("로그인디버깅","LoginActivity intent보냄");
                    finish();


                }
            }
        });
    }


    //회원가입버튼
    public void activitylogin_onclick_signup(View view) {
        startActivity(new Intent(LoginActivity.this, SignupActivity.class));
    }

    @Override
    protected void onPause() {
        Log.e("로그인디버깅","LoginActivity onPause()");
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e("로그인디버깅","LoginActivity onStop()");

    }

    @Override
    protected void onDestroy() {
        Log.e("로그인디버깅","LoginActivity onDestroy()");
        super.onDestroy();
    }
}