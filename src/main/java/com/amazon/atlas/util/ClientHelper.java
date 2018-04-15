package com.amazon.atlas.util;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;

/**
 * Created by Jeff on 12/6/16.
 */
public class ClientHelper {
    private static ClientHelper m_instance = null;

    private ClientHelper() {
    }

    public synchronized static ClientHelper instance() {

        if(m_instance == null) {
            m_instance = new ClientHelper();
        }

        return m_instance;
    }

    /*
    public MongoClient getMongoClient() {
        MongoClientURI uri = new MongoClientURI(MONGO_CONN_URI);
        MongoClient client = new MongoClient(uri);

        return client;
    }
    */
    public AmazonDynamoDBClient getDynamoClient() {

        AmazonDynamoDBClient client = new AmazonDynamoDBClient(new ProfileCredentialsProvider());
        client.setRegion(Region.getRegion(Regions.US_EAST_1));

        return client;
    }

    /*
    public AmazonDynamoDB getDAXClient(String clusterUrl) {

        AmazonDaxClientBuilder daxClientBuilder = AmazonDaxClientBuilder.standard();
        daxClientBuilder.withRegion(Regions.US_EAST_1).withEndpointConfiguration(clusterUrl);
        AmazonDynamoDB client = daxClientBuilder.build();

        return client;
    }
    */
}
