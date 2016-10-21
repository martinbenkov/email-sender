package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.example.services.RecoveryService;

@Controller
public class RecoveryController
{
	@Autowired
	private RecoveryService recoveryService;
	
	@RequestMapping(value = "/recover", method=RequestMethod.POST)
	public ResponseEntity<Void> recover()
	{
		recoveryService.recoverEmail();
		return new ResponseEntity<Void>(HttpStatus.OK);
	}
}
