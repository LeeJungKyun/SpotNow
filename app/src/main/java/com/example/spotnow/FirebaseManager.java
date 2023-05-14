package com.example.spotnow;

import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import kotlin.Function;

public abstract class FirebaseManager {
    public static FirebaseDatabase database;

    public static void init(){
        database = FirebaseDatabase.getInstance();
    }

    public static <T>void WriteData(String ref, ArrayList<String> path, String valueName, T data){
        DatabaseReference nDatabase = database.getReference(ref);

        if(path.size() == 0)
            return;

        for(int i =0; i< path.size(); i++){
            nDatabase = nDatabase.child(path.get(i));
        }

        nDatabase.child(valueName).push().setValue(data);
    }

    public static DatabaseReference GetReferencePath(String ref, ArrayList<String> path){
        DatabaseReference nDatabase = database.getReference(ref);

        if(path.size() == 0)
            return null;

        for(int i =0; i< path.size(); i++){
            nDatabase = nDatabase.child(path.get(i));
        }

        return nDatabase;
    }

}
