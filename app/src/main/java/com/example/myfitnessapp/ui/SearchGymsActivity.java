package com.example.myfitnessapp.ui;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;

import com.example.myfitnessapp.R;
import com.example.myfitnessapp.models.Constants;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchGymsActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String TAG = SearchGymsActivity.class.getSimpleName();
    //@BindView(R.id.gymSearchEditText)
    //EditText mGymSearchEditText;
    @BindView(R.id.submitGymLocationButton)
    Button mGymSearchButton;
    @BindView(R.id.savedGymnasiumsButton) Button mSavedGymnasiumsButton;


    private DatabaseReference mSearchedLocationReference;
    private ValueEventListener mSearchedLocationReferenceListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_gyms);

        ButterKnife.bind(this);
        mGymSearchButton.setOnClickListener(this);
        mSavedGymnasiumsButton.setOnClickListener(this);




        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("message");

        myRef.setValue("Hello, World!");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d(TAG, "Value is: " + value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        //Firebase Implementation
        mSearchedLocationReference = FirebaseDatabase
                .getInstance()
                .getReference()
                .child(Constants.FIREBASE_CHILD_SEARCHED_LOCATION);

        mSearchedLocationReferenceListener = mSearchedLocationReference.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                System.out.println("In the OnDataChange_ __ _  _  _ ___  ___  _ _ __ : Got location");

                for(DataSnapshot locationSnapshot : dataSnapshot.getChildren()){
                    String location = locationSnapshot.getValue(String.class);
                    Log.d("Locations updated", "location: " + location);
                    System.out.println("Location: "+location);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(TAG, "Failed to read value: ",databaseError.toException());

            }
        });

    }

    @Override
    public void onClick(View v){
        if(v == mGymSearchButton ){
            Animation animation = AnimationUtils.loadAnimation(this,R.anim.zoom_out);
            mGymSearchButton.startAnimation(animation);
            //String searchGymLocation = mGymSearchEditText.getText().toString();
            Intent gymIntent = new Intent(SearchGymsActivity.this, GymnasiumListActivity.class);
            startActivity(gymIntent);
        }

        if (v == mSavedGymnasiumsButton) {
            Intent intent = new Intent(SearchGymsActivity.this, SavedGymnasiumsListActivity.class);
            startActivity(intent);
        }
    }

//    private void saveLocationToFirebase(String searchGymLocation) {
//        mSearchedLocationReference.push().setValue(searchGymLocation);
//        System.out.println("_____-------____------");
//        Log.d(TAG, "Saved Location :"+searchGymLocation);
//    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        mSearchedLocationReference.removeEventListener(mSearchedLocationReferenceListener);
    }
}
