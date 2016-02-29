package com.insta.annuaire.tests;

import static org.junit.Assert.*;

import org.junit.Test;

import com.insta.annuaire.Exceptions.JsonResponseFailed;

public class TestJsonResponseFailed {

	@Test
	public void testMessage() {
		String expected = "testmessage";
		JsonResponseFailed ex = new JsonResponseFailed(expected);
		assertEquals(expected, ex.getMessage());
	}

}
