package com.java.awsproject.service;

import com.java.awsproject.domain.model.Session;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class DynamoDbService {

    private final DynamoDbClient dynamoDbClient;

    @Value("${aws.dynamodb.table-name:sesiones-alumnos}")
    private String tableName;

    public DynamoDbService(DynamoDbClient dynamoDbClient) {
        this.dynamoDbClient = dynamoDbClient;
    }

    public void saveSession(Session session) {
        Map<String, AttributeValue> item = new HashMap<>();
        item.put("id", AttributeValue.builder().s(session.getId()).build());
        item.put("fecha", AttributeValue.builder().n(String.valueOf(session.getFecha())).build());
        item.put("alumnoId", AttributeValue.builder().n(String.valueOf(session.getAlumnoId())).build());
        item.put("active", AttributeValue.builder().bool(session.getActive()).build());
        item.put("sessionString", AttributeValue.builder().s(session.getSessionString()).build());

        PutItemRequest putItemRequest = PutItemRequest.builder()
                .tableName(tableName)
                .item(item)
                .build();

        dynamoDbClient.putItem(putItemRequest);
    }

    public Optional<Session> findSessionByString(String sessionString) {
        Map<String, AttributeValue> expressionAttributeValues = new HashMap<>();
        expressionAttributeValues.put(":sessionStr", AttributeValue.builder().s(sessionString).build());

        ScanRequest scanRequest = ScanRequest.builder()
                .tableName(tableName)
                .filterExpression("sessionString = :sessionStr")
                .expressionAttributeValues(expressionAttributeValues)
                .build();

        ScanResponse scanResponse = dynamoDbClient.scan(scanRequest);
        if (scanResponse.hasItems() && !scanResponse.items().isEmpty()) {
            Map<String, AttributeValue> item = scanResponse.items().get(0);
            Session session = new Session();
            session.setId(item.get("id").s());
            session.setFecha(Long.parseLong(item.get("fecha").n()));
            session.setAlumnoId(Long.parseLong(item.get("alumnoId").n()));
            session.setActive(item.get("active").bool());
            session.setSessionString(item.get("sessionString").s());
            return Optional.of(session);
        }
        return Optional.empty();
    }

    public boolean deactivateSession(String sessionString) {
        Optional<Session> sessionOpt = findSessionByString(sessionString);
        if (sessionOpt.isPresent()) {
            Session session = sessionOpt.get();
            
            Map<String, AttributeValue> key = new HashMap<>();
            key.put("id", AttributeValue.builder().s(session.getId()).build());

            Map<String, AttributeValueUpdate> attributeUpdates = new HashMap<>();
            attributeUpdates.put("active", AttributeValueUpdate.builder()
                    .value(AttributeValue.builder().bool(false).build())
                    .action(AttributeAction.PUT)
                    .build());

            UpdateItemRequest updateItemRequest = UpdateItemRequest.builder()
                    .tableName(tableName)
                    .key(key)
                    .attributeUpdates(attributeUpdates)
                    .build();

            dynamoDbClient.updateItem(updateItemRequest);
            return true;
        }
        return false;
    }
}
