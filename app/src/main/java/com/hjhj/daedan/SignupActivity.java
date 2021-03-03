package com.hjhj.daedan;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class SignupActivity extends AppCompatActivity {
    ImageView iv_profile;
    EditText et_nickname, et_email, et_checknumber, et_password, et_password2;
    Uri imgUri;//선택된 이미지의 컨텐츠 주소(경로)

    private FirebaseAuth mAuth;
    String profileUrl;
    String token;

    boolean checknum = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        iv_profile = findViewById(R.id.signupactivity_iv_profile);
        et_nickname = findViewById(R.id.signupactivity_et_nickname);
        et_email = findViewById(R.id.signupactivity_et_email);
        et_checknumber = findViewById(R.id.signupactivity_et_checknumber);
        et_password = findViewById(R.id.signupactivity_et_password);
        et_password2 = findViewById(R.id.signupactivity_et_password2);

        mAuth = FirebaseAuth.getInstance();

//토큰값 가져오기
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if(!task.isSuccessful()){
//                    Toast.makeText(LoginActivity.this, "토큰등록 실패", Toast.LENGTH_SHORT).show();
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


    }


    public void click_profile(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,10);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==10 && resultCode==RESULT_OK){
            imgUri = data.getData();
            Picasso.get().load(imgUri).into(iv_profile);

        }
    }

    public void click_sendtoEmail(View view) {
        String substring1 ="@hufs.ac.kr";
        String substring2 ="@khu.ac.kr";
        String substring3 ="@uos.ac.kr";
        if(et_email.getText().toString().contains(substring1)&& et_email.getText().toString().contains(substring2)&& et_email.getText().toString().contains(substring3)){
            Snackbar.make(iv_profile,"인증번호를 무시하고 비밀번호를 등록하세요", BaseTransientBottomBar.LENGTH_INDEFINITE).setAction("ok", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                }
            }).show();
        }else{
            Snackbar.make(iv_profile,"인증번호를 무시하고 비밀번호를 등록하세요", BaseTransientBottomBar.LENGTH_INDEFINITE).setAction("ok", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                }
            }).show();
        }
    }

    Boolean ischecked = true;//false로 해야함. 테스트로 true함
    public void click_check(View view) {
        //todo: 인증번호 확인되면 ischecked라는 boolean을 true로 해서 click_ok함수 if문에 넣음
    }

    public void click_ok(View view) {
        if(et_nickname.getText().toString().length() <3){
            Toast.makeText(this, "닉네임은 3문자 이상 입력해주세요", Toast.LENGTH_LONG).show();
            return;
        }
        String substring1 ="@hufs.ac.kr";
        String substring2 ="@khu.ac.kr";
        String substring3 ="@uos.ac.kr";
        if(!et_email.getText().toString().contains(substring1) && !et_email.getText().toString().contains(substring2) && !et_email.getText().toString().contains(substring3)){
            Snackbar.make(iv_profile,"@huf.ac.kr, @khu.ac.kr, @uos.ac.kr 만 사용하실 수 있습니다. ", BaseTransientBottomBar.LENGTH_INDEFINITE).setAction("ok", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                }
            }).show();
            return;
        }
        if(ischecked == false){
            Toast.makeText(this, "인증번호를 다시 확인해주세요", Toast.LENGTH_LONG).show();
            return;
        }
        if(!signup()) {
            return;
        }
        else{
//            //todo: db에 프로필, 닉네임, 이메일 저장,,, auth에도 등록
//
//            //프로필사진을 firebase storage에저장
//            //현재시간
//            TimeZone zone = TimeZone.getTimeZone("Asia/Seoul");  // TimeZone에 표준시 설정
//            DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmm", Locale.KOREAN);
//            dateFormat.setTimeZone(zone);
//            String uploadtime = dateFormat.format(new Date());
//            if(imgUri != null){
//                String fileName = uploadtime+"png";
//                FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
//                StorageReference imgRef = firebaseStorage.getReference("profile/"+fileName);
//
//                UploadTask uploadTask = imgRef.putFile(imgUri);
//                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                    @Override
//                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                        imgRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                            @Override
//                            public void onSuccess(Uri uri) {
//                                profileUrl = uri.toString();
////                                Toast.makeText(SignupActivity.this, "프로필이미지 firestorage에 저장쓰", Toast.LENGTH_SHORT).show();
//
//                                //★★★프로필을 스토리지에 올렸으면, 그 url을 유저db에다가 보관
//                                FirebaseFirestore firestore = FirebaseFirestore.getInstance();
//                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//                                String userid = user.getUid();
//                                String userEmail = user.getEmail();
//                                UsersItem item = new UsersItem(profileUrl,et_nickname.getText().toString(),userid, userEmail,token); //프로필이미지, 닉네임, userid, 이메일
//                                firestore.collection("users").document(userid).set(item)
//                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
//                                            @Override
//                                            public void onSuccess(Void aVoid) {
//                                                Log.d("process","6666");
//                                                Toast.makeText(SignupActivity.this, "정보등록 성공", Toast.LENGTH_SHORT).show();
//                                                Intent intent = new Intent(SignupActivity.this, MainActivity.class);
//                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                                                startActivity(intent);
//                                            }
//                                        })
//                                        .addOnFailureListener(new OnFailureListener() {
//                                            @Override
//                                            public void onFailure(@NonNull Exception e) {
//                                                Log.d("process","7777");
//                                                Toast.makeText(SignupActivity.this, "정보등록 실패ㅠ", Toast.LENGTH_SHORT).show();
//                                                Log.w("TAG",e);
//                                            }
//                                        });
//
//                            }
//                        });
//                    }
//                });
//            }


            Log.d("process","8888");
        }

    }


    private boolean signup() {

        String email = et_email.getText().toString();
        String password = et_password.getText().toString();
        String password2 = et_password2.getText().toString();

        if(email.length()>0 && password.length()>0){
            if(password.equals(password2)){
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    Toast.makeText(SignupActivity.this, "회원가입성공", Toast.LENGTH_SHORT).show();

                                    //쓰레드때문에 여기서 가입완료를 해야 함
                                    //todo: db에 프로필, 닉네임, 이메일 저장,,, auth에도 등록

                                    //프로필사진을 firebase storage에저장
                                    //현재시간
                                    TimeZone zone = TimeZone.getTimeZone("Asia/Seoul");  // TimeZone에 표준시 설정
                                    DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmm", Locale.KOREAN);
                                    dateFormat.setTimeZone(zone);
                                    String uploadtime = dateFormat.format(new Date());
                                    if(imgUri != null){
                                        String fileName = uploadtime+"png";
                                        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
                                        StorageReference imgRef = firebaseStorage.getReference("profile/"+fileName);

                                        UploadTask uploadTask = imgRef.putFile(imgUri);
                                        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                            @Override
                                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                imgRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                    @Override
                                                    public void onSuccess(Uri uri) {
                                                        profileUrl = uri.toString();
//                                Toast.makeText(SignupActivity.this, "프로필이미지 firestorage에 저장쓰", Toast.LENGTH_SHORT).show();

                                                        //★★★프로필을 스토리지에 올렸으면, 그 url을 유저db에다가 보관
                                                        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
                                                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                                        String userid = user.getUid();
                                                        String userEmail = user.getEmail();
                                                        UsersItem item = new UsersItem(profileUrl,et_nickname.getText().toString(),userid, userEmail,token); //프로필이미지, 닉네임, userid, 이메일
                                                        firestore.collection("users").document(userid).set(item)
                                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                    @Override
                                                                    public void onSuccess(Void aVoid) {
                                                                        Log.d("process","6666");
                                                                        Toast.makeText(SignupActivity.this, "정보등록 성공", Toast.LENGTH_SHORT).show();
                                                                        Intent intent = new Intent(SignupActivity.this, MainActivity.class);
                                                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                                        startActivity(intent);
                                                                    }
                                                                })
                                                                .addOnFailureListener(new OnFailureListener() {
                                                                    @Override
                                                                    public void onFailure(@NonNull Exception e) {
                                                                        Log.d("process","7777");
                                                                        Toast.makeText(SignupActivity.this, "정보등록 실패ㅠ", Toast.LENGTH_SHORT).show();
                                                                        Log.w("TAG",e);
                                                                    }
                                                                });

                                                    }
                                                });
                                            }
                                        });
                                    }


                                }else{
                                    if(task.getException() != null)
//                                        Toast.makeText(SignupActivity.this, ""+task.getException().toString(), Toast.LENGTH_SHORT).show();
                                        Toast.makeText(SignupActivity.this, "비밀번호는 6자 이상으로 해주세요", Toast.LENGTH_SHORT).show();
                                        checknum = false;
                                }
                            }
                        });
            }else{
                Toast.makeText(this, "비밀번호 일치안합니다!", Toast.LENGTH_SHORT).show();
                return false;
            }
        }else{
            Toast.makeText(this, "이메일 또는 비번입력!", Toast.LENGTH_SHORT).show();
        }



    //마무리했으면 그 정보를 sharedPreference에 저장
        SharedPreferences pref = getSharedPreferences("account", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        editor.putString("email", email);
        editor.putString("password",password);
        editor.commit();

        return true;

    }


}