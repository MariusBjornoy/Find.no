/* Title: Project Part 02: Find.no Ecommerce Application
 * NAME: Marius Roedder Bjoernoey
 * Date: 27-11-2022 (Fall 2022)
 * Description: Shows the product page. This page displays information about the product and allows the user
 * To make purchases, or adding the products to their favorite list
 * */

package com.daclink.gymlog_v_sp22;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Delete;
import androidx.room.Room;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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

public class ProductPage extends AppCompatActivity {
    private static final String PRODUCT_NAME = "com.daclink.gymlog_v_sp22.ProductName";
    private static final String PUSER_ID_KEY = "com.daclink.gymlog_v_sp22.puserIdKey";
    private static final String USER_ID_KEY = "com.daclink.gymlog_v_sp22.userIdKey";
    private static final String PRODUCT_DESC_TEXT = "com.daclink.gymlog_v_sp22.ProductDescription";
    private static final String PRODUCT_PRICE = "com.daclink.gymlog_v_sp22.ProductPrice";
    private static final String PRODUCT_COUNT = "com.daclink.gymlog_v_sp22.ProductCount";
    private FindnoDAO mFindnoDAO;


    //TV for TEXT VIEW refewring to the fields in the xml file Product_page.xml.
    TextView mProductTitleTV;
    TextView mProductDescriptionTV;
    TextView mProductCountTV;
    TextView mProductPriceTV;
    Button mAddToCartButton;
    Button mBuyNow;
    Button mAddToFavorite;

    String mProductTitle;
    String mProductDescription;
    int mProductCount;
    Double mProductPrice;
    int mUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_page);
        getDatabase();

        mProductTitleTV = findViewById(R.id.ProductTitlePP);
        mProductDescriptionTV = findViewById(R.id.productDescriptionPP);
        mProductCountTV = findViewById(R.id.ProductCountPP);
        mProductPriceTV = findViewById(R.id.ProductPricePP);
        mAddToCartButton = findViewById(R.id.AddToCart);
        mBuyNow = findViewById(R.id.BuyNow);
        mAddToFavorite = findViewById(R.id.Favorite);

        Intent intent = getIntent();
        mProductTitle = intent.getStringExtra(PRODUCT_NAME);
        mProductDescription = intent.getStringExtra(PRODUCT_DESC_TEXT);
        mProductCount = intent.getIntExtra(PRODUCT_COUNT, -1);
        mProductPrice = intent.getDoubleExtra(PRODUCT_PRICE, -1.0);
        mUserId = intent.getIntExtra(USER_ID_KEY,-1);

        mProductTitleTV.setText(mProductTitle);
        mProductDescriptionTV.setText(mProductDescription);
        mProductPriceTV.setText(mProductPrice+"$");


        //Checks that there is more available items for sale
        Product product = mFindnoDAO.getProductByProductName(mProductTitle);
        if(product.getProductCount() <= 0){
            mProductCountTV.setText("Currently Sold out");
            mProductCountTV.setTextSize(24);
            mBuyNow.setVisibility(View.GONE);
            mAddToCartButton.setVisibility(View.GONE);
            mProductCountTV.setTextColor(Color.parseColor("#FF0000"));
        }else{
            mProductCountTV.setText("QTY: " + mProductCount);
        }

        addToCart();
        addToFavorite();
        buyNow();
    }

    //Retrieves all data from the database
    private void getDatabase() {
        mFindnoDAO = Room.databaseBuilder(this, AppDatabase.class, AppDatabase.DATABASE_NAME)
                .allowMainThreadQueries()
                .build()
                .GymLogDAO();
    }

    //Allows user to buy a product right away.
    private void buyNow() {
        mBuyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Product product = mFindnoDAO.getProductByProductName(mProductTitle);
                if(product.getUserId() != mUserId) {
                    product.setProductCount(product.getProductCount() - 1);

                    mFindnoDAO.update(product);
                    OrderHistory order = new OrderHistory(mUserId, product.getProductId());
                    mFindnoDAO.insert(order);

                    Intent intent = MainActivity.intentFactory(getApplicationContext(), mUserId);
                    startActivity(intent);
                }else{
                    Toast.makeText(ProductPage.this, "This is your product, you Goofball", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //Allows user to add or remove item from favorites, if the item is in the database already it gets removed from favorites
    private void addToFavorite() {
        mAddToFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IsFavorite favorite = mFindnoDAO.checkIfFavoriteIsSet(mFindnoDAO.getProductByProductName(mProductTitle).getProductId());
                IsFavorite newfavorite = new IsFavorite(mUserId,mFindnoDAO.getProductByProductName(mProductTitle).getProductId());
                if (favorite == null){
                    mFindnoDAO.insert(newfavorite);
                    Toast.makeText(ProductPage.this, "Your item was added to favorites",Toast.LENGTH_SHORT).show();
                }else{
                    mFindnoDAO.delete(favorite);
                    Toast.makeText(ProductPage.this, "Item was removed from favorites", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //If item is added to cart it is pushed to the cart table. The count does not change at this point
    private void addToCart() {
        mAddToCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Product product = mFindnoDAO.getProductByProductName(mProductTitle);
                if(product.getUserId() != mUserId) {
                    Cart cart = new Cart(mUserId, mFindnoDAO.getProductByProductName(mProductTitle).getProductId());
                    mFindnoDAO.insert(cart);
                    Toast.makeText(ProductPage.this, "Product was added to your Cart", Toast.LENGTH_SHORT).show();
                    Intent intent = UserCart.intentFactory(getApplicationContext(), mUserId);
                    startActivity(intent);
                }else{
                    Toast.makeText(ProductPage.this, "you already own this product, silly", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //gives user possibility to go back to main page
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

    //retrieve all data needed to display the page.An alternative way of doing this with less lines of code would be
    //to use the PID and a query to collect the data, instead of sending everything.
    public static Intent intentFactory(Context context, String productName, String productDescription, double productPrice, int productCount, int puserId,int userId){
        Intent intent = new Intent(context, ProductPage.class);
        intent.putExtra(PRODUCT_NAME, productName);
        intent.putExtra(PRODUCT_DESC_TEXT, productDescription);
        intent.putExtra(PRODUCT_PRICE, productPrice);
        intent.putExtra(PRODUCT_COUNT, productCount);
        intent.putExtra(PUSER_ID_KEY, puserId);
        intent.putExtra(USER_ID_KEY,userId);
        return intent;
    }
}