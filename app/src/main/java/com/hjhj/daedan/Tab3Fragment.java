package com.hjhj.daedan;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.GuardedBy;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.Context.MODE_PRIVATE;

public class Tab3Fragment extends Fragment {
    CircleImageView cv;
    TextView tv_name;
    TextView tv_school;
    private FirebaseAuth mAuth;
    String email;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tab3,container,false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAuth = FirebaseAuth.getInstance();

        cv = view.findViewById(R.id.tab3Fragment_iv);
        tv_name = view.findViewById(R.id.tab3Fragment_nickname);
        tv_school = view.findViewById(R.id.tab3Fragment_school);

        tv_name.setText(GUser.nickname);
        tv_school.setText("("+GUser.school+")");
        Glide.with(this).load(GUser.profileUrl).into(cv);

        view.findViewById(R.id.tab3_gotoMyText).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseFirestore firestore = FirebaseFirestore.getInstance();
                ArrayList<String> ids= new ArrayList<>();
                firestore.collection("markers").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            List<DocumentSnapshot> snapshots = task.getResult().getDocuments();
                            for(DocumentSnapshot document : snapshots){
                                MarkersItem item = document.toObject(MarkersItem.class);
                                ids.add(item.userid);
                            }
                            if(ids.contains(GUser.userId)){
                                Intent intent = new Intent(getActivity(), WatchViewActivity.class);
                                intent.putExtra("markerUserid",GUser.userId);
                                startActivity(intent);
                            }else{
                                Toast.makeText(getActivity(), "게시한 글이 없어요", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
            }
        });

        view.findViewById(R.id.tab3_gotoProfile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(getActivity(), ProfileUpdateActivity.class));
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("이 페이지는 현재 보수중입니다").show();
            }
        });

        view.findViewById(R.id.tab3_gotoFavorite).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),FavActivity.class);
                startActivity(intent);
            }
        });


        view.findViewById(R.id.tab3_logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("로그아웃이 됩니다")
                    .setPositiveButton("예", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mAuth.signOut();
                            //로그아웃했으니 sharedpreference에 저장된 정보지움
                            SharedPreferences pref = getActivity().getSharedPreferences("account", MODE_PRIVATE);
                            SharedPreferences.Editor editor = pref.edit();
                            editor.putString("email", null);
                            editor.putString("password",null);
                            editor.commit();

                            //다시 로그인화면으로 이동
                            startActivity(new Intent(getActivity(), LoginActivity.class));
                            getActivity().finish();
                        }
                    }).setNegativeButton("아니요", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).show();
            }
        });






        // Intent intent = new Intent(getActivity(),WatchViewActivity.class);
        //                                intent.putExtra("markerUserid",marker.getTag().toString());
        //                                startActivity(intent);
    }
}
