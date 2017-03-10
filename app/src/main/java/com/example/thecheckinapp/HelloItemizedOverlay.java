package com.example.thecheckinapp;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;

import java.util.ArrayList;

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
		return mOverlays.get(i);
		
	}

	@Override
	public int size() {
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
