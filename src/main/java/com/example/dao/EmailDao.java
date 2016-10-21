package com.example.dao;

import com.example.domain.Email;
import com.example.domain.SendStatus;

public interface EmailDao
{
	public void save(Email email);
	
	public void markAsSent(Email email);
	
	public void markAsRejected(Email email);
	
	public void markAsPending(Email email);
	
	public Email getPending();
	
	public SendStatus getStatus(long emailId);
}
