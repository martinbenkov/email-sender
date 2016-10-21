package com.example.services;

import static org.junit.Assert.*;

import org.junit.Test;

import com.example.domain.Email;

public class TestEmailValidator
{
	
	EmailValidationService emailValidationService;

	public TestEmailValidator()
	{
		emailValidationService = new EmailValidationService();
	}
	
	@Test
	public void testValid()
	{
		Email email = new Email(getValidEmail(), getValidEmail(), getLongString(10), getLongString(10));
		assertTrue(emailValidationService.isValid(email));
		
	}
	
	@Test
	public void testInvalidFrom()
	{
		Email email = new Email(getInvalidEmail(), getValidEmail(), getLongString(10), getLongString(10));
		assertFalse(emailValidationService.isValid(email));
		
	}
	
	@Test
	public void testInvalidTo()
	{
		Email email = new Email(getValidEmail(), getInvalidEmail(), getLongString(10), getLongString(10));
		assertFalse(emailValidationService.isValid(email));
		
	}
	
	@Test
	public void testInvalidSubjectLong()
	{
		Email email = new Email(getValidEmail(), getValidEmail(), getLongString(10000), getLongString(10));
		assertFalse(emailValidationService.isValid(email));
		
	}
	
	@Test
	public void testInvalidSubjectShort()
	{
		Email email = new Email(getValidEmail(), getValidEmail(), getLongString(0), getLongString(10));
		assertFalse(emailValidationService.isValid(email));
		
	}
	
	@Test
	public void testInvalidTextLong()
	{
		Email email = new Email(getValidEmail(), getValidEmail(), getLongString(100), getLongString(10010));
		assertFalse(emailValidationService.isValid(email));
		
	}
	
	@Test
	public void testInvalidTextShort()
	{
		Email email = new Email(getValidEmail(), getValidEmail(), getLongString(100), getLongString(0));
		assertFalse(emailValidationService.isValid(email));
		
	}
	
	
	private String getValidEmail()
	{
		return "a@a.com";
	}
	
	private String getInvalidEmail()
	{
		return "asd.com";
	}
	
	private String getLongString(int size)
	{
		String res = "";
		for (int i=0; i<size; i++)
		{
			res += ".";
		}
		return res;
	}

}
