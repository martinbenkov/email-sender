package com.example.senders;

import org.springframework.stereotype.Component;

import com.example.senders.impl.EmailSenderManagerImpl;
import com.example.senders.impl.MailgunSenderImpl;
import com.example.senders.impl.SendgridSenderImpl;


public class EmailSenderManagerFactory
{

	public static EmailSenderManager create(EmailSenderType emailSenderType, int queueSize)
	{
		if (emailSenderType == EmailSenderType.MAILGUN)
			return new EmailSenderManagerImpl(new MailgunSenderImpl(), queueSize);
		if (emailSenderType == EmailSenderType.SENDGRID)
			return new EmailSenderManagerImpl(new SendgridSenderImpl(), queueSize);
		
		throw new RuntimeException("unknow type");
	}
}
