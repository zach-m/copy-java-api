package com.tectonica.copy.util;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.JsonGenerator;
import com.tectonica.copy.model.Credentials;

public class CredentialsStore
{
	private final String fileName = System.getProperty("user.home") + "/.copy-credentials";
	private final JsonFactory factory;

	public CredentialsStore(JsonFactory factory)
	{
		this.factory = factory;
	}

	public Credentials readCredentials() throws IOException
	{
		try (final FileReader fr = new FileReader(fileName))
		{
			return factory.createJsonParser(fr).parseAndClose(Credentials.class);
		}
		catch (FileNotFoundException e)
		{
			return null;
		}
	}

	public void saveCredentials(Credentials creds) throws IOException
	{
		try (PrintWriter pw = new PrintWriter(fileName))
		{
			final JsonGenerator gen = factory.createJsonGenerator(pw);
			gen.serialize(creds);
			gen.flush();
		}
	}
}
