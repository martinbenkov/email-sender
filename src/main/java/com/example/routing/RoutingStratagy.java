package com.example.routing;

import java.util.List;

import com.example.senders.EmailSender;
import com.example.senders.EmailSenderManager;

public interface RoutingStratagy
{
	/**
	 * 
	 * @param emailSenderManagers - list of EmailSenderManager
	 * @return the most appropriate EmailSenderManager, regarding the stratagy
	 */
	public EmailSenderManager getEmailSender(List<EmailSenderManager> emailSenderManagers);
}
