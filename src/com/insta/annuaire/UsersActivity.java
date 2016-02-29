package com.insta.annuaire;

import java.util.Timer;
import java.util.TimerTask;

import com.insta.annuaire.Exceptions.ConnexionException;
import com.insta.annuaire.Exceptions.JsonResponseFailed;
import com.insta.annuaire.services.RequestUtil;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class UsersActivity extends Activity {
	static UsersActivity instance; 
	RequestUtil requestUtil;
	UsersAdapterFilterable adapter;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		instance = this;
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_users);
		super.onStart();
		ResearchInListView();
		ListView listView = (ListView) findViewById(R.id.ListViewUsers);
		requestUtil = new RequestUtil(getResources().getString(
				R.string.urlWebService));
		try {
			adapter = new UsersAdapterFilterable(this,
					requestUtil.getAllUsers());
			listView.setAdapter(adapter);
		} catch (JsonResponseFailed e) {
			e.printStackTrace();
		}
		 catch (ConnexionException e) {
			 Toast.makeText(getApplicationContext(), "Aucune connexion", Toast.LENGTH_LONG).show();
			}
	}
	
	
	private void ResearchInListView(){
		EditText editTextSearch = (EditText) findViewById(R.id.searchEditText);
		editTextSearch.addTextChangedListener(new TextWatcher() {
			private Timer timer = new Timer();
			private final long DELAY = 500; // in ms
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (count < before) {
					// We're deleting char so we need to reset the adapter data
					adapter.resetData();
				}
			}
			@Override
			public void afterTextChanged(final Editable s) {
				timer.cancel();
				timer = new Timer();
				timer.schedule(new TimerTask() {
					@Override
					public void run() {
						adapter.getFilter().filter(s.toString());
					}
				}, DELAY);
			}
		});
	}	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.listview_option, menu);
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		OptionMenuButton optionMenuButton = new OptionMenuButton(this);
	    switch (item.getItemId()) {
	        case R.id.aPropos:
				final Dialog dialogAPropos = new Dialog(this);
				optionMenuButton.aproposCase(dialogAPropos);
	            return true;	
	        case R.id.contact:
				final Dialog dialogContact = new Dialog(this);
				optionMenuButton.contactCase(dialogContact);
	            return true;
	        case R.id.disconnect:
	        	this.finish();
	        	return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
}