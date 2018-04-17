package com.advse.universitybazaar.trade;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.advse.universitybazaar.R;
import com.advse.universitybazaar.bean.Item;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ItemDetails extends AppCompatActivity {

    TextView nameTextView;
    TextView descriptionView;
    TextView priceTextView;
    Button buyItemButton;
    Button cancel;
    DatabaseReference db;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);
        getSupportActionBar().hide();


        intent = getIntent();

        nameTextView = (TextView) findViewById(R.id.name);
        nameTextView.setText(intent.getStringExtra("name"));

        descriptionView = (TextView) findViewById(R.id.description);
        descriptionView.setText(intent.getStringExtra("description"));

        priceTextView = (TextView) findViewById(R.id.price);
        priceTextView.setText(intent.getStringExtra("price"));

        buyItemButton = (Button) findViewById(R.id.buyItem);
        cancel = (Button) findViewById(R.id.cancel);

        if(intent.getStringExtra("buy").equals("0")) {
            buyItemButton.setVisibility(View.INVISIBLE);
        } else {

            cancel.setVisibility(View.VISIBLE);

            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setResult(Activity.RESULT_CANCELED,null);
                    finish();
                }
            });

            buyItemButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String itemId = intent.getStringExtra("itemId");
                    db = FirebaseDatabase.getInstance().getReference("Items/" + itemId);
                    db.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            SharedPreferences prefs = getSharedPreferences("LOGIN_PREF",MODE_PRIVATE);
                            String buyerId = prefs.getString("mavID",null);
                            Item item = dataSnapshot.getValue(Item.class);
                            item.setBuyerId(buyerId);
                            db.child("buyerId").setValue(item.getBuyerId());
                            setResult(Activity.RESULT_OK,null);
                            finish();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            });
        }
    }
}
