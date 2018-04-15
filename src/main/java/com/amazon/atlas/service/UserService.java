package com.amazon.atlas.service;

import com.amazon.atlas.data.User;
import com.amazon.atlas.util.ClientHelper;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.document.*;
import com.amazonaws.services.dynamodbv2.document.spec.DeleteItemSpec;
import com.amazonaws.services.dynamodbv2.document.spec.UpdateItemSpec;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.amazonaws.services.dynamodbv2.model.ConditionalCheckFailedException;
import com.amazonaws.services.dynamodbv2.model.DeleteItemResult;
import com.amazonaws.services.dynamodbv2.model.ReturnValue;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

public class UserService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);
    private final String USER_TABLE_NAME = "Users";
    private DynamoDB m_dynamo = null;
    private static UserService mInstance = null;
    private Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static synchronized  UserService instance() {
        if(mInstance == null) {
            mInstance = new UserService();
        }

        return mInstance;
    }

    public Optional<User> create(String userId, String emailAddress, String password, boolean isAdmin) {
        assert userId != null;
        assert emailAddress != null;
        assert password != null;
        Optional<User> newUser;

        LOGGER.info("Creating user ({s}, {s}, {s}, {b}", userId, emailAddress, password, isAdmin);

        newUser = Optional.of(new User(userId, emailAddress, password, new Date(), isAdmin));

        try {
            Table userTable = m_dynamo.getTable(USER_TABLE_NAME);
            Item userItem = new Item()
                    .withPrimaryKey("Id", userId)
                    .withString("EmailAddress", emailAddress)
                    .withString("Password", password)
                    .withBoolean("IsAdmin", isAdmin)
                    .withString("LastUpdated", formatter.format(newUser.get().getLastUpdated()));

            userTable.putItem(userItem);
        } catch (Exception ex) {
            LOGGER.error("Caught exception committing user [{s}] to DynamoDB", emailAddress, ex);
            newUser.empty();
            return newUser;
        }

        LOGGER.info("User [{s}] created", emailAddress);
        return newUser;
    }

    public boolean update(String userId, String newPassword, boolean isAdmin) {
        assert userId != null;
        assert newPassword != null;

        LOGGER.info("Updating User {s} - pw={s} isAdmin={b}", userId, newPassword, isAdmin);
        try {
            Table userTable = m_dynamo.getTable(USER_TABLE_NAME);

            UpdateItemSpec updateItemSpec = null;
            UpdateItemOutcome outcome = null;

            updateItemSpec = new UpdateItemSpec()
                    .withPrimaryKey("Id", userId)
                    .withUpdateExpression("set Id = :user_id, Password = :pw, IsAdmin = :is_admin")
                    .withValueMap(new ValueMap()
                            .withString(":user_id", userId)
                            .withString(":pw", newPassword)
                            .withBoolean("is_admin", isAdmin)
                    )
                    .withReturnValues(ReturnValue.ALL_OLD);

            outcome = userTable.updateItem(updateItemSpec);
        }catch (ConditionalCheckFailedException ex) {
            if (ex.getErrorCode().equals("ConditionalCheckFailedException")) {
                LOGGER.info("User {s} could not be found to be updated", userId);
            }

            LOGGER.error("User {s} update failed [check failure]", userId, ex);
            return false;
        }catch (Exception ex) {
            LOGGER.error("User {s} update failed", userId, ex);

            return false;
        }

        LOGGER.info("User {s} - updated", userId);
        return true;
    }

    public Optional<User> find(String userId) {
        assert userId != null;
        Optional<User> foundUser;

        LOGGER.info("Finding user id {s}", userId);
        try {
            Table userTable = m_dynamo.getTable(USER_TABLE_NAME);

            Item item = userTable.getItem("Id", userId,
                    "UserId, EmailAddress, Password, IsAdmin", null);

            if(item != null) {
                ObjectMapper mapper = new ObjectMapper();
                String s = item.toJSONPretty();
                foundUser = Optional.of(mapper.readValue(item.toJSON(), User.class));
            } else {

                LOGGER.info("NULL user item returned");
                foundUser = Optional.empty();
            }

        } catch (Exception ex) {
            LOGGER.error("Caught exception while finding user {s}", userId, ex);
            foundUser = Optional.empty();
        }

        return foundUser;
    }

    public boolean delete(String userId) {
       assert userId != null;

        LOGGER.info("Deleting user ({s})", userId);

        DeleteItemOutcome result = null;
        try {
            Table userTable = m_dynamo.getTable(USER_TABLE_NAME);

            DeleteItemSpec deleteItemSpec = new DeleteItemSpec()
                    .withPrimaryKey("Id", userId)
                    .withConditionExpression("Id = :val")
                    .withValueMap(new ValueMap()
                            .withString(":val", userId))
                    .withReturnValues(ReturnValue.ALL_OLD);

            result = userTable.deleteItem(deleteItemSpec);
        } catch (Exception ex) {
            LOGGER.error("Caught exception removing user [{s}] to DynamoDB", userId, ex);
            return false;
        }

        LOGGER.info("User [{s}] deleted", userId);
        return true;
    }

    private UserService() {
        AmazonDynamoDBClient client = ClientHelper.instance().getDynamoClient();
        m_dynamo = new DynamoDB(client);
    }
}
