package com.example.spotnow;

import android.location.LocationManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOverlay;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.naver.maps.map.LocationTrackingMode;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.MapView;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.UiSettings;
import com.naver.maps.map.util.FusedLocationSource;

public class HomeFragment extends Fragment implements OnMapReadyCallback {
    private MapFragment mapView; // MapView 객체 선언
    private Button myButton; // 버튼 객체 선언
    private EditText searchbar;

    //현 위치로 지정 부분
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1000;
    private FusedLocationSource locationSource;
    private NaverMap naverMap;



    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.home_fragment, container, false);

        FragmentManager fm = getFragmentManager();
        mapView = (MapFragment)fm.findFragmentById(R.id.map_fragment);
        if (mapView == null) {
            mapView = MapFragment.newInstance();
            fm.beginTransaction().add(R.id.map_fragment, mapView).commit();
        }

        mapView.getMapAsync(this);


        locationSource = new FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE);


        myButton = rootView.findViewById(R.id.reset_location_button);
        myButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 버튼 클릭 이벤트 처리
                naverMap.setLocationTrackingMode(LocationTrackingMode.Follow);

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

    public void onStart(){
        super.onStart();
    }

    public void onResume(){
        super.onResume();
    }

    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,  @NonNull int[] grantResults) {
        if (locationSource.onRequestPermissionsResult(
                requestCode, permissions, grantResults)) {
            if (!locationSource.isActivated()) { // 권한 거부됨
                naverMap.setLocationTrackingMode(LocationTrackingMode.None);
            }
            return;
        }
        super.onRequestPermissionsResult(
                requestCode, permissions, grantResults);
    }

    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {
        this.naverMap = naverMap;
        naverMap.setLocationSource(locationSource);
        naverMap.setLocationTrackingMode(LocationTrackingMode.Follow);
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
