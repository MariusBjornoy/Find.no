/* Title: Project Part 02: Find.no Ecommerce Application
 * NAME: Marius Roedder Bjoernoey
 * Date: 27-11-2022 ()
 * Description: This page allows the admin to add promos turning all prices in the application down by a certain amount.
 * */

package com.daclink.gymlog_v_sp22;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.daclink.gymlog_v_sp22.DB.AppDatabase;
import com.daclink.gymlog_v_sp22.DB.FindnoDAO;

public class PromoAdmin extends AppCompatActivity {
    private FindnoDAO mFindnoDAO;
    private Button mTenOf;
    private Button mTwentyOf;
    private Button mThirtyOf;
    private Button mFourtyOf;
    private Button mFiftyOf;
    private Button mSixtyOf;
    private Button mSeventyOf;
    private Button mEightyOf;
    private Button mNintyOf;
    private Button mhundredOf;
    private static final String USER_ID_KEY = "com.daclink.gymlog_v_sp22.userIdKey";

    //Creates all buttons for each promotion.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promo_admin);
        getDatabase();
        buttonHookUp();
        mhundredOf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculatePromo(1.0);
                Toast.makeText(PromoAdmin.this, "The promo is online, all items are 100% of", Toast.LENGTH_SHORT).show();
            }
        });

        mNintyOf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculatePromo(0.9);
                Toast.makeText(PromoAdmin.this, "The promo is online, all items are 90% of", Toast.LENGTH_SHORT).show();
            }
        });

        mEightyOf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculatePromo(0.8);
                Toast.makeText(PromoAdmin.this, "The promo is online, all items are 80% of", Toast.LENGTH_SHORT).show();
            }
        });

        mSeventyOf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculatePromo(0.7);
                Toast.makeText(PromoAdmin.this, "The promo is online, all items are 70% of", Toast.LENGTH_SHORT).show();
            }
        });


        mSixtyOf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculatePromo(0.6);
                Toast.makeText(PromoAdmin.this, "The promo is online, all items are 60% of", Toast.LENGTH_SHORT).show();
            }
        });

        mFiftyOf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculatePromo(0.5);
                Toast.makeText(PromoAdmin.this, "The promo is online, all items are 50% of", Toast.LENGTH_SHORT).show();
            }
        });

        mFourtyOf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculatePromo(0.4);
                Toast.makeText(PromoAdmin.this, "The promo is online, all items are 40% of", Toast.LENGTH_SHORT).show();
            }
        });

        mThirtyOf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculatePromo(0.3);
                Toast.makeText(PromoAdmin.this, "The promo is online, all items are 30% of", Toast.LENGTH_SHORT).show();
            }
        });

        mTwentyOf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculatePromo(0.2);
                Toast.makeText(PromoAdmin.this, "The promo is online, all items are 20% of", Toast.LENGTH_SHORT).show();
            }
        });

        mTenOf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculatePromo(0.1);
                Toast.makeText(PromoAdmin.this, "The promo is online, all items are 10% of", Toast.LENGTH_SHORT).show();
            }
        });
    }


    //Connects all the buttons to the input fields
    private void buttonHookUp() {
        mTenOf = findViewById(R.id.button10p);
        mTwentyOf = findViewById(R.id.button10p);
        mThirtyOf = findViewById(R.id.button10p);
        mFourtyOf = findViewById(R.id.button10p);
        mFiftyOf = findViewById(R.id.button10p);
        mSixtyOf = findViewById(R.id.button10p);
        mSeventyOf = findViewById(R.id.button10p);
        mEightyOf = findViewById(R.id.button10p);
        mNintyOf = findViewById(R.id.button10p);
        mhundredOf = findViewById(R.id.button10p);
    }

    //Retrieves the data from the database
    private void getDatabase() {
        mFindnoDAO = Room.databaseBuilder(this, AppDatabase.class, AppDatabase.DATABASE_NAME)
                .allowMainThreadQueries()
                .build()
                .GymLogDAO();
    }

    //Takes all products in the database and changes the price by adding a multiplier to each one.
    private void calculatePromo(double promo){
        for(Product p : mFindnoDAO.getAllProducts()){
            double price = p.getProductPrice()*(1-promo);
            p.setProductPrice(price);
            mFindnoDAO.update(p);
        }
    }

    //Sends the admins to this class
    public static Intent intentFactory(Context context, int userId){
        Intent intent = new Intent(context, PromoAdmin.class);
        intent.putExtra(USER_ID_KEY, userId);
        return intent;
    }

    //Allows the user to get back to the main page by clicking on the icon in the header
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int mUserId = getIntent().getIntExtra(USER_ID_KEY, -1);
        switch (item.getItemId()){
            case R.id.GoHome:
                Intent intent = MainActivity.intentFactory(getApplicationContext(),mUserId);
                startActivity(intent);
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}