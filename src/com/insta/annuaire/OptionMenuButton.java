package com.insta.annuaire;

import com.insta.annuaire.pojo.Globals;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageView;

public class OptionMenuButton  {

	private Activity activity;
	
	public OptionMenuButton(Activity currentActivity){
		this.activity = currentActivity;
	}
	
	public void setButtonCall(Dialog dialog) {
		Button callButton = (Button)dialog.findViewById(R.id.contact_button_tel);
		callButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent callIntent = new Intent(Intent.ACTION_CALL);
				callIntent.setData(Uri.parse("tel:"+activity.getResources().getString(R.string.contact_button_tel)));
				activity.startActivity(callIntent);
			}
		});
	}

	public void setButtonMail(Dialog dialog) {
		Button sendMailButton = (Button)dialog.findViewById(R.id.contact_button_mail);
		sendMailButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent email = new Intent(Intent.ACTION_SEND);
				email.putExtra(Intent.EXTRA_EMAIL, new String[] {"" + activity.getResources().getString(R.string.contact_button_mail) });
				email.setType("message/rfc822");
				activity.startActivity(Intent.createChooser(email, "Envoyer un email:"));
			}
		});
	}
	
	public void aproposCase(Dialog dialogAPropos) {
		dialogAPropos.setContentView(R.layout.dialog_apropos);
		dialogAPropos.setTitle(R.string.aPropos);	
		dialogAPropos.show();
	}
	public void contactCase(Dialog dialogContact) {
		dialogContact.setContentView(R.layout.dialog_contact);
		dialogContact.setTitle("Contact");	
		dialogContact.show();
		this.setButtonCall(dialogContact);
		this.setButtonMail(dialogContact);
	}
	
	public void listenerPicAdouci(final ImageView idPic, final int imageAdouci,
			final int image){
		idPic.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View arg0, MotionEvent arg1) {
	            if(arg1.getAction() == MotionEvent.ACTION_DOWN){
	            	idPic.setImageResource(imageAdouci);
	            } else if (arg1.getAction() == MotionEvent.ACTION_UP){
	            	idPic.setImageResource(image);
	            	idPic.performClick();
	            } else {
	            	idPic.setImageResource(image);
	            }
	            return true;
	        }
	    });
	}
}
