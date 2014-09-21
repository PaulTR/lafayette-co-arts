package com.ptrprograms.paultrebilcoxruiz.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.ptrprograms.paultrebilcoxruiz.R;
import com.ptrprograms.paultrebilcoxruiz.models.Artwork;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

/**
 * Created by paulruiz on 9/20/14.
 */
public class GalleryGridAdapter extends ArrayAdapter<Artwork> {

    public GalleryGridAdapter( Context context ) {
        this( context, 0 );
    }

    public GalleryGridAdapter(Context context, int resource) {
        super(context, resource);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder mImageHolder = null;

        if( convertView == null ) {
            mImageHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate( R.layout.view_gallery_grid_image, null );
            mImageHolder.image = (ImageView) convertView.findViewById( R.id.grid_view_item );

            if( mImageHolder.image == null )
                return null;

            convertView.setTag( mImageHolder );
        } else
            mImageHolder = (ViewHolder) convertView.getTag();


        mImageHolder.image.setVisibility( View.GONE );
        setupGridImage( mImageHolder, position );

        return convertView;
    }

    private void setupGridImage( ViewHolder holder, final int position ) {

        final String imageUrl = getItem( position ).getResizedUrl();
        final ViewHolder tmpHolder = holder;
        Picasso.with(getContext()).load( imageUrl ).error( R.drawable.ic_launcher ).into( holder.image, new Callback() {
            @Override
            public void onSuccess() {
                tmpHolder.image.setVisibility( View.VISIBLE );
                Log.e( "GalleryGridAdapter", "Picasso loaded successfully" );
            }

            @Override
            public void onError() {
                Artwork item = getItem( position );
                int resId = tmpHolder.image.getContext().getResources().getIdentifier( item.getImage().toLowerCase().replaceAll("-", "_").substring( 0, item.getImage().length() - 4 ), "drawable", tmpHolder.image.getContext().getPackageName() );
                tmpHolder.image.setImageResource( resId );
                tmpHolder.image.setVisibility( View.VISIBLE );


            }
        });
    }


    public class ViewHolder {
        ImageView image;
    }
}
