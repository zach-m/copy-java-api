package com.tectonica.copy.model;

import java.util.List;

import com.google.api.client.util.GenericData;
import com.google.api.client.util.Key;

public class Link extends GenericData
{
	@Key
	public String id; // ": "hPTBeqqN9Bg9",
	@Key("public")
	public Boolean is_public; // ": true,
	@Key
	public Boolean expires; // ": false,
	@Key
	public Boolean expired; // ": false,
	@Key
	public String url; // ": "https://copy.com/hPTBeqqN9Bg9/Applications",
	@Key
	public String url_short; // ": "https://copy.com/hPTBeqqN9Bg9",
	@Key
	public List<Recipient> recipients; // ": [],
	@Key
	public String creator_id; // ": "1381231",
	@Key
	public Long created_time; // ": 1366825349,
	@Key
	public Integer object_count; // ": 1,
	@Key
	public Boolean confirmation_required; // ": false
	@Key
	public String status; // ": "viewed", // TODO: ENUM
}