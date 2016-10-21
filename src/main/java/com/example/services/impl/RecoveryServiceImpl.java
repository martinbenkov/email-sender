package com.example.services.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.dao.EmailDao;
import com.example.domain.Email;
import com.example.runnable.EmailSendingRouter;
import com.example.services.RecoveryService;

@Service
public class RecoveryServiceImpl implements RecoveryService
{
	private final static Logger logger = LoggerFactory.getLogger(RecoveryServiceImpl.class);
	
	@Autowired
	private EmailSendingRouter emailSendingRouter;

	@Autowired
	private EmailDao emailDao;

	/**
	 * Try to resend pending emails
	 */
	public void recoverEmail()
	{
		try
		{
			Email email = emailDao.getPending();
			if (email != null)
			{
				emailSendingRouter.dispatchToManagers(email);
				logger.debug("Recovered email " + email.getId());
			}
		}
		catch (Exception e)
		{
			logger.error("Error", e);
		}
	}


}
