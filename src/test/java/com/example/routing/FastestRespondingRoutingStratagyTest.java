package com.example.routing;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.example.routing.impl.FastestRespondingRoutingStratagy;
import com.example.routing.impl.RoundRobinRoutingStratagy;
import com.example.senders.EmailSenderManager;

public class FastestRespondingRoutingStratagyTest
{
private RoutingStratagy routingStratagy;
	
	public FastestRespondingRoutingStratagyTest()
	{
		routingStratagy = new FastestRespondingRoutingStratagy();
	}
	
	
	@Test
	public void test0()
	{
		EmailSenderManager m1 = new EmailSenderManagerMock(true, 100);
		EmailSenderManager m2 = new EmailSenderManagerMock(true, 400);
		EmailSenderManager m3 = new EmailSenderManagerMock(true, 900);
		
		List<EmailSenderManager> managers = new ArrayList<EmailSenderManager>();
		managers.add(m1);
		managers.add(m2);
		managers.add(m3);
		
		assertEquals(routingStratagy.getEmailSender(managers), m2);
		assertEquals(routingStratagy.getEmailSender(managers), m3);
		assertEquals(routingStratagy.getEmailSender(managers), m1);
	}

	@Test
	public void test1()
	{
		EmailSenderManager m1 = new EmailSenderManagerMock(true, 1000);
		EmailSenderManager m2 = new EmailSenderManagerMock(true, 4000);
		EmailSenderManager m3 = new EmailSenderManagerMock(true, 9000);
		
		List<EmailSenderManager> managers = new ArrayList<EmailSenderManager>();
		managers.add(m1);
		managers.add(m2);
		managers.add(m3);
		
		assertEquals(routingStratagy.getEmailSender(managers), m3);
		assertEquals(routingStratagy.getEmailSender(managers), m3);
	}
	
	@Test
	public void test2()
	{
		EmailSenderManager m1 = new EmailSenderManagerMock(true, 100);
		EmailSenderManager m2 = new EmailSenderManagerMock(true, 2000);
		EmailSenderManager m3 = new EmailSenderManagerMock(true, 2200);
		
		List<EmailSenderManager> managers = new ArrayList<EmailSenderManager>();
		managers.add(m1);
		managers.add(m2);
		managers.add(m3);
		
		assertEquals(routingStratagy.getEmailSender(managers), m3);
		assertEquals(routingStratagy.getEmailSender(managers), m2);
	}

}
