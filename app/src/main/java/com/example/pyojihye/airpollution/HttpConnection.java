package com.example.pyojihye.airpollution;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.example.pyojihye.airpollution.activity.MainActivity;
import com.example.pyojihye.airpollution.activity.SignInActivity;

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
import java.util.Date;

import P_Data.Util_STATUS;
import P_Manager.Gps_Manager;

/**
 * Created by PYOJIHYE on 2016-08-04.
 */
public class HttpConnection extends AsyncTask<String, String, String> {
    Context connectContext;
    Activity activity;
    public static final int CONNECTION_TIMEOUT=10000;
    public static final int READ_TIMEOUT=15000;
    HttpURLConnection conn;
    URL url = null;
    Handler hHnadler;
    SharedPreferences pref;

    //0 DEFAULT 1 SIGN IN& SIGN UP  2 DEVICE REGISTER // 3 CONNECTION REQUEST
    //4 RESPONSE HISTORY DATA 5 GET REAL TIME USER DATA

    public HttpConnection(Activity activity, Context connectContext) {
        this.activity=activity;
        this.connectContext=connectContext;
        //hHnadler=handler;
    }
    public HttpConnection(Activity activity,Context connectContext,Handler handler)
    {
        this.activity=activity;
        this.connectContext=connectContext;
        hHnadler=handler;
    }

    @Override
    protected String doInBackground(String... str) {

        try{
            // Enter URL address where your php file resides
            //0 sign in 1 SIGN up 2 DEVICE REGISTER  // 3 CONNECTION REQUEST
            //4 RESPONSE HISTORY DATA 5 GET REAL TIME USER DATA

            //set url
            switch (Util_STATUS.HTTP_CONNECT_KIND)
            {
                case 0: //sign in
                {
                    url = new URL("http://teama-iot.calit2.net/slim/recieveData.php/sign-in");
                    break;
                }
                case 1: //sign up
                {
                    url = new URL("http://teama-iot.calit2.net/slim/recieveData.php/sign-up");
                    break;
                }
                case 2: ///device register
                {

                    url = new URL("http://teama-iot.calit2.net/slim/recieveData.php/device-connect");

                    //Cursor cursor=db.rawQuery("SELECT Lat,Lon FROM Gps_data WHERE regdate="+String.valueOf(System.currentTimeMillis()/1000),null);
                    //cursor.moveToNext();
                    //url = new URL("http://teama-iot.calit2.net/slim/recieveData.php/device-connect");

                    break;
                }
                case 3: //connection request
                {
                    url = new URL("http://teama-iot.calit2.net/slim/recieveData.php/connection-manage");
                    //teama-iot.calit2.net/slim/recieveData.php/connection_manage

                    break;
                }
                case 4: //response histroy data
                {
                    break;
                }
                case 5: //get real time user data
                {
                    url=new URL("http://teama-iot.calit2.net/slim/recieveData.php/get-real-time-user-data");
                    break;
                }
                case 6: //input ar data
                {   url=new URL("http://teama-iot.calit2.net/slim/recieveData.php/input-air-data");
                    //teama-iot.calit2.net/slim/recieveData.php/get-real-time-user-data
                    //str[0] json data
                    break;
                }
                case 7: //input hr data
                {
                    Log.d("EXCUTE","HR DATA");
                    url=new URL("http://teama-iot.calit2.net/slim/recieveData.php/input-HR-data");
                    break;
                }


            }


            if(str.length<=2){ //signIn

                //
            }else{  //signUp

            }

        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "exception";
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

            //1.1.1. {"type":"app","userID"	:"123","request":"0","deviceTYPE":"0","deviceMAC":"11-A1-AA-11-11-A1"}
            Log.d("http 커넥션 번호 ㅇㅇㅇㅇㅇㅇㅇㅇ",String.valueOf(Util_STATUS.HTTP_CONNECT_KIND));
            switch (Util_STATUS.HTTP_CONNECT_KIND) {

                case 0: //sign in
                {
                    try{
                        json.put("type","app");
                        json.put("email", str[0]);
                        json.put("pwd", str[1]);

                        User_Info.Email=str[0];


                    } catch (JSONException e) {
                        e.printStackTrace();
                        return "exception";
                    }
                    break;
                }
                case 1: //sign up
                {
                    json.put("type","app");
                    json.put("email", str[0]);
                    json.put("pwd", str[1]);
                    json.put("checkpwd", str[2]);
                    json.put("fname", str[3]);
                    json.put("lname", str[4]);
                    break;
                }
                case 2: //device register
                {
                    //USER ID 랑 ADDRESS 받음
                    json.put("type","app");
                    json.put("userID",str[0]); //userID
                    json.put("request","0");
                    if (Util_STATUS.SELECT_DEVICE ==0) //UDOO
                    {
                        json.put("deviceTYPE","0");
                    }
                    else if(Util_STATUS.SELECT_DEVICE ==1)
                    {
                        json.put("deviceTYPE","1");
                    }
                    String mac="x'";
                    for(int i=0;i<str[1].split(":").length;i++)
                    {
                        mac+=str[1].split(":")[i];
                    }
                    mac+="'";
                    json.put("deviceMAC",mac);
                    //str[1] //device MAC
                    //json.put("deviceMAC",)
                    //json.put("deviceMAC",x)
                    break;
                }
                case 3: //connect request device id 들어옴
                {

                    json.put("type","app");
                    json.put("deviceID",str[0]);
                    json.put("request","0");

                    switch (Util_STATUS.REQ_CONNECTION_STATE)
                    {
                        case 0: //reqeust connection
                        {

                            switch (Util_STATUS.SELECT_DEVICE)
                            {
                                case 0:
                                {
                                    json.put("type","app");
                                    json.put("userID",activity.getSharedPreferences("MAC",0).getString("userID",""));
                                    //activity.getSharedPreferences("MAC",0).getString("deviceID","")
                                    json.put("deviceID",str[0]);
                                    json.put("request","0");
                                }
                                break;
                                case 1:
                                {

                                    json.put("type","app");
                                    json.put("userID",activity.getSharedPreferences("MAC",0).getString("userID",""));
                                    //activity.getSharedPreferences("MAC",0).getString("deviceID","")
                                    json.put("deviceID",str[0]);
                                    json.put("request","0");
                                }
                                break;
                            }

                            break;
                        }

                        case 1:
                        {
                            switch (Util_STATUS.SELECT_DEVICE)
                            {
                                case 0:
                                {
                                    json.put("type","app");
                                    json.put("userID",activity.getSharedPreferences("MAC",0).getString("userID",""));
                                    //activity.getSharedPreferences("MAC",0).getString("deviceID","")
                                    json.put("deviceID",str[0]);
                                    json.put("request","1");
                                }
                                break;
                                case 1:
                                {

                                    json.put("type","app");
                                    json.put("userID",activity.getSharedPreferences("MAC",0).getString("userID",""));
                                    //activity.getSharedPreferences("MAC",0).getString("deviceID","")
                                    json.put("deviceID",str[0]);
                                    json.put("request","1");
                                }
                                break;
                            }
                            break;
                        }
                    }

                    break;
                }
                case 4: //get history data
                {

                    break;
                }
                case 5: //get real time user data
                {
                    //JSONObject jsonObject=new JSONObject();

                    json.put("type","app"); //app
                    json.put("connectionID",Util_STATUS.UDOO_CONNECTION_ID); //connection id
                    json.put("LATITUDE", Gps_Manager.latLng.latitude); //latitude
                    json.put("LONGITUDE",Gps_Manager.latLng.longitude); //longitude
                    int z=5;

                    //1.1.1. {"type":"app","connectionID":"111","LATITUDE":"38.1234567","LONGITUDE":"-118.1234567"}

                    //json.getInt("CO");
                    break;
                }
                case 6: //input ar data
                {
                    JSONObject getjson = new JSONObject(str[0]); //String to json parsing

                    json.put("type","app");
                    //json.put("connectionID",activity.getSharedPreferences("MAC",0).getString("connectionID",""));
                    json.put("connectionID",String.valueOf(Util_STATUS.UDOO_CONNECTION_ID));
                    //json.put("connectionID",String.valueOf(123));
                    json.put("timeSTAMP",String.valueOf(getjson.getInt("TIME")));
                    //json.put("LATITUDE",String.valueOf(1234.1234));
                    //json.put("LONGITUDE",String.valueOf(1234.1233));

                    json.put("LATITUDE", String.valueOf(Gps_Manager.latLng.latitude));
                    json.put("LONGITUDE",String.valueOf(Gps_Manager.latLng.longitude));
                    json.put("CO",String.valueOf(getjson.getDouble("CO"))); //0 500
                    json.put("NO2",String.valueOf(getjson.getDouble("NO2")));
                    json.put("O3",String.valueOf(getjson.getDouble("O3")));
                    json.put("PM",String.valueOf(getjson.getDouble("PM25")));
                    json.put("SO2",String.valueOf(getjson.getDouble("SO2")));
                    json.put("TEMP",String.valueOf(getjson.getInt("TEMP")));
                    //Log.d(new Date((float)getjson.getInt("TIME")))getjson.getInt("TIME")
                    String ss=String.valueOf(getjson.getInt("TIME"));
                    Date date=new Date();
                    date.setTime((long)getjson.getInt("TIME")*1000);
                    Log.d("time",date.toString());
                    Log.d("AIR CONNECTION ID",String.valueOf(Util_STATUS.UDOO_CONNECTION_ID));


                    break;
                }
                case 7: //input hr data
                {
                    json = new JSONObject();
                    json.put("type","app");
                    json.put("connectionID",String.valueOf(Util_STATUS.HEART_CONNECTION_ID));
                    json.put("timeSTAMP",String.valueOf(System.currentTimeMillis()/1000));
                    json.put("LATITUDE",String.valueOf(Gps_Manager.latLng.latitude));
                    json.put("LONGITUDE",String.valueOf(Gps_Manager.latLng.longitude));
                    json.put("HEARTRATE",str[0]);
                    Log.d("HR CONNECTION ID",String.valueOf(Util_STATUS.HEART_CONNECTION_ID));
                    break;
                }
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
            return "exception";
        } catch(Exception e){
            return "exception";
        }

        try {
            int response_code = conn.getResponseCode();
            Log.d("response",String.valueOf(response_code));
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
                Log.d("HTTP RESULT",result.toString());
                return (result.toString());
            } else{
                return ("unsuccessful");
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "exception";
        }catch(Exception e){
            return "exception";
        }finally {
            conn.disconnect();
        }
    }

    @Override
    protected void onPostExecute(String result) {

        try {
            String response;
            JSONObject jsonObject = new JSONObject(result);
            response=jsonObject.getString("response");

            switch (Util_STATUS.HTTP_CONNECT_KIND)
            {

                case 0: //sign in
                {
                    response=jsonObject.getString("response");
                    switch (response)
                    {
                        case "0":
                            //pref = activity.getSharedPreferences("MAC", 0);
                            pref=activity.getSharedPreferences("MAC",0);
                            //pref = MainActivity.getInstance().getSharedPreferences("MAC",0);
                            SharedPreferences.Editor editor = pref.edit();
                            editor.putString("userID",jsonObject.getString("userID"));
                            editor.commit();
                            //editor.putString("UDOOMAC", address);

                            User_Info.UserFirstName=jsonObject.getString("fname");
                            User_Info.UserLastName=jsonObject.getString("lname");

                            Toast.makeText(connectContext, "Log-In Success!", Toast.LENGTH_SHORT).show();
                            //Intent IntentSettingDevice = new Intent(activity, SettingDeviceActivity.class);
                            //activity.startActivity(IntentSettingDevice);
                            Intent mainIntent=new Intent(activity, MainActivity.class);
                            activity.startActivity(mainIntent);
                            Log.d("http 통신 ","sign in success");
                            break;
                        case "1":
                            Toast.makeText(connectContext, "No exist in DB", Toast.LENGTH_SHORT).show();
                            break;
                        case "2":
                            Toast.makeText(connectContext, "Password is wrong", Toast.LENGTH_SHORT).show();
                            break;
                        case "3":
                            Toast.makeText(connectContext, "Lock account.\nPlease check the link in Email\nPlease Go to Web site, Change your Password!", Toast.LENGTH_LONG).show();
                            break;

                    }
                    break;
                }
                case 1: //sign up
                {

                    response=jsonObject.getString("response");
                    switch (response)
                    {
                        case "4":
                            Toast.makeText(connectContext, "Sign up in with this account", Toast.LENGTH_SHORT).show();
                            break;
                        case "5":
                            Toast.makeText(connectContext, "Password must be at least 8 character long", Toast.LENGTH_SHORT).show();
                            break;
                        case "6":
                            Toast.makeText(activity, "Sign Up Success!\n Please Check the link in Email.\nActivated your account", Toast.LENGTH_SHORT).show();
                            Intent IntentSignIn = new Intent(activity, SignInActivity.class);
                            activity.startActivity(IntentSignIn);
                            Log.d("STATE","Sign up");
                    }
                    break;
                }
                case 2: ///device register
                {

                    response=jsonObject.getString("response");
                    if(response=="0")
                    {
                        //pref 안에 디바이스 이름 디바이스 맥 유저 아이디 디바이스 아이디 저장
                        //{"type":"web","response":0,"deviceID":{"maxID":"5"}}Invalid HTTP status code211

                        switch (Util_STATUS.SELECT_DEVICE)
                        {
                            case 0: //UDOO
                            {
                                pref=activity.getSharedPreferences("MAC",0);
                                SharedPreferences.Editor editor = pref.edit();
                                editor.putString("UDOOdeviceID",jsonObject.getString("deviceID"));
                                editor.commit();
                                Log.d("http 통신 ","udoo device register success");
                                break;
                            }
                            case 1:
                            {
                                pref=activity.getSharedPreferences("MAC",0);
                                SharedPreferences.Editor editor=pref.edit();
                                editor.putString("HEARTdeviceID",jsonObject.getString("deviceID"));
                                editor.commit();
                                Log.d("http 통신 ","heart device register success");
                                break;
                            }
                        }

                        Toast.makeText(connectContext,"Device register success!",Toast.LENGTH_SHORT).show();
                    }
                    else if(response=="1")
                    {
                        Toast.makeText(connectContext,"Device register error!",Toast.LENGTH_SHORT).show();
                    }
                    //여기서 디바이스 아이디 저장
                    //1.1.1. {"type":"web","response":"0","deviceID":1234}

                    Log.d("STATE","device register");
                    break;
                }
                case 3: //connection request response
                {
                    //1.1.1. {"type":"web","response":"0","deviceID":1234,"connectionID":1234}//connection request

                    response=jsonObject.getString("response");
                    if(Util_STATUS.REQ_CONNECTION_STATE==0) //connect
                    {
                        if(Util_STATUS.SELECT_DEVICE ==0) //UDOO
                        {
                            if(response=="0") {
                                //pref=activity.getSharedPreferences("MAC",0);
                                //SharedPreferences.Editor editor = pref.edit();
                                //editor.putString("UDOOconnectionID",jsonObject.getString("connectionID"));
                                //editor.commit();
                                Util_STATUS.UDOO_CONNECTION_ID=jsonObject.getInt("connectionID");
                                Toast.makeText(activity,"Connection request success!",Toast.LENGTH_SHORT).show();
                                Log.d("http 통신 ","udoo connection success");
                            }
                            else if(response=="1")
                            {
                                Toast.makeText(activity,"Connection request error!",Toast.LENGTH_SHORT).show();
                            }
                        }
                        else if(Util_STATUS.SELECT_DEVICE ==1) //HEART
                        {
                            if(response=="0") {

                                Util_STATUS.HEART_CONNECTION_ID=jsonObject.getInt("connectionID");
                                Toast.makeText(activity,"Connection request success!",Toast.LENGTH_SHORT).show();
                                Log.d("http 통신 ","heart connection success");
                            }
                            else if(response=="1")
                            {
                                Toast.makeText(activity,"Connection request error!",Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                    else if(Util_STATUS.REQ_CONNECTION_STATE==1) //disconnect
                    {
                        if(Util_STATUS.SELECT_DEVICE ==0) //UDOO
                        {
                            if(response=="0") {

                                //버퍼에 상수값으로 저장
                                Util_STATUS.UDOO_CONNECTION_ID=0;
                                Toast.makeText(activity,"DisConnection request success!",Toast.LENGTH_SHORT).show();
                                Log.d("http 통신 ","udoo disconnection success");
                            }
                            else if(response=="1")
                            {
                                Toast.makeText(activity,"DisConnection request error!",Toast.LENGTH_SHORT).show();
                            }
                        }
                        else if(Util_STATUS.SELECT_DEVICE ==1) //HEART
                        {
                            if(response=="0") {
                                //pref=activity.getSharedPreferences("MAC",0);
                                //SharedPreferences.Editor editor = pref.edit();
                                //editor.remove("HEARTconnectionID");
                                //editor.commit();
                                Util_STATUS.HEART_CONNECTION_ID=0;
                                Toast.makeText(activity,"DisConnection request success!",Toast.LENGTH_SHORT).show();
                                Log.d("http 통신 ","heart connection success");
                            }
                            else if(response=="1")
                            {
                                Toast.makeText(activity,"DisConnection request error!",Toast.LENGTH_SHORT).show();
                            }
                        }
                    }



                    Log.d("STATE","reqeuest connection");
                    break;
                }
                case 4: //response histroy data
                {
                    break;
                }
                case 5: //get real time user data //receivce
                {
                    if(response=="0") {
                        Bundle bundle=new Bundle();
                        bundle.putString("data", String.valueOf(jsonObject));
                        Message msg=new Message();
                        msg.setData(bundle);
                        hHnadler.sendMessage(msg);
                        //{"type":"web","response":0,"count":1,"data":
                        // {"1":{"data":{"LONGITUDE":"-117.234817","LATITUDE":"32.882407"},
                        // "CO":"1.5","SO2":"0","NO2":"0","O3":"0.6","TEMP":"41","PM":"8.8","userID":"78"}}}
                    }
                    else if(response=="1")
                    {

                    }

                    break;
                }
            }


        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("HTTP TYPE",String.valueOf(Util_STATUS.HTTP_CONNECT_KIND));
        }
    }
}