package com.example.services.impl;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;

import com.example.domain.Email;
import com.example.services.EmailValidationService;

@Service
public class EmailValidationServiceImpl implements EmailValidationService
{
	// consts because they are in the db also.
	private static final int MAX_EMAIL_LENGTH = 100;

	private static final int MAX_SUBJECT_LENGTH = 1000;

	private static final int MAX_TEXT_LENGTH = 10000;

	public static final Pattern VALID_EMAIL_ADDRESS_REGEX = 
			Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

	public boolean isValid(Email email)
	{
		if ((email.getFrom() == null) || (email.getFrom().length() > MAX_EMAIL_LENGTH) ||
				(email.getFrom().length() == 0))
		{
			return false;
		}

		if ((email.getTo() == null) || (email.getTo().length() > MAX_EMAIL_LENGTH) ||
				(email.getTo().length() == 0))
		{
			return false;
		}

		if ((email.getSubject() == null) || (email.getSubject().length() > MAX_SUBJECT_LENGTH) ||
				(email.getSubject().length() == 0))
		{
			return false;
		}

		if ((email.getText() == null) || (email.getText().length() > MAX_TEXT_LENGTH) ||
				(email.getText().length() == 0))
		{
			return false;
		}

		if ((!validate(email.getTo())) || (!validate(email.getFrom())))
		{
			return false;
		}

		return true;
	}


	private boolean validate(String emailStr) {
		Matcher matcher = VALID_EMAIL_ADDRESS_REGEX .matcher(emailStr);
		return matcher.find();
	}

}
