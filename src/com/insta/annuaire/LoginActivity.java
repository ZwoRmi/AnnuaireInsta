package com.insta.annuaire;

import java.util.Locale;

import com.insta.annuaire.Exceptions.ConnexionException;
import com.insta.annuaire.services.RequestUtil;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

public class LoginActivity extends Activity {
	private String log,pass;
    private EditText login,password;
    private CheckBox saveLoginCheckBox;
    private SharedPreferences loginPreferences;
    private SharedPreferences.Editor loginPrefsEditor;
    private Boolean saveLogin;
    private Button loginButton;
    
	@Override
    protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
		loginButton = (Button)this.findViewById(R.id.submitLogin);
		login = (EditText) this.findViewById(R.id.login);
		password = (EditText) this.findViewById(R.id.password);
		saveLoginCheckBox = (CheckBox)findViewById(R.id.saveLoginCheckBox);
        loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        loginPrefsEditor = loginPreferences.edit();
        saveLogin = loginPreferences.getBoolean("saveLogin", false);
        if (saveLogin == true) {
            login.setText(loginPreferences.getString("username", ""));
            password.setText(loginPreferences.getString("password", ""));
            saveLoginCheckBox.setChecked(true);
        }
		ImageView fbPic = (ImageView) findViewById(R.id.facebookIcon);
		ImageView twittPic = (ImageView) findViewById(R.id.twitterIcon);
		ImageView ytPic = (ImageView) findViewById(R.id.youtubeIcon);
		ImageView gpPic = (ImageView) findViewById(R.id.googleplusIcon);
		
		password.setOnEditorActionListener(new OnEditorActionListener() {        
		    @Override
            public boolean onEditorAction(TextView v, int actionId,
                    KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER))
                        || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    loginButton.performClick();
                }
                return false;
            }
		});
		
		loginButton.setOnClickListener(new View.OnClickListener() {
	        @Override
			 public void onClick(View v) {
                log = login.getText().toString();
                pass = password.getText().toString();
	        	// hide virtual keyboard
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                if (saveLoginCheckBox.isChecked()) {
                    loginPrefsEditor.putBoolean("saveLogin", true);
                    loginPrefsEditor.putString("username", log);
                    loginPrefsEditor.putString("password", pass);
                    loginPrefsEditor.commit();
                } else {
                    loginPrefsEditor.clear();
                    loginPrefsEditor.commit();
                }
                signUp(log,pass);
	        }
		});
		
		this.listenerPic(fbPic,R.drawable.facebookadouci,R.drawable.facebook, "https://fr-fr.facebook.com/pages/CFA-INSTA/116052425081254");
		this.listenerPic(twittPic,R.drawable.twitteradouci,R.drawable.twitter, "https://twitter.com/cfainsta");
		this.listenerPic(ytPic,R.drawable.youtubeadouci,R.drawable.youtube, "https://www.youtube.com/channel/UC8rJ-lN5Giji70nfd8xLjtw");
		this.listenerPic(gpPic,R.drawable.googleplusadouci,R.drawable.googleplus, "https://plus.google.com/104840516292635973069/posts");
    }
	
	public void listenerPic(final ImageView idPic, final int imageAdouci,final int image,final String url){
		OptionMenuButton optionMenuButton = new OptionMenuButton(this);
		idPic.setOnClickListener(new View.OnClickListener() {
	        @Override
			 public void onClick(View v) {
	    		Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
	    		startActivity(browserIntent);
	        }
		});
		optionMenuButton.listenerPicAdouci(idPic,imageAdouci,image);
	}
	
	private void signUp(String login, String password){
		final Intent intent;
		intent = new Intent(this, UsersActivity.class);
		RequestUtil requestUtil = new RequestUtil(getResources().getString(R.string.urlWebService));
		String urlLogin = getResources().getString(R.string.login);
		if(login.matches("") || password.matches(""))
		{
			Toast.makeText(getApplicationContext(), getResources().getString(R.string.saisieIdentifiant), Toast.LENGTH_LONG).show();
		}
		else
		{
			try{
				boolean result = requestUtil.connect(login, password, urlLogin);
				if (result) {
					startActivity(intent);
				}
				else{
					Toast.makeText(getApplicationContext(), getResources().getString(R.string.erreurIdentifiant), Toast.LENGTH_LONG).show();
				}
			}
			catch (ConnexionException ex){
				Toast.makeText(getApplicationContext(),getResources().getString(R.string.erreurConnexion), Toast.LENGTH_LONG).show();
			}
			catch (Exception ex){
				Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_LONG).show();
			}
		}	
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login_option, menu);
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
	        case R.id.parameters:
				final Dialog dialogParameters = new Dialog(this);
				dialogParameters.setContentView(R.layout.dialog_parameters);
				dialogParameters.setTitle(getResources().getString(R.string.change_to_language));	
				dialogParameters.show();
				setButtonChangeLanguage(dialogParameters);
	            return true;	
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}

	private void setButtonChangeLanguage(final Dialog dialogParameters) {
		Button changeLanguageButton = (Button)dialogParameters.findViewById(R.id.changeLanguage);
		changeLanguageButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				setLocale(getResources().getString(R.string.code_change_to_language), dialogParameters);
			}
		});
	}
	
	public void setLocale(String lang, Dialog dialog) { 
	    Locale myLocale = new Locale(lang); 
	    Resources res = getResources(); 
	    DisplayMetrics dm = res.getDisplayMetrics(); 
	    Configuration conf = res.getConfiguration(); 
	    conf.locale = myLocale; 
	    res.updateConfiguration(conf, dm);
	    dialog.dismiss();
	    Intent refresh = new Intent(this, LoginActivity.class); 
	    startActivity(refresh); 
	    this.finish();
	}
}
