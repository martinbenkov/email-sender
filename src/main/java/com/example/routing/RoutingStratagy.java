package com.example.routing;

import java.util.List;

import com.example.senders.EmailSender;
import com.example.senders.EmailSenderManager;

public interface RoutingStratagy
{
	public EmailSenderManager getEmailSender(List<EmailSenderManager> emailSenderManagers);
}
