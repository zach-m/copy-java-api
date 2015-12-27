package com.tectonica.copy.model;

import com.google.api.client.util.Key;

public class Scope
{
	@Key
	public Profile profile;
	@Key
	public ReadWrite inbox;
	@Key
	public Company company;
	@Key
	public ReadWrite links;
	@Key
	public ReadWrite filesystem;

	// //////////////////////////////////////////////////////////////////////////////////////////////////

	public static class ReadWrite
	{
		@Key
		public Boolean read;
		@Key
		public Boolean write;

		public ReadWrite()
		{}

		public ReadWrite(boolean read)
		{
			this.read = read;
		}

		public ReadWrite(boolean read, boolean write)
		{
			this.read = read;
			this.write = write;
		}
	}

	public static class Profile extends ReadWrite
	{
		@Key
		public ReadWrite email;

		public Profile()
		{
			super();
		}

		public Profile(boolean read)
		{
			super(read);
		}

		public Profile(boolean read, boolean write)
		{
			super(read, write);
		}
	}

	public static class Company
	{
		@Key
		public Boolean multi;
		@Key
		public ReadWrite filesystem;
		@Key
		public ReadWrite inbox;
		@Key
		public ReadWrite email;
	}

	// //////////////////////////////////////////////////////////////////////////////////////////////////

	public static final Scope ALL;
	static
	{
		ALL = new Scope();
		ALL.profile = new Profile(true, true);
		ALL.profile.email = new ReadWrite(true);
		ALL.inbox = new ReadWrite(true);
		ALL.company = new Company();
		ALL.company.multi = true;
		ALL.company.filesystem = new ReadWrite(true, true);
		ALL.company.inbox = new ReadWrite(true);
		ALL.company.email = new ReadWrite(true);
		ALL.links = new ReadWrite(true, true);
		ALL.filesystem = new ReadWrite(true, true);
	}

	public static final Scope FILE_SYSTEM;
	static
	{
		FILE_SYSTEM = new Scope();
		FILE_SYSTEM.filesystem = new ReadWrite(true, true);
	}

	public static final Scope PROFILE;
	static
	{
		PROFILE = new Scope();
		PROFILE.profile = new Profile(true, true);
		PROFILE.profile.email = new ReadWrite(true);
	}
}
