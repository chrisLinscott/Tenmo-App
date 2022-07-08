package com.techelevator.tenmo.services;


import com.techelevator.tenmo.model.*;

import javax.swing.plaf.synth.SynthOptionPaneUI;
import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;

public class ConsoleService {

    private final Scanner scanner = new Scanner(System.in);

    public int promptForMenuSelection(String prompt) {
        int menuSelection;
        System.out.print(prompt);
        try {
            menuSelection = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            menuSelection = -1;
        }
        return menuSelection;
    }

    public void printTransfers(List<Transfer> transfers, AuthenticatedUser currentUser) {

        String formatString = "%-10s %-20s %-10s";
        System.out.println("-------------------------------------------");
        System.out.println("Transfers");
        System.out.printf(formatString, "ID", "From/To", "Amount");
        System.out.println();
        System.out.println("-------------------------------------------");

        for (Transfer transfer : transfers) {
            if (transfer.getUserFrom().equals(currentUser.getUser())) {
                System.out.printf(formatString, transfer.getId(), "To: " + transfer.getUserTo().getUsername(),
                                  "$" + transfer.getAmount()
                                 );
            } else {
                System.out.printf(formatString, transfer.getId(), "From: " + transfer.getUserFrom().getUsername(),
                                  "$" + transfer.getAmount()
                                 );
            }
            System.out.println();

        }

    }

    public void printTransferDetails(Transfer transfer){
        System.out.println("-------------------------------------------");
        System.out.println("Transfer Details");
        System.out.println("-------------------------------------------");
        System.out.println("Id: "+transfer.getId());
        System.out.println("From: "+transfer.getUserFrom().getUsername());
        System.out.println("To: "+transfer.getUserTo().getUsername());
        System.out.println("Type: "+transfer.getTransferType());
        System.out.println("Status: "+transfer.getTransferStatus());
        System.out.println("Amount: $"+transfer.getAmount());

    }

    public int promptForTransferId() {
        System.out.println("-------------------------------------------");

        int selectedTransferId = promptForInt("Please enter the transfer ID to view details (0 to cancel):");
        return selectedTransferId;
    }


    public User promptForUserSelection(List<User> userList) {
        System.out.println("---------------------");
        System.out.println("Users");
        System.out.printf("%-10s%-10s", "ID", "Name");
        System.out.println();
        System.out.println("---------------------");
        for (User user : userList) {
            System.out.printf("%-10s%-10s", user.getId(), user.getUsername());
            System.out.println();
        }
        System.out.println("------------");

        User selectedUser = null;

        while (selectedUser == null) {
            int userId = promptForInt("From the list above, enter ID of user you are sending to (0 to cancel):");
            if (userId == 0) {
                return null;
            }
            for (User user : userList) {
                if (user.getId() == userId) {
                    selectedUser = user;
                }
            }

        }
        return selectedUser;
    }

    public void printGreeting() {
        System.out.println("*********************");
        System.out.println("* Welcome to TEnmo! *");
        System.out.println("*********************");
    }

    public void printLoginMenu() {
        System.out.println();
        System.out.println("1: Register");
        System.out.println("2: Login");
        System.out.println("0: Exit");
        System.out.println();
    }

    public void printMainMenu() {
        System.out.println();
        System.out.println("1: View your current balance");
        System.out.println("2: View your past transfers");
        System.out.println("3: View your pending requests");
        System.out.println("4: Send TE bucks");
        System.out.println("5: Request TE bucks");
        System.out.println("0: Exit");
        System.out.println();
    }


    public void printAccountBalances(List<Account> accounts) {

        for (Account account : accounts) {
            System.out.println("Your balance for account ID " + account.getId() + " is $" + account.getAccountBalance());
        }

    }


    public UserCredentials promptForCredentials() {
        String username = promptForString("Username: ");
        String password = promptForString("Password: ");
        return new UserCredentials(username, password);
    }

    public String promptForString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    public int promptForInt(String prompt) {
        System.out.print(prompt);
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a number.");
            }
        }
    }

    public BigDecimal promptForBigDecimal(String prompt) {
        System.out.print(prompt);
        while (true) {
            try {
                return new BigDecimal(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a decimal number.");
            }
        }
    }

    public void pause() {
        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }

    public void printErrorMessage() {
        System.out.println("An error occurred. Check the log for details.");
    }

    public void printMessage(String message) {
        System.out.println(message);
    }

}
