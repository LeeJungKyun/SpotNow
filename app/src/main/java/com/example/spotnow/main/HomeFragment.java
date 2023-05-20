package com.example.spotnow.main;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.spotnow.ActivityInfo;
import com.example.spotnow.R;
import com.example.spotnow.SpotInfo;
import com.example.spotnow.activity_listview_info;
import com.example.spotnow.common.Coordinate;
import com.example.spotnow.common.FirebaseManager;
import com.example.spotnow.common.MarkerInfo;
import com.example.spotnow.common.Utility;
import com.example.spotnow.ownerActivity;
import com.example.spotnow.spot_listview_info;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.LocationTrackingMode;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.overlay.Overlay;
import com.naver.maps.map.util.FusedLocationSource;
import com.naver.maps.map.util.MarkerIcons;
import com.example.spotnow.activitySampleData;
import com.example.spotnow.activity_listview_adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HomeFragment extends Fragment implements OnMapReadyCallback {
    private MapFragment mapView; // MapView 객체 선언
    private Button resetLocationButton; // 버튼 객체 선언
    private EditText searchbar;
    private Button plusButton;

    //현 위치로 지정 부분
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1000;
    private FusedLocationSource locationSource;
    private NaverMap naverMap;

    //액티비티 리스트뷰 관련
    ArrayList<activitySampleData> sampleDataList;
    ArrayList<activity_listview_info> activityDataList;
    activity_listview_adapter myAdapter;

    public long clickedSpotID;
    private DatabaseReference mDatabase;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.home_fragment, container, false);

        // 주소 문자열로 가져오는 방법
        String result = Utility.GetAddressFromGPS(getContext(),Utility.GetGPS(getContext()));
        TextView activity_address = rootView.findViewById(R.id.location_textview);
        activity_address.setText(result);

        //로그인 안하고 디버깅할때 해야함소
        FirebaseManager.init();

        FragmentManager fm = getFragmentManager();
        mapView = (MapFragment) fm.findFragmentById(R.id.map_fragment);
        if (mapView == null) {
            mapView = MapFragment.newInstance();
            fm.beginTransaction().add(R.id.map_fragment, mapView).commit();
        } else
            fm.beginTransaction().replace(R.id.map_fragment, mapView).commit();
        mapView.getMapAsync(this);
        locationSource = new FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE);


        resetLocationButton = rootView.findViewById(R.id.reset_location_button);
        resetLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 버튼 클릭 이벤트 처리
                v = HomeFragment.this.getView();
                TextView activity_address = v.findViewById(R.id.location_textview);
                activity_address.setText(result);
                naverMap.setLocationTrackingMode(LocationTrackingMode.Follow);

            }
        });


        searchbar = rootView.findViewById(R.id.search_bar);
        searchbar.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                //검색창에서 엔터키 눌렀을 때 이벤트 처리
                if (i == KeyEvent.KEYCODE_ENTER) {
                    String searchWord = String.valueOf(searchbar.getText());
                    Toast.makeText(getContext(), searchWord, Toast.LENGTH_SHORT).show();

                    return true;
                }
                return false;
            }
        });

//        this.InitializeActivityData();


        //모달창에서 플러스 버튼 눌러서 액티비티 생성하기
        plusButton = rootView.findViewById(R.id.createActivity_button);
        plusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //액티비티 생성 버튼을 누르는 동시에 spotID를 intent에 담아 ownerAcitivity에 넘겨줌
                Intent intent = new Intent(getActivity(), ownerActivity.class);
                intent.putExtra("spotID", clickedSpotID);
                startActivity(intent);
            }
        });


        return rootView;
    }

//    public void InitializeActivityData() {
//        sampleDataList = new ArrayList<activitySampleData>();
//        sampleDataList.add(new activitySampleData(R.drawable.circle, "소웨랑 농구 뜰 사람 구함1", "가천대 운동장으로 집합"));
//        sampleDataList.add(new activitySampleData(R.drawable.circle, "소웨랑 농구 뜰 사람 구함2", "가천대 운동장으로 집합"));
//        sampleDataList.add(new activitySampleData(R.drawable.circle, "소웨랑 농구 뜰 사람 구함3", "가천대 운동장으로 집합"));
//
//    }

    public void getActivityList(long spotID){
        mDatabase = FirebaseDatabase.getInstance().getReference("activities");
        activityDataList=new ArrayList<activity_listview_info>();

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    ActivityInfo activityInfo = snapshot.getValue(ActivityInfo.class);
                    if(activityInfo.getSpotID()==(spotID))
                    {
                        activityDataList.add(new activity_listview_info(activityInfo.getImageUrl(), activityInfo.getTitle(),activityInfo.getContent()));
                    }

                }

                myAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void onStart() {
        super.onStart();
    }

    public void onResume() {

        super.onResume();
    }

    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
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
        initMap();
    }

    public void initMap() {
        naverMap.setLocationSource(locationSource);
        naverMap.setLocationTrackingMode(LocationTrackingMode.Follow);

        ArrayList<String> path = new ArrayList<String>();
        DatabaseReference dr = FirebaseManager.GetReferencePath("spots", null);


        HashMap<String, Object> spotResult = new HashMap<>();
        dr.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                } else {
                    spotResult.putAll((HashMap<String, Object>) task.getResult().getValue());
                    Log.d("firebase", spotResult.toString());

                    for (Map.Entry<String, Object> entry : spotResult.entrySet()) {
                        MarkerInfo marker = new MarkerInfo((String) entry.getKey());

                        Map<String, Object> value = (Map<String, Object>) entry.getValue();
                        marker.location = new Coordinate((double) value.get("latitude"),(double) value.get("longitude"));
                        marker.spotID = (long) value.get("spotID");

                        setMark(marker);
                    }
                }
            }
        });

    }

    public void setMark(MarkerInfo m) {
        Marker marker = new Marker();
        marker.setPosition(new LatLng(m.location._latitude, m.location._longitude));
        marker.setIcon(MarkerIcons.RED);
        marker.setWidth(50);
        marker.setHeight(60);
        marker.setMap(naverMap);

        marker.setOnClickListener(new Overlay.OnClickListener() {
            @Override
            public boolean onClick(@NonNull Overlay overlay) {

                //마커 클릭시 모달창 위치 텍스트를 스팟이름으로 바꿔줌
                View v = HomeFragment.this.getView();
                TextView activity_address = v.findViewById(R.id.location_textview);
                activity_address.setText(m.spotName);
                clickedSpotID = m.spotID;

                getActivityList(clickedSpotID);
                //리스트뷰 또한 클릭된 스팟에 존재하는 액티비티로 띄워야함..
                ListView listView = (ListView) v.findViewById(R.id.activity_listview);

                myAdapter = new activity_listview_adapter(getActivity(), activityDataList);

                listView.setAdapter(myAdapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView parent, View v, int position, long id) {

                        //여기 부분에 액티비티 리스트 아이템 클릭했을 때 그 액티비티 창 뜨도록 해야함

                        Toast.makeText(getActivity(),
                                myAdapter.getItem(position).getActivityTitle(),
                                Toast.LENGTH_LONG).show();
                    }
                });



                Toast.makeText(getContext(), m.spotName + " Marker click!" + "spotID:" + clickedSpotID, Toast.LENGTH_SHORT).show();


                return true;
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        // Fragment1이 소멸될 때 MapView 객체도 함께 소멸시킴
        if (mapView != null) {
            mapView.onDestroy();
            mapView = null;

            Fragment fragment = getFragmentManager().findFragmentById(R.id.map_fragment);
            if (fragment != null)
                getFragmentManager().beginTransaction().remove(fragment).commit();
        }
    }
}
