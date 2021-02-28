package com.hjhj.daedan;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class PushService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        Log.d("TAG","onMessageRevcieved....");

        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = null;
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel("ch1", "push ch1", NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);

            builder = new NotificationCompat.Builder(this,"ch1");
        }else {
            builder = new NotificationCompat.Builder(this, null);
        }

        //파라미터로 전달된 remoteMessage에 있는정보 얻어
        remoteMessage.getFrom(); //메세지 보낸 기기명[firebase서버에서 자동지정한 이름]

        String notiTitle = "title"; //원격메세지에 정보없을때를 대비한 디폴트값정함
        String notiText = "message";

        if(remoteMessage.getNotification() !=null){
//            notiTitle = remoteMessage.getNotification().getTitle();
//            notiText = remoteMessage.getNotification().getBody();

        }
        notiTitle = remoteMessage.getData().get("name");
        notiText = remoteMessage.getData().get("msg");
        //알림설정들
        builder.setSmallIcon(R.drawable.ic_fcm);
        builder.setContentTitle(notiTitle);
        builder.setContentText(notiText);
        builder.setAutoCancel(true);


        //알림말고 추가옵션으로 데이터보내서 받아온거:[키:벨류]쌍으로 전달되어옴
        Map<String, String> data = remoteMessage.getData();
        if (data != null){
            String name = data.get("name");
            String message = data.get("msg");

            Intent intent = new Intent(this, LoginActivity.class); //나중에 채팅방으로 만들자..일단은 런쳐액티비티로 보냄
            intent.putExtra("name",name);
            intent.putExtra("msg",message);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//노티피케이션은 이거 안해도되긴함 걍 한거
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);//똑같은액티비티 또 뜰까봐..이게 중요

            PendingIntent pendingIntent = PendingIntent.getActivity(this, 100, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentIntent(pendingIntent);

        }



        //알림창 선택햇을때 실행될 곳으로 보낼인텐트

        //알림매니저를 통해 알림공지
        notificationManager.notify(11,builder.build());


        //배터리문제있을수 있어서 포어그라운드로 실행하기
//        Notification notification = builder.build();
//        startForeground(200, notification);
    }
}



















