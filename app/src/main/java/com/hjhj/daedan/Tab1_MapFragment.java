package com.hjhj.daedan;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import static androidx.core.content.ContextCompat.checkSelfPermission;

public class Tab1_MapFragment extends Fragment {
    GoogleMap gMap;
    MapView mapView;
    MarkerOptions myMarker = null;
    TextView tv_temperature;
    ImageView iv_weather;
    String weather, temperature;
    String date, time;
    String server;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(Build.VERSION.SDK_INT >=Build.VERSION_CODES.M){
            if(checkSelfPermission(getActivity(),Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED){
                String[] permissions = new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE};
                requestPermissions(permissions, 0);
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.page_map_tab1, container, false);

        tv_temperature = view.findViewById(R.id.page_map_tv_temperature);
        iv_weather = view.findViewById(R.id.page_map_iv_weather);

        getTime();
        getInfo();


        mapView = (MapView) view.findViewById(R.id.mappage_mapview);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        MapsInitializer.initialize(getActivity().getApplicationContext());
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                gMap = googleMap;
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                UiSettings settings = gMap.getUiSettings();
                settings.setZoomControlsEnabled(true);
                settings.setMyLocationButtonEnabled(true);
                gMap.setMyLocationEnabled(true);
            //----------지도준비됨

                //현재위치가져오는 함수
                gMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
                    @Override
                    public void onMapLoaded() {
                        Log.e("POSITION", gMap.getCameraPosition().target.toString());
                    }
                });

                LatLng seoul = new LatLng(37.562087, 127.035192);
//                gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(seoul, 15));//줌 1~25
//                gMap.addMarker(new MarkerOptions().position(seoul).title("Title").snippet("Marker Description"));

//                CameraPosition cameraPosition = new CameraPosition.Builder().target(seoul).zoom(12).build();
//                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                CameraPosition cameraPosition = new CameraPosition.Builder().target(seoul).zoom(12).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));


            }

            public void onCameraMoveStarted(int reason) {

                if (reason == GoogleMap.OnCameraMoveStartedListener.REASON_GESTURE) {
                    Toast.makeText(getActivity(), "The user gestured on the map.", Toast.LENGTH_SHORT).show();
                } else if (reason == GoogleMap.OnCameraMoveStartedListener.REASON_API_ANIMATION) {
                    Toast.makeText(getActivity(), "The user tapped something on the map.", Toast.LENGTH_SHORT).show();
                } else if (reason == GoogleMap.OnCameraMoveStartedListener.REASON_DEVELOPER_ANIMATION) {
                    Toast.makeText(getActivity(), "The app moved the camera.", Toast.LENGTH_SHORT).show();
                }
            }


            public void onCameraMove() {
                Toast.makeText(getActivity(), "The camera is moving.", Toast.LENGTH_SHORT).show();
            }
        });


        return view;
    }


    private void getInfo() {
        String api = "ZkmHEAVaoQ0yFwBbRFQSMJkOhXX%2FMQzcTpYDB0Q513dcVb3Vuz6vCU7QSEPdyYs0A3aOUSDG2WuzVo%2BQDF4beQ%3D%3D";

        String url1 = "http://apis.data.go.kr/1360000/VilageFcstInfoService/getUltraSrtNcst?serviceKey=";
        String url2 = "&numOfRows=100&pageNo=1&dataType=XML&base_date=";
        //todo: 현재 좌표 가져와서 변환해서 넣어야함

        server = url1+api+url2+date+"&base_time="+time+"&nx="+"55"+"&ny="+"127";

        new Thread(){
            @Override
            public void run() {
                super.run();

                try {
                    Log.e("Test","work0");
                    URL url = new URL(server);
                    Log.e("Test","work1");
                    InputStream is = url.openStream();
                    Log.e("Test","work2");
                    InputStreamReader isr = new InputStreamReader(is);
                    Log.e("Test","work3");
                    XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                    XmlPullParser xpp = factory.newPullParser();
                    xpp.setInput(isr);

                    int eventType = xpp.getEventType();

                    while(eventType!= XmlPullParser.END_DOCUMENT){
                        switch (eventType){
                            case  XmlPullParser.START_DOCUMENT:
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getActivity(), "start!", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                break;
                            case XmlPullParser.START_TAG:
                                String tagName = xpp.getName();
                                if(tagName.equals("category")){
                                    xpp.next();
                                    if(xpp.getText().equals("T1H")){
                                        for(int i=0;i<9;i++){
                                            xpp.next();
                                        }
                                        temperature = xpp.getText();
                                    }
                                    if(xpp.getText().equals("PTY")){
                                        for(int i=0;i<9;i++){
                                            xpp.next();
                                        }
                                        weather = xpp.getText();

                                    }
                                }
                                break;

                            case XmlPullParser.TEXT:
                                break;
                            case XmlPullParser.END_TAG:
                                break;

                        }
                        eventType = xpp.next();
                    }

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tv_temperature.setText(temperature+"도");
                            //- 강수형태(PTY) 코드 : 없음(0), 비(1), 비/눈(2), 눈(3), 소나기(4), 빗방울(5), 빗방울/눈날림(6), 눈날림(7)
                            switch (weather){
                                case "0":
                                    Glide.with(getActivity()).load(R.drawable.icon_earth).into(iv_weather);
                                    break;
                                case "1":
                                    Glide.with(getActivity()).load(R.drawable.icon_rainy).into(iv_weather);
                                    break;
                                case "2":
                                    Glide.with(getActivity()).load(R.drawable.icon_rainsnow).into(iv_weather);
                                    break;
                                case "3":
                                    Glide.with(getActivity()).load(R.drawable.icon_snowy).into(iv_weather);
                                    break;
                                case "4":
                                    Glide.with(getActivity()).load(R.drawable.icon_rainy).into(iv_weather);
                                    break;
                                case "5":
                                    Glide.with(getActivity()).load(R.drawable.icon_rainy).into(iv_weather);
                                    break;
                                case "6":
                                    Glide.with(getActivity()).load(R.drawable.icon_rainsnow).into(iv_weather);
                                    break;
                                case "7":
                                    Glide.with(getActivity()).load(R.drawable.icon_snowy).into(iv_weather);
                                    break;
                            }
                        }
                    });

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                }
            }
        }.start();

    }

    private void getTime() {
        TimeZone zone = TimeZone.getTimeZone("Asia/Seoul");  // TimeZone에 표준시 설정
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd", Locale.KOREAN);
        dateFormat.setTimeZone(zone);                    //DateFormat에 TimeZone 설정

        DateFormat timeFormat = new SimpleDateFormat("HHmm", Locale.KOREAN);
        timeFormat.setTimeZone(zone);

        date = dateFormat.format(new Date());
        String currentTime = timeFormat.format(new Date());
        //t시 40분이 넘으면 t시, 안넘으면 t-1시..단 t-1이 -1이 되면 23시로.
        String currentTime1 = currentTime.substring(0,2);
        String currentTime2 = currentTime.substring(2);

        if(Integer.parseInt(currentTime1)<11){
            if(Integer.parseInt(currentTime2) >40){
                time = currentTime1+"00";
            }else{
                if(Integer.parseInt(currentTime1)-1==-1){
                    currentTime1 = 23+"";
                }else{
                    currentTime1 = "0"+(Integer.parseInt(currentTime1)-1);
                }
                time = currentTime1+"00";
            }
        }else{
            if(Integer.parseInt(currentTime2) >40){
                time = currentTime1+"00";
            }else{
                if(Integer.parseInt(currentTime1)-1==-1){
                    currentTime1 = 23+"";
                }else{
                    currentTime1 = ""+(Integer.parseInt(currentTime1)-1);
                }
                time = currentTime1+"00";
            }
        }

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case 0:
//                if(grantResults[0] ==PackageManager.PERMISSION_GRANTED || grantResults[1] ==PackageManager.PERMISSION_DENIED){
//                    Toast.makeText(this, "이 앱의 내 위치 사용불가", Toast.LENGTH_SHORT).show();
//                }

                for(int i=0; i<grantResults.length; i++){
                    if(grantResults[i] == PackageManager.PERMISSION_DENIED){
                        Toast.makeText(getActivity(), "내 위치 사용불가", Toast.LENGTH_SHORT).show();
                        break;
                    }
                }

                break;
        }
    }

}
