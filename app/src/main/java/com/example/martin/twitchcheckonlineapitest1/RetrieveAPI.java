package com.example.martin.twitchcheckonlineapitest1;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;


public class RetrieveAPI extends AsyncTask<String,Void,String> {
    private static final String CLIENT_ID = "whic0bvguxirq6g0v18xbn1vmjfd44";
    private static final String URL_GETSTREAM = "https://api.twitch.tv/kraken/streams/";

    TextView textV;
    ProgressBar progressBar;
    private String addUser;
    private Context context;

    public RetrieveAPI(Context context,TextView textV, ProgressBar progress) {
        this.context=context;
        this.textV = textV;
        this.progressBar = progress;
    }

    @Override
    protected void onPreExecute() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    protected String doInBackground(String... params) {
        addUser = params[0];
        try{
            URL url = new URL(URL_GETSTREAM + addUser);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            //set header
            urlConnection.setRequestProperty("Client-ID", CLIENT_ID);
            try{
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(urlConnection.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line;
                while( (line= reader.readLine()) !=null){
                        sb.append(line).append("\n");
                }
                reader.close();
                return sb.toString();
            }finally {
                urlConnection.disconnect();
            }
        }catch (IOException e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(String response) {
        progressBar.setVisibility(View.GONE);
        try{
            String stream;

            JSONObject json = new JSONObject(response);
            stream = json.getString("stream");

            if(stream.equalsIgnoreCase("null")){
                textV.setText(addUser.toUpperCase() + " is OFFLINE");
                Drawable img = ContextCompat.getDrawable(context, R.drawable.redcross);
                img.setBounds(0,0,60,60);
                textV.setCompoundDrawables(null,null,img,null);
            }else{
                textV.setText(addUser.toUpperCase() + " is STREAMING!");
                textV.setTextColor(Color.parseColor("#372A6E"));
                Drawable img = ContextCompat.getDrawable(context, R.drawable.greentick);
                img.setBounds(0,0,60,60);
                textV.setCompoundDrawables(null,null,img,null);
            }
        }catch (JSONException e){
            Log.e("JSON Parser", "Error parsing " + e.toString());
        }
    }
}