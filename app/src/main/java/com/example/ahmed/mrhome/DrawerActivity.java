package com.example.ahmed.mrhome;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.List;

public class DrawerActivity extends AppCompatActivity {



    private String[] roomsList={
            "Living Room",
            "Kitchen",
            "Garage",
            "Bedroom 1",
            "Bathroom",
            "Bedroom 2",
            "Bedroom 3"
    };

    DrawerLayout mDrawerLayout;
    ListView RoomList;
    ActionBarDrawerToggle mDrawerToggle;
    CharSequence mTitle;
    CharSequence mDrawerTitle;
    String token;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        RoomList = (ListView) findViewById(R.id.roomsList);
        sharedPreferences = getSharedPreferences(MainActivity.PREF_NAME,MODE_PRIVATE);

        // get the title of drawerActivity
        mTitle = mDrawerTitle = getTitle();

        //get the token of the user
        token = sharedPreferences.getString(MainActivity.KEY_TOKEN,"not found token");




        // make list its type is FloorItem
        List<FloorItem> floorItemList = new ArrayList<FloorItem>();
        floorItemList.add(new FloorItem("Living Room",R.drawable.living_room));
        floorItemList.add(new FloorItem("Kitchen",R.drawable.chen ));
        floorItemList.add(new FloorItem("Garage",R.drawable.garage));
        floorItemList.add(new FloorItem("BedRoom 1",R.drawable.bedroom));
        floorItemList.add(new FloorItem("BathRoom",R.drawable.bathroom ));
        floorItemList.add(new FloorItem("BedRoom 2",R.drawable.bedroom));
        floorItemList.add(new FloorItem("BedRoom 3",R.drawable.bedroom));

        // set adapter to ListView
        RoomList.setAdapter(new FloorCustomAdapter(this,floorItemList));

        // when Click on item in the listView
        RoomList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectRoom(position);
            }
        });





         mDrawerToggle = new ActionBarDrawerToggle(this,mDrawerLayout,R.string.drawer_open,R.string.drawer_close){
            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle("Rooms");
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }



        };


        mDrawerLayout.setDrawerListener(mDrawerToggle);

        // add fragment to the activity
        LivingRoomFragment livingRoomFragment = new LivingRoomFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.container_frame,livingRoomFragment).commit();
        setTitle("Living Room");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

    }




    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }



    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getSupportActionBar().setTitle(mTitle);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle your other action bar items...

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

       // boolean drawerOpen =mDrawerLayout.isDrawerOpen(Drawer_Linear);
       // menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }



    public void selectRoom(int position){

        switch (position){
            case 0:{
                //check if there is fragment
                // if there is delete it first
                // then add new fragment
                Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.container_frame);
                if(fragment !=null){
                    getSupportFragmentManager().beginTransaction().remove(fragment).commit();
                }


                LivingRoomFragment livingRoomFragment = new LivingRoomFragment();
                getSupportFragmentManager().beginTransaction().add(R.id.container_frame,livingRoomFragment).commit();
                RoomList.setItemChecked(position,true);
                setTitle(roomsList[position]);
                mDrawerLayout.closeDrawer(RoomList);
                break;
            }

            case 1:{
                Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.container_frame);
                if(fragment !=null){
                    getSupportFragmentManager().beginTransaction().remove(fragment).commit();
                }
                KitchenFragment kitchenFragment = new KitchenFragment();


                getSupportFragmentManager().beginTransaction().add(R.id.container_frame,kitchenFragment).commit();
                RoomList.setItemChecked(position,true);
                setTitle(roomsList[position]);
                mDrawerLayout.closeDrawer(RoomList);
                break;
            }

            case 2:{
                Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.container_frame);
                if(fragment !=null){
                    getSupportFragmentManager().beginTransaction().remove(fragment).commit();
                }
                GarageFragment garageFragment = new GarageFragment();


                getSupportFragmentManager().beginTransaction().add(R.id.container_frame,garageFragment).commit();
                RoomList.setItemChecked(position,true);
                setTitle(roomsList[position]);
                mDrawerLayout.closeDrawer(RoomList);
                break;
            }

            case 3:{
                Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.container_frame);
                if(fragment !=null){
                    getSupportFragmentManager().beginTransaction().remove(fragment).commit();
                }
                BedRoom1Fragment bedRoom1Fragment = new BedRoom1Fragment();


                getSupportFragmentManager().beginTransaction().add(R.id.container_frame,bedRoom1Fragment).commit();
                RoomList.setItemChecked(position,true);
                setTitle(roomsList[position]);
                mDrawerLayout.closeDrawer(RoomList);
                break;
            }

            case 4:{
                Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.container_frame);
                if(fragment !=null){
                    getSupportFragmentManager().beginTransaction().remove(fragment).commit();
                }
                BathRoomFragment bathRoomFragment = new BathRoomFragment();


                getSupportFragmentManager().beginTransaction().add(R.id.container_frame,bathRoomFragment).commit();
                RoomList.setItemChecked(position,true);
                setTitle(roomsList[position]);
                mDrawerLayout.closeDrawer(RoomList);
                break;
            }

            case 5:{
                Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.container_frame);
                if(fragment !=null){
                    getSupportFragmentManager().beginTransaction().remove(fragment).commit();
                }
                BedRoom2Fragment bedRoom2Fragment = new BedRoom2Fragment();


                getSupportFragmentManager().beginTransaction().add(R.id.container_frame,bedRoom2Fragment).commit();
                RoomList.setItemChecked(position,true);
                setTitle(roomsList[position]);
                mDrawerLayout.closeDrawer(RoomList);
                break;
            }
            case 6:{
                Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.container_frame);
                if(fragment !=null){
                    getSupportFragmentManager().beginTransaction().remove(fragment).commit();
                }
                BedRoom3Fragment bedRoom3Fragment = new BedRoom3Fragment();


                getSupportFragmentManager().beginTransaction().add(R.id.container_frame,bedRoom3Fragment).commit();
                RoomList.setItemChecked(position,true);
                setTitle(roomsList[position]);
                mDrawerLayout.closeDrawer(RoomList);
                break;
            }




        }

    }






}
