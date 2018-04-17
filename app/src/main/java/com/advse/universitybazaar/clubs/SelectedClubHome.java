package com.advse.universitybazaar.clubs;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.advse.universitybazaar.bean.Club;
import com.advse.universitybazaar.bean.Student;
import com.advse.universitybazaar.bean.UserAdapter;
import com.advse.universitybazaar.R;
import com.google.firebase.database.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SelectedClubHome extends AppCompatActivity {

    private DatabaseReference db;
    private DatabaseReference clubRef;
    private SharedPreferences sharedPreferences;
    private Button requestMembership;
    private Button deleteClub;
    private TextView clubDescription;
    private TextView clubOwner;
    private List<Student> listOfStudents;
    private UserAdapter listAdapter;
    private String mavID;
    private String userName;
    private String clubID;
    private ValueEventListener clubRefListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_club_home);

        // Get mavID from shared prefs and club id from intent bundle
        sharedPreferences = getSharedPreferences("LOGIN_PREF", MODE_PRIVATE);
        mavID = sharedPreferences.getString("mavID", null);
        userName = sharedPreferences.getString("name", null);

        Intent intent = getIntent();
        clubID = intent.getStringExtra("clubId");

        db = FirebaseDatabase.getInstance().getReference();
        clubRef = db.child("Clubs/" + clubID);

        requestMembership = (Button) findViewById(R.id.requestMembership);
        deleteClub = (Button) findViewById(R.id.deleteClub);
        clubDescription = (TextView) findViewById(R.id.displayClubDescription);
        clubOwner = (TextView) findViewById(R.id.displayClubOwner);

        listOfStudents = new ArrayList<>();
        listAdapter = new UserAdapter(this,0,listOfStudents);

        ListView clubMembersList = (ListView) findViewById(R.id.listOfStudents);
        clubMembersList.setAdapter(listAdapter);

        clubRefListener = clubRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                final Club club = dataSnapshot.getValue(Club.class);
                System.out.print("\n\n\n\n\n\n\n\n\n" + club);
                HashMap<String, String> members = new HashMap<>();
                HashMap<String, String> requests = new HashMap<>();
                //clubList.add(snapShot.getValue(Club.class));
                for(DataSnapshot m : dataSnapshot.child("members").getChildren()){
                    listAdapter.add(new Student(m.getKey(),m.getValue().toString(),club.getClubId(),"M"));
                    members.put(m.getKey(), m.getValue().toString());
                }
                for(DataSnapshot m : dataSnapshot.child("requests").getChildren()){
                    listAdapter.add(new Student(m.getKey(),m.getValue().toString(),club.getClubId(),"R"));
                    requests.put(m.getKey(), m.getValue().toString());
                }

                //To display name, owner and description
                //getSupportActionBar().setTitle(club.getClubName());
                clubDescription.setText(club.getClubDescription());

                // Getting owner name using the owner's id
                Query getName = db.child("Users/" + club.getClubOwner());
                getName.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snap) {
                        Student student = snap.getValue(Student.class);
                        clubOwner.setText("Owner: " + student.getName());
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {}
                });

                if (club.getClubOwner().equals(mavID)) {

                    requestMembership.setVisibility(View.GONE);
                    deleteClub.setVisibility(View.VISIBLE);
                    deleteClub.setText("Delete Club");
                    deleteClub.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            db.child("Clubs/").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    Intent intent = new Intent();
                                    intent.putExtra("deleteClub","true");
                                    intent.putExtra("deleteClubID",String.valueOf(club.getClubId()));
                                    setResult(Activity.RESULT_OK,intent);
                                    clubRef.removeEventListener(clubRefListener);
                                    finish();
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }
                    });

                }

                else if(!club.getClubOwner().equals(mavID) && members.containsKey(mavID)){
                    requestMembership.setVisibility(View.GONE);
                    deleteClub.setVisibility(View.GONE);
                }
                else{
                    Query getRequests = clubRef.child("requests");
                    getRequests.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snap) {
                            HashMap<String, String> requests = new HashMap<String, String>();
                            for(DataSnapshot r : snap.getChildren())
                                requests.put(r.getKey().toString(), r.getValue().toString());

                            if(requests.containsKey(mavID)){
                                requestMembership.setVisibility(View.VISIBLE);
                                requestMembership.setText("Request Sent");
                                deleteClub.setVisibility(View.GONE);
                            }
                            else{
                                System.out.println("\n\n\n\n\n\n\n\n\n\n" + userName);
                                requestMembership.setVisibility(View.VISIBLE);
                                deleteClub.setVisibility(View.GONE);
                                requestMembership.setText("Join");
                                requestMembership.setOnClickListener(new Button.OnClickListener() {
                                    public void onClick(View v) {
                                        //clubRef.child("requests").child(mavID).setValue(userName);
                                        Intent intent = new Intent();
                                        intent.putExtra("requestMembership","true");
                                        intent.putExtra("requestedClub",String.valueOf(club.getClubId()));
                                        intent.putExtra("requesterID",mavID);
                                        intent.putExtra("requesterName",userName);
                                        setResult(Activity.RESULT_OK,intent);
                                        clubRef.removeEventListener(clubRefListener);
                                        finish();
                                    }
                                });

                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {}
                    });


                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });





        // Notifies as soon as a member is deleted from the club
        ChildEventListener memberDeletedListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) { }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) { }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                System.out.println(dataSnapshot);
                List<Student> updatedList = new ArrayList<>();

                for(DataSnapshot m : dataSnapshot.child("members").getChildren()){
                    updatedList.add(new Student(m.getKey(),m.getValue().toString(),clubID,"M"));
                }
                for(DataSnapshot m : dataSnapshot.child("requests").getChildren()){
                    updatedList.add(new Student(m.getKey(),m.getValue().toString(),clubID,"R"));
                }

                listAdapter.refreshMembersList(updatedList);
                listAdapter.notifyDataSetChanged();

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) { }

            @Override
            public void onCancelled(DatabaseError databaseError) { }
        };
        clubRef.addChildEventListener(memberDeletedListener);
    }
}
