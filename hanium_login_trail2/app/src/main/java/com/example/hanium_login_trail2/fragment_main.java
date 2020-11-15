package com.example.hanium_login_trail2;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import net.daum.mf.map.api.MapCircle;
import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import java.util.ArrayList;

public class fragment_main extends Fragment implements LocationListener {
    final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;

    ArrayList<String> Namelist;
    ArrayList<String> AddressList;

    ///테스트용 - 실제는 DB에서 가져올 것이다.
    ArrayList<Double> Latitudes;
    ArrayList<Double> Longitudes;
    ///
    LocationManager lm;
    MyReceiver receiver;
    PendingIntent pintent;
    Intent intent;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        final MapView mapView = new MapView(getActivity());
        RelativeLayout kakaoMap = (RelativeLayout) view.findViewById(R.id.mapView);
        kakaoMap.addView(mapView);
        //// db연동해서 위치 받아오기 + 카카오 전용 지오코더 이용하기.

        // 근접 경보 코드
        lm = (LocationManager) getActivity().getSystemService(getActivity().LOCATION_SERVICE);
        // manifest에 등록하지않고 코드상으로 브로드캐스트 리시버 등록작업.
        myPermissionCheck();
        receiverMaker();
        //

        Namelist = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            Namelist.add(String.format("Name %d", i));
        }
        AddressList = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            AddressList.add(String.format("Address %d", i));
        }
        ///테스트용
        Latitudes = new ArrayList<>();
        Latitudes.add(37.4500221);
        Latitudes.add(37.89);
        Longitudes = new ArrayList<>();
        Longitudes.add(126.65888);
        Longitudes.add(126.46);
        // 테스트 lm등록

        RecyclerView recyclerView = view.findViewById(R.id.RecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        ItemAdapter itemAdapter = new ItemAdapter(Namelist, AddressList);
        recyclerView.setAdapter(itemAdapter);
// 리싸이클러뷰 연결부분 + adapter로 데이터바인딩

        itemAdapter.setOnItemClickListener(new ItemAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Drawmarker(mapView, Latitudes.get(position), Longitudes.get(position), Namelist.get(position));
                DrawBoundary(mapView, Latitudes.get(position), Longitudes.get(position), 200); // 현재 임시로 인하대학교 근처 200m로
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
        // 약자 주거지 근처로 원 그려주는 함수 & lm에 접근경보도 걸어준다
        // 테스트로 함수로 묶어놨지만 나중에는 locationManager에 등록해주는 것은 바깥에서 해줘야 할 것
        // 지금은 itemClick시 원 그려지고 경보 등록인데 경보는 항상 등록되어있어야 굳이 확인하지 않아도 24시간 알람이 울린다.

        MapCircle circle = new MapCircle(
                MapPoint.mapPointWithGeoCoord(latitude, longitude),
                radius,
                Color.argb(128, 255, 0, 0),
                Color.argb(128, 0, 255, 0)
        );
        circle.setTag(1);
        mapview.addCircle(circle);
        //locationManager 연결해주는 코드
    }

    public void receiverMaker() {

        //리시버 등록 코드
        receiver = new MyReceiver();
        IntentFilter filter = new IntentFilter("beomGeun");
        getActivity().registerReceiver(receiver, filter);

// 인텐트의 액션 정보 정의 - 목표지점을 등록할 때 사용하는 인텐트를 브로드캐스트 수신자에서
// 받아 처리할 수 있어야 하므로 전송될 인텐트와 수신을 위한 인텐트 필터에 동일한 액션 정보(키값)-beomgeun 를 정의함
        intent = new Intent("beomGeun");
        try {
                pintent = PendingIntent.getBroadcast(getActivity(),
                        0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

                lm.addProximityAlert(37.4500221,126.65888
                        , 100, 10000, pintent);
            //2) 인텐트와 펜딩인텐트를 이용한 목표지점 추가 - 인텐트를 생성하고 목표지점의 위도, 경도와 같은 정보를
// 추가하면 이를 이용해 브로드캐스팅을 위한 펜딩인텐트로 만들 수 있다.
// addProximityAlert()메소드를 이용해 목표지점을 추가할 때 좌표값과 펜딩 인텐트를 파라미터로전달

        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    public void myPermissionCheck() {
        //런타임 퍼미션 체크
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // 퍼미션에 대한 설명을 해줘야하니? - 네
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                //다이어로그를 사용하여 설명해주기
            } else {
                //퍼미션에 대한 설명 필요없으면, 바로 권한 부여
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
            }
        } else {
            //허용되었을 때
            try {
                lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, (LocationListener) lm);
            } catch (SecurityException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onLocationChanged(Location location) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onProviderDisabled(String provider) {
    }
}
