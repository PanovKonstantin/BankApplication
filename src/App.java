import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

import java.awt.*;
import java.awt.event.*;

public class App extends JFrame {
    ConnectionDatabase conn;
    AccountGenerator generator;
    JButton exit;
    LoginSignupTabbedPane loginSignupTP;
    HomeTabbedPane homeTP;
    int identificator;

    App() {
        super("Bank Application");
        conn = new ConnectionDatabase();
        generator = new AccountGenerator();
        setDefaultCloseOperation(this.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        loginSignupTP = new LoginSignupTabbedPane();
        loginSignupTP.addLoginActionListener(a -> {
            String[] info = loginSignupTP.getLoginInfo();
            int id = conn.loginUser(info[0], info[1]);
            switch (id) {
                case -1:
                    loginSignupTP.loginInform("Invalide username.. ");
                    break;
                default:
                    loginAccount(id);
                    break;
            }
        });
        loginSignupTP.addSignupActionListener(a -> {
            String[] info = loginSignupTP.getSignupInfo();
            int id = conn.addClient(info[0], info[1], info[2], info[3], info[4], info[5], info[6], info[7], info[8],
                    info[9], info[10], info[11], info[12], info[13]);
            switch (id) {
                case -1:
                    loginSignupTP.signupInform("Error..");
                    break;
                default:
                    loginAccount(id);
                    break;
            }
        });
        add(loginSignupTP);

        homeTP = new HomeTabbedPane();

        homeTP.setVisible(false);
        exit = new JButton("Logout");
        exit.addActionListener( a -> logoutAccount());
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
    }

    public void logoutAccount() {
        identificator = 0;
        loginSignupTP.setVisible(true);
        homeTP.setVisible(false);
        exit.setVisible(false);
    }

    public void generateAccounts(int number) {
        String[] accounts = new String[number];
        for (int i = 0; i < number; i++) {
            accounts[i] = generator.generateAccount();
        }
        conn.addAccounts(accounts);
    }

    public void refresh(){
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
        homeTP.savings.savings.setValueAt("Bank account", 0, 0);
        homeTP.savings.savings.setValueAt("Saving bank account", 1, 0);
        homeTP.savings.savings.setValueAt(clientData.get("BANK_ACCOUNT"), 0, 1);
        homeTP.savings.savings.setValueAt(clientData.get("SAVING_BANK_ACCOUNT"), 1, 1);
        homeTP.savings.savings.setValueAt(clientData.get("BANK_ACCOUNT_FUNDS"), 0, 2);
        homeTP.savings.savings.setValueAt(clientData.get("SAVING_BANK_ACCOUNT_FUNDS"), 1, 2);
    }

    public void generateAccounts(int number) {
        String[] accounts = new String[number];
        for (int i = 0; i < number; i++) {
            accounts[i] = generator.generateAccount();
        }
        conn.addAccounts(accounts);
    }

    public static void main(String[] args) {
        App app = new App();
    }
}