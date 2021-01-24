import javax.swing.*;
import java.awt.event.*;

public class LoginSignupTabbedPane extends JTabbedPane {
    static final long serialVersionUID = 42L;
    LoginPanel loginPanel;
    SignupPanel signupPanel;

    LoginSignupTabbedPane() {
        super();
        setBounds(50, 50, 200, 200);
        setSize(500, 500);

        loginPanel = new LoginPanel();
        JPanel lp = new JPanel();
        lp.add(loginPanel);
        add("Login", lp);

        signupPanel = new SignupPanel();
        JPanel sp = new JPanel();
        sp.add(signupPanel);
        add("Signup", sp);
    }

    public String[] getLoginInfo() {
        return loginPanel.getInfo();
    }

    public void loginInform(String info) {
        loginPanel.inform(info);
    }

    public String[] getSignupInfo() {
        return signupPanel.getInfo();
    }

    public void signupInform(String info) {
        signupPanel.inform(info);
    }

    public void addLoginActionListener(ActionListener l) {
        loginPanel.addActionListener(l);
    }

    public void addSignupActionListener(ActionListener l) {signupPanel.addActionListener(l);}

    public void clear() {
        loginPanel.clear();
        signupPanel.clear();
    }
}
