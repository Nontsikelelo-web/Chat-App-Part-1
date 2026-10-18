/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.chatapp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 *
 * ST10508156 N.Panyapanya
 */
public class ChatApp {

    public static void main(String[] args) {
       Scanner input = new Scanner(System.in);
       
       
        System.out.println("=== REGISTRATION ===");
        //Asks the user to enter their first name.
        System.out.println("Enter first name");
        String firstName = input.nextLine();
        
        //Asks the user to enter their last name.
        System.out.println("Enter last name");
        String lastName = input.nextLine();
        
        //Asks the user to enter their username.
        System.out.println("Enter username:");
        String username = input.nextLine();
        
        //Asks the user to enter thier password.
        System.out.println("Enter password:");
        String password = input.nextLine();
        
        //Asks the user to enter thier cellphone number.
        System.out.println("Enter cellphone number:");
        String cellPhoneNumber = input.nextLine();
        
        Login user = new Login(firstName, lastName, username, password, cellPhoneNumber);
        
        String registrationResult = user.registerUser();
        System.out.println(registrationResult);
        
        String hasReg = "User registered successfully.";
        
        
        if(user.registerUser()== hasReg){
        System.out.println("\n=== LOGIN ===");
        
        System.out.println("Enter username:");
        String loginUsername = input.nextLine();
        
        System.out.println("Enter password:");
        String loginPassword = input.nextLine();
        
        boolean loginSuccessful = user.loginUser(loginUsername, loginPassword);
        
        System.out.println(user.returnLoginStatus());
        
        if (loginSuccessful){
            runQuickChat(input);
            }
        }
        input.close();
    }
     private static void runQuickChat(Scanner input) {
        List<Message> sentMessages = new ArrayList<>();
        boolean running = true;

        System.out.println("\nWelcome to QuickChat.");

        while (running) {
            System.out.println("\n=== QUICKCHAT MENU ===");
            System.out.println("1. Send Messages");
            System.out.println("2. Show recently sent messages");
            System.out.println("3. Quit");
            System.out.print("Choose an option (1-3): ");

            String choice = input.nextLine().trim();

            switch (choice) {
                case "1":
                    System.out.print(
                            "How many messages would you like to enter? ");

                    int numberOfMessages;

                    try {
                        numberOfMessages =
                                Integer.parseInt(input.nextLine().trim());
                    } catch (NumberFormatException e) {
                        System.out.println(
                                "Please enter a valid whole number.");
                        break;
                    }

                    if (numberOfMessages <= 0) {
                        System.out.println(
                                "Enter a number greater than zero.");
                        break;
                    }

                    for (int i = 0; i < numberOfMessages; i++) {
                        System.out.println(
                                "\nEntering message " + (i + 1));

                        System.out.print("Enter recipient number: ");
                        String recipient = input.nextLine().trim();

                        System.out.println(
                                Message.validateRecipientNumber(recipient));

                        if (!Message.isValidRecipient(recipient)) {
                            continue;
                        }

                        System.out.print("Enter your message: ");
                        String messageText = input.nextLine();

                        System.out.println(
                                Message.validateMessageLength(messageText));

                        if (messageText.trim().isEmpty()
                                || messageText.length() > 250) {
                            continue;
                        }

                        Message message = new Message(
                                i, recipient, messageText);

                        System.out.println("\n1. Send message");
                        System.out.println("2. Disregard message");
                        System.out.println("3. Store message");
                        System.out.print("Choose an option (1-3): ");

                        String action = input.nextLine().trim();

                        switch (action) {
                        case "1":
                           System.out.println(message.sendMessage());
                                 message.displayMessage();
                            sentMessages.add(message);
                           break;

                        case "2":
                          System.out.println("Press 0 to delete the message.");
                          System.out.print("Enter 0 to confirm deletion: ");

                          String deleteChoice = input.nextLine().trim();

                          if ("0".equals(deleteChoice)) {
                          System.out.println("Message deleted successfully.");
                   } else {
                          System.out.println("Message was not deleted.");
                   }
                         break;

                         case "3":
                 try {
                         System.out.println(
                         message.storeMessageWithStatus(
                            java.nio.file.Path.of("messages.json")));
                                message.displayMessage();
                  } catch (IOException e) {
                            System.out.println(
                                "Could not store the message: " + e.getMessage());
                  }
                  break;

                  default:
                  System.out.println("Invalid action. Message discarded.");
                  break;
                 }
                    }
                    break;

                case "2":
                    System.out.println("Coming Soon. ");
                    break;

                case "3":
                    running = false;
                    System.out.println(
                            "Thank you for using QuickChat!");
                    break;

                default:
                    System.out.println(
                            "Invalid option. Please choose 1, 2 or 3.");
                    break;
            }
        }
    }
}