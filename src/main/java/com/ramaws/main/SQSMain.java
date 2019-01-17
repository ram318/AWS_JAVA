package com.ramaws.main;

import java.util.List;
import java.util.concurrent.TimeUnit;

import com.amazonaws.services.sqs.model.Message;
import com.ramaws.services.*;

public class SQSMain {
    public static void main(String args[]) throws InterruptedException {

        SQSService sqsService = new SQSService();

        // CREATING THE QUEUE
        String queueName = "MyQueue";
        String result = sqsService.createQueue(queueName);
        System.out.println(result);

        String queueURL = result;

        // SEND MESSAGES TO QUEUE
        sqsService.sendMessage(queueURL, "Hello. This is a test message");
        sqsService.sendMessage(queueURL, "Hello. And this is the second message");
        sqsService.sendMessage(queueURL, "I am the third message");
        System.out.println("INSERTED THE MESSAGES. DONE");

        // PAUSE FOR 30 SECONDS.
        // MEANWHILE, GO TO AWS WEB CONSOLE AND CHECK THE QUEUE AND MESSAGES
        TimeUnit.SECONDS.sleep(30);

        // FETCHING THE 3 MESSAGES FROM THE QUEUE AND THEN DELETING THE MESSAGES
        System.out.println("FETCHING THE MESSAGES...");
        for (int i = 0; i < 3; i++) {
            List<Message> messagesList = sqsService.receiveMessage(queueURL);
            Message messageTobeDeleted;
            // System.out.println(messagesList.size());
            for (Message message : messagesList) {
                messageTobeDeleted = new Message();
                System.out.println("\nMESSAGE -------------");
                System.out.println("MessageId:" + message.getMessageId());
                System.out.println("Body:" + message.getBody());
                messageTobeDeleted = message;
                // DELETION OF MESSAGE
                sqsService.deleteMessage(queueURL, messageTobeDeleted);
            }
        }

        // PAUSE FOR 30 SECONDS AGAIN
        // MEANWHILE, GO TO AWS WEB CONSOLE AND CHECK THE QUEUE AND MESSAGES
        Thread.sleep(30000);

        // DELETION OF QUEUE
        sqsService.deleteQueue(queueURL);

    }
}
