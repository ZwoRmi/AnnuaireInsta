package com.insta.annuaire.pojo;

public class LoginResponse {
	private boolean result;
	private String comment;
	
	public boolean getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = Boolean.parseBoolean(result);
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
}
