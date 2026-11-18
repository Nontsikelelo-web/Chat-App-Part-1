/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.chatapp;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Message {

    private static final java.security.SecureRandom RANDOM =
            new java.security.SecureRandom();

    private static int totalMessagesSent = 0;

    
    private final String messageID;
    private final String messageHash;
    private final String sender;
    private final String recipient;
    private final String messageText;
    private final int messageNumber;

    private boolean sent;

    // Original constructor
    public Message(int messageNumber, String recipient, String messageText) {
        this(
                String.valueOf(
                        RANDOM.nextLong(1_000_000_000L, 10_000_000_000L)
                ),
                messageNumber,
                "Developer",
                recipient,
                messageText,
                null
        );
    }

    
    public Message(int messageNumber, String sender,
                   String recipient, String messageText) {

        this(
                String.valueOf(
                        RANDOM.nextLong(1_000_000_000L, 10_000_000_000L)
                ),
                messageNumber,
                sender,
                recipient,
                messageText,
                null
        );
    }

    // Constructor for unit test data
    public Message(String messageID, int messageNumber,
                   String sender, String recipient, String messageText) {

        this(
                messageID,
                messageNumber,
                sender,
                recipient,
                messageText,
                null
        );
    }

    // Constructor used when reading messages from JSON.
    private Message(String messageID, int messageNumber,
                    String sender, String recipient,
                    String messageText, String savedHash) {

        if (messageID == null || messageID.isBlank()) {
            throw new IllegalArgumentException("Message ID cannot be empty.");
        }

        if (sender == null || sender.isBlank()) {
            throw new IllegalArgumentException("Sender cannot be empty.");
        }

        if (recipient == null || recipient.isBlank()) {
            throw new IllegalArgumentException("Recipient cannot be empty.");
        }

        if (messageText == null || messageText.isBlank()) {
            throw new IllegalArgumentException("Message cannot be empty.");
        }

        if (messageText.length() > 250) {
            throw new IllegalArgumentException(
                    "Message exceeds 250 characters."
            );
        }

        if (messageNumber < 0) {
            throw new IllegalArgumentException(
                    "Message number cannot be negative."
            );
        }

        this.messageID = messageID;
        this.messageNumber = messageNumber;
        this.sender = sender;
        this.recipient = recipient;
        this.messageText = messageText;

        if (savedHash == null) {
            this.messageHash = createMessageHash();
        } else {
            this.messageHash = savedHash;
        }
    }

    
    public String getMessageID() {
        return messageID;
    }

    public String getMessageHash() {
        return messageHash;
    }

    public String getSender() {
        return sender;
    }

    public String getRecipient() {
        return recipient;
    }

    public String getMessageText() {
        return messageText;
    }

    public int getMessageNumber() {
        return messageNumber;
    }

    public static int getTotalMessagesSent() {
        return totalMessagesSent;
    }

    public boolean isSent() {
        return sent;
    }

    // Creates a message hash.
    private String createMessageHash() {

        String cleanedMessage = messageText
                .replaceAll("[^a-zA-Z0-9 ]", "")
                .trim();

        String[] words = cleanedMessage.split("\\s+");

        String firstWord = words[0].toUpperCase(Locale.ROOT);
        String lastWord = words[words.length - 1]
                .toUpperCase(Locale.ROOT);

        return messageID.substring(0, 2)
                + ":"
                + messageNumber
                + ":"
                + firstWord
                + lastWord;
    }

    // Validates a South African recipient number.
    public static boolean isValidRecipient(String recipient) {

        return recipient != null
                && recipient.matches("\\+27\\d{9}");
    }

    // Sends a message.
    public String sendMessage() {

        if (!sent) {
            sent = true;
            totalMessagesSent++;
        }

        return "Message successfully sent.";
    }

    // Returns the disregard instruction.
    public String disregardMessage() {
        return "Press 0 to delete the message.";
    }

    // Stores a message in the JSON file.
    public String storeMessage(Path file) {

        try {

            List<Message> messages = readMessages(file);

            // Prevent storing the same message twice.
            boolean alreadyStored = false;

            for (Message message : messages) {

                if (message.getMessageHash().equals(messageHash)) {
                    alreadyStored = true;
                    break;
                }
            }

            if (!alreadyStored) {
                messages.add(this);
                writeMessages(file, messages);
            }

            return "Message successfully stored.";

        } catch (IOException exception) {

            return "Error storing message: " + exception.getMessage();
        }
    }

    public String storeMessageWithStatus(Path file) {
        return storeMessage(file);
    }

    // Reads messages from the JSON file into a List.
    public static List<Message> readMessages(Path file)
            throws IOException {

        List<Message> messages = new ArrayList<>();

        if (!Files.exists(file)) {
            return messages;
        }

        String json = Files.readString(
                file,
                StandardCharsets.UTF_8
        );

        Pattern objectPattern = Pattern.compile("\\{([^{}]*)\\}");
        Matcher objectMatcher = objectPattern.matcher(json);

        while (objectMatcher.find()) {

            String object = objectMatcher.group(1);

            String id = getJsonString(object, "messageID");
            String hash = getJsonString(object, "messageHash");
            String sender = getJsonString(object, "sender");
            String recipient = getJsonString(object, "recipient");
            String text = getJsonString(object, "messageText");
            int number = getJsonNumber(object, "messageNumber");

            // Support older JSON files without a sender field.
            if (sender == null || sender.isBlank()) {
                sender = "Unknown";
            }

            if (id != null && hash != null
                    && recipient != null && text != null) {

                messages.add(
                        new Message(
                                id,
                                number,
                                sender,
                                recipient,
                                text,
                                hash
                        )
                );
            }
        }

        return messages;
    }

    // Reads a string field from a JSON object.
    private static String getJsonString(String json, String key) {

        Pattern pattern = Pattern.compile(
                "\"" + Pattern.quote(key)
                + "\"\\s*:\\s*\"((?:\\\\.|[^\"\\\\])*)\""
        );

        Matcher matcher = pattern.matcher(json);

        if (matcher.find()) {
            return unescapeJson(matcher.group(1));
        }

        return null;
    }

    // Reads a number field from a JSON object.
    private static int getJsonNumber(String json, String key) {

        Pattern pattern = Pattern.compile(
                "\"" + Pattern.quote(key) + "\"\\s*:\\s*(\\d+)"
        );

        Matcher matcher = pattern.matcher(json);

        if (matcher.find()) {
            return Integer.parseInt(matcher.group(1));
        }

        return 0;
    }

    // Converts escaped JSON characters back to normal characters.
    private static String unescapeJson(String value) {

        StringBuilder result = new StringBuilder();

        for (int i = 0; i < value.length(); i++) {

            char current = value.charAt(i);

            if (current == '\\' && i + 1 < value.length()) {

                char next = value.charAt(++i);

                switch (next) {

                    case '"':
                        result.append('"');
                        break;

                    case '\\':
                        result.append('\\');
                        break;

                    case '/':
                        result.append('/');
                        break;

                    case 'n':
                        result.append('\n');
                        break;

                    case 'r':
                        result.append('\r');
                        break;

                    case 't':
                        result.append('\t');
                        break;

                    case 'b':
                        result.append('\b');
                        break;

                    case 'f':
                        result.append('\f');
                        break;

                    case 'u':
                        if (i + 4 < value.length()) {
                            String hex = value.substring(i + 1, i + 5);
                            result.append((char) Integer.parseInt(hex, 16));
                            i += 4;
                        }
                        break;

                    default:
                        result.append(next);
                }

            } else {
                result.append(current);
            }
        }

        return result.toString();
    }

    // Writes messages into a JSON file.
    public static void writeMessages(Path file, List<Message> messages)
            throws IOException {

        StringBuilder json = new StringBuilder();

        json.append("[\n");

        for (int i = 0; i < messages.size(); i++) {

            Message message = messages.get(i);

            json.append("  {\n");

            json.append("    \"messageID\": \"")
                    .append(escapeJson(message.messageID))
                    .append("\",\n");

            json.append("    \"messageHash\": \"")
                    .append(escapeJson(message.messageHash))
                    .append("\",\n");

            json.append("    \"sender\": \"")
                    .append(escapeJson(message.sender))
                    .append("\",\n");

            json.append("    \"recipient\": \"")
                    .append(escapeJson(message.recipient))
                    .append("\",\n");

            json.append("    \"messageText\": \"")
                    .append(escapeJson(message.messageText))
                    .append("\",\n");

            json.append("    \"messageNumber\": ")
                    .append(message.messageNumber)
                    .append("\n");

            json.append("  }");

            if (i < messages.size() - 1) {
                json.append(",");
            }

            json.append("\n");
        }

        json.append("]\n");

        Path parent = file.toAbsolutePath().getParent();

        if (parent != null) {
            Files.createDirectories(parent);
        }

        Files.writeString(
                file,
                json.toString(),
                StandardCharsets.UTF_8,
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING,
                StandardOpenOption.WRITE
        );
    }

    // Allows a Message array to be written into JSON.
    public static void writeMessages(Path file, Message[] messages)
            throws IOException {

        List<Message> list = new ArrayList<>();

        for (Message message : messages) {

            if (message != null) {
                list.add(message);
            }
        }

        writeMessages(file, list);
    }

    // Escapes special characters before saving JSON.
    private static String escapeJson(String value) {

        return value
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t")
                .replace("\b", "\\b")
                .replace("\f", "\\f");
    }

    // Deletes a stored message using its hash.
    public static boolean deleteMessageByHash(Path file, String hash)
            throws IOException {

        List<Message> messages = readMessages(file);

        boolean deleted = false;

        for (int i = 0; i < messages.size(); i++) {

            if (messages.get(i).getMessageHash().equals(hash)) {
                messages.remove(i);
                deleted = true;
                break;
            }
        }

        if (deleted) {
            writeMessages(file, messages);
        }

        return deleted;
    }

    // Finds the longest message in an array.
    public static Message findLongestMessage(Message[] messages) {

        Message longest = null;

        for (Message message : messages) {

            if (message != null
                    && (longest == null
                    || message.getMessageText().length()
                    > longest.getMessageText().length())) {

                longest = message;
            }
        }

        return longest;
    }

    // Searches for a message using its ID.
    public static Message findByMessageID(
            Message[] messages, String id) {

        for (Message message : messages) {

            if (message != null
                    && message.getMessageID().equals(id)) {

                return message;
            }
        }

        return null;
    }

    // Searches for all messages sent to a particular recipient.
    public static Message[] findByRecipient(
            Message[] messages, String recipient) {

        List<Message> found = new ArrayList<>();

        for (Message message : messages) {

            if (message != null
                    && message.getRecipient().equals(recipient)) {

                found.add(message);
            }
        }

        return found.toArray(new Message[0]);
    }

    // Creates a report containing the message details.
    public static String createReport(Message[] messages) {

        StringBuilder report = new StringBuilder();

        for (Message message : messages) {

            if (message != null) {

                report.append("Message ID: ")
                        .append(message.getMessageID())
                        .append("\n");

                report.append("Message Hash: ")
                        .append(message.getMessageHash())
                        .append("\n");

                report.append("Sender: ")
                        .append(message.getSender())
                        .append("\n");

                report.append("Recipient: ")
                        .append(message.getRecipient())
                        .append("\n");

                report.append("Message: ")
                        .append(message.getMessageText())
                        .append("\n");

                report.append("------------------------------\n");
            }
        }

        return report.toString();
    }

    // Displays a message.
    public void displayMessage() {

        System.out.println("Message ID: " + messageID);
        System.out.println("Message Hash: " + messageHash);
        System.out.println("Sender: " + sender);
        System.out.println("Recipient: " + recipient);
        System.out.println("Message: " + messageText);
    }
    public static String validateMessageLength(String messageText) {
    if (messageText.length() <= 250) {
        return "Message ready to send.";
    }

    int excess = messageText.length() - 250;

    return "Message exceeds 250 characters by " + excess
            + "; please reduce the size.";
}

public static String validateRecipientNumber(String recipient) {
    if (recipient.matches("^\\+27[0-9]{9}$")) {
        return "Cell phone number successfully captured.";
    }

    return "Cell phone number is incorrectly formatted or "
            + "does not contain an international code.";
}
    }
