package com.insta.annuaire.pojo;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import android.app.Activity;
import android.content.ContentProviderOperation;
import android.content.Context;
import android.graphics.Bitmap;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Data;

public class AndroidContact {
	private UserResponse user;
	private Bitmap picture;

	public AndroidContact(UserResponse user, Bitmap picture) {
		this.user = user;
		this.picture = picture;
	};

	public boolean AddToContacts(Activity context) {
		try {
			context.getContentResolver().applyBatch(ContactsContract.AUTHORITY,
					this.getContentProviderOperation());
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	private ArrayList<ContentProviderOperation> getContentProviderOperation() {
		ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();

		ops.add(ContentProviderOperation
				.newInsert(ContactsContract.RawContacts.CONTENT_URI)
				.withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
				.withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
				.build());

		ops.add(getNameInfo());
		ops.add(getMobileNumberInfo());
		ops.add(getEmailInfo());
		ops.add(getCompanyInfo());
		ops.add(getPicture());

		return ops;
	}

	private ContentProviderOperation getNameInfo() {
		String displayName = this.user.getName() + " "
				+ this.user.getFirstName();
		return ContentProviderOperation
				.newInsert(ContactsContract.Data.CONTENT_URI)
				.withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
				.withValue(
						ContactsContract.Data.MIMETYPE,
						ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
				.withValue(
						ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME,
						displayName).build();
	}

	private ContentProviderOperation getMobileNumberInfo() {
		String mobileNumber = this.user.getPhone();
		return ContentProviderOperation
				.newInsert(ContactsContract.Data.CONTENT_URI)
				.withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
				.withValue(
						ContactsContract.Data.MIMETYPE,
						ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
				.withValue(ContactsContract.CommonDataKinds.Phone.NUMBER,
						mobileNumber)
				.withValue(ContactsContract.CommonDataKinds.Phone.TYPE,
						ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
				.build();
	}

	private ContentProviderOperation getEmailInfo() {
		String email = this.user.getEmail();
		return ContentProviderOperation
				.newInsert(ContactsContract.Data.CONTENT_URI)
				.withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
				.withValue(
						ContactsContract.Data.MIMETYPE,
						ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)
				.withValue(ContactsContract.CommonDataKinds.Email.DATA, email)
				.withValue(ContactsContract.CommonDataKinds.Email.TYPE,
						ContactsContract.CommonDataKinds.Email.TYPE_WORK)
				.build();
	}

	private ContentProviderOperation getCompanyInfo() {
		String company = "Insta Promo " + this.user.getPromoNumber();
		String jobTitle = this.user.getPromoTitle();
		return ContentProviderOperation
				.newInsert(ContactsContract.Data.CONTENT_URI)
				.withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
				.withValue(
						ContactsContract.Data.MIMETYPE,
						ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE)
				.withValue(
						ContactsContract.CommonDataKinds.Organization.COMPANY,
						company)
				.withValue(ContactsContract.CommonDataKinds.Organization.TYPE,
						ContactsContract.CommonDataKinds.Organization.TYPE_WORK)
				.withValue(ContactsContract.CommonDataKinds.Organization.TITLE,
						jobTitle)
				.withValue(ContactsContract.CommonDataKinds.Organization.TYPE,
						ContactsContract.CommonDataKinds.Organization.TYPE_WORK)
				.build();
	}

	private ContentProviderOperation getPicture() {
		Bitmap image = this.picture;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		return ContentProviderOperation
				.newInsert(Data.CONTENT_URI)
				.withValueBackReference(Data.RAW_CONTACT_ID, 0)
				.withValue(
						ContactsContract.Data.MIMETYPE,
						ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE)
				.withValue(ContactsContract.CommonDataKinds.Photo.PHOTO,
						baos.toByteArray()).build();
	}

}
