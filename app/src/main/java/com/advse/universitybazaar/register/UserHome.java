package com.advse.universitybazaar.register;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.advse.universitybazaar.R;
import com.advse.universitybazaar.clubs.ClubHome;
import com.advse.universitybazaar.messages.MessageHome;
import com.advse.universitybazaar.posts.PostHome;
import com.advse.universitybazaar.trade.TradeActivity;

public class UserHome extends AppCompatActivity {

    //private DrawerLayout navbarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R .layout.activity_user_home);

        SharedPreferences prefs = getSharedPreferences("LOGIN_PREF",MODE_PRIVATE);
        String setTitle = prefs.getString("name",null);
        getSupportActionBar().setTitle(setTitle + "'s Home");
        //getSupportActionBar().setTitle(setTitle + "'s Home");

       /* navbarLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                int id = menuItem.getItemId();
                menuItem.setChecked(true);
                navbarLayout.closeDrawers();
                switch (id) {
                    case R.id.nav_clubs:
                        LayoutInflater inflator = getLayoutInflater();
                        ConstraintLayout container = (ConstraintLayout) findViewById(R.id.club_home);
                        inflator.inflate(R.layout.activity_user_home,container);
                        break;
                }
                return true;
            }
        });*/

        final Button clubHome = (Button) findViewById(R.id.clubButton);
        final Button tradeHome = (Button) findViewById(R.id.tradeButton);
        final Button postsHome = (Button) findViewById(R.id.postButton);
        final Button mesageHome = (Button) findViewById(R.id.messageButton);
        final Button logoutButton = (Button) findViewById(R.id.logout);

        clubHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ClubHome.class);
                startActivity(intent);
            }
        });


        //Market place
        tradeHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), TradeActivity.class);
                startActivity(intent);
            }
        });



        postsHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), PostHome.class);
                startActivity(intent);
            }
        });

        mesageHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MessageHome.class);
                startActivity(intent);
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences pref = getSharedPreferences("LOGIN_PREF",MODE_PRIVATE);
                pref.edit().clear().apply();
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);
    }
}