package com.insta.annuaire.pojo;

public class UserResponse implements Comparable<UserResponse> {
	private int id;
	private String name;
	private String picture;
	private String firstName;
	private String promoTitle;
	private String promoNumber;
	private String homeAddress;
	private String phone;
	private String email;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPicture() {
		return picture;
	}
	public void setPicture(String picture) {
		this.picture = picture;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getPromoTitle() {
		return promoTitle;
	}
	public void setPromoTitle(String promoTitle) {
		this.promoTitle = promoTitle;
	}
	public String getPromoNumber() {
		return promoNumber;
	}
	public void setPromoNumber(String promoNumber) {
		this.promoNumber = promoNumber;
	}
	public String getHomeAddress() {
		return homeAddress;
	}
	public void setHomeAddress(String homeAddress) {
		this.homeAddress = homeAddress;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	@Override
	public int compareTo(UserResponse another) {
		return this.getName().compareTo(another.getName());
	}
	
}
