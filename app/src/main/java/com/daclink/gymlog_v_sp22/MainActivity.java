/* Title: Project Part 02: Find.no Ecommerce Application
 * NAME: Marius Roedder Bjoernoey
 * Date: 27-11-2022 (Fall 2022)
 * Description: This page is kind of like a hub for all the functionality given to the user.
 * The user clicks on whatever it wants to do and the page redirects him/her.
 * */


package com.daclink.gymlog_v_sp22;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.room.Room;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.daclink.gymlog_v_sp22.DB.AppDatabase;
import com.daclink.gymlog_v_sp22.DB.FindnoDAO;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String USER_ID_KEY = "com.daclink.gymlog_v_sp22.userIdKey";
    private static final String PREFERENCES_KEY = "com.daclink.gymlog_v_sp22.PREFERENCES_KEY";

    private SharedPreferences mPreferences = null;


    private Button mAdmin;
    private Button mDeleteUser;
    private Button mAllProducts;
    private TextView mWelcome;
    private Button mAddItem;
    private Button mViewListings;
    private Button mPromoButton;
    private Button mFavoriteList;
    private Button mOrderList;




    private FindnoDAO mFindnoDAO;

    private User mUser;
    private int mUserId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getDatabase();
        checkForUser();
        addsampleproducts();

        mAdmin = findViewById(R.id.textViewAdmin);
        mWelcome = findViewById(R.id.welcomemessage);
        mDeleteUser = findViewById(R.id.DeleteUser);
        mAllProducts = findViewById(R.id.showallproducts);
        mViewListings = findViewById(R.id.viewMyProducts);
        mAddItem = findViewById(R.id.addProduct);
        mPromoButton = findViewById(R.id.Promo);
        mFavoriteList = findViewById(R.id.FavoritesList);
        mOrderList = findViewById(R.id.Orders);

        //Using prefernces allows the user to be logged in for quite some time
        addUserToPreference(mUserId);
        loginUser(mUserId);

        //Gives a personal message to the user
        if (mUser!=null && mUser.getUserName() != null){
            mWelcome.setText("welcome " + mUser.getUserName() + " it is great to see you!");
        }

        if(mUser!= null && (mUser.getIsAdmin().equals("true"))){
            mAdmin.setVisibility(View.VISIBLE);
        }else {
            mAdmin.setVisibility(View.GONE);
        }

        //sends user to favorite list
        mFavoriteList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = FavoriteList.intentFactory(getApplicationContext(),mUserId);
                startActivity(intent);
            }
        });

        //Sends user to all its orders
        mOrderList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = ShowAllOrders.intentFactory(getApplicationContext(),mUserId);
                startActivity(intent);
            }
        });


        //Send admin to the promo side where he can add discounts to all products
        mPromoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = PromoAdmin.intentFactory(getApplicationContext(),mUserId);
                startActivity(intent);
            }
        });

        if(mUser!= null && (mUser.getIsAdmin().equals("true"))){
            mPromoButton.setVisibility(View.VISIBLE);
        }else {
            mPromoButton.setVisibility(View.GONE);
        }

        //sends user to the additem page
        mAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = AddItem.intentFactory(getApplicationContext(),mUserId);
                startActivity(intent);
            }
        });

        //Sends user to the page where all its own products are located.
        mViewListings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = ShowAllUserListings.intentFactory(getApplicationContext(),mUserId);
                startActivity(intent);
            }
        });

        //sends the user to the page where all products are located
        mAllProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = ShowAllProducts.intentFactory(getApplicationContext(),mUserId);
                startActivity(intent);
            }
        });

        // Allows admin to be redirected to either the page to delete a user or to delete a product
        mAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder mAlertBuilder = new AlertDialog.Builder(MainActivity.this);
                final AlertDialog mAlertDialog = mAlertBuilder.create();
                mAlertBuilder.setMessage("Do you want to delete a User or product");
                mAlertBuilder.setPositiveButton(" Delete a Product ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                       Intent intent = DeleteProductAdmin.intentFactory(getApplicationContext(), mUserId);
                       startActivity(intent);
                    }
                });
                mAlertBuilder.setNegativeButton(" Delete Users ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = ShowAllUsers.intentFactory(getApplicationContext(), mUserId);
                        startActivity(intent);
                    }
                });
                mAlertBuilder.create().show();

            }
        });

        //Allows user(Not admin) to delete its own account
        mDeleteUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder mAlertBuilder = new AlertDialog.Builder(MainActivity.this);
                final AlertDialog mAlertDialog = mAlertBuilder.create();
                mAlertBuilder.setMessage("Are you sure You want to Delete the user?");
                mAlertBuilder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(mUser.getIsAdmin().equals("true")){
                            Toast.makeText(MainActivity.this, "Can not delete an admin account", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(MainActivity.this, "Your account has been deleted", Toast.LENGTH_SHORT).show();
                            Intent intent = LoginActivity.intentFactory(getApplicationContext());
                            startActivity(intent);
                            mFindnoDAO.delete(mFindnoDAO.getUserByUserId(mUserId));
                        }
                    }
                });
                mAlertBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mAlertDialog.dismiss();
                    }
                });
                mAlertBuilder.create().show();
                }


        });

    }

    //Adds some sample products with no owner (UserID =-1)
    private void addsampleproducts() {
        List<Product> allProducts = mFindnoDAO.getAllProducts();
        if(allProducts.size()<=0){
            Product speakers = new Product("Speakers",
                    "Good condition speakers for sale",
                    32.5,2,mUserId
                    );
            Product cats = new Product("Cat",
                    "I have an unpleasent amount of cats, please buy them",
                    10.0, 23, mUserId);

            mFindnoDAO.insert(cats);
            mFindnoDAO.insert(speakers);
        }
    }

    //Logs in user and gets the data parsed in through the login page.
    private void loginUser(int userId){
        mUser = mFindnoDAO.getUserByUserId(userId);
        invalidateOptionsMenu();
    }

    //adds the user to preference allowing the user to stay logged in for quite some time.
    private void addUserToPreference(int userId){
        if (mPreferences == null){
            getPrefs();
        }
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putInt(USER_ID_KEY,userId);
    }

    //Removes user from intents and prefs.
    private void clearUserFromIntent(){
        getIntent().putExtra(USER_ID_KEY,-1);
    }
    private void clearUserFromPref(){
        addUserToPreference(-1);
    }

    //Retrieves the preference
    private void getPrefs(){
        mPreferences = this.getSharedPreferences(PREFERENCES_KEY, Context.MODE_PRIVATE);
    }

    //Retrieves all the data from the database
    private void getDatabase(){
        mFindnoDAO = Room.databaseBuilder(this, AppDatabase.class, AppDatabase.DATABASE_NAME)
                .allowMainThreadQueries()
                .build()
                .GymLogDAO();
    }

    //Checks if the user is logged in if not, it has to log in. If there are no users in the database two users are created
    private void checkForUser() {
        mUserId = getIntent().getIntExtra(USER_ID_KEY, -1);
        if (mUserId != -1){
            return;
        }
         if (mPreferences == null){
            getPrefs();
        }
        mUserId = mPreferences.getInt(USER_ID_KEY,-1);

        if(mUserId != -1){
             return;
        }
        List<User> users = mFindnoDAO.getAllUsers();
        if (users.size() <=0 ){
            User adminUser = new User( "true","admin2","admin2");
            User testUser = new User( "false","testuser1","testuser1");
            mFindnoDAO.insert(adminUser);
            mFindnoDAO.insert(testUser);
        }
        Intent intent = LoginActivity.intentFactory(this);
        startActivity(intent);
    }

    //Creates the header containing the search bar, logout and cart option
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if(mUser != null){
            MenuItem item = menu.findItem(R.id.logoutItem);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.logoutItem:
                Toast.makeText(this,"Logout selected", Toast.LENGTH_SHORT).show();
                clearUserFromIntent();
                clearUserFromPref();
                mUserId =-1;
                checkForUser();
                return true;
            case R.id.CartIcon:
                Intent intent = UserCart.intentFactory(getApplicationContext(),mUserId);
                startActivity(intent);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.example_menu, menu);

        MenuItem searchViewItem = menu.findItem(R.id.app_bar_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchViewItem);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                    getSearchResaults(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    //checks to see if there are any items in the database matching the item, if there is the user gets redirected to that item
    private void getSearchResaults(String search){
        for(Product p: mFindnoDAO.getAllProducts()) {
            String productName = p.getProductName();
            String pName = productName.toLowerCase();
            String sName = search.toLowerCase();
            if (pName.equals(sName)) {
                Toast.makeText(MainActivity.this, "Found an item!", Toast.LENGTH_SHORT).show();
                Intent intent = ProductPage.intentFactory(getApplicationContext(),p.getProductName(),p.getProductDescription(),p.getProductPrice(),p.getProductCount(),p.getUserId(),mUserId);
                startActivity(intent);
            }
        }
    }

    //intent factory sends user to the mainpage
    public static Intent intentFactory(Context context, int userId){
        Intent intent = new Intent(context,MainActivity.class);
        intent.putExtra(USER_ID_KEY, userId);
        return intent;
    }
}