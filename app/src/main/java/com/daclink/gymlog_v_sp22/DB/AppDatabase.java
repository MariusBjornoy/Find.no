/* Title: Project Part 02: Find.no Ecommerce Application
* NAME: Marius Roedder Bjoernoey
* Date: 27-11-2022 (Fall 2022)
* Description: This is the Database that units the tables and deliver the attributes we need to access what we need
* to retrieve and send in data.
* */

package com.daclink.gymlog_v_sp22.DB;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.daclink.gymlog_v_sp22.Cart;
import com.daclink.gymlog_v_sp22.IsFavorite;
import com.daclink.gymlog_v_sp22.OrderHistory;
import com.daclink.gymlog_v_sp22.Product;
import com.daclink.gymlog_v_sp22.User;

@Database(entities = {Product.class, User.class, IsFavorite.class, Cart.class, OrderHistory.class}, version = 6)
public abstract class AppDatabase extends RoomDatabase {
    public static final String DATABASE_NAME = "Findno.db";
    public static final String USER_TABLE = "USER_TABLE";
    public static final String PRODUCT_TABLE = "PRODUCT_TABLE";
    public static final String CART_TABLE = "CART_TABLE";
    public static final String ISFAVORITE_TABLE = "ISFAVORITE_TABLE";
    public static final String ORDER_HISTORY = "ORDER_HISTORY_TABLE";

    private static volatile AppDatabase instance;
    private static final Object LOCK = new Object();
    
    public abstract FindnoDAO GymLogDAO();
    public static AppDatabase getInstance(Context context){
        if (instance == null){
            synchronized (LOCK){
                if (instance == null){
                    instance = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class,
                            DATABASE_NAME).build();
                }
            }
        }
        return instance;
    }
}
