package com.insta.annuaire;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.insta.annuaire.pojo.AndroidContact;
import com.insta.annuaire.pojo.Globals;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class UserActivity extends Activity {

	private OptionMenuButton optionMenuButton;
	
	public UserActivity(){
		optionMenuButton = new OptionMenuButton(this);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user);
		this.setPicture();
		this.setButtons();
	}
	
	private void setButtons() {
		this.setTextFields();
		this.setButtonMail();
		this.setButtonCall();
		this.setButtonAddContact();
		this.setButtonSms();
	}

	private void setButtonCall() {
		ImageView callButton = (ImageView) findViewById(R.id.call);
		optionMenuButton.listenerPicAdouci(callButton, R.drawable.calladouci, R.drawable.call);
		callButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent callIntent = new Intent(Intent.ACTION_CALL);
				callIntent.setData(Uri.parse("tel:" + Globals.user.getPhone()));
				startActivity(callIntent);
			}
		});
	}

	private void setButtonMail() {
		ImageView sendMailButton = (ImageView) findViewById(R.id.sendMail);
		optionMenuButton.listenerPicAdouci(sendMailButton, R.drawable.send_mailadouci,
				R.drawable.send_mail);
		sendMailButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent email = new Intent(Intent.ACTION_SEND);
				email.putExtra(Intent.EXTRA_EMAIL,
						new String[] { Globals.user.getEmail() });
				email.setType("message/rfc822");
				startActivity(Intent.createChooser(email, "Envoyer un email:"));
			}
		});
	}

	private void setButtonSms() {
		ImageView sendSmsButton = (ImageView) findViewById(R.id.sendSms);
		optionMenuButton.listenerPicAdouci(sendSmsButton, R.drawable.send_smsadouci,
				R.drawable.send_sms);
		sendSmsButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String phoneNumber = Globals.user.getPhone();
				Uri uri = Uri.parse("smsto:" + phoneNumber);
				Intent intentSms = new Intent(Intent.ACTION_SENDTO, uri);
				intentSms.putExtra("compose_mode", true);
				intentSms.putExtra("sms_body", "");
				startActivity(intentSms);
			}
		});
	}

	DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
		@Override
		public void onClick(DialogInterface dialog, int which) {
			switch (which) {
			case DialogInterface.BUTTON_POSITIVE:
				UserActivity.this.AddToContacts();
				break;

			case DialogInterface.BUTTON_NEGATIVE:
				// No button clicked
				break;
			}
		}
	};

	private void setButtonAddContact() {
		ImageView addToContactsButton = (ImageView) findViewById(R.id.addContacts);
		optionMenuButton.listenerPicAdouci(addToContactsButton, R.drawable.add_contactadouci,
				R.drawable.add_contact);
		addToContactsButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				AlertDialog.Builder builder = new AlertDialog.Builder(
						UserActivity.this);
				builder.setMessage("Êtes-vous sûr ?")
						.setPositiveButton("Oui", dialogClickListener)
						.setNegativeButton("Non", dialogClickListener).show();
			}
		});
	}

	private void AddToContacts() {
		AndroidContact contact = new AndroidContact(Globals.user, Globals.picture);   
		if(contact.AddToContacts(this)){
			Toast.makeText(this, "Contact ajouté à votre carnet d'adresse",
					Toast.LENGTH_SHORT).show();
		}
		else
		{
			Toast.makeText(this, "Impossible d'ajouter ce contact à votre carnet d'adresses",
					Toast.LENGTH_SHORT).show();
		}
	}

	private void setPicture() {
		ImageView picture = (ImageView) findViewById(R.id.userPicture);
		picture.setImageBitmap(Globals.picture);
		picture.setClickable(true);
		picture.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				final Dialog dialog = new Dialog(UserActivity.this);
				dialog.setContentView(R.layout.userpicture);
				ImageView image = (ImageView) dialog
						.findViewById(R.id.userPicture);
				image.setImageBitmap(Globals.picture);
				dialog.setTitle(Globals.user.getFirstName() +" "+ Globals.user.getName());
				dialog.getWindow().setLayout(LayoutParams.FILL_PARENT,
						LayoutParams.FILL_PARENT);
				dialog.show();
			}
		});
	}

	private void setTextFields() {
		TextView firstName = (TextView) findViewById(R.id.firstName);
		firstName.setText(Globals.user.getFirstName());
		TextView lastName = (TextView) findViewById(R.id.lastName);
		lastName.setText(Globals.user.getName());
		TextView promoTitle = (TextView) findViewById(R.id.promoTitle);
		promoTitle.setText(Globals.user.getPromoTitle());
		TextView promoNumber = (TextView) findViewById(R.id.promoNumber);
		promoNumber.setText(Globals.user.getPromoNumber());
		TextView email = (TextView) findViewById(R.id.email);
		email.setText(Globals.user.getEmail());
		TextView phone = (TextView) findViewById(R.id.phone);
		phone.setText(this.formatPhoneNumber(Globals.user.getPhone()));
		TextView addressHome = (TextView) findViewById(R.id.addressHome);
		String address = Globals.user.getHomeAddress();
		Pattern regex = Pattern
				.compile("([a-zA-Z_0-9_ ]+)([0-9]{5} [a-zA-Z_0-9_ ]+)");
		Matcher match = regex.matcher(address);
		if (match.find()) {
			addressHome.setText(match.group(1) + "\n" + match.group(2));
		}
	}

	private String formatPhoneNumber(String phone) {
		String result = "";
		for (int i = 0; i < phone.length(); i = i + 2) {
			result = result + phone.substring(i, i + 2);
			if (!(i >= phone.length() - 2))
				result = result + ".";
		}
		return result;
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.user_option, menu);
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
	        	UsersActivity.instance.finish();
	        	return true;
	        case R.id.search:
	        	this.finish();
	        	return true;	
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
}
