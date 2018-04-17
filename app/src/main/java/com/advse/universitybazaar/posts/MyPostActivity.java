package com.advse.universitybazaar.posts;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.advse.universitybazaar.R;
import com.advse.universitybazaar.bean.Post;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MyPostActivity extends AppCompatActivity {

    TableLayout table;
    TableRow tableRow;
    TextView headingTextView;
    TextView poster;
    TableLayout.LayoutParams layoutparamstr;
    TableRow.LayoutParams layoutparams;
    private DatabaseReference db;
    String ownerID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_post);

        getSupportActionBar().hide();


        SharedPreferences prefs = getSharedPreferences("LOGIN_PREF", Context.MODE_PRIVATE);
        ownerID = prefs.getString("mavID",null);

        table = (TableLayout)findViewById(R.id.table);

        db = FirebaseDatabase.getInstance().getReference("Posts/");
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                while (table.getChildCount() > 1)
                    table.removeView(table.getChildAt(table.getChildCount() - 1));

                for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Post post = snapshot.getValue(Post.class);
                    if(post.getPostOwnerId().equals(ownerID)) {
                        addToView(post);
                    }

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void addToView(final Post post) {

        final Post currentPost = post;
        tableRow = new TableRow(this);

        layoutparamstr = new TableLayout.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.MATCH_PARENT);
        layoutparamstr.setMargins(0, 30,0, 30);
        tableRow.setLayoutParams(layoutparamstr);
        tableRow.setId(Integer.valueOf(post.getPostId()));
        tableRow.setBackgroundColor(Color.GRAY);
        tableRow.setPadding(0, 40, 0, 40);

        tableRow.setOnClickListener(new TableRow.OnClickListener(){
            public void onClick(View v) {
                showPostDetails(post);
            }
        });

        layoutparams = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT, 50);

        headingTextView = new TextView(this);
        headingTextView.setLayoutParams(layoutparams);
        headingTextView.setGravity(Gravity.CENTER);
        headingTextView.setText(post.getPostHeading());
        headingTextView.setTextSize(15);
        tableRow.addView(headingTextView);

        poster = new TextView(this);
        poster.setLayoutParams(layoutparams);
        poster.setGravity(Gravity.CENTER);
        poster.setText(post.getPostOwnerId());
        poster.setTextSize(15);
        tableRow.addView(poster);

        table.addView(tableRow);

    }

    public void showPostDetails(Post post) {

        Intent postHome = new Intent(getApplicationContext(), PostDetailsActivity.class);
        postHome.putExtra("postId",String.valueOf(post.getPostId()));
        postHome.putExtra("postHeading",post.getPostHeading());
        postHome.putExtra("postDescription",post.getPostDescription());
        postHome.putExtra("postLocation", post.getLocation());
        postHome.putExtra("postOwnerId" , post.getPostOwnerId());
        postHome.putExtra("yourPost","1");

        startActivityForResult(postHome,1);
    }

    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode == 1 && resultCode == RESULT_OK) {
            Snackbar snack = Snackbar.make(findViewById(R.id.Snackbar_MyPost),"Post Deleted Successfully",Snackbar.LENGTH_SHORT);
            snack.getView().setBackgroundColor(Color.parseColor("#298E10"));
            snack.show();
        } else if(resultCode == RESULT_CANCELED) {

        }
    }
}


