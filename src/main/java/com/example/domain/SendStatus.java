package com.example.domain;

public enum SendStatus
{
	Sent,		// email is sent
	Pending,	// scheduled for sending
	Rejected,	// rejected by the provider
	Error,		// internal error
	NotValid,	// invalid request
	NotSent,	// not sent and will not retry more
}
