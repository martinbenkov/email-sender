package com.example.configuration.impl;

import com.example.configuration.ConfigurationManager;

public class EnvVarConfigurationManager implements ConfigurationManager
{

	@Override
	public String getSendGridSendUrl()
	{
		return System.getenv("getSendGridSendUrl");
	}

	@Override
	public String getSendGridAuthHeader()
	{
		return System.getenv("getSendGridAuthHeader");
	}

	@Override
	public String getMailgunSendUrl()
	{
		return System.getenv("getMailgunSendUrl");
	}

	@Override
	public String getMailgunApiKey()
	{
		return System.getenv("getMailgunApiKey");
	}

	@Override
	public int getPingMailTime()
	{
		return Integer.parseInt(System.getenv("getPingMailTime"));
	}

	@Override
	public int getEmailQueueSize()
	{
		return Integer.parseInt(System.getenv("getEmailQueueSize"));
	}

	@Override
	public int getSendsersOfOneTypeCount()
	{
		return Integer.parseInt(System.getenv("getSendsersOfOneTypeCount"));
	}

	@Override
	public int getRetrySentAttempts()
	{
		return Integer.parseInt(System.getenv("getRetrySentAttempts"));
	}

	@Override
	public String getDBUrl()
	{
		return System.getenv("getDBUrl");
	}

	@Override
	public String getDBUser()
	{
		return System.getenv("getDBUser");
	}

	@Override
	public String getDBPass()
	{
		return System.getenv("getDBPass");
	}

}
