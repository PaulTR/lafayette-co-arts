package com.ptrprograms.paultrebilcoxruiz.activities;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ptrprograms.paultrebilcoxruiz.R;
import com.ptrprograms.paultrebilcoxruiz.adapters.ArtworkViewPagerAdapter;
import com.ptrprograms.paultrebilcoxruiz.models.Artwork;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by paulruiz on 9/20/14.
 */
public class ImageDetailActivity extends ActionBarActivity {

    public static final String EXTRA_CUR_IMAGE = "curImage";

    private int mCurrentImagePosition = 0;
    private ArtworkViewPagerAdapter mAdapter;
    private ViewPager mViewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artwork_detail);
        mViewPager = ( ViewPager ) findViewById( R.id.activity_image_view_pager );

        setupImageList();

        if( mAdapter == null || mViewPager == null )
            return;

        mViewPager.setAdapter( mAdapter );
    }

    private void setupImageList() {
        if( getIntent() == null || getIntent().getExtras() == null )
            return;

        Gson gson = new Gson();
        String json = loadJSONFromResource( R.raw.art );
        Type collection = new TypeToken<ArrayList<Artwork>>(){}.getType();
        List<Artwork> tmpList = gson.fromJson( json, collection );
        mCurrentImagePosition = getIntent().getExtras().getInt( EXTRA_CUR_IMAGE, 0 );
        mAdapter = new ArtworkViewPagerAdapter( getSupportFragmentManager(), tmpList );
        mViewPager.setAdapter( mAdapter );
    }

    private String loadJSONFromResource( int resource ) {
        if( resource <= 0 )
            return null;

        String json = null;
        InputStream is = getResources().openRawResource(resource);
        try {
            if( is != null ) {
                int size = is.available();
                byte[] buffer = new byte[size];
                if( buffer != null ) {
                    is.read(buffer);
                    json = new String(buffer, "UTF-8");
                }
            }
        } catch( IOException e ) {

        } finally {
            try {
                is.close();
            } catch( IOException e ) {}
        }

        return json;
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mCurrentImagePosition = savedInstanceState.getInt( EXTRA_CUR_IMAGE, 0 );
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mCurrentImagePosition = mViewPager.getCurrentItem();
        outState.putInt( EXTRA_CUR_IMAGE, mCurrentImagePosition );
    }

    @Override
    protected void onResume() {
        super.onResume();

        if( mViewPager == null )
            return;

        mViewPager.setCurrentItem( mCurrentImagePosition );
    }
}
