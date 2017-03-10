package com.example.thecheckinapp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;


public class Checkpoints {
	private double lat;
	private double longi;
	private String name;
	private String idfsch;
	public String uid;
	private int distance;
	public boolean visited=false;
	
	//edw ftiaxnw to kathe checkpoint
	public Checkpoints(String or,double x, double y,String nam,int d)
	{
		idfsch=or;
		lat=x;
		longi=y;
		name=nam;
		distance=d;
		
	}
	
	public String getidfsch()
	{
		return idfsch;
	}

	
	public double getlat()
	{
		return lat;
	}
	
	public double getlongi()
	{
		return longi;
	}
	public String getname()
	{
		return name;
	}
	/**
	 * @return
	 * @uml.property  name="distance"
	 */
	//apostash
	public int getDistance()
	{
		return distance;
	}
	
	
	public void setUid(String u)
	{
		uid=u;
	}
	public void setVisited(boolean v,Context cm)
	{
		visited=v;
				
		new UpdateVisits().execute(cm);	
		
	}
	public void setDistance(double x)
	{
		distance = (int)x;
		
	}
	
	
	  private class UpdateVisits extends AsyncTask<Context, Void, Context> {
	       	        
	    	protected Context doInBackground(Context... params) {
	    		//Log.i("to pireeeeeeeeeeee: ", idfsch);
	    		Connection conn2 = null;
		        
	            try {
					Class.forName("com.mysql.jdbc.Driver").newInstance();
				
	            String connString = "jdbc:mysql://141.237.78.194:3306/test";
	            String username = "root";
	            String password = "bill";
	            conn2 = DriverManager.getConnection(connString,username,password);
	            Log.w("Connection","open");
	            Statement stmt = conn2.createStatement();
	            ResultSet reset;
	    		reset = stmt.executeQuery("select id_venue from `venues` where name_venue='"+name+"'");
	    		
	    		
	    		if(reset.next())
	    		{	
	    			String cid=reset.getString("id_venue");	   			
	    			stmt.execute("INSERT INTO  `test`.`visits` (`idvisits` ,`idvenue`)VALUES ('"+uid+"','"+cid+"');");	    			
	    			stmt.executeUpdate("UPDATE  `test`.`user` SET  `plvisits` =  plvisits+1, WHERE id = '"+uid+"'");
	    				    			
	    		}
	        
	    		conn2.close();
	            } catch (Exception e)
                {
                Log.e("insert visit fail",""+e.getMessage());
                
                }
	        	
	    		
	    		
	            return params[0];
	            }


	        protected void onPostExecute(Context result) {
	        	if(PlayGame.shown_checkpoints>0)
	        	{
	        		PlayGame.task.setText("");
	       
	        	}
	        	AlertDialog.Builder dialog2 = new AlertDialog.Builder(result);	             
	             dialog2.setMessage("The checkpoint '"+name +"' was stored in database succesfully");
	     		 dialog2.show();
	        	
	        }
	        protected void onPreExecute() {

	        	PlayGame.task.setText("Checking-in...");
	        }
	    }

}
