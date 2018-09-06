package com.dankov.eve;

import android.content.IntentFilter;
import android.provider.Telephony;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class ServiceNavigator extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    SMSReciever smsReciever;

    int currentService = 0;
    List<ServiceFragment> services;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_navigator);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            public void onDrawerOpened(View view) {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            }
        };

        drawer.addDrawerListener(toggle);
        toggle.syncState();

        switchToDefaultFragment();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //This is a list of essentially modules that have been instantiated
        services = new ArrayList<>();
        loadServices();

        //This is our SMS Reciever
        //First argument is the twilio phone number
        //Second argument is like a header so like you can have the reciever only accept texts which start with "Twilio" or "我"
        //RN its disabled though for testing
        smsReciever = new SMSReciever(ServiceFragment.PHONE_NUMBER, "Twilio");
        registerReceiver(smsReciever, new IntentFilter(Telephony.Sms.Intents.SMS_RECEIVED_ACTION));

        smsReciever.setListener(new SMSReciever.Listener(){
            @Override
            public void onTextReceived(String text) {
                Log.d("SMSReciever",text);
                for(ServiceFragment s : services){
                    if(text.startsWith(s.prefix())){
                        s.recieveSMS(text);
                    }
                }
            }
        });

        Log.d("SMSReciever","Eve has been initialized");
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.service_navigator, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            //Settings Handling here
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        switchToNavFragment(item);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void loadServices() {
        services.add(new DefaultFragment());
        services.add(new WikiFragment());
        services.add(new DirectionsFragment());
        services.add(new NewsFragment());
        services.add(new TransitFragment());
    }

    public void switchToNavFragment(MenuItem item) {
        setTitle(item.getTitle());

        FragmentManager fm = getSupportFragmentManager();

        switch(item.getItemId()) {
            case R.id.nav_wiki:
                currentService = 1;
                break;

            case R.id.nav_directions:
                currentService = 2;
                break;

            case R.id.nav_news:
                currentService = 3;
                break;

            case R.id.nav_transit:
                currentService = 4;
                break;
            default:
                return;
        }

        fm.beginTransaction().replace(R.id.serviceFragment, services.get(currentService)).commit();
    }

    public void switchToDefaultFragment() {
        FragmentManager fm = getSupportFragmentManager();
        ServiceFragment service = new DefaultFragment();
        fm.beginTransaction().replace(R.id.serviceFragment, service).commit();
    }

    public void openNav() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.openDrawer(Gravity.START);
    }
}
