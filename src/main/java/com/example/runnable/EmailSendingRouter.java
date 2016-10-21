package com.example.runnable;

import java.util.List;

import com.example.domain.Email;
import com.example.senders.EmailSenderManager;

public interface EmailSendingRouter 
{
	public void dispatchToManagers(Email email);
	
	public List<EmailSenderManager> getEmailSenderManagers();
	
	public long getNotQueuedCount();
	
	public int getInQueueCount();
}
