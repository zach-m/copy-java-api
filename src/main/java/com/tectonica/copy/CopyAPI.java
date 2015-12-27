package com.tectonica.copy;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import com.google.api.client.auth.oauth.OAuthHmacSigner;
import com.google.api.client.auth.oauth.OAuthParameters;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpHeaders;
import com.google.api.client.http.HttpMediaType;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.HttpUnsuccessfulResponseHandler;
import com.google.api.client.http.InputStreamContent;
import com.google.api.client.http.MultipartContent;
import com.google.api.client.http.MultipartContent.Part;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.JsonObjectParser;
import com.tectonica.copy.model.RequestError;
import com.tectonica.copy.model.Credentials;
import com.tectonica.copy.model.Node;
import com.tectonica.copy.model.Node.Nodes;

public class CopyAPI
{
	private static final String API_ENDPOINT = "https://api.copy.com/rest";

//	private final JsonFactory jsonFactory;
//	private final HttpTransport httpTransport;
	private final HttpRequestFactory requestFactory;

	public CopyAPI(final Credentials creds, final JsonFactory jsonFactory, final HttpTransport httpTransport)
	{
		if (creds == null || jsonFactory == null || httpTransport == null)
			throw new NullPointerException("all parameters are required");

//		this.jsonFactory = jsonFactory;
//		this.httpTransport = httpTransport;

		final OAuthHmacSigner signer = new OAuthHmacSigner();
		signer.clientSharedSecret = creds.consumerSecret;
		signer.tokenSharedSecret = creds.tokenSecret;

		final OAuthParameters parameters = new OAuthParameters();
		parameters.signer = signer;
		parameters.consumerKey = creds.consumerKey;
		parameters.token = creds.token;

		final JsonObjectParser jsonParser = new JsonObjectParser(jsonFactory);
		final HttpUnsuccessfulResponseHandler errorHandler = new HttpUnsuccessfulResponseHandler()
		{
			@Override
			public boolean handleResponse(HttpRequest request, HttpResponse response, boolean supportsRetry) throws IOException
			{
				throw new CopyException(response.parseAs(RequestError.class));
			}
		};

		requestFactory = httpTransport.createRequestFactory(new HttpRequestInitializer()
		{
			@Override
			public void initialize(HttpRequest request) throws IOException
			{
				request.setParser(jsonParser);
				request.getHeaders().set("X-Api-Version", "1").set("Accept", Arrays.asList("application/json"));
				parameters.initialize(request); // sets an interceptor
				request.setUnsuccessfulResponseHandler(errorHandler);
			}
		});
	}

	/**
	 * 
	 * @param path
	 *            either a folder or a file path
	 * @throws IOException
	 */
	public void rm(String path) throws IOException
	{
		GenericUrl url = new GenericUrl(API_ENDPOINT + "/meta/copy" + path);
		HttpRequest request = requestFactory.buildDeleteRequest(url);
		request.execute();
	}

	/**
	 * 
	 * @param fromPath
	 *            either a folder or a file path
	 * @param toPath
	 *            either a folder or a file path, but the same type as 'fromPath'
	 * @throws IOException
	 */
	public void mv(String fromPath, String toPath) throws IOException
	{
		GenericUrl url = new GenericUrl(API_ENDPOINT + "/meta/copy" + fromPath + "?path=" + toPath);
		HttpRequest request = requestFactory.buildPutRequest(url, null);
		request.execute();
	}

	/**
	 * 
	 * @param path
	 *            either a folder or a file path
	 * @param limit
	 *            how many records to return (doesn't affect the returning 'children_count')
	 * @param offset
	 *            zero-based start position
	 * @return
	 * @throws IOException
	 */
	public Node ls(String path, Integer limit, Integer offset) throws IOException
	{
		final String query;
		if (limit != null && offset != null)
			query = String.format("?limit=%d&offset=%d", limit, offset);
		else
			query = "";
		GenericUrl url = new GenericUrl(API_ENDPOINT + "/meta/copy" + path + query);
		HttpRequest request = requestFactory.buildGetRequest(url);
		return request.execute().parseAs(Node.class);
	}

	public Node ls(String path) throws IOException
	{
		return ls(path, null, null);
	}

	public Node mkdirs(String folderPath) throws IOException
	{
		GenericUrl url = new GenericUrl(API_ENDPOINT + "/meta/copy" + folderPath);
		HttpRequest request = requestFactory.buildPostRequest(url, null);
		return request.execute().parseAs(Node.class);
	}

	public Nodes upload(String folderPath, String fileName, String fileContentType, InputStream fileContent) throws IOException
	{
		HttpMediaType mediaType = new HttpMediaType("multipart/form-data").setParameter("boundary", "_____BOUNDARY_____");
		String disposition = String.format("form-data; filename=\"%s\"", fileName);

		Part part = new Part();
		part.setContent(new InputStreamContent(fileContentType, fileContent));
		part.setHeaders(new HttpHeaders().set("Content-Disposition", disposition).set("Content-Transfer-Encoding", "binary"));

		MultipartContent content = new MultipartContent();
		content.setMediaType(mediaType);
		content.setParts(Arrays.asList(part));

		GenericUrl url = new GenericUrl(API_ENDPOINT + "/meta/copy" + folderPath);
		HttpRequest request = requestFactory.buildPostRequest(url, content);
		return request.execute().parseAs(Nodes.class);
	}
}
