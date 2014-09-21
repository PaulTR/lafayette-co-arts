package com.ptrprograms.paultrebilcoxruiz.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.ptrprograms.paultrebilcoxruiz.R;
import com.ptrprograms.paultrebilcoxruiz.events.DrawerNavigationItemClickedEvent;
import com.ptrprograms.paultrebilcoxruiz.fragments.ArtMapFragment;
import com.ptrprograms.paultrebilcoxruiz.fragments.GalleryFragment;
import com.ptrprograms.paultrebilcoxruiz.utils.NavigationBus;
import com.squareup.otto.Subscribe;

import java.io.File;
import java.io.FileOutputStream;


public class MainActivity extends ActionBarActivity {

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private NavigationBus mNavigationBus;
    private String mCurFragmentTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavigationBus = NavigationBus.getInstance();

        initActionBar();
        initViews();
        initFragment();
        initDrawer();
    }

    private void initActionBar() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    private void initViews() {
        mDrawerLayout = (DrawerLayout) findViewById( R.id.drawer_layout );
    }

    private void initFragment() {
        getSupportFragmentManager().beginTransaction().replace(R.id.container, GalleryFragment.getInstance()).commit();
        mCurFragmentTitle = getString( R.string.category_gallery );
    }

    private void initDrawer() {
        mDrawerToggle = new ActionBarDrawerToggle( this, mDrawerLayout,
                R.drawable.ic_navigation_drawer, R.string.drawer_open_title, R.string.drawer_closed_title ) {

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                if( getSupportActionBar() == null )
                    return;

                getSupportActionBar().setTitle( R.string.drawer_closed_title );
                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                if( getSupportActionBar() == null )
                    return;

                getSupportActionBar().setTitle( R.string.drawer_open_title );
                invalidateOptionsMenu();
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if( mDrawerToggle.onOptionsItemSelected( item ) )
            return true;

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mNavigationBus.register(this);
    }

    @Override
    protected void onStop() {
        mNavigationBus.unregister(this);
        super.onStop();
    }

    @Subscribe
    public void onDrawerNavigationClickedEvent( DrawerNavigationItemClickedEvent event ) {
        if( !mCurFragmentTitle.equalsIgnoreCase(event.section) ) {
            if ( getString(R.string.category_gallery ).equalsIgnoreCase(event.section)) {
               getSupportFragmentManager().beginTransaction().replace(R.id.container, GalleryFragment.getInstance()).commit();
                mCurFragmentTitle = event.section;
            } else if( getString( R.string.category_map ).equalsIgnoreCase(event.section ) ) {
                getSupportFragmentManager().beginTransaction().replace(R.id.container, ArtMapFragment.getInstance()).commit();
                mCurFragmentTitle = event.section;
            } else if( getString( R.string.category_camera ).equalsIgnoreCase( event.section ) ) {
                takePhoto();
            }
        }
        mDrawerLayout.closeDrawers();
    }

    public void takePhoto() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, 1);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            String file_path = Environment.getExternalStorageDirectory().getAbsolutePath() +
                    "/ProjectName";
            File dir = new File(file_path);
            if(!dir.exists())
                dir.mkdirs();
            File file = new File(dir, "hack4arts.jpg");
            FileOutputStream fOut;
            try {
                fOut = new FileOutputStream(file);
                imageBitmap.compress(Bitmap.CompressFormat.PNG, 85, fOut);
                fOut.flush();
                fOut.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            Uri uri = Uri.fromFile(file);
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_SEND);
            intent.setType("image/*");

            intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Lafayette Art");
            intent.putExtra(android.content.Intent.EXTRA_TEXT, "Lafayette Art");
            intent.putExtra(Intent.EXTRA_STREAM, uri);
            startActivity(Intent.createChooser(intent, "Lafayette Art"));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
}
