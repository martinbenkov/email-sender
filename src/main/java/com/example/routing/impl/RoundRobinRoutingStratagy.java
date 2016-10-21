package com.example.routing.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.example.routing.RoutingStratagy;
import com.example.senders.EmailSenderManager;

@Component
@Qualifier("RoundRobinRoutingStratagy")
public class RoundRobinRoutingStratagy implements RoutingStratagy
{
	private int index;

	public EmailSenderManager getEmailSender(List<EmailSenderManager> emailSenderManagers)
	{
		EmailSenderManager result = null;
		for (int i = 0; i<emailSenderManagers.size(); i++)
		{
			index = ++index % emailSenderManagers.size();
			result = emailSenderManagers.get(index);
			if ((result.isHealthy() && result.canSend()))
			{
				break;
			}
		}
		return result;
	}

}
