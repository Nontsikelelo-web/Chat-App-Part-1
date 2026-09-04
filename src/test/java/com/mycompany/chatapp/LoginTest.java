/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package com.mycompany.chatapp;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * ST10508156 N.Panyapanya 
 */
public class LoginTest { 
// Username not writen correctly
    @Test
    public void testUsernameCorrectlyFormatted() {

        Login login = new Login(
                "Kyle",
                "Smith",
                "kyl_1",
                "Ch&sec@ke9!",
                "+27838968976"
        );

        assertEquals(true, login.checkUserName());
    }


    //Username written incorrectly
    @Test
    public void testUsernameIncorrectlyFormatted() {

        Login login = new Login(
                "Kyle",
                "Smith",
                "kyle!!!!!!",
                "Ch&sec@ke9!",
                "+27838968976"
        );

        assertEquals(false, login.checkUserName());
    }


    // The password meets the requirements
    @Test
    public void testPasswordMeetsComplexity() {

        Login login = new Login(
                "Kyle",
                "Smith",
                "kyl_1",
                "Ch&sec@ke9!",
                "+27838968976"
        );

        assertEquals(true, login.checkPasswordComplexity());
    }


    // The password does not meet requirements needed
    @Test
    public void testPasswordDoesNotMeetComplexity() {

        Login login = new Login(
                "Kyle",
                "Smith",
                "kyl_1",
                "password",
                "+27838968976"
        );

        assertEquals(false, login.checkPasswordComplexity());
    }


    // The cellphone number is not written correctly
    @Test
    public void testCellPhoneCorrectlyFormatted() {

        Login login = new Login(
                "Kyle",
                "Smith",
                "kyl_1",
                "Ch&sec@ke9!",
                "+27838968976"
        );

        assertEquals(true, login.checkCellPhoneNumber());
    }


    // The cellphone number is written incorrectly
    @Test
    public void testCellPhoneIncorrectlyFormatted() {

        Login login = new Login(
                "Kyle",
                "Smith",
                "kyl_1",
                "Ch&sec@ke9!",
                "08966553"
        );

        assertEquals(false, login.checkCellPhoneNumber());
    }


    // The login is successful
    @Test
    public void testLoginSuccessful() {

        Login login = new Login(
                "Kyle",
                "Smith",
                "kyl_1",
                "Ch&sec@ke9!",
                "+27838968976"
        );

        assertTrue(login.loginUser("kyl_1", "Ch&sec@ke9!"));
    }


    // The login failed
    @Test
    public void testLoginFailed() {

        Login login = new Login(
                "Kyle",
                "Smith",
                "kyl_1",
                "Ch&sec@ke9!",
                "+27838968976"
        );

        assertFalse(login.loginUser("kyl_1", "password"));
    }


    // The username is written correctly
    @Test
    public void testUsernameCorrectlyFormattedTrue() {

        Login login = new Login(
                "Kyle",
                "Smith",
                "kyl_1",
                "Ch&sec@ke9!",
                "+27838968976"
        );

        assertTrue(login.checkUserName());
    }


    // The username is written incorrectly 
    @Test
    public void testUsernameIncorrectlyFormattedFalse() {

        Login login = new Login(
                "Kyle",
                "Smith",
                "kyle!!!!!!",
                "Ch&sec@ke9!",
                "+27838968976"
        );

        assertFalse(login.checkUserName());
    }


    //The password meets the complexity
    @Test
    public void testPasswordComplexityTrue() {

        Login login = new Login(
                "Kyle",
                "Smith",
                "kyl_1",
                "Ch&sec@ke9!",
                "+27838968976"
        );

        assertTrue(login.checkPasswordComplexity());
    }


    //The password does not meet complexity
    @Test
    public void testPasswordComplexityFalse() {

        Login login = new Login(
                "Kyle",
                "Smith",
                "kyl_1",
                "password",
                "+27838968976"
        );

        assertFalse(login.checkPasswordComplexity());
    }


    //The cellphone number is correctly formatted
    @Test
    public void testCellPhoneCorrectlyFormattedTrue() {

        Login login = new Login(
                "Kyle",
                "Smith",
                "kyl_1",
                "Ch&sec@ke9!",
                "+27838968976"
        );

        assertTrue(login.checkCellPhoneNumber());
    }


    // The cellphone is incorrectly formatted
    @Test
    public void testCellPhoneIncorrectlyFormattedFalse() {

        Login login = new Login(
                "Kyle",
                "Smith",
                "kyl_1",
                "Ch&sec@ke9!",
                "08966553"
        );

        assertFalse(login.checkCellPhoneNumber());
    }
}
    
    
