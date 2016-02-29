package com.insta.annuaire.tests;

import static org.junit.Assert.*;

import org.junit.Test;

import com.insta.annuaire.pojo.LoginResponse;

public class TestLoginResponse {

	@Test
	public void testResult() {
		String result = "false";
		boolean expected = false;
		LoginResponse response = new LoginResponse();
		response.setResult(result);
		assertEquals(expected, response.getResult());
	}

	@Test
	public void testComment() {
		String expected = "commentTest";
		LoginResponse response = new LoginResponse();
		response.setComment(expected);
		assertEquals(expected, response.getComment());
	}
}
