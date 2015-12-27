package com.tectonica.copy.oauth;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Logger;

import com.google.api.client.auth.oauth.OAuthCallbackUrl;
import com.google.api.client.auth.oauth.OAuthCredentialsResponse;
import com.google.api.client.auth.oauth.OAuthGetAccessToken;
import com.google.api.client.auth.oauth.OAuthGetTemporaryToken;
import com.google.api.client.auth.oauth.OAuthHmacSigner;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.util.escape.CharEscapers;
import com.tectonica.copy.model.Credentials;
import com.tectonica.copy.model.Scope;

public class Authorizer
{
	private static final String REQUEST_ENDPOINT = "https://api.copy.com/oauth/request";
	private static final String AUTHORIZE_ENDPOINT = "https://www.copy.com/applications/authorize";
	private static final String ACCESS_ENDPOINT = "https://api.copy.com/oauth/access";

	private static Logger logger = Logger.getLogger(Authorizer.class.getName());

	private final CallbackServer callbackServer;
	private final JsonFactory jsonFactory;
	private final HttpTransport httpTransport;

	public Authorizer(CallbackServer callbackServer, JsonFactory jsonFactory, HttpTransport httpTransport)
	{
		if (callbackServer == null || jsonFactory == null || httpTransport == null)
			throw new NullPointerException("all parameters are required");
		this.callbackServer = callbackServer;
		this.jsonFactory = jsonFactory;
		this.httpTransport = httpTransport;
	}

	public Credentials authorize(final String consumerKey, final String consumerSecret, final Scope scope) throws IOException
	{
		final String scopeJson = jsonFactory.toString(scope);
		logger.fine("authorize: consumerKey =" + consumerKey);
		logger.fine("authorize: scope =" + scopeJson);

		// /////////////////////////////////////////////////////////////////////////////////////////////
		// Step 1: ask for a Request-Token
		// /////////////////////////////////////////////////////////////////////////////////////////////

		OAuthHmacSigner signer = new OAuthHmacSigner();
		signer.clientSharedSecret = consumerSecret;
		OAuthGetTemporaryToken req1 = new OAuthGetTemporaryToken(REQUEST_ENDPOINT + "?scope=" + CharEscapers.escapeUri(scopeJson));
		req1.transport = httpTransport;
		req1.signer = signer;
		req1.consumerKey = consumerKey;
		req1.callback = callbackServer.getUrl();
		OAuthCredentialsResponse requestToken = req1.execute();
		logger.info("Request-Token: " + jsonFactory.toString(requestToken));

		// /////////////////////////////////////////////////////////////////////////////////////////////
		// Step 2: ask the user to authorize this application
		// /////////////////////////////////////////////////////////////////////////////////////////////

		// launch a browser and wait for it to perform callback
		try
		{
			URI uri = new URI(AUTHORIZE_ENDPOINT + "?oauth_token=" + requestToken.token);
			Desktop.getDesktop().browse(uri);
		}
		catch (URISyntaxException e)
		{
			throw new RuntimeException(e);
		}

		// wait for the callback server to pick up the notification of the user authorization
		OAuthCallbackUrl callbackUrl = callbackServer.waitForCallback();
		logger.info("Callback: " + jsonFactory.toString(callbackUrl));

		// /////////////////////////////////////////////////////////////////////////////////////////////
		// Step 3: ask for Access-Token
		// /////////////////////////////////////////////////////////////////////////////////////////////

		signer.tokenSharedSecret = requestToken.tokenSecret; // in addition to the consumer secret
		OAuthGetAccessToken req2 = new OAuthGetAccessToken(ACCESS_ENDPOINT);
		req2.transport = httpTransport;
		req2.signer = signer;
		req2.consumerKey = consumerKey;
		req2.temporaryToken = callbackUrl.token;
		req2.verifier = callbackUrl.verifier;
		OAuthCredentialsResponse accessToken = req2.execute();
		logger.info("Access-Token: " + jsonFactory.toString(accessToken));

		return new Credentials(consumerKey, consumerSecret, accessToken.token, accessToken.tokenSecret);
	}
}
