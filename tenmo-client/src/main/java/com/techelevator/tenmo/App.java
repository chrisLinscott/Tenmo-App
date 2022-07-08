package com.techelevator.tenmo;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.model.UserCredentials;
import com.techelevator.tenmo.services.AuthenticationService;
import com.techelevator.tenmo.services.ConsoleService;
import com.techelevator.tenmo.services.TransferService;
import com.techelevator.tenmo.services.UserService;

import java.math.BigDecimal;
import java.util.List;

public class App {

    private static final String API_BASE_URL = "http://localhost:8080/";

    private final ConsoleService consoleService = new ConsoleService();
    private final AuthenticationService authenticationService = new AuthenticationService(API_BASE_URL);
    private final UserService userService = new UserService(API_BASE_URL);
    private AuthenticatedUser currentUser;
    private TransferService transferService = new TransferService(API_BASE_URL);

    public static void main(String[] args) {
        App app = new App();
        app.run();
    }

    private void run() {
        consoleService.printGreeting();
        loginMenu();
        if (currentUser != null) {
            mainMenu();
        }
    }

    private void loginMenu() {
        int menuSelection = -1;
        while (menuSelection != 0 && currentUser == null) {
            consoleService.printLoginMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                handleRegister();
            } else if (menuSelection == 2) {
                handleLogin();
            } else if (menuSelection != 0) {
                System.out.println("Invalid Selection");
                consoleService.pause();
            }
        }
    }

    private void handleRegister() {
        System.out.println("Please register a new user account");
        UserCredentials credentials = consoleService.promptForCredentials();
        if (authenticationService.register(credentials)) {
            System.out.println("Registration successful. You can now login.");
        } else {
            consoleService.printErrorMessage();
        }
    }

    private void handleLogin() {
        UserCredentials credentials = consoleService.promptForCredentials();
        currentUser = authenticationService.login(credentials);
        if (currentUser == null) {
            consoleService.printErrorMessage();
        }
    }

    private void mainMenu() {
        int menuSelection = -1;
        while (menuSelection != 0) {
            consoleService.printMainMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                viewCurrentBalance();
            } else if (menuSelection == 2) {
                viewTransferHistory();
            } else if (menuSelection == 3) {
                viewPendingRequests();
            } else if (menuSelection == 4) {
                sendBucks();
            } else if (menuSelection == 5) {
                requestBucks();
            } else if (menuSelection == 0) {
                continue;
            } else {
                System.out.println("Invalid Selection");
            }
            consoleService.pause();
        }
    }

    private void viewCurrentBalance() {
        consoleService.printAccountBalances(userService.getAccounts(currentUser));

    }

    private void viewTransferHistory() {
        List<Transfer> transferList = transferService.getTransfers(currentUser);
        consoleService.printTransfers(transferList, currentUser);

        int transferId = consoleService.promptForTransferId();

        if (transferId == 0) {
            return;
        }
        for (Transfer transfer : transferList) {
            if (transfer.getId() == transferId) {
                Transfer selectedTransfer = transfer;
                consoleService.printTransferDetails(selectedTransfer);
                return;
            }
        }
        consoleService.printMessage("You entered a bad transfer id");

    }

    private void viewPendingRequests() {
        // TODO Auto-generated method stub

    }

    private void sendBucks() {
        User selectedUser = consoleService.promptForUserSelection(userService.getOtherUsers(currentUser));

        if (selectedUser != null) {
            BigDecimal amountToTransfer = consoleService.promptForBigDecimal("Enter amount:");
            Transfer transfer = new Transfer();
            transfer.setUserFrom(currentUser.getUser());
            transfer.setUserTo(selectedUser);
            transfer.setTransferType("Send");
            transfer.setAmount(amountToTransfer);
            String message = transferService.doTransfer(transfer, currentUser);
            consoleService.printMessage(message);
        }

    }

    private void requestBucks() {
        // TODO Auto-generated method stub

    }

}
