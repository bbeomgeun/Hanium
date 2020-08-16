package com.example.hanium_login_trail2;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import net.daum.mf.map.api.MapView;

public class fragment_main extends Fragment{
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        MapView mapView = new MapView(getActivity());
        RelativeLayout kakaoMap = (RelativeLayout) view.findViewById(R.id.mapView);
        kakaoMap.addView(mapView);

        return view;
    }

}
