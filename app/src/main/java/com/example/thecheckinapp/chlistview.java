package com.example.thecheckinapp;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;


public class chlistview extends Activity{

	  private ListView mainListView ;
	  private ArrayAdapter<String> listAdapter ;
	  Button btn;
	 	  
	  /** Called when the activity is first created. */
	  @Override
	  public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.list_checkin);
	    
	 // Find the ListView resource. 
	    mainListView = (ListView) findViewById( R.id.mainListView );
	    
	    /** get the bundle from intent*/
	    Bundle b = this.getIntent().getExtras();
	    
	    /** retrieve the string extra passed */   
	    String[] planets=b.getStringArray("transfer");
	    	    
	    ArrayList<String> namesList = new ArrayList<String>();
	    namesList.addAll( Arrays.asList(planets) );
	    
	    // Create ArrayAdapter using the list.
	   // listAdapter = new ArrayAdapter<String>(this, R.layout.simplerow, namesList);
	    listAdapter = new ArrayAdapter<String>(this, R.layout.simplerow, namesList);
	    // Set the ArrayAdapter as the ListView's adapter.
	    mainListView.setAdapter( listAdapter );  
	    
	    btn=(Button)findViewById(R.id.findSelected);
        btn.setOnClickListener(new View.OnClickListener() {


             public void onClick(View v) {
               // Intent i=new Intent(chlistview.this,xxxxxx.class);
                //startActivity(i);

             }});

    }
	    
 } 	  
	  
	
	 