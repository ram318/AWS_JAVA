package com.ramaws.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.document.DeleteItemOutcome;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.DeleteItemRequest;
import com.amazonaws.services.dynamodbv2.model.DeleteItemResult;
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement;
import com.ramaws.configuration.AmazonConfiguration;
import com.amazonaws.services.dynamodbv2.model.ScalarAttributeType;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;
import com.amazonaws.services.dynamodbv2.util.TableUtils;
import com.amazonaws.services.dynamodbv2.model.KeyType;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.model.PutItemRequest;
import com.amazonaws.services.dynamodbv2.model.PutItemResult;
import com.amazonaws.services.dynamodbv2.model.ReturnValue;
import com.amazonaws.services.dynamodbv2.model.AttributeDefinition;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.Condition;

public class DynamoDBService {

    AmazonDynamoDB dynamoDB;

    public DynamoDBService() {
        AmazonConfiguration config = new AmazonConfiguration();
        dynamoDB = config.getDynamoDBClient();
    }

    public void createTable(String tableName, String keyAttribute){
	    CreateTableRequest createTableRequest = new CreateTableRequest()
	       .withTableName(tableName).withKeySchema(new KeySchemaElement().withAttributeName(keyAttribute).withKeyType(KeyType.HASH)
	       .withAttributeDefinitions(new AttributeDefinition().withAttributeName(keyAttribute).withAttributeType(ScalarAttributeType.S))
	       .withProvisionedThroughput(new ProvisionedThroughput().withReadCapacityUnits(1L).withWriteCapacityUnits(1L));
	       
	       TableUtils.createTableIfNotExists(dynamoDB, createTableRequest);
	       try{
	           TableUtils.waitUntilActive(dynamoDB, tableName);
	       }catch(TableNeverTransitionedToStateException | InterruptedException e){
                    e.printStackTrace();
            }
	}
	
	
	public boolean createItem(String tableName, Map<String, AttributeValue> item){
	    PutItemRequest putItemRequest = new PutItemRequest(tableName, item);
         PutItemResult putItemResult = dynamoDB.putItem(putItemRequest);
         return putItemResult!=null?true:false;	    
	}
	
	public boolean deleteItem(String tableName, Map<String, AttributeValue> key) {
		DeleteItemRequest delItemRequest = new DeleteItemRequest(tableName, key,ReturnValue.ALL_OLD);
		DeleteItemResult deleteItemResult = dynamoDB.deleteItem(delItemRequest);
		DeleteItemOutcome delOutcome = new DeleteItemOutcome(deleteItemResult);
		return delOutcome.getItem()!=null?true:false;

	}
	
	
	public List<Map<String, AttributeValue>> scanItems(String tableName, ComparisonOperator comparisionOperator, String attributeName, String attributeValue) {
			
		HashMap<String, Condition> scanFilter = new HashMap<String, Condition>();
        Condition condition = new Condition()
            .withComparisonOperator(comparisionOperator.toString())
            .withAttributeValueList(new AttributeValue().withS(attributeValue));
        scanFilter.put(attributeName, condition);
        ScanRequest scanRequest = new ScanRequest(tableName).withScanFilter(scanFilter);
        ScanResult scanResult = dynamoDB.scan(scanRequest);
        List<Map<String,AttributeValue>> result = scanResult.getItems();		
		return result;
	}

}
