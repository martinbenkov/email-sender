package com.example.configuration;

public interface ConfigurationManager
{
	
	public String getSendGridSendUrl();
	
	public String getSendGridAuthHeader();
	
	public String getMailgunSendUrl();
	
	public String getMailgunApiKey();
	
	public int getPingMailTime();
	
	public int getEmailQueueSize();
	
	public int getSendsersOfOneTypeCount();
	
	public int getRetrySentAttempts();
	
	public String getDBUrl();
	
	public String getDBUser();
	
	public String getDBPass();

}
