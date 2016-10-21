package com.example.senders;

import com.example.domain.Email;
import com.example.domain.SendStatus;

public interface EmailSenderManager
{
	/**
	 * 
	 * @return true if the queue of the provider has space
	 */
	public boolean canSend();
	
	/**
	 * 
	 * @return true if the {@link EmailSenderManager} has successfully sent emails shortly
	 */
	public boolean isHealthy();
	
	/**
	 * 
	 * @return timestamp of the last successful sent
	 */
	public long getLastSuccessfulSendTs();
	
	/** Adds the email to the internal queue 
	 * @param email the email to be send
	 */
	public void sendToProviderQueue(Email email);
	
	/**
	 * 
	 * @return count of processed emails by this manager
	 */
	public long getAllEmails();

	/**
	 * 
	 * @return count of successfully sent emails by this manager
	 */
	public long getSuccessfullySent();
	
	public EmailSenderType getType();
	
}
