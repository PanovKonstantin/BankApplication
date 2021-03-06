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
    Map<String, String> clientData;

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

        homeTP.info.addActionListener(e -> {
            String username = homeTP.info.username.getText();
            String phone = homeTP.info.phone.getText();
            String email = homeTP.info.email.getText();
            String name = homeTP.info.name.getText();
            String surname = homeTP.info.surname.getText();
            String message = "";
            final String connfailed = "Connection failed.";
            boolean correct = true;
            switch(conn.changeClientUsername(username, identificator)){
                case -1:
                    homeTP.info.info.setText(connfailed);
                    return;
                case -2:
                    message += "Username is taken. ";
                    correct = false;
                    break;
                default: break;
            }
            if(!phone.matches("\\d+")||phone.length()!=9){
                message += "Phone number is incorrect. ";
                correct = false;
            }
            if(!correct){
                homeTP.info.info.setText(message);
                homeTP.info.info.setVisible(true);
                return;
            }
            if(conn.changeClientEmail(email, identificator) == -1){
                homeTP.info.info.setText(connfailed);
                return;
            }
            if(conn.changeClientName(name, identificator) == -1){
                homeTP.info.info.setText(connfailed);
                return;
            }
            if(conn.changeClientSurame(surname, identificator) == -1){
                homeTP.info.info.setText(connfailed);
                return;
            }
            if(conn.changeClientPhone(phone, identificator) == -1){
                homeTP.info.info.setText(connfailed);
                return;
            }
            refresh();
            homeTP.info.editbutton.setText("Edit");
            homeTP.info.username.setEditable(false);
            homeTP.info.email.setEditable(false);
            homeTP.info.name.setEditable(false);
            homeTP.info.surname.setEditable(false);
            homeTP.info.phone.setEditable(false);
            homeTP.info.info.setVisible(false);
            homeTP.info.savebutton.setEnabled(false);

        });
        homeTP.info.editbutton.addActionListener(e -> {
            if(e.getActionCommand().equals("Edit")){
                homeTP.info.editbutton.setText("Cancel");
                homeTP.info.username.setEditable(true);
                homeTP.info.email.setEditable(true);
                homeTP.info.name.setEditable(true);
                homeTP.info.surname.setEditable(true);
                homeTP.info.phone.setEditable(true);
                homeTP.info.savebutton.setEnabled(true);
            } else {
                refresh();
                homeTP.info.editbutton.setText("Edit");
                homeTP.info.username.setEditable(false);
                homeTP.info.email.setEditable(false);
                homeTP.info.name.setEditable(false);
                homeTP.info.surname.setEditable(false);
                homeTP.info.phone.setEditable(false);
                homeTP.info.info.setVisible(false);
                homeTP.info.savebutton.setEnabled(false);
            }
        });
        homeTP.savings.transferTo.addActionListener(e->{
            String target = "";
            String target_funds = "";
            String amount = homeTP.savings.amount.getText();
            String funds = "";
            if (amount.equals("") || amount.equals("0")){
                homeTP.savings.amount.setText("");
                return;
            }
            funds = homeTP.home.balance.getText();
            if (Integer.parseInt(funds) < Integer.parseInt(amount)){
                homeTP.savings.amount.setText("");
                return;
            }
            for(int i = 0; i < homeTP.savings.savings.getRowCount(); i++){
                if (((JRadioButton)homeTP.savings.savings.getValueAt(i, 3)).isSelected()){
                    target = (String)homeTP.savings.savings.getValueAt(i, 0);
                    target_funds = (String)homeTP.savings.savings.getValueAt(i, 1);
                    amount = homeTP.savings.amount.getText();
                    funds = Integer.toString(Integer.parseInt(funds) - Integer.parseInt(amount));
                    target_funds = Integer.toString(Integer.parseInt(target_funds) + Integer.parseInt(amount));
                    conn.tarnsferUpdate(clientData.get("BANK_ACCOUNT"), target, funds, target_funds);
                    refresh();
                }
            }
            homeTP.savings.amount.setText("");
            return;
        });
        homeTP.savings.transferFrom.addActionListener(e->{
            String target = "";
            String target_funds = "";
            String amount = homeTP.savings.amount.getText();
            String funds = "";
            if (amount.equals("") || amount.equals("0")){
                homeTP.savings.amount.setText("");
                return;
            }
            for(int i = 0; i < homeTP.savings.savings.getRowCount(); i++){
                if (((JRadioButton)homeTP.savings.savings.getValueAt(i, 3)).isSelected()){
                    target = (String)homeTP.savings.savings.getValueAt(i, 0);
                    target_funds = (String)homeTP.savings.savings.getValueAt(i, 1);
                    amount = homeTP.savings.amount.getText();
                    funds = homeTP.home.balance.getText();
                    if (Integer.parseInt(target_funds) < Integer.parseInt(amount)){
                        homeTP.savings.amount.setText("");
                        return;
                    }
                    funds = Integer.toString(Integer.parseInt(funds) + Integer.parseInt(amount));
                    target_funds = Integer.toString(Integer.parseInt(target_funds) - Integer.parseInt(amount));
                    conn.tarnsferUpdate(clientData.get("BANK_ACCOUNT"), target, funds, target_funds);
                    refresh();
                }
            }
            homeTP.savings.amount.setText("");
            return;
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
        clientData = conn.getClientData(identificator);

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