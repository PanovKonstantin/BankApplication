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
            int id = conn.addClient(info);
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
        

        homeTP.home.refresh(conn.getTransactionHistory(identificator));
    }

    public static void main(String[] args) {
        new App();
    }
}