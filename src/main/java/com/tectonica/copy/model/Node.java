package com.tectonica.copy.model;

import java.util.List;

import com.google.api.client.util.GenericData;
import com.google.api.client.util.Key;

public class Node extends GenericData
{
	public static class Dimensions extends GenericData
	{
		@Key
		public Integer width;
		@Key
		public Integer height;
	}

	public static class Nodes extends GenericData
	{
		@Key
		public List<Node> objects;
		@Key
		public String user_auth_token;
	}

	@Key
	public String id; // ": "\/copy\/test\/hello.txt",
	@Key
	public String path; // ": "\/test\/hello.txt",
	@Key
	public String name; // ": "hello.txt",
	@Key
	public String type; // TODO: ENUM - file, dir, link, root, copy, inbox
	@Key
	public Long size; // ": 11,
	@Key
	public Long date_last_synced; // ": 1451153582,
	@Key
	public Long modified_time; // ": 1451153582,
	@Key
	public Boolean stub; // ": true,
	@Key
	public Integer children_count;
	@Key
	public List<Node> children;
	@Key
	public Boolean recipient_confirmed; // ": false,
	@Key
	public Boolean object_available; // ": false,
	@Key
	public String mime_type; // ": "text\/plain",
	@Key
	public String link_name; // ": null,
	@Key
	public String token; // ": null,
	@Key
	public String creator_id; // ": null,
	@Key
	public String permissions; // ": null,
	@Key
	public Boolean syncing; // ": false,
	@Key("public")
	public Boolean is_public; // ": false,
	@Key
	public List<Link> links; // ": [],
	@Key
	public String url; // ": "\/test\/hello.txt",
	@Key
	public Integer revision; // ": 3,
//	@Key
//	public Integer revision_id; // ": 3,
	@Key
	public String thumb; // ": null,
	@Key
	public Dimensions thumb_original_dimensions; // ": null,
	@Key
	public String share; // ": null
	
	// TODO: the fields 'counts' is an array, but serialized a single value of leangth = 1
//	@Key
//	public Counts counts; // ": [],
//	
//	public static class Counts extends GenericData
//	{
//		@Key("new")
//		public Integer new_; // ": 7,
//		@Key
//		public Integer viewed; // ": 30,
//		@Key
//		public Integer hidden; // ": 95
//	}
}
