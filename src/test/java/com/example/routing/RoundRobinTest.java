package com.example.routing;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.example.routing.impl.RoundRobinRoutingStratagy;
import com.example.senders.EmailSenderManager;

public class RoundRobinTest
{
	
	private RoutingStratagy routingStratagy;
	
	public RoundRobinTest()
	{
		routingStratagy = new RoundRobinRoutingStratagy();
	}
	

	@Test
	public void test1()
	{
		EmailSenderManager m1 = new EmailSenderManagerMock(true, 1);
		EmailSenderManager m2 = new EmailSenderManagerMock(true, 4);
		EmailSenderManager m3 = new EmailSenderManagerMock(true, 9);
		
		List<EmailSenderManager> managers = new ArrayList<EmailSenderManager>();
		managers.add(m1);
		managers.add(m2);
		managers.add(m3);
		
		assertEquals(routingStratagy.getEmailSender(managers), m2);
		assertEquals(routingStratagy.getEmailSender(managers), m3);
		assertEquals(routingStratagy.getEmailSender(managers), m1);
		assertEquals(routingStratagy.getEmailSender(managers), m2);
	}
	
	@Test
	public void test2()
	{
		EmailSenderManager m1 = new EmailSenderManagerMock(true, 1);
		EmailSenderManager m2 = new EmailSenderManagerMock(false, 4);
		EmailSenderManager m3 = new EmailSenderManagerMock(true, 9);
		
		List<EmailSenderManager> managers = new ArrayList<EmailSenderManager>();
		managers.add(m1);
		managers.add(m2);
		managers.add(m3);
		
		assertEquals(routingStratagy.getEmailSender(managers), m3);
		assertEquals(routingStratagy.getEmailSender(managers), m1);
		assertEquals(routingStratagy.getEmailSender(managers), m3);
		assertEquals(routingStratagy.getEmailSender(managers), m1);
	}
	
	@Test
	public void test3()
	{
		EmailSenderManager m1 = new EmailSenderManagerMock(false, 1);
		EmailSenderManager m2 = new EmailSenderManagerMock(false, 4);
		EmailSenderManager m3 = new EmailSenderManagerMock(true, 9);
		
		List<EmailSenderManager> managers = new ArrayList<EmailSenderManager>();
		managers.add(m1);
		managers.add(m2);
		managers.add(m3);
		
		assertEquals(routingStratagy.getEmailSender(managers), m3);
		assertEquals(routingStratagy.getEmailSender(managers), m3);
	}

}
