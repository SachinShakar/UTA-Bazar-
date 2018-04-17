package com.advse.universitybazaar.trade;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.advse.universitybazaar.R;
import com.advse.universitybazaar.bean.Club;
import com.advse.universitybazaar.bean.Item;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class PostedItemsActivity extends AppCompatActivity {

    TableLayout table ;
    TableRow tableRow;
    TextView nameTextView;
    TextView priceTextView;
    TableLayout.LayoutParams layoutparamstr;
    TableRow.LayoutParams layoutparams;
    private DatabaseReference db;
    //ArrayList<Club> clubList;
    String ownerID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posted_items);
        getSupportActionBar().hide();

        SharedPreferences prefs = getSharedPreferences("LOGIN_PREF", Context.MODE_PRIVATE);
        ownerID = prefs.getString("mavID",null);
        System.out.println(ownerID);

        table = (TableLayout)findViewById(R.id.table);

        db = FirebaseDatabase.getInstance().getReference("Items/");
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //Clear the table and then refill the data
                while (table.getChildCount() > 1)
                    table.removeView(table.getChildAt(table.getChildCount() - 1));
                //clubList = new ArrayList<>();

                for(DataSnapshot snapShot : dataSnapshot.getChildren()) {
                    Item item = snapShot.getValue(Item.class);
                    System.out.println(item.getName() + " " + item.getPrice());
                    //System.out.println(snapShot);
                    if(item.getSellerId().equals(ownerID)){
                        addToView(item);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void addToView(Item item){
        final Item currentItem = item;
        final int itemId = item.getItemId();
        tableRow = new TableRow(this);
        layoutparamstr = new TableLayout.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.MATCH_PARENT);
        layoutparamstr.setMargins(0, 30,0, 30);
        tableRow.setLayoutParams(layoutparamstr);
        tableRow.setId(Integer.valueOf(item.getItemId()));
        tableRow.setBackgroundColor(Color.GRAY);
        tableRow.setPadding(0, 40, 0, 40);
        tableRow.setOnClickListener(new TableRow.OnClickListener(){
            public void onClick(View v) {
                showItem(currentItem);
            }
        });

        layoutparams = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT, 50);



        nameTextView = new TextView(this);
        nameTextView.setLayoutParams(layoutparams);
        nameTextView.setGravity(Gravity.CENTER);
        nameTextView.setText(item.getName());
        nameTextView.setTextSize(15);
        tableRow.addView(nameTextView);

        priceTextView = new TextView(this);
        priceTextView.setLayoutParams(layoutparams);
        priceTextView.setGravity(Gravity.CENTER);
        priceTextView.setText(String.valueOf(item.getPrice()));
        priceTextView.setTextSize(15);
        tableRow.addView(priceTextView);

        table.addView(tableRow);

    }

    public void showItem(Item item){
        Intent itemHome = new Intent(getApplicationContext(), ItemDetails.class);
        itemHome.putExtra("itemId",String.valueOf(item.getItemId()));
        itemHome.putExtra("name",item.getName());
        itemHome.putExtra("description",item.getDescription());
        itemHome.putExtra("buyerId",item.getBuyerId());
        itemHome.putExtra("sellerId",item.getSellerId());
        itemHome.putExtra("price",String.valueOf(item.getPrice()));


        itemHome.putExtra("buy","0");

        startActivity(itemHome);

    }

}
