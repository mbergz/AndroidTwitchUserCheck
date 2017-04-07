package com.example.martin.twitchcheckonlineapitest1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.HashMap;


public class MainScreen extends AppCompatActivity {
    private String add_user;
    private MyDBHandler dbHandler;
    private HashMap<String,TextView> mapUserTextView;
    ProgressBar progressBar;
    EditText addStreamer;
    LinearLayout linearLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        addStreamer = (EditText) findViewById(R.id.editText_addStreamer);
        linearLayout = (LinearLayout) findViewById(R.id.linearLayout_userInfo);
        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        mapUserTextView = new HashMap<String, TextView>();
        dbHandler = new MyDBHandler(this,null,null,1);
        initializeUsersFromDB();
    }

    private void initializeUsersFromDB(){
        ArrayList<String> list = dbHandler.getUsers();
        if(!list.isEmpty()){
            for(String user:list){
                TextView rowTextView = new TextView(this);
                mapUserTextView.put(user,rowTextView);
                linearLayout.addView(rowTextView);
                new RetrieveAPI(getBaseContext(),rowTextView,progressBar).execute(user);
            }
        }
    }

    private void updateTextViews(){
        ArrayList<String> list = dbHandler.getUsers();
        for(String user: mapUserTextView.keySet()){
            if(!list.contains(user)){
                mapUserTextView.get(user).setVisibility(View.GONE);
            }else{
                TextView txt = mapUserTextView.get(user);
                new RetrieveAPI(getBaseContext(),txt,progressBar).execute(user);
            }
        }
    }

    private boolean searchUserDB(String username){
        return dbHandler.checkUser(username);
    }

    public void onAddButtonClick(View view) {
        add_user =  addStreamer.getText().toString().trim();
        if(!searchUserDB(add_user)){
            dbHandler.addUser(add_user);
            TextView txtV = new TextView(this);
            linearLayout.addView(txtV);

            RetrieveAPI getAPI = new RetrieveAPI(getBaseContext(),txtV,progressBar);
            getAPI.execute(add_user);

            mapUserTextView.put(add_user,txtV);
            updateTextViews();
        }else{
            Toast.makeText(getApplicationContext(), "User is already in database", Toast.LENGTH_SHORT).show();
        }
    }

    public void onRemoveButtonClick(View view){
        String remove_user =  addStreamer.getText().toString().trim();
        if(searchUserDB(remove_user)){
            dbHandler.removeUser(remove_user);
            updateTextViews();
            Toast.makeText(getApplicationContext(), "User: "+remove_user+" successfully deleted", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(getApplicationContext(), "User is not in database, check spelling", Toast.LENGTH_SHORT).show();
        }

    }
}
