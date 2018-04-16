package com.amazon.atlas.service;

import com.amazon.atlas.data.Project;
import com.amazon.atlas.data.User;
import com.amazon.atlas.exceptions.InternalServiceException;
import com.amazon.atlas.exceptions.NotFoundException;
import com.amazon.atlas.exceptions.ObjectExistsException;
import com.amazon.atlas.util.ClientHelper;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.document.*;
import com.amazonaws.services.dynamodbv2.document.spec.DeleteItemSpec;
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.amazonaws.services.dynamodbv2.model.ConditionalCheckFailedException;
import com.amazonaws.services.dynamodbv2.model.ReturnValue;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class ProjectService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProjectService.class);
    private final String PROJECT_TABLE_NAME = "Projects";
    private DynamoDB m_dynamo = null;
    private Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private static ProjectService mInstance = null;

    public synchronized static ProjectService instance() {

       if(mInstance == null) {
           mInstance = new ProjectService();
       }

       return mInstance;
    }

    public Project create(String name, String description, String ownerId)
            throws ObjectExistsException, InternalServiceException {
        assert name != null;
        assert ownerId != null;

        LOGGER.info("Creating project ({s}, {s}, {s}", name, description, ownerId);

        Optional<Project> projectCheck = find(name);
        if(projectCheck.isPresent()) {
            LOGGER.error("Project {s} already exists", name);
            throw new ObjectExistsException(String.format("Project {s} already exists", name));
        }

        Project newProject = new Project(name, description, ownerId);

        try {
            Table projectTable = m_dynamo.getTable(PROJECT_TABLE_NAME);
            Item projectItem = new Item()
                    .withPrimaryKey("Id", newProject.getId())
                    .withString("Name", newProject.getDescription())
                    .withString("Description", newProject.getDescription())
                    .withString("Owner", newProject.getOwner())
                    .withString("CreatedOn", formatter.format(newProject.getCreatedOn()))
                    .withString("LastUpdated", formatter.format(newProject.getLastUpdated()));

            projectTable.putItem(projectItem);
        } catch (Exception ex) {
            LOGGER.error("Caught exception committing project [{s}] to DynamoDB", name, ex);
            throw new InternalServiceException(String.format("Failed to create project {s}", name));
        }

        LOGGER.info("project [{s}] created", name);
        return newProject;
    }

    public Optional<Project> find(String projectName) throws InternalServiceException {
        assert(projectName != null);
        Optional<Project> foundProject;

        LOGGER.info("Finding project [{s}]", projectName);
        try {
            Table projectTable = m_dynamo.getTable(PROJECT_TABLE_NAME);

            Item item = projectTable.getItem("Name", projectName,
                    "Id, Name, Description, Owner, CreatedOn, LastUpdated", null);

            if(item != null) {
                ObjectMapper mapper = new ObjectMapper();
                String s = item.toJSONPretty();
                foundProject = Optional.of(mapper.readValue(item.toJSON(), Project.class));
            } else {
                LOGGER.info("NULL project item returned");
                foundProject = Optional.empty();
            }

        } catch (Exception ex) {
            LOGGER.error("Caught exception while finding project [{s}]", projectName, ex);
            throw new InternalServiceException(String.format("Internal error searching for Project - [{s}]", projectName), ex);
        }

        return foundProject;
    }

    public List<Project> getProjectsByUser(String userId) {
        assert userId != null;

        return null;
    }

    public void delete(String projectId) throws NotFoundException, InternalServiceException {
        assert projectId != null;

        LOGGER.info("Deleting project ({s})", projectId);

        DeleteItemOutcome result = null;
        try {
            Table projectTable = m_dynamo.getTable(PROJECT_TABLE_NAME);

            DeleteItemSpec deleteItemSpec = new DeleteItemSpec()
                    .withPrimaryKey("Id", projectId)
                    .withConditionExpression("Id = :val")
                    .withValueMap(new ValueMap()
                            .withString(":val", projectId))
                    .withReturnValues(ReturnValue.ALL_OLD);

            result = projectTable.deleteItem(deleteItemSpec);
            LOGGER.info("Project [{s}] deleted", projectId);
        } catch(ConditionalCheckFailedException ex) {
            if (ex.getErrorCode().equals("ConditionalCheckFailedException")) {
                LOGGER.info("Project {s} could not be found", projectId);
                throw new NotFoundException(String.format("Project [{s}] not found", projectId), ex);
            }

            throw new InternalServiceException(String.format("Internal error deleting project - [{s}]", projectId), ex);
        } catch(Exception ex) {
            LOGGER.error("Caught exception removing project [{s}] to DynamoDB", projectId, ex);
            throw new InternalServiceException(String.format("Internal error deleting project - [{s}]", projectId), ex);
        }
    }

    private ProjectService() {
        AmazonDynamoDBClient client = ClientHelper.instance().getDynamoClient();
        m_dynamo = new DynamoDB(client);
    }
}
