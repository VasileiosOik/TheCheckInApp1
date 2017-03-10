package com.example.thecheckinapp;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.ClipData.Item;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;

public class HelloItemizedOverlay extends ItemizedOverlay<OverlayItem> {
	

	private ArrayList<OverlayItem> mOverlays = new ArrayList<OverlayItem>();
	PlayGame p1;
	private Context mContext;
	
	public HelloItemizedOverlay(Drawable defaultMarker) {
		
		        super(boundCenterBottom(defaultMarker));
		}
		
		
	//The constructor must define the default marker
	public HelloItemizedOverlay(Drawable defaultMarker,Context context){//,PlayGame p2) {
			this(defaultMarker);
		    this.mContext=context;
		    //p2=p1;
		}
	//In order to add new OverlayItem objects to the ArrayList,
	public void addOverlay(OverlayItem overlay) {
	    mOverlays.add(overlay);
	    populate();
	}
	//it will call createItem(int) in the ItemizedOverlay to retrieve each OverlayItem.
	@Override
	protected OverlayItem createItem(int i) {
		// TODO Auto-generated method stub
		return mOverlays.get(i);
		
	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return mOverlays.size();
		
	}
	//will handle the event when an item is tapped by the user:
	
	@Override
	protected boolean onTap(int index) {
	  OverlayItem item = mOverlays.get(index);
	  
	  AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);

	  Log.i("aaaaaaaaaaaaa","bbbbbbbbbbbbb");
	  dialog.setTitle(item.getTitle());
	  dialog.setMessage(item.getSnippet());
	  dialog.show();
	  return true;
	}
	
	
	
}
