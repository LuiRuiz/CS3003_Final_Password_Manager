# CS 3003 Final Project Password Manager

Command Line Password Manager written in Java that implements AES algorithms from the Java crypto library to encrypt a list of serializable password objects sent to a text file. Subsequent runs of the program will decrypt the file and deserialize the objects into a working list to view the passwords.  New passwords are added to the working list, and then the file is overwritten with the data from the new list.

---
## Class Architecture 
(found in src)
* Main.java
* PasswordApp.java - CLI loop and business logic
* AESFileHandler.java - for sending and reading encrypted password list to and from file
* Password.java 
* NewPassword.java - extends Password
