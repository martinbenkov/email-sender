package com.example.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.runnable.EmailSendingRouter;
import com.example.senders.EmailSenderManager;

@Service
public class StatusService
{
	private boolean isForceUnavailable;
	
	@Autowired
	private EmailSendingRouter emailSendingRouter;
	
	public boolean isOk()
	{
		if (isForceUnavailable)
		{
			return false;
		}
		List<EmailSenderManager> emailSenderManagers = emailSendingRouter.getEmailSenderManagers();
		
		for (EmailSenderManager emailSenderManager : emailSenderManagers)
		{
			if (emailSenderManager.isHealthy())
			{
				return true;
			}
		}
		
		return false;
	}
	
	public void setForceUnavailable(boolean isForceStop)
	{
		this.isForceUnavailable = isForceStop;
	}
}
