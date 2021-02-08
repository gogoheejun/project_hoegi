package com.hjhj.daedan;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
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
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
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
import static androidx.core.content.ContextCompat.getSystemService;
import static java.lang.Math.cos;
import static java.lang.Math.log;
import static java.lang.Math.pow;
import static java.lang.Math.tan;

public class Tab1_MapFragment extends Fragment implements
        GoogleMap.OnCameraMoveStartedListener,
        GoogleMap.OnCameraMoveListener,
        GoogleMap.OnCameraMoveCanceledListener,
        GoogleMap.OnCameraIdleListener,
        OnMapReadyCallback {
    GoogleMap gMap;
    MapView mapView;
    MarkerOptions myMarker = null;
    TextView tv_temperature;
    ImageView iv_weather;
    String weather, temperature;
    String date, time;
    String server;
    double lat, lon;

    ImageView iv_filter, iv_write;
    double bluedotLat, bluedotLon;

    LocationManager locationManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED) {
                String[] permissions = new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE};
                requestPermissions(permissions, 0);
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);


        View view = inflater.inflate(R.layout.page_map_tab1, container, false);

        tv_temperature = view.findViewById(R.id.page_map_tv_temperature);
        iv_weather = view.findViewById(R.id.page_map_iv_weather);

        getTime();
        getInfoAndShow();

        mapView = (MapView) view.findViewById(R.id.mappage_mapview);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        MapsInitializer.initialize(getActivity().getApplicationContext());
        mapView.getMapAsync(this);

        //--------------위에까진 지도부르는 기능
        iv_write = view.findViewById(R.id.tab1MapPage_write);
        iv_filter = view.findViewById(R.id.tab1MapPage_filter);
        View myLocationButton = ((View) mapView.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));//현재위치버튼(오른쪽위버튼) 객체가져오기


        iv_write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //현재 위치 위도경도 보내기. 그래야 작성해서 db에 저장할때 내용이랑 위치도 저장하니까
                myLocationButton.performClick();
                //todo:여기해야해. 지도이동시간 걸려서 현재위치 잘못가져옴;;
//                locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
//                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                    return;
//                }
//                //내 위치 실시간 받기
//                if(locationManager.isProviderEnabled("gps")){
//                    locationManager.requestLocationUpdates("gps",5000, 2,locationListener);//시간이 5초 지나거나 2미터가 지나면 갱신하겠다
//                }else if(locationManager.isProviderEnabled("network")){
//                    locationManager.requestLocationUpdates("network",5000,2,locationListener);
//                }


                //------------
                Intent intent = new Intent(getActivity(), Tab1_Map_WriteActivity.class);
                Bundle extra = new Bundle();
                intent.putExtra("lat", bluedotLat); //카메라 움직이면 알아서 저 아래 getLocation함수 실행되서 lat lon 구해짐
                intent.putExtra("lon", bluedotLon);
                startActivity(intent, extra);

            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        //내 위치 실시간 받기
        if (locationManager.isProviderEnabled("gps")) {
            locationManager.requestLocationUpdates("gps", 5000, 2, locationListener);//시간이 5초 지나거나 2미터가 지나면 갱신하겠다
        } else if (locationManager.isProviderEnabled("network")) {
            locationManager.requestLocationUpdates("network", 5000, 2, locationListener);
        }
    }

    LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(@NonNull Location location) {
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            if (locationManager.isProviderEnabled("gps")) {
                location = locationManager.getLastKnownLocation("gps");
            } else if (locationManager.isProviderEnabled("network")) {
                location = locationManager.getLastKnownLocation("network");
            }
            bluedotLat = location.getLatitude();
            bluedotLon = location.getLongitude();
            Log.d("gotit", "locationlistenr:" + bluedotLat + " " + bluedotLon);

        }
    };


    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d("MOVE", "map ready");
        gMap = googleMap;
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        UiSettings settings = gMap.getUiSettings();
        settings.setMyLocationButtonEnabled(true);
        gMap.setMyLocationEnabled(true);

        //----------지도준비됨


        LatLng seoul = new LatLng(37.562087, 127.035192);
//                gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(seoul, 15));//줌 1~25
//                gMap.addMarker(new MarkerOptions().position(seoul).title("Title").snippet("Marker Description"));

//                CameraPosition cameraPosition = new CameraPosition.Builder().target(seoul).zoom(12).build();
//                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        CameraPosition cameraPosition = new CameraPosition.Builder().target(seoul).zoom(12).build();
        gMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        //아래 두줄이 신의 한수.ㅠㅠㅠ감격
        gMap.setOnCameraMoveListener(this);
        gMap.setOnCameraMoveStartedListener(this);
    }


    @Override
    public void onCameraIdle() {

    }

    @Override
    public void onCameraMoveCanceled() {

    }

    @Override
    public void onCameraMove() {
//        Log.d("MOVE","moving anyway");//너무 많이 함수불러짐...좀만 움직여도 몇십번 작동..
    }

    float x, y;//변환된 좌표
    int intX, intY; //변환된 좌표를 반올림한 정수형 좌표

    @Override
    public void onCameraMoveStarted(int reason) {
        Log.d("MOVE", "move started1");
        getLocation();
        getInfoAndShow();


        if (reason == GoogleMap.OnCameraMoveStartedListener.REASON_GESTURE) {
//            Log.d("MOVE","The user gestured on the map.");
        } else if (reason == GoogleMap.OnCameraMoveStartedListener.REASON_API_ANIMATION) {
//            Log.d("MOVE","The user tapped something on the map.");
        } else if (reason == GoogleMap.OnCameraMoveStartedListener.REASON_DEVELOPER_ANIMATION) {
//            Log.d("MOVE","The app moved the camera.");
        }
    }

    //onCameraMoveStarted안에 들어갈 현재위치 설정해주는 함수
    public void getLocation() {
        gMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                lat = gMap.getCameraPosition().target.latitude;
                lon = gMap.getCameraPosition().target.longitude;
//                Log.e("POSITION", gMap.getCameraPosition().target.latitude+"");
                transNum(); //api형식의 좌표로 바꿔서 검색해서 표시
            }
        });
    }

    //좌표단위변환함수
    public void transNum() {

        double Re = 6371.00877; // 지도반경
        double grid = 5.0; // 격자간격 (km)
        double slat1 = 30.0; // 표준위도 1
        double slat2 = 60.0; // 표준위도 2
        double olon = 126.0; // 기준점 경도
        double olat = 38.0; // 기준점 위도
        double xo = 210 / grid; // 기준점 X좌표
        double yo = 675 / grid; // 기준점 Y좌표
        double first = 0;
        float lon1, lat1; /* Longitude, Latitude [degree] */

        double PI, DEGRAD, RADDEG;
        double re, sn, sf, ro;
        double alon, alat, xn, yn, ra, theta;
        PI = Math.asin(1.0) * 2.0;
        DEGRAD = PI / 180.0;
        RADDEG = 180.0 / PI;

        re = Re / grid;
        slat1 = slat1 * DEGRAD;
        slat2 = slat2 * DEGRAD;
        olon = olon * DEGRAD;
        olat = olat * DEGRAD;

        sn = tan(PI * 0.25 + slat2 * 0.5) / tan(PI * 0.25 + slat1 * 0.5);
        sn = log(cos(slat1) / cos(slat2)) / log(sn);
        sf = tan(PI * 0.25 + slat1 * 0.5);
        sf = pow(sf, sn) * cos(slat1) / sn;
        ro = tan(PI * 0.25 + olat * 0.5);
        ro = re * sf / pow(ro, sn);
        first = 1;

        //---
        ra = tan(PI * 0.25 + (lat) * DEGRAD * 0.5); //여기서 lat이랑 lon들어가서 바꿔줌
        ra = re * sf / pow(ra, sn);
        theta = (lon) * DEGRAD - olon;
        if (theta > PI) theta -= 2.0 * PI;
        if (theta < -PI) theta += 2.0 * PI;
        theta *= sn;
        x = (float) ((float) (ra * Math.sin(theta)) + xo);
        intX = Math.round(x);
        y = (float) ((float) (ro - ra * cos(theta)) + yo);
        intY = Math.round(y);
        Log.d("TRANS", intX + " &" + intY);
    }


    //온도랑 날씨데이터 가져와서 화면의 글씨수정하는 함수
    private void getInfoAndShow() {
        String api = "ZkmHEAVaoQ0yFwBbRFQSMJkOhXX%2FMQzcTpYDB0Q513dcVb3Vuz6vCU7QSEPdyYs0A3aOUSDG2WuzVo%2BQDF4beQ%3D%3D";

        String url1 = "http://apis.data.go.kr/1360000/VilageFcstInfoService/getUltraSrtNcst?serviceKey=";
        String url2 = "&numOfRows=100&pageNo=1&dataType=XML&base_date=";
//        if(x<1) x=55;
//        if(y<1) y=127;
        server = url1 + api + url2 + date + "&base_time=" + time + "&nx=" + intX + "&ny=" + intY;
        //todo: 현재 좌표 가져와서 변환해서 넣어야함

        new Thread() {
            @Override
            public void run() {
                super.run();

                try {
                    Log.e("Test", "work0");
                    URL url = new URL(server);
                    Log.e("Test", "work1");
                    InputStream is = url.openStream();
                    Log.e("Test", "work2");
                    InputStreamReader isr = new InputStreamReader(is);
                    Log.e("Test", "work3");
                    XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                    XmlPullParser xpp = factory.newPullParser();
                    xpp.setInput(isr);

                    int eventType = xpp.getEventType();

                    while (eventType != XmlPullParser.END_DOCUMENT) {
                        switch (eventType) {
                            case XmlPullParser.START_DOCUMENT:
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
//                                        Toast.makeText(getActivity(), "parse start!", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                break;
                            case XmlPullParser.START_TAG:
                                String tagName = xpp.getName();
                                if (tagName.equals("category")) {
                                    xpp.next();
                                    if (xpp.getText().equals("T1H")) {
                                        for (int i = 0; i < 9; i++) {
                                            xpp.next();
                                        }
                                        temperature = xpp.getText();
                                    }
                                    if (xpp.getText().equals("PTY")) {
                                        for (int i = 0; i < 9; i++) {
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
                            try {
                                if (temperature == null) temperature = "-";
                                tv_temperature.setText(temperature + "도");
                            } catch (Exception e) {
                                temperature = "-";
                                tv_temperature.setText(temperature + "도");
                            }

//                            Log.d("TEMP",temperature);
                            //- 강수형태(PTY) 코드 : 없음(0), 비(1), 비/눈(2), 눈(3), 소나기(4), 빗방울(5), 빗방울/눈날림(6), 눈날림(7)
                            try {
                                switch (weather) {
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
                                    default:
                                        Glide.with(getActivity()).load(R.drawable.icon_earth).into(iv_weather);
                                        break;

                                }
                            } catch (Exception e) {
                                Glide.with(getActivity()).load(R.drawable.icon_earth).into(iv_weather);
                                Log.e("ERROR", "에러쓰");
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

    //api주소안에 넣을 시간가져오는 함수
    private void getTime() {
        TimeZone zone = TimeZone.getTimeZone("Asia/Seoul");  // TimeZone에 표준시 설정
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd", Locale.KOREAN);
        dateFormat.setTimeZone(zone);                    //DateFormat에 TimeZone 설정

        DateFormat timeFormat = new SimpleDateFormat("HHmm", Locale.KOREAN);
        timeFormat.setTimeZone(zone);

        date = dateFormat.format(new Date());
        String currentTime = timeFormat.format(new Date());
        //t시 40분이 넘으면 t시, 안넘으면 t-1시..단 t-1이 -1이 되면 23시로.
        String currentTime1 = currentTime.substring(0, 2);
        String currentTime2 = currentTime.substring(2);

        if (Integer.parseInt(currentTime1) < 11) {
            if (Integer.parseInt(currentTime2) > 40) {
                time = currentTime1 + "00";
            } else {
                if (Integer.parseInt(currentTime1) - 1 == -1) {
                    currentTime1 = 23 + "";
                } else {
                    currentTime1 = "0" + (Integer.parseInt(currentTime1) - 1);
                }
                time = currentTime1 + "00";
            }
        } else {
            if (Integer.parseInt(currentTime2) > 40) {
                time = currentTime1 + "00";
            } else {
                if (Integer.parseInt(currentTime1) - 1 == -1) {
                    currentTime1 = 23 + "";
                } else {
                    currentTime1 = "" + (Integer.parseInt(currentTime1) - 1);
                }
                time = currentTime1 + "00";
            }
        }

    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 0:
//                if(grantResults[0] ==PackageManager.PERMISSION_GRANTED || grantResults[1] ==PackageManager.PERMISSION_DENIED){
//                    Toast.makeText(this, "이 앱의 내 위치 사용불가", Toast.LENGTH_SHORT).show();
//                }

                for (int i = 0; i < grantResults.length; i++) {
                    if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                        Toast.makeText(getActivity(), "내 위치 사용불가", Toast.LENGTH_SHORT).show();
                        break;
                    }
                }

                break;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        locationManager.removeUpdates(locationListener);
        Log.d("gotit", "locationManager stopped");

    }
}
