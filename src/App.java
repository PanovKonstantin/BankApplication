import javax.swing.*;
import java.util.Map;

import java.awt.*;

public class App extends JFrame {
    static final long serialVersionUID = 42L;
    transient ConnectionDatabase conn;
    JButton exit;
    LoginSignupTabbedPane loginSignupTP;
    HomeTabbedPane homeTP;
    int identificator;

    App() {
        super("Bank Application");
        conn = new ConnectionDatabase();
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setLocation(350, 80);
        setLayout(new BorderLayout());

        loginSignupTP = new LoginSignupTabbedPane();
        loginSignupTP.addLoginActionListener(a -> {
            String[] info = loginSignupTP.getLoginInfo();
            int id = conn.loginUser(info[0], info[1]);
            switch (id) {
                case -1:
                    loginSignupTP.loginInform("Login error!");
                    break;
                case -2:
                    loginSignupTP.loginInform("Incorrect username or password.");
                    break;
                default:
                    loginAccount(id);
                    break;
            }
        });
        loginSignupTP.addSignupActionListener(a -> {
            String[] info = loginSignupTP.getSignupInfo();
            int id = conn.addClient(info);
            switch (id) {
                case -1:
                    loginSignupTP.signupInform("Unknown error occured.");
                    break;
                case -2:
                    loginSignupTP.signupInform("Unknown country.");
                    break;
                case -3:
                    loginSignupTP.signupInform("Invalid date of birth.");
                    break;
                case -4:
                    loginSignupTP.signupInform("Invalid phone number.");
                    break;
                case -5:
                    loginSignupTP.signupInform("Username is taken.");
                    break;
                case -6:
                    loginSignupTP.signupInform("Passwords do not match.");
                    break;
                case -7:
                    loginSignupTP.signupInform("Home field can contain only numbers.");
                    break;
                default:
                    loginAccount(id);
                    break;
            }
        });

        add(loginSignupTP);

        homeTP = new HomeTabbedPane();
        homeTP.home.addActionListener(a -> {
            String target = homeTP.home.target.getText();
            String amount = homeTP.home.amount.getText();
            String id = Integer.toString(identificator);

            int result = conn.makeTransaction(id, amount, target);
            switch (result) {
                case -4:
                    homeTP.home.inform("Input is incorrect!");
                    break;
                case -3:
                    homeTP.home.inform("Connection failed!");
                    break;
                case -2:
                    homeTP.home.inform("Not enough funds!");
                    break;
                case -1:
                    homeTP.home.inform("Transaction failed!");
                    break;
                default:
                    homeTP.home.inform("Transfer succeeded!");
                    refresh();
                    homeTP.home.clear();
                    break;
            }
        });

        homeTP.setVisible(false);
        exit = new JButton("Logout");
        exit.addActionListener(a -> logoutAccount());
        exit.setVisible(false);
        add(exit, BorderLayout.SOUTH);
        add(homeTP, BorderLayout.CENTER);

        setSize(600, 600);
        setResizable(false);
        setVisible(true);
    }

    public void loginAccount(int id) {
        identificator = id;
        refresh();
        loginSignupTP.setVisible(false);
        homeTP.setVisible(true);
        exit.setVisible(true);
        loginSignupTP.clear();
        homeTP.home.clear();
    }

    public void logoutAccount() {
        identificator = 0;
        loginSignupTP.setVisible(true);
        homeTP.setVisible(false);
        exit.setVisible(false);
    }

    public void refresh() {
        Map<String, String> clientData = conn.getClientData(identificator);

        homeTP.home.balance.setText(clientData.get("BANK_ACCOUNT_FUNDS"));
        homeTP.info.name.setText(clientData.get("FIRST_NAME"));
        homeTP.info.surname.setText(clientData.get("SECOND_NAME"));
        homeTP.info.birthdate.setText(clientData.get("BIRTH_DATE"));
        homeTP.info.email.setText(clientData.get("EMAIL"));
        homeTP.info.phone.setText(clientData.get("PHONE_NUMBER"));
        homeTP.info.accountid.setText(clientData.get("ID"));
        homeTP.info.username.setText(clientData.get("USERNAME"));
        homeTP.info.address.setText(clientData.get("ADDRESS"));

        homeTP.savings.refresh(conn.getSavingAccounts(identificator));
        homeTP.home.refresh(conn.getTransactionHistory(identificator));
    }

    public static void main(String[] args) {
        new App();
    }
}