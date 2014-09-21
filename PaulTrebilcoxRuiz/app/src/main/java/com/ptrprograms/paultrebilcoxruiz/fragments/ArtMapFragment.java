package com.ptrprograms.paultrebilcoxruiz.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ptrprograms.paultrebilcoxruiz.R;
import com.ptrprograms.paultrebilcoxruiz.models.Artwork;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by paulruiz on 9/20/14.
 */
public class ArtMapFragment extends Fragment implements GooglePlayServicesClient.ConnectionCallbacks, GooglePlayServicesClient.OnConnectionFailedListener {
    private GoogleMap mGoogleMap;
    private LocationClient mLocationClient;
    private SupportMapFragment mMapFragment;
    private List<Marker> mMarkerLocations = new ArrayList<Marker>();
    private List<Artwork> mCollections;

    public static ArtMapFragment getInstance() {
        return new ArtMapFragment();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mLocationClient = new LocationClient( getActivity(), this, this );
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate( R.layout.fragment_artwork_map, container, false );
         mMapFragment = SupportMapFragment.newInstance();
        mGoogleMap = mMapFragment.getMap();
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.add(R.id.container, mMapFragment).commit();
        return view;
    }

    @Override
    public void onConnected(Bundle bundle) {
        setInitialCameraPosition();
        initMarkers();
    }

    private void initMarkers() {
        loadData();
        mMarkerLocations.clear();
        mGoogleMap.clear();
        int resId;
        Bitmap bitmap;
        for( Artwork artwork : mCollections ) {
            resId = getActivity().getResources().getIdentifier( artwork.getImage().toLowerCase().replaceAll("-", "_").substring( 0, artwork.getImage().length() - 4 ), "drawable", getActivity().getPackageName() );
            bitmap = BitmapFactory.decodeResource( getResources(), resId );
            bitmap = getResizedBitmap( bitmap, 100, 100 );
            Marker marker = mGoogleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(artwork.getLatitude(), artwork.getLongitude()))
                    .title(artwork.getTitle())
                    .icon(BitmapDescriptorFactory.fromBitmap( bitmap ) ) );
            mMarkerLocations.add( marker );
        }

        mGoogleMap.setOnInfoWindowClickListener( new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                String uri = String.format( Locale.ENGLISH, "http://maps.google.com/maps?&daddr=%f,%f (%s)", marker.getPosition().latitude, marker.getPosition().longitude, marker.getTitle() );
                Intent intent = new Intent( Intent.ACTION_VIEW, Uri.parse(uri) );
                intent.setClassName( "com.google.android.apps.maps", "com.google.android.maps.MapsActivity" );
                startActivity( intent );
            }
        });

        mGoogleMap.setMyLocationEnabled( true );
    }

    public Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
        return resizedBitmap;
    }

    @Override
    public void onStart() {
        super.onStart();
        if( mLocationClient != null )
            mLocationClient.connect();
        if( mMapFragment != null )
            mGoogleMap = mMapFragment.getMap();
    }

    @Override
    public void onStop() {
        super.onStop();
        if( mLocationClient != null )
            mLocationClient.disconnect();
        mGoogleMap = null;
    }

    private void loadData() {
        Gson gson = new Gson();
        String json = loadJSONFromResource( R.raw.art );
        Type collection = new TypeToken<ArrayList<Artwork>>(){}.getType();
        mCollections = gson.fromJson( json, collection );
    }

    @Override
    public void onDisconnected() {
        mGoogleMap = null;
        mMapFragment = null;
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    private void setInitialCameraPosition() {
        double lng, lat;
        float tilt, bearing, zoom;

        lng = mLocationClient.getLastLocation().getLongitude();
        lat = mLocationClient.getLastLocation().getLatitude();
        zoom = 16;
        bearing = 0;
        tilt = 0;

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target( new LatLng( lat, lng) )
                .zoom( zoom )
                .bearing( bearing )
                .tilt( tilt )
                .build();

        if( cameraPosition == null || mGoogleMap == null )
            return;

        mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    private String loadJSONFromResource( int resource ) {
        if( resource <= 0 )
            return null;

        String json = null;
        InputStream is = getActivity().getResources().openRawResource( resource );
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
