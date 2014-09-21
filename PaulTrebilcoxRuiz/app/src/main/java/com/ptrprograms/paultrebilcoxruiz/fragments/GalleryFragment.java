package com.ptrprograms.paultrebilcoxruiz.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ptrprograms.paultrebilcoxruiz.R;
import com.ptrprograms.paultrebilcoxruiz.activities.ImageDetailActivity;
import com.ptrprograms.paultrebilcoxruiz.adapters.GalleryGridAdapter;
import com.ptrprograms.paultrebilcoxruiz.models.Artwork;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by paulruiz on 9/20/14.
 */
public class GalleryFragment extends Fragment {

    private GridView mGridView;
    private GalleryGridAdapter mAdapter;
    private List<Artwork> mCollections;

    public static GalleryFragment getInstance() {
        GalleryFragment fragment = new GalleryFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate( R.layout.fragment_gallery_grid, container, false );
        mGridView = (GridView) view.findViewById( R.id.gallery_grid_view );
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        loadFeed();
        initAdapter();
        initGrid();
    }

    private void initGrid() {
        mGridView.setOnItemClickListener( new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent( getActivity(), ImageDetailActivity.class );
                intent.putExtra( ImageDetailActivity.EXTRA_CUR_IMAGE, i );
                startActivity( intent );
            }
        });
    }

    private void initAdapter() {
        mAdapter = new GalleryGridAdapter( getActivity() );
        mAdapter.addAll( mCollections );
        mGridView.setAdapter( mAdapter );
    }

    private void loadFeed() {
        Gson gson = new Gson();
        String json = loadJSONFromResource( R.raw.art );
        Type collection = new TypeToken<ArrayList<Artwork>>(){}.getType();
        mCollections = gson.fromJson( json, collection );
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
