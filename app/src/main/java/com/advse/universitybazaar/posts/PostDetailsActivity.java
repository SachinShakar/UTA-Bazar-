package com.advse.universitybazaar.posts;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;

import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import android.widget.TextView;
import android.widget.Toast;

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

public class PostDetailsActivity extends AppCompatActivity {

    EditText headingTextView;
    EditText descriptionTextView;
    EditText locationTextView;

    Button updateButton;
    Button deleteButton;
    Button savePostButton;

    DatabaseReference db;
    Intent intent;

    TableLayout tableLayout;
    TableRow tableRow;
    TableLayout.LayoutParams layoutparamstr;
    TableRow.LayoutParams layoutparams;
    TextView commenter;
    TextView commentText;
    ScrollView scroll;

    Button imageButton;

    EditText newComment;
    private AlertDialog deleteAlert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_details);
        getSupportActionBar().hide();
        intent = getIntent();

        headingTextView = (EditText) findViewById(R.id.postHeading);
        headingTextView.setText(intent.getStringExtra("postHeading"));

        descriptionTextView = (EditText) findViewById(R.id.postDescription);
        descriptionTextView.setText(intent.getStringExtra("postDescription"));

        locationTextView = (EditText) findViewById(R.id.postLocation);
        locationTextView.setText(intent.getStringExtra("postLocation"));

        updateButton = (Button) findViewById(R.id.updatePost);
        deleteButton = (Button) findViewById(R.id.deletePost);
        savePostButton = (Button) findViewById(R.id.savePost);
        scroll = (ScrollView) findViewById(R.id.scroll);

        tableLayout = (TableLayout) findViewById(R.id.tableComments);

        imageButton = (Button) findViewById(R.id.postCommentButton);


//        hideKeyboard();

        if(intent.getStringExtra("yourPost").equals("0")) {

            updateButton.setVisibility(View.GONE);
            deleteButton.setVisibility(View.GONE);
            savePostButton.setVisibility(View.GONE);
            //scroll.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,220));


        }else {

            updateButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

//                    scroll.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,180));

                    headingTextView.setFocusable(true);
                    headingTextView.setFocusableInTouchMode(true);

                    descriptionTextView.setFocusable(true);
                    descriptionTextView.setFocusableInTouchMode(true);

                    locationTextView.setFocusable(true);
                    locationTextView.setFocusableInTouchMode(true);

                    updateButton.setVisibility(View.INVISIBLE);

                    savePostButton.setVisibility(View.VISIBLE);
                    deleteButton.setVisibility(View.INVISIBLE);
                }
            });

            savePostButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    db = FirebaseDatabase.getInstance().getReference("Posts/").child(intent.getStringExtra("postId"));
                    db.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            dataSnapshot.getRef().child("postHeading").
                                      setValue(headingTextView.getText().toString());

                            dataSnapshot.getRef().child("postDescription").
                                    setValue(descriptionTextView.getText().toString());

                            dataSnapshot.getRef().child("location").
                                    setValue(locationTextView.getText().toString());
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                    headingTextView.setFocusable(false);
                    descriptionTextView.setFocusable(false);
                    locationTextView.setFocusable(false);

                    updateButton.setVisibility(View.VISIBLE);
                    savePostButton.setVisibility(View.INVISIBLE);
                    deleteButton.setVisibility(View.VISIBLE);

                    Toast.makeText(getApplicationContext(),"Post updated successfully",Toast.LENGTH_SHORT).show();



                }
            });

            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //Toast.makeText(getApplicationContext(),"Delete",Toast.LENGTH_SHORT).show();

                    final AlertDialog.Builder deleteAlertBuilder = new AlertDialog.Builder(PostDetailsActivity.this, R.style.myDialog);
                    deleteAlertBuilder.setMessage("Are You Sure You Want to Delete?");

                    deleteAlertBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            db = FirebaseDatabase.getInstance().getReference("Posts/").child(intent.getStringExtra("postId"));
                            db.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {

                                    dataSnapshot.getRef().removeValue();
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });

                            setResult(RESULT_OK,null);
                            finish();
                        }
                    });

                    deleteAlertBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            deleteAlert.cancel();
                        }
                    });

                    deleteAlert = deleteAlertBuilder.create();
                    deleteAlert.show();
                }
            });
        }


        //To fetch and update comments

        db = FirebaseDatabase.getInstance().getReference("Posts/"+ intent.getStringExtra("postId") + "/comments");
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //Clear the table and then refill the data
                while (tableLayout.getChildCount() > 0)
                    tableLayout.removeView(tableLayout.getChildAt(tableLayout.getChildCount() - 1));

                for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Comment comment = snapshot.getValue(Comment.class);
                        if(comment.getCommentId() != 1 )
                            addToView(comment);


                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        // to add comment

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                newComment = (EditText) findViewById(R.id.postComment);

                if(newComment.getText().toString().trim().length() == 0) {
                    Toast.makeText(getApplicationContext(),"Cannot be empty",Toast.LENGTH_SHORT).show();
                    hideKeyboard();
                } else if (newComment.getText().toString().trim().length() > 150) {
                    Toast.makeText(getApplicationContext(),"Cannot be more than 150 characters",Toast.LENGTH_SHORT).show();
                    hideKeyboard();
                } else {
                    db = FirebaseDatabase.getInstance().getReference("Posts/"+ intent.getStringExtra("postId") + "/comments");
                    final Query getLastRow = db.orderByKey().limitToLast(1);

                    getLastRow.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            SharedPreferences prefs = getSharedPreferences("LOGIN_PREF", Context.MODE_PRIVATE);
                            String commenter = prefs.getString("mavID",null);

                            ArrayList<Comment> listOfPosts = new ArrayList<>();
                            for(DataSnapshot snap : dataSnapshot.getChildren()) {
                                listOfPosts.add(snap.getValue(Comment.class));
                            }

                            Comment comment = new Comment(listOfPosts.get(0).getCommentId() + 1, commenter, newComment.getText().toString());

                            db.child(String.valueOf(listOfPosts.get(0).getCommentId() + 1)).setValue(comment);
                            hideKeyboard();
                            newComment.setText("");
                            Toast.makeText(getApplicationContext(),"Comment Added",Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }

            }
        });



    }

    public void addToView(Comment comment){

        tableRow = new TableRow(this);
        layoutparamstr = new TableLayout.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.MATCH_PARENT);
        layoutparamstr.setMargins(0, 30,0, 30);
        tableRow.setLayoutParams(layoutparamstr);
        tableRow.setBackgroundColor(Color.GRAY);
        //tableRow.setPadding(0, 40, 0, 40);

        layoutparams = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT, 20);



        commenter = new TextView(this);
        commenter.setLayoutParams(layoutparams);
        commenter.setGravity(Gravity.CENTER);
        commenter.setText(comment.getOwnerId());
        commenter.setTextSize(15);
        tableRow.addView(commenter);

        layoutparams = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT, 80);

        commentText = new TextView(this);
        commentText.setLayoutParams(layoutparams);
        commentText.setGravity(Gravity.CENTER);
        commentText.setText(comment.getCommentText());
        commentText.setTextSize(15);
        tableRow.addView(commentText);

        tableLayout.addView(tableRow);

    }

    public void hideKeyboard() {
        InputMethodManager inputManager = (InputMethodManager) getSystemService(this.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

}
