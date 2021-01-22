import java.sql.*;
import java.util.Random;
import java.util.HashMap;
import java.util.Map;

public class ConnectionDatabase {

    private final String URL = "jdbc:oracle:thin:@ora4.ii.pw.edu.pl:1521/pdb1.ii.pw.edu.pl";
    private final String login = "BD1_Z09";
    private final String password = "7fncmp";
    private Connection conn;

    public void openConnection() {
        try {
            conn = DriverManager.getConnection(URL, login, password);
        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void closeConnection() {
        try {
            conn.close();
        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int addClient(String firstName, String lastName, String username, String email, String address,
            String birthdate, String phone, String password, String pwRepeat) {
        if (!password.equals(pwRepeat))
            return -1;
        openConnection();
        try {
            Statement statement = conn.createStatement();
            String id = "";
            String bankAccount = "";
            String savingBankAccount = "";
            Random rand = new Random();
            ResultSet rs = statement.executeQuery("SELECT SEQ_USERS.NEXTVAL FROM DUAL");
            while (rs.next()) {
                id = rs.getString(1); // getting client id
            }
            rs = statement.executeQuery("SELECT BANK_ACCOUNT, IN_USE FROM ALL_ACCOUNTS WHERE IN_USE=0");
            while (rs.next()) {
                bankAccount = rs.getString("BANK_ACCOUNT"); // getting bank account
                break;
            }
            while (rs.next()) {
                savingBankAccount = rs.getString("BANK_ACCOUNT"); // getting saving bank account
                break;
            }
            statement.executeUpdate("UPDATE ALL_ACCOUNTS SET IN_USE = 1 WHERE BANK_ACCOUNT=" + bankAccount);
            statement.executeUpdate("UPDATE ALL_ACCOUNTS SET IN_USE = 1 WHERE BANK_ACCOUNT=" + savingBankAccount);

            statement.executeUpdate("INSERT INTO CLIENTS VALUES('" + id + "','" + firstName + "','" + lastName + "','"
                    + birthdate + "','" + phone + "','" + email + "')");
            statement.executeUpdate("INSERT INTO BANK_ACCOUNTS VALUES('" + id + "','" + bankAccount + "','"
                    + Integer.toString(rand.nextInt(5000)) + "')");
            statement.executeUpdate("INSERT INTO SAVING_BANK_ACCOUNTS VALUES('" + id + "','" + savingBankAccount + "','"
                    + Integer.toString(rand.nextInt(50000)) + "')");
            statement.executeUpdate("INSERT INTO USERS VALUES('" + id + "','" + username + "','" + password + "')");
            closeConnection();
            return Integer.parseInt(id);
        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        closeConnection();
        return -1;
    }

    public int loginUser(String username, String password) {
        openConnection();
        try (Statement statement = conn.createStatement()) {
            ResultSet rs = statement.executeQuery("SELECT * FROM USERS");
            while (rs.next()) {
                int id = rs.getInt("ID");
                String table_username = rs.getString("USERNAME");
                String table_password = rs.getString("PASSWORD");
                if (username.equals(table_username) && password.equals(table_password)) {
                    closeConnection();
                    return id;
                }
            }
        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        closeConnection();
        return -1;
    }

    public boolean addAccounts(String[] accounts) {
        openConnection();
        try {
            Statement statement = conn.createStatement();
            for (int i = 0; i < accounts.length; i++) {
                statement.executeUpdate("INSERT INTO ALL_ACCOUNTS VALUES('" + accounts[i] + "','0')");
            }
            closeConnection();
            return true;
        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        closeConnection();
        return false;
    }

    public Map getClientData(int id) {
        Map<String, String> clientData = new HashMap<String, String>();
        String clientID = Integer.toString(id);
        clientData.put("ID", clientID);
        openConnection();
        try {
            Statement statement = conn.createStatement();
            String firstName = "", secondName = "", birthDate = "", phoneNumber = "", email = "", bankAccount = "",
                    savingBankAccount = "", bankAccountFunds = "", savingBankAccountFunds = "";
            ResultSet rs = statement.executeQuery("select * from clients where id=" + clientID);
            while (rs.next()) {
                firstName = rs.getString("FIRST_NAME");
                secondName = rs.getString("LAST_NAME");
                birthDate = rs.getString("BIRTH_DATE");
                phoneNumber = rs.getString("PHONE_NUMBER");
                email = rs.getString("EMAIL");
            }
            clientData.put("FIRST_NAME", firstName);
            clientData.put("LAST_NAME", secondName);
            clientData.put("BIRTH_DATE", birthDate);
            clientData.put("PHONE_NUMBER", phoneNumber);
            clientData.put("EMAIL", email);

            rs = statement.executeQuery("SELECT * FROM BANK_ACCOUNTS WHERE ID=" + clientID);
            while (rs.next()) {
                bankAccount = rs.getString("BANK_ACCOUNT");
                bankAccountFunds = rs.getString("AVAILABLE_FUNDS");
            }
            clientData.put("BANK_ACCOUNT", bankAccount);
            clientData.put("BANK_ACCOUNT_FUNDS", bankAccountFunds);

            rs = statement.executeQuery("SELECT * FROM SAVING_BANK_ACCOUNTS WHERE ID=" + clientID);
            while (rs.next()) {
                savingBankAccount = rs.getString("BANK_ACCOUNT");
                savingBankAccountFunds = rs.getString("AVAILABLE_FUNDS");
            }
            clientData.put("SAVING_BANK_ACCOUNT", savingBankAccount);
            clientData.put("SAVING_BANK_ACCOUNT_FUNDS", savingBankAccountFunds);

        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        closeConnection();
        return clientData;
    }

}
