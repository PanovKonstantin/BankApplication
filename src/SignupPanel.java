import javax.swing.*;

import java.awt.*;
import java.awt.event.*;

public class SignupPanel extends JPanel {
    static final long serialVersionUID = 42L;
    JTextField firstNameText;
    JTextField secondNameText;
    JTextField userNameText;
    JTextField emailText;
    JTextField countryText;
    JTextField cityText;
    JTextField streetText;
    JTextField homeText;
    JTextField apartmentText;
    JTextField postCodeText;
    JTextField birthdateText;
    JTextField phoneText;
    JPasswordField passwordText;
    JPasswordField pwRepeatText;
    JLabel firstNameLabel;
    JLabel secondNameLabel;
    JLabel userNameLabel;
    JLabel emailLabel;
    JLabel countryLabel;
    JLabel cityLabel;
    JLabel streetLabel;
    JLabel homeLabel;
    JLabel apartmentLabel;
    JLabel postCodeLabel;
    JLabel birthdateLabel;
    JLabel phoneLabel;
    JLabel passwordLabel;
    JLabel pwRepeatLabel;
    JLabel message;
    JButton submit;

    SignupPanel() {
        super(new GridLayout(15, 2));
        firstNameLabel = new JLabel();
        firstNameLabel.setText("First Name:");
        firstNameText = new JTextField("");

        secondNameLabel = new JLabel();
        secondNameLabel.setText("Second Name :");
        secondNameText = new JTextField("");

        userNameLabel = new JLabel();

        userNameLabel.setText("User Name :");
        userNameText = new JTextField("");

        emailLabel = new JLabel();
        emailLabel.setText("Email :");
        emailText = new JTextField("");

        countryLabel = new JLabel();
        countryLabel.setText("Country :");
        countryText = new JTextField("");

        cityLabel = new JLabel();
        cityLabel.setText("City :");
        cityText = new JTextField("");

        streetLabel = new JLabel();
        streetLabel.setText("Street :");
        streetText = new JTextField("");

        homeLabel = new JLabel();
        homeLabel.setText("Home (Only digits) :");
        homeText = new JTextField("");

        apartmentLabel = new JLabel();
        apartmentLabel.setText("Apartment :");
        apartmentText = new JTextField("");

        postCodeLabel = new JLabel();
        postCodeLabel.setText("Post code :");
        postCodeText = new JTextField("");

        birthdateLabel = new JLabel();
        birthdateLabel.setText("Birth Date (DD/MM/YYYY):");
        birthdateText = new JTextField("");

        phoneLabel = new JLabel();
        phoneLabel.setText("Phone Number (9 digits):");
        phoneText = new JTextField("");

        passwordLabel = new JLabel();
        passwordLabel.setText("Password :");
        passwordText = new JPasswordField("");

        pwRepeatLabel = new JLabel();
        pwRepeatLabel.setText("Repeat Password :");
        pwRepeatText = new JPasswordField("");

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
        add(countryLabel);
        add(countryText);
        add(cityLabel);
        add(cityText);
        add(streetLabel);
        add(streetText);
        add(homeLabel);
        add(homeText);
        add(apartmentLabel);
        add(apartmentText);
        add(postCodeLabel);
        add(postCodeText);
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
        return new String[] { firstNameText.getText(), secondNameText.getText(), userNameText.getText(),
                emailText.getText(), birthdateText.getText(), phoneText.getText(),
                new String(passwordText.getPassword()), new String(pwRepeatText.getPassword()), countryText.getText(),
                cityText.getText(), streetText.getText(), homeText.getText(), apartmentText.getText(),
                postCodeText.getText() };
    }

    public void inform(String info) {
        message.setText(info);
    }

    public void addActionListener(ActionListener l) {
        firstNameText.addActionListener(l);
        secondNameText.addActionListener(l);
        userNameText.addActionListener(l);
        emailText.addActionListener(l);
        countryText.addActionListener(l);
        cityText.addActionListener(l);
        streetText.addActionListener(l);
        homeText.addActionListener(l);
        apartmentText.addActionListener(l);
        postCodeText.addActionListener(l);
        birthdateText.addActionListener(l);
        phoneText.addActionListener(l);
        passwordText.addActionListener(l);
        pwRepeatText.addActionListener(l);
        submit.addActionListener(l);
    }

    public void clear() {
        firstNameText.setText("");
        secondNameText.setText("");
        userNameText.setText("");
        emailText.setText("");
        countryText.setText("");
        cityText.setText("");
        streetText.setText("");
        homeText.setText("");
        apartmentText.setText("");
        postCodeText.setText("");
        birthdateText.setText("");
        phoneText.setText("");
        passwordText.setText("");
        pwRepeatText.setText("");
        submit.setText("");
    }
}