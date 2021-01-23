import javax.swing.*;

import java.awt.*;
import java.awt.event.*;

public class InfoPanel extends JPanel implements ActionListener{
    JTextField accountid;
    JTextField username;
    JTextField email;
    JTextField address;
    JTextField birthdate;
    JTextField phone;
    JTextField name;
    JTextField surname;
    JLabel accountidLabel;
    JLabel usernameLabel;
    JLabel nameLabel;
    JLabel surnameLabel;
    JLabel emailLabel;
    JLabel addressLabel;
    JLabel birthdateLabel;
    JLabel phoneLabel;
    JButton editbutton;
    JButton savebutton;
    JLabel info;

    InfoPanel() {
        super(new GridLayout(10, 2));
        String acc = "123";
        String usrname = "admin";
        String fname = "admean";
        String sname = "adminowicz";
        String mail = "admin@idinahuj.psina";
        String addr = "adres";
        String bd = "66.66.6666";
        String ph = "999-999-999";
        accountid = new JTextField(acc);
        accountid.setEditable(false);
        username = new JTextField(usrname);
        username.setEditable(false);
        name = new JTextField(fname);
        name.setEditable(false);
        surname = new JTextField(sname);
        surname.setEditable(false);
        email = new JTextField(mail);
        email.setEditable(false);
        address = new JTextField(addr);
        address.setEditable(false);
        birthdate = new JTextField(bd);
        birthdate.setEditable(false);
        phone = new JTextField(ph);
        phone.setEditable(false);
        accountidLabel = new JLabel();
        accountidLabel.setText("Account ID:");
        usernameLabel = new JLabel();
        usernameLabel.setText("Username:");
        nameLabel = new JLabel();
        nameLabel.setText("First name:");
        surnameLabel = new JLabel();
        surnameLabel.setText("Second name:");
        emailLabel = new JLabel();
        emailLabel.setText("Email:");
        addressLabel = new JLabel();
        addressLabel.setText("Address:");
        birthdateLabel = new JLabel();
        birthdateLabel.setText("Birthdate:");
        phoneLabel = new JLabel();
        phoneLabel.setText("Phone number:");
        info = new JLabel("Information is not correct.");
        info.setVisible(false);

        editbutton = new JButton("Edit");
        savebutton = new JButton("Save");
        savebutton.setEnabled(false);
        editbutton.addActionListener(this);
        savebutton.addActionListener(this);

        add(accountidLabel);
        add(accountid);
        add(usernameLabel);
        add(username);
        add(nameLabel);
        add(name);
        add(surnameLabel);
        add(surname);
        add(emailLabel);
        add(email);
        add(addressLabel);
        add(address);
        add(birthdateLabel);
        add(birthdate);
        add(phoneLabel);
        add(phone);
        add(editbutton);
        add(savebutton);
        add(info);
    }
    public void actionPerformed(ActionEvent e) {
        switch(e.getActionCommand()){
            case "Edit":
                accountid.setEditable(true);
                username.setEditable(true);
                email.setEditable(true);
                address.setEditable(true);
                birthdate.setEditable(true);
                phone.setEditable(true);
                savebutton.setEnabled(true);
                break;
            case "Save":
                if(isInformationCorrect(phone.getText())){
                    accountid.setEditable(false);
                    username.setEditable(false);
                    email.setEditable(false);
                    address.setEditable(false);
                    birthdate.setEditable(false);
                    phone.setEditable(false);
                    savebutton.setEnabled(false);
                    info.setVisible(false);
                }
                else{
                    info.setVisible(true);
                }
                break;
            default:
                break;
        } 
    }
    
    public boolean isInformationCorrect(String a){return a.length() == 11;}

}
