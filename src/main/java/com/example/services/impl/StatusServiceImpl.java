package com.example.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.runnable.EmailSendingRouter;
import com.example.senders.EmailSenderManager;
import com.example.services.StatusService;

@Service
public class StatusServiceImpl implements StatusService
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
