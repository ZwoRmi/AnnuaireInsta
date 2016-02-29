package com.insta.annuaire.tests;

import org.junit.Test;
import com.insta.annuaire.pojo.UserResponse;
import junit.framework.TestCase;

public class TestUserResponse extends TestCase {
	
	@Test
	public void testName() throws Exception {
		final String expected = "nameTest";
		UserResponse user = new UserResponse();
		user.setName(expected);
		assertEquals(expected, user.getName());
	}
	
	@Test
	public void testFirstName() throws Exception {
		final String expected = "firstNameTest";
		UserResponse user = new UserResponse();
		user.setFirstName(expected);
		assertEquals(expected, user.getFirstName());
	}
	
	@Test
	public void testId() throws Exception {
		final int expected = 5;
		UserResponse user = new UserResponse();
		user.setId(expected);
		assertEquals(expected, user.getId());
	}
	
	@Test
	public void testHomeAddress() throws Exception {
		final String expected = "firstNameTest";
		UserResponse user = new UserResponse();
		user.setHomeAddress(expected);
		assertEquals(expected, user.getHomeAddress());
	}
	
	@Test
	public void testEmail() throws Exception {
		final String expected = "firstNameTest";
		UserResponse user = new UserResponse();
		user.setEmail(expected);
		assertEquals(expected, user.getEmail());
	}
	
	@Test
	public void testPhone() throws Exception {
		final String expected = "0123456789";
		UserResponse user = new UserResponse();
		user.setPhone(expected);
		assertEquals(expected, user.getPhone());
	}
	
	@Test
	public void testPicture() throws Exception {
		final String expected = "http://urlpicture.com";
		UserResponse user = new UserResponse();
		user.setPicture(expected);
		assertEquals(expected, user.getPicture());
	}
	
	@Test
	public void testPromoNumber() throws Exception {
		final String expected = "PromoNumbertest";
		UserResponse user = new UserResponse();
		user.setPromoNumber(expected);
		assertEquals(expected, user.getPromoNumber());
	}
	
	@Test
	public void testPromoTitle() throws Exception {
		final String expected = "PromoNumbertest";
		UserResponse user = new UserResponse();
		user.setPromoTitle(expected);
		assertEquals(expected, user.getPromoTitle());
	}
	
	@Test
	public void testCompareToFirst() throws Exception {
		String firstEntry = "First";
		String secondEntry = "Second";
		int expected = firstEntry.compareTo(secondEntry);
		UserResponse user1 = new UserResponse();
		user1.setName(firstEntry);
		UserResponse user2 = new UserResponse();
		user2.setName(secondEntry);
		assertEquals(expected, user1.compareTo(user2));
	}
	
	@Test
	public void testCompareToSecond() throws Exception {
		String firstEntry = "First";
		String secondEntry = "First";
		int expected = firstEntry.compareTo(secondEntry);
		UserResponse user1 = new UserResponse();
		user1.setName(firstEntry);
		UserResponse user2 = new UserResponse();
		user2.setName(secondEntry);
		assertEquals(expected, user1.compareTo(user2));
	}
}
