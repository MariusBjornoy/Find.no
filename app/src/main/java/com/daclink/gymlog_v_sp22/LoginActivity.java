/* Title: Project Part 02: Find.no Ecommerce Application
 * NAME: Marius Roedder Bjoernoey
 * Date: 27-11-2022 ()
 * Description: The login page is the first page the user is greeted with. It has an option to redirect the user to a signup page.
 * The users username and password is compared in the database to test if the user exists in the database.
 * */

package com.daclink.gymlog_v_sp22;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.daclink.gymlog_v_sp22.DB.AppDatabase;
import com.daclink.gymlog_v_sp22.DB.FindnoDAO;

public class LoginActivity extends AppCompatActivity {

    private EditText mUsername;

    private EditText mPassword;

    private Button mButton;
    private Button mSignUpButton;

    private FindnoDAO mFindnoDAO;

    private String mUsernameString;
    private String mPasswordString;
    private User mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        wireupDisplay();

        getDatabase();

    }

    //Retrieves data from the database
    private void getDatabase() {
        mFindnoDAO = Room.databaseBuilder(this, AppDatabase.class, AppDatabase.DATABASE_NAME)
                .allowMainThreadQueries()
                .build()
                .GymLogDAO();
    }

    //Connects input fields and variables. Also defines the functionality if the login and sign up button.
    private void wireupDisplay(){
        mUsername = findViewById(R.id.editTextLoginUserName);
        mPassword = findViewById(R.id.editTextLoginPassword);
        mButton = findViewById(R.id.buttonLogin);
        mSignUpButton = findViewById(R.id.Signup_button);

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getValuesFromDisplay();
                if (checkForUserInDataBase()) {
                    if (!validatePassword()) {
                        Toast.makeText(LoginActivity.this,"INVALID PASSWORD", Toast.LENGTH_SHORT).show();
                    }else{
                        Intent intent = MainActivity.intentFactory(getApplicationContext(), mUser.getUserId());
                        startActivity(intent);
                    }
                }
            }
        });

        mSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = SignupActivity.intentFactory(getApplicationContext());
                startActivity(intent);
            }
        });
    }

    //Checks if password is correct
    private boolean validatePassword(){
        return mUser.getPassword().equals(mPasswordString);
    }

    //converts input values to String values
    private void getValuesFromDisplay(){
        mUsernameString = mUsername.getText().toString();
        mPasswordString = mPassword.getText().toString();
    }

    //Checks if the username is already in th edatabase
    private boolean checkForUserInDataBase(){
        mUser = mFindnoDAO.getUserByUsername(mUsernameString);
        if (mUser == null){
            Toast.makeText(this, "No user " + mUsernameString,Toast.LENGTH_SHORT).show();
            return false;
        }else{
            return true;
        }
    }

    //intent factory sends the user to this page.
    public static Intent intentFactory(Context context){
        Intent intent = new Intent(context,LoginActivity.class);
        return intent;
    }

}