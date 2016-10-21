package com.example.services;

import com.example.domain.Email;
import com.example.domain.SendStatus;

public interface EmailService 
{
	public SendStatus process(Email email);

	public SendStatus getStatus(long emailId);
}