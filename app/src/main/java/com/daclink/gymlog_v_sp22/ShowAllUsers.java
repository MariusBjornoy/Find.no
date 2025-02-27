/* Title: Project Part 02: Find.no Ecommerce Application
 * NAME: Marius Roedder Bjoernoey
 * Date: 27-11-2022 ()
 * Description: Shows all users in the database. Only visible to the Admin
 * */

package com.daclink.gymlog_v_sp22;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.daclink.gymlog_v_sp22.DB.AppDatabase;
import com.daclink.gymlog_v_sp22.DB.FindnoDAO;

import java.util.ArrayList;
import java.util.List;

public class ShowAllUsers extends AppCompatActivity {

    private FindnoDAO mFindnoDAO;
    private static final String USER_ID_KEY = "com.daclink.gymlog_v_sp22.userIdKey";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_all_users);
        getDatabase();
        createDisplay();
    }

    //Retrieves all the information from the database.
    private void getDatabase(){
        mFindnoDAO = Room.databaseBuilder(this, AppDatabase.class, AppDatabase.DATABASE_NAME)
                .allowMainThreadQueries()
                .build()
                .GymLogDAO();
    }

    //Crteates a display containing all the Users, upon clicing on the user the admin can delete
    //the user from the database.
    private void createDisplay(){
        List<String> usernames = new ArrayList<>();
        for (User u: mFindnoDAO.getAllUsers()){
            usernames.add(u.getUserName());
        }
        ListView listView = findViewById(R.id.allUsers);
        ArrayAdapter arrayAdapter = new ArrayAdapter<>(ShowAllUsers.this,
        android.R.layout.simple_list_item_1,usernames);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String userElement = (String) parent.getItemAtPosition(position);
                if(mFindnoDAO.getUserByUsername(userElement).getIsAdmin().equals("true")) {
                    Toast.makeText(ShowAllUsers.this, "Can not delete an admin", Toast.LENGTH_SHORT).show();
                }else {
                    int mUserId = getIntent().getIntExtra(USER_ID_KEY, -1);
                    Toast.makeText(ShowAllUsers.this, " User " + userElement + " was deleted", Toast.LENGTH_SHORT).show();
                    mFindnoDAO.delete(mFindnoDAO.getUserByUsername(userElement));
                    Intent intent = ShowAllUsers.intentFactory(getApplicationContext(), mUserId);
                    startActivity(intent);
                }
            }
        });
    }

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

    public static Intent intentFactory(Context context, int userId){
        Intent intent = new Intent(context, ShowAllUsers.class);
        intent.putExtra(USER_ID_KEY, userId);
        return intent;
    }


}