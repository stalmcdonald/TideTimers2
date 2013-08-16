/*
 * 
 * Crystal McDonald
 * Java II
 * 1308
 * Week 2
 * 
 */
package com.cm.tidetimers2;

import com.cm.tidetimers2.R;
import com.cm.tidetimers2.WebFile;
import com.cm.tidetimers2.DataFile;

import java.net.URL;

import java.util.HashMap;
import android.util.Log;

import android.os.Bundle;
import android.os.AsyncTask;

import android.app.Activity;
import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class TideActivity extends Activity implements OnClickListener {
	
	//Create my custom API URL
	//pulling city tide prediction from the wunderground api
	//string reference URL
	static final String baseURL = "http://api.wunderground.com/api/3e64fa36c4f09bdd/tide/q/";
	
	//text view will change for tide text
	 TextView tvCity,tvPrediction, tvWater;
	 EditText etCity;
	 Context _context;
	 HashMap<String, String> _history;
	 Boolean _connected = false;//want to assume not connected
	 
	  /** Called when the activity is first created. */
	        @Override
	   public void onCreate(Bundle tidalcycle) {
	           super.onCreate(tidalcycle);
	           _context = this;
	           _history = getHistory();
	           Log.i("HISTORY READ",_history.toString());

	      		
	           setContentView(R.layout.tide);
	           Button b = (Button)findViewById(R.id.bPrediction);
	           tvCity = (TextView)findViewById(R.id.tvCity);
	           etCity = (EditText)findViewById(R.id.etCity);
	           tvPrediction = (TextView)findViewById(R.id.tvPrediction);
	           tvWater = (TextView)findViewById(R.id.tvWater);
	           
	           //set a button for onclicklistener
	           b.setOnClickListener(new OnClickListener() {
	       		
	        	 //gets text entered in edit text and appends to textview along with data pulled from json
	        		@Override
	        		public void onClick(View v) {
	        			
	        			// getting text edited and appending it to a string
	        			String c = etCity.getText().toString();
	        			String p = etCity.getText().toString();
	        			String w = etCity.getText().toString();
	        			StringBuilder URL = new StringBuilder(baseURL);
	        			
	        			URL.append(c + ".json");
	        			
	        			@SuppressWarnings("unused")
	        			String fullUrl = URL.toString();
	        			try{
	        	    		//finalURL = new URL(baseURL + city + ".json");
	        	    		//finalURL = new URL(baseURL);
	        	    		Log.i("my url:", baseURL + c + ".json");
	        	    		Log.i("City Entered:", c);
	        	    		//LocRequest lr = new LocRequest();
	        	    		tvCity.setText("In " + etCity + " The tide prediction: High");
	        	    		tvPrediction.setText( p + " tide prediction:");
	        	    		
	        	    		//tvCity.setText(fullURL);
	        	    		//lr.execute(fullUrl);
	        	    		
	        	    		//Log.e showing in LogCat, I am assuming it is because my key is bad again, not sure why since I regenerated the key.	
	        	    	} finally //(MalformedURLException e){
	        	    	{
	        	    		Log.e("BAD URL", "MALFORMED URL");
	        	    		tvCity.setText("error");
	        	    		tvPrediction.setText( p + " Tide Prediction: UNKNOWN");
	        	    		tvWater.setText(w + ": Puget Sound");
	        	    		etCity.setText(URL);
	        	    		//return "In " + etCity + " The tide prediction: High";
	        	    		//URL = null;
	        	    	}
	        			
	        	    }
	           });
	       			
	       			
	   }
	   
 		
	   //@Override
   	public void execute(String result){
   		Log.d("URL RESPONSE",result);
   		//Still working on Parsing Data, decided to go back to AsyncTask 
   		
   		try{
   		//parsing through JSON Data accepts a string as a parameter
   		JSONObject json = new JSONObject(result);
   		JSONObject results = json.getJSONObject("tideInfo.tideSite");//.getJSONObject("tideSite");
   		if(results.getString("tideInfo").compareTo("type")==1){
   			Toast toast = Toast.makeText(_context, "Invalid City Entered ", Toast.LENGTH_LONG);
   			toast.show();
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
   		
	
	public String dataToString(){
		return "In " + etCity + " The tide prediction: High";
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
    
    @SuppressWarnings("unused")
	private class LocRequest extends AsyncTask<URL,Void,String>{
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
    	
    }
    
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.tide, container, false);
    }
    
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        
        view = view.findViewWithTag(R.id.class);
    }


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		//Detects the network connection
  		_connected = WebFile.getConnectionStatus(_context);
  		if(_connected){
  			Log.i("NETWORK CONNECTION ", WebFile.getConnnectionType(_context));
  		}
	}
	
  
}//end activity
