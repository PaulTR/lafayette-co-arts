package com.ptrprograms.paultrebilcoxruiz.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ptrprograms.paultrebilcoxruiz.R;
import com.ptrprograms.paultrebilcoxruiz.activities.StreetViewActivity;
import com.ptrprograms.paultrebilcoxruiz.models.Artwork;
import com.squareup.picasso.Picasso;

import java.util.Locale;

/**
 * Created by paulruiz on 9/20/14.
 */
public class ArtworkDetailFragment extends Fragment {

    private ImageView mImageView;
    private TextView mTitleView;
    private TextView mArtistView;
    private Button mLocationButton;
    private Button mStreetViewButton;

    private String mImageFileName;
    private String mTitle;
    private String mArtist;
    private double mLatitude;
    private double mLongitude;

    private static final String EXTRA_IMAGE = "extra_image";
    private static final String EXTRA_TITLE = "extra_title";
    private static final String EXTRA_LONG = "extra_long";
    private static final String EXTRA_LAT = "extra_lat";
    private static final String EXTRA_ARTIST = "extra_artist";

    public static ArtworkDetailFragment getInstance( Artwork artwork ) {
        ArtworkDetailFragment fragment = new ArtworkDetailFragment();

        Bundle b = new Bundle();
        b.putString( EXTRA_IMAGE, artwork.getImage() );
        b.putString( EXTRA_TITLE, artwork.getTitle() );
        b.putDouble(EXTRA_LONG, artwork.getLongitude());
        b.putDouble(EXTRA_LAT, artwork.getLatitude());
        b.putString(EXTRA_ARTIST, artwork.getArtist());
        fragment.setArguments(b);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate( R.layout.fragment_artwork_detail, container, false );
        mImageView = (ImageView) view.findViewById( R.id.image );
        mTitleView = (TextView) view.findViewById( R.id.title );
        mArtistView = (TextView) view.findViewById( R.id.artist );
        mLocationButton = (Button) view.findViewById( R.id.locate_button );
        mStreetViewButton = (Button) view.findViewById( R.id.street_view_button );
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        if( getArguments() != null ) {
            if( getArguments().containsKey( EXTRA_IMAGE ) ) {
                mImageFileName = getArguments().getString( EXTRA_IMAGE );
            }
            if( getArguments().containsKey( EXTRA_TITLE ) ) {
                mTitle = getArguments().getString( EXTRA_TITLE );
                mTitleView.setText( mTitle );
            }
            if( getArguments().containsKey( EXTRA_LAT ) )
                mLatitude = getArguments().getDouble(EXTRA_LAT);
            if( getArguments().containsKey( EXTRA_LONG ) ) {
                mLongitude = getArguments().getDouble(EXTRA_LONG);
            }
            if( getArguments().containsKey( EXTRA_ARTIST ) ) {
                mArtist = getArguments().getString( EXTRA_ARTIST );
                mArtistView.setText( mArtist );
            }
        }

        mLocationButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String uri = String.format( Locale.ENGLISH, "http://maps.google.com/maps?&daddr=%f,%f (%s)", mLatitude, mLongitude, mTitle );
                Intent intent = new Intent( Intent.ACTION_VIEW, Uri.parse(uri) );
                intent.setClassName( "com.google.android.apps.maps", "com.google.android.maps.MapsActivity" );
                startActivity( intent );
            }
        });

        mStreetViewButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent( getActivity(), StreetViewActivity.class );
                intent.putExtra( StreetViewActivity.EXTRA_LAT, mLatitude );
                intent.putExtra( StreetViewActivity.EXTRA_LONG, mLongitude );
                startActivity( intent );
            }
        });

        if(TextUtils.isEmpty( mImageFileName ) )
            return;

        Picasso.with( getActivity() ).load( getString( R.string.image_url, mImageFileName ) ).into( mImageView );
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Picasso.with( getActivity() ).cancelRequest( mImageView );
    }

}
