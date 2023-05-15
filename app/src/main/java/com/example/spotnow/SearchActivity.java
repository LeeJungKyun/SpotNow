package com.example.spotnow;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

    private DatabaseReference mDatabase;
    user_listview_adapter myAdapter;

    EditText search_bar;
    Button search_button;

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);

        search_bar = findViewById(R.id.search_bar);
        search_button = findViewById(R.id.search_button);

        search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getUserList(search_bar.getText().toString());

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
                        userDataList.add(new user_listview_info(R.drawable.circle, userInfo.getName(),userInfo.getIntroduce_self()));
                    }

                }

                myAdapter.notifyDataSetChanged();

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void InitializeUserData()
    {
        userDataList = new ArrayList<user_listview_info>();

        userDataList.add(new user_listview_info(R.drawable.circle, "정준희","15세 이상관람가"));
        userDataList.add(new user_listview_info(R.drawable.circle, "이윤서","19세 이상관람가"));
        userDataList.add(new user_listview_info(R.drawable.circle, "정성훈","12세 이상관람가"));
    }
}



