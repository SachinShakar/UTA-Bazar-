package com.advse.universitybazaar.posts;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.advse.universitybazaar.R;
import com.advse.universitybazaar.bean.Comment;
import com.advse.universitybazaar.bean.Post;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class NewPostActivity extends AppCompatActivity {

    private DatabaseReference db;
    private Button postButton;
    private EditText postHeading;
    private EditText postDescription;
    private EditText postLocation;
    private TextInputLayout postHeadingTIL;
    private TextInputLayout postDescriptionTIL;
    private TextInputLayout postLocationTIL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);
        getSupportActionBar().hide();

        postHeading = (EditText) findViewById(R.id.postHeading);
        postDescription = (EditText) findViewById(R.id.postDescription);
        postLocation = (EditText) findViewById(R.id.postLocation);

        postHeadingTIL = (TextInputLayout) findViewById(R.id.postHeadingTil);
        postDescriptionTIL = (TextInputLayout) findViewById(R.id.postDescriptionTil);
        postLocationTIL = (TextInputLayout) findViewById(R.id.postLocationTil);

        postButton = (Button) findViewById(R.id.createPostButton);

        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validate()) {
                    addPostToList();
                }
            }
        });
    }

    public void addPostToList() {

        db = FirebaseDatabase.getInstance().getReference("Posts/");
        Query getLastRow = db.orderByKey().limitToLast(1);
        getLastRow.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                SharedPreferences prefs = getSharedPreferences("LOGIN_PREF",MODE_PRIVATE);
                String postOwnerID = prefs.getString("mavID",null);

//                ArrayList<Comment> comments = new ArrayList<>();
//
//                comments.add(new Comment(1, "", ""));

                if(!dataSnapshot.hasChildren()) {
                    Post post = new Post(1,postHeading.getText().toString(),postDescription.getText().toString(),
                            postLocation.getText().toString(),postOwnerID);
                    db.child("1").setValue(post);
                    db.child("1").child("comments").child("1").setValue(new Comment(1, "", ""));
                    setResult(RESULT_OK,null);
                    finish();
                }
                else {
                    ArrayList<Post> listOfPosts = new ArrayList<>();
                    for(DataSnapshot snap : dataSnapshot.getChildren()) {
                        listOfPosts.add(snap.getValue(Post.class));
                    }
                    Post post = new Post(listOfPosts.get(0).getPostId() + 1, postHeading.getText().toString(),
                            postDescription.getText().toString(),postLocation.getText().toString(),postOwnerID);

                    db.child(String.valueOf(listOfPosts.get(0).getPostId() + 1)).setValue(post);
                    db.child(String.valueOf(listOfPosts.get(0).getPostId() + 1)).child("comments").child("1").setValue(new Comment(1, "", ""));
                    setResult(RESULT_OK,null);
                    finish();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public boolean validate() {

        String heading = postHeading.getText().toString();
        String description = postDescription.getText().toString();
        String location = postLocation.getText().toString();

        if(heading.length() <= 0 || description.length() <= 0 || location.length() <= 0) {
            Snackbar snack = Snackbar.make(findViewById(R.id.Snackbar_New_Post),"Please Enter all the fields",Snackbar.LENGTH_SHORT);
            snack.getView().setBackgroundColor(Color.parseColor("#B21919"));
            snack.show();
            return false;
        }
        else if(description.length() > 150) {
            postDescriptionTIL.setError("Description cannot be more than 150 characters");
            return false;
        }

        return true;
    }
}
