package com.ramaws.configuration;


import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;

public class AmazonConfiguration {
    
    static String region = Regions.US_EAST_1.getName();    
    
    	 public AmazonDynamoDB getDynamoDBClient() {
		 
		 AmazonDynamoDB dynamoDB;
		 ProfileCredentialsProvider credentials = getCredentials();
		 dynamoDB = AmazonDynamoDBClientBuilder.standard()
		            .withCredentials(credentials)
		            .withRegion(region)
		            .build();
		 return dynamoDB;
	 }
	 
	 public AmazonSQS getSQSClient() {
		 
		 AmazonSQS sqs;
		 ProfileCredentialsProvider credentials = getCredentials();
		 sqs = AmazonSQSClientBuilder.standard()
	             .withCredentials(credentials)
	             .withRegion(region)
	             .build();
		 return sqs;
	 }
    
    ProfileCredentialsProvider getCredentials(){
		 ProfileCredentialsProvider credentials = new ProfileCredentialsProvider();
	        try {
	            credentials.getCredentials();
	        } catch (Exception e) {
	            throw new AmazonClientException("ERROR LOADING CREDENTIALS");
	        }
	        
	        return credentials;
	 }
}
