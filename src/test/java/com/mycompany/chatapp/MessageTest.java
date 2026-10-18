/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */

package com.mycompany.chatapp;

import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MessageTest {

    // Test 1: A message of 250 characters is accepted.
    @Test
    void testMessageWithin250Characters() {
        assertEquals(
                "Message ready to send.",
                Message.validateMessageLength("A".repeat(250)));
    }

    // Test 2: A message exceeding 250 characters is rejected.
    @Test
    void testMessageExceeds250Characters() {
        assertEquals(
                "Message exceeds 250 characters by 5; please reduce the size.",
                Message.validateMessageLength("A".repeat(255)));
    }

    // Test 3: A valid international cellphone number is accepted.
    @Test
    void testValidRecipientNumber() {
        assertEquals(
                "Cell phone number successfully captured.",
                Message.validateRecipientNumber("+27718693002"));
    }

    // Test 4: An incorrectly formatted cellphone number is rejected.
    @Test
    void testInvalidRecipientNumber() {
        assertFalse(Message.isValidRecipient("08575975889"));
        assertFalse(Message.isValidRecipient("12345"));
        assertFalse(Message.isValidRecipient(null));
    }

    // Test 5: The message hash matches the expected example.
    @Test
    void testMessageHash() {
        Message message = new Message(
                0, "+27718693002",
                "Hi Mike, can you join us for dinner tonight?");

        assertEquals("00:0:HITONIGHT", message.getMessageHash());
    }

    // Test 6: Additional message hashes are tested in a loop.
    @Test
    void testMessageHashesInLoop() {
        String[] texts = {
            "Hello Peter",
            "Good morning everyone",
            "Meet me tomorrow"
        };

        String[] expectedEndings = {
            "HELLOPETER",
            "GOODEVERYONE",
            "MEETTOMORROW"
        };

        for (int i = 0; i < texts.length; i++) {
            Message message = new Message(
                    i, "+27718693002", texts[i]);

            String expected = message.getMessageID().substring(0, 2)
                    + ":" + i + ":" + expectedEndings[i];

            assertEquals(expected, message.getMessageHash());
        }
    }

    // Test 7: The message ID has ten digits and IDs are unique.
    @Test
    void testMessageID() {
        Message first = new Message(
                0, "+27718693002", "First message");

        Message second = new Message(
                1, "+27718693002", "Second message");

        assertTrue(first.getMessageID().matches("[0-9]{10}"));
        assertNotEquals(first.getMessageID(), second.getMessageID());
    }

    // Test 8: Sending a message returns the correct response.
    @Test
    void testSendMessage() {
        Message message = new Message(
                0, "+27718693002", "Hello there");

        int before = Message.getTotalMessagesSent();

        assertEquals(
                "Message successfully sent.",
                message.sendMessage());

        assertTrue(message.isSent());
        assertEquals(before + 1, Message.getTotalMessagesSent());
    }

    // Test 9: Disregarding a message returns the required response.
    @Test
    void testDisregardMessage() {
        Message message = new Message(
                0, "+27718693002", "Hello there");

        assertEquals(
                "Press 0 to delete the message.",
                message.disregardMessage());

        assertFalse(message.isSent());
    }

    // Test 10: A message is stored in JSON successfully.
    @Test
    void testStoreMessage() throws Exception {
        Path file = Files.createTempFile("message", ".json");

        try {
            Files.writeString(file, "[]");

            Message message = new Message(
                    0, "+27718693002", "Hello there");

            assertEquals(
                    "Message successfully stored.",
                    message.storeMessageWithStatus(file));

            String json = Files.readString(file);

            assertTrue(json.contains("\"messageID\""));
            assertTrue(json.contains("\"messageHash\""));
            assertTrue(json.contains("\"recipient\""));
            assertTrue(json.contains("\"messageText\""));
            assertTrue(json.contains("Hello there"));
        } finally {
            Files.deleteIfExists(file);
        }
    }

    // Test 11: Multiple messages are stored in the JSON array.
    @Test
    void testStoreMultipleMessages() throws Exception {
        Path file = Files.createTempFile("messages", ".json");

        try {
            Files.writeString(file, "[]");

            Message first = new Message(
                    0, "+27718693002", "First message");

            Message second = new Message(
                    1, "+27718693003", "Second message");

            first.storeMessage(file);
            second.storeMessage(file);

            String json = Files.readString(file);

            assertTrue(json.contains("First message"));
            assertTrue(json.contains("Second message"));

            int count = json.split("\"messageID\"", -1).length - 1;
            assertEquals(2, count);
        } finally {
            Files.deleteIfExists(file);
        }
    }

    // Test 12: An empty recipient is rejected.
    @Test
    void testEmptyRecipient() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new Message(0, "", "Hello"));
    }

    // Test 13: An empty message is rejected.
    @Test
    void testEmptyMessage() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new Message(0, "+27718693002", ""));
    }
}


