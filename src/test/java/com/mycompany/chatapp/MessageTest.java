/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */

package com.mycompany.chatapp;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.io.TempDir;

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

        assertEquals(message.getMessageID().substring(0, 2)+ ":0:HITONIGHT", message.getMessageHash());
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
    @TempDir
    Path temporaryFolder;

    // Lecturer test data: Message 1.
    private Message createMessage1() {

        return new Message(
                1,
                "Developer",
                "+27834557896",
                "Did you get the cake?"
        );
    }

    // Lecturer test data: Message 2.
    private Message createMessage2() {

        return new Message(
                2,
                "Developer",
                "+27838884567",
                "Where are you? You are late! I have asked you to be on time."
        );
    }

    // Lecturer test data: Message 3.
    private Message createMessage3() {

        return new Message(
                3,
                "Developer",
                "+27834484567",
                "Yohoooo, I am at your gate."
        );
    }

    // Lecturer test data: Message 4.
    // The lecturer's expected ID search value is 0838884567.
    private Message createMessage4() {

        return new Message(
                "0838884567",
                4,
                "Developer",
                "0838884567",
                "It is dinner time !"
        );
    }

    // Lecturer test data: Message 5.
    private Message createMessage5() {

        return new Message(
                5,
                "Developer",
                "+27838884567",
                "Ok, I am leaving without you."
        );
    }

    // TEST 1: Sent Messages array correctly populated.
    @Test
    void testSentMessagesArrayCorrectlyPopulated() {

        Message[] sentMessages = new Message[2];

        sentMessages[0] = createMessage1();
        sentMessages[1] = createMessage4();

        assertEquals(
                "Did you get the cake?",
                sentMessages[0].getMessageText()
        );

        assertEquals(
                "It is dinner time !",
                sentMessages[1].getMessageText()
        );

        assertEquals(2, sentMessages.length);
    }

    // TEST 2: Display the longest message.
    @Test
    void testDisplayLongestMessage() {

        Message[] messages = {
            createMessage1(),
            createMessage2(),
            createMessage3(),
            createMessage4()
        };

        Message longest =
                Message.findLongestMessage(messages);

        assertNotNull(longest);

        assertEquals(
                "Where are you? You are late! I have asked you to be on time.",
                longest.getMessageText()
        );
    }

    // TEST 3: Search for message ID.
    @Test
    void testSearchForMessageID() {

        Message[] messages = {
            createMessage1(),
            createMessage2(),
            createMessage3(),
            createMessage4()
        };

        Message found = Message.findByMessageID(
                messages,
                "0838884567"
        );

        assertNotNull(found);

        assertEquals(
                "It is dinner time !",
                found.getMessageText()
        );
    }

    // TEST 4: Search messages for a particular recipient.
    @Test
    void testSearchMessagesByRecipient() {

        Message[] messages = {
            createMessage1(),
            createMessage2(),
            createMessage3(),
            createMessage4(),
            createMessage5()
        };

        Message[] found = Message.findByRecipient(
                messages,
                "+27838884567"
        );

        assertEquals(2, found.length);

        assertEquals(
                "Where are you? You are late! I have asked you to be on time.",
                found[0].getMessageText()
        );

        assertEquals(
                "Ok, I am leaving without you.",
                found[1].getMessageText()
        );
    }

    // TEST 5: Delete a message using its message hash.
    @Test
    void testDeleteMessageUsingMessageHash() throws IOException {

        Path file = temporaryFolder.resolve("messages.json");

        Message message1 = createMessage1();
        Message message2 = createMessage2();
        Message message5 = createMessage5();

        List<Message> messages = new ArrayList<>();

        messages.add(message1);
        messages.add(message2);
        messages.add(message5);

        Message.writeMessages(file, messages);

        boolean deleted = Message.deleteMessageByHash(
                file,
                message2.getMessageHash()
        );

        assertTrue(deleted);

        List<Message> remainingMessages =
                Message.readMessages(file);

        assertEquals(2, remainingMessages.size());

        assertFalse(
                remainingMessages.stream().anyMatch(
                        message -> message.getMessageHash()
                                .equals(message2.getMessageHash())
                )
        );
    }

    // TEST 6: Display report containing sent messages.
    @Test
    void testDisplayMessageReport() {

        Message[] sentMessages = {
            createMessage1(),
            createMessage4()
        };

        String report =
                Message.createReport(sentMessages);

        assertTrue(report.contains("Message Hash:"));
        assertTrue(report.contains("Recipient:"));
        assertTrue(report.contains("Message:"));

        assertTrue(
                report.contains("Did you get the cake?")
        );

        assertTrue(
                report.contains("It is dinner time !")
        );
    }

    // TEST 7: Check that a normal message receives a 10-digit ID.
    @Test
    void testMessageIDContainsTenDigits() {

        Message message = createMessage1();

        assertTrue(
                message.getMessageID().matches("\\d{10}")
        );
    }

    
    // TEST 10: Check that an empty message is rejected.
    @Test
    void testEmptyMessageIsRejected() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new Message(
                        1,
                        "Developer",
                        "+27834557896",
                        ""
                )
        );
    }

    // TEST 11: Check that a message over 250 characters is rejected.
    @Test
    void testMessageOver250CharactersIsRejected() {

        String longMessage = "A".repeat(251);

        assertThrows(
                IllegalArgumentException.class,
                () -> new Message(
                        1,
                        "Developer",
                        "+27834557896",
                        longMessage
                )
        );
    }

    // TEST 12: Check that messages can be written and read from JSON.
    @Test
    void testReadAndWriteJSONFile() throws IOException {

        Path file = temporaryFolder.resolve("messages.json");

        Message message = createMessage1();

        List<Message> messages = new ArrayList<>();
        messages.add(message);

        Message.writeMessages(file, messages);

        List<Message> loadedMessages =
                Message.readMessages(file);

        assertEquals(1, loadedMessages.size());

        assertEquals(
                message.getMessageText(),
                loadedMessages.get(0).getMessageText()
        );

        assertEquals(
                message.getRecipient(),
                loadedMessages.get(0).getRecipient()
        );

        assertEquals(
                message.getSender(),
                loadedMessages.get(0).getSender()
        );
    }

    // TEST 13: Check that storing a message saves it in JSON.
    @Test
    void testStoreMessageInJSONFile() throws IOException {

        Path file = temporaryFolder.resolve("messages.json");

        Message message = createMessage2();

        String result = message.storeMessage(file);

        assertEquals(
                "Message successfully stored.",
                result
        );

        List<Message> storedMessages =
                Message.readMessages(file);

        assertEquals(1, storedMessages.size());

        assertEquals(
                message.getMessageText(),
                storedMessages.get(0).getMessageText()
        );
    }

    // TEST 14: An unknown hash must not delete a message.
    @Test
    void testUnknownHashDoesNotDeleteMessage() throws IOException {

        Path file = temporaryFolder.resolve("messages.json");

        Message message = createMessage2();

        List<Message> messages = new ArrayList<>();
        messages.add(message);

        Message.writeMessages(file, messages);

        boolean deleted = Message.deleteMessageByHash(
                file,
                "UNKNOWNHASH"
        );

        assertFalse(deleted);

        List<Message> remainingMessages =
                Message.readMessages(file);

        assertEquals(1, remainingMessages.size());
    }

    // TEST 15: Check that JSON keeps special characters.
    @Test
    void testJSONKeepsSpecialCharacters() throws IOException {

        Path file = temporaryFolder.resolve("messages.json");

        Message message = new Message(
                1,
                "Developer",
                "+27834557896",
                "Hello! \"How are you?\""
        );

        List<Message> messages = new ArrayList<>();
        messages.add(message);

        Message.writeMessages(file, messages);

        List<Message> loadedMessages =
                Message.readMessages(file);

        assertEquals(
                "Hello! \"How are you?\"",
                loadedMessages.get(0).getMessageText()
        );
    }
}



