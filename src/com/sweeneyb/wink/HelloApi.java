package com.sweeneyb.wink;

import java.io.IOException;
import java.util.Properties;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.google.gson.Gson;

public class HelloApi {

	public static String main = "https://winkapi.quirky.com";

	public static void main(String[] args) throws ClientProtocolException,
			IOException {
		Gson gson = new Gson();
		String server = main;
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost post = new HttpPost(server + "/oauth2/token");
		post.addHeader("Content-Type", "application/json");
		System.out.println(gson.toJson(new Auth()));
		post.setEntity(new StringEntity(gson.toJson(new Auth())));
		HttpResponse response;
		String temp;
		response = httpclient.execute(post);
		temp = EntityUtils.toString(response.getEntity());
		System.out.println(temp);
		Token token = gson.fromJson(temp, Data.class).data;
		System.out.println(token.access_token);

		HttpGet get = new HttpGet(server + "/users/me/wink_devices");

		get.addHeader("Authorization", "Bearer " + token.access_token);

		response = httpclient.execute(get);
		temp = EntityUtils.toString(response.getEntity());
		// System.out.println(temp);
		// System.out.println(response.getStatusLine());

		// put = new HttpPut(server+"/")
		get = new HttpGet(server + "/light_bulbs/158385");
		get.addHeader("Authorization", "Bearer " + token.access_token);
		response = httpclient.execute(get);
		temp = EntityUtils.toString(response.getEntity());
		// System.out.println(temp);
		// System.out.println(response.getStatusLine());

		HttpPut put = new HttpPut(server + "/light_bulbs/158385");
		put.addHeader("Authorization", "Bearer " + token.access_token);
		put.addHeader("Content-Type", "application/json");
		String json = gson.toJson(new Adjust());
		System.out.println(json);
		StringEntity se = new StringEntity(json);
		// System.out.println("entity"+se.);
		put.setEntity(se);
		response = httpclient.execute(put);
		temp = EntityUtils.toString(response.getEntity());
		System.out.println(temp);
		System.out.println(response.getStatusLine());

	}

	static class Adjust {
		static class Desired_state {
			boolean powered = true;
			float brightness = .5f;
		}

		Desired_state desired_state = new Desired_state();
	}

	static class Auth {
		transient Properties props = new Properties();
		public String client_id;
		public String client_secret;
		public String username;
		public String password;
		public String grant_type = "password";

		public Auth() throws IOException {
			props.load(ClassLoader
					.getSystemResourceAsStream("secrets.properties"));
			client_id = props.getProperty("client_id");
			client_secret = props.getProperty("client_secret");
			username = props.getProperty("username");
			;
			password = props.getProperty("password");
			;
		}

	}

	static class Data {
		Token data;
	}

	static class Token {
		public String access_token;
		public String refresh_token;
		public String token_type;
	}

}
