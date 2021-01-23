import javax.swing.*;

import java.awt.*;
import java.awt.event.*;

public class LoginPanel extends JPanel{
    JLabel userLabel;
    JLabel passwordLabel;
    JLabel message;
    JTextField userNameText;
    JPasswordField passwordText;
    JButton submit;

    LoginPanel() {
        super(new GridLayout(3, 1));
        userLabel = new JLabel();
        userLabel.setText("User Name :");
        userNameText = new JTextField();

        passwordLabel = new JLabel();
        passwordLabel.setText("Password :");
        passwordText = new JPasswordField();

        submit = new JButton("Login");
        message = new JLabel();

        add(userLabel);
        add(userNameText);
        add(passwordLabel);
        add(passwordText);
        add(message);
        add(submit);
    }

    public String[] getInfo() {return new String [] {userNameText.getText(), new String(passwordText.getPassword())};}

    public void inform(String info) {message.setText(info);}

    public void addActionListener(ActionListener l) {
        submit.addActionListener(l);
        userNameText.addActionListener(l);
        passwordText.addActionListener(l);
    }

    public void clear(){
        userNameText.setText("");
        passwordText.setText("");
    }
}
