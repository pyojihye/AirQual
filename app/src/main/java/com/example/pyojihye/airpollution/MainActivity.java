package com.example.pyojihye.airpollution;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.maps.GoogleMap;

import P_Adapter.Pager_Adapter;
import P_Data.Air_Data;
import P_Fragment.Fr_R_G_Map;
import P_Fragment.Fr_View_pager;
import P_Manager.GMap_Manager;
import P_Manager.Gps_Manager;
import P_Service.Air_Fake_Service;
import P_Utils.Util_STATUS;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    GoogleMap gmap;

    //class
    public Gps_Manager gps_manager;
    public GMap_Manager gmap_manager;
    //Fragment 관련
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    public static Pager_Adapter pa; //STATIC PAGER ADAPTER USING Viewpager add
    Air_Fake_Service air_fake_service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        init();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    public void init() {
        gps_manager = new Gps_Manager(getApplicationContext());
        air_fake_service=new Air_Fake_Service(sHandler);
        Util_STATUS util=new Util_STATUS(); //util
        gmap_manager=new GMap_Manager();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }



    private final Handler sHandler=new Handler(){

        @Override
        public void handleMessage(Message msg) {


            Air_Data ar=(Air_Data)msg.getData().getSerializable("data");
            gmap_manager.Set_Circle(ar);





        }
    };

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        //클릭시마다 상태확인
        if (id == R.id.nav_main) {

        } else if (id == R.id.nav_realtime_data) {
            pa = new Pager_Adapter(getLayoutInflater(), getApplicationContext());
            fragmentManager = getFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            Fragment fr = new Fr_View_pager();


            fragmentTransaction.replace(R.id.list_container, fr);

            fragmentTransaction.commit();
        } else if (id == R.id.nav_chart) {

        } else if (id == R.id.nav_realtime_map) {
           // MapFragment map=(MapFragment)getFragmentManager().findFragmentById(R.id.map);
            Fragment fr = new Fr_R_G_Map(gps_manager.get_LatLng());

            fragmentManager = getFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.list_container, fr);
            //fragmentTransaction.add(R.id.list_container,fr);
            fragmentTransaction.commit();
            Air_Fake_Service.RECEIVE_DATA_STATUS=true;
        } else if (id == R.id.nav_history_map) {
            //getMapFragment();
            //MapFragment map=(MapFragment)getFragmentManager().findFragmentById(R.id.map);

            //fragmentManager.findFragmentById(R.id.map);
        } else if (id == R.id.nav_management) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
