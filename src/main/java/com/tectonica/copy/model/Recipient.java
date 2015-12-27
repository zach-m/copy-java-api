package com.tectonica.copy.model;

import java.util.List;

import com.google.api.client.util.GenericData;
import com.google.api.client.util.Key;

public class Recipient extends GenericData
{
	public static class Email extends GenericData
	{
		@Key
		public Boolean primary; // ": true,
		@Key
		public Boolean confirmed; // ": true,
		@Key
		public String email; // ": "user@test.com",
		@Key
		public String gravatar; // ": "eca957c6552e783627a0ced1035e1888"
	}

	@Key
	public String user_id; // ": "1381231",
	@Key
	public String first_name; // ": "Test",
	@Key
	public String last_name; // ": "User",
	@Key
	public String contact_type; // ": "user", // TODO: ENUM
	@Key
	public String contact_id; // ": "user-1381231",
	@Key
	public String contact_source; // ": "link-3514165",
	@Key
	public String permissions; // ": "sync", // TODO: ENUM
	@Key
	public String email; // ": "user@test.com",
	@Key
	public List<Email> emails;
}