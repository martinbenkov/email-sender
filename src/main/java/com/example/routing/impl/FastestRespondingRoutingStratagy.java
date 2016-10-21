package com.example.routing.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.example.routing.RoutingStratagy;
import com.example.senders.EmailSenderManager;

@Component
@Qualifier("FastestRespondingRoutingStratagy")
public class FastestRespondingRoutingStratagy implements RoutingStratagy
{
	private static final int DEFAULT_DELTA = 1000;
	
	private int acceptableTimeDelta;
	
	private int c;
	
	public FastestRespondingRoutingStratagy()
	{
		this(DEFAULT_DELTA);
	}
	
	FastestRespondingRoutingStratagy(int acceptableTimeDelta)
	{
		this.acceptableTimeDelta = acceptableTimeDelta;
	}

	public EmailSenderManager getEmailSender(List<EmailSenderManager> emailSenderManagers)
	{
		c = ++c % 10000000;
		int n = emailSenderManagers.size();
		long[] times = new long[n];
		for (int i=0; i<n; i++)
		{
			EmailSenderManager emailSenderManager = emailSenderManagers.get(i);
			if (emailSenderManager.canSend())
			{
				times[i] = emailSenderManager.getLastSuccessfulSendTs();
			}
		}
		long max = -1;
		for (int i=0; i<n; i++)
		{
			max = Math.max(max, times[i]);
		}
		List<Integer> allSendersWithinDelta = new ArrayList<Integer>();
		for (int i=0; i<n; i++)
		{
			if (max - times[i] < acceptableTimeDelta)
			{
				allSendersWithinDelta.add(i);
			}
		}
		
		int index = allSendersWithinDelta.get(c % allSendersWithinDelta.size());
		return emailSenderManagers.get(index);
	}

}
