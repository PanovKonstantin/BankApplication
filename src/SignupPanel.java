import javax.swing.*;

import java.awt.*;
import java.awt.event.*;

public class SignupPanel extends JPanel{
    JTextField firstNameText;
    JTextField secondNameText;
    JTextField userNameText;
    JTextField emailText;
    JTextField addressText;
    JTextField birthdateText;
    JTextField phoneText;
    JPasswordField passwordText;
    JPasswordField pwRepeatText;
    JLabel firstNameLabel;
    JLabel secondNameLabel;
    JLabel userNameLabel;
    JLabel emailLabel;
    JLabel addressLabel;
    JLabel birthdateLabel;
    JLabel phoneLabel;
    JLabel passwordLabel;
    JLabel pwRepeatLabel;
    JLabel message;
    JButton submit;

    SignupPanel() {
        super(new GridLayout(10, 2));
        firstNameLabel = new JLabel();
        firstNameLabel.setText("First Name:");
        firstNameText = new JTextField();

        secondNameLabel = new JLabel();
        secondNameLabel.setText("Second Name :");
        secondNameText = new JTextField();

        userNameLabel = new JLabel();

        userNameLabel.setText("User Name :");
        userNameText = new JTextField();

        emailLabel = new JLabel();
        emailLabel.setText("Email :");
        emailText = new JTextField();

        addressLabel = new JLabel();
        addressLabel.setText("Address :");
        addressText = new JTextField();

        birthdateLabel = new JLabel();
        birthdateLabel.setText("Birth Date :");
        birthdateText = new JTextField();

        phoneLabel = new JLabel();
        phoneLabel.setText("Phone Number :");
        phoneText = new JTextField();

        passwordLabel = new JLabel();
        passwordLabel.setText("Password :");
        passwordText = new JPasswordField();

        pwRepeatLabel = new JLabel();
        pwRepeatLabel.setText("Repeat Password :");
        pwRepeatText = new JPasswordField();

        submit = new JButton("Signup");
        message = new JLabel();

        add(firstNameLabel);
        add(firstNameText);
        add(secondNameLabel);
        add(secondNameText);
        add(userNameLabel);
        add(userNameText);
        add(emailLabel);
        add(emailText);
        add(addressLabel);
        add(addressText);
        add(birthdateLabel);
        add(birthdateText);
        add(phoneLabel);
        add(phoneText);
        add(passwordLabel);
        add(passwordText);
        add(pwRepeatLabel);
        add(pwRepeatText);
        add(message);
        add(submit);
    }

    public String[] getInfo() {
        return new String [] {firstNameText.getText(), 
            secondNameText.getText(), userNameText.getText(), 
            emailText.getText(), addressText.getText(),
            birthdateText.getText(), phoneText.getText(),
            new String(passwordText.getPassword()),
            new String(pwRepeatText.getPassword())};
    }

    public void inform(String info) {message.setText(info);}
    
    public void addActionListener(ActionListener l) {
        firstNameText.addActionListener(l);
        secondNameText.addActionListener(l);
        userNameText.addActionListener(l);
        emailText.addActionListener(l);
        addressText.addActionListener(l);
        birthdateText.addActionListener(l);
        phoneText.addActionListener(l);
        passwordText.addActionListener(l);
        pwRepeatText.addActionListener(l);
        submit.addActionListener(l);
    }
}