package com.example.application;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.example.configuration.ConfigurationManager;

@Configuration
@EnableTransactionManagement(proxyTargetClass = true)
public class DataSourceFactory
{
	@Autowired
	ConfigurationManager configurationManager;
	
	@Autowired
	@Bean(destroyMethod = "close")
	public DataSource dataSource()
	{
		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setDriverClassName("com.mysql.jdbc.Driver");
		dataSource.setUrl(configurationManager.getDBUrl());
		dataSource.setUsername(configurationManager.getDBUser());
		dataSource.setPassword(configurationManager.getDBPass());
		//TODO other params
		
		return dataSource;
	}
}
