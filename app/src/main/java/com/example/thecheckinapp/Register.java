package com.example.thecheckinapp;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class Register extends Activity implements OnClickListener {

	
	private  View register_button;	
	private boolean is_registered=false;
	private TextView task_text ;
	private EditText nickname_text;
	 /** Called when the activity is first created. */
		@Override
		    public void onCreate(Bundle savedInstanceState) {
		        super.onCreate(savedInstanceState);
		        
		        //register screen
		        Log.i("Register", "Gia na doume");
		        setContentView(R.layout.register);
		        
		      //Initialize Views
		        register_button = findViewById(R.id.register_button);
		        register_button.setOnClickListener(this);
		       nickname_text=(EditText) findViewById(R.id.registerText);
		       task_text = (TextView)findViewById(R.id.registertask_label);
		       
		       
		}
		
	@Override
	public void onClick(View v) {

		switch(v.getId()){
    	case R.id.register_button:
    		//tipwnw oti egrapsa mesa sto edittext
    		Log.v("EditText value=", nickname_text.getText().toString());
    		Log.i("prin bei sto register", "Register");
    		//kalw thn registerfunction me orisma ton string xarakthra pou dwthike gia onoma
            new Registerfunction().execute(nickname_text.getText().toString());          
            break;
    		
    
    	}
	}

	 private class Registerfunction extends AsyncTask<String, Void, String> {
		 
	    	protected String doInBackground(String... params) {
	    		Log.i("RegisterScreenActivity", "RegisterThread: Start");
	    		Connection conn;
	            try {
	            Class.forName("com.mysql.jdbc.Driver").newInstance();
	            String connString = "jdbc:mysql://141.237.78.194:3306/test";                
                String username = "root";
                String password = "bill";
	            conn = DriverManager.getConnection(connString,username,password);
	            Statement stmt = conn.createStatement();
	            Log.i("RegisterScreenActivity", "RegisterThread: Connection Established");
	           
	            
	            stmt.execute("INSERT INTO `test`.`user` (`id`, `name`, `plvisits`) VALUES ('"+Start.person_id+"', '"+params[0]+"', '0');");
	            Start.person_name=params[0];
	            Log.i("RegisterScreenActivity", "RegisterThread: Registration of User Succesful");
	            is_registered=true;
	           
	            conn.close();

	            } catch (Exception e)
	            {
		        Log.w("RegisterScreenActivity", "RegisterThread error: "+e.getMessage());
	            is_registered=false;
	            }
	        	
	            Log.i("RegisterScreenActivity", "RegisterThread: Thread Exit");
	    		
	            return params[0];
	            }


	        protected void onPostExecute(String result) {
	        	
	        	
	        	if(!is_registered)
	        	{
	        		task_text.setText(R.string.nameChange);
	        		Log.i("RegisterScreenActivity", "Nickname already taken");
	        		register_button.setEnabled(true);
	        	}
	        	else
	        		{
	        		Toast.makeText(getApplicationContext(), "Succesful account creation",Toast.LENGTH_LONG).show();
	        		Log.i("RegisterScreenActivity", "Call MenuScreenActivity");
	        		startActivity(new Intent(Register.this,Beginning.class));
	        		finish();
	        		}
	        }
	        
	        protected void onPreExecute() {
	        	register_button.setEnabled(false);
	        	
	        	task_text.setText(R.string.accountNew);
	        }
	        
	    }  
	 
	 /** Physical back button click handler **/
		@Override
		public void onBackPressed() {
			//Intent intent = new Intent(Register.this,Start.class); 
			//startActivity(intent); 
				//Register.this.finish();
			super.finish();
		}
}
