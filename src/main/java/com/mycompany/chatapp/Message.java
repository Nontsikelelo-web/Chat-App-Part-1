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
import java.util.Locale;
import java.util.regex.Pattern;

public class Message {

    private static long nextId = 1;
    private static int totalMessagesSent = 0;

    private final String messageID;
    private final String messageHash;
    private final String recipient;
    private final String messageText;
    private final int messageNumber;
    private boolean sent;

    public Message(int messageNumber, String recipient,
                   String messageText) {

        if (messageNumber < 0) {
            throw new IllegalArgumentException(
                    "Message number cannot be negative.");
        }

        if (recipient == null || recipient.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Recipient cannot be empty.");
        }

        if (messageText == null || messageText.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Message cannot be empty.");
        }

        if (messageText.trim().length() > 250) {
            throw new IllegalArgumentException(
                    "Message cannot exceed 250 characters.");
        }

        this.messageNumber = messageNumber;
        this.recipient = recipient.trim();
        this.messageText = messageText.trim();

        this.messageID = String.format(
                Locale.ROOT, "%010d", nextId++);

        this.messageHash = createMessageHash();
        this.sent = false;
    }

    public String getMessageID() {
        return messageID;
    }

    public String getMessageHash() {
        return messageHash;
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

    public static String validateMessageLength(String text) {
        if (text == null) {
            text = "";
        }

        int length = text.length();

        if (length <= 250) {
            return "Message ready to send.";
        }

        return "Message exceeds 250 characters by "
                + (length - 250)
                + "; please reduce the size.";
    }

    public static boolean isValidRecipient(String number) {
        if (number == null) {
            return false;
        }

        return Pattern.matches(
                "^\\+27[0-9]{9}$", number.trim());
    }

    public static String validateRecipientNumber(String number) {
        if (isValidRecipient(number)) {
            return "Cell phone number successfully captured.";
        }

        return "Cell phone number is incorrectly formatted or does not "
                + "contain an international code. Please correct the "
                + "number and try again.";
    }

    private String createMessageHash() {
        String[] words = messageText.trim().split("\\s+");

        String firstWord = cleanWord(words[0]);
        String lastWord = cleanWord(words[words.length - 1]);

        return messageID.substring(0, 2)
                + ":" + messageNumber
                + ":" + firstWord.toUpperCase(Locale.ROOT)
                + lastWord.toUpperCase(Locale.ROOT);
    }

    private String cleanWord(String word) {
        return word.replaceAll(
                "^[^a-zA-Z0-9]+|[^a-zA-Z0-9]+$", "");
    }

    public String sendMessage() {
        if (!sent) {
            sent = true;
            totalMessagesSent++;
        }

        return "Message successfully sent.";
    }

    public String disregardMessage() {
        return "Press 0 to delete the message.";
    }

    public void storeMessage() throws IOException {
        storeMessage(Path.of("messages.json"));
    }

    public void storeMessage(Path file) throws IOException {
        String existing = "[]";

        if (Files.exists(file)) {
            existing = Files.readString(
                    file, StandardCharsets.UTF_8).trim();

            if (existing.isEmpty()) {
                existing = "[]";
            }
        }

        if (!existing.startsWith("[")
                || !existing.endsWith("]")) {
            throw new IOException(
                    "The existing JSON file is not a JSON array.");
        }

        String contents = existing.substring(
                1, existing.length() - 1).trim();

        String separator = contents.isEmpty() ? "" : ",";

        String jsonObject = "{"
                + "\"messageID\":\"" + escapeJson(messageID) + "\","
                + "\"messageHash\":\"" + escapeJson(messageHash) + "\","
                + "\"recipient\":\"" + escapeJson(recipient) + "\","
                + "\"messageText\":\"" + escapeJson(messageText) + "\","
                + "\"messageNumber\":" + messageNumber
                + "}";

        String updated = "[" + contents + separator
                + jsonObject + "]";

        Path parent = file.toAbsolutePath().getParent();

        if (parent != null) {
            Files.createDirectories(parent);
        }

        Files.writeString(
                file,
                updated,
                StandardCharsets.UTF_8,
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING,
                StandardOpenOption.WRITE);
    }

    public String storeMessageWithStatus(Path file)
            throws IOException {
        storeMessage(file);
        return "Message successfully stored.";
    }

    private String escapeJson(String value) {
        return value
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }

    public void displayMessage() {
        System.out.println("Message ID: " + messageID);
        System.out.println("Message Hash: " + messageHash);
        System.out.println("Recipient: " + recipient);
        System.out.println("Message: " + messageText);
    }
}
