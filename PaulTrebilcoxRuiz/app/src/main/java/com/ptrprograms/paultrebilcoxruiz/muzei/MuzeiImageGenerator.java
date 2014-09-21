package com.ptrprograms.paultrebilcoxruiz.muzei;

/**
 * Created by paulruiz on 9/20/14.
 */
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;

import com.google.android.apps.muzei.api.Artwork;
import com.google.android.apps.muzei.api.RemoteMuzeiArtSource;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ptrprograms.paultrebilcoxruiz.R;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MuzeiImageGenerator extends RemoteMuzeiArtSource {

    private static final int ROTATE_TIME_MILLIS = 5 * 1000;
    private static final String ACCEPTED_FILE_EXTENSION = "jpg";
    private static final String NAME = "Hack4Arts";
    private List<com.ptrprograms.paultrebilcoxruiz.models.Artwork> mArtworks;

    public MuzeiImageGenerator() {
        super( NAME );
    }

    @Override
    protected void onTryUpdate( int reason ) throws RetryException {
        String link;
        try {
            do {
                link = getMuzeiImageUrl();
            } while( !imageHasAcceptableExtension( link ) );
        } catch( Exception e ) {
            throw new RetryException();
        }

        setMuzeiImage( link );
    }

    private boolean imageHasAcceptableExtension( String link ) {
        return link.substring( link.length() - 3 ).equals( ACCEPTED_FILE_EXTENSION );
    }

    private void setMuzeiImage( String link ) {
        publishArtwork(new Artwork.Builder()
                .title( "Hack4Arts" )
                .imageUri(Uri.parse(link))
                .viewIntent( new Intent(Intent.ACTION_VIEW, Uri.parse( link ) ) )
                .build() );

        scheduleUpdate(System.currentTimeMillis() + ROTATE_TIME_MILLIS);
    }

    private String getMuzeiImageUrl() {
        loadData();
        Random generator = new Random();
        String imageUrl = null;
        while( imageUrl == null || TextUtils.isEmpty( imageUrl ) ) {
            imageUrl = getString( R.string.image_url, mArtworks.get( generator.nextInt( mArtworks.size() ) ).getImage() );
        }

        return imageUrl;
    }

    private void loadData() {
        if( mArtworks != null && !mArtworks.isEmpty() )
            return;

        Gson gson = new Gson();
        String json = loadJSONFromResource( R.raw.art );
        Type collection = new TypeToken<ArrayList<com.ptrprograms.paultrebilcoxruiz.models.Artwork>>(){}.getType();
        mArtworks = gson.fromJson( json, collection );
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
}
