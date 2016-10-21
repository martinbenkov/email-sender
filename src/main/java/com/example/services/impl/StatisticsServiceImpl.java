package com.example.services.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.runnable.EmailSendingRouter;
import com.example.senders.EmailSenderManager;
import com.example.services.StatisticsService;

@Service
public class StatisticsServiceImpl implements StatisticsService
{
	@Autowired
	private EmailSendingRouter emailSendingRouter;

	
	public Map<String, Object> getStats()
	{
		Map<String, Object> res = new HashMap<String, Object>();
		
		res.put("Count of mails currently in queue in router", emailSendingRouter.getInQueueCount());
		res.put("Count of mails that were not queued in router", emailSendingRouter.getNotQueuedCount());
		
		for (EmailSenderManager emailSenderManager : emailSendingRouter.getEmailSenderManagers())
		{
			String key = "Manager: " + emailSenderManager.getType().toString();
			String data = "is healthy: " + emailSenderManager.isHealthy();
			data += "all mails: " + emailSenderManager.getAllEmails();
			data += "successful sent: " + emailSenderManager.getSuccessfullySent();
			data += "last successful sent ts: " + emailSenderManager.getLastSuccessfulSendTs();
			res.put(key, data);
			
		}
		
		return res;
	}
	
}
