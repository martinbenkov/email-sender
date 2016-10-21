Email Service

The solution focuses on back-end

Trade-offs - use prototype spring beans and remove static initializer (Init.java)

Endpoints:
POST /send - send an email. In the body of the request should be a json. All fields should not be null or empty. Example:

{
	"from": "test@example.com",
	"to": "to@example.com",
	"subject": "json mail",
	"text": "json"
}

Response Codes 

202 - mail is accepted and is queued. In the response there's a location header to check the status

400 - if something is wrong with the request (not valid emails etc.)

500 - error in the service


GET /send/{emailId} - checks the status of an email with id

Response Codes:

200 - email is sent

202 - email is queued for delivery

204 - no such email

400 - email cannot be sent

406 - email was rejecteed by the sending providers

500 - error in the service

POST /recover - tries to get the oldest not sent email from the database and retries to send it

200 - always returns 200

GET /statistics - shows some statistics about the application

200 - always returns 200

GET /status - checks if the application is healthy.

200 - if the application is healthy and can send emails

404 - otherwise

DELETE /status - makes the /status return 404. Should be used in deploy.

200 - always returns 200

POST /status - makes the /status return 200.

200 - always returns 200

There's a DDoS filter that allows 5 requests from a connection per second. Requests in excess of this are first delayed, then throttled.

The design is the following: When a request is being received  it is validated, stored in the db and added in a queue. The client receives a status. 
Then another thread gets the email and dispatches it to a alive sender. Each sender has its own queue sends the email and updates the status in the db.
There are 2 implementations of the routing strategy - round-robin and fastest responding. 
The health check is on /status on start does not return 200. Only after the aplication is sure that it can send emails, does /status return 200. 
This is done by the application itself, no need for other infrastructure to do it. To achieve this the application sends dummy(ping) emails periodically in all its providers.

The recovery service is designed for a cluster of applications. If 2 instances try to recover the same email only one will succeed.

In order to use the app for real you must implement a new ConfigurationManager or use the EnvVarConfigurationManager and set all the parameters as environment variables.

Asuming that the load balancers check a url returns 200 to send traffic, if the return code is not 200 - don't send traffic. 
Also the lb does the ssl offloading so the service is http only.


The deploy should be something like (will update one by one all the applications):

1. Check if node n-1 is ok 
2. Send DELETE /status to disable the node
3. Wait for a few seconds
4. Kill node n
5. Update version
6. Start application
7. Move n++



The integration tests are going to be very dependant on the framework and infrastructure. Some cases may be:

1. Send email, check db status, check if email has arrived.
2. Insert a pending email in the db, call /recover, check if the status in the db is changed;
3. Give wrong configuration so that no email can be send, try to send one, check status in db, call recover, check if attemts is grown, call it more than maxAttemtps time, check status
4. Wrong configuratoin for one provider, check if emails are send. Same for other provider.
5. Test the recovery mechanism with 2 or more instances running at the same time. See if one email can be recovered by more than one application.

Adding db healtch checks may be added to check.



