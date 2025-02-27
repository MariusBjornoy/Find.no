/* Title: Project Part 02: Find.no Ecommerce Application
 * NAME: Marius Roedder Bjoernoey
 * Date: 27-11-2022
 * Description: This class allows the Admin to delete products from the database.
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

public class DeleteProductAdmin extends AppCompatActivity {
    private FindnoDAO mFindnoDAO;
    private static final String USER_ID_KEY = "com.daclink.gymlog_v_sp22.userIdKey";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_product_admin);
        getDatabase();
        createDisplay();
    }

    /*Creates the display by making a for loop that loops through all products in the database.
    When the admin clicks on one of the items he gets the possibility to delete that item*/
    private void createDisplay(){
        List<String> usernames = new ArrayList<>();
        for (Product p: mFindnoDAO.getAllProducts()){
            usernames.add(p.getProductName());
        }
        ListView listView = findViewById(R.id.DeleteproductsAdmin);
        ArrayAdapter arrayAdapter = new ArrayAdapter<>(DeleteProductAdmin.this,
                android.R.layout.simple_list_item_1,usernames);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int mUserId = getIntent().getIntExtra(USER_ID_KEY, -1);
                String productName = (String) parent.getItemAtPosition(position);
                Product product = mFindnoDAO.getProductByProductName(productName);
                Toast.makeText(DeleteProductAdmin.this,product.getProductName() + " was deleted",Toast.LENGTH_SHORT).show();
                mFindnoDAO.delete(product);
                Intent intent = DeleteProductAdmin.intentFactory(getApplicationContext(),mUserId);
                startActivity(intent);
                }
        });
    }

    //Retrieves the data from the database.
    private void getDatabase() {
        mFindnoDAO = Room.databaseBuilder(this, AppDatabase.class, AppDatabase.DATABASE_NAME)
                .allowMainThreadQueries()
                .build()
                .GymLogDAO();
    }

    //Creates a Go to home screen option in the header
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

    //Intent factory sends the user to this class upon activation. User Id is something that needs to be parsed in.
    public static Intent intentFactory(Context context, int userId){
        Intent intent = new Intent(context, DeleteProductAdmin.class);
        intent.putExtra(USER_ID_KEY, userId);
        return intent;
    }
}