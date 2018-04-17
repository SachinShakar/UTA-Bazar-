package com.advse.universitybazaar.trade;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.advse.universitybazaar.R;
import com.advse.universitybazaar.clubs.ClubHome;
import com.advse.universitybazaar.trade.SellItemActivity;

public class TradeActivity extends AppCompatActivity {

    private int requestId = 90;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trade);

        SharedPreferences prefs = getSharedPreferences("LOGIN_PREF",MODE_PRIVATE);
        String setTitle = prefs.getString("name",null);

        Button boughtItemsButton = (Button) findViewById(R.id.bought);
        Button allItemsButton = (Button) findViewById(R.id.forSale);
        Button postedItemsButton = (Button) findViewById(R.id.posted);

        boughtItemsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), BoughtItemsActivity.class);
                startActivity(intent);
            }
        });
        FloatingActionButton sellItemButton = (FloatingActionButton) findViewById(R.id.sellItemButton);
        sellItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent createItemIntent = new Intent(getApplicationContext(), SellItemActivity.class);
                startActivityForResult(createItemIntent, requestId);
            }
        });
        allItemsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AllItemsActivity.class);
                startActivity(intent);
            }
        });
        postedItemsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), PostedItemsActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode == requestId && resultCode == RESULT_OK) {
            Snackbar snack = Snackbar.make(findViewById(R.id.Snackbar_TradeHome),"Item added successfully",Snackbar.LENGTH_SHORT);
            snack.getView().setBackgroundColor(Color.parseColor("#298E10"));
            snack.show();
        } else if(resultCode == RESULT_CANCELED) {

        }
    }
}
