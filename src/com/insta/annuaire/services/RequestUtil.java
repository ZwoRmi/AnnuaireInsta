package com.insta.annuaire.services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.*;
import org.apache.http.client.methods.*;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.insta.annuaire.Exceptions.ConnexionException;
import com.insta.annuaire.Exceptions.JsonResponseFailed;
import com.insta.annuaire.pojo.LoginResponse;
import com.insta.annuaire.pojo.UserResponse;

public class RequestUtil {
	private String url;

	// To avoid new
	public RequestUtil(String url) {
		this.setUrl(url);
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public boolean connect(String login, String password, String urlLogin) throws ConnexionException {
		String strResponse;
		strResponse = this.requestContent(this.url + urlLogin + login + "/"
				+ password);

		LoginResponse loginResponse = this.getLoginResponse(strResponse);
		return loginResponse.getResult();
	}

	private LoginResponse getLoginResponse(String response) {
		LoginResponse result = null;
		try {

			JSONObject json = new JSONObject(response);
			result = new LoginResponse();
			result.setResult(json.getString("success"));
			result.setComment(json.getString("message"));
		} catch (JSONException e) {
			// manage exceptions
		}
		return result;
	}
	
	public ArrayList<UserResponse> getAllUsers() throws JsonResponseFailed, ConnexionException{
		String strResponseJSon;
		strResponseJSon = this.requestContent(this.url + "android_getAllUser");
		return this.getAllUserResponse(strResponseJSon);
	}
	

	private ArrayList<UserResponse> getAllUserResponse(String response)
			throws JsonResponseFailed {
		ArrayList<UserResponse> result = null;
		try {

			JSONObject json = new JSONObject(response);
			if (!json.getBoolean("success")) {
				throw new JsonResponseFailed(
						"Impossible de récupérer les données");
			}
			result = this.getUsers(response, true);
		} catch (JSONException e) {
			// manage exceptions
		}
		return result;
	}

	private ArrayList<UserResponse> getUsers(String response,
			Boolean withDetails) throws JSONException {
		JSONObject outerObject = new JSONObject(response);
		JSONArray jsonArray = outerObject.getJSONArray("data");
		ArrayList<UserResponse> result = new ArrayList<UserResponse>();
		for (int i = 0, size = jsonArray.length(); i < size; i++) {
			JSONObject objectInArray = jsonArray.getJSONObject(i);
			if (withDetails) {
				result.add(getUserWithDetails(objectInArray));
			} else {
				result.add(getUser(objectInArray));
			}
		}
		return result;
	}

	private UserResponse getUser(JSONObject jsonObjectUser) throws JSONException {
		UserResponse result = new UserResponse();
		result.setId(jsonObjectUser.getInt("id"));
		result.setPicture(jsonObjectUser.getString("picture"));
		result.setFirstName(jsonObjectUser.getString("firstName"));
		result.setName(jsonObjectUser.getString("lastName"));
		result.setPromoNumber(jsonObjectUser.getString("promoNumber"));
		result.setPromoTitle(jsonObjectUser.getString("promoTitle"));
		return result;
	}

	private UserResponse getUserWithDetails(JSONObject jsonObjectUser) throws JSONException {
		UserResponse result = getUser(jsonObjectUser);
		result.setEmail(jsonObjectUser.getString("email"));
		result.setHomeAddress(jsonObjectUser.getString("homeAddress"));
		result.setPhone(jsonObjectUser.getString("phone"));
		return result;
	}

	public String requestContent(String url) throws ConnexionException {
		HttpClient httpclient = new DefaultHttpClient();
		String result = null;
		HttpGet httpget = new HttpGet(url);
		HttpResponse response = null;
		InputStream instream = null;

		try {
			response = httpclient.execute(httpget);
			HttpEntity entity = response.getEntity();

			if (entity != null) {
				instream = entity.getContent();
				result = convertStreamToString(instream);
			}

		} 
		catch(HttpHostConnectException h){
			throw new ConnexionException("Erreur de connexion au réseau");
		}
		catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (instream != null) {
				try {
					instream.close();
				} catch (Exception exc) {

				}
			}
		}

		return result;
	}

	public String convertStreamToString(InputStream is) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();
		String line = null;

		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (IOException e) {
		} finally {
			try {
				is.close();
			} catch (IOException e) {
			}
		}

		return sb.toString();
	}
}
