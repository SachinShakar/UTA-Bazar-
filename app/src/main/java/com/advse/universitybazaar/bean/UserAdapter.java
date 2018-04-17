package com.advse.universitybazaar.bean;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.view.*;
import android.widget.*;

import com.google.firebase.database.*;

import com.advse.universitybazaar.R;

import java.util.List;

/**
 * Created by shahsk0901 on 2/28/18.
 */

public class UserAdapter extends ArrayAdapter<Student> {

    ViewHolder holder;
    private List<Student> membersList;
    private DatabaseReference db;
    private SharedPreferences prefs;

    public UserAdapter(@NonNull Context context, int resource,List<Student> membersList) {
        super(context,resource,membersList);
        this.membersList = membersList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null) {

            prefs = getContext().getSharedPreferences("LOGIN_PREF",Context.MODE_PRIVATE);
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.club_member_list_layout,parent,false);

            holder = new ViewHolder();
            holder.clubMemberName = (TextView) convertView.findViewById(R.id.clubMemberName);
            holder.clubMemberActions = (Button) convertView.findViewById(R.id.deleteMember);
            db = FirebaseDatabase.getInstance().getReference();
            convertView.setTag(holder);

        }

        holder = (ViewHolder) convertView.getTag();

        final String currentUser = prefs.getString("mavID",null);

        final Student student = membersList.get(position);

        holder.clubMemberActions.setVisibility(View.GONE);

        holder.clubMemberName.setText(student.getName());

        db.child("Clubs/" + student.getClubId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Club club = dataSnapshot.getValue(Club.class);
                if(club.getClubOwner().equals(currentUser) && student.getType().equals("M")) {
                    holder.clubMemberActions.setText("Delete");
                    holder.clubMemberActions.setVisibility(View.VISIBLE);
                    holder.clubMemberActions.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {

                            DatabaseReference db1 = db.child("Clubs").child(String.valueOf(student.getClubId())).child("members").child(student.getMavID());
                            db1.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    dataSnapshot.getRef().removeValue();
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }
                    });
                }
                else if(club.getClubOwner().equals(currentUser) && student.getType().equals("R")) {
                    holder.clubMemberActions.setText("Approve");
                    holder.clubMemberActions.setVisibility(View.VISIBLE);
                    holder.clubMemberActions.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {

                            DatabaseReference db1 = db.child("Clubs").child(String.valueOf(student.getClubId())).child("requests").child(student.getMavID());
                            db1.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    db.child("Clubs").child(String.valueOf(student.getClubId())).child("members").child(student.getMavID()).setValue(student.getName());
                                    dataSnapshot.getRef().removeValue();
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }
                    });
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return convertView;
    }

    public void refreshMembersList(List<Student> membersList) {
        this.membersList.clear();
        this.membersList.addAll(membersList);
    }

    static class ViewHolder {
        TextView clubMemberName;
        Button clubMemberActions;
    }

}