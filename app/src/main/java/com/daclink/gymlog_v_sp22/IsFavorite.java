/* Title: Project Part 02: Find.no Ecommerce Application
 * NAME: Marius Roedder Bjoernoey
 * Date: 27-11-2022 (Fall 2022)
 * Description: Table for favorite listings, it uses product IDs and User IDs as foreign keys, making it easy to
 * filter through all the different objects.
 * */

package com.daclink.gymlog_v_sp22;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.daclink.gymlog_v_sp22.DB.AppDatabase;

@Entity(tableName = AppDatabase.ISFAVORITE_TABLE)
public class IsFavorite {

    @PrimaryKey(autoGenerate = true)
    private int isFavoriteId;

    private int mUserId;
    private int mProductId;



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

    public IsFavorite(int userId, int productId) {
        mUserId = userId;
        mProductId = productId;
    }

    public int getIsFavoriteId() {
        return isFavoriteId;
    }

    public void setIsFavoriteId(int isFavoriteId) {
        this.isFavoriteId = isFavoriteId;
    }
}
