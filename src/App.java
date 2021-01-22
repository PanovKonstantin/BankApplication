import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import java.util.HashMap;
import java.util.Map;

import java.awt.*;
import java.awt.event.*;

public class App extends JFrame implements ActionListener {
    LoginSignupTabbedPane loginSignupTP;
    HomeTabbedPane homeTP;
    ConnectionDatabase conn;
    AccountGenerator generator;


    App() {
        super("Bank Application");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        loginSignupTP = new LoginSignupTabbedPane();
        add(loginSignupTP);

        homeTP = new HomeTabbedPane();
        homeTP.setVisible(false);
        add(homeTP, BorderLayout.CENTER);

        setSize(600, 600);
        setResizable(false);
        setVisible(true);
    }

    public void loginAccount(int id) {
        loginSignupTP.setVisible(false);
        Map<String, String> clientData = conn.getClientData(id);
        System.out.println(clientData);
        homeTP.setVisible(true);
    }

    private class HomeTabbedPane extends JTabbedPane {
        HomeTabbedPane() {
            super();
            setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));

            JPanel home = new HomePanel();
            add("Home", home);

            JPanel info = new InfoPanel();
            add("Information", info);

            JPanel savings = new SavingsPanel();
            add("Savings", savings);
        }
    }

    private class HomePanel extends JPanel {
        JLabel balance;
        JButton transfer;
        JTable history;

        HomePanel() {
            super(new BorderLayout());
            balance = new JLabel();
            balance.setText("100000 PLN");

            transfer = new JButton("Make transfer");

            history = new JTable(new HistoryTableModel());
            JScrollPane scrollPane = new JScrollPane(history);
            history.setFillsViewportHeight(true);

            JPanel p = new JPanel(new GridLayout(1, 1));
            p.add(balance);
            p.add(transfer);
            add(p, BorderLayout.NORTH);
            add(scrollPane, BorderLayout.CENTER);

        }
    }

    private class InfoPanel extends JPanel implements ActionListener {
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
            String acc = "123", usrname = "admin", fname = "admean", sname = "adminowicz",
                    mail = "admin@idinahuj.psina", addr = "adres", bd = "66.66.6666", ph = "999-999-999";
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
            switch (e.getActionCommand()) {
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
                    if (isInformationCorrect(phone.getText())) {
                        accountid.setEditable(false);
                        username.setEditable(false);
                        email.setEditable(false);
                        address.setEditable(false);
                        birthdate.setEditable(false);
                        phone.setEditable(false);
                        savebutton.setEnabled(false);
                        info.setVisible(false);
                    } else {
                        info.setVisible(true);
                    }
                    break;
                default:
                    break;
            }
        }

        public boolean isInformationCorrect(String a) {
            if (a.length() == 11)
                return true;
            return false;
        }

    }

    class HistoryTableModel extends AbstractTableModel {

        String[] columnNames = { "Type", "From", "To", "Amount", "Date" };
        Object[][] data = { { "dasda", "asdas", "asdasd", 123213, "2112-21-41" },
                { "dasda", "asdas", "asdasd", 123213, "2112-21-41" },
                { "dasda", "asdas", "asdasd", 123213, "2112-21-41" },
                { "dasda", "asdas", "asdasd", 123213, "2112-21-41" },
                { "dasda", "asdas", "asdasd", 123213, "2112-21-41" }, { "vxvc", "xqerz", "wfda", 2222, "0000-00-00" } };

        @Override
        public int getColumnCount() {
            return columnNames.length;
        }

        @Override
        public int getRowCount() {
            return data.length;
        }

        @Override
        public String getColumnName(int col) {
            return columnNames[col];
        }

        public Object getValueAt(int row, int col) {
            return data[row][col];
        }

        @Override
        public Class getColumnClass(int c) {
            return getValueAt(0, c).getClass();
        }

        @Override
        public boolean isCellEditable(int row, int col) {
            return false;
        }
    }

    private class SavingsPanel extends JPanel implements ActionListener {
        JLabel amountLabel;
        JTextField amount;
        JButton transferTo;
        JButton transferFrom;
        JTable savings;

        SavingsPanel() {
            super(new BorderLayout());

            transferTo = new JButton("Transfer to selected account");
            transferFrom = new JButton("Transfer from selected account");
            amountLabel = new JLabel("Amount");
            amount = new JTextField();

            DefaultTableModel savingsTM = new DefaultTableModel();
            savingsTM.setDataVector(
                    new Object[][] { { "Konto 1", 123123, 1000, 3, new JRadioButton() },
                            { "Konto 2", 1123, 13000, 11, new JRadioButton() } },
                    new Object[] { "Name", "Number", "Balance", "Percent", "Select" });
            savings = new JTable(savingsTM) {
                public void tableChanged(TableModelEvent tme) {
                    super.tableChanged(tme);
                    repaint();
                }
            };
            ButtonGroup bg = new ButtonGroup();
            bg.add((JRadioButton) savingsTM.getValueAt(0, 4));
            bg.add((JRadioButton) savingsTM.getValueAt(1, 4));
            savings.getColumn("Select").setCellRenderer(new RadioButtonRenderer());
            savings.getColumn("Select").setCellEditor(new RadioButtonEditor(new JCheckBox()));
            JScrollPane sp = new JScrollPane(savings);
            add(sp, BorderLayout.CENTER);
            transferTo.addActionListener(this);
            transferFrom.addActionListener(this);

            JPanel p = new JPanel(new GridLayout(2, 2));
            p.add(amountLabel);
            p.add(amount);
            p.add(transferTo);
            p.add(transferFrom);
            add(p, BorderLayout.NORTH);

        }

        public void actionPerformed(ActionEvent e) {
            switch (e.getActionCommand()) {
                case "Transfer to selected account":
                    break;
                case "Transfer from selected account":
                    break;
                default:
                    break;
            }
        }

    }

    private class LoginSignupTabbedPane extends JTabbedPane {
        LoginSignupTabbedPane() {
            super();
            setBounds(50, 50, 200, 200);
            setSize(500, 500);

            JPanel loginPanel = new LoginPanel();
            JPanel lp = new JPanel();
            lp.add(loginPanel);
            add("Login", lp);

            JPanel signupPanel = new SignupPanel();
            JPanel sp = new JPanel();
            sp.add(signupPanel);
            add("Signup", sp);
        }
    }

    private class LoginPanel extends JPanel implements ActionListener {

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

            submit = new JButton("SUBMIT");
            message = new JLabel();

            add(userLabel);
            add(userNameText);
            add(passwordLabel);
            add(passwordText);

            add(message);
            add(submit);
            userNameText.addActionListener(this);
            passwordText.addActionListener(this);
            submit.addActionListener(this);
        }

        public void actionPerformed(ActionEvent e) {
            String userName = userNameText.getText();
            String password = new String(passwordText.getPassword());

            int id = conn.loginUser(userName, password);
            if (id != -1) {
                message.setText(" Hello " + userName + "");
                loginAccount(id);
            } else {
                message.setText(" Invalid user.. ");
            }
        }
    }

    private class SignupPanel extends JPanel implements ActionListener {
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

            submit = new JButton("SUBMIT");
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
            firstNameText.addActionListener(this);
            secondNameText.addActionListener(this);
            userNameText.addActionListener(this);
            emailText.addActionListener(this);
            addressText.addActionListener(this);
            birthdateText.addActionListener(this);
            phoneText.addActionListener(this);
            passwordText.addActionListener(this);
            pwRepeatText.addActionListener(this);
            submit.addActionListener(this);
        }

        public void actionPerformed(ActionEvent e) {
            String firstName = firstNameText.getText();
            String secondName = firstNameText.getText();
            String username = userNameText.getText();
            String email = emailText.getText();
            String address = addressText.getText();
            String birthdate = birthdateText.getText();
            String phone = phoneText.getText();
            String password = new String(passwordText.getPassword());
            String pwRepeat = new String(pwRepeatText.getPassword());

            int id = conn.addClient(firstName, secondName, username, email, address, birthdate, phone, password,
                    pwRepeat);
            if (id != -1) {
                loginAccount(id);
            }

        }
    }

    public void actionPerformed(ActionEvent e) {
    }

    public static void main(String[] args) {
        App app = new App();
        app.conn = new ConnectionDatabase();
        app.generator = new AccountGenerator();
        // app.generateAccounts(5);
    }

    public void generateAccounts(int number) {
        String[] accounts = new String[number];
        for (int i = 0; i < number; i++) {
            accounts[i] = generator.generateAccount();
        }
        conn.addAccounts(accounts);
    }

}

class RadioButtonRenderer implements TableCellRenderer {
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
            int row, int column) {
        if (value == null)
            return null;
        return (Component) value;
    }
}

class RadioButtonEditor extends DefaultCellEditor implements ItemListener {
    private JRadioButton button;

    public RadioButtonEditor(JCheckBox checkBox) {
        super(checkBox);
    }

    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        if (value == null)
            return null;
        button = (JRadioButton) value;
        button.addItemListener(this);
        return (Component) value;
    }

    public Object getCellEditorValue() {
        button.removeItemListener(this);
        return button;
    }

    public void itemStateChanged(ItemEvent e) {
        super.fireEditingStopped();
    }
}