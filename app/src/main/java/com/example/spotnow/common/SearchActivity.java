package com.example.spotnow.common;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.spotnow.R;
import com.example.spotnow.profile.UserInfo;
import com.example.spotnow.profile.user_listview_adapter;
import com.example.spotnow.profile.user_listview_info;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {

    ArrayList<user_listview_info> userDataList;
    private DatabaseReference mDatabase;
    user_listview_adapter myAdapter;

    EditText search_bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);

        search_bar = findViewById(R.id.search_bar);

        search_bar.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {

                    // Call a method to retrieve the user list based on the search keyword
                    getUserList(search_bar.getText().toString());

                    ListView listView = findViewById(R.id.user_listView);
                    myAdapter = new user_listview_adapter(getApplicationContext(), userDataList);

                    listView.setAdapter(myAdapter);

                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView parent, View v, int position, long id) {
                            // When a user is selected, return the selected user name to the calling activity
                            Intent intent = new Intent();
                            intent.putExtra("selected_user_name", myAdapter.getItem(position).getName());
                            setResult(RESULT_OK, intent);
                            finish();
                        }
                    });
                    return true;
                }
                return false;
            }
        });
    }

    public void getUserList(String word) {
        mDatabase = FirebaseDatabase.getInstance().getReference("users");
        userDataList = new ArrayList<>();

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    // Retrieve user information from the database
                    UserInfo userInfo = snapshot.getValue(UserInfo.class);
                    if (userInfo.getName().contains(word)) {
                        // If the user's name contains the search keyword, add it to the list
                        userDataList.add(new user_listview_info(R.drawable.user_circle, userInfo.getName(), userInfo.getIntroduce_self()));
                    }
                }
                // Notify the adapter that the data has changed
                myAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle database error
            }
        });
    }
}