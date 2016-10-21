package com.example.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.example.services.StatisticsService;

@Controller
public class StatisticsController
{
	@Autowired
	private StatisticsService statisticsService;

	@RequestMapping(value = "/statistics", method=RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> getStatus()
	{
		Map<String, Object> data = statisticsService.getStats();
		return new ResponseEntity<Map<String, Object>>(data, HttpStatus.OK);
	}
}
