package com.insta.annuaire.tests;

import static org.junit.Assert.*;

import org.junit.Test;

import android.graphics.Bitmap;

import com.insta.annuaire.pojo.Globals;
import com.insta.annuaire.pojo.UserResponse;

public class TestGlobals {

	@Test
	public void TestUser() {
		UserResponse user = new UserResponse();
		user.setName("test");
		Globals.user=user;
		assertEquals(user, Globals.user);
	}
	
	@Test
	public void TestPicture() {
		Bitmap picture = null;
		Globals.picture=picture ;
		assertEquals(picture , Globals.picture);
	}
}
