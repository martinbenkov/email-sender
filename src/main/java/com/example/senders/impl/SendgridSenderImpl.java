package com.example.senders.impl;

import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.application.Init;
import com.example.configuration.ConfigurationManager;
import com.example.domain.Email;
import com.example.domain.SendStatus;
import com.example.senders.EmailSender;
import com.example.senders.EmailSenderType;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;


public class SendgridSenderImpl implements EmailSender
{
	private final static Logger logger = LoggerFactory.getLogger(SendgridSenderImpl.class);

	private String authHeader;

	private String sendUrl;

	private Client client;


	@Autowired
	private ConfigurationManager configurationManager;


	public SendgridSenderImpl()
	{
		Init.autoWire(this);
		this.authHeader = configurationManager.getSendGridAuthHeader();
		this.sendUrl = configurationManager.getSendGridSendUrl();

		client = Client.create();
	}

	public SendStatus sendToProvider(Email email)
	{
		ClientResponse clientResponse = null;
		try
		{
			String mailJson = convertToTheirJsonFormat(email);

			WebResource webResource = client.resource(sendUrl);
			clientResponse = webResource.type(MediaType.APPLICATION_JSON_TYPE).
					header("Authorization", authHeader).
					post(ClientResponse.class, mailJson);

			return processResponse(clientResponse);
		}
		catch (Exception e)
		{
			logger.error("error", e);
			return SendStatus.Error;
		}
		finally
		{
			if (clientResponse != null)
			{
				clientResponse.close();
			}
		}
	}

	private SendStatus processResponse(ClientResponse clientResponse)
	{
		int status = clientResponse.getStatus();
		if (status == 202 )
		{
			return SendStatus.Sent;
		}
		else if (status >= 400 && status < 500)
		{
			return SendStatus.Rejected;
		}
		return SendStatus.Error;
	}

	private  String convertToTheirJsonFormat(Email email)
	{
		String res = "{\"personalizations\": [{\"to\": [{\"email\": \"" + email.getTo() + "\"";
		res += "}],\"subject\": \"" + email.getSubject() + "\"";
		res += "}],\"from\": {\"email\": \"" + email.getFrom() + "\"";
		res += "}, \"content\": [{\"type\": \"text/plain\",\"value\": \"" + email.getText() + "\"}]}";

		return res;
	}

	public EmailSenderType getType()
	{
		return EmailSenderType.SENDGRID;
	}
}
