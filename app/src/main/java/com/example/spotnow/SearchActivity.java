package com.example.spotnow;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EdgeEffect;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity
{

    ArrayList<user_listview_info> userDataList;
    ArrayList<spot_listview_info> spotDataList;
    private DatabaseReference mDatabase;
    user_listview_adapter myAdapter;
    spot_listview_adapter myAdapter2;

    EditText search_bar;

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);

        search_bar = findViewById(R.id.search_bar);

        search_bar.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {

                    getUserList(search_bar.getText().toString());
                    getSpotList(search_bar.getText().toString());

                    ListView listView2 = (ListView)findViewById(R.id.spot_listView);
                    myAdapter2 = new spot_listview_adapter(getApplicationContext(),spotDataList);

                    listView2.setAdapter(myAdapter2);

                    listView2.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                        @Override
                        public void onItemClick(AdapterView parent, View v, int position, long id){
                            Toast.makeText(getApplicationContext(),
                                    myAdapter2.getItem(position).getName(),
                                    Toast.LENGTH_LONG).show();
                        }
                    });

                    ListView listView = (ListView)findViewById(R.id.user_listView);
                    myAdapter = new user_listview_adapter(getApplicationContext(),userDataList);

                    listView.setAdapter(myAdapter);

                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                        @Override
                        public void onItemClick(AdapterView parent, View v, int position, long id){
                            Toast.makeText(getApplicationContext(),
                                    myAdapter.getItem(position).getName(),
                                    Toast.LENGTH_LONG).show();
                        }
                    });

                    return true;
                }
                return false;
            }
        });

    }

    public void getSpotList(String word)
    {
        mDatabase = FirebaseDatabase.getInstance().getReference("spots");
        spotDataList = new ArrayList<spot_listview_info>();

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    SpotInfo spotInfo = snapshot.getValue(SpotInfo.class);
                    if(spotInfo.getName().contains(word))
                    {
                        spotDataList.add(new spot_listview_info(R.drawable.gachon_logo, spotInfo.getName(),spotInfo.getAddress()));
                    }
                }
                myAdapter2.notifyDataSetChanged();

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    //씨름중인 부분
    public void getUserList(String word)
    {
        mDatabase = FirebaseDatabase.getInstance().getReference("users");
        userDataList = new ArrayList<user_listview_info>();

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    UserInfo userInfo = snapshot.getValue(UserInfo.class);
                    if(userInfo.getName().contains(word))
                    {
                        userDataList.add(new user_listview_info(R.drawable.user_circle, userInfo.getName(),userInfo.getIntroduce_self()));
                    }

                }

                myAdapter.notifyDataSetChanged();

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }



    /*public void InitializeUserData()
    {
        userDataList = new ArrayList<user_listview_info>();

        userDataList.add(new user_listview_info(R.drawable.circle, "정준희","15세 이상관람가"));
        userDataList.add(new user_listview_info(R.drawable.circle, "이윤서","19세 이상관람가"));
        userDataList.add(new user_listview_info(R.drawable.circle, "정성훈","12세 이상관람가"));
    }*/
}



