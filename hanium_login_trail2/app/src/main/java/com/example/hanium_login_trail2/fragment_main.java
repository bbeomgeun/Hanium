package com.example.hanium_login_trail2;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

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
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        MapView mapView = new MapView(getActivity());
        RelativeLayout kakaoMap = (RelativeLayout) view.findViewById(R.id.mapView);
        kakaoMap.addView(mapView);
        //// db연동해서 위치 받아오기 + 카카오 전용 지오코더 이용하기.
        MapPoint mapPoint = MapPoint.mapPointWithGeoCoord(37.5514579595, 126.951949155);
        mapView.setMapCenterPoint(mapPoint, true);
        MapPOIItem marker = new MapPOIItem();
        marker.setItemName("Test");
        marker.setTag(0);
        marker.setMapPoint(mapPoint);
        marker.setMarkerType(MapPOIItem.MarkerType.BluePin); // 기본으로 제공하는 BluePin 마커 모양.
        marker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin); // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.
        mapView.addPOIItem(marker);
        /////
        ArrayList<String>list = new ArrayList<>();
        for(int i=0 ; i<5 ; i++){
            list.add(String.format("Name %d", i)) ;
        }
        ArrayList<String>list2 = new ArrayList<>();
        for(int i=0 ; i<5 ; i++){
            list2.add(String.format("Address %d", i)) ;
        }

        RecyclerView recyclerView = view.findViewById(R.id.RecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        ItemAdapter itemAdapter = new ItemAdapter(list, list2);
        recyclerView.setAdapter(itemAdapter);
        return view;
    }
}
