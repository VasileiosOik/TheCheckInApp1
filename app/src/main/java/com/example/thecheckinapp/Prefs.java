package com.example.thecheckinapp;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;


public class Prefs extends  Activity implements OnCheckedChangeListener,OnClickListener
{
	static boolean satelliteMap;
	static boolean	st_music;
	private CheckBox set_button1;
	private CheckBox set_button2;
	String msg="";
	View detailedMap;
	
	//gia to sattelite view
	boolean Mapmode;
	//gia thn energopoihsh ths mousikhs
	private boolean myMusic = false;
	
	Intent svc;
	
	
	 @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	       
	        setContentView(R.layout.settingsmenu);
	        
	        set_button1=(CheckBox) findViewById(R.id.set_button1);
	        set_button2=(CheckBox) findViewById(R.id.set_button2);
	        setListener();
	        
	      //mapview orismos
			detailedMap= findViewById(R.id.set_button2);
	        detailedMap.setOnClickListener(this);
	        
	      
	        
	    	//set up listeners for buttons
	  	   View SAVEButton = findViewById(R.id.save_button);
	  	   SAVEButton.setOnClickListener(this);
	  	   //h nea klash gia th mousikh
	  	 svc=new Intent(Prefs.this,MusicService.class);
	  	   
	 }

	 private void setListener() {
		 set_button2.setOnCheckedChangeListener(new OnCheckedChangeListener() {
	        	@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
	        		  //  msg="View Change is Switched OFF";
	        		    Mapmode=false;
						if(isChecked){
						//	msg="View Change is Switched ON";	
							Mapmode=true;							
							set_button2=(CheckBox) detailedMap;
							set_button2.setChecked(true);
						}
						//Toast.makeText(Prefs.this, msg ,Toast.LENGTH_SHORT).show();
				}
			});  
		 
		 set_button1.setOnCheckedChangeListener(new OnCheckedChangeListener() {
	        	@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
	        		 //   msg="Music is Switched OFF";	        		  
	        				myMusic=false;
	        				//st_music=false;
						if(isChecked){
							//msg="Music is Switched ON";							
							//st_music=true;
							myMusic=true;
							
							startService(svc);
							
														
						}
						//Toast.makeText(Prefs.this, msg ,Toast.LENGTH_SHORT).show();
				}
			});
			
	 }
	 

	 @Override
	 public void onPause() {
	     super.onPause(); 
	     save2(set_button2.isChecked());
	     save1(set_button1.isChecked());
	 }

	 @Override
	 public void onResume() {
	     super.onResume();	    
	     set_button2.setChecked(load2());
	     set_button1.setChecked(load1());
	 }

	 private void save2(final boolean isChecked) {
	     SharedPreferences sharedPreferences = getPreferences(Context.MODE_PRIVATE);//MODE_PRIVATE
	     SharedPreferences.Editor editor = sharedPreferences.edit();
	     editor.putBoolean("check2",isChecked);
	     editor.commit();
	 }
	 
	 private void save1(final boolean isChecked) {
	     SharedPreferences sharedPreferences = getPreferences(Context.MODE_PRIVATE);
	     SharedPreferences.Editor editor = sharedPreferences.edit();
	     editor.putBoolean("check1", isChecked);
	     editor.commit();
	 }

	 private boolean load2() { 
	     SharedPreferences sharedPreferences = getPreferences(Context.MODE_PRIVATE);
	     return sharedPreferences.getBoolean("check2",Mapmode);//false);
	 }
	 
	 private boolean load1() { 
	     SharedPreferences sharedPreferences = getPreferences(Context.MODE_PRIVATE);
	     return sharedPreferences.getBoolean("check1", myMusic);//false);
	 }
	 
	 	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
    	case R.id.save_button:
    		
    		if(myMusic==true)
    		{
    			//gia th mousikh
    			st_music=true;
    			Context context = getApplicationContext();
    			CharSequence text = "Music will play";
    			int duration = Toast.LENGTH_LONG;// LENGTH_SHORT;   			
    			Toast toast = Toast.makeText(context, text, duration);
    			toast.show();
    			
    			//startService(svc);
    			
    		}
    		else
    			if(myMusic==false)
    		{
    				st_music=false;
    			Context context = getApplicationContext();
    		//	CharSequence text = "No Music will play";
    			int duration = Toast.LENGTH_LONG;// LENGTH_SHORT;   			
    		//	Toast toast = Toast.makeText(context, text, duration);
    		//	toast.show();
    			stopService(svc);
    		//	stopService(new Intent(this, MusicService.class));
    			
    		}
    		
    		if(Mapmode==true)
    		{
    			satelliteMap=true;
    			Context context = getApplicationContext();
    			CharSequence text = "Sattelite View enabled";
    			int duration = Toast.LENGTH_LONG;// LENGTH_SHORT;   			
    			Toast toast = Toast.makeText(context, text, duration);
    			toast.show();
    			Prefs.this.finish();
    		    
    		}
    		else
    		if(Mapmode==false)
    		{	
    			satelliteMap=false;
    			Context context = getApplicationContext();
    			//CharSequence text = "No view changed";
    			int duration = Toast.LENGTH_LONG;// LENGTH_SHORT;   			
    			//Toast toast = Toast.makeText(context, text, duration);
    			//toast.show();
    			Prefs.this.finish();
    		}
    		break;
		}
		
		
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
  	public void onBackPressed() {
  		Prefs.this.finish();
  	}

}