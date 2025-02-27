/* Title: Project Part 02: Find.no Ecommerce Application
 * NAME: Marius Roedder Bjoernoey
 * Date: 27-11-2022 ()
 * Description: TAble for all product attributes
 * */

package com.daclink.gymlog_v_sp22;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.daclink.gymlog_v_sp22.DB.AppDatabase;

@Entity(tableName = AppDatabase.PRODUCT_TABLE)
public class Product {


    @PrimaryKey(autoGenerate = true)
    private int mProductId;

    private String mProductName;
    private String mProductDescription;
    private double mProductPrice;
    private int mProductCount;
    private int mUserId;

    public Product(String productName, String productDescription, double productPrice, int productCount,  int userId) {
        mProductName = productName;
        mProductDescription = productDescription;
        mProductPrice = productPrice;
        mProductCount = productCount;
        mUserId = userId;
    }

    public String getProductName() {
        return mProductName;
    }

    public void setProductName(String productName) {
        mProductName = productName;
    }

    public String getProductDescription() {
        return mProductDescription;
    }

    public void setProductDescription(String productDescription) {
        mProductDescription = productDescription;
    }

    public double getProductPrice() {
        return mProductPrice;
    }

    public void setProductPrice(double productPrice) {
        mProductPrice = productPrice;
    }

    public int getProductCount() {
        return mProductCount;
    }

    public void setProductCount(int productCount) {
        mProductCount = productCount;
    }

    public int getUserId() {
        return mUserId;
    }

    public void setUserId(int userId) {
        mUserId = userId;
    }

    public int getProductId() {
        return mProductId;
    }

    public void setProductId(int productId) {
        mProductId = productId;
    }
}
