/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.chatapp;

/**
 *
 * ST10508156 N.Panyapanya
 */
class Login {
      // Variables
    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private String cellPhoneNumber;

    private boolean loginStatus;


    // Constructor
    public Login(String firstName, String lastName, String username,
                 String password, String cellPhoneNumber) {

        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.cellPhoneNumber = cellPhoneNumber;
    }


    // it Check if the user name is written properly.
    public boolean checkUserName() {

        if (username.contains("_") && username.length() <= 5) {
            return true;
        } else {
            return false;
            
      }
        
    }


    // It checks if the password meets the requirments
    public boolean checkPasswordComplexity() {

        boolean hasCapital = false;
        boolean hasNumber = false;
        boolean hasSpecialCharacter = false;

        for (int i = 0; i < password.length(); i++) {

            char character = password.charAt(i);

            if (Character.isUpperCase(character)) {
                hasCapital = true;
            }

            if (Character.isDigit(character)) {
                hasNumber = true;
            }

            if (!Character.isLetterOrDigit(character)) {
                hasSpecialCharacter = true;
            }
        }

        if (password.length() >= 8
                && hasCapital
                && hasNumber
                && hasSpecialCharacter) {

            return true;

        } else {

            return false;
        }
    }


    // It checks if the cellphone number is a South African Cellphone number
    public boolean checkCellPhoneNumber() {

        String phonePattern = "^\\+27[0-9]{9}$";

        if (cellPhoneNumber.matches(phonePattern)) {
            return true;
        } else {
            return false;
            }
    }


    // Makes the user to be registered and returns the message that is required 
    public String registerUser() {

        if (!checkUserName()) {

            return "Username is not correctly formatted; please ensure "
                    + "that your username contains an underscore and is "
                    + "no more than five characters in length.";

        }else
        {

        System.out.println("Username successfully captured.");
        }


        if (!checkPasswordComplexity()) {

            return "Password is not correctly formatted; please ensure "
                    + "that the password contains at least eight characters, "
                    + "a capital letter, a number, and a special character.";

        }else{

            System.out.println("Password successfully captured.");
            
        }


        if (!checkCellPhoneNumber()) {

            return "Cell number is incorrectly formatted or does not "
                    + "contain an international code; please correct the "
                    + "number and try again.";

        }else{

            System.out.println("Cell number successfully captured.");
        }


        return "User registered successfully.";
        
         }


    // Checks if the login details are correct
    public boolean loginUser(String enteredUsername, String enteredPassword) {

        if (enteredUsername.equals(username)
                && enteredPassword.equals(password)) {

            loginStatus = true;

        }else{

            loginStatus = false;
         }

        return loginStatus;
        
    }


    // Returns the login status message  to the user
    public String returnLoginStatus() {

        if (loginStatus) {

            return "Welcome "+ firstName + " "+ lastName
                    + " it is great to see you.";

        } else {

            return "Username or password incorrect, please try again.";
        
                 }
        
    }
    
       
}
    

