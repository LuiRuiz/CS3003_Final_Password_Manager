import java.util.*;
import java.io.File;
import java.awt.*;
import java.awt.datatransfer.*;

public class PasswordApp {
    private ArrayList<Password> passwordList = new ArrayList<>();
    private int pageIndex = 0;  // To handle pagination

    public PasswordApp() {
        try {
            // read existing passwords if file exists
            File file = new File(AESFileManager.DATA_FILE);
            if (file.exists()) {
                passwordList = new ArrayList<>(AESFileManager.readPasswordList());
                System.out.println("Read " + passwordList.size() + " passwords from file.");
            } else {
                // use empty List
                System.out.println("No passwords found");
            }
        } catch (Exception e) {
            // use empty list if file not read correctly
            System.out.println("No passwords found.");
        }
    }

    public void start() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\nPasswords:");
            // displays five passwords or length of list
            int maxDisplay = Math.min(passwordList.size() - pageIndex, 5);
            // display up to five passwords at a time
            for (int i = 0; i < maxDisplay; i++) {
                System.out.println(i + 1+ " " + passwordList.get(pageIndex + i).getName());
            }
            // if no password found print empty from where display left off
            for (int i = maxDisplay; i < 5; i++) {
                System.out.println( i + 1 + " -Empty-");
            }

            System.out.println("6. Next page");
            System.out.println("7. Add pass");
            System.out.println("8. Quit");
            System.out.print("Choose an option: ");

            String input = scanner.nextLine();
            int choice;
            try {
                choice = Integer.parseInt(input);
            } catch (Exception e) {
                System.out.println("Please enter a number.");
                continue;
            }

            if (choice >= 1 && choice <= 5) {
                int passIndex = pageIndex + choice - 1;
                if (passIndex < passwordList.size()) {
                    Password currentPass = passwordList.get(passIndex);
                    System.out.println("Name: " + currentPass.getName());
                    System.out.println("Password: " + currentPass.getPassword());
                    copyToClipboard(currentPass.getPassword());// copy to clip here
                } else {
                    System.out.println("No Password Found.");
                }
            } else if (choice == 6) {
                //update page index to show next five if still within size of list
                if ((pageIndex + 5) < passwordList.size()) {
                    pageIndex += 5;
                } else {
                    //reset index to start
                    pageIndex = 0;
                }
            } else if (choice == 7) {
                System.out.print("Enter name for new password: ");
                String passName = scanner.nextLine();

                System.out.print("Enter password length: ");
                int passLength;
                try {
                    passLength = Integer.parseInt(scanner.nextLine());
                } catch (NumberFormatException e) {
                    System.out.println("please enter a number");
                    continue;
                }
                // create new password and add to list
                NewPassword newPass = new NewPassword(passLength, passName);
                passwordList.add(newPass);
                // save updated list every time password is added to list to prevent
                // errors from closing program, probably not efficent
                try {
                    AESFileManager.sendPasswordList(passwordList);
                    System.out.println("Passwords saved.");
                } catch (Exception e) {
                    System.out.println("Failed to save password");
                }
            } else if (choice == 8) {
                System.out.println("Good Bye.");
                break;
            } else {
                System.out.println("Invalid choice.");
            }
        }
        scanner.close();
    }

    void copyToClipboard(String passString){
        StringSelection selection = new StringSelection(passString);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(selection, selection);
        System.out.println("Password Copied to Clipboard");
    }
}