package com.ptrprograms.paultrebilcoxruiz.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.ptrprograms.paultrebilcoxruiz.fragments.ArtworkDetailFragment;
import com.ptrprograms.paultrebilcoxruiz.models.Artwork;

import java.util.List;

/**
 * Created by paulruiz on 9/20/14.
 */
public class ArtworkViewPagerAdapter extends FragmentStatePagerAdapter {

    private List<Artwork> mArtworkList;

    public ArtworkViewPagerAdapter(FragmentManager fm) {
        super( fm );
    }

    public ArtworkViewPagerAdapter(FragmentManager fm, List<Artwork> imageList) {
        super( fm );

        setList(imageList);
    }

    public void setList( List<Artwork> list ) {
        if( list == null )
            return;

        mArtworkList = list;
    }

    @Override
    public Fragment getItem( int position ) {
        return( position < 0 || position > ( mArtworkList.size() - 1 ) ) ? null :
                ArtworkDetailFragment.getInstance(mArtworkList.get(position));
    }

    @Override
    public int getCount() {
        return ( mArtworkList == null ) ? 0 : mArtworkList.size();
    }
}