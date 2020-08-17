package com.example.hanium_login_trail2;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        final MapView mapView = new MapView(getActivity());
        RelativeLayout kakaoMap = (RelativeLayout) view.findViewById(R.id.mapView);
        kakaoMap.addView(mapView);
        //// db연동해서 위치 받아오기 + 카카오 전용 지오코더 이용하기.

        /////
        Namelist = new ArrayList<>();
        for(int i=0 ; i<2 ; i++){
            Namelist.add(String.format("Name %d", i)) ;
        }
        AddressList = new ArrayList<>();
        for(int i=0 ; i<2 ; i++){
            AddressList.add(String.format("Address %d", i)) ;
        }
        ///테스트용
        Latitudes = new ArrayList<>();
        Latitudes.add(37.4500221);
        Latitudes.add(37.89);
        Longitudes = new ArrayList<>();
        Longitudes.add(126.65888);
        Longitudes.add(126.46);

        RecyclerView recyclerView = view.findViewById(R.id.RecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        ItemAdapter itemAdapter = new ItemAdapter(Namelist, AddressList);
        recyclerView.setAdapter(itemAdapter);
// 리싸이클러뷰 연결부분 + adapter로 데이터바인딩

        itemAdapter.setOnItemClickListener(new ItemAdapter.OnItemClickListener(){
            @Override
            public void onItemClick(View v, int position){
                Drawmarker(mapView, Latitudes.get(position), Longitudes.get(position), Namelist.get(position) );
            }
        });
        return view;
    }

    public void Drawmarker (MapView mapView, Double latitude, Double longitude, String name ) { // db에서 위,경도 바로 받아오자
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
}
