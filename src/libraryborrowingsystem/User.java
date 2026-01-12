/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package libraryborrowingsystem;

import java.util.Scanner;

public class User {

    // variables to hold user details
    String name;
    String email;
    String phoneNo;
    String userID;

    // constructor (used to create a new user object)
    public User(String name, String email, String phoneNo, String userID) {
        this.name = name;
        this.email = email;
        this.phoneNo = phoneNo;
        this.userID = userID;
    }
    public String getName() {
        return name;
    }

    // ✅ Getter for userID (this is what fixes the error)
    public String getUserID() {
        return userID;
    }
    
    String getEmail() {
      return email; 
    }

    // ✅ Setter (optional but harmless to include)
    public void setUserID(String userID) {
        this.userID = userID;
    }

    // method to register user by asking for details
    public static User registerUser() {
        Scanner input = new Scanner(System.in);

        System.out.print("Enter your full name: ");
        String name = input.nextLine();

        System.out.print("Enter your email address: ");
        String email = input.nextLine();

        System.out.print("Enter your phone number: ");
        String phoneNo = input.nextLine();

        //System.out.print("Enter a User ID (e.g. U001): ");
        String userID = input.nextLine();

        // create a new User object with the given details
        User newUser = new User(name, email, phoneNo, userID);

        System.out.println("\nRegistration successful! Welcome, " + name + "\n");

        return newUser; // return the created user
    }

    // method to display user details
    public void displayUserDetails() {
        System.out.println("User Details:");
        System.out.println("Name: " + name);
        System.out.println("Email: " + email);
        System.out.println("Phone No: " + phoneNo);
        System.out.println("User ID: " + userID);
    }

    // Method to verify the user account
    public void verifyAccount() {
        System.out.println("Account for " + name + " has been verified.");
    }

    
}
