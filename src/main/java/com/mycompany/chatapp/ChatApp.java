/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.chatapp;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Scanner;

/**
 *
 * ST10508156 N.Panyapanya
 */
public class ChatApp {

    private static final Path MESSAGE_FILE = Path.of("messages.json");
    private static final int ARRAY_SIZE = 100;

    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);

        System.out.println("=== REGISTRATION ===");

        System.out.print("Enter your first name: ");
        String firstName = input.nextLine();

        System.out.print("Enter your last name: ");
        String lastName = input.nextLine();

        System.out.print("Enter username: ");
        String username = input.nextLine();

        System.out.print("Enter password: ");
        String password = input.nextLine();

        System.out.print("Enter cellphone number: ");
        String cellPhoneNumber = input.nextLine();

        Login user = new Login(
                firstName,
                lastName,
                username,
                password,
                cellPhoneNumber
        );

        String registrationResult = user.registerUser();

        System.out.println(registrationResult);

        if ("User registered successfully.".equals(registrationResult)) {

            System.out.println("\n=== LOGIN ===");

            System.out.print("Enter username: ");
            String loginUsername = input.nextLine();

            System.out.print("Enter password: ");
            String loginPassword = input.nextLine();

            boolean loginSuccessful =
                    user.loginUser(loginUsername, loginPassword);

            System.out.println(user.returnLoginStatus());

            if (loginSuccessful) {

                String senderName = firstName + " " + lastName;

                runQuickChat(input, senderName);
            }
        }

        input.close();
    }

    // Main QuickChat application.
    public static void runQuickChat(
            Scanner input, String senderName) {

        Message[] sentMessages = new Message[ARRAY_SIZE];
        Message[] disregardedMessages = new Message[ARRAY_SIZE];
        Message[] storedMessages = new Message[ARRAY_SIZE];

        // These arrays stay in the same order as allMessages.
        Message[] allMessages = new Message[ARRAY_SIZE];
        String[] messageHashes = new String[ARRAY_SIZE];
        String[] messageIDs = new String[ARRAY_SIZE];

        int sentCount = 0;
        int disregardedCount = 0;
        int storedCount = loadStoredMessages(storedMessages);
        int allCount = 0;
        int messageNumber = 1;

        // Populate the parallel arrays with messages already stored.
        allCount = rebuildParallelArrays(
                sentMessages, sentCount,
                disregardedMessages, disregardedCount,
                storedMessages, storedCount,
                allMessages, messageIDs, messageHashes
        );

        boolean running = true;

        while (running) {

            System.out.println("\n========== QUICKCHAT ==========");
            System.out.println("1. Send Messages");
            System.out.println("2. Stored Messages");
            System.out.println("3. Quit");

            System.out.print("Choose an option: ");

            String choice = input.nextLine();

            switch (choice) {

                case "1":

                    int numberOfMessages = getNumberOfMessages(input);

                    for (int i = 0; i < numberOfMessages; i++) {

                        if (allCount >= ARRAY_SIZE) {
                            System.out.println("The message array is full.");
                            break;
                        }

                        System.out.println("\nMessage " + (i + 1));

                        String recipient = getRecipient(input);
                        String messageText = getMessageText(input);

                        Message message = new Message(
                                messageNumber,
                                senderName,
                                recipient,
                                messageText
                        );

                        messageNumber++;

                        // Add the message to the parallel arrays.
                        allMessages[allCount] = message;
                        messageIDs[allCount] = message.getMessageID();
                        messageHashes[allCount] = message.getMessageHash();

                        allCount++;

                        System.out.println("\nMessage ID: "
                                + message.getMessageID());

                        System.out.println("Message Hash: "
                                + message.getMessageHash());

                        System.out.println("\nChoose what to do:");
                        System.out.println("1. Send Message");
                        System.out.println("2. Disregard Message");
                        System.out.println("3. Store Message");

                        System.out.print("Enter your choice: ");

                        String action = input.nextLine();

                        switch (action) {

                            case "1":

                                message.sendMessage();

                                sentMessages[sentCount] = message;
                                sentCount++;

                                System.out.println(
                                        "Message successfully sent."
                                );

                                message.displayMessage();

                                break;

                            case "2":

                                System.out.println(
                                        message.disregardMessage()
                                );

                                System.out.print(
                                        "Enter 0 to disregard the message: "
                                );

                                String discardChoice = input.nextLine();

                                if ("0".equals(discardChoice)) {

                                    disregardedMessages[disregardedCount]
                                            = message;

                                    disregardedCount++;

                                    System.out.println(
                                            "Message disregarded successfully."
                                    );

                                } else {

                                    System.out.println(
                                            "Message was not disregarded."
                                    );
                                }

                                break;

                            case "3":

                                String result =
                                        message.storeMessage(MESSAGE_FILE);

                                System.out.println(result);

                                storedCount =
                                        loadStoredMessages(storedMessages);

                                // Rebuild all arrays after storing.
                                allCount = rebuildParallelArrays(
                                        sentMessages, sentCount,
                                        disregardedMessages, disregardedCount,
                                        storedMessages, storedCount,
                                        allMessages, messageIDs, messageHashes
                                );

                                break;

                            default:

                                System.out.println(
                                        "Invalid option. Message not sent."
                                );
                        }
                    }

                    System.out.println(
                            "\nTotal messages sent: "
                            + sentCount
                    );

                    break;

               
                
                case "2":

                    storedCount = loadStoredMessages(storedMessages);

        allCount = rebuildParallelArrays(
                sentMessages, sentCount,
                disregardedMessages, disregardedCount,
                storedMessages, storedCount,
                allMessages, messageIDs, messageHashes
        );

        storedCount = storedMessagesMenu(
                input,
                sentMessages,
                sentCount,
                disregardedMessages,
                disregardedCount,
                storedMessages,
                storedCount,
                allMessages,
                messageIDs,
                messageHashes,
                allCount
        );

        storedCount = loadStoredMessages(storedMessages);

        allCount = rebuildParallelArrays(
                sentMessages, sentCount,
                disregardedMessages, disregardedCount,
                storedMessages, storedCount,
                allMessages, messageIDs, messageHashes
        );

        break;
        
        case "3":

                    running = false;

                    System.out.println(
                            "Thank you for using Quick Chat."
                    );

                    break;

                default:

                    System.out.println(
                            "Invalid option. Please try again."
                    );
            }
        }

        System.out.println("\n========== SESSION SUMMARY ==========");
        System.out.println("Sent messages: " + sentCount);
        System.out.println("Disregarded messages: " + disregardedCount);
        System.out.println("Stored messages: " + storedCount);
    }

    // Gets the number of messages to process.
    private static int getNumberOfMessages(Scanner input) {

        while (true) {

            System.out.print(
                    "How many messages would you like to process? "
            );

            String answer = input.nextLine();

            try {

                int number = Integer.parseInt(answer);

                if (number > 0 && number <= ARRAY_SIZE) {
                    return number;
                }

                System.out.println(
                        "Please enter a number between 1 and "
                        + ARRAY_SIZE + "."
                );

            } catch (NumberFormatException exception) {

                System.out.println("Please enter a valid number.");
            }
        }
    }

    // Validates the recipient number.
    private static String getRecipient(Scanner input) {

        while (true) {

            System.out.print("Enter recipient number (+27XXXXXXXXX): ");

            String recipient = input.nextLine();

            if (Message.isValidRecipient(recipient)) {
                return recipient;
            }

            System.out.println(
                    "Invalid recipient number. Please use +27 "
                    + "followed by 9 digits."
            );
        }
    }

    // Validates the message text.
    private static String getMessageText(Scanner input) {

        while (true) {

            System.out.print("Enter your message: ");

            String message = input.nextLine();

            if (message.isBlank()) {

                System.out.println("Message cannot be empty.");

            } else if (message.length() > 250) {

                System.out.println(
                        "Message exceeds 250 characters by "
                        + (message.length() - 250)
                        + "; please reduce the size."
                );

            } else {

                return message;
            }
        }
    }

    // Reads messages from JSON into the storedMessages array.
    private static int loadStoredMessages(Message[] storedMessages) {

        for (int i = 0; i < storedMessages.length; i++) {
            storedMessages[i] = null;
        }

        try {

            java.util.List<Message> messages =
                    Message.readMessages(MESSAGE_FILE);

            int count = Math.min(
                    messages.size(),
                    storedMessages.length
            );

            for (int i = 0; i < count; i++) {
                storedMessages[i] = messages.get(i);
            }

            return count;

        } catch (IOException exception) {

            System.out.println(
                    "Could not read messages.json: "
                    + exception.getMessage()
            );

            return 0;
        }
    }

    // Populates the message, ID and hash arrays in matching positions.
    private static int rebuildParallelArrays(
            Message[] sentMessages,
            int sentCount,
            Message[] disregardedMessages,
            int disregardedCount,
            Message[] storedMessages,
            int storedCount,
            Message[] allMessages,
            String[] messageIDs,
            String[] messageHashes) {

        for (int i = 0; i < allMessages.length; i++) {
            allMessages[i] = null;
            messageIDs[i] = null;
            messageHashes[i] = null;
        }

        int count = 0;

        // Add sent messages.
        for (int i = 0; i < sentCount && count < ARRAY_SIZE; i++) {

            if (sentMessages[i] != null) {

                count = addToParallelArrays(
                        sentMessages[i],
                        allMessages,
                        messageIDs,
                        messageHashes,
                        count
                );
            }
        }

        // Add disregarded messages.
        for (int i = 0; i < disregardedCount && count < ARRAY_SIZE; i++) {

            if (disregardedMessages[i] != null) {

                count = addToParallelArrays(
                        disregardedMessages[i],
                        allMessages,
                        messageIDs,
                        messageHashes,
                        count
                );
            }
        }

        // Add stored messages.
        for (int i = 0; i < storedCount && count < ARRAY_SIZE; i++) {

            if (storedMessages[i] != null) {

                count = addToParallelArrays(
                        storedMessages[i],
                        allMessages,
                        messageIDs,
                        messageHashes,
                        count
                );
            }
        }

        return count;
    }

    // Adds one message and its ID and hash at the same array position.
    private static int addToParallelArrays(
            Message message,
            Message[] allMessages,
            String[] messageIDs,
            String[] messageHashes,
            int count) {

        // Avoid adding the same message twice.
        for (int i = 0; i < count; i++) {

            if (allMessages[i] != null
                    && allMessages[i].getMessageID()
                            .equals(message.getMessageID())) {

                return count;
            }
        }

        if (count < ARRAY_SIZE) {

            allMessages[count] = message;
            messageIDs[count] = message.getMessageID();
            messageHashes[count] = message.getMessageHash();

            count++;
        }

        return count;
    }

    // Stored Messages submenu.
    private static int storedMessagesMenu(
            Scanner input,
            Message[] sentMessages,
            int sentCount,
            Message[] disregardedMessages,
            int disregardedCount,
            Message[] storedMessages,
            int storedCount,
            Message[] allMessages,
            String[] messageIDs,
            String[] messageHashes,
            int allCount) {

        boolean inMenu = true;

        while (inMenu) {

            System.out.println("\n========== STORED MESSAGES ==========");
            System.out.println("1. Display sender and recipient");
            System.out.println("2. Display longest stored message");
            System.out.println("3. Search for a message ID");
            System.out.println("4. Search messages by recipient");
            System.out.println("5. Delete a message using its hash");
            System.out.println("6. Display message report");
            System.out.println("7. Return to main menu");

            System.out.print("Choose an option: ");

            String choice = input.nextLine();

            switch (choice) {

                case "1":

                    displaySenderAndRecipient(
                            storedMessages,
                            storedCount
                    );

                    break;

                case "2":

                    displayLongestMessage(
                            storedMessages,
                            storedCount
                    );

                    break;

                case "3":

                    searchByMessageID(
                            input,
                            allMessages,
                            messageIDs,
                            allCount
                    );

                    break;

                case "4":

                    searchByRecipient(
                            input,
                            sentMessages,
                            sentCount,
                            storedMessages,
                            storedCount
                    );

                    break;

                case "5":

                    storedCount = deleteByMessageHash(
                            input,
                            storedMessages,
                            storedCount
                    );

                    // Update all parallel arrays after deletion.
                    allCount = rebuildParallelArrays(
                            sentMessages,
                            sentCount,
                            disregardedMessages,
                            disregardedCount,
                            storedMessages,
                            storedCount,
                            allMessages,
                            messageIDs,
                            messageHashes
                    );

                    break;

                case "6":

                    displayFullReport(
                            sentMessages,
                            sentCount,
                            storedMessages,
                            storedCount
                    );

                    break;

                case "7":

                    inMenu = false;

                    break;

                default:

                    System.out.println(
                            "Invalid option. Please try again."
                    );
            }
        }

        return storedCount;
    }

    // Displays the sender and recipient of all stored messages.
    private static void displaySenderAndRecipient(
            Message[] storedMessages, int storedCount) {

        System.out.println("\n=== SENDERS AND RECIPIENTS ===");

        if (storedCount == 0) {
            System.out.println("No stored messages found.");
            return;
        }

        for (int i = 0; i < storedCount; i++) {

            Message message = storedMessages[i];

            if (message != null) {

                System.out.println("Sender: " + message.getSender());
                System.out.println("Recipient: " + message.getRecipient());
                System.out.println("------------------------------");
            }
        }
    }

    // Displays the longest stored message.
    private static void displayLongestMessage(
            Message[] storedMessages, int storedCount) {

        System.out.println("\n=== LONGEST STORED MESSAGE ===");

        Message longest =
                Message.findLongestMessage(storedMessages);

        if (longest == null || storedCount == 0) {

            System.out.println("No stored messages found.");

        } else {

            longest.displayMessage();
        }
    }

    // Searches the parallel message ID array.
    private static void searchByMessageID(
            Scanner input,
            Message[] allMessages,
            String[] messageIDs,
            int allCount) {

        System.out.print("Enter the message ID: ");

        String searchID = input.nextLine();

        for (int i = 0; i < allCount; i++) {

            if (messageIDs[i] != null
                    && messageIDs[i].equals(searchID)) {

                System.out.println("\nMessage found.");

                System.out.println(
                        "Recipient: "
                        + allMessages[i].getRecipient()
                );

                System.out.println(
                        "Message: "
                        + allMessages[i].getMessageText()
                );

                return;
            }
        }

        System.out.println("Message ID not found.");
    }

    // Searches sent and stored messages for a recipient.
    private static void searchByRecipient(
            Scanner input,
            Message[] sentMessages,
            int sentCount,
            Message[] storedMessages,
            int storedCount) {

        System.out.print("Enter recipient number: ");

        String recipient = input.nextLine();

        boolean found = false;

        System.out.println(
                "\n=== MESSAGES FOR " + recipient + " ==="
        );

        // Search sent messages.
        for (int i = 0; i < sentCount; i++) {

            Message message = sentMessages[i];

            if (message != null
                    && message.getRecipient().equals(recipient)) {

                System.out.println(message.getMessageText());
                found = true;
            }
        }

        // Search stored messages.
        for (int i = 0; i < storedCount; i++) {

            Message message = storedMessages[i];

            if (message != null
                    && message.getRecipient().equals(recipient)) {

                System.out.println(message.getMessageText());
                found = true;
            }
        }

        if (!found) {
            System.out.println("No messages found for this recipient.");
        }
    }

    // Deletes a stored message using its message hash.
    private static int deleteByMessageHash(
            Scanner input,
            Message[] storedMessages,
            int storedCount) {

        System.out.print("Enter the message hash: ");

        String searchHash = input.nextLine();

        int index = -1;

        // Search the stored array for the matching hash.
        for (int i = 0; i < storedCount; i++) {

            if (storedMessages[i] != null
                    && storedMessages[i].getMessageHash()
                            .equals(searchHash)) {

                index = i;
                break;
            }
        }

        if (index == -1) {

            System.out.println("Stored message hash not found.");
            return storedCount;
        }

        Message deletedMessage = storedMessages[index];

        try {

            boolean deleted = Message.deleteMessageByHash(
                    MESSAGE_FILE,
                    searchHash
            );

            if (deleted) {

                System.out.println(
                        "Message: \""
                        + deletedMessage.getMessageText()
                        + "\" successfully deleted."
                );

                // Remove the message from the stored array.
                for (int i = index; i < storedCount - 1; i++) {
                    storedMessages[i] = storedMessages[i + 1];
                }

                storedMessages[storedCount - 1] = null;

                return storedCount - 1;

            } else {

                System.out.println("Message could not be deleted.");
                return storedCount;
            }

        } catch (IOException exception) {

            System.out.println(
                    "Error deleting message: "
                    + exception.getMessage()
            );

            return storedCount;
        }
    }

    // Displays a report of sent and stored messages.
    private static void displayFullReport(
            Message[] sentMessages,
            int sentCount,
            Message[] storedMessages,
            int storedCount) {

        System.out.println("\n========== MESSAGE REPORT ==========");

        boolean found = false;

        System.out.println("\n--- SENT MESSAGES ---");

        for (int i = 0; i < sentCount; i++) {

            if (sentMessages[i] != null) {

                sentMessages[i].displayMessage();
                System.out.println("------------------------------");

                found = true;
            }
        }

        System.out.println("\n--- STORED MESSAGES ---");

        for (int i = 0; i < storedCount; i++) {

            if (storedMessages[i] != null) {

                storedMessages[i].displayMessage();
                System.out.println("------------------------------");

                found = true;
            }
        }

        if (!found) {
            System.out.println("No messages available for the report.");
        }

        System.out.println("\n========== END OF REPORT ==========");
    }
}
