package com.example.greenplanet;

import java.util.Timer;
import java.util.TimerTask;

import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
//import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Looper;
import android.widget.Toast;

public class LocationService extends Service 
                             implements LocationListener {

	LocationManager lm;	
	Timer timer;
	LocTask task;
	Looper loop;	
	Intent msg;
		
	////*******************  פונקציות של הסרוויס הנוכחי ***********************//////
	
	//
	@Override
	public void onCreate()
	{
		super.onCreate();
        lm = (LocationManager) getSystemService(LOCATION_SERVICE);        
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,this);	
        
    	msg = new Intent(this,UploadService.class);
        // msg.putExtra(Constants.SenderId,Constants.SenderID.Loc_Ser);
	}
	
	//
	@Override
	public void onDestroy()
	{
		super.onDestroy();	
	    lm.removeUpdates(this);
      	
	    if (StaticContent.locationServiceEndedSuccessfully)
	    {
	    	StringBuffer sbs = new StringBuffer("Longitude = ");            	    	
	        sbs.append(String.valueOf(StaticContent.location.getLongitude()));
			sbs.append("\nLatitude = ");
			sbs.append(String.valueOf(StaticContent.location.getLatitude()));
	    	Toast.makeText(this,sbs.toString(), Toast.LENGTH_LONG).show();
	    	
	    	if (isSendBtnPressed())
	    	{
		    	// need to ensure thet the network available
		    	//ניצור תיבת דיאלוג שתבקש מהמשתמש להפעיל את הרשת אם היא אינה פעילה 
		    	// אנו נפנה אותו לשם - והוא יוכל להמשיך רק דרך שלח / ביטול שליחה
		    	startService(msg);	// Start UploadService immediately   
	    	}
	    }
	    else  // לוקיישןסרוויס לא הסתיים בהצלחה - כלומר עם נתוני המיקום
	    {
	    	if (isSendBtnPressed())
	    	{
		    	StaticContent.isGPSlocationState = false; //  tell SendActivity which buttontext need to take
		    	
		    	//SharedPreferences sharedPreferences = getSharedPreferences(Constants.GreenPlanet_Preferences, MODE_PRIVATE);    
			    //SharedPreferences.Editor editor = sharedPreferences.edit();    
			    //editor.putString(Constants.Longitude,null); 
			    //editor.putString(Constants.Latitude,null); 		    	    
			    //editor.commit();
		    	
		    	// למעשה זה מיותר היות ובניגוד לפרפרנסס הוא אינו נשמר בין הפעלות שונות של האפליקציה ולכן אין צורך במחיקתו
			    StaticContent.location = null;
			    
			    Toast.makeText(this,"LocationService faild \n require to add address !"
			    		                                    , Toast.LENGTH_LONG).show();
			    Intent pass = new Intent(this,SendActivity.class);
		        //	pass.putExtra(Constants.SenderId,Constants.SenderID.Loc_Ser);
		      	pass.setFlags(Constants.FLAG_ACTIVITY_NEW_TASK);
		        startActivity(pass);  
	    	}
	    	else
	    	{
	    		riseAlertDialog();
	    	}
	    }    
	}
	
	//
	@Override
	public int onStartCommand(Intent intent, int flags, int startId)
	{	 
	    timer = new Timer(); 
		task = new LocTask();
		timer.schedule(task, 3 * 60 * 1000);
		
		return 1;  // 1 = START_STICKY	 
	}

	//
	@Override
	public IBinder onBind(Intent intent) 
	{
		return null;
	}
	 
	///**********************  מימוש הממשק לוקיישןליסטנר *******************//////
	
	//
	@Override
	public void onLocationChanged(Location location) 
	{
	    //  SharedPreferences sharedPreferences = getSharedPreferences(Constants.GreenPlanet_Preferences, MODE_PRIVATE);    
	    //  SharedPreferences.Editor editor = sharedPreferences.edit();    
	    //  editor.putString(Constants.Longitude,String.valueOf(location.getLongitude())); 
	    //  editor.putString(Constants.Latitude, String.valueOf(location.getLatitude())); 
		StaticContent.location = location;	    
	    //	editor.putBoolean(Constants.LocationServiceEndedSuccessfully,true); 	    	
		StaticContent.locationServiceEndedSuccessfully = true;   
	    	
	    //editor.commit();
	    stopSelf();	
	}

	//
	@Override
	public void onProviderDisabled(String provider) 
	{
		//
	}

	//
	@Override
	public void onProviderEnabled(String provider) 
	{
		//
	}

	//
	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) 
	{	
		//
	}


	////****************************  פונקציות עזר פרטיות ********************/////// 
    
	//
	private boolean isSendBtnPressed()
	{    
    	//SharedPreferences sharedPreferences = 
    	//		   getSharedPreferences(Constants.GreenPlanet_Preferences, MODE_PRIVATE);       	
		//return sharedPreferences.getBoolean(Constants.SendBtnState, false);
		return StaticContent.sendBtnState;
	}
		
	//
	private void riseAlertDialog()
	{
		Context context = getApplicationContext();
		OnClickListener listener = new DialogInterface.OnClickListener() 
		{			
			@Override
			public void onClick(DialogInterface dialog, int which) 
			{
				
			}
		};
		try
		{
			new AlertDialog.Builder(context)
	         .setTitle("alert message") // headline
	         .setMessage("we need to refrash this activity !") // the body message
	         .setNegativeButton("ok",listener).show();
		}
		catch (Exception e)
		{                 } 
	}
	
	// 
	private class LocTask extends TimerTask 
	{
		@Override
		public void run() 
		{
	    	//SharedPreferences sharedPreferences = getSharedPreferences(Constants.GreenPlanet_Preferences, MODE_PRIVATE);    
	        //SharedPreferences.Editor editor = sharedPreferences.edit();    
	    	//editor.putBoolean(Constants.LocationServiceEndedSuccessfully,false);		
            //editor.commit();
            StaticContent.locationServiceEndedSuccessfully = false;   			
            stopSelf(); 
		}
			
	}
		
}
