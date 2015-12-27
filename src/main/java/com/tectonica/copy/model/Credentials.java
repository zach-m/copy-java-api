package com.tectonica.copy.model;

import com.google.api.client.util.Key;

public class Credentials
{
	@Key
	public String consumerKey;
	@Key
	public String consumerSecret;
	@Key
	public String token;
	@Key
	public String tokenSecret;

	public Credentials()
	{}

	public Credentials(String consumerKey, String consumerSecret, String token, String tokenSecret)
	{
		this.consumerKey = consumerKey;
		this.consumerSecret = consumerSecret;
		this.token = token;
		this.tokenSecret = tokenSecret;
	}
}
