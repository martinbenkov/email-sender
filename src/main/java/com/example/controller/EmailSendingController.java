package com.example.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.domain.Email;
import com.example.domain.SendStatus;
import com.example.services.EmailService;


@Controller
public class EmailSendingController
{
	private final static Logger logger = LoggerFactory.getLogger(EmailSendingController.class);
	
	@Autowired
	EmailService emailService;

	
	@RequestMapping(value = "/send", method=RequestMethod.POST)
	public ResponseEntity<Void> send(@RequestBody Email email)
	{

		SendStatus sendStatus = emailService.process(email);
		if (sendStatus == SendStatus.Pending)
		{
			HttpHeaders httpHeaders = new HttpHeaders();
			httpHeaders.setLocation(ServletUriComponentsBuilder
					.fromCurrentRequest().path("/{id}")
					.buildAndExpand(email.getId()).toUri());
			return new ResponseEntity<Void>(httpHeaders, HttpStatus.ACCEPTED);
		}
		else if (sendStatus == SendStatus.NotValid)
		{
			return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
		}
		else if (sendStatus == SendStatus.Error)
		{
			return new ResponseEntity<Void>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		logger.error("Unhandled sendStatus:" + sendStatus.toString());
		return new ResponseEntity<Void>(HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@RequestMapping(value = "/send/{emailId}", method=RequestMethod.GET)
	public ResponseEntity<Void> getEmailStatus(@PathVariable long emailId) 
	{
		SendStatus sendStatus = emailService.getStatus(emailId);
		if (sendStatus == null)
		{
			return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
		}
		if (sendStatus == SendStatus.Sent)
		{
			return new ResponseEntity<Void>(HttpStatus.OK);
		}
		else if (sendStatus == SendStatus.Rejected)
		{
			return new ResponseEntity<Void>(HttpStatus.NOT_ACCEPTABLE);
		}
		else if (sendStatus == SendStatus.Pending)
		{
			return new ResponseEntity<Void>(HttpStatus.ACCEPTED);
		}
		else if (sendStatus == SendStatus.NotSent)
		{
			return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
		}
		
		logger.error("Unhandled sendStatus:" + sendStatus.toString());
		return new ResponseEntity<Void>(HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
}
