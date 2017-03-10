package com.example.thecheckinapp;



import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;


public class Start extends Activity implements OnClickListener {

	//declarations
	public static String person_id;
	public static String person_name;
	//private TextView text;
	private boolean exists;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);	
		setContentView(R.layout.activity_start);
		
		
		//set up the click listeners for all buttons
		View button1=findViewById(R.id.button1);
		button1.setOnClickListener(this);
		View button2=findViewById(R.id.button2);
		button2.setOnClickListener(this);
		View button3=findViewById(R.id.button3);
		button3.setOnClickListener(this);
		
		//serial number of the phone (IMEI)
	   TelephonyManager tManager = (TelephonyManager)this.getSystemService(Context.TELEPHONY_SERVICE);
       person_id = tManager.getDeviceId();
		String deviceId = Settings.Secure.getString(this.getContentResolver(),
				Settings.Secure.ANDROID_ID);

       Log.i("Start","Phone Serial Number Fetched");
       Log.i("IMEI", person_id);
		Log.i("IMEI", deviceId);
       
       //krathma timwn
       if(Prefs.st_music){
    	   Intent svc=new Intent(Start.this,MusicService.class);
    	   startService(svc);
       }
       else
       {
    	   //Intent svc=new Intent(Start.this,MusicService.class);
    	   //startService(svc);
    	   Context context = getApplicationContext();
			//CharSequence text = "NOOOOOOOOOOO";
			int duration = Toast.LENGTH_LONG;// LENGTH_SHORT;   			
			//Toast toast = Toast.makeText(context, text, duration);
			//toast.show();
       }
	}
	
	
	@Override
	public void onClick(View v) {

		//epilogh gia kathe koubi ksekinw thn diadikasia tou
		switch(v.getId())
		{
		case R.id.button3:
			//Start.this.finish();
			 System.exit(0);
			break;
		case R.id.button2:
			Intent i=new Intent(this,About.class);
			startActivity(i);
			break;
		case R.id.button1:
			 //Check if there is an Internet Connection
	        if (! isNetworkAvailable())
	        {
	        	Log.i("Start","No Internet Connection");
	        	buildAlertMessageNoInternet();        	
	        }
	        else
	        {
	        	Log.i("Start","Internet Connection OK");       	
	        	//text.setText("Please Wait...");
	        	new personexist().execute(this);
	        }
	        break;
	      
		}	
	
		
	}	
	
	
	/** Builds an alert Message if there is no Internet Connection **/
	  private void buildAlertMessageNoInternet() {
		  
	  	    final AlertDialog.Builder builder = new AlertDialog.Builder(this);
	  	  
	  	    
	  	    builder.setMessage("This application needs an Internet Connection. Do you want to enable it? ")
	  	           .setCancelable(false)
	  	           .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
	  	               public void onClick(final DialogInterface dialog, final int id) {
	  	                   startActivity(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS ));
	  	               }
	  	           })
	  	           .setNegativeButton("No", new DialogInterface.OnClickListener() {
	  	               public void onClick(final DialogInterface dialog, final int id) {
	  	                    dialog.cancel();
	  	                   // System.exit(0);
	  	               }
	  	           });
	  	    final AlertDialog alert = builder.create();
	  	    alert.show();
	  	    Log.i("Start","AlertMessage for no Internet shown");
	  	}  
	  
	  
	  
	  /** Checks if there is an Internet Connection **/
	  private boolean isNetworkAvailable() {
	  	    ConnectivityManager connectivityManager 
	          = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
	    Log.i("Start","Internet Connection Checked");
	    return activeNetworkInfo != null;
	}
	
	  
	   	  
	  //Thread that connects to the mySQL Server and checks if the user is already registered 
	  private class personexist extends AsyncTask<Context, Void, Context> {
	        
	    	protected Context doInBackground(Context... params) {
	    	 
	    		Log.i("Start","CheckifRegistered Thread: Started");
	    		Connection conn;
	            try {
	            	
	            	//Connect to mySQL Server
	            	//driver installed
	                Class.forName("com.mysql.jdbc.Driver").newInstance();
	                Log.i("workkkkkkkkkkk","workkkkkkkkkkkkkkk");
	                //h ip pou vrethike me to ip check gia to kinhto,akoma eftiaksa bash me to onoma tstdb pou exei vash me to onoma test
	                //epishs to my.cnf arxeio bind to 0.0.0.0 kai restart meta apo kathe allagh
	            
	                
	                String connString = "jdbc:mysql://141.237.78.194:3306/test";	                
	                String username = "root";
	                String password = "bill";
	                
	                /*
	                //apo ton server pou eftiaksa se free domain page	                
	                String connString = "jdbc:mysql://mysql6.000webhost.com/a9152141_testdbb";	                
	                String username = "a9152141_root";
	                String password = "bill123";
	                */
	                
	                conn = DriverManager.getConnection(connString,username,password);
	                Log.i("bikeeeeeee","bikeeeeeeeeee");
	                Statement stmt = conn.createStatement();
		    		Log.i("Start.java","personexist: SQL connection established");

	                
	                //Execute Query to find if user is already registered
	                ResultSet result_set = stmt.executeQuery("select id,name from user where id="+person_id+"");
		    		Log.i("Start.java","personexist: Query sent succesfully");
		    		
	                
	                if(result_set.next())	//user exists
	                {
	                	person_name=result_set.getString("name");
	                	exists=true;
	    	    		Log.i("Start.java","personexist: User Already Registered");

	                }
	                else 						//new user
	                {
	                	exists=false;
	    	    		Log.i("Start.java","personexist: New user");
	                }
	                	//kleinw thn epoikoinwnia me thn apomakrismenh vash
	                conn.close();

	              } catch (Exception e)
	                    {
	            	    Log.w("Start.java","personexist Error :"+e.getMessage());
	            	    exists=false;
	    	    		Log.i("Start.java","personexist: New user");
	                    }
	    		Log.i("Start.java","personexist: telos");

	            return params[0];
	            }
	    	
	    	
	    	
	        protected void onPostExecute(Context c) {
	        	 if(!exists)
		            {
		            	//New User:  Call RegisterScreenActivity
	    	    		Log.i("Start","Call Register");
		    			startActivity(new Intent(Start.this,Register.class));
		    			finish();
		            }
		            else
		            {
		            	//Old User:  Call MenuScreenActivity
		            	Log.i("Start","Call Beggining");		            
		    			startActivity(new Intent(Start.this,Beginning.class));
		    			finish();
		            }
	        }  
	    }
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_start, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
		case R.id.menu_settings:
			startActivity(new Intent(this,Prefs.class));
			return true;
		}
		return false;
		
	}
	
	@Override
  	public void onBackPressed() {
  		Start.this.finish();
  		
  	}
	
	
}
