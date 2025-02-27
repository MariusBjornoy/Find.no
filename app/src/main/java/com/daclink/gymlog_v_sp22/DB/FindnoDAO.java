/* Title: Project Part 02: Find.no Ecommerce Application
 * NAME: Marius Roedder Bjoernoey
 * Date: 27-11-2022 (Fall 2022)
 * Description: Queries and functions, mainly to send in, modify or delete objects from our database.
 * */

package com.daclink.gymlog_v_sp22.DB;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Index;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.daclink.gymlog_v_sp22.Cart;
import com.daclink.gymlog_v_sp22.IsFavorite;
import com.daclink.gymlog_v_sp22.OrderHistory;
import com.daclink.gymlog_v_sp22.Product;
import com.daclink.gymlog_v_sp22.User;

import java.util.List;

@Dao
public interface FindnoDAO {
    //adds Users to our database
    @Insert
    void insert(User... users);

    //Update user data in the user object
    @Update
    void update(User... users);

    //Deletes a user from the User Table
    @Delete
    void delete(User user);

    @Insert
    void insert(Cart... Carts);

    @Update
    void update(Cart... Carts);

    @Delete
    void delete(Cart carts);

    @Insert
    void insert(IsFavorite... Favorites);

    @Update
    void update(IsFavorite... Favorites);

    @Delete
    void delete(IsFavorite Favorites);

    @Insert
    void insert(Product... products);

    @Update
    void update(Product... products);

    @Delete
    void delete(Product products);

    @Insert
    void insert(OrderHistory... Orders);

    @Update
    void update(OrderHistory... Orders);

    @Delete
    void delete(OrderHistory Orders);

    //the queries collect data, depending on the conditions given. Conditions are given with the key phrase WHERE
    @Query("SELECT * FROM " + AppDatabase.USER_TABLE)
    List<User> getAllUsers();

    @Query("SELECT * FROM " + AppDatabase.PRODUCT_TABLE)
    List<Product> getAllProducts();

    @Query("SELECT * FROM " + AppDatabase.USER_TABLE + " WHERE mUserName = :username")
    User getUserByUsername(String username);

    @Query("SELECT * FROM " + AppDatabase.PRODUCT_TABLE + " WHERE mProductName = :Pname")
    Product getProductByProductName(String Pname);

    @Query("SELECT * FROM " + AppDatabase.USER_TABLE + " WHERE mUserId = :userId")
    User getUserByUserId(int userId);

    @Query("SELECT * FROM " + AppDatabase.PRODUCT_TABLE + " WHERE mProductId = :pId")
    Product getProductByPId(int pId);

    @Query("SELECT * FROM " + AppDatabase.PRODUCT_TABLE + " WHERE mUserId = :UserId")
    List<Product> getAllProductsByUserID(int UserId);

    @Query("SELECT * FROM " + AppDatabase.ORDER_HISTORY + " WHERE mUserId = :UserId")
    List<OrderHistory> getAllOrdersByUserID(int UserId);

    @Query("SELECT * FROM " + AppDatabase.ISFAVORITE_TABLE + " WHERE mUserId = :UserId")
    List<IsFavorite> getAllFavoritesByUserID(int UserId);

    @Query("SELECT * FROM " + AppDatabase.ORDER_HISTORY + " WHERE productID = :productID")
    OrderHistory getOrderbyProductID(int productID);

    @Query("SELECT * FROM " + AppDatabase.ISFAVORITE_TABLE + " WHERE mProductId = :pID")
    IsFavorite getFavoritefromPid(int pID);

    @Query("SELECT * FROM " + AppDatabase.ISFAVORITE_TABLE + " WHERE mProductId = :pID")
    IsFavorite checkIfFavoriteIsSet(int pID);

    @Query("SELECT * FROM " + AppDatabase.CART_TABLE + " WHERE mUserID = :userId")
    List<Cart> getAllItemsInUserCart(int userId);

    @Query("SELECT * FROM " + AppDatabase.CART_TABLE + " WHERE mProductID = :PID")
    Cart getCartItemFromPID(int PID);

}
