/* Title: Project Part 02: Find.no Ecommerce Application
 * NAME: Marius Roedder Bjoernoey
 * Date: 27-11-2022 ()
 * Description: Table for all orders made
 * */

package com.daclink.gymlog_v_sp22;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.daclink.gymlog_v_sp22.DB.AppDatabase;

@Entity(tableName = AppDatabase.ORDER_HISTORY)
public class OrderHistory {
    @PrimaryKey(autoGenerate = true)
    private int mLogNumber;

    private int mUserID;
    private int productID;

    public OrderHistory(int userID, int productID) {
        mUserID = userID;
        this.productID = productID;
    }


    public int getLogNumber() {
        return mLogNumber;
    }

    public void setLogNumber(int logNumber) {
        mLogNumber = logNumber;
    }

    public int getUserID() {
        return mUserID;
    }

    public void setUserID(int userID) {
        mUserID = userID;
    }

    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }
}
