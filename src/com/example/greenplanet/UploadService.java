package com.example.greenplanet;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
//import android.widget.Toast;

public class UploadService extends IntentService {
	
	//Intent msg;	
	StringBuffer sbs;	

	////**************  פונקציות שאנו זקוקים להם באינטנטסרוויס הנוכחי *****************//////
	
	//
	public UploadService() 
	{
		super("UploadService");
	}

	//
	@Override
	public void onCreate()
	{
		super.onCreate();
	    //	msg = new Intent(this,Adapter.class);
	    //	msg.putExtra(Constants.SenderId,Constants.SenderID.Up_Ser);
	}
	
	//
	@Override
	public void onDestroy()
	{
		super.onDestroy();		
	    // startService(msg);
		
		// need permission WRITE_EXTERNAL_STORAGE
    	File file = new File(StaticContent.Path); 
    	if (file.delete()) //deleteOnExit()
    	{	
    		//Toast.makeText(this, "file deleted", Toast.LENGTH_LONG).show(); 
    	}
    	else
    	{	
    		//Toast.makeText(this, "file faild", Toast.LENGTH_LONG).show();
    	}
		
    	notifyMe();//
	    // if uploadservice ended successfully 
		     // exit from app 	    	
		//Toast.makeText(this, StaticContent.serverResponseBody, Toast.LENGTH_LONG).show();
	}
	
	//
	@Override
	public void onHandleIntent(Intent intent)  
	{   			        	
		prepInfoToSend();      	
		sendAll(); 
	}
	
    ////****************************  פונקציות עזר פרטיות ********************///////   
		   
    //
    private void prepInfoToSend()
    {    
        sbs = new StringBuffer("");
        sbs.append(Constants.GREEN_PLANET_SERVER + Constants.SERVER_REQUEST + "&"); 
        String eqe = "=";
        String and = "&"; 
        if ( !getUserID().equals(Constants.EMPTY) ) //  && (getUserID() != null ) ) 
        	sbs.append(Constants.USER_ID + eqe + getUserID() + and );	
        if ( !getGoogleAccount().equals(Constants.EMPTY) )
        	sbs.append(Constants.GOOGLE_ACCOUNT + eqe + getGoogleAccount() + and );	
    	if (StaticContent.isGPSLocationType) 
    	{
    		sbs.append(Constants.LONGITUDE + eqe + String.valueOf(StaticContent.location.getLongitude()) + and );
    		sbs.append(Constants.LATITUDE + eqe + String.valueOf(StaticContent.location.getLatitude()) + and );
    	}
    	else 
    	{
    		sbs.append(Constants.CITY + eqe + StaticContent.city + and );
    		sbs.append(Constants.STREET + eqe + StaticContent.street + and );
    		sbs.append(Constants.NUMBER + eqe + StaticContent.number + and );
    	}	   	
	//	sbs.append(Constants.IMAGE_DATE + eqe + StaticContent.ImageCaptureDate + and );
	//	sbs.append(Constants.IMAGE_TIME + eqe + StaticContent.ImageCaptureTime + and );  		     	    		
        sbs.toString();      
    }
	
	//
	private String getUserID()
	{
		SharedPreferences sharedPreferences = 
				      getSharedPreferences(Constants.GREEN_PLANET_PREFERENCES, MODE_PRIVATE);
		return sharedPreferences.getString(Constants.USER_ID,Constants.EMPTY);
	}

	//
	private void setUserID(String userId)
	{
		SharedPreferences sharedPreferences = 
		    getSharedPreferences(Constants.GREEN_PLANET_PREFERENCES, MODE_PRIVATE);
	    SharedPreferences.Editor editor = sharedPreferences.edit();    
	    editor.putString(Constants.USER_ID,userId);     	    
	    editor.commit();
	}
    
	//
	private String getGoogleAccount()
	{
		SharedPreferences sharedPreferences = 
				      getSharedPreferences(Constants.GREEN_PLANET_PREFERENCES, MODE_PRIVATE);
		return sharedPreferences.getString(Constants.GOOGLE_ACCOUNT,Constants.EMPTY);
	}
	
	//
	private void sendAll()
    {  
		try    	
		{   
			// באופן זה מתאפשרת לנו התמיכה בעברית שאותה אנו מצרפים (לסטרינג הכתובת של השרת
			String urlReq = new String(sbs.toString().getBytes(), "ISO-8859-1");
			
			URL url = new URL(urlReq);    	    
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			
			// Allow Inputs & Outputs   	    
			connection.setDoInput(true);    	    
			connection.setDoOutput(true);    	    
			connection.setUseCaches(false);
	    
			// Set POST method    	    
			connection.setRequestMethod("POST");    	    
			connection.setRequestProperty("Connection", "Keep-Alive");    	    	    
			connection.setRequestProperty("Content-Type", "multipart/form-data;boundary="+ Constants.BOUNDARY);    	
	    
			//
			DataOutputStream outputStream = new DataOutputStream( connection.getOutputStream() );    	    	    
			outputStream.writeBytes(Constants.TWO_HYPHENS + Constants.BOUNDARY + Constants.LINE_END);    	   	    
			outputStream.writeBytes("Content-Disposition: form-data; name=\"uploadedfile\";filename=\""  
			                                           + StaticContent.Path + "\"" + Constants.LINE_END );    	    
			outputStream.writeBytes(Constants.LINE_END);
			
			// Read file
			FileInputStream fileInputStream = new FileInputStream(new File(StaticContent.Path) );
			int bytesAvailable = fileInputStream.available();    	    	    
			int bufferSize = Math.min(bytesAvailable, Constants.MAX_BUFFER_SIZE);    	    	    
			byte[] buffer = new byte[bufferSize];   	   	     			    	    	    
			int bytesRead = fileInputStream.read(buffer, 0, bufferSize);  	    	   
			while (bytesRead > 0)     	    
			{    	         
				outputStream.write(buffer, 0, bufferSize);    	         
				bytesAvailable = fileInputStream.available();    	         
				bufferSize = Math.min(bytesAvailable, Constants.MAX_BUFFER_SIZE);    	         
				bytesRead = fileInputStream.read(buffer, 0, bufferSize);    	    
			}    	    
			outputStream.writeBytes( Constants.LINE_END );    	    	    
			outputStream.writeBytes(  Constants.TWO_HYPHENS + Constants.BOUNDARY 
					                + Constants.TWO_HYPHENS + Constants.LINE_END );
	    	   
			// Responses from the server (code and message)    	    
			StaticContent.serverResponseCode = connection.getResponseCode();    	    
			StaticContent.serverResponseMessage = connection.getResponseMessage();   		
			
		    // Get Response body      
			InputStream is = connection.getInputStream();      
			BufferedReader rd = new BufferedReader(new InputStreamReader(is));      
			String line;      
			StringBuffer response = new StringBuffer();       
			while((line = rd.readLine()) != null)      
				response.append(line);           			
			StaticContent.serverResponseBody = response.toString();
			if ( (!StaticContent.serverResponseBody.equals(null)) &&
			       (!StaticContent.serverResponseBody.equals(Constants.EMPTY)) )
				setUserID(StaticContent.serverResponseBody);
			StaticContent.uploadServiceEndedSuccessfully = true;	
			
			// need to be in finally ?
			fileInputStream.close();    	    
			outputStream.flush();    	    
			outputStream.close(); 				   	    		
		}    	
		catch (Exception e)    	
		{   
			StaticContent.serverResponseMessage = e.getMessage();
			StaticContent.uploadServiceEndedSuccessfully = false;	    			
		}     		    		
    }
	
	//
	private void notifyMe()
	{  
		NotificationManager mgr = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

		int icon = R.drawable.leaf;
        CharSequence tickerText = "Uplaod Status!";
        long when = System.currentTimeMillis();
		Notification note = new Notification(icon,tickerText,when);
		
        CharSequence contentTitle = "GreenPlanet - UploadService";
        String contentText;
        if (StaticContent.uploadServiceEndedSuccessfully)       
        	contentText = "Had been Successful !";
        else
        	contentText = "Had been Failure !";       
        
        Context context = getApplicationContext(); 
        PendingIntent mPendingIntent = PendingIntent.getActivity(this, 0, new Intent(),0); 
        note.setLatestEventInfo(context, contentTitle, contentText, mPendingIntent); 
                
    //  Intent i = new Intent(this,AddressActivity.class);
    //  i.setFlags(Constants.FLAG_ACTIVITY_NEW_TASK); 
    //  PendingIntent mPendingIntent = PendingIntent.getActivity(this, 0, i,Intent.FLAG_ACTIVITY_NEW_TASK); 
    //  note.setLatestEventInfo(context, contentTitle, contentText, mPendingIntent);
                 	
        // need uses-permission 
        note.vibrate = new long[]{100l,200l,100l,400l};
        mgr.notify(Constants.UPLOAD_SERVICE_NOTIFICATION_ID, note);                    
	}
		
}
