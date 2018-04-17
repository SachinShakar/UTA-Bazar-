package com.advse.universitybazaar.register;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by sachin on 2/14/18.
 */

public class EmailActivity extends AsyncTask<String,String, String> {
    private Context context;
    private String email;

    public EmailActivity(Context context, String email) {
        this.context = context;
        this.email = email;
    }

    @Override
    protected String doInBackground(String... arg0) {
        try {
            String s = (String) arg0[0];
            String link = "https://utalibrary.000webhostapp.com/email.php?email=" + s;
            URL url = new URL(link);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;
            // Read Server Response
            while ((line = reader.readLine()) != null) {
                sb.append(line);
                break;
            }


            return sb.toString();
        }
        catch (Exception e) {
            return new String("Exception: " + e.getMessage());
        }
    }

    @Override
    protected void onPostExecute(String result) {
        //Toast.makeText(context,result,Toast.LENGTH_SHORT).show();

    }

}
