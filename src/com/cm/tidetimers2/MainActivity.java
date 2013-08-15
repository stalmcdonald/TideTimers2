/*
 * Crystal McDonald
 * Java II
 * 1308
 * Week 2
 * Tidal Prediction Application
 * 
 */
package com.cm.tidetimers2;


import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import com.cm.tidetimers2.DataFile;
import com.cm.tidetimers2.WebFile;


import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;

import android.util.Log;
import android.view.Menu;
import android.view.View.OnClickListener;

import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	Context _context;
	
	//text view will change for tidal prediction
	TextView tv;
	EditText city;
	HashMap<String, String> _history;
    
	 
	/** Called when the activity is first created. */
	   @Override
	   public void onCreate(Bundle icicle) {
	           super.onCreate(icicle);
	           
	           setContentView(R.layout.activity_main);
	           Button b = (Button)findViewById(R.id.predict);
	           tv = (TextView)findViewById(R.id.curPre);
	           city = (EditText)findViewById(R.id.city);
	           //set a button for onclicklistener
	           b.setOnClickListener((OnClickListener) this);
	  
	   }
	   
	   /*  STILL NOT SURE WHY MY NEW KEY I GENERATED STILL WONT PULL DATA. */
	   
	 //pulling city tide prediction from the wunderground api
	 //string reference URL
	 //JSON Path Example: http://api.wunderground.com/api/d4509f6df6f598a0/tide/q/Seattle.json

	   //Create my custom API URL
	     private void getfaveWatLoc(String city){
	     	Log.i("CLICK",city);
	     	//JSON output.  tide by city.
	     	String baseURL = "http://api.wunderground.com/api/3e64fa36c4f09bdd/tide/q/ "+ city +".json";
	     			
	     	URL finalURL;
	     	try{
	     		finalURL = new URL(baseURL + city + ".json");
	     		//finalURL = new URL(baseURL);
	     		Log.i("my url:", baseURL);
	     		LocRequest lr = new LocRequest();
	     		lr.execute(finalURL);
	     		
	     	} catch (MalformedURLException e){
	     		Log.e("BAD URL", "MALFORMED URL");
	     		finalURL = null;
	     	}
	     }
	     
	   //create method to get history from Hard drive
	     @SuppressWarnings("unchecked")
	 	private HashMap<String, String> getHistory(){
	     	Object stored = DataFile.readObjectFile(_context, "history", false);
	     	
	     	HashMap<String, String> history;
	     	if(stored == null){
	     		Log.i("HISTORY", "NO HISTORY FILE FOUND");
	     		history = new HashMap<String, String>();
	     	}else{
	     		history = (HashMap<String, String>)stored;
	     	}
	     	return history;
	     }
	     
	     public class LocRequest extends AsyncTask<URL,Void,String>{
	     	//override 2 separate functions
	     	@Override
	     	protected String doInBackground(URL...urls){
	     		String response = "";
	     		//pass an array even though it only holds one
	     		for(URL url: urls){
	     			response = WebFile.getURLSTringResponse(url);
	     		}
	     		return response;
	     	}
	     	
	     	@Override
	     	protected void onPostExecute(String result){
	     		Log.d("URL RESPONSE",result);
	     		
	     		try{
	     		//parsing through JSON Data   accepts a string as a parameter
	     		JSONObject json = new JSONObject(result);
	     		JSONObject results = json.getJSONObject("tideInfo.tideSite");//.getJSONObject("tideSite");
	     		if(results.getString("tideInfo").compareTo("type")==1){
//	     			Toast toast = Toast.makeText(_context, "Invalid City Entered ", Toast.LENGTH_LONG);
//	     			toast.show();
	     		}else{
	     			Toast toast = Toast.makeText(_context, "Tide Info" + results.get("tideInfo"), Toast.LENGTH_LONG);
	     			toast.show();
	     			
	     			//makes sure history is there
	     			_history.put(results.getString("string"), results.toString());
	     			//target file to write history to harddrive
	     			DataFile.storeObjectFile(_context, "tide", _history, false);
	     			DataFile.storeStringFile(_context, "tide", results.toString(), true);
	     		}
	     		} 
	     		catch (JSONException e){
	     			Log.e("JSON", "JSON OBJECT EXCEPTION " + e.toString());
	     		}
	     	}
	     	
	     }

	 

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

}
