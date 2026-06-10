/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.poepartone;

/**
 *
 * @author Student
 */

    public class login {

        private String firstname;
        private String lastname;
        private String passWord;
        private String phoneNumber;
        private String userName;

        // Store user details
        public void LoginDetails(String username, String password, String firstName, String lastName, String phoneNum) {
            userName    = username;
            passWord    = password;
            firstname   = firstName;
            lastname    = lastName;
            phoneNumber = phoneNum;
        }

        // Username check
        public  boolean validUsername() {
            boolean ok = userName.contains("_") && userName.length() <= 5;

            if (ok) {
                System.out.println("Username successfully captured.");
            } else {
                System.out.println("Username is not correctly formatted; must contain '_' and be ≤ 5 characters.");
            }

            return ok;
        }
         //used AI to assist with password check
        // Password check
        public boolean validPassword() {
            boolean ok = passWord.length() >= 8 &&
                    passWord.matches(".*[A-Z].*") &&
                    passWord.matches(".*[0-9].*") &&
                    passWord.matches(".*[\\p{Punct}].*");

            if (ok) {
                System.out.println("Password successfully captured.");
            } else {
                System.out.println("Password must be 8+ chars, include capital, number, and special character.");
            }

            return ok;
        }

        // Cell number check
        public boolean validPhone() {
            boolean ok = phoneNumber.startsWith("+27") && phoneNumber.length() == 12;

            if (ok) {
                System.out.println("Cell phone number successfully added.");
            } else {
                System.out.println("Cell number must start with +27 and be 12 digits long.");
            }

            return ok;
        }
          // used you tube to help here
        // Register user
        public String registerUser() {

            if (!validUsername()) {
                return "Username is not correctly formatted.";
            }

            if (!validPassword()) {
                return "Password is not correctly formatted.";
            }

            if (!validPhone()) {
                return "Cell number is incorrect.";
            }

            return "User successfully captured";
        }

        // Login check
        public boolean LoginUser(String u, String p) {
            return userName.equals(u) && passWord.equals(p);
        }

        // Login message
        public String returnLoginStatus(boolean status) {
            return status
                    ? "Welcome " + firstname + " " + lastname + ", it is great to see you again."
                    : "Username or password incorrect, please try again.";
        }
    }

