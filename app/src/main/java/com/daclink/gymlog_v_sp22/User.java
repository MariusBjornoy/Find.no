/*NAME: Marius Roedder Bjoernoey
 * Date: 11-13-2022
 * Description: This is where we define our User Attributes. The usertable has three attributes
 * that need to be defined, and one identifier which is automaticly generated. This identifier also
 * works as a primary key, meaning each key has a unique value, this is so that we can find a specific
 * user.
 *
 * Other than the attributes to the table, the class mostly contains getters and setters to both define
 * and gather information about our values.
 * */

package com.daclink.gymlog_v_sp22;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.daclink.gymlog_v_sp22.DB.AppDatabase;

@Entity(tableName = AppDatabase.USER_TABLE)
public class User {

    @PrimaryKey(autoGenerate = true)
    private int mUserId;

    private String mUserName;
    private String mPassword;
    private String mIsAdmin;

    public User(String isAdmin, String userName, String password) {
        mIsAdmin = isAdmin;
        mUserName = userName;
        mPassword = password;
    }

    public int getUserId() {
        return mUserId;
    }

    public void setUserId(int userId) {
        mUserId = userId;
    }

    public String getUserName() {
        return mUserName;
    }

    public void setUserName(String userName) {
        mUserName = userName;
    }

    public String getPassword() {
        return mPassword;
    }

    public void setPassword(String password) {
        mPassword = password;
    }

    public String getIsAdmin() {
        return mIsAdmin;
    }

    public void setIsAdmin(String isAdmin) {
        mIsAdmin = isAdmin;
    }

    @Override
    public String toString() {
        return "User{" +
                "mUserId=" + mUserId +
                ", mUserName='" + mUserName + '\'' +
                ", mPassword='" + mPassword + '\'' +
                ", mIsAdmin='" + mIsAdmin + '\'' +
                '}';
    }
}

