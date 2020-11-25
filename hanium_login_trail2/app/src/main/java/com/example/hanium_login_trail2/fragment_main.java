package com.example.hanium_login_trail2;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import net.daum.android.map.coord.MapCoordLatLng;
import net.daum.mf.map.api.MapCircle;
import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import java.util.ArrayList;

public class fragment_main extends Fragment{

    ArrayList<String> Namelist;
    ArrayList<String> AddressList;

    ///테스트용 - 실제는 DB에서 가져올 것이다.
    ArrayList<Double> Latitudes;
    ArrayList<Double> Longitudes;
    ///
    ArrayList<MapCoordLatLng> LatLngs;
    ///

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        final MapView mapView = new MapView(getActivity());
        RelativeLayout kakaoMap = (RelativeLayout) view.findViewById(R.id.mapView);
        kakaoMap.addView(mapView);
        //// db연동해서 위치 받아오기 + 카카오 전용 지오코더 이용하기.

        // 근접 경보 코드

        Namelist = new ArrayList<>();
//        for (int i = 0; i < 2; i++) {
//            Namelist.add(String.format("Name %d", i));
//        }
        Namelist.add("임송일");
        Namelist.add("박범근");
        AddressList = new ArrayList<>();
//        for (int i = 0; i < 2; i++) {
//            AddressList.add(String.format("Address %d", i));
//        }
        AddressList.add(("인천광역시 용현 1.4동 177-21"));
        AddressList.add(("인천광역시 인하로 47번길 87"));
        ///테스트용
        Latitudes = new ArrayList<>();
        Latitudes.add(37.45315);
        Latitudes.add(37.45831);
        Longitudes = new ArrayList<>();
        Longitudes.add(126.66043);
        Longitudes.add(126.65432);

        final MapCoordLatLng target_latlng = new MapCoordLatLng(37.4500221, 126.65888); // 임시 고정위치 (인하대학교)

        RecyclerView recyclerView = view.findViewById(R.id.RecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        ItemAdapter itemAdapter = new ItemAdapter(Namelist, AddressList);
        recyclerView.setAdapter(itemAdapter);
// 리싸이클러뷰 연결부분 + adapter로 데이터바인딩

        itemAdapter.setOnItemClickListener(new ItemAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Drawmarker(mapView, Latitudes.get(position), Longitudes.get(position), Namelist.get(position));
                DrawBoundary(mapView, target_latlng.getLatitude(), target_latlng.getLongitude(), 1000); // 현재 임시로 인하대학교 근처 200m로
                MapCoordLatLng latlng = new MapCoordLatLng(Latitudes.get(position), Longitudes.get(position)); // 클릭한 환자의 위치
                inOutCheck(target_latlng, latlng, 1000); // 거리 계산 후 결과 return
            }
        });
        return view;
    }

    public void Drawmarker(MapView mapView, Double latitude, Double longitude, String name) { // db에서 위,경도 바로 받아오자
        mapView.clearAnimation();
        MapPoint mapPoint = MapPoint.mapPointWithGeoCoord(latitude, longitude);
        mapView.setMapCenterPoint(mapPoint, true);
        MapPOIItem marker = new MapPOIItem();
        marker.setItemName(name);
        marker.setTag(0);
        marker.setMapPoint(mapPoint);
        marker.setMarkerType(MapPOIItem.MarkerType.BluePin); // 기본으로 제공하는 BluePin 마커 모양.
        marker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin); // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.
        mapView.addPOIItem(marker);
    }

    public void DrawBoundary(MapView mapview, Double latitude, Double longitude, int radius) {
        // 약자 주거지 근처로 원 그려주는 함수
        // 지금은 itemClick시 원 그려지고 경보 등록인데 경보는 항상 등록되어있어야 굳이 확인하지 않아도 24시간 알람이 울린다.

        MapCircle circle = new MapCircle(
                MapPoint.mapPointWithGeoCoord(latitude, longitude),
                radius,
                Color.argb(128, 255, 0, 0),
                Color.argb(128, 0, 255, 0)
        );
        circle.setTag(1);
        mapview.addCircle(circle);
    }


    //TODO : 거리계산 함수랑 inoutCheck 함수 쓰레드로 옮기기. or 클래스로 따로 만들기 + 데이터를 어떻게 쓰레드로 가져올지 생각하기.
    private double calculateDistance(MapCoordLatLng target, MapCoordLatLng weakPosition){
        double R = 6371e3; // metres
        double φ1 = Math.PI * target.getLatitude() / 180;
        double φ2 = Math.PI * weakPosition.getLatitude() / 180;
        double Δφ = φ2 - φ1;
        double Δλ = Math.PI * (target.getLongitude() - weakPosition.getLongitude()) / 180;
        double a = Math.sin(Δφ / 2) * Math.sin(Δφ / 2) + Math.cos(φ1) * Math.cos(φ2) * Math.sin(Δλ / 2) * Math.sin(Δλ / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        double d = R * c;
        return d;
    }

    public void inOutCheck(MapCoordLatLng target, MapCoordLatLng weakPosition, double radius){
        if(radius < calculateDistance(target, weakPosition)){
            Toast toast = Toast.makeText(getActivity(), "나갔어요", Toast.LENGTH_LONG);
            toast.show();
            NotificationManager notificationManager=(NotificationManager)getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationCompat.Builder builder =  null;
            if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.O) {
                String channelID = "channel_01";
                String channelName = "MyChannel01";

                NotificationChannel channel = new NotificationChannel(channelID, channelName, NotificationManager.IMPORTANCE_DEFAULT);
                notificationManager.createNotificationChannel(channel);
                builder = new NotificationCompat.Builder(getActivity(), channelID);
            } else{
                builder = new NotificationCompat.Builder(getActivity(), null);
            }

            builder.setSmallIcon(R.drawable.round_border);
            builder.setContentTitle("약자 위치 알람");
            builder.setContentText("지정하신 거주지를 이탈 하였습니다. 눌러서 확인");
            builder.setAutoCancel(true);
            Notification notification = builder.build();

            notificationManager.notify(1, notification);
        }
        else{
            Toast toast = Toast.makeText(getActivity(), "안에 있어요", Toast.LENGTH_LONG);
            toast.show();
            NotificationManager notificationManager=(NotificationManager)getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationCompat.Builder builder =  null;
            if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.O) {
                String channelID = "channel_01";
                String channelName = "MyChannel01";

                NotificationChannel channel = new NotificationChannel(channelID, channelName, NotificationManager.IMPORTANCE_DEFAULT);
                notificationManager.createNotificationChannel(channel);
                builder = new NotificationCompat.Builder(getActivity(), channelID);
            } else{
                builder = new NotificationCompat.Builder(getActivity(), null);
            }

            builder.setSmallIcon(R.drawable.round_border);
            builder.setContentTitle("약자 위치 알람");
            builder.setContentText("지정하신 거주지안에 들어왔습니다. 눌러서 확인");
            builder.setAutoCancel(true);
            Notification notification = builder.build();

            notificationManager.notify(1, notification);
        }
    }
}
