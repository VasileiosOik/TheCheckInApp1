package com.example.thecheckinapp;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.Arrays;


public class Listview extends Activity{

	Button btn;
	 	  
	  /** Called when the activity is first created. */
	  @Override
	  public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.list_checkin);
	    
	 // Find the ListView resource. 
		  ListView mainListView = (ListView) findViewById(R.id.mainListView);

	    // get the bundle from intent
	    Bundle b = this.getIntent().getExtras();
	    
	    // retrieve the string extra passed
	    String[] planets=b.getStringArray("transfer");
	    	    
	    ArrayList<String> namesList = new ArrayList<String>();
	    namesList.addAll( Arrays.asList(planets) );
	    
	    // Create ArrayAdapter using the list.
	   // listAdapter = new ArrayAdapter<String>(this, R.layout.simplerow, namesList);
		  ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(this, R.layout.simplerow, namesList);
	    // Set the ArrayAdapter as the ListView's adapter.
	    mainListView.setAdapter(listAdapter);
	    
	    btn=(Button)findViewById(R.id.findSelected);
        btn.setOnClickListener(new View.OnClickListener() {


             public void onClick(View v) {
               // Intent i=new Intent(Listview.this,xxxxxx.class);
                //startActivity(i);

             }});

    }
	    
 } 	  
	  
	
	 