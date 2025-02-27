/* Title: Project Part 02: Find.no Ecommerce Application
 * NAME: Marius Roedder Bjoernoey
 * Date: 27-11-2022 ()
 * Description: Diaplsys all the products in the database
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.daclink.gymlog_v_sp22.DB.AppDatabase;
import com.daclink.gymlog_v_sp22.DB.FindnoDAO;

import java.util.ArrayList;
import java.util.List;

public class ShowAllProducts extends AppCompatActivity {
    private FindnoDAO mFindnoDAO;
    private static final String USER_ID_KEY = "com.daclink.gymlog_v_sp22.userIdKey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_all_products);
        getDataBase();
        createDisplay();
    }

    //Creates a loop going through all the products and adding the to the linnear view
    //If the user clicks on one of the items it is redirected to the product page
    private void createDisplay() {
        int mUserId = getIntent().getIntExtra(USER_ID_KEY, -1);
        List<String> productNames = new ArrayList<>();
        for (Product p: mFindnoDAO.getAllProducts()){
            productNames.add(p.getProductName());
        }
        ListView mlistView = findViewById(R.id.allProducts);
        ArrayAdapter arrayAdapter2 = new ArrayAdapter<>(ShowAllProducts.this,
                android.R.layout.simple_list_item_1,productNames);
        mlistView.setAdapter(arrayAdapter2);
        mlistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String productElement = (String) parent.getItemAtPosition(position);
                Toast.makeText(ShowAllProducts.this, productElement, Toast.LENGTH_SHORT).show();
                Product product = mFindnoDAO.getProductByProductName(productElement);
                Intent intent = ProductPage.intentFactory(getApplicationContext(),
                        product.getProductName(),
                        product.getProductDescription(),
                        product.getProductPrice(),
                        product.getProductCount(),
                        product.getUserId(),mUserId);
                startActivity(intent);
            }
        });
    }

    //Retrieves data from the database
    private void getDataBase() {
        mFindnoDAO = Room.databaseBuilder(this, AppDatabase.class, AppDatabase.DATABASE_NAME)
                .allowMainThreadQueries()
                .build()
                .GymLogDAO();
    }

    //Allows user to go back to the main page by clicking on the house icon in the header bar
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

    //intent factory sends user into this class upon activation, the user ID is also parsed in
    public static Intent intentFactory(Context context, int UserId){
        Intent intent = new Intent(context, ShowAllProducts.class);
        intent.putExtra(USER_ID_KEY,UserId);
        return intent;
    }
}