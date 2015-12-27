package com.tectonica.copy.model;

import com.google.api.client.util.GenericData;
import com.google.api.client.util.Key;

public class RequestError extends GenericData
{
	@Key
	public Integer error;
	@Key
	public String message;
}