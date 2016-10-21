package com.example.runnable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.domain.Email;
import com.example.senders.EmailSenderManager;

public class PingSenderRunnable  implements Runnable
{
	private final static Logger logger = LoggerFactory.getLogger(PingSenderRunnable.class);
	
	private EmailSenderManager emailSenderManager;
	
	private int delay;
	
	public PingSenderRunnable(EmailSenderManager emailSenderManager, int delay)
	{
		this.emailSenderManager = emailSenderManager;
		this.delay = delay;
	}
	

	public void run()
	{
		while (true)
		{
			try 
			{
				Email ping = Email.createPingEmail();
				emailSenderManager.sendToProviderQueue(ping);
				Thread.sleep(delay);
				
				//TODO remove break
				break;
			}
			catch (Throwable t)
			{
				logger.debug("error", t);;
			}
		}
	}
	

}
