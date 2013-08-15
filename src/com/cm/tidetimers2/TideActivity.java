package com.cm.tidetimers2;

import java.net.MalformedURLException;
import java.util.HashMap;

import com.cm.tidetimers2.MainActivity.LocRequest;
import com.cm.tidetimers2.DataFile;
import com.cm.tidetimers2.WebFile;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class TideActivity extends Activity implements OnClickListener {
	//Create my custom API URL
	//pulling city and state and temp from the google api
	//string reference URL
	static final String baseURL = "http://api.wunderground.com/api/3e64fa36c4f09bdd/tide/q/";
	//text view will change for weather text
	 TextView tvCity;
	 EditText etCity;
	 HashMap<String, String> _history;
	 
	  /** Called when the activity is first created. */
	   @Override
	   public void onCreate(Bundle icicle) {
	           super.onCreate(icicle);
	           
	           setContentView(R.layout.tide);
	           Button b = (Button)findViewById(R.id.bPrediction);
	           tvCity = (TextView)findViewById(R.id.tvCity);
	           etCity = (EditText)findViewById(R.id.etCity);
	           
	           //set a button for onclicklistener
	           b.setOnClickListener(this);
	  
	   }
	@Override
	public void onClick(View v) {
		// getting text edited and appending it to a string
		String c = etCity.getText().toString();
		StringBuilder URL = new StringBuilder(baseURL);
		
		URL.append(c + ".json");
		
		String fullUrl = URL.toString();
		try{
    		//finalURL = new URL(baseURL + city + ".json");
    		//finalURL = new URL(baseURL);
    		Log.i("my url:", baseURL + c + ".json");
    		//LocRequest lr = new LocRequest();
    		tvCity.setText(fullUrl);
    		//tvCity.setText(URL);
    		//lr.execute(fullUrl);
    		
    		
    	} finally //(MalformedURLException e){
    	{
    		Log.e("BAD URL", "MALFORMED URL");
    		tvCity.setText("error");
    		etCity.setText(URL);
    		URL = null;
    	}
    }
		
	
	 
}
