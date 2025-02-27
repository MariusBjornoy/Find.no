/* Title: Project Part 02: Find.no Ecommerce Application
 * NAME: Marius Roedder Bjoernoey
 * Date: 27-11-2022 (Fall 2022)
 * Description: Shows all the items the user has in the cart
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

public class UserCart extends AppCompatActivity {
    private static final String USER_ID_KEY = "com.daclink.gymlog_v_sp22.userIdKey";
    int mUserId;
    private FindnoDAO mFindnoDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_cart);
        mUserId = getIntent().getIntExtra(USER_ID_KEY, -1);
        getDatabase();
        createDisplay();
    }

    //Retrieves data from the database.
    private void getDatabase() {
        mFindnoDAO = Room.databaseBuilder(this, AppDatabase.class, AppDatabase.DATABASE_NAME)
                .allowMainThreadQueries()
                .build()
                .GymLogDAO();
    }

    // creates the display using linearview. For all the items corresponding to a given User id, gets added to the list,
    // The items then gets presented, and the user can click on each one choosing if he wants to remove it from cart or buy the item.
    private void createDisplay(){
        List<String> usernames = new ArrayList<>();
        for (Cart C: mFindnoDAO.getAllItemsInUserCart(mUserId)) {
            if (mFindnoDAO.getProductByPId(C.getProductID()) != null) { //checks if the item still exists
            usernames.add(mFindnoDAO.getProductByPId(C.getProductID()).getProductName());
            }
        }
        ListView listView = findViewById(R.id.allcartItems);
        ArrayAdapter arrayAdapter = new ArrayAdapter<>(UserCart.this,
                android.R.layout.simple_list_item_1,usernames);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String productName = (String) parent.getItemAtPosition(position);
                AlertDialog.Builder mAlertBuilder = new AlertDialog.Builder(UserCart.this);
                final AlertDialog mAlertDialog = mAlertBuilder.create();
                mAlertBuilder.setMessage("Do You want to edit or delete the post?");
                mAlertBuilder.setPositiveButton(" Remove from cart ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(UserCart.this, "Item was removed from cart", Toast.LENGTH_SHORT).show();
                        Product product = mFindnoDAO.getProductByProductName(productName);
                        Cart cartItem = mFindnoDAO.getCartItemFromPID(product.getProductId());
                        mFindnoDAO.delete(cartItem);
                        Intent intent = UserCart.intentFactory(getApplicationContext(),mUserId);
                        startActivity(intent);
                    }
                });
                mAlertBuilder.setNegativeButton(" Buy Item ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(UserCart.this,"Item was bought" , Toast.LENGTH_SHORT).show();
                        Product product = mFindnoDAO.getProductByProductName(productName);
                        Cart cartItem = mFindnoDAO.getCartItemFromPID(product.getProductId());
                        OrderHistory newItemLog = new OrderHistory(mUserId, product.getProductId());
                        mFindnoDAO.insert(newItemLog);
                        mFindnoDAO.delete(cartItem);
                        Intent intent = UserCart.intentFactory(getApplicationContext(),mUserId);
                        startActivity(intent);
                    }
                });
                mAlertBuilder.create().show();
            }
        });
    }

    //Creates a Home icon making it possible for the user to go back to the main pag by clicking it.
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

    //Intent factory is used to send the user to this class.
    public static Intent intentFactory(Context context, int userId){
        Intent intent = new Intent(context, UserCart.class);
        intent.putExtra(USER_ID_KEY, userId);
        return intent;
    }
}