package com.tectonica.copy.oauth;

import java.io.IOException;

import com.google.api.client.auth.oauth.OAuthCallbackUrl;

public interface CallbackServer
{
	String getUrl();

	/**
	 * NOTE: only the token and verifier are required in the return value
	 */
	OAuthCallbackUrl waitForCallback() throws IOException;
}
