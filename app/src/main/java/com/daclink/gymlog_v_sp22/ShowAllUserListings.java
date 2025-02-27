/* Title: Project Part 02: Find.no Ecommerce Application
 * NAME: Marius Roedder Bjoernoey
 * Date: 27-11-2022 ()
 * Description: Shows all the Users listings (All items made by the user).
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

public class ShowAllUserListings extends AppCompatActivity {
    private FindnoDAO mFindnoDAO;
    private static final String USER_ID_KEY = "com.daclink.gymlog_v_sp22.userIdKey";
    int mUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_all_user_listings);
        getDataBase();
        mUserId = getIntent().getIntExtra(USER_ID_KEY, -1);
        createDisplay();
    }

    //Creates a display containing all the User listings, so that the user can edit or remove its own listings.
    private void createDisplay() {
        mUserId = getIntent().getIntExtra(USER_ID_KEY, -1);
        List<String> productNames = new ArrayList<>();
        for (Product p: mFindnoDAO.getAllProductsByUserID(mUserId)){
            productNames.add(p.getProductName());
        }
        ListView mlistView = findViewById(R.id.allUserProducts);
        ArrayAdapter arrayAdapter3 = new ArrayAdapter<>(ShowAllUserListings.this,
                android.R.layout.simple_list_item_1,productNames);
        mlistView.setAdapter(arrayAdapter3);
        mlistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String userElement = (String) parent.getItemAtPosition(position);
                String name = mFindnoDAO.getProductByProductName(userElement).getProductName();
                //Toast.makeText(ShowAllUserListings.this, name, Toast.LENGTH_SHORT).show();

                AlertDialog.Builder mAlertBuilder = new AlertDialog.Builder(ShowAllUserListings.this);
                final AlertDialog mAlertDialog = mAlertBuilder.create();
                mAlertBuilder.setMessage("Do You want to edit or delete the post?");
                mAlertBuilder.setPositiveButton("Edit post", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(ShowAllUserListings.this, name, Toast.LENGTH_SHORT).show();
                        Intent intent = EditListing.intentFactory(getApplicationContext(),
                                mFindnoDAO.getProductByProductName(name).getProductId(),
                                mFindnoDAO.getProductByProductName(name).getUserId());
                        startActivity(intent);
                    }
                });
                mAlertBuilder.setNegativeButton("Delete Post", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(ShowAllUserListings.this, name + " Was deleted", Toast.LENGTH_SHORT).show();
                        Intent intent = ShowAllUserListings.intentFactory(getApplicationContext(),mUserId);
                        startActivity(intent);
                        mFindnoDAO.delete(mFindnoDAO.getProductByProductName(name));
                    }
                });
                mAlertBuilder.create().show();
            }
        });


    }

    //Retrieves all the information from the database
    private void getDataBase() {
        mFindnoDAO = Room.databaseBuilder(this, AppDatabase.class, AppDatabase.DATABASE_NAME)
                .allowMainThreadQueries()
                .build()
                .GymLogDAO();
    }

    //Allows the users to go back to the main page, by clicking on the house icon
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


    //intent factory sends admins into this class
    public static Intent intentFactory(Context context, int userId){
        Intent intent = new Intent(context, ShowAllUserListings.class);
        intent.putExtra(USER_ID_KEY, userId);
        return intent;
    }
}