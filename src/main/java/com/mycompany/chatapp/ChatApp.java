/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.chatapp;

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
        
        System.out.println(user.registerUser());
        
        System.out.println("\n=== LOGIN ===");
        
        System.out.println("Enter username:");
        String loginUsername = input.nextLine();
        
        System.out.println("Enter password:");
        String loginPassword = input.nextLine();
        
        user.loginUser(loginUsername, loginPassword);
        
        System.out.println(user.returnLoginStatus());
        input.close();
    }
}
