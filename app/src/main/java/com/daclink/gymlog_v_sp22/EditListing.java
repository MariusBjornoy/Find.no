/* Title: Project Part 02: Find.no Ecommerce Application
 * NAME: Marius Roedder Bjoernoey
 * Date: 27-11-2022 ()
 * Description: Creates an edit listing page. This page allows the owner of a post to edit the post.
 * This class uses extras to keep the values from when the listing was created. This makes it easier
 * for the user to edit a post, without having to rewrite everything.
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

public class EditListing extends AppCompatActivity {
    private static final String USER_ID_KEY = "com.daclink.gymlog_v_sp22.userIdKey";
    private static final String PRODUCT_ID_KEY = "com.daclink.gymlog_v_sp22.productIdKey";

    private TextView mEditTitle;
    private Button mAddListingButtonField;
    private TextView mProductTitleField;
    private TextView mProductDescriptionField;
    private TextView mProductCountField;
    private TextView mProductPriceField;

    private String mProductTitle;
    private String mProductDescription;
    private String mProductPrice;
    private String mProductCount;
    private int mProductCount2;
    private double mProductPrice2;

    private FindnoDAO mFindnoDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_listing);
        int mUserId = getIntent().getIntExtra(USER_ID_KEY,-1);
        int mProductId = getIntent().getIntExtra(PRODUCT_ID_KEY,-1);
        getDatabase();
        wireUpDisplay();
        Product product = mFindnoDAO.getProductByPId(mProductId);
        mEditTitle.setText("Editing " + product.getProductName());
        mProductTitleField.setText(product.getProductName());
        mProductDescriptionField.setText(product.getProductDescription());
        mProductPriceField.setText(product.getProductPrice()+"");
        mProductCountField.setText(product.getProductCount()+"");
        mAddListingButtonField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getValuesFromDisplay();
                if(validators()) {
                    converters();
                    Product product = mFindnoDAO.getProductByPId(mProductId);
                    product.setProductDescription(mProductDescription);
                    product.setProductName(mProductTitle);
                    product.setProductCount(mProductCount2);
                    product.setUserId(mUserId);
                    product.setProductPrice(mProductPrice2);
                    mFindnoDAO.update(product);
                    Intent intent = MainActivity.intentFactory(getApplicationContext(), mUserId);
                    startActivity(intent);
                }
            }
        });
    }

    //Creates an Icon to make it easier to get back to the main page.
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

    //Connects values from the parsed in data to datatypes we can work with
    private void getValuesFromDisplay() {
        mProductTitle = mProductTitleField.getText().toString();
        mProductDescription = mProductDescriptionField.getText().toString();
        mProductCount = mProductCountField.getText().toString();
        mProductPrice = mProductPriceField.getText().toString();
    }

    //Retrieves Data from the database
    private void getDatabase() {
        mFindnoDAO = Room.databaseBuilder(this, AppDatabase.class, AppDatabase.DATABASE_NAME)
                .allowMainThreadQueries()
                .build()
                .GymLogDAO();
    }

    /*Connects the input fields to variables we can modify, this allows us to change properties of the
    *Textviews, and we can retrieve tha input data*/
    private void wireUpDisplay() {
        mEditTitle = findViewById(R.id.editItem);
        mProductTitleField = findViewById(R.id.productTitleEdit);
        mProductDescriptionField = findViewById(R.id.productDescriptionEdit);
        mProductCountField = findViewById(R.id.ProductCountEdit);
        mProductPriceField = findViewById(R.id.ProductPriceEdit);
        mAddListingButtonField = findViewById(R.id.addListingButtonEdit);
    }

    //Checks if there is input value in each field
    private boolean validators(){
        getValuesFromDisplay();
        if (mProductTitle.equals("")){
            Toast.makeText(EditListing.this, "We need a product Title",Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (mProductDescription.equals("")){
            Toast.makeText(EditListing.this, "We need a product Description",Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(mProductCount.equals("")) {
            Toast.makeText(EditListing.this, "We need a product Count", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(mProductPrice.equals("")) {
            Toast.makeText(EditListing.this, "We need a product Price", Toast.LENGTH_SHORT).show();
            return false;
        }else {return true;}
    }

    //Converts the string values to integers and doubles
    private void converters(){
        mProductCount2 = Integer.parseInt(mProductCount);
        mProductPrice2 = Double.parseDouble(mProductPrice);
    }

    //Intent factory redirects user to this class and parses in UserId and ProductID with it
    public static Intent intentFactory(Context context, int productId, int userId){
        Intent intent = new Intent(context, EditListing.class);
        intent.putExtra(USER_ID_KEY, userId);
        intent.putExtra(PRODUCT_ID_KEY, productId);
        return intent;
    }
}