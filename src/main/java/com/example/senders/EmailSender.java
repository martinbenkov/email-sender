package com.example.senders;

import com.example.domain.Email;
import com.example.domain.SendStatus;

public interface EmailSender
{
	/**
	 * Physically send the email to a provider
	 * 
	 * @param email the email to be send
	 * @return {@link SendStatus#Sent} - if the provider accepted the mail, 
	 * {@link SendStatus#Error} if there was an internal error
	 * {@link SendStatus#Rejected} if the provider did not accept the mail
	 */
	public SendStatus sendToProvider(Email email);
	
	public EmailSenderType getType();
}
