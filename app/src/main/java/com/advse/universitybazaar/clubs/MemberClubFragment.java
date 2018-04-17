package com.advse.universitybazaar.clubs;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.advse.universitybazaar.bean.Club;
import com.advse.universitybazaar.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 */
public class MemberClubFragment extends Fragment {

    TableLayout table ;
    TableRow tr;
    TextView tv1;
    TextView tv2;
    TableLayout.LayoutParams layoutparamstr;
    TableRow.LayoutParams layoutparams;
    private DatabaseReference db;
    boolean finished = false;
    //ArrayList<Club> clubList;
    String ownerID;


    public MemberClubFragment() {
        System.out.println("Inside Constructor");

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        SharedPreferences prefs = this.getActivity().getSharedPreferences("LOGIN_PREF", Context.MODE_PRIVATE);
        ownerID = prefs.getString("mavID",null);
        System.out.println(ownerID);

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_member_club,container,false);
        table = (TableLayout) view.findViewById(R.id.table);

        db = FirebaseDatabase.getInstance().getReference("Clubs/");
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                while (table.getChildCount() > 1)
                    table.removeView(table.getChildAt(table.getChildCount() - 1));
                //clubList = new ArrayList<>();

                for(DataSnapshot snapShot : dataSnapshot.getChildren()) {
                    //System.out.println(snapShot);
                    HashMap<String, String> members = new HashMap<String, String>();
                    //clubList.add(snapShot.getValue(Club.class));
                    for(DataSnapshot m : snapShot.child("members").getChildren()){
                        members.put(m.getKey(), m.getValue().toString());
                        //System.out.println(m);
                    }
                    if(!members.isEmpty() && members.containsKey(ownerID ))
                        addToView(snapShot.getValue(Club.class));

                }


//                for(Club club : clubList){
//                    addToView(club);
//                }

                finished = true;
                System.out.println(finished);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return view;

    }


    public void addToView(Club club){
        final int clubId = club.getClubId();
        tr= new TableRow(getActivity());
        layoutparamstr = new TableLayout.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.MATCH_PARENT);
        layoutparamstr.setMargins(0, 30,0, 30);
        tr.setLayoutParams(layoutparamstr);
        tr.setId(club.getClubId());
        tr.setBackgroundColor(Color.GRAY);
        tr.setPadding(0, 40, 0, 40);
        tr.setOnClickListener(new TableRow.OnClickListener(){
            public void onClick(View v) {
                showClub(clubId);
            }
        });

        layoutparams = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT, 50);



        tv1 = new TextView(getActivity());
        tv1.setLayoutParams(layoutparams);
        tv1.setGravity(Gravity.CENTER);
        tv1.setText(club.getClubName());
        tv1.setTextSize(15);
        tr.addView(tv1);

        tv2 = new TextView(getActivity());
        tv2.setLayoutParams(layoutparams);
        tv2.setGravity(Gravity.CENTER);
        tv2.setText(club.getClubOwner());
        tv2.setTextSize(15);
        tr.addView(tv2);
        table.addView(tr);

    }


    public void showClub(int id){
        Intent clubHome = new Intent(getActivity().getApplicationContext(), com.advse.universitybazaar.clubs.SelectedClubHome.class);
        clubHome.putExtra("clubId",String.valueOf(id) );
        startActivity(clubHome);

    }

}
