## how to run the project


1. Import in Intellj as maven project
2 Change the json file path in Constant.java file
3 Change the log file path in log4j.property file under src/main/resources
4. Run clean install maven commands
5. Run on tomcat server
6. Access the urls using postman



## below are the urls to create, get, list, and update the account

#################################
1. get meta data
URL : http://localhost:8080/JaxRSCrudDemo/v1/accounts/account/metadata
Method: GET
##################################

2. Create Account
URL : http://localhost:808/JaxRSCrudDemo/v1/accounts/account/
Method: POST
Body: raw (application/json)
Json:
{
	"accountId" : "20",
	"customerName" : "test1111222",
	"currency" : "INR1",
	"amount" : 0
	
}
##################################

3. Get list of all Accounts
URL: http://localhost:8082/JaxRSCrudDemo/v1/accounts/account/
Method: GET

##################################

4. Get Account detail by its id
URL: http://localhost:8082/JaxRSCrudDemo/v1/accounts/account/1
Method: GET

##################################

5. Update the Account
URL: http://localhost:808/JaxRSCrudDemo/v1/accounts/account/
Method: Patch
Body: raw (application/json)
Json:
{
	"accountId" : "20",
	"customerName" : "test1111222",
	"currency" : "INR1",
	"amount" : 0
	
}