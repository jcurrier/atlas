package com.amazon.atlas.service;

import com.amazon.atlas.data.User;
import com.amazon.atlas.exceptions.InternalServiceException;
import com.amazon.atlas.exceptions.NotFoundException;
import com.amazon.atlas.exceptions.ObjectExistsException;
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
import org.omg.CORBA.INTERNAL;
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

    public User create(String userId, String emailAddress, String password, boolean isAdmin)
        throws ObjectExistsException, InternalServiceException {
        assert userId != null;
        assert emailAddress != null;
        assert password != null;
        User newUser;

        LOGGER.info("Creating user ({s}, {s}, {s}, {b}", userId, emailAddress, password, isAdmin);

        if(checkIfUserExists(userId, emailAddress)) {
            LOGGER.error("User ({%s}, {s}) already exists");
            throw new ObjectExistsException(String.format("User {s} already exists", emailAddress));
        }

        newUser = new User(userId, emailAddress, password, new Date(), isAdmin);

        try {
            Table userTable = m_dynamo.getTable(USER_TABLE_NAME);
            Item userItem = new Item()
                    .withPrimaryKey("Id", userId)
                    .withString("EmailAddress", emailAddress)
                    .withString("Password", password)
                    .withBoolean("IsAdmin", isAdmin)
                    .withString("LastUpdated", formatter.format(newUser.getLastUpdated()));

            userTable.putItem(userItem);
        } catch (Exception ex) {
            LOGGER.error("Caught exception committing user [{s}] to DynamoDB", emailAddress, ex);
            throw new InternalServiceException(String.format("Failed to create user {s}", emailAddress));
        }

        LOGGER.info("User [{s}] created", emailAddress);
        return newUser;
    }

    public void update(String userId, String newPassword, boolean isAdmin)
        throws NotFoundException, InternalServiceException {
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
            LOGGER.info("User {s} - updated", userId);
        }catch (ConditionalCheckFailedException ex) {
            if (ex.getErrorCode().equals("ConditionalCheckFailedException")) {
                LOGGER.info("User {s} could not be found to be updated", userId);
                throw new NotFoundException(String.format("User {s} not found", userId), ex);
            }

            LOGGER.error("User {s} update failed [check failure]", userId, ex);
            throw new InternalServiceException(String.format("Update on user {s} failed", userId), ex);
        }catch (Exception ex) {
            LOGGER.error("User {s} update failed", userId, ex);
            throw new InternalServiceException(String.format("Update on user {s} failed", userId), ex);
        }
    }

    public Optional<User> find(String userId) throws InternalServiceException {
        assert userId != null;
        Optional<User> foundUser;

        LOGGER.info("Finding user id {" + userId + "}");
        try {
            Table userTable = m_dynamo.getTable(USER_TABLE_NAME);

            Item item = userTable.getItem("Id", userId,
                    "Id, EmailAddress, Password, LastUpdated, IsAdmin", null);

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
            throw new InternalServiceException(String.format("Internal error searching for User - {s}", userId), ex);
        }

        return foundUser;
    }

    public void delete(String userId) throws NotFoundException, InternalServiceException {
       assert userId != null;

        LOGGER.info(String.format("Deleting user {%s}", userId));

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
            LOGGER.info("User [{s}] deleted", userId);
        } catch(ConditionalCheckFailedException ex) {
            if (ex.getErrorCode().equals("ConditionalCheckFailedException")) {
                LOGGER.info("User {s} could not be found", userId);
                throw new NotFoundException(String.format("User {s} not found", userId), ex);
            }

            throw new InternalServiceException(String.format("Internal error deleting User - {s}", userId), ex);
        } catch(Exception ex) {
            LOGGER.error("Caught exception removing user [{s}] to DynamoDB", userId, ex);
            throw new InternalServiceException(String.format("Internal error deleting User - {s}", userId), ex);
        }
    }

    private boolean checkIfUserExists(String userId, String emailAddress) {
        boolean verdict = false;

        try {
            Table userTable = m_dynamo.getTable(USER_TABLE_NAME);

            Item item = userTable.getItem("Id", userId,
                    "Id, EmailAddress", null);

            if(item != null) {
                // NOTE: It's possible that two users could exist with the same email address.
                //       However, I'm keeping a separate Id apart from email to make it easier for users to change
                //       their email address. Therefore, treat the existance of another user with the same email as
                //       as being from the same user.
                verdict =  true;
            }

        } catch (Exception ex) {
            LOGGER.error("Caught exception while checking if user {s} exists", userId, ex);
        }

        return verdict;
    }

    private UserService() {
        AmazonDynamoDBClient client = ClientHelper.instance().getDynamoClient();
        m_dynamo = new DynamoDB(client);
    }
}
