package com.example.services;


import com.example.domain.Email;

public interface EmailValidationService
{
	public boolean isValid(Email email);
}
