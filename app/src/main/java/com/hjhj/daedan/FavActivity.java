package com.hjhj.daedan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FavActivity extends AppCompatActivity {
    ArrayList<MarkersItem> items = new ArrayList<>();
    RecyclerView recyclerView;
    FavAdapter adapter;
    SwipeRefreshLayout refreshLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fav);

        recyclerView= findViewById(R.id.fav_recycler);
//        adapter= new favAdapter(this, items);
//        recyclerView.setAdapter(adapter);

    }
String favID;
    @Override
    protected void onResume() {
        super.onResume();

        loadData();
    }
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
                items.clear();


                ArrayList<FavItem> list = response.body();
                for(FavItem listitem: list ){
                    favID = listitem.favID;
                    loadData2();
//                    Log.d("loadfav",listitem.favID);
                    // TODO: 2021-03-02
//                    adapter.notifyItemInserted(0);

                }
            }

            @Override
            public void onFailure(Call<ArrayList<FavItem>> call, Throwable t) {
                Log.d("loadfav","에러"+t.getMessage());
            }
        });
    }
//------------------------
    void loadData2(){
        Log.d("TAG", "list_loadData");
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.collection("markers").whereEqualTo("userid",favID).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    //일부러 MarkersItem을 MarkersItem_static이랑 따로만듦. 스태틱으로만 하면 item안에 있는 요소들 다 똑같아짐
                    for (QueryDocumentSnapshot document : task.getResult()){
                        Map<String, Object> marker = document.getData();

                        items.add( new MarkersItem(marker.get("school").toString(),marker.get("nickname").toString(),marker.get("userid").toString(),marker.get("category").toString(),
                                marker.get("uploadTime").toString(), marker.get("timeLength").toString(),marker.get("title").toString(),marker.get("message").toString(), marker.get("imgUrl").toString(),
                                marker.get("lat").toString(), marker.get("lon").toString()));


                    }
//                    Log.d("markeritem",items.get(0).nickname+"\n"+items.get(1).nickname+"\n"+items.get(2).nickname);

                    //리사이클러뷰에 어댑터랑 items를 결합!...원래는 onViewCreated에서 했었는데 여기선 데이터를 다 받아온 다음에 해야하니까
//                    여기서 함
                    adapter = new FavAdapter(FavActivity.this,items);
                    recyclerView.setAdapter(adapter);
                }
            }
        });
    }


}