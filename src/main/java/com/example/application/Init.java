package com.example.application;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class Init implements ApplicationContextAware
{
	private static ApplicationContext applicationContextInstance;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException
	{
		applicationContextInstance = applicationContext;
	}
	
	public static void autoWire(Object toAutowire)
	{
		applicationContextInstance.getAutowireCapableBeanFactory().autowireBean(toAutowire);
	}

}
