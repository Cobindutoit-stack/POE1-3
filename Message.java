/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.poepartone;

/**
 * Message class - handles validation and display of a single message.
 * Part 2 - PROG5121
 *
 * @author Student
 */
public class Message {

    
    private String messageID;
    private int    messageNumber;
    private String recipient;
    private String messagePayload;
    private String messageHash;

    // -----------------------------------------------
    // Setter - mirrors LoginDetails() in login
    // -----------------------------------------------
    public void MessageDetails(String messageID, int messageNumber, String recipient, String messagePayload) {
        this.messageID      = messageID;
        this.messageNumber  = messageNumber;
        this.recipient      = recipient;
        this.messagePayload = messagePayload;
        this.messageHash    = createMessageHash();
    }

    // -----------------------------------------------
    // Checks message ID is 10 characters or fewer
    // -----------------------------------------------
    public boolean checkMessageID() {
        return messageID != null && messageID.length() <= 10;
    }

    // -----------------------------------------------
    // Checks recipient has international code (+27, 12 chars)
    // -----------------------------------------------
    public String checkRecipientCell() {
        if (recipient != null && recipient.startsWith("+27") && recipient.length() == 12) {
            return "Cell phone number successfully captured.";
        }
        return "Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.";
    }

    // -----------------------------------------------
    // Checks message is 250 characters or fewer
    // -----------------------------------------------
    public String checkMessageLength() {
        int len = messagePayload.length();
        if (len <= 250) {
            return "Message ready to send.";
        }
        return "Message exceeds 250 characters by " + (len - 250) + "; please reduce the size.";
    }

    // -----------------------------------------------
    // Creates hash: IDPREFIX:NUMBER:FIRSTLAST (caps)
    // Example: 00:1:HITONIGHT
    // -----------------------------------------------
    public String createMessageHash() {
        if (messageID == null || messagePayload == null || messagePayload.trim().isEmpty()) {
            return "";
        }
        String   idPrefix  = messageID.substring(0, 2);
        String[] words     = messagePayload.trim().split("\\s+");
        String   firstWord = words[0];
        String   lastWord  = words[words.length - 1].replaceAll("[^a-zA-Z0-9]", "");
        return (idPrefix + ":" + messageNumber + ":" + firstWord + lastWord).toUpperCase();
    }

    // -----------------------------------------------
    // Handles send / discard / store choice
    // 1 = Send, 2 = Disregard, 3 = Store
    // -----------------------------------------------
    public String sentMessage(int choice) {
        switch (choice) {
            case 1  -> { return "Message successfully sent."; }
            case 2  -> { return "Press 0 to delete the message."; }
            case 3  -> { return "Message successfully stored."; }
            default -> { return "Invalid option."; }
        }
    }

    // -----------------------------------------------
    // Returns full message details as a formatted string
    // -----------------------------------------------
    public String printMessages() {
        return "Message ID   : " + messageID     + "\n"
             + "Message Hash : " + messageHash   + "\n"
             + "Recipient    : " + recipient      + "\n"
             + "Message      : " + messagePayload + "\n";
    }

    // -----------------------------------------------
    // Getters
    // -----------------------------------------------
    public String getMessageID()      { return messageID; }
    public int    getMessageNumber()  { return messageNumber; }
    public String getRecipient()      { return recipient; }
    public String getMessagePayload() { return messagePayload; }
    public String getMessageHash()    { return messageHash; }
}