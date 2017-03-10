package com.example.thecheckinapp;


import android.app.Activity;
import android.util.Log;
import android.view.View.OnClickListener;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;


public class Beginning extends Activity implements OnClickListener {

	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("Start","Beggining");
        setContentView(R.layout.start2);
        Log.i("Start","after Beggining");
               
        //Initialize Views and Set up listeners for buttons
        View startButton = findViewById(R.id.start_button);
        startButton.setOnClickListener(this);
       
      
        
    }
    
    /**	Builds an alert Message if there GPS is not enabled **/
	  private void buildAlertMessageNoGps() {
	    final AlertDialog.Builder builder = new AlertDialog.Builder(this);
	    builder.setMessage("This application needs GPS in order to continue. Do you want to activate it? ")
	           .setCancelable(false)
	           .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
	               public void onClick(final DialogInterface dialog, final int id) {
	                   startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));// ACTION_LOCATION_SOURCE_SETTINGS));
	               }
	           })
	           .setNegativeButton("No", new DialogInterface.OnClickListener() {
	               public void onClick(final DialogInterface dialog, final int id) {
	                    dialog.cancel();
	               }
	           });
	    final AlertDialog alert = builder.create();
	    alert.show();
	}
	  
	  /** Builds an alert Message if there is no Internet Connection **/
	  private void buildAlertMessageNoInternet() {
	  	    final AlertDialog.Builder builder = new AlertDialog.Builder(this);
	  	    builder.setMessage("The application needs an Internet Connection. Do you want to activate it? ")
	  	           .setCancelable(false)
	  	           .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
	  	               public void onClick(final DialogInterface dialog, final int id) {
	  	                   startActivity(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS ));
	  	               }
	  	           })
	  	           .setNegativeButton("No", new DialogInterface.OnClickListener() {
	  	               public void onClick(final DialogInterface dialog, final int id) {
	  	                    dialog.cancel();
	  	               }
	  	           });
	  	    final AlertDialog alert = builder.create();
	  	    alert.show();
	  	}  
	  
	  
	  
	  /** Checks if there is an Internet Connection **/
	  private boolean isNetworkAvailable() {
	  	    ConnectivityManager connectivityManager 
	          = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
	    return activeNetworkInfo != null;
	  }
	  
	@Override
	public void onClick(View v) {
		switch(v.getId()){
    	case R.id.start_button:
    		final LocationManager manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );
	    	
    	    if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) 
    	        buildAlertMessageNoGps();    	    
    	    else
    	    	if (! isNetworkAvailable())
    	    		{
    	    		buildAlertMessageNoInternet();
    	    		}
    	    	else
    	    		{
    	    		
    	    		startActivity(new Intent(Beginning.this,SpalshScreenActivity.class));
    	    		finish();
    	    		}
    		break;
		}
	}
	
	// Physical back button click handler 
	@Override
	public void onBackPressed() {
	 //Beginning.this.finish();
		super.finish();
	}
	
	/*
	  @Override  
	   public boolean onKeyDown(int keyCode, KeyEvent event)  
	   {  
	       if (keyCode == KeyEvent.KEYCODE_BACK) {
	           finish();
	           return true;
	       }

	       return super.onKeyDown(keyCode, event); 
	}
	*/
	/*
	@Override
	public void onBackPressed() {
	   Log.d("CDA", "onBackPressed Called");
	   Intent setIntent = new Intent(Intent.ACTION_MAIN);
	   setIntent.addCategory(Intent.CATEGORY_HOME);
	   setIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
	   startActivity(setIntent);
	}
	*/
}
