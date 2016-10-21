package com.example.senders.impl;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.example.application.Init;
import com.example.configuration.ConfigurationManager;
import com.example.configuration.impl.ConstConfigurationManager;
import com.example.dao.EmailDao;
import com.example.domain.Email;
import com.example.domain.SendStatus;
import com.example.runnable.PingSenderRunnable;
import com.example.senders.EmailSender;
import com.example.senders.EmailSenderManager;
import com.example.senders.EmailSenderType;

public class EmailSenderManagerImpl implements EmailSenderManager, Runnable
{
	private final static Logger logger = LoggerFactory.getLogger(EmailSenderManagerImpl.class);
	
	private static final int inHealthyDelayCoef = 3;
	
	private BlockingQueue<Email> queue;
	
	private long lastSuccessfulSendTs; 
	
	private int pingDelay;
	
	private EmailSender emailSender;
	
	@Autowired
	private EmailDao emailDao;
	
	@Autowired
	private ConfigurationManager configurationManager;
	
	private ExecutorService executorService;
	
	private long allEmails;
	
	private long successfullySent;
	
	public EmailSenderManagerImpl(EmailSender emailSender, int queueSize)
	{
		Init.autoWire(this);
		
		this.emailSender = emailSender;
		this.queue = new ArrayBlockingQueue<Email>(queueSize);
		this.pingDelay = configurationManager.getPingMailTime();
		
		executorService = Executors.newSingleThreadExecutor();
		PingSenderRunnable pingSenderRunnable = new PingSenderRunnable(this, pingDelay);
		
		executorService.submit(pingSenderRunnable);
	}
	
	public void run()
	{
		while (true)
		{
			try
			{
				Email email = queue.take();
				allEmails++;
				SendStatus sendStatus = emailSender.sendToProvider(email);
				handleSentStatus(email, sendStatus);
			}
			catch (Throwable t)
			{
				logger.debug("error", t);
			}
		}
		
	}

	private void handleSentStatus(Email email, SendStatus sendStatus)
	{
		if (sendStatus == SendStatus.Sent)
		{
			emailDao.markAsSent(email);
			lastSuccessfulSendTs = System.currentTimeMillis();
			successfullySent++;
		}
		else if (sendStatus == SendStatus.Rejected)
		{
			emailDao.markAsRejected(email);
		}
		else
		{
			emailDao.markAsPending(email);
		}
		
	}

	public void sendToProviderQueue(Email email)
	{
		boolean added = queue.offer(email);
		if (!added)
		{
			logger.debug("Cannot enqueue email " + email.getId());
		}
	}
	
	public boolean isHealthy()
	{
		return (System.currentTimeMillis() - lastSuccessfulSendTs) < inHealthyDelayCoef * pingDelay;
	}
	
	public long getLastSuccessfulSendTs()
	{
		return lastSuccessfulSendTs;
	}

	public boolean canSend()
	{
		return queue.remainingCapacity() > 0;
	}
	
	public long getAllEmails()
	{
		return allEmails;
	}

	public long getSuccessfullySent()
	{
		return successfullySent;
	}

	@Override
	public EmailSenderType getType()
	{
		return emailSender.getType();
	}

}
