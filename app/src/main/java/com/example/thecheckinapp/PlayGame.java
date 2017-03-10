package com.example.thecheckinapp;

import android.view.View;
import android.view.View.OnClickListener;
import java.io.IOException;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


import org.json.JSONException;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;

public class PlayGame extends MapActivity implements OnClickListener
{

	private MapView map;
	private MapController map_controller;
	private double current_latitude=0;
	private double current_longitude=0;
	The4squarehelp foursquare_checkpoints=null;
	private LocationManager location_manager;
	private MyLocationOverlay user_overlay;
	private boolean is_first_time=true;
	private boolean is_visited[];
	private static ArrayList<Checkpoints> checkpoints_list;

	private String connection_address = "jdbc:mysql://141.237.78.194:3306/test";
	private String username = "root";
	private String password = "bill";
	private Statement stmt ;
	private Connection conn;

	public static int shown_checkpoints=0;
	private boolean is_in_thread=false; 
	static public TextView task;
	private int size;
	

	//********************************************initialize*********************************************
	@Override
	public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			
			/**Show Start Screen**/
			setContentView(R.layout.mapstart);
			
			//Thread Bug. Initializing
			try {
				Class.forName("android.os.AsyncTask");
			} catch (ClassNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			//Initialize Views
			//minimataki panw apo to xarth gia euresh topothesias
			task=(TextView)findViewById(R.id.maptask_label);
			//info=(TextView)findViewById(R.id.mapinfText);
			
			//initialize button gia to print twn topothesiwn 
			View rankButton = findViewById(R.id.map_print_button);
			rankButton.setOnClickListener(this);	
	        
		/**Initialize  MAP**/
			map = (MapView) findViewById(R.id.mapView);
			map.setClickable(true);
			map.setEnabled(true);
			map.displayZoomControls(true);	
			map.setBuiltInZoomControls(true);
			//apo ta settings
			map.setSatellite(Prefs.satelliteMap);
			
			//sinartisi epistrofhs topothesias xrhsth
			task.append("Finding your position on map and the checkpoints in your area\n");
			Userlocation();		
	}
	
		//******************************************USER LOCATION************************
	/**Get User Position and add overlay**/
	private void Userlocation()
	{
		//manage panning and zooming of a map.
		map_controller = map.getController();
		//An Overlay for drawing the user's current location (and accuracy) on the map, and/or a compass-rose inset. 
		user_overlay = new MyLocationOverlay(this, map);
		//ftiaxnw to overlay pou deixnei thn topothesia mou
		user_overlay.enableMyLocation();
		//eikonidio piksidas
		user_overlay.enableCompass();
		//Queues a runnable to be executed as soon as we have a location fix.
		user_overlay.runOnFirstFix(new Runnable() 
		{
			public void run() {
				// Zoom in to current location
				map_controller.setZoom(19);// setZoom(8);
				map_controller.animateTo(user_overlay.getMyLocation());
				current_latitude=user_overlay.getMyLocation().getLatitudeE6()/1e6;
				current_longitude=user_overlay.getMyLocation().getLongitudeE6()/1e6;
				
					}
			});	
		//prosthetei to diko mou
		map.getOverlays().add(user_overlay);
		
		
		//LocationManager is the main class through which your application can access location services on Android. 
		location_manager = (LocationManager)getSystemService(Context.LOCATION_SERVICE); 		
		  //Set LOcation Listener either wifi or GPS
			//location_manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
			location_manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
			//Log.i("User Coordinates: ","latitude: "+current_latitude+" longitude: "+current_longitude);
			
	}
	
	//**********************ORISMOS TOU LOCATIONLISTENER GIA THN EURESH THS TOPOTHESIAS******************************
	LocationListener locationListener = new LocationListener() {
	    public void onLocationChanged(Location location) {
	    	
	      // Called when a new location is found by the network location provider.
	    		current_latitude=location.getLatitude();
	    		current_longitude = location.getLongitude();
	    		
	    		Log.i("User Coordinates: ","latitude: "+current_latitude+" longitude: "+current_longitude);	
	    		
	    		//Log.i("Location Listener","Location changed");
	    		if(is_first_time ==true && is_in_thread==false)
	      		{	    		
	    			//drawCheckpointsonMap();	
	    		new UpdateMarkers().execute(); 
	      		}
	    		// Remove the listener you previously added
	    		location_manager.removeUpdates(locationListener);
	    			    	
	    }
	    
	    public void onStatusChanged(String provider, int status, Bundle extras) {}

	    public void onProviderEnabled(String provider) {}

	    public void onProviderDisabled(String provider) {}
	  };
	  
	  
	  
	  //********************************************Get Checkpoints from Foursquare******************************************
	  private void getFSCheckpoints()
	  {
	  	
	  	try {		
	  		
	  		foursquare_checkpoints=new The4squarehelp(current_latitude,current_longitude);
	  			
	  		
	  			} catch (IOException e) {
	  					Log.e("Foursquare Error","Checkpoints not found");
	  					e.printStackTrace();
	  		
	  			} catch (JSONException e) {
	  					Log.e("Foursquare Error","Checkpoints not found");
	  					e.printStackTrace();
	  		}	
	  }

	  //***********************h lista me ta checkpoints*********************************
	  static public ArrayList<Checkpoints> getCheckPoints()
	  {
	  	return checkpoints_list;
	  	
	  }
	  
	  //********************************* add to database******************************************
	  private void addCheckpointtoDB(String id,String name,long latitude,long longitude)
	  {
	  	
	      try {
	      	ResultSet reset;
	  		reset = stmt.executeQuery("select id_venue from `venues` where name_venue='"+name+"'");
	  		if(reset.next()==false)
	  		{
	  			stmt.execute("INSERT INTO  `test`.`venues` (`id_venue` ,`name_venue` ,`latitude` ,`longitude`)VALUES ('"+id+"','"+name+"','"+latitude+"','"+longitude+"');");
	  			//bohthiko
	  			stmt.execute("INSERT INTO  `test`.`visits` (`idvenue`)VALUES ('"+id+"');");  
	  			Log.i("venue insert ok",name);
	  		}
	  		else
	  			Log.i("checkpoint already added",name);
	  		
	      } catch (SQLException e) {
	    	  // TODO Auto-generated catch block
	    	  Log.e("insert venue error",name);
	      }
	      
	     
	  }
	  
	//**************************distance upologismos*******************************
	  public double distance(double lat1, double lng1, double lat2, double lng2) {
		    double earthRadius = 3958.75;
		    double dLat = Math.toRadians(lat2-lat1);
		    double dLng = Math.toRadians(lng2-lng1);
		    double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
		               Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
		               Math.sin(dLng/2) * Math.sin(dLng/2);
		    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
		    double dist = earthRadius * c;

		    return (dist*1609.344);
		    }
	  
	  
	  //*************************************SXEDIASMO TWN CHECKPOINTS STO XARTH***********************************
	  public void drawCheckpointsonMap()
	  {
	  /**Show Foursquare Checkpoints on Map**/
	  	if(foursquare_checkpoints!=null && checkpoints_list!=null)
	  	{		  	
	  	//Initialize map marker overlays
	  	List<Overlay> mapOverlays = map.getOverlays();
	  	//to mple pin
	  	Drawable drawable = PlayGame.this.getResources().getDrawable(R.drawable.pin);
	  	HelloItemizedOverlay itemizedoverlay = new HelloItemizedOverlay(drawable,this);//,PlayGame.this);
	  	//to kokkino pin
	  	Drawable drawable2 = PlayGame.this.getResources().getDrawable(R.drawable.pin_red);
	  	HelloItemizedOverlay itemizedoverlay2 = new HelloItemizedOverlay(drawable2,this);//,PlayGame.this);
	  	
	  	OverlayItem overlayitem[]=new OverlayItem[checkpoints_list.size()];
	  	GeoPoint gp[]=new GeoPoint[checkpoints_list.size()];
	  	
	  	
	  	//Clear markers from the android map
	  	mapOverlays.clear();
	  	map.getOverlays().clear();
	  	
	 //Get coordinates of each Checkpoint and show on Map
	 shown_checkpoints=0;
	 for(int i=0;i<checkpoints_list.size();i++)
	 {
	  		  	
	  	// Draw markers on map if the checkpoints are not visited by the current user
	  		//edw upirxe to is_visited[i]==false && shown_checkpoints<3 
	  	if(is_visited[i]==false  && checkpoints_list.get(i).visited==false)
	  	{
	  		
	  	 shown_checkpoints++;
	  	 float[] d=new float[1];
	  	 //vriskei thn apostash metaksei emou kai twn checkpoints pou vriskontai sto xarth
	  	 Location.distanceBetween(current_latitude,current_longitude,(checkpoints_list.get(i).getlat()),(checkpoints_list.get(i).getlongi()),d);
	  	 //h epistrefomenh apostash tou ekastote shmeiou
	  	 checkpoints_list.get(i).setDistance(d[0]);
	  	 //edw pernaw to onoma autou pou episkeftike to checkpoint
	  	 checkpoints_list.get(i).setUid(Start.person_id);
	  	 gp[i]= new GeoPoint((int)(checkpoints_list.get(i).getlat()*1e6),(int)(checkpoints_list.get(i).getlongi()*1e6));
	  	 overlayitem[i] = new OverlayItem(gp[i], checkpoints_list.get(i).getname(), "Distance: "+checkpoints_list.get(i).getDistance()+"m");
	  	 
	  	  //apostash megaliterh twn 50 metrwn(dikia mou simvash)
	  	 	if(checkpoints_list.get(i).getDistance()>50) // blue marker
	  	 	{
	  	 		//add this OverlayItem to your collection in the HelloItemizedOverlay instance and them add it to map 
	  	 		itemizedoverlay.addOverlay(overlayitem[i]);		 
	  	 		mapOverlays.add(itemizedoverlay);
	  	 	}
	  	 	else	//red marker <50
	  	 	{
	  	 		itemizedoverlay2.addOverlay(overlayitem[i]);		 
	  	 		mapOverlays.add(itemizedoverlay2);
	  	 	}
	  	
	  	}	 
	  	
	  		
	}
	  	//edw pali prosthete to diko mou marker
	  	 map.getOverlays().add(user_overlay);
	  	 
	  	 is_first_time=false;
	  	 //an den exoun brethei katholou checkpoints emfanise mnm
	  	 if(shown_checkpoints<1)
	  	 {
	  		 is_first_time=true;
	  		 Toast toast = Toast.makeText(getApplicationContext(),"Loading new checkpoints. Please wait..", Toast.LENGTH_LONG);
	  		 toast.show();
	  		 if(is_in_thread==false)
	     		{
	   		new UpdateMarkers().execute(); //drawCheckpointsonMap()(kalesmata);
	     		}
	  	 }
	  }
	  	//Refresh overlay object in Map Activity
	  	map.invalidate();
	}
	  
	  
	  //**************************************edw h megalh diskolia******************************************************
	 public class UpdateMarkers extends AsyncTask<Void, Void, Void> 
	 {
	         
          PlayGame map_activity;
          List<Overlay> mapOverlays;
          
          Drawable drawable_blue,drawable_red;
          HelloItemizedOverlay itemizedoverlay;
    	  HelloItemizedOverlay itemizedoverlay2;
    		
    		OverlayItem overlayitem[];
    		
    		GeoPoint gp[];
    		//boolean visited[];
    		
    	protected Void doInBackground(Void... params) {
  		
    		Log.i("UpdateMarkers Thread","UpdateMarkers Thread Start");	
  			
  			//Get list of checkpoints
  	  		if(is_first_time==true)
  	  		{
  	  			Log.i("UpdateMarkers Thread","FIRST TIME = TRUE");
  	
  	  			//kalw FS ginetai h sindesh kai epistrefei apotelesma
  	  			getFSCheckpoints();
  	  			if(foursquare_checkpoints!=null)
  	  			{
  	  				checkpoints_list=foursquare_checkpoints.getCheckpoints();
  	  				if(checkpoints_list!=null)
  	  					//h lista me ta checkpoints pou vriskontai diathesima se 1500 metra
  	  					Log.i("UpdateMarkers Thread ","Foursquare Success:"+checkpoints_list.size()+" Checkpoints Aquired");
  	  			}
  	  		}

  	  		
if(foursquare_checkpoints!=null)
{
  			overlayitem=new OverlayItem[checkpoints_list.size()];
  			gp=new GeoPoint[checkpoints_list.size()];
  			is_visited=new boolean[checkpoints_list.size()];
  			shown_checkpoints=0;
  		
  		
  		try {
  			if(conn==null)
  			{
  			Class.forName("com.mysql.jdbc.Driver").newInstance();
  		    
  		    conn = (Connection) DriverManager.getConnection(connection_address,username,password);
  		    Log.w("Connection","open");
  		    stmt = (Statement) conn.createStatement();

  			}
  			else
  				if(conn.isClosed())
  				{
  					Class.forName("com.mysql.jdbc.Driver").newInstance();
  				    
  				    conn = (Connection) DriverManager.getConnection(connection_address,username,password);
  				    Log.w("Connection","open");
  				    stmt = (Statement) conn.createStatement();
  				}
  		    } catch (Exception e)
  		    {
  		    Log.w("Error connection","" + e.getMessage());
  		    }
  		
  		//ta diathesima pouvrike einai n
  		Log.i("UpdateMarkers Thread ","1st Checkpoints List size :"+checkpoints_list.size());
  	
  	for(int i=0;i<checkpoints_list.size();i++)
  	{
  		
  			Log.i("UpdateMarkers Thread ","Checkpoints List size :"+checkpoints_list.size());
  			Log.i("UpdateMarkers Thread ","Current check: "+i+" Shown checkpoints :"+shown_checkpoints);
  			is_visited[i]=false;
  		
  			// Check if the user visited the current checkpoints
  			//edw upirxe to is_first_time==true && shown_checkpoints<3 
  			if(is_first_time==true  &&  checkpoints_list.size()>i)
  			{
  				ResultSet reset,res2= null;
  				try {
  					// Get id of checkpoint
  					reset = stmt.executeQuery("select id_venue from `venues` where name_venue='"+checkpoints_list.get(i).getname()+"'");
  					Log.i("UpdateMarkers Thread ","Query sent :"+checkpoints_list.get(i).getname());
  				
  					if(reset.next()==true) //if checkpoint exists in DB
  					{
  						Log.i("UpdateMarkers Thread ","Checkpoint already in database:"+checkpoints_list.get(i).getname());
  						
  						//Check if the user visited that checkpoint
  						//Log.i("edwwwwwwwwwwwww","edwwwwwwwwwwwwwwww");
  						//res2 = stmt.executeQuery("select * from `visits` where idvenue="+reset.getString("id_venue")+" AND idvisits='"+Start.person_id+"'"); 
  						res2 = stmt.executeQuery("select * from `visits` where idvenue='"+reset+"' AND idvisits='"+Start.person_id+"'");
  						
  						if(res2.next()==true) //if checkpoint exists in user visits
  						{
  							is_visited[i]=true;
  							Log.i("UpdateMarkers Thread ","Checkpoint already in visits:"+checkpoints_list.get(i).getname());
  						}
  						else
  						{
  							is_visited[i]=false;
  							Log.i("UpdateMarkers Thread ","Checkpoint not visited: "+checkpoints_list.get(i).getname());
  						}
  						
  					
  					}
  					else              
  					{
  						is_visited[i]=false;
  						Log.i("UpdateMarkers Thread ","Checkpoint not in database: "+checkpoints_list.get(i).getname());
  						addCheckpointtoDB(checkpoints_list.get(i).getidfsch(), checkpoints_list.get(i).getname(),(int)(checkpoints_list.get(i).getlat()*1e6),(int)(checkpoints_list.get(i).getlongi()*1e6));
  					 
  					}
  		     
  					//Log.i("UpdateMarkers Thread DB check",checkpoints_list.get(i).getname()+": CheckpointClass: "+checkpoints_list.get(i).visited+ " Database: "+ is_visited[i]);
  				
  					} catch (SQLException e) {
  						Log.i("UpdateMarkers Thread db query","not found" +e.getMessage());
  						try {
  							conn = (Connection) DriverManager.getConnection(connection_address,username,password);
  						} catch (SQLException e1) {
  							// TODO Auto-generated catch block
  							e1.printStackTrace();
  						}
  						Log.w("UpdateMarkers Thread Connection","open");
  						try {
  							stmt = (Statement) conn.createStatement();
  						} catch (SQLException e1) {
  							// TODO Auto-generated catch block
  							e1.printStackTrace();
  						}
  						is_visited[i]=true;
  					}
  				
  			}
  		
  		
  		
  			// Draw markers on map if the checkpoints are not visited by the current user
  			//edw upirxe to is_visited[i]==false && shown_checkpoints<3
  			if(is_visited[i]==false && checkpoints_list.get(i).visited==false)
  			{
  		 
  				//Log.i("UpdateMarkers Thread place to visit"," "+i+" "+checkpoints_list.get(i).getname());
  		
  				//upologismoa apostashs
  				float[] d=new float[1];
  				Location.distanceBetween(current_latitude,current_longitude,(checkpoints_list.get(i).getlat()),(checkpoints_list.get(i).getlongi()),d);
  				checkpoints_list.get(i).setDistance(d[0]);
  				//edw peranw to onoma autou pou episkeftike to checkpoint
  				checkpoints_list.get(i).setUid(Start.person_id);
  				gp[shown_checkpoints]= new GeoPoint((int)(checkpoints_list.get(i).getlat()*1e6),(int)(checkpoints_list.get(i).getlongi()*1e6));
  				overlayitem[shown_checkpoints] = new OverlayItem(gp[shown_checkpoints], checkpoints_list.get(i).getname(), "Distance: "+checkpoints_list.get(i).getDistance()+"m");
  				shown_checkpoints++;
  		 
  			}	 
  			else
  			{
  				if(checkpoints_list!=null)
  				{
  					checkpoints_list.remove(i);
  					i--;
  				}
  			}
  			if(i>=0 && checkpoints_list.size()>0)
  				Log.i("Checkpoints to draw"," "+i+" "+checkpoints_list.get(i).getname());
  		
  	}
  		  		
    	
}
  		 		
  		Log.i("UpdateMarkers Thread","UpdateMarkers Thread Exit "+checkpoints_list.size());		
  		return null;
        	
}
  	
      protected void onPostExecute(Void result) {
      	    	
      	Log.i("UpdateMarkersPost","Start");
      	
      	mapOverlays.clear();
      	map.getOverlays().clear();
      	
      	Log.i("UpdateMarkersPost","Start drawing");
      	if(checkpoints_list.size()!=0)
      	for(int i=0;i< shown_checkpoints;i++)  
      	{
      		if(checkpoints_list.get(i).getDistance()>50) // blue marker
      		{
      			itemizedoverlay.addOverlay(overlayitem[i]);		 
      			mapOverlays.add(itemizedoverlay);
      		}
      		else	//red marker
      		{
      			itemizedoverlay2.addOverlay(overlayitem[i]);		 
      			mapOverlays.add(itemizedoverlay2);
      		}
      	}
      	
      	Log.i("UpdateMarkersPost",shown_checkpoints +" places on map");
      	//to megethos you pinaka gia ton swsto orismo tou
      	size=shown_checkpoints;
   
      	map.getOverlays().add(user_overlay);
      	if(is_first_time==true)
      	{
      		Toast toast = Toast.makeText(getApplicationContext(),"Checkpoints on map updated succesfully", Toast.LENGTH_LONG);
      		toast.show();
      		task.setText("");
      	}
      	is_first_time=false;
      	if(shown_checkpoints==0)
      	{
      		//first_time=true;
      		
      		//task.append("Φόρτωση νέων σημείων ελέγχου. Παρακαλώ περιμένετε.\n");
      		task.setText("There are no checkpoints in a radius of 1500 meters available for check-in\n");
      	} 
      	Log.i("UpdateMarkersPost","First time "+is_first_time);
      	Log.i("UpdateMarkersPost","Finished");
      	map.invalidate();
      	is_in_thread=false;
      	
      }
    	  	    	
    	  protected void onPreExecute()
    	  {
    		/**Show Foursquare Checkpoints on Map**/
    		is_in_thread=true;
    		if(is_first_time==true)  
    		task.append("Updating checkpoints...\n");
    	 		
    		//Initialize map marker overlays blue and red
    		mapOverlays = map.getOverlays();
    		drawable_blue = PlayGame.this.getResources().getDrawable(R.drawable.pin);
    		itemizedoverlay = new HelloItemizedOverlay(drawable_blue,PlayGame.this);//,map_context,map_context);
    		
    		drawable_red = PlayGame.this.getResources().getDrawable(R.drawable.pin_red);
    		itemizedoverlay2 = new HelloItemizedOverlay(drawable_red,PlayGame.this);//,map_context,map_context);
    		  
    	  }
    }
	    
	 //****************************h synartisi gia thn ektipwsh twn onomatwn pou exei mesa o pinakas venues****************************************************************************
	  
	  public class printplaces extends AsyncTask<Context, Void, Context>
	  {

		@Override
		protected Context doInBackground(Context... params) {
			// TODO Auto-generated method stub
			Log.i("PlayGame","Select the rows");
			//dilwsh pinaka
			String[] new_ch=new String[size];
			
    		try {
            	
            	//Connect to mySQL Server
            	//driver installed
                Class.forName("com.mysql.jdbc.Driver").newInstance();              
                String connString = "jdbc:mysql://141.237.78.194:3306/test";                
                String username = "root";
                String password = "bill";
                Statement stmt = (Statement) conn.createStatement();
	    		Log.i("PlayGame"," SQL connection established");

	    		for(int i=0;i<checkpoints_list.size();i++)
		  		{
	    		
                //Execute Query to find the names of the checkpoints
	    		
                ResultSet result_set = stmt.executeQuery("select name_venue from `venues` where name_venue='"+checkpoints_list.get(i).getname()+"'");
	    	//	Log.i("PlayGame.java","sinartisi: Query sent succesfully");
	    			if(result_set.next()==true){
	    				Log.i("heyyy",checkpoints_list.get(i).getname());
	    				new_ch[i]=checkpoints_list.get(i).getname();
	    			}else
	    			{
	    				finish();
	    			}
            }
	    		//gia na perasw ton pinaka se allo activity
	    		Bundle b=new Bundle();
	    		b.putStringArray("transfer", new_ch);
	    		Intent i=new Intent(PlayGame.this,chlistview.class);
	    	//	Intent i=new Intent(PlayGame.this,ListViewCheckboxesActivity.class);
	    		
	    		i.putExtras(b);
	    		startActivity(i);
            }
            catch (Exception e)
            {
    	    Log.w("PlayGame"," Errorrrrrrrrr :"+e.getMessage());
    	   // exists=false;
    		Log.i("PlayGame","okkkkkkkk:");
            }
            Log.i("Print data"," telos kalo");

            return params[0];
   		
	     }
		
		protected void onPostExecute(Context result) {
			
			Toast toast = Toast.makeText(getApplicationContext(),"Succesfully Printed", Toast.LENGTH_LONG);
      		toast.show();
      		task.setText("");
      				
		}
		
	  }
	//*******************************to print button***********************************************************************
	  
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		
		case R.id.map_print_button:			 			      
			new printplaces().execute(this);
			//startActivity(new Intent(PlayGame.this,listofpossiblecheckins1.class));
				break;
		default: 
				finish();
		}
	}
	
	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public void onBackPressed() {
		//Intent intent = new Intent(PlayGame.this,Beginning.class); 
		//startActivity(intent); 
		PlayGame.this.finish();
	 }
		
	public void finish() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage("Do you really want to exit?");
        builder1.setCancelable(false);
        builder1.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                quit();
            }
        });
        builder1.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        AlertDialog alert1= builder1.create();
        alert1.show();
    }

    public void quit() {
        super.finish();
        System.exit(0);
    };
	
}
