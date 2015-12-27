package com.tectonica.copy.oauth;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.api.client.auth.oauth.OAuthCallbackUrl;

public class LocalCallbackServer implements CallbackServer
{
	private static Logger logger = Logger.getLogger(LocalCallbackServer.class.getName());
	private static final Pattern HTTP_GET_HEADER = Pattern.compile("GET ([^ ]*) HTTP/1.1");

	private final int port;
	private final String callbackServerUrl;

	public LocalCallbackServer(int port)
	{
		this.port = port;
		callbackServerUrl = "http://localhost:" + port;
	}

	@Override
	public String getUrl()
	{
		return callbackServerUrl;
	}

	@Override
	public OAuthCallbackUrl waitForCallback() throws IOException
	{
		final OAuthCallbackUrl callbackUrl;
		try (ServerSocket server = new ServerSocket(port))
		{
			logger.info("Waiting on port " + port + "..");

			Socket client = server.accept();
			InputStreamReader isr = new InputStreamReader(client.getInputStream());
			BufferedReader reader = new BufferedReader(isr);
			String line = reader.readLine();
			logger.fine("First line in callback HTTP request: " + line);

			Matcher matcher = HTTP_GET_HEADER.matcher(line);
			if (matcher.matches())
				callbackUrl = new OAuthCallbackUrl(callbackServerUrl + matcher.group(1));
			else
				throw new RuntimeException("The request made to the local server is not an HTTP GET");

			String msg = "Callback:\n" + ("token=" + callbackUrl.token) + "\n" + ("verifier=" + callbackUrl.verifier);
			String httpResponse = "HTTP/1.1 200 OK\nContent-type: text/plain\n\n" + msg;
			try (OutputStream outputStream = client.getOutputStream())
			{
				outputStream.write(httpResponse.getBytes("UTF-8"));
			}
			logger.info(msg);
		}
		return callbackUrl;
	}

}
