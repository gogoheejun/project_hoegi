package com.hjhj.daedan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bnv;
    Fragment[] fragments = new Fragment[3];
    FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragments[0] = new Tab1Fragment();

        fragmentManager = getSupportFragmentManager();
        FragmentTransaction tran = fragmentManager.beginTransaction();
        tran.add(R.id.MainActivity_container,fragments[0]);

        tran.commit();

        bnv = findViewById(R.id.MainActivity_bnv);
        bnv.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                FragmentTransaction tran = fragmentManager.beginTransaction();
                if(fragments[0]!=null) tran.hide(fragments[0]);
                if(fragments[1]!=null) tran.hide(fragments[1]);
                if(fragments[2]!=null) tran.hide(fragments[2]);

                switch (item.getItemId()){
                    case R.id.menu_bnv_tab1:
                        tran.show(fragments[0]);
                        break;
                    case R.id.menu_bnv_tab2:
                        if(fragments[1]==null){
                            fragments[1] = new Tab2Fragment();
                            tran.add(R.id.MainActivity_container, fragments[1]);
                        }
                        tran.show(fragments[1]);
                        break;
                    case R.id.menu_bnv_tab3:
                        if(fragments[2]==null){
                            fragments[2] = new Tab3Fragment();
                            tran.add(R.id.MainActivity_container, fragments[2]);
                        }
                        tran.show(fragments[2]);
                        break;
                }
                tran.commit();
                return true;
            }
        });

    }

}