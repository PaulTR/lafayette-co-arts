package com.ptrprograms.paultrebilcoxruiz.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.ptrprograms.paultrebilcoxruiz.R;
import com.ptrprograms.paultrebilcoxruiz.adapters.DrawerAdapter;
import com.ptrprograms.paultrebilcoxruiz.events.DrawerNavigationItemClickedEvent;
import com.ptrprograms.paultrebilcoxruiz.models.DrawerItem;
import com.ptrprograms.paultrebilcoxruiz.utils.NavigationBus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by paulruiz on 9/20/14.
 */
public class DrawerListView extends ListView implements ListView.OnItemClickListener {

    public DrawerListView(Context context) {
        this(context, null);
    }

    public DrawerListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DrawerListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initAdapter();
        setOnItemClickListener( this );
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        String drawerText = ( (DrawerItem) adapterView.getAdapter().getItem( position ) ).getDrawerText();
        NavigationBus.getInstance().post( new DrawerNavigationItemClickedEvent( drawerText ) );
    }

    private void initAdapter() {
        List<String> itemTitles = Arrays.asList(getResources().getStringArray(R.array.drawer_items));
        List<DrawerItem> items = new ArrayList<DrawerItem>();
        items.add( new DrawerItem( itemTitles.get( 0 ), R.drawable.ic_launcher ) );
        items.add( new DrawerItem( itemTitles.get( 1 ), R.drawable.ic_launcher ) );
        items.add( new DrawerItem( itemTitles.get( 2 ), R.drawable.ic_launcher ) );
        DrawerAdapter adapter = new DrawerAdapter( getContext() );
        adapter.addAll( items );
        setAdapter(adapter);
    }
}
