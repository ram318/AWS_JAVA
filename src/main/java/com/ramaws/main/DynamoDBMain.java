package com.ramaws.main;

public class DynamoDBMain {
    public static void main(String args[]){
        String tableName = "TestingJava";//TABLE NAME IN AWS DYNAMODB
        DynamoDBService dynamodbService = new DynamoDBService();
        
        
        //CREATING TABLE IN DYNAMODB
        //dynamodbService.createTable(tableName,"AttributeOne");
        
        
        Map<String,AttributeValue> item= new HashMap<>();
		item.put("AttributeOne",new AttributeValue("ValueOne1"));	
		item.put("AttributeTwo",new AttributeValue("ValueTwo2"));
		dynamodbService.createItem(tableName, item);
        
        
        
        //SEARCH BEGIN ->
		List<Map<String, AttributeValue>> items = dynamodbService.scanItems(tableName, ComparisonOperator.EQ, "AttributeOne", "ValueOne");
		if(items.size()<1) {
			System.out.println("NO ITEMS FOUND");
		}else {
			System.out.println("ITEMS FOUND\n");
		}
		Iterator<Map<String, AttributeValue>> i = items.iterator();
		
		while(i.hasNext()) {
			Map<String, AttributeValue> resultItem = (Map<String, AttributeValue>) i.next();
			System.out.println(resultItem);
		}
        
        //<-SEARCH END>
        
        
        //DELETE OPERATION (SUCCESSFUL)->
        Map<String,AttributeValue> key= new HashMap<>();
		key.put("AttributeOne",new AttributeValue("ValueOne1"));	
		boolean result = dynamodbService.deleteItem(tableName, key);
		if(result) {
			System.out.println("ITEM DELETED");
		}
		else {
			System.out.println("ITEM NOT DELETED");
		}

        
        
        //DELETE OPERATION (FAILURE) ->
        		key= new HashMap<>();
		key.put("AttributeOne",new AttributeValue("ValueOne5"));	
		result = dynamodbService.deleteItem(tableName, key);
		if(result) {
			System.out.println("ITEM DELETED");
		}
		else {
			System.out.println("ITEM NOT DELETED");
		}
        
        
        
    }
}
