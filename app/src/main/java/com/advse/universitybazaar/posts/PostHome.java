package com.advse.universitybazaar.posts;

import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.advse.universitybazaar.R;
import com.advse.universitybazaar.register.UserHome;

public class PostHome extends AppCompatActivity {

    FloatingActionButton newPostButton;
    Button yourPostButton;
    Button othersPostButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_home);
        getSupportActionBar().setTitle("Post Home");

        newPostButton = (FloatingActionButton) findViewById(R.id.addNewPostButton);
        othersPostButton = (Button) findViewById(R.id.othersPostButton);
        yourPostButton = (Button) findViewById(R.id.yourPostButton);

        newPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), NewPostActivity.class);
                startActivityForResult(intent,1);
            }
        });

        yourPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MyPostActivity.class);
                startActivity(intent);
            }
        });



        othersPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), OthersPostActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(),UserHome.class);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode == 1 && resultCode == RESULT_OK) {
            Snackbar snack = Snackbar.make(findViewById(R.id.Snackbar_PostHome),"Post Submitted Successfully",Snackbar.LENGTH_SHORT);
            snack.getView().setBackgroundColor(Color.parseColor("#298E10"));
            snack.show();
        } else if(resultCode == RESULT_CANCELED) {

        }
    }
}
