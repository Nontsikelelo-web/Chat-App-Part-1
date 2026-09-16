# ChatApp – Registration and Login System

## Project Overview

ChatApp is a Java-based registration and login application developed as part of an academic programming task. The application allows a user to register an account using a username, password and South African cellphone number. The system validates the information entered before allowing the user to register.

After registration, the user can log in using the same username and password. The application then displays an appropriate message depending on whether the login details are correct.

## Technologies Used

- Java
- NetBeans
- Maven
- JUnit
- Regular Expressions
- GitHub

## Main Features

### 1. User Registration

The application allows the user to enter:

- First name
- Last name
- Username
- Password
- Cellphone number

The username must contain an underscore (`_`) and must not be more than five characters long.

The password must:

- Be at least eight characters long.
- Contain a capital letter.
- Contain a number.
- Contain a special character.

The cellphone number is validated using a regular expression and must contain the South African international country code (`+27`).

Java provides the `Pattern` and `Matcher` classes for working with regular expressions (Oracle, 2026).

### 2. Login

After successful registration, the user can enter their username and password to log in.

If the details are correct, the system displays a welcome message containing the user's first and last name.

If the details are incorrect, the system displays:

> Username or password incorrect, please try again.

## Classes

### ChatApp

`ChatApp.java` is the main class used to run the application. It collects information from the user and calls the relevant methods from the `Login` class.

### Login

`Login.java` contains the main registration and authentication functionality.

The methods implemented include:

- `checkUserName()`
- `checkPasswordComplexity()`
- `checkCellPhoneNumber()`
- `registerUser()`
- `loginUser()`
- `returnLoginStatus()`

## Validation

Input validation was implemented to ensure that incorrect information is identified before registration.

Examples of test data include:

| Input | Example |
|---|---|
| Valid username | `kyl_1` |
| Invalid username | `kyle!!!!!!` |
| Valid password | `Ch&sec@ke9!` |
| Invalid password | `password` |
| Valid cellphone | `+27838968976` |
| Invalid cellphone | `08966553` |

## Unit Testing

JUnit tests were created to test the methods in the `Login` class. Both valid and invalid inputs are tested using assertions such as `assertEquals`, `assertTrue` and `assertFalse`.

The tests verify:

- Username validation
- Password validation
- Cellphone number validation
- Successful login
- Failed login

The test cases are based on the test data provided in the assignment.

## Learning Outcomes

Through this project, I gained experience in:

- Java programming
- Object-oriented programming
- Methods and constructors
- User input validation
- Regular expressions
- Boolean values
- Authentication and login functionality
- Unit testing with JUnit
- Using NetBeans and Maven
- Using GitHub for version control

## References

Oracle, 2026. *Pattern (Java SE 26 & JDK 26)*. Available at:
https://docs.oracle.com/en/java/javase/26/docs/api/java.base/java/util/regex/Pattern.html
[Accessed 4 September 2026].

Oracle, 2026. *Regular Expressions*. Available at:
https://docs.oracle.com/javase/tutorial/essential/regex/
[Accessed 4 September 2026].
