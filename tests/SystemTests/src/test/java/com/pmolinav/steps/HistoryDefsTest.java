//package com.pmolinav.steps;
//
//import io.cucumber.java.en.Then;
//import com.pmolinav.userslib.model.History;
//import com.pmolinav.database.UsersDatabaseConnector;
//
//import java.sql.SQLException;
//import java.util.List;
//
//import static org.junit.Assert.*;
//
//public class HistoryDefsTest extends BaseSystemTest {
//
//    @Then("an entity (.*) with id (.*) has been stored into historical by user (.*) and with type (.*)$")
//    public void aBookingHasBeenStored(String entity, String entityId, String user, String type) {
//        try {
//            dbConnector = new UsersDatabaseConnector();
//            List<History> histories = dbConnector.getHistoriesByEntityUserAndType(entity, user, type);
//            assertNotNull(histories);
//            if (!entityId.equalsIgnoreCase("N/A")) {
//                assertTrue(histories.stream().anyMatch(history -> history.getEntityId().equals(entityId)));
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//            fail();
//        }
//    }
//
//}