package com.example.runnable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.example.configuration.ConfigurationManager;
import com.example.domain.Email;
import com.example.routing.RoutingStratagy;
import com.example.senders.EmailSenderManager;
import com.example.senders.EmailSenderManagerFactory;
import com.example.senders.EmailSenderType;

@Component
public class EmailSendingRouterImpl implements EmailSendingRouter, Runnable, InitializingBean
{

	private final static Logger logger = LoggerFactory.getLogger(EmailSendingRouterImpl.class);
	
	@Autowired
	@Qualifier("RoundRobinRoutingStratagy")
	private RoutingStratagy routingStratagy;
	
	@Autowired
	private ConfigurationManager configurationManager;
	
	private BlockingQueue<Email> queue;
	
	private ExecutorService executorService;
	
	private List<EmailSenderManager> emailSenderManagers;
	
	private AtomicLong notQueuedCount;
	
	public EmailSendingRouterImpl()
	{
	}
	
	@Override
	public void afterPropertiesSet() throws Exception
	{
		int queueSize = configurationManager.getEmailQueueSize();
		int sendsersOfOneTypeCount = configurationManager.getSendsersOfOneTypeCount();
		
		notQueuedCount = new AtomicLong();
		emailSenderManagers = new ArrayList<EmailSenderManager>();
		queue = new ArrayBlockingQueue<Email>(queueSize);
		int threadPoolSize = sendsersOfOneTypeCount * 2;
		executorService = Executors.newFixedThreadPool(threadPoolSize);
		for (int i=0; i< sendsersOfOneTypeCount; i++)
		{
			EmailSenderManager mailgunSenderManager = EmailSenderManagerFactory.
					create(EmailSenderType.MAILGUN, queueSize);
			emailSenderManagers.add(mailgunSenderManager);
			executorService.submit((Runnable)mailgunSenderManager);
			
			EmailSenderManager sendgridSenderManager = EmailSenderManagerFactory.
					create(EmailSenderType.SENDGRID, queueSize);
			emailSenderManagers.add(sendgridSenderManager);
			executorService.submit((Runnable)sendgridSenderManager);
		}
		
	}
	
	public void dispatchToManagers(Email email)
	{
		boolean added = queue.offer(email);
		
		if (!added)
		{
			notQueuedCount.incrementAndGet();
			logger.error("Cannot enqueue email " + email.getId());
		}
		
	}


	public void run()
	{
		while (true)
		{
			try
			{
				Email email = queue.take();
				EmailSenderManager emailSenderManager = routingStratagy.getEmailSender(emailSenderManagers);
				emailSenderManager.sendToProviderQueue(email);
			}
			catch (Throwable t)
			{
				logger.debug("error", t);
				
			}
		}
	}
	
	public List<EmailSenderManager> getEmailSenderManagers()
	{
		return emailSenderManagers;
	}
	
	public long getNotQueuedCount()
	{
		return notQueuedCount.longValue();
	}
	
	public int getInQueueCount()
	{
		return queue.size();
	}



}
