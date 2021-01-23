import javax.swing.*;

import java.awt.*;
import java.awt.event.*;

public class App extends JFrame implements ActionListener {
    ConnectionDatabase conn;
    AccountGenerator generator;
    JButton  exit;
    LoginSignupTabbedPane loginSignupTP;
    HomeTabbedPane homeTP;

    App() {
        super("Bank Application");
        conn = new ConnectionDatabase();
        generator = new AccountGenerator();
        setDefaultCloseOperation(this.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        

        loginSignupTP = new LoginSignupTabbedPane();
        loginSignupTP.addLoginActionListener( a -> {
            String [] info = loginSignupTP.getLoginInfo();
            if (conn.loginUser(info[0], info[1])) loginAccount(); 
            else loginSignupTP.loginInform("Invalide username.. ");
        });
        loginSignupTP.addSignupActionListener( a -> {
            String [] info = loginSignupTP.getSignupInfo();
            if (conn.addClient(info[0], info[1], info[2], info[3], info[4], info[5], info[6], info[7], info[8])) loginAccount();
        });
        add(loginSignupTP);

        homeTP = new HomeTabbedPane();
        
        homeTP.setVisible(false);
        exit = new JButton("Logout");
        exit.setVisible(false);
        add(exit, BorderLayout.SOUTH);
        add(homeTP, BorderLayout.CENTER);

        setSize(600, 600);
        setResizable(false);
        setVisible(true);
    }

    public void loginAccount() {
        loginSignupTP.setVisible(false);
        homeTP.setVisible(true);
        exit.setVisible(true);
    }

    public void logoutAccount() {
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

    public void actionPerformed(ActionEvent e) {
        String [] info;
        switch(e.getActionCommand()){
            case "Login": 
                break;

            case "Signup": 
                break;

            default:
                break;
        }
    }

    public static void main(String[] args) {
        App app = new App();
        // app.generateAccounts(5);
    }
}
