/* Title: Project Part 02: Find.no Ecommerce Application
 * NAME: Marius Roedder Bjoernoey
 * Date: 27-11-2022 ()
 * Description: Table for Cart items, containing all necessary attributes.
 * */

package com.daclink.gymlog_v_sp22;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.daclink.gymlog_v_sp22.DB.AppDatabase;

@Entity(tableName = AppDatabase.CART_TABLE)
public class Cart {

    @PrimaryKey(autoGenerate = true)
    private int CartID;


    private int mUserID;
    private int mProductID;

    public int getUserID() {
        return mUserID;
    }

    public void setUserID(int userID) {
        mUserID = userID;
    }

    public int getProductID() {
        return mProductID;
    }

    public void setProductID(int productID) {
        mProductID = productID;
    }

    public Cart(int userID, int productID) {
        mUserID = userID;
        mProductID = productID;
    }



    public int getCartID() {
        return CartID;
    }

    public void setCartID(int cartID) {
        CartID = cartID;
    }
}
