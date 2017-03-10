package com.example.thecheckinapp;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class The4squarehelp {

ArrayList<Checkpoints> ch=new ArrayList<Checkpoints>();
	
   public The4squarehelp(double x,double y) throws IOException, JSONException
   {

	 URL fs = new URL("https://api.foursquare.com/v2/venues/search?limit=20&ll="+x+","+y+"&radius=50&&client_id=1T5X3LDPLKVMTPJTDH2W4Z1PJO1ICGNWHFBM05T5AWVCT52V&client_secret=XO4XJCFMNXBSIY53CK5JYLFNAUHA31U3HUJTRYHWEY1ZZM0Z&v=20120301");

		InputStream is = null;
		String result = "";
	
		//anoigw thn sindesh
		URLConnection yc = fs.openConnection();
		//etoimazw to string
	    BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
	    String inputLine=in.readLine(); 
	    //gia ta ellhnika
	    is=new ByteArrayInputStream(inputLine.getBytes("iso-8859-7"));
	    
		//convert response to string
		try{
			BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-7"),8);
			//BufferedReader reader = new BufferedReader(new InputStreamReader(is,"Windows-1253"),8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			is.close();
			result=sb.toString();
		}catch(Exception e){
			Log.e("log_tag", "Error converting result "+e.toString());
		}
		
		//tipwnei to apotelesma se ena megalo string
		System.out.println("result: "+result);
		
		//edw pairnw ta onomata apo to string pou einai ta onomata twn venues
		JSONObject json=new JSONObject(result);
		JSONArray venues = json.getJSONObject("response").getJSONArray("venues"); 
	     String[] name=new String [venues.length()];
	     String[] idfs=new String [venues.length()];
	     for (int i = 0; i<venues.length();i++){
	         name[i] = venues.getJSONObject(i).optString("name");
	     	 idfs[i]=venues.getJSONObject(i).optString("id");
	     }
	        //tupwnw onomata
	    // for (int i = 0; i<venues.length();i++)
	      //   Log.i("Name "+i+" ", name[i]);
	   
	   	
	     double geoLat;
	     double  geoLong;    
	     int distance;
	     for(int i=0;i<venues.length();i++)
	     {
	    	 Log.i("Name "+i+" ", name[i]); 
	    	 Log.i("id merous "+i+" ", idfs[i]);
	    	 JSONObject venueObject = venues.getJSONObject(i);
	    	 JSONObject loc=venueObject.getJSONObject("location");
	    	 geoLat = loc.getDouble("lat");
	    	 geoLong = loc.optDouble("lng");
	    	 distance=loc.getInt("distance");
	    	 	     
	    	  System.out.println("lat "+geoLat+" Long " +geoLong +" Distance "+distance);
	         //ftiaxnw to checkpoint me tis plirofories pou pira apo to FS
	    	 ch.add(new Checkpoints(idfs[i],geoLat,geoLong,name[i], distance));
	     
	     }
	          
	}
     
     public ArrayList<Checkpoints> getCheckpoints()
     {
    	 return ch;
    	 
     }
     
}
