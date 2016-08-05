package com.example.pyojihye.airpollution;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by PYOJIHYE on 2016-08-04.
 */
public  class HttpConnection extends AsyncTask<String, String, String> {

    Context connectContext;
    public static final int CONNECTION_TIMEOUT=10000;
    public static final int READ_TIMEOUT=15000;
    HttpURLConnection conn;
    URL url = null;

    public HttpConnection(Context connectContext,String... str) {
        this.connectContext=connectContext;
    }

    @Override
    protected String doInBackground(String... str) {
        try{
            // Enter URL address where your php file resides
            url = new URL("http://teama-iot.calit2.net/slim/recieveData.php/rcvJSON");
            //url = new URL("http://teamb-iot.calit2.net/week3b/bluebase/receive/recieveData.php/rcvJSON");
            //url = new URL("http://teamc-iot.calit2.net/IOT/public/Login");

        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "MalformedURLException";
        }

        try {
            // Setup HttpURLConnection class to send and receive data from php and mysql
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(READ_TIMEOUT);
            conn.setRequestProperty("Cache-Control", "no-cache");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            conn.setConnectTimeout(CONNECTION_TIMEOUT);
            conn.setRequestMethod("POST");

            // setDoInput and setDoOutput method depict handling of both send and receive
            conn.setDoInput(true);
            conn.setDoOutput(true);

            // Append str to URL
            JSONObject json = new JSONObject();

            try{
                json.put("email", str[0]);
                json.put("pwd", str[1]);

                if(str.length>2){
                    json.put("checkpwd", str[2]);
                    json.put("fname", str[3]);
                    json.put("lname", str[4]);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                return "JSON Exception";
            }

            String body = json.toString();

            // Open connection for sending data
            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(body);
            writer.flush();
            writer.close();
            os.close();
            conn.connect();
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
            Log.d("where",e1.getMessage());
            return "IOException";
        } catch(Exception e){
            Log.w("Unexpected", e.getMessage());
            return "Exception";
        }

        try {
            int response_code = conn.getResponseCode();
            // Check if successful connection made
            if (response_code == HttpURLConnection.HTTP_OK) {
                // Read data sent from server
                InputStream input = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                StringBuilder result = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    result.append(line);
            }
                // Pass data to onPostExecute method
                return (result.toString());
            } else{
                return ("unsuccessful");
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "exception";
        }        catch(Exception e){
            Log.w("Unexpected", e.getMessage());
            return null;
        }finally {
            conn.disconnect();
        }
    }

    @Override
    protected void onPostExecute(String result) {

        try{
            JSONObject jsonObject = new JSONObject(result);
            //Toast.makeText(connectContext, jsonObject.getString("pwd"), Toast.LENGTH_SHORT).show();
            Toast.makeText(connectContext, result, Toast.LENGTH_SHORT).show();

        }catch (JSONException e){
            e.printStackTrace();

        }

//        if (result.equalsIgnoreCase("true")) {
//            Toast.makeText(connectContext,"Success!", Toast.LENGTH_SHORT).show();
//
//        } else if (result.equalsIgnoreCase("false")) {
//            Toast.makeText(connectContext,"Failed!", Toast.LENGTH_SHORT).show();
//
//        } else if (result.equalsIgnoreCase("exception")) {
//            Toast.makeText(connectContext,"exception!", Toast.LENGTH_SHORT).show();
//
//        }else if (result.equalsIgnoreCase("unsuccessful")){
//            Toast.makeText(connectContext,"unsuccessful!", Toast.LENGTH_SHORT).show();
//
//        }else if(result.equalsIgnoreCase("")){
//            Toast.makeText(connectContext,"!", Toast.LENGTH_SHORT).show();

//        }else{
//            JSONObject jsonObject = new JSONObject(result);
//            Toast.makeText(connectContext, result, Toast.LENGTH_SHORT).show();
//        }
    }
}
