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

import com.example.spotnow.MainActivity;
import com.example.spotnow.SearchActivity;
import com.example.spotnow.ownerActivitymodify;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelSlideListener;
import com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelState;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.slidingpanelayout.widget.SlidingPaneLayout;

import com.example.spotnow.ActivityInfo;
import com.example.spotnow.user_listview_info;
import com.example.spotnow.R;
import com.example.spotnow.SpotInfo;
import com.example.spotnow.activity_listview_info;
import com.example.spotnow.common.Coordinate;
import com.example.spotnow.common.FirebaseManager;
import com.example.spotnow.common.MarkerInfo;
import com.example.spotnow.common.Utility;
import com.example.spotnow.ownerActivity;
import com.example.spotnow.participantFragment;
import com.example.spotnow.spot_listview_info;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

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
    private TextView searchbar;
    private Button plusButton;

    //현 위치로 지정 부분
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1000;
    private FusedLocationSource locationSource;
    private NaverMap naverMap;

    //액티비티 리스트뷰 관련
    ArrayList<activitySampleData> sampleDataList;
    ArrayList<activity_listview_info> activityDataList;
    activity_listview_adapter myAdapter;
    private SlidingUpPanelLayout slidingUp;

    public long clickedSpotID;
    private DatabaseReference mDatabase;
    public String output;

    public HomeFragment() {
        // Required empty public constructor
    }
    private FirebaseAuth mAuth;
    private String currentUserId = null;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.home_fragment, container, false);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        ArrayList<String> path = new ArrayList<>();
        path.add(currentUser.getUid());
        path.add("name");

        FirebaseManager.GetReferencePath("users", path).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                } else {
                    Log.d("firebase", String.valueOf(task.getResult().getValue()));
                    currentUserId = String.valueOf(task.getResult().getValue());

                }
            }
        });

        // 주소 문자열로 가져오는 방법
        String result = Utility.GetAddressFromGPS(getContext(),Utility.GetGPS(getContext()));
        TextView activity_address = rootView.findViewById(R.id.location_textview);
        String input = result;
        //현위치의 주소 앞 뒤 적당히 자름
        int spaceCount = 0;
        int startIndex = -1;
        int endIndex = -1;

        for (int i = 0; i < input.length(); i++) {
            if (input.charAt(i) == ' ') {
                spaceCount++;
                if (spaceCount == 1) {
                    startIndex = i + 1; // 첫 번째 띄어쓰기 이후부터 시작
                } else if (spaceCount == 5) {
                    endIndex = i;
                    break;
                }
            }
        }

        if (startIndex != -1 && endIndex != -1) {
            output = input.substring(startIndex, endIndex);
            activity_address.setText(output);
        }
//        activity_address.setText(result);

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
                activity_address.setText(output);
                naverMap.setLocationTrackingMode(LocationTrackingMode.Follow);

            }
        });


        searchbar = rootView.findViewById(R.id.search_bar);

        searchbar.setOnClickListener(new View.OnClickListener() { // 검색 액티비티로 이동
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SearchActivity.class);
                getActivity().startActivityForResult(intent,1004);
            }
        });




        slidingUp=rootView.findViewById(R.id.main_panel);
        slidingUp.setPanelState(PanelState.COLLAPSED);


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
                        activityDataList.add(new activity_listview_info(activityInfo.getImageUrl(), activityInfo.getTitle(),activityInfo.getContent() ,activityInfo.getActivityOwner() ));
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

                // 마커 클릭시 모달창 위치 텍스트를 스팟이름으로 바꿔줌
                View v = HomeFragment.this.getView();
                TextView activity_address = v.findViewById(R.id.location_textview);
                activity_address.setText(m.spotName);
                clickedSpotID = m.spotID;

                getActivityList(clickedSpotID);

                // 리스트뷰 또한 클릭된 스팟에 존재하는 액티비티로 띄워야함
                ListView listView = (ListView) v.findViewById(R.id.activity_listview);

                myAdapter = new activity_listview_adapter(getActivity(), activityDataList);

                listView.setAdapter(myAdapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        // 클릭한 액티비티 정보 가져오기
                        activity_listview_info selectedItem = myAdapter.getItem(position);

                        String activityOwnerId = selectedItem.getActivityOwner();
                        Class<?> activityClass = ownerActivitymodify.class;

                        if (!currentUserId.equals(activityOwnerId)) {
                            activityClass = participantFragment.class;
                        }

                        // Use the selected activityClass in the intent
                        Intent intent = new Intent(getActivity(), activityClass);
                        intent.putExtra("activityTitle", selectedItem.getActivityTitle());
                        intent.putExtra("activityContent", selectedItem.getActivityContent());
                        startActivity(intent);
                    }
                });

                slidingUp = v.findViewById(R.id.main_panel);
                slidingUp.setPanelState(PanelState.EXPANDED);

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
