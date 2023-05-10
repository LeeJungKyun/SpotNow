package com.example.spotnow;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;


public class HomeFragment extends Fragment {

    private MapView mapView; // MapView 객체 선언
    private Button myButton; // 버튼 객체 선언
    private LocationManager locationManager;
    private EditText searchbar;
    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.home_fragment, container, false);

        ViewGroup mapViewContainer = rootView.findViewById(R.id.map_view);

        if (mapView != null) {
            // 기존에 생성된 MapView 객체가 있다면 제거
            mapViewContainer.removeView(mapView);
        }

        // 새로운 MapView 객체 생성
        mapView = new MapView(getContext());
        mapViewContainer.addView(mapView);

        myButton = rootView.findViewById(R.id.reset_location_button);
        myButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 버튼 클릭 이벤트 처리
                Toast.makeText(getContext(), "버튼이 클릭되었습니다.", Toast.LENGTH_SHORT).show();

            }
        });


        searchbar=rootView.findViewById(R.id.search_bar);
        searchbar.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                //검색창에서 엔터키 눌렀을 때 이벤트 처리
                if (i == KeyEvent.KEYCODE_ENTER) {
                    String searchWord= String.valueOf(searchbar.getText());
                    Toast.makeText(getContext(),  searchWord, Toast.LENGTH_SHORT).show();

                    return true;
                }
                return false;
            }
        });



        return rootView;
    }


//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//
//        // Fragment1이 소멸될 때 MapView 객체도 함께 소멸시킴
//        if (mapView != null) {
//            mapView.onDestroy();
//            mapView = null;
//        }
//    }
}
