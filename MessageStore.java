package com.mycompany.poepartone;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;

/**
 * MessageStore - handles storing and managing messages for Part 3.
 *
 * @author Student
 */
public class MessageStore {

    // Max messages we can hold
    private static final int MAX = 50;

    // Sent arrays
    private final String[] sentMessages   = new String[MAX];
    private final String[] sentRecipients = new String[MAX];
    private final String[] sentHashes     = new String[MAX];
    private int      sentCount      = 0;

    // Stored arrays
    private final String[] storedMessages   = new String[MAX];
    private final String[] storedRecipients = new String[MAX];
    private final String[] storedHashes     = new String[MAX];
    private final String[] storedIDs        = new String[MAX];
    private int      storedCount      = 0;

    // Disregarded arrays
    private final String[] disregardedMessages = new String[MAX];
    private int      disregardedCount    = 0;

    private static final String STORE_FILE = "stored_messages.txt";

    // -----------------------------------------------
    // Add a message based on user choice
    // 1 = Send, 2 = Disregard, 3 = Store
    // -----------------------------------------------
    public void addMessage(Message msg, int choice) {
        switch (choice) {
            case 1 -> {
                sentMessages[sentCount]   = msg.getMessagePayload();
                sentRecipients[sentCount] = msg.getRecipient();
                sentHashes[sentCount]     = msg.getMessageHash();
                sentCount++;
            }
            case 2 -> {
                disregardedMessages[disregardedCount] = msg.getMessagePayload();
                disregardedCount++;
            }
            case 3 -> {
                storedMessages[storedCount]   = msg.getMessagePayload();
                storedRecipients[storedCount] = msg.getRecipient();
                storedHashes[storedCount]     = msg.getMessageHash();
                storedIDs[storedCount]        = msg.getMessageID();
                storedCount++;
                storeMessage(msg);
            }
            default -> {
            }
        }
    }

    // -----------------------------------------------
    // a) Show all stored messages with their recipient
    // -----------------------------------------------
    public String displayStoredSenderRecipient() {
        if (storedCount == 0) {
            return "No stored messages found.";
        }
        String result = "--- Stored Messages ---\n";
        for (int i = 0; i < storedCount; i++) {
            result += (i + 1) + ") Recipient: " + storedRecipients[i]
                    + " | Message: " + storedMessages[i] + "\n";
        }
        return result;
    }

    // -----------------------------------------------
    // b) Return the longest message across all arrays
    // -----------------------------------------------
    public String displayLongestMessage() {
        String longest = "";

        for (int i = 0; i < sentCount; i++) {
            if (sentMessages[i] != null && sentMessages[i].length() > longest.length()) {
                longest = sentMessages[i];
            }
        }
        for (int i = 0; i < storedCount; i++) {
            if (storedMessages[i] != null && storedMessages[i].length() > longest.length()) {
                longest = storedMessages[i];
            }
        }
        for (int i = 0; i < disregardedCount; i++) {
            if (disregardedMessages[i] != null && disregardedMessages[i].length() > longest.length()) {
                longest = disregardedMessages[i];
            }
        }

        if (longest.equals("")) {
            return "No messages found.";
        }
        return longest;
    }

    // -----------------------------------------------
    // c) Search for a message by its ID
    // -----------------------------------------------
    public String searchByMessageID(String id) {
        for (int i = 0; i < storedCount; i++) {
            if (storedIDs[i].equals(id)) {
                return "Recipient: " + storedRecipients[i]
                     + " | Message: " + storedMessages[i];
            }
        }
        return "Message ID not found.";
    }

    // -----------------------------------------------
    // d) Search all messages sent to a particular recipient
    // -----------------------------------------------
    public String searchByRecipient(String recipient) {
        String result = "";

        for (int i = 0; i < sentCount; i++) {
            if (sentRecipients[i].equals(recipient)) {
                result += "[Sent] " + sentMessages[i] + "\n";
            }
        }
        for (int i = 0; i < storedCount; i++) {
            if (storedRecipients[i].equals(recipient)) {
                result += "[Stored] " + storedMessages[i] + "\n";
            }
        }

        if (result.equals("")) {
            return "No messages found for recipient: " + recipient;
        }
        return result.trim();
    }

    // -----------------------------------------------
    // e) Delete a stored message using its hash
    // -----------------------------------------------
    public String deleteMessageByHash(String hash) {
        for (int i = 0; i < storedCount; i++) {
            if (storedHashes[i].equals(hash)) {
                String deleted = storedMessages[i];

                // Shift everything down to fill the gap
                for (int j = i; j < storedCount - 1; j++) {
                    storedMessages[j]   = storedMessages[j + 1];
                    storedRecipients[j] = storedRecipients[j + 1];
                    storedHashes[j]     = storedHashes[j + 1];
                    storedIDs[j]        = storedIDs[j + 1];
                }
                storedCount--;
                return "Message: \"" + deleted + "\" successfully deleted.";
            }
        }
        return "Message with hash \"" + hash + "\" not found.";
    }

    // -----------------------------------------------
    // f) Display a full report of all sent and stored messages
    // -----------------------------------------------
    public String displayReport() {
        if (sentCount == 0 && storedCount == 0) {
            return "No messages to report.";
        }

        String report = "============================================\n";
        report       += "        QUICKCHAT MESSAGE REPORT\n";
        report       += "============================================\n";

        if (sentCount > 0) {
            report += "\n--- SENT MESSAGES ---\n";
            for (int i = 0; i < sentCount; i++) {
                report += "Message Hash : " + sentHashes[i]     + "\n";
                report += "Recipient    : " + sentRecipients[i] + "\n";
                report += "Message      : " + sentMessages[i]   + "\n";
                report += "--------------------------------------------\n";
            }
        }

        if (storedCount > 0) {
            report += "\n--- STORED MESSAGES ---\n";
            for (int i = 0; i < storedCount; i++) {
                report += "Message Hash : " + storedHashes[i]     + "\n";
                report += "Recipient    : " + storedRecipients[i] + "\n";
                report += "Message      : " + storedMessages[i]   + "\n";
                report += "--------------------------------------------\n";
            }
        }

        report += "============================================\n";
        return report;
    }

    // -----------------------------------------------
    // Save a message to the text file
    // Each message is saved as four lines: ID, hash, recipient, message
    // -----------------------------------------------
    public void storeMessage(Message msg) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(STORE_FILE, true))) {
            writer.println(msg.getMessageID());
            writer.println(msg.getMessageHash());
            writer.println(msg.getRecipient());
            writer.println(msg.getMessagePayload());
        } catch (Exception e) {
            System.out.println("Could not save message: " + e.getMessage());
        }
    }

    // -----------------------------------------------
    // Load stored messages back from the text file on startup
    // -----------------------------------------------
    public void loadStoredMessages() {
        try (BufferedReader reader = new BufferedReader(new FileReader(STORE_FILE))) {
            String id;
            while ((id = reader.readLine()) != null) {
                storedIDs[storedCount]        = id;
                storedHashes[storedCount]     = reader.readLine();
                storedRecipients[storedCount] = reader.readLine();
                storedMessages[storedCount]   = reader.readLine();
                storedCount++;
            }
            System.out.println("Loaded " + storedCount + " stored message(s) from file.");
        } catch (Exception e) {
            // No file to load, that's fine
        }
    }

    // -----------------------------------------------
    // Getters
    // -----------------------------------------------
    public int getSentCount()             { return sentCount; }
    public int getStoredCount()           { return storedCount; }
    public int getDisregardedCount()      { return disregardedCount; }

    public String getSentMessage(int i)        { return sentMessages[i]; }
    public String getStoredMessage(int i)      { return storedMessages[i]; }
    public String getStoredHash(int i)         { return storedHashes[i]; }
    public String getDisregardedMessage(int i) { return disregardedMessages[i]; }

    public int returnTotalMessages()      { return sentCount; }
}
