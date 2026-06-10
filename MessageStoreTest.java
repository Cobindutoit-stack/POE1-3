package com.mycompany.poepartone;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for MessageStore.java - Part 3, PROG5121 POE.
 *
 * Test data from the Part 3 spec:
 *   Message 1 : Sent      - +27834557896 - "Did you get the cake?"
 *   Message 2 : Stored    - +27838884567 - "Where are you? You are late! I have asked you to be on time."
 *   Message 3 : Disregard - +27834484567 - "Yohoooo, I am at your gate."
 *   Message 4 : Sent      - 0838884567   - "It is dinner time !"
 *   Message 5 : Stored    - +27838884567 - "Ok, I am leaving without you."
 *
 * @author Student
 */
public class MessageStoreTest {

    private MessageStore store;

    // Test data from spec
    private static final String ID_1   = "1234560001";
    private static final String CELL_1 = "+27834557896";
    private static final String MSG_1  = "Did you get the cake?";

    private static final String ID_2   = "1234560002";
    private static final String CELL_2 = "+27838884567";
    private static final String MSG_2  = "Where are you? You are late! I have asked you to be on time.";

    private static final String ID_3   = "1234560003";
    private static final String CELL_3 = "+27834484567";
    private static final String MSG_3  = "Yohoooo, I am at your gate.";

    private static final String ID_4   = "1234560004";
    private static final String CELL_4 = "0838884567";
    private static final String MSG_4  = "It is dinner time !";

    private static final String ID_5   = "1234560005";
    private static final String CELL_5 = "+27838884567";
    private static final String MSG_5  = "Ok, I am leaving without you.";

    // -----------------------------------------------
    // Helper - builds a Message object
    // -----------------------------------------------
    private Message buildMessage(String id, int num, String cell, String payload) {
        Message m = new Message();
        m.MessageDetails(id, num, cell, payload);
        return m;
    }

    // -----------------------------------------------
    // Helper - loads all five spec messages into the store
    // -----------------------------------------------
    private void populateAll() {
        store.addMessage(buildMessage(ID_1, 1, CELL_1, MSG_1), 1); // Sent
        store.addMessage(buildMessage(ID_2, 2, CELL_2, MSG_2), 3); // Stored
        store.addMessage(buildMessage(ID_3, 3, CELL_3, MSG_3), 2); // Disregard
        store.addMessage(buildMessage(ID_4, 4, CELL_4, MSG_4), 1); // Sent
        store.addMessage(buildMessage(ID_5, 5, CELL_5, MSG_5), 3); // Stored
    }

    @BeforeEach
    public void setUp() {
        store = new MessageStore();
    }

    // -----------------------------------------------
    // Sent array correctly populated
    // -----------------------------------------------
    @Test
    public void testSentArray_ContainsMsg1() {
        populateAll();
        assertEquals(MSG_1, store.getSentMessage(0));
    }

    @Test
    public void testSentArray_ContainsMsg4() {
        populateAll();
        assertEquals(MSG_4, store.getSentMessage(1));
    }

    @Test
    public void testSentArray_CountIsTwo() {
        populateAll();
        assertEquals(2, store.getSentCount());
    }

    // -----------------------------------------------
    // Display longest message
    // -----------------------------------------------
    @Test
    public void testDisplayLongestMessage_ReturnsMsg2() {
        populateAll();
        assertEquals(MSG_2, store.displayLongestMessage());
    }

    @Test
    public void testDisplayLongestMessage_EmptyStore() {
        assertEquals("No messages found.", store.displayLongestMessage());
    }

    // -----------------------------------------------
    // Search by message ID
    // -----------------------------------------------
    @Test
    public void testSearchByMessageID_Found() {
        populateAll();
        assertTrue(store.searchByMessageID(ID_2).contains(MSG_2));
    }

    @Test
    public void testSearchByMessageID_NotFound() {
        populateAll();
        assertEquals("Message ID not found.", store.searchByMessageID("9999999999"));
    }

    // -----------------------------------------------
    // Search by recipient
    // -----------------------------------------------
    @Test
    public void testSearchByRecipient_ContainsMsg2() {
        populateAll();
        assertTrue(store.searchByRecipient(CELL_2).contains(MSG_2));
    }

    @Test
    public void testSearchByRecipient_ContainsMsg5() {
        populateAll();
        assertTrue(store.searchByRecipient(CELL_2).contains(MSG_5));
    }

    @Test
    public void testSearchByRecipient_NoMatch() {
        populateAll();
        assertTrue(store.searchByRecipient("+27999999999").contains("No messages found"));
    }

    // -----------------------------------------------
    // Delete by hash
    // -----------------------------------------------
    @Test
    public void testDeleteByHash_ReturnsSuccessMessage() {
        populateAll();
        String hash = buildMessage(ID_2, 2, CELL_2, MSG_2).getMessageHash();
        assertTrue(store.deleteMessageByHash(hash).contains("successfully deleted"));
    }

    @Test
    public void testDeleteByHash_ReducesStoredCount() {
        populateAll();
        String hash = buildMessage(ID_2, 2, CELL_2, MSG_2).getMessageHash();
        store.deleteMessageByHash(hash);
        assertEquals(1, store.getStoredCount());
    }

    @Test
    public void testDeleteByHash_InvalidHash() {
        populateAll();
        assertTrue(store.deleteMessageByHash("XX:9:FAKEFAKE").contains("not found"));
    }

    // -----------------------------------------------
    // Display report
    // -----------------------------------------------
    @Test
    public void testDisplayReport_ContainsSentMessage() {
        populateAll();
        assertTrue(store.displayReport().contains(MSG_1));
    }

    @Test
    public void testDisplayReport_ContainsStoredMessage() {
        populateAll();
        assertTrue(store.displayReport().contains(MSG_2));
    }

    @Test
    public void testDisplayReport_EmptyReturnsNotice() {
        assertEquals("No messages to report.", store.displayReport());
    }

    // -----------------------------------------------
    // Total messages sent
    // -----------------------------------------------
    @Test
    public void testReturnTotalMessages_Two() {
        populateAll();
        assertEquals(2, store.returnTotalMessages());
    }

    @Test
    public void testReturnTotalMessages_Zero() {
        assertEquals(0, store.returnTotalMessages());
    }

    // -----------------------------------------------
    // Disregarded array
    // -----------------------------------------------
    @Test
    public void testDisregardedArray_ContainsMsg3() {
        populateAll();
        assertEquals(MSG_3, store.getDisregardedMessage(0));
    }
}
