package com.example.senders.impl;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.application.Init;
import com.example.configuration.ConfigurationManager;
import com.example.configuration.impl.ConstConfigurationManager;
import com.example.domain.Email;
import com.example.domain.SendStatus;
import com.example.senders.EmailSender;
import com.example.senders.EmailSenderType;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;
import com.sun.jersey.core.util.MultivaluedMapImpl;



public class MailgunSenderImpl implements EmailSender
{
	private final static Logger logger = LoggerFactory.getLogger(MailgunSenderImpl.class);
	
	private String apiKey;

	private String sendUrl;

	private Client client;
	
	@Autowired
	private ConfigurationManager configurationManager;

	public MailgunSenderImpl()
	{
		Init.autoWire(this);
		this.apiKey = configurationManager.getMailgunApiKey();
		this.sendUrl = configurationManager.getMailgunSendUrl();

		client = Client.create();
		client.addFilter(new HTTPBasicAuthFilter("api", apiKey));
	}


	public SendStatus sendToProvider(Email email) {
		
		ClientResponse clientResponse = null;
		try
		{
			WebResource webResource = client.resource(sendUrl);
			MultivaluedMapImpl formData = new MultivaluedMapImpl();
			formData.add("from", "<" + email.getFrom() + ">");
			formData.add("to", email.getTo());
			formData.add("subject", email.getSubject());
			formData.add("text", email.getText());
			clientResponse =  webResource.type(MediaType.APPLICATION_FORM_URLENCODED).
					post(ClientResponse.class, formData);

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
		if (status == 200)
		{
			return SendStatus.Sent;
		}
		else if (status >= 400 && status < 500)
		{
			return SendStatus.Rejected;
		}
		return SendStatus.Error;
	}


	public EmailSenderType getType()
	{
		return EmailSenderType.MAILGUN;
	}

}
