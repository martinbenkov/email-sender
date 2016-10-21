package com.example.domain;

import java.util.UUID;

public class Email
{
	private long id;
	
	private String from;
	
	private String to;
	
	private String subject;
	
	private String text;
	
	private boolean isPing;
	
	public Email()
	{
	}

	public Email(String from, String to, String subject, String text)
	{
		super();
		this.from = from;
		this.to = to;
		this.subject = subject;
		this.text = text;
	}

	public void setFrom(String from)
	{
		this.from = from;
	}
	public void setTo(String to)
	{
		this.to = to;
	}
	public void setSubject(String subject)
	{
		this.subject = subject;
	}
	public void setText(String text)
	{
		this.text = text;
	}
	public String getFrom()
	{
		return from;
	}

	public String getTo()
	{
		return to;
	}

	public String getSubject()
	{
		return subject;
	}

	public String getText()
	{
		return text;
	}
	
	public void setPing()
	{
		this.isPing = true;
	}
	
	public boolean isPing()
	{
		return isPing;
	}
	
	public long getId()
	{
		return id;
	}

	public void setId(long id)
	{
		this.id = id;
	}
	
	public static Email createPingEmail()
	{
		Email ping = new Email();
		ping.setFrom("from@example.com");
		ping.setTo("to@example.com");
		ping.setSubject("Ping");
		ping.setText("Ping");
		ping.setPing();
		return ping;
	}
}
