package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.example.services.StatusService;


@Controller
public class StatusController
{
	@Autowired
	private StatusService statusService;

	@RequestMapping(value = "/status", method=RequestMethod.GET)
	public ResponseEntity<Void> getStatus()
	{
		if (statusService.isOk())
		{
			return new ResponseEntity<Void>(HttpStatus.OK);
		}
		else
		{
			return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
		}
	}

	@RequestMapping(value = "/status", method=RequestMethod.POST)
	public ResponseEntity<Void> disableForceUnavailable()
	{
		statusService.setForceUnavailable(false);
		return new ResponseEntity<Void>(HttpStatus.OK);
	}
	
	@RequestMapping(value = "/status", method=RequestMethod.DELETE)
	public ResponseEntity<Void> enableForceUnavailable()
	{
		statusService.setForceUnavailable(true);
		return new ResponseEntity<Void>(HttpStatus.OK);
	}

}
