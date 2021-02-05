package com.hjhj.daedan;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

public class SignupActivity extends AppCompatActivity {
    ImageView iv_profile;
    EditText nickname, email, checknumber, password, password2;
    Uri imgUri;//선택된 이미지의 컨텐츠 주소(경로)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        iv_profile = findViewById(R.id.signupactivity_iv_profile);
        nickname = findViewById(R.id.signupactivity_et_nickname);
        email = findViewById(R.id.signupactivity_et_email);
        checknumber = findViewById(R.id.signupactivity_et_checknumber);
        password = findViewById(R.id.signupactivity_et_password);
        password2 = findViewById(R.id.signupactivity_et_password2);

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
        if(email.getText().toString().contains(substring1)|| email.getText().toString().contains(substring2)|| email.getText().toString().contains(substring3)){

        }else{
            Snackbar.make(iv_profile,"@huf.ac.kr, @khu.ac.kr, @uos.ac.kr 만 사용하실 수 있습니다", BaseTransientBottomBar.LENGTH_INDEFINITE).setAction("ok", new View.OnClickListener() {
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

        if(nickname.getText().toString().length() <3){
            Toast.makeText(this, "닉네임은 3문자 이상 입력해주세요", Toast.LENGTH_LONG).show();
            return;
        }
        if(ischecked == false){
            Toast.makeText(this, "인증번호를 다시 확인해주세요", Toast.LENGTH_LONG).show();
            return;
        }
        if(!password.getText().toString().equals(password2.getText().toString())){
            Toast.makeText(this, "비밀번호를 다시 확인해주세요", Toast.LENGTH_LONG).show();
            return;
        }
        else{
            //todo: db에 프로필, 닉네임, 이메일 저장,,, auth에도 등록
            startActivity(new Intent(this, MainActivity.class));
            Toast.makeText(this, "입장!!", Toast.LENGTH_SHORT).show();
        }

    }


}