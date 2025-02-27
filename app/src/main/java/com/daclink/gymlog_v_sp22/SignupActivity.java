/* Title: Project Part 02: Find.no Ecommerce Application
 * NAME: Marius Roedder Bjoernoey
 * Date: 27-11-2022 ()
 * Description: Purpose if this class is to register new users to the database.
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

public class SignupActivity extends AppCompatActivity {
    private Button mSignupButton;
    private Button mLogingRef;
    private EditText mUsernameField;
    private EditText mPasswordField;
    private String mUsername;
    private String mPassword;

    private FindnoDAO mFindnoDAO;
    private static final String admin  = "false";

    private User mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        wireUpDisplay();

        getDatabase();
        //Refers to the login page if the user wants to log in instead of creating an account.
        mLogingRef.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = LoginActivity.intentFactory(getApplicationContext());
                startActivity(intent);
            }
        });

        //Tries to create a new user in the database upon button click
        mSignupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getValuesFromDisplay();
                if (!checkUserName()){
                    Toast.makeText(SignupActivity.this,"UserName already exists", Toast.LENGTH_SHORT).show();
                }else{
                    addUserToDataBase();
                    User signedUpUser = mFindnoDAO.getUserByUsername(mUsername);
                    Intent intent = MainActivity.intentFactory(getApplicationContext(), signedUpUser.getUserId());
                    startActivity(intent);
                }
            }
        });
    }

    //wires up the variables to the XML views
    private void wireUpDisplay() {
        mSignupButton = findViewById(R.id.Signup_button_from_SUpage);
        mLogingRef = findViewById(R.id.LoginReferenceButton);
        mUsernameField = findViewById(R.id.username_register);
        mPasswordField = findViewById(R.id.password_register);
    }

    //Converts the values into strings
    private void getValuesFromDisplay(){
        mUsername = mUsernameField.getText().toString();
        mPassword = mPasswordField.getText().toString();
    }

    //Checks if username already exists
    private boolean checkUserName(){
        mUser = mFindnoDAO.getUserByUsername(mUsername);
        if (mUser == null){
            return true;
        }else{
            Toast.makeText(this, " Username Already exists ",Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    //Adds the user to the database using the insert function.
    private void addUserToDataBase(){
        User newUser = new User(admin, mUsername, mPassword);
        mFindnoDAO.insert(newUser);
    }

    //Retrieves data from the database
    private void getDatabase() {
        mFindnoDAO = Room.databaseBuilder(this, AppDatabase.class, AppDatabase.DATABASE_NAME)
                .allowMainThreadQueries()
                .build()
                .GymLogDAO();
    }

    //sends the user to this class upon activation, at this point the user doesn't have any data needed to get transferred.
    public static Intent intentFactory(Context context){
        Intent intent = new Intent(context,SignupActivity.class);
        return intent;
    }
}