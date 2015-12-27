package com.tectonica.copy;

import com.tectonica.copy.model.RequestError;

@SuppressWarnings("serial")
public class CopyException extends RuntimeException
{
	final public Integer errorCode;

	public CopyException(RequestError error)
	{
		super(error.message);
		this.errorCode = error.error;
	}
}