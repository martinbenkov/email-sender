package com.example.services;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.dao.EmailDao;
import com.example.domain.Email;
import com.example.domain.SendStatus;
import com.example.runnable.EmailSendingRouter;

@Service
public class EmailService implements InitializingBean
{
	private final static Logger logger = LoggerFactory.getLogger(EmailService.class);

	@Autowired
	private EmailSendingRouter emailSendingRouter;

	@Autowired
	private EmailValidationService emailValidationService;

	@Autowired
	private EmailDao emailDao;

	private ExecutorService executorService;

	public EmailService()
	{
	}

	/**
	 * Validates, stores in db and dispatches it to managers
	 * @param email
	 * @return {@link SendStatus#NotValid} if the validation failed
	 * {@link SendStatus#Pending} if it is stored in db and is going to be processed
	 *  {@link SendStatus#Error} if an error occured
	 */
	public SendStatus process(Email email)
	{
		if (!emailValidationService.isValid(email))
		{
			return SendStatus.NotValid;

		}
		try
		{
			emailDao.save(email);
			emailSendingRouter.dispatchToManagers(email);
			return SendStatus.Pending;
		}
		catch (Exception e)
		{
			logger.error("Error", e);
			return SendStatus.Error;
		}
	}

	public SendStatus getStatus(long emailId)
	{
		try
		{
			return emailDao.getStatus(emailId);
		}
		catch (Exception e)
		{
			logger.error("Error", e);
			return SendStatus.Error;
		}
	}

	@Override
	public void afterPropertiesSet() throws Exception
	{
		executorService = Executors.newSingleThreadExecutor();
		executorService.submit((Runnable) emailSendingRouter);
	}
}