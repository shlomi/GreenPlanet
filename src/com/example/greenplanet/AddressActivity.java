package com.example.greenplanet;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.app.Activity;
import android.content.Intent;

public class AddressActivity extends Activity { 
	
	EditText City,Street,Number;	
	String city = null,street = null, number = null;
	
	//
	@Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.address_activity);
        
        City = (EditText) findViewById (R.id.editText1);
        Street = (EditText) findViewById (R.id.editText2);
        Number = (EditText) findViewById (R.id.editText3);
    }
	
	////****************************  מטפלים באירועים ********************///////	

    // לחצן - שלח
	public void onOK(View view)
	{	
		city = City.getText().toString();
		street = Street.getText().toString();
		number = Number.getText().toString();
		if (isNetworkAvailable()) 
			if  (city!=null && street!=null && number!=null ) 
			{							    
				StaticContent.city = city;
	    		StaticContent.street = street;
	    		StaticContent.number = number;  	
			    			    
				Intent getUploadSer = new Intent(this,UploadService.class);
			//	getUploadSer.putExtra(Constants.SenderId,Constants.SenderID.Act_3);
			//	getUploadSer.putExtra(Constants.TaskName,Constants.StartUploadService);					
				startService(getUploadSer);			
				finish(); 	
			}
			else // ...=null
				Toast.makeText(this, "enter all info", Toast.LENGTH_SHORT).show(); 
		else
			gotoNetworkSetting();
	}
	
    // לחצן - חזור למסך ראשי
	public void onCancel(View view)
	{
		 Intent back = new Intent(this,GreenPlanet.class);
	//     back.putExtra(Constants.SenderId,Constants.SenderID.Act_3);
	     startActivity(back);
	     finish();
	}
    
    ////****************************  פונקציות עזר פרטיות ********************///////
	
    //
	// מצריכה הרשאת NETWORK_STATE
    private boolean isNetworkAvailable() 
    {
        ConnectivityManager conManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo Network_Enabled = conManager.getActiveNetworkInfo();
        return (Network_Enabled != null);
    }
      
    //
    private void gotoNetworkSetting()
    {
    	Intent intent = new Intent(
			android.provider.Settings.ACTION_WIFI_SETTINGS);    		
    	startActivity(intent);    	
    }
    
}
