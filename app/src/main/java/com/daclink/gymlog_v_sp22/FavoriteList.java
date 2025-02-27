/* Title: Project Part 02: Find.no Ecommerce Application
 * NAME: Marius Roedder Bjoernoey
 * Date: 27-11-2022 ()
 * Description: Adds Objects to the favorite list making it easy to get the favorite products
 * of each user.
 * */

package com.daclink.gymlog_v_sp22;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Context;
import android.content.DialogInterface;
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

public class FavoriteList extends AppCompatActivity {
    private FindnoDAO mFindnoDAO;
    private static final String USER_ID_KEY = "com.daclink.gymlog_v_sp22.userIdKey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_list);
        getDataBase();
        createDisplay();
    }

    /*Creates a display that adds an item for each entry in the IsFavorite Table connected to the User ID
    These fields can be clicked. If they are clicked an alarm box pops up asking if the user wants to
    remove the item from favorites or if it wants to go to that product page.
    * */
    private void createDisplay() {
        int mUserId = getIntent().getIntExtra(USER_ID_KEY, -1);
        List<String> productNames = new ArrayList<>();
        for (IsFavorite order: mFindnoDAO.getAllFavoritesByUserID(mUserId)){
            if (mFindnoDAO.getProductByPId(order.getProductId()) != null) { //checks if the item still exists
                productNames.add(mFindnoDAO.getProductByPId(order.getProductId()).getProductName());
            }
        }
        ListView mlistView = findViewById(R.id.FavoriteListDisplay);
        ArrayAdapter arrayAdapter2 = new ArrayAdapter<>(FavoriteList.this,
                android.R.layout.simple_list_item_1,productNames);
        mlistView.setAdapter(arrayAdapter2);
        mlistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String productElement = (String) parent.getItemAtPosition(position);
                AlertDialog.Builder mAlertBuilder = new AlertDialog.Builder(FavoriteList.this);
                final AlertDialog mAlertDialog = mAlertBuilder.create();
                mAlertBuilder.setMessage("Do you want to go to " + productElement + " or remove it from favorite list");
                mAlertBuilder.setNegativeButton("Remove from favorites", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Product product = mFindnoDAO.getProductByProductName(productElement);
                        int pID = product.getProductId();
                        IsFavorite favorite = mFindnoDAO.getFavoritefromPid(pID);
                        mFindnoDAO.delete(favorite);
                        Intent intent = MainActivity.intentFactory(getApplicationContext(),mUserId);
                        startActivity(intent);
                    }
                });
                mAlertBuilder.setPositiveButton(" Go to product ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
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
                mAlertBuilder.create().show();
            }
        });
    }

    //Creates an option to go back to main page clicking the icon in the header
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


    //Retrieves data from database.
    private void getDataBase() {
        mFindnoDAO = Room.databaseBuilder(this, AppDatabase.class, AppDatabase.DATABASE_NAME)
                .allowMainThreadQueries()
                .build()
                .GymLogDAO();
    }

    //Intent factory sends user to this class, it needs the userId to filter through all the productIDs connected to the UserId
    public static Intent intentFactory(Context context, int mUserId){
        Intent intent = new Intent(context,FavoriteList.class);
        intent.putExtra(USER_ID_KEY, mUserId);
        return intent;
    }
}