/* Title: Project Part 02: Find.no Ecommerce Application
 * NAME: Marius Roedder Bjoernoey
 * Date: 27-11-2022 ()
 * Description: The purpose of this file is to add items to the database, accessible to every user
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
import android.widget.TextView;
import android.widget.Toast;

import com.daclink.gymlog_v_sp22.DB.AppDatabase;
import com.daclink.gymlog_v_sp22.DB.FindnoDAO;

public class AddItem extends AppCompatActivity {
    private static final String USER_ID_KEY = "com.daclink.gymlog_v_sp22.userIdKey";
    private Button mAddListingButtonField;
    private TextView mProductTitleField;
    private TextView mProductDescriptionField;
    private TextView mProductCountField;
    private TextView mProductPriceField;

    private String mProductTitle;
    private String mProductDescription;
    private String mProductPrice;
    private String mProductCount;
    private double mProductPrice2;
    private Integer mProductCount2;

    private FindnoDAO mFindnoDAO;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
        getDatabase();
        wireUpDisplay();

        mAddListingButtonField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getValuesFromDisplay();
                addtoDatabase();
            }
        });

    }

    private void addtoDatabase(){
        if(this.validators()) {
            converters();
            int userId = getIntent().getIntExtra(USER_ID_KEY, -1);
            Product newProduct = new Product(mProductTitle,
                    mProductDescription,
                    mProductPrice2,
                    mProductCount2,
                    userId);
            mFindnoDAO.insert(newProduct);
            Toast.makeText(AddItem.this, "Your listing has been posted", Toast.LENGTH_SHORT).show();
            Intent intent = MainActivity.intentFactory(getApplicationContext(), userId);
            startActivity(intent);
        }
    }

    //Checks if the input fields are empty
    private boolean validators(){
        getValuesFromDisplay();
        if (mProductTitle.equals("")){
            Toast.makeText(AddItem.this, "We need a product title",Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (mProductDescription.equals("")){
            Toast.makeText(AddItem.this, "We need a product Description",Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(mProductCount.equals("")) {
            Toast.makeText(AddItem.this, "We need a product Count", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(mProductPrice.equals("")) {
            Toast.makeText(AddItem.this, "We need a product Price", Toast.LENGTH_SHORT).show();
            return false;
        }else {return true;}
    }

    //converts the text values to integers
    private void converters(){
        mProductCount2 = Integer.parseInt(mProductCount);
        mProductPrice2 = Double.parseDouble(mProductPrice);
    }

    //Retrieves data from teh database
    private void getDatabase() {
        mFindnoDAO = Room.databaseBuilder(this, AppDatabase.class, AppDatabase.DATABASE_NAME)
                .allowMainThreadQueries()
                .build()
                .GymLogDAO();
    }

    //Connects the values tytped in to the user to data types we can use
    private void getValuesFromDisplay(){
        mProductTitle = mProductTitleField.getText().toString();
        mProductDescription = mProductDescriptionField.getText().toString();
        mProductPrice = mProductPriceField.getText().toString();
        mProductCount = mProductCountField.getText().toString();
    }

    //Connects each input field to a certain variable
    private void wireUpDisplay() {
        mProductTitleField= findViewById(R.id.productTitle);
        mProductDescriptionField = findViewById(R.id.productDescription);
        mProductCountField = findViewById(R.id.ProductCount);
        mProductPriceField = findViewById(R.id.ProductPrice);
        mAddListingButtonField = findViewById(R.id.addListingButton);
    }

    //Creates the option to go back to the home screen
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

    //Intent factory made to redirect to the add item page and to parese in the User id
    public static Intent intentFactory(Context context, int userId) {
        Intent intent = new Intent(context, AddItem.class);
        intent.putExtra(USER_ID_KEY, userId);
        return intent;
    }


}