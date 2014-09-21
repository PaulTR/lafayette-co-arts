package com.ptrprograms.paultrebilcoxruiz.activities;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.maps.StreetViewPanorama;
import com.google.android.gms.maps.StreetViewPanoramaFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.StreetViewPanoramaCamera;
import com.ptrprograms.paultrebilcoxruiz.R;

/**
 * Created by paulruiz on 9/20/14.
 */
public class StreetViewActivity extends Activity implements GooglePlayServicesClient.ConnectionCallbacks,
        GooglePlayServicesClient.OnConnectionFailedListener {

    public static final String EXTRA_LONG = "current_long";
    public static final String EXTRA_LAT = "current_lat";
    private static final String EXTRA_BEARING = "current_bearing";
    private static final String EXTRA_TILT = "current_tilt";
    private static final String EXTRA_ZOOM = "current_zoom";

    private LocationClient mLocationClient;
    private LatLng mCurrentLocation;
    private float mBearing;
    private float mTilt;
    private float mZoom;

    private StreetViewPanorama mPanorama;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_street_view);
        if( getIntent().getExtras() != null ) {
            if (getIntent().getExtras().containsKey(EXTRA_LAT) && getIntent().getExtras().containsKey(EXTRA_LONG)) {
                mCurrentLocation = new LatLng(getIntent().getExtras().getDouble(EXTRA_LAT), getIntent().getExtras().getDouble(EXTRA_LONG));
            }
        } else {
            if (savedInstanceState != null) {
                if (savedInstanceState.containsKey(EXTRA_LAT) && savedInstanceState.containsKey(EXTRA_LONG)) {
                    mCurrentLocation = new LatLng(savedInstanceState.getDouble(EXTRA_LAT), savedInstanceState.getDouble(EXTRA_LONG));
                    if (savedInstanceState.containsKey(EXTRA_TILT) && savedInstanceState.containsKey(EXTRA_BEARING)) {
                        mTilt = savedInstanceState.getFloat(EXTRA_TILT);
                        mBearing = savedInstanceState.getFloat(EXTRA_BEARING);
                        mZoom = savedInstanceState.getFloat(EXTRA_ZOOM);
                    }
                }
            }
        }
    }

    private void initStreetView( ) {
        Log.e("StreetView", "initStreetView" );
        StreetViewPanoramaFragment fragment = ((StreetViewPanoramaFragment) getFragmentManager().findFragmentById(R.id.street_view_panorama_fragment));
        if( mPanorama == null ) {
            if( fragment != null ) {
                mPanorama = fragment.getStreetViewPanorama();
                if( mPanorama != null && mCurrentLocation != null ) {
                    StreetViewPanoramaCamera.Builder builder = new StreetViewPanoramaCamera.Builder( mPanorama.getPanoramaCamera() );
                    if( mBearing != builder.bearing )
                        builder.bearing = mBearing;
                    if( mTilt != builder.tilt )
                        builder.tilt = mTilt;
                    if( mZoom != builder.zoom )
                        builder.zoom = mZoom;
                    mPanorama.animateTo(builder.build(), 0);
                    mPanorama.setPosition( mCurrentLocation, 300 );
                    mPanorama.setStreetNamesEnabled( true );
                }
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if( mPanorama != null && mPanorama.getLocation() != null && mPanorama.getLocation().position != null ) {
            outState.putDouble( EXTRA_LAT, mPanorama.getLocation().position.latitude );
            outState.putDouble( EXTRA_LONG, mPanorama.getLocation().position.longitude );
        }
        if( mPanorama != null && mPanorama.getPanoramaCamera() != null ) {
            outState.putFloat( EXTRA_TILT, mPanorama.getPanoramaCamera().tilt );
            outState.putFloat( EXTRA_BEARING, mPanorama.getPanoramaCamera().bearing );
            outState.putFloat( EXTRA_ZOOM, mPanorama.getPanoramaCamera().zoom );
        }
    }

    private void updateStreetViewPosition() {
        Log.e("StreetView", "updateStreetViewPosition" );

        if( mLocationClient == null )
            mLocationClient = new LocationClient( this, this, this );
        if( !mLocationClient.isConnected() && !mLocationClient.isConnecting() ) {
            mLocationClient.connect();
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        if( mCurrentLocation == null ) {
            Log.e("StreetView", "mCurrentLocation is NULL" );
        }
        initStreetView();
    }

    @Override
    public void onDisconnected() {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateStreetViewPosition();
    }
}