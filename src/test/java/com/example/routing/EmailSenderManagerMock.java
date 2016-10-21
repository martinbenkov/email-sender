package com.example.routing;

import com.example.domain.Email;
import com.example.senders.EmailSenderManager;
import com.example.senders.EmailSenderType;

public class EmailSenderManagerMock implements EmailSenderManager
{
	private boolean isHealthy;
	
	private long lastSuccessfulSendTs;
	
	public EmailSenderManagerMock(boolean isHealthy, long lastSuccessfulSendTs)
	{
		this.isHealthy = isHealthy;
		this.lastSuccessfulSendTs = lastSuccessfulSendTs;
	}

	

	@Override
	public boolean canSend()
	{
		return true;
	}

	@Override
	public boolean isHealthy()
	{
		return isHealthy;
	}

	@Override
	public long getLastSuccessfulSendTs()
	{
		return lastSuccessfulSendTs;
	}

	@Override
	public void sendToProviderQueue(Email email)
	{
		
	}

	@Override
	public long getAllEmails()
	{
		return 0;
	}

	@Override
	public long getSuccessfullySent()
	{
		return 0;
	}

	@Override
	public EmailSenderType getType()
	{
		return null;
	}

}
