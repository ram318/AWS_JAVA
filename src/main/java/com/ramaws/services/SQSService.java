package com.ramaws.services;

import java.util.List;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.CreateQueueRequest;
import com.amazonaws.services.sqs.model.CreateQueueResult;
import com.amazonaws.services.sqs.model.DeleteMessageRequest;
import com.amazonaws.services.sqs.model.DeleteMessageResult;
import com.amazonaws.services.sqs.model.DeleteQueueRequest;
import com.amazonaws.services.sqs.model.DeleteQueueResult;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.ramaws.configuration.AmazonConfiguration;

public class SQSService {
    
    AmazonSQS sqs;
    
    public SQSService(){
       AmazonConfiguration config = new AmazonConfiguration();
       sqs = config.getSQSClient();
    }
    
    public String createQueue(String queueName){
        CreateQueueRequest createQueueRequest = new CreateQueueRequest(queueName);
        CreateQueueResult createQueueResult = sqs.createQueue(createQueueRequest);
        String queueURL = createQueueResult.getQueueUrl();
        return queueURL;
    }
    
    public void sendMessage(String queueURL, String message){
        sqs.sendMessage(new SendMessageRequest(queueURL,message));
    }
    
    public List<Message> receiveMessage(String queueURL){
        ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest(queueURL);
        List<Message> messages = sqs.receiveMessage(receiveMessageRequest).getMessages();
        
        return messages;
    }
    
    public void deleteMessage(String queueURL, Message message){
        DeleteMessageRequest deleteMessageRequest = new DeleteMessageRequest(queueURL, message.getReceiptHandle());
        DeleteMessageResult deleteMessageResult = sqs.deleteMessage(deleteMessageRequest);
        System.out.println("[DELETION MESSAGE RESPONSE METADATA]"+deleteMessageResult.getSdkHttpMetadata().getHttpStatusCode());        
    }
    
    public void deleteQueue(String queueURL) {
		DeleteQueueResult deleteQueueResult = sqs.deleteQueue(new DeleteQueueRequest(queueURL));
		System.out.println(deleteQueueResult.getSdkResponseMetadata());
	}

	public AmazonSQS getSqs() {
		return sqs;
	}
    
    
}
