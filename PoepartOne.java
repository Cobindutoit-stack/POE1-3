package com.mycompany.poepartone;
 
import java.util.Random;
import java.util.Scanner;
 
/**
 * Main entry point for PoeQuickChat.
 * Part 1 - Registration and Login
 * Part 2 - Sending Messages
 * Part 3 - Stored Data and Message Report
 *
 * @author Student
 */
public class PoepartOne {
 
    static Scanner input = new Scanner(System.in);
 
    public static void main(String[] args) {
 
        login        account = new login();
        MessageStore store   = new MessageStore();
 
        // Load any messages previously stored to JSON
        store.loadStoredMessages();
 
        // -----------------------------------------------
        // PART 1 - Registration
        // -----------------------------------------------
        System.out.println("=== Register ===");
 
        String firstname = getInput("First name: ");
        String lastname  = getInput("Last name: ");
 
        String userName = "";
        while (true) {
            userName = getInput("Username (must have '_', max 5 chars): ");
            account.LoginDetails(userName, "temp", firstname, lastname, "+27000000000");
            if (account.validUsername()) break;
        }
 
        String phoneNumber = "";
        while (true) {
            phoneNumber = getInput("Cell (+27, e.g. +27831234567): ");
            account.LoginDetails(userName, "temp", firstname, lastname, phoneNumber);
            if (account.validPhone()) break;
        }
 
        String passWord = "";
        while (true) {
            passWord = getInput("Password (8+ chars, capital, number, special char): ");
            account.LoginDetails(userName, passWord, firstname, lastname, phoneNumber);
            if (account.validPassword()) break;
        }
 
        String result = account.registerUser();
        System.out.println("\n" + result);
 
        // -----------------------------------------------
        // PART 1 - Login
        // -----------------------------------------------
        if (result.toLowerCase().contains("successfully captured")) {
 
            System.out.println("\n=== Login ===");
 
            boolean logged = false;
            while (!logged) {
                String loginUser = getInput("Username: ");
                String loginPass = getInput("Password: ");
                logged = account.LoginUser(loginUser, loginPass);
                System.out.println(account.returnLoginStatus(logged));
            }
 
            // -----------------------------------------------
            // PART 2 - Messaging menu
            // -----------------------------------------------
            System.out.println("\n==============================================");
            System.out.println("          Welcome to QuickChat.");
            System.out.println("==============================================");
 
            boolean running = true;
 
            while (running) {
 
                System.out.println("\n----------------------------------------------");
                System.out.println(" 1) Send Messages");
                System.out.println(" 2) Show recently sent messages");
                System.out.println(" 3) Stored Messages");
                System.out.println(" 4) Quit");
                System.out.println("----------------------------------------------");
                System.out.print("Your choice: ");
 
                int mainChoice = readInt();
 
                // -----------------------------------------------
                // Option 1 - Send messages
                // -----------------------------------------------
                switch (mainChoice) {
                    case 1 -> {
                        System.out.print("\nHow many messages do you want to send? ");
                        int numMessages = readInt();
                        for (int i = 1; i <= numMessages; i++) {
                            System.out.println("\n--- Message " + i + " of " + numMessages + " ---");
                            
                            Message msg       = new Message();
                            String  msgID     = generateID();
                            String  recipient = "";
                            
                            // Validate recipient
                            while (true) {
                                recipient = getInput("Recipient cell (e.g. +27831234567): ");
                                msg.MessageDetails(msgID, i, recipient, "test");
                                String check = msg.checkRecipientCell();
                                System.out.println(">> " + check);
                                if (check.equals("Cell phone number successfully captured.")) break;
                            }
                            
                            // Validate message length
                            String payload = "";
                            while (true) {
                                payload = getInput("Enter message (max 250 chars): ");
                                msg.MessageDetails(msgID, i, recipient, payload);
                                if (msg.checkMessageLength().equals("Message ready to send.")) break;
                                System.out.println(">> " + msg.checkMessageLength());
                            }
                            
                            // Finalise and display
                            msg.MessageDetails(msgID, i, recipient, payload);
                            System.out.println("\n-- Message Details --");
                            System.out.println(msg.printMessages());
                            
                            // Send / discard / store choice
                            System.out.println("What do you want to do?");
                            System.out.println(" 1) Send Message");
                            System.out.println(" 2) Disregard Message");
                            System.out.println(" 3) Store Message to send later");
                            System.out.print("Your choice: ");
                            
                            int    sendChoice = readInt();
                            String sendResult = msg.sentMessage(sendChoice);
                            System.out.println("\n>> " + sendResult);
                            
                            store.addMessage(msg, sendChoice);
                        }
                        System.out.println("\nTotal messages sent this session: " + store.returnTotalMessages());
                        // -----------------------------------------------
                        // Option 2 - Coming soon
                        // -----------------------------------------------
                    }
                    case 2 -> System.out.println("\n>> Coming Soon.");
                    // -----------------------------------------------
                    // PART 3 - Stored Messages menu
                    // -----------------------------------------------
                    case 3 -> {
                        boolean storeMenu = true;
                        while (storeMenu) {
                            System.out.println("\n--- Stored Messages Menu ---");
                            System.out.println(" a) Show all stored messages");
                            System.out.println(" b) Display longest message");
                            System.out.println(" c) Search by Message ID");
                            System.out.println(" d) Search messages by recipient");
                            System.out.println(" e) Delete a message by hash");
                            System.out.println(" f) Display message report");
                            System.out.println(" x) Back");
                            System.out.print("Your choice: ");
                            
                            String storeChoice = input.nextLine().trim().toLowerCase();
                            
                            switch (storeChoice) {
                                case "a" -> System.out.println("\n" + store.displayStoredSenderRecipient());
                                case "b" -> System.out.println("\nLongest message:\n\"" + store.displayLongestMessage() + "\"");
                                case "c" -> {
                                    String id = getInput("Enter Message ID: ");
                                    System.out.println("\n" + store.searchByMessageID(id));
                                }
                                case "d" -> {
                                    String rec = getInput("Enter recipient cell number: ");
                                    System.out.println("\n" + store.searchByRecipient(rec));
                                }
                                case "e" -> {
                                    String hash = getInput("Enter message hash to delete: ");
                                    System.out.println("\n" + store.deleteMessageByHash(hash));
                                }
                                case "f" -> System.out.println("\n" + store.displayReport());
                                case "x" -> storeMenu = false;
                                default -> System.out.println(">> Please enter a, b, c, d, e, f, or x.");
                            }   }
                        // -----------------------------------------------
                        // Option 4 - Quit
                        // -----------------------------------------------
                    }
                    case 4 -> {
                        System.out.println("\nGoodbye!");
                        running = false;
                    }
                    default -> System.out.println(">> Please enter 1, 2, 3, or 4.");
                }
            }
        }
 
        input.close();
    }
 
    // -----------------------------------------------
    // Generates a random 10-digit message ID
    // -----------------------------------------------
    public static String generateID() {
        Random rng = new Random();
        String id  = "" + (char)('1' + rng.nextInt(9));
        for (int i = 1; i < 10; i++) {
            id += rng.nextInt(10);
        }
        return id;
    }
 
    // -----------------------------------------------
    // Helpers
    // -----------------------------------------------
    public static String getInput(String prompt) {
        System.out.print(prompt);
        return input.nextLine();
    }
 
    public static int readInt() {
        try {
            return Integer.parseInt(input.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}