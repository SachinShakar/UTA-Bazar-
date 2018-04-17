package com.advse.universitybazaar.clubs;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.advse.universitybazaar.bean.Club;
import com.advse.universitybazaar.R;
import com.google.firebase.database.*;

import java.util.ArrayList;

public class CreateClubActivity extends AppCompatActivity {

    private DatabaseReference db;
    private Button createClubButton;
    private EditText clubName;
    private EditText clubDesc;
    private TextInputLayout clubNameTIL;
    private TextInputLayout clubDescTIL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_club);

        createClubButton = (Button) findViewById(R.id.CreateClubButton);
        clubName = (EditText) findViewById(R.id.clubName);
        clubDesc = (EditText) findViewById(R.id.clubDescription);
        clubNameTIL = (TextInputLayout) findViewById(R.id.clubNameTil);
        clubDescTIL = (TextInputLayout) findViewById(R.id.clubDescTil);

        createClubButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validate()) {
                    createClub();
                }
            }
        });
    }

    public void createClub() {
        db = FirebaseDatabase.getInstance().getReference("Clubs/");
        Query getLastRow = db.orderByKey().limitToLast(1);

        getLastRow.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
              //  GenericTypeIndicator<ArrayList<Club>> clubsIndicator = new GenericTypeIndicator<ArrayList<Club>>() {};
                ArrayList<Club> clubList = new ArrayList<>();
                for(DataSnapshot snapShot : dataSnapshot.getChildren()) {
                    clubList.add(snapShot.getValue(Club.class));
                }
                System.out.println(dataSnapshot);
                SharedPreferences prefs = getSharedPreferences("LOGIN_PREF",MODE_PRIVATE);
                String ownerID = prefs.getString("mavID",null);

                Club addClub = new Club(clubList.get(0).getClubId()+1,clubName.getText().toString(),
                        clubDesc.getText().toString(),ownerID);
                db.child(String.valueOf(clubList.get(0).getClubId()+1)).setValue(addClub);

                setResult(RESULT_OK,null);
                finish();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public boolean validate() {
        String name = clubName.getText().toString();
        String desc = clubDesc.getText().toString();
        if(name.length() <= 0 && desc.length() <= 0) {
            Snackbar snack = Snackbar.make(findViewById(R.id.Snackbar_Create_Club),"Please Enter all the fields",Snackbar.LENGTH_SHORT);
            snack.getView().setBackgroundColor(Color.parseColor("#B21919"));
            snack.show();
            return false;
        }
        else if(desc.length() > 150) {
            clubDescTIL.setError("Description cannot be more than 150 characters");
            return false;
        }
        return true;
    }
}
