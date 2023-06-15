package com.example.spotnow.main;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.spotnow.R;
import com.example.spotnow.activity.ActivityInfo;
import com.example.spotnow.activity.activitySampleData;
import com.example.spotnow.activity.activity_listview_adapter;
import com.example.spotnow.activity.activity_listview_info;
import com.example.spotnow.activity.ownerActivity;
import com.example.spotnow.activity.ownerActivitymodify;
import com.example.spotnow.activity.participantFragment;
import com.example.spotnow.common.Coordinate;
import com.example.spotnow.common.FirebaseManager;
import com.example.spotnow.common.MarkerInfo;
import com.example.spotnow.common.SearchActivity;
import com.example.spotnow.common.Utility;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelState;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HomeFragment extends Fragment implements OnMapReadyCallback {
    private MapFragment mapView; // Declaration of MapView object
    private Button resetLocationButton; // Declaration of button object
    private TextView searchbar;
    private Button plusButton;

    // Location setting variables
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1000;
    private FusedLocationSource locationSource;
    private NaverMap naverMap;

    // Activity ListView related variables
    ArrayList<activitySampleData> sampleDataList;
    ArrayList<activity_listview_info> activityDataList;
    activity_listview_adapter myAdapter;
    private SlidingUpPanelLayout slidingUp;

    public long clickedSpotID;
    public String clickedSpotAddress;
    private DatabaseReference mDatabase;
    public String output;

    public HomeFragment() {
        // Required empty public constructor
    }
    private FirebaseAuth mAuth;
    private String currentUserId = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.home_fragment, container, false);

        // Get the current user and their name from Firebase
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        ArrayList<String> path = new ArrayList<>();
        path.add(currentUser.getUid());
        path.add("name");

        // Retrieve the name of the current user from the Firebase database
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

        // Get the current location and display the address
        String result = Utility.GetAddressFromGPS(getContext(), Utility.GetGPS(getContext()));
        TextView activity_address = rootView.findViewById(R.id.location_textview);
        String input = result;

        // Extract the relevant portion of the address
        int spaceCount = 0;
        int startIndex = -1;
        int endIndex = -1;

        for (int i = 0; i < input.length(); i++) {
            if (input.charAt(i) == ' ') {
                spaceCount++;
                if (spaceCount == 1) {
                    startIndex = i + 1; // Start from the character after the first space
                } else if (spaceCount == 5) {
                    endIndex = i;
                    break;
                }
            }
        }

        // Set the extracted address portion to the text view
        if (startIndex != -1 && endIndex != -1) {
            output = input.substring(startIndex, endIndex);
            activity_address.setText(output);
        }

        // Initialize Firebase
        FirebaseManager.init();

        FragmentManager fm = getFragmentManager();
        mapView = (MapFragment) fm.findFragmentById(R.id.map_fragment);

        // Add or replace the map fragment
        if (mapView == null) {
            mapView = MapFragment.newInstance();
            fm.beginTransaction().add(R.id.map_fragment, mapView).commit();
        } else {
            fm.beginTransaction().replace(R.id.map_fragment, mapView).commit();
        }

        mapView.getMapAsync(this);
        locationSource = new FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE);

        resetLocationButton = rootView.findViewById(R.id.reset_location_button);
        resetLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Button click event handling
                v = HomeFragment.this.getView();
                TextView activity_address = v.findViewById(R.id.location_textview);
                activity_address.setText(output);
                naverMap.setLocationTrackingMode(LocationTrackingMode.Follow);
            }
        });

        searchbar = rootView.findViewById(R.id.search_bar);

        searchbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Move to the search activity
                Intent intent = new Intent(getActivity(), SearchActivity.class);
                getActivity().startActivityForResult(intent, 1004);
            }
        });

        slidingUp = rootView.findViewById(R.id.main_panel);
        slidingUp.setPanelState(PanelState.COLLAPSED);

        plusButton = rootView.findViewById(R.id.createActivity_button);
        plusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create an activity and pass the spotID to the ownerActivity via intent
                Intent intent = new Intent(getActivity(), ownerActivity.class);
                intent.putExtra("spotID", clickedSpotID);
                startActivity(intent);
            }
        });

        return rootView;
    }


    public void getActivityList(long spotID) {
        // Get a reference to the "activities" node in the Firebase database
        mDatabase = FirebaseDatabase.getInstance().getReference("activities");
        // Initialize the list to store activity data
        activityDataList = new ArrayList<activity_listview_info>();

        // Add a ValueEventListener to the database reference to listen for changes in the data
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Iterate through the data snapshot to retrieve each activity's information
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    // Get the activity info object from the snapshot
                    ActivityInfo activityInfo = snapshot.getValue(ActivityInfo.class);
                    // Check if the activity's spotID matches the provided spotID
                    if (activityInfo.getSpotID() == spotID) {
                        // Create a new activity listview info object and add it to the list
                        activityDataList.add(new activity_listview_info(
                                activityInfo.getImageUrl(),
                                activityInfo.getTitle(),
                                activityInfo.getContent(),
                                activityInfo.getActivityOwner(),
                                activityInfo.getSport(),
                                activityInfo.getPeopleCnt(),
                                activityInfo.getStartTime(),
                                activityInfo.getEndTime()
                        ));
                    }
                }
                // Notify the adapter that the data has changed
                myAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle database cancellation error
            }
        });
    }

    public void onStart() {
        super.onStart();
        // Perform any necessary actions when the fragment starts
    }

    public void onResume() {
        super.onResume();
        // Perform any necessary actions when the fragment resumes
    }


    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        // Check if the location source handled the permission request result
        if (locationSource.onRequestPermissionsResult(requestCode, permissions, grantResults)) {
            if (!locationSource.isActivated()) {
                // Location permission denied, set the location tracking mode to None
                naverMap.setLocationTrackingMode(LocationTrackingMode.None);
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {
        this.naverMap = naverMap;
        initMap();
    }

    public void initMap() {
        // Set the location source and location tracking mode for the NaverMap
        naverMap.setLocationSource(locationSource);
        naverMap.setLocationTrackingMode(LocationTrackingMode.Follow);

        // Create a path to the "spots" node in the Firebase database
        ArrayList<String> path = new ArrayList<String>();
        DatabaseReference dr = FirebaseManager.GetReferencePath("spots", null);

        // Initialize a HashMap to store the spot data retrieved from the database
        HashMap<String, Object> spotResult = new HashMap<>();

        // Retrieve the data from the "spots" node in the Firebase database
        dr.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                } else {
                    // Retrieve the spot data as a HashMap
                    spotResult.putAll((HashMap<String, Object>) task.getResult().getValue());
                    Log.d("firebase", spotResult.toString());

                    // Iterate through each spot entry in the HashMap
                    for (Map.Entry<String, Object> entry : spotResult.entrySet()) {
                        // Create a MarkerInfo object for the spot
                        MarkerInfo marker = new MarkerInfo((String) entry.getKey());

                        // Retrieve the spot data from the entry
                        Map<String, Object> value = (Map<String, Object>) entry.getValue();
                        marker.location = new Coordinate((double) value.get("latitude"), (double) value.get("longitude"));
                        marker.spotID = (long) value.get("spotID");
                        marker.address = (String) value.get("address");

                        // Set the marker on the map
                        setMark(marker);
                    }
                }
            }
        });
    }


    public void setMark(MarkerInfo m) {
        // Create a new marker
        Marker marker = new Marker();
        marker.setPosition(new LatLng(m.location._latitude, m.location._longitude));
        marker.setIcon(MarkerIcons.RED);
        marker.setWidth(50);
        marker.setHeight(60);
        marker.setMap(naverMap);

        marker.setOnClickListener(new Overlay.OnClickListener() {
            @Override
            public boolean onClick(@NonNull Overlay overlay) {
                // Handle marker click event

                // Update the spot name in the modal dialog to the clicked spot's name
                View v = HomeFragment.this.getView();
                TextView spotName = v.findViewById(R.id.location_textview);
                spotName.setText(m.spotName);
                TextView activity_address = v.findViewById(R.id.spotLocation);
                activity_address.setText(m.address);
                clickedSpotID = m.spotID;
                clickedSpotAddress = m.address;

                // Get the activity list for the clicked spot
                getActivityList(clickedSpotID);

                // Set up the list view to display the activities
                ListView listView = (ListView) v.findViewById(R.id.activity_listview);
                myAdapter = new activity_listview_adapter(getActivity(), activityDataList);
                listView.setAdapter(myAdapter);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        // Handle click event on an activity item

                        // Get the selected activity information
                        activity_listview_info selectedItem = myAdapter.getItem(position);

                        String activityOwnerId = selectedItem.getActivityOwner();
                        Class<?> activityClass = ownerActivitymodify.class;

                        // Determine the appropriate activity class based on the user's role (owner or participant)
                        if (!currentUserId.equals(activityOwnerId)) {
                            activityClass = participantFragment.class;
                        }

                        // Create an intent and pass the selected activity information to it
                        Intent intent = new Intent(getActivity(), activityClass);
                        intent.putExtra("activityTitle", selectedItem.getActivityTitle());
                        intent.putExtra("activityContent", selectedItem.getActivityContent());
                        intent.putExtra("activityOwner", selectedItem.getActivityOwner());
                        intent.putExtra("activitySport", selectedItem.getActivitySport());
                        intent.putExtra("activityPeopleCnt", selectedItem.getPeopleCnt());
                        intent.putExtra("activityStartTime", selectedItem.getStartTime());
                        intent.putExtra("activityEndTime", selectedItem.getEndTime());

                        startActivity(intent);
                    }
                });

                // Expand the sliding panel
                slidingUp = v.findViewById(R.id.main_panel);
                slidingUp.setPanelState(PanelState.EXPANDED);

                return true;
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        // Destroy the MapView along with the Fragment
        if (mapView != null) {
            mapView.onDestroy();
            mapView = null;

            Fragment fragment = getFragmentManager().findFragmentById(R.id.map_fragment);
            if (fragment != null)
                getFragmentManager().beginTransaction().remove(fragment).commit();
        }
    }

}
