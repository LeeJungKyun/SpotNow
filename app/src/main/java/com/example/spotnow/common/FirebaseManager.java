package com.example.spotnow.common;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public abstract class FirebaseManager {
    public static FirebaseDatabase database; // Static instance of the FirebaseDatabase

    public static void init() {
        database = FirebaseDatabase.getInstance(); // Initialize the FirebaseDatabase instance
    }

    //[WriteData usage]
    //    ArrayList<String> path = new ArrayList<>();
    //    path.add("5cCu0sBUaDXynAsvjp7CJiZzocv2");
    //    path.add("follower");
    //    FirebaseManager.WriteData("users", path, 123);
    public static <T> void WriteData(String ref, ArrayList<String> path, T data) {
        DatabaseReference nDatabase = database.getReference(ref); // Get a DatabaseReference for the specified path

        if (path == null)
            nDatabase.push().setValue(data); // If the path is null, push the data to a new child node

        for (int i = 0; i < path.size(); i++) {
            nDatabase = nDatabase.child(path.get(i)); // Traverse the path by getting child references
        }

        nDatabase.push().setValue(data); // Set the data at the final location in the database
    }

    //[Read usage]
    //    ArrayList<String> path = new ArrayList<>();
    //    path.add("5cCu0sBUaDXynAsvjp7CJiZzocv2");
    //    path.add("follower");
    //
    //    FirebaseManager.GetReferencePath("users", path).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
    //        @Override
    //        public void onComplete(@NonNull Task<DataSnapshot> task) {
    //            if (!task.isSuccessful()) {
    //                Log.e("firebase", "Error getting data", task.getException());
    //            }
    //            else {
    //                Log.d("firebase", String.valueOf(task.getResult().getValue()));
    //            }
    //        }
    //    });
    public static DatabaseReference GetReferencePath(String ref, ArrayList<String> path) {
        DatabaseReference nDatabase = database.getReference(ref); // Get a DatabaseReference for the specified path

        if (path == null) {
            return nDatabase; // If the path is null, return the DatabaseReference as-is
        }

        for (int i = 0; i < path.size(); i++) {
            nDatabase = nDatabase.child(path.get(i)); // Traverse the path by getting child references
        }

        return nDatabase; // Return the final DatabaseReference
    }
}
