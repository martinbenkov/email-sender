package com.example.configuration.impl;

import org.springframework.stereotype.Component;

import com.example.configuration.ConfigurationManager;

@Component
public class ConstConfigurationManager implements ConfigurationManager
{
	public String getSendGridSendUrl()
	{
		return "https://api.sendgrid.com/v3/mail/send";
	}

	public String getSendGridAuthHeader()
	{
		return "Bearer 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";
	}

	public String getMailgunSendUrl()
	{
		return "https://api.mailgun.net/v3/sandboxe2c2266af2a84a56921b3e6d15f9541f.mailgun.org/messages";
	}

	public String getMailgunApiKey()
	{
		return "key-446c1e6bf15403b671a03c2a3d88ba31";
	}

	public int getPingMailTime()
	{
		return 10000;
	}

	@Override
	public int getEmailQueueSize()
	{
		return 5;
	}

	@Override
	public int getSendsersOfOneTypeCount()
	{
		return 1;
	}

	@Override
	public int getRetrySentAttempts()
	{
		return 3;
	}

	@Override
	public String getDBUrl()
	{
		return "jdbc:mysql://localhost:3306/email_sender_db";
	}

	@Override
	public String getDBUser()
	{
		return "trader";
	}

	@Override
	public String getDBPass()
	{
		return "trader";
	}

}
