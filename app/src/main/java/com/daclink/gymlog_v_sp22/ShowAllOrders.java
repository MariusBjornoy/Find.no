/* Title: Project Part 02: Find.no Ecommerce Application
 * NAME: Marius Roedder Bjoernoey
 * Date: 27-11-2022 ()
 * Description: Displays all the orders of the user
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

public class ShowAllOrders extends AppCompatActivity {
    private FindnoDAO mFindnoDAO;
    private static final String USER_ID_KEY = "com.daclink.gymlog_v_sp22.userIdKey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_all_orders);
        getDatabase();
        createDisplay();
    }

    /*Creates a linear view that creates items depending on the amount parsed in, in this case I use a for loop
    generating a new item for each order, allowing the user to see through all its orders. It also allows
    The user to cancel an order, by clicking on one of its orders.
    * */
    private void createDisplay() {
        int mUserId = getIntent().getIntExtra(USER_ID_KEY, -1);
        List<String> productNames = new ArrayList<>();
        for (OrderHistory order: mFindnoDAO.getAllOrdersByUserID(mUserId)){
            productNames.add(mFindnoDAO.getProductByPId(order.getProductID()).getProductName());
        }
        ListView mlistView = findViewById(R.id.OrderList);
        ArrayAdapter arrayAdapter2 = new ArrayAdapter<>(ShowAllOrders.this,
                android.R.layout.simple_list_item_1,productNames);
        mlistView.setAdapter(arrayAdapter2);
        mlistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String productElement = (String) parent.getItemAtPosition(position);
                Toast.makeText(ShowAllOrders.this, productElement, Toast.LENGTH_SHORT).show();
                AlertDialog.Builder mAlertBuilder = new AlertDialog.Builder(ShowAllOrders.this);
                final AlertDialog mAlertDialog = mAlertBuilder.create();
                mAlertBuilder.setMessage("Do you want to remove your order?");
                mAlertBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                mAlertBuilder.setPositiveButton(" Cancel Order ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Product product = mFindnoDAO.getProductByProductName(productElement);
                        product.setProductCount(mFindnoDAO.getProductByProductName(productElement).getProductCount()+1);
                        Toast.makeText(ShowAllOrders.this, "Order was cancelled", Toast.LENGTH_SHORT).show();
                        OrderHistory order = mFindnoDAO.getOrderbyProductID(mFindnoDAO.getProductByProductName(productElement).getProductId());
                        mFindnoDAO.update(product);
                        mFindnoDAO.delete(order);
                        Intent intent = MainActivity.intentFactory(getApplicationContext(),mUserId);
                        startActivity(intent);
                    }
                });
                mAlertBuilder.create().show();
            }
        });
    }

    //allows users to get to the main page by clicking at the house icon in the header
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

    //Retrieves data from the database
    private void getDatabase() {
        mFindnoDAO = Room.databaseBuilder(this, AppDatabase.class, AppDatabase.DATABASE_NAME)
                .allowMainThreadQueries()
                .build()
                .GymLogDAO();
    }

    //Sends user to this class
    public static Intent intentFactory(Context context, int userId){
        Intent intent = new Intent(context, ShowAllOrders.class);
        intent.putExtra(USER_ID_KEY,userId);
        return intent;
    }
}