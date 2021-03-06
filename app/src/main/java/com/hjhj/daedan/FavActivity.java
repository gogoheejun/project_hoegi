package com.hjhj.daedan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FavActivity extends AppCompatActivity {
    ArrayList<MarkersItem> markersitems = new ArrayList<>();
    RecyclerView recyclerView;
    FavAdapter adapter;
    SwipeRefreshLayout refreshLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fav);
        refreshLayout = findViewById(R.id.layout_refresh);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                markersitems.clear();
                loadData();
                adapter.notifyDataSetChanged();
                refreshLayout.setRefreshing(false);
                if(markersitems.size()==0) findViewById(R.id.fav_noMessage).setVisibility(View.VISIBLE);
            }
        });

        recyclerView= findViewById(R.id.fav_recycler);
        adapter = new FavAdapter(FavActivity.this, markersitems);
        markersitems.add(new MarkersItem());

//        recyclerView.setAdapter(adapter);  //원래 여기에 하고, markersitems.add(markersItem)이거 할때마다 notify해주는게 맞음....근데 why..?

    }

    @Override
    protected void onResume() {
        super.onResume();

        loadData();

    }

    ArrayList<FavItem> list;
    void loadData(){

        Retrofit.Builder builder= new Retrofit.Builder();
        builder.baseUrl("http://alexang.dothome.co.kr/");
        builder.addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit= builder.build();

        RetrofitService retrofitService= retrofit.create(RetrofitService.class);
        Call<ArrayList<FavItem>> call= retrofitService.loadFavList(GUser.userId);
        call.enqueue(new Callback<ArrayList<FavItem>>() {
            @Override
            public void onResponse(Call<ArrayList<FavItem>> call, Response<ArrayList<FavItem>> response) {


                markersitems.clear();
                list = response.body();
//                Log.d("fav","loadData()"+list.get(0).favID);
                loadData2();

//                list = response.body();
//                for(FavItem listitem: list ){
//                    favID = listitem.favID;
//                    loadData2();
//
////                    Log.d("loadfav",listitem.favID);
//                    // TODO: 2021-03-02
////                    adapter.notifyItemInserted(0);
//
//                }
            }

            @Override
            public void onFailure(Call<ArrayList<FavItem>> call, Throwable t) {
                Log.d("fav","에러"+t.getMessage());
            }
        });
    }
//------------------------
    int a=0;
    int b=0;
    void loadData2() {
        for (FavItem listitem : list) {
            a++;
            String favID = listitem.favID;
            Log.d("fav", "loadDATA2()//a=" +a+"//"+ favID);

            FirebaseFirestore firestore = FirebaseFirestore.getInstance();
            firestore.collection("markers").document(favID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot documentSnapshot = task.getResult();
                        MarkersItem markersItem = new MarkersItem();
                        markersItem.school = documentSnapshot.getString("school");
                        markersItem.nickname = documentSnapshot.getString("nickname");
                        markersItem.userid = documentSnapshot.getString("userid");
                        markersItem.category = documentSnapshot.getString("category");
                        markersItem.uploadTime = documentSnapshot.getString("uploadTime");
                        markersItem.timeLength = documentSnapshot.getString("timeLength");
                        markersItem.title = documentSnapshot.getString("title");
                        markersItem.message = documentSnapshot.getString("message");
                        markersItem.imgUrl = documentSnapshot.getString("imgUrl");
                        markersItem.lat = documentSnapshot.getString("lat");
                        markersItem.lon = documentSnapshot.getString("lon");
                        markersitems.add(markersItem);
                        //
                        adapter.notifyItemInserted(markersitems.size()-1);
//                        Log.d("fav", "loadDATA2()22" + markersitems.size()+"  "+ markersitems.get(b).nickname);
                        b++;
                        Log.d("fav", " onComplete///b="+b );
                        Log.d("fav", " "+markersItem.nickname );
                        if (a == b) {
//                            Log.d("fav", "a=b"+a+b +"........."+ markersitems.get(0).category);
//                            Log.d("fav", "a=b"+a+b +"........."+ markersitems.get(1).category);
                            recyclerView.setAdapter(adapter);
                            if(markersitems.size()>0) findViewById(R.id.fav_noMessage).setVisibility(View.INVISIBLE);
                        }
                    }
                }
            });
        }


    }
}